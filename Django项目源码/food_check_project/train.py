# -*- coding:utf-8 -*-
import pickle
import torch
import torch.nn as nn
from torch.optim import Adam, lr_scheduler
from dataset import get_data_loaders
from models.resnet import ResNet50Model
from models.vgg import VGG16Model
import argparse
import time


def train_model(model, train_loader, val_loader, num_epochs=20, lr=1e-4):
    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
    model.to(device)
    criterion = nn.CrossEntropyLoss()
    optimizer = Adam(model.parameters(), lr=lr)
    scheduler = lr_scheduler.StepLR(optimizer, step_size=5, gamma=0.5)

    best_val_acc = 0.0
    best_model_wts = model.state_dict().copy()
    start_time = time.time()

    for epoch in range(num_epochs):
        print(f'Epoch {epoch + 1}/{num_epochs}')
        print('-' * 10)

        # 训练阶段
        model.train()
        running_loss = 0.0
        running_corrects = 0

        for inputs, labels in train_loader:
            inputs = inputs.to(device)
            labels = labels.to(device)

            optimizer.zero_grad()
            outputs = model(inputs)
            _, preds = torch.max(outputs, 1)
            loss = criterion(outputs, labels)

            loss.backward()
            optimizer.step()

            running_loss += loss.item() * inputs.size(0)
            running_corrects += torch.sum(preds == labels.data)

        scheduler.step()

        # 验证阶段
        model.eval()
        val_running_loss = 0.0
        val_running_corrects = 0

        with torch.no_grad():
            for inputs, labels in val_loader:
                inputs = inputs.to(device)
                labels = labels.to(device)

                outputs = model(inputs)
                _, preds = torch.max(outputs, 1)
                loss = criterion(outputs, labels)

                val_running_loss += loss.item() * inputs.size(0)
                val_running_corrects += torch.sum(preds == labels.data)

        # 计算指标
        epoch_loss = running_loss / len(train_loader.dataset)
        epoch_acc = running_corrects.float() / len(train_loader.dataset)
        val_epoch_loss = val_running_loss / len(val_loader.dataset)
        val_epoch_acc = val_running_corrects.float() / len(val_loader.dataset)

        print(f'Train Loss: {epoch_loss:.4f} Acc: {epoch_acc:.4f}')
        print(f'Val Loss: {val_epoch_loss:.4f} Acc: {val_epoch_acc:.4f}')

        # 保存最佳模型
        if val_epoch_acc > best_val_acc:
            best_val_acc = val_epoch_acc
            best_model_wts = model.state_dict().copy()
            torch.save(best_model_wts, './logs/best_model.pth')
            print("模型已更新")

    print(f'\n训练完成，耗时: {time.time() - start_time:.0f}s')
    print(f'最佳验证准确率: {best_val_acc:.4f}')
    model.load_state_dict(best_model_wts)
    return model


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='菜品识别模型训练')
    parser.add_argument('--model', type=str, choices=['vgg16', 'resnet50'], default='resnet50', help='选择基础模型')
    parser.add_argument('--train_dir', type=str, default='./data/train', help='训练数据路径')
    parser.add_argument('--val_dir', type=str, default='./data/val', help='验证数据路径')
    parser.add_argument('--batch_size', type=int, default=32, help='批量大小')
    parser.add_argument('--epochs', type=int, default=20, help='训练轮数')
    parser.add_argument('--lr', type=float, default=1e-4, help='学习率')
    args = parser.parse_args()

    # 加载数据
    train_loader, val_loader, _, class_to_idx = get_data_loaders(
        train_dir=args.train_dir,
        val_dir=args.val_dir,
        test_dir='',
        batch_size=args.batch_size
    )

    # 初始化模型
    if args.model == 'vgg16':
        model = VGG16Model(num_classes=len(class_to_idx))
    else:
        model = ResNet50Model(num_classes=len(class_to_idx))

    # 开始训练
    train_model(model, train_loader, val_loader, args.epochs, args.lr)

    # 训练结束后
    with open("./class_to_idx.pkl", "wb") as f:
        pickle.dump(class_to_idx, f)