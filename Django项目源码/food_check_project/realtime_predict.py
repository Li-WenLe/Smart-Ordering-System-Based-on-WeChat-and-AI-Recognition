# -*- coding:utf-8 -*-
import cv2
import numpy as np
import torchvision
from PIL import Image
import io
import torch
from time import time
from socket import socket, AF_INET, SOCK_DGRAM
from predict import load_model, predict_maturity
from dataset import get_data_loaders

# 配置 ESP32 摄像头连接参数
UDP_PORT = 9090
BUFFER_SIZE = 200000  # 增大缓冲区，避免 UDP 数据截断（根据实际图片大小调整）

# 加载模型和类别映射
model = load_model(model_path="logs/best_model.pth", model_type="resnet50", num_classes=3)
_, _, _, class_to_idx = get_data_loaders(
    train_dir="data/train",  # 任意有效路径，仅用于获取 class_to_idx
    val_dir="data/val",
    test_dir="",
    batch_size=128
)

# 创建 UDP 接收 socket
s = socket(AF_INET, SOCK_DGRAM)
s.bind(("0.0.0.0", UDP_PORT))


def preprocess_frame(frame):
    """预处理图像（适配 ESP32 发送的 RGB 格式）"""
    # 调整尺寸（保持原始 RGB 通道顺序，模型输入需要 RGB）
    img = cv2.resize(frame, (224, 224))

    # 标准化（与训练时一致，直接处理 RGB 输入）
    transform = torchvision.transforms.Compose([
        torchvision.transforms.ToTensor(),
        torchvision.transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
    ])
    return transform(img).unsqueeze(0)  # 添加 batch 维度


while True:
    try:
        data, _ = s.recvfrom(BUFFER_SIZE)  # 接收图像数据（增大缓冲区）
        since = time()

        # 将字节流转换为 OpenCV 可显示的 BGR 格式图像
        bytes_stream = io.BytesIO(data)
        image = Image.open(bytes_stream)

        # 强制转换为 RGB 模式（确保三通道，避免灰度图）
        if image.mode != "RGB":
            image = image.convert("RGB")

        frame = np.asarray(image)  # RGB 格式的 numpy 数组（形状：[H, W, 3]）

        # 【关键修改】显示前将 RGB 转为 BGR（OpenCV 要求 BGR 格式）
        img_show = cv2.cvtColor(frame, cv2.COLOR_RGB2BGR)
        img_show = cv2.resize(img_show, (600, 600))  # 调整显示尺寸

        # 预处理图像（模型需要 RGB，直接使用原始 frame 无需额外转换）
        input_tensor = preprocess_frame(frame)

        # 实时预测
        class_name, probability = predict_maturity(input_tensor, model, class_to_idx)

        # 显示结果和性能信息
        end_predict = time()
        fps = round(1 / (end_predict - since))
        font = cv2.FONT_HERSHEY_SIMPLEX

        cv2.putText(img_show, f"Maturity: {class_name}", (10, 30), font, 1, (0, 0, 255), 2)
        cv2.putText(img_show, f"Probability: {probability:.2f}", (10, 60), font, 1, (0, 255, 0), 2)
        cv2.putText(img_show, f"FPS: {fps}", (10, 90), font, 1, (255, 0, 0), 2)

        cv2.imshow("Real-time Maturity Prediction", img_show)
        if cv2.waitKey(1) & 0xFF == 27:  # 按 ESC 退出
            break

    except Exception as e:
        print(f"Error processing frame: {e}")
        break

# 资源释放
s.close()
cv2.destroyAllWindows()