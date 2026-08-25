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
import numpy as np
from sklearn.metrics import precision_recall_curve


def train_model(model, train_loader, val_loader, num_epochs=20, lr=1e-4):
    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
    model.to(device)
    criterion = nn.CrossEntropyLoss()
    optimizer = Adam(model.parameters(), lr=lr)
    scheduler = lr_scheduler.StepLR(optimizer, step_size=5, gamma=0.5)

    best_val_acc = 0.0
    best_model_wts = model.state_dict().copy()
    start_time = time.time()

    # 用于记录训练特征
    all_features = []

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

            # 记录特征
            features = model.get_features(inputs)  # 假设模型有 get_features 方法返回特征
            all_features.extend(features.cpu().detach().numpy())

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

    # 计算训练特征的均值和标准差
    all_features = np.array(all_features)
    feature_mean = np.mean(all_features, axis=0)
    feature_std = np.std(all_features, axis=0)

    # 保存特征统计信息
    np.save('./logs/feature_mean.npy', feature_mean)
    np.save('./logs/feature_std.npy', feature_std)

    # 在验证集上确定阈值
    val_features = []
    val_labels = []
    model.eval()
    with torch.no_grad():
        for inputs, labels in val_loader:
            inputs = inputs.to(device)
            labels = labels.to(device)
            features = model.get_features(inputs).cpu().detach().numpy()
            val_features.extend(features)
            val_labels.extend(labels.cpu().detach().numpy())

    val_features = np.array(val_features)
    val_labels = np.array(val_labels)

    val_scores = []
    for feature in val_features:
        z_scores = np.abs((feature - feature_mean) / feature_std)
        score = np.max(z_scores)
        val_scores.append(score)

    precision, recall, thresholds = precision_recall_curve(val_labels, val_scores)

    # 选择一个合适的阈值，例如在精确率和召回率之间取得平衡
    threshold_index = np.argmax(precision + recall)
    threshold = thresholds[threshold_index]

    # 保存阈值
    np.save('./logs/threshold.npy', threshold)

    return model


def detect_anomaly(model, input_image, threshold):
    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
    input_image = input_image.unsqueeze(0).to(device)

    # 加载特征统计信息
    feature_mean = np.load('./logs/feature_mean.npy')
    feature_std = np.load('./logs/feature_std.npy')

    # 获取输入图像的特征
    features = model.get_features(input_image).cpu().detach().numpy()[0]

    # 计算 z-score
    z_scores = np.abs((features - feature_mean) / feature_std)

    # 检查是否有 z-score 超过阈值
    if np.any(z_scores > threshold):
        return True  # 异常，判定为未知菜
    return False


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

    # 读取阈值
    try:
        threshold = np.load('./logs/threshold.npy')
        print(f"加载阈值: {threshold}")
    except FileNotFoundError:
        print("未找到阈值文件，使用默认阈值 3.0")
        threshold = 3.0

    # 这里可以添加使用阈值进行异常检测的逻辑，例如在验证集上进行检测
    model.eval()
    anomaly_count = 0
    total_count = 0
    with torch.no_grad():
        for inputs, labels in val_loader:
            for input_image in inputs:
                is_anomaly = detect_anomaly(model, input_image, threshold)
                if is_anomaly:
                    anomaly_count += 1
                total_count += 1

    print(f"验证集中检测到的异常样本数量: {anomaly_count} / {total_count}")

    # 训练结束后
    with open("./class_to_idx.pkl", "wb") as f:
        pickle.dump(class_to_idx, f)
