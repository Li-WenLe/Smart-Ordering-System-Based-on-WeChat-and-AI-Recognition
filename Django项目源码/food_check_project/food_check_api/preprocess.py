# -*- coding:utf-8 -*-
# -*- coding:utf-8 -*-
import cv2
import torch
from torchvision import transforms

def preprocess_image(img_array):  # 接收 numpy 数组（BGR 格式）
    """预处理内存中的图像数组（BGR 转 RGB，调整尺寸，标准化）"""
    img = cv2.cvtColor(img_array, cv2.COLOR_BGR2RGB)  # 转为 RGB
    img = cv2.resize(img, (224, 224))  # 调整尺寸
    transform = transforms.Compose([
        transforms.ToTensor(),
        transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
    ])
    return transform(img).unsqueeze(0)  # 添加 batch 维度