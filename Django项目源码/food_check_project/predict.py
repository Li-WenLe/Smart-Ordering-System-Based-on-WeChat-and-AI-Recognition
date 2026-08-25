# -*- coding:utf-8 -*-
import torch

from models.vgg import VGG16Model
from models.resnet import ResNet50Model

def load_model(model_path, model_type='resnet50', num_classes=3):
    """加载训练好的模型"""
    global model
    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
    if model_type == 'resnet50':
        model = ResNet50Model(num_classes=num_classes)
    elif model_type == 'vgg16':
        model = VGG16Model(num_classes=num_classes)
    model.load_state_dict(torch.load(model_path, map_location=device))
    model.to(device)
    model.eval()
    return model

def predict_maturity(input_tensor, model, class_to_idx):
    """预测图像张量的成熟度（输入为带 batch 维度的 tensor）"""
    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
    with torch.no_grad():
        outputs = model(input_tensor.to(device))
        probabilities = torch.softmax(outputs, dim=1).squeeze().cpu().numpy()
        class_idx = torch.argmax(outputs, dim=1).item()
        class_name = {v: k for k, v in class_to_idx.items()}[class_idx]
    return class_name, probabilities[class_idx]
