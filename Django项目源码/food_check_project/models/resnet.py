# -*- coding:utf-8 -*-
import torch
import torch.nn as nn
import torchvision.models as models
from torchvision.models import ResNet50_Weights, resnet50


class ResNet50Model(nn.Module):
    def __init__(self, num_classes):
        super(ResNet50Model, self).__init__()
        self.resnet = resnet50(weights=models.ResNet50_Weights.DEFAULT)
        num_ftrs = self.resnet.fc.in_features
        self.resnet.fc = nn.Linear(num_ftrs, num_classes)
        # 旧代码（导致警告）
        # self.base_model = models.resnet50(pretrained=True)

        # 修正后（PyTorch 0.13+ 推荐写法）
        self.base_model = models.resnet50(weights=models.ResNet50_Weights.DEFAULT)

        in_features = self.base_model.fc.in_features
        self.base_model.fc = nn.Linear(in_features, num_classes)

    def forward(self, x):
        return self.base_model(x)

    def get_features(self, x):
        # 提取特征，这里取最后一个卷积层的输出
        x = self.resnet.conv1(x)
        x = self.resnet.bn1(x)
        x = self.resnet.relu(x)
        x = self.resnet.maxpool(x)

        x = self.resnet.layer1(x)
        x = self.resnet.layer2(x)
        x = self.resnet.layer3(x)
        x = self.resnet.layer4(x)

        # 全局平均池化
        x = self.resnet.avgpool(x)
        x = torch.flatten(x, 1)
        return x


class ResNet18Model(nn.Module):
    def __init__(self, num_classes):
        super(ResNet18Model, self).__init__()
        self.base_model = models.resnet50(weights=ResNet50_Weights.DEFAULT)
        in_features = self.base_model.fc.in_features
        self.base_model.fc = nn.Linear(in_features, num_classes)

    def forward(self, x):
        return self.base_model(x)