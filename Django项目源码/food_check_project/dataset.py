# -*- coding:utf-8 -*-
from torchvision import datasets, transforms
import torch.utils.data as data


def get_data_loaders(train_dir, val_dir, test_dir, batch_size):
    # 数据增强（彩色图像处理）
    train_transform = transforms.Compose([
        transforms.RandomResizedCrop(224),
        transforms.RandomHorizontalFlip(),
        transforms.ColorJitter(brightness=0.2, contrast=0.2, saturation=0.2),
        transforms.ToTensor(),
        transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
    ])

    val_test_transform = transforms.Compose([
        transforms.Resize((224, 224)),
        transforms.ToTensor(),
        transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
    ])

    # 加载数据集
    train_dataset = datasets.ImageFolder(train_dir, transform=train_transform)
    val_dataset = datasets.ImageFolder(val_dir, transform=val_test_transform)
    test_dataset = datasets.ImageFolder(test_dir, transform=val_test_transform) if test_dir else None

    # 创建数据加载器
    train_loader = data.DataLoader(train_dataset, batch_size=batch_size, shuffle=True, num_workers=4)
    val_loader = data.DataLoader(val_dataset, batch_size=batch_size, shuffle=False, num_workers=4)
    test_loader = data.DataLoader(test_dataset, batch_size=batch_size, shuffle=False,
                                  num_workers=4) if test_dir else None

    return train_loader, val_loader, test_loader, train_dataset.class_to_idx