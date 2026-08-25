# -*- coding:utf-8 -*-
import torch
import torch.nn as nn
import torchvision.models as models
from torchvision.models import VGG16_Weights  # 添加导入

class VGG16Model(nn.Module):
    def __init__(self, num_classes):
        super(VGG16Model, self).__init__()
        # 加载预训练VGG16

        # 推荐写法（PyTorch 0.13+）
        self.base_model = models.vgg16(weights=VGG16_Weights.DEFAULT)
        # 修改输入通道为3（默认即为3通道，无需修改）
        # 修改分类层
        in_features = self.base_model.classifier[6].in_features
        self.base_model.classifier[6] = nn.Linear(in_features, num_classes)

    def forward(self, x):
        return self.base_model(x)