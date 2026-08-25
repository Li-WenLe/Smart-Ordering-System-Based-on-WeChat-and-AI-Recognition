# -*- coding:utf-8 -*-
import cv2
import numpy as np
from PIL import Image, ImageDraw, ImageFont
import torchvision
import torch
from time import time
import pickle
from predict import load_model, predict_maturity
from dataset import get_data_loaders

# 配置模型参数（与原服务端一致）
UDP_PORT = 9090  # 保留原配置（虽不使用网络，但需兼容函数参数）
BUFFER_SIZE = 100000

# 加载模型和类别映射（完全复用原服务端逻辑）
model = load_model(model_path="logs/best_model.pth", model_type="resnet50", num_classes=len(pickle.load(open("./class_to_idx.pkl", "rb"))))
_, _, _, class_to_idx = get_data_loaders(
    train_dir="data/train",
    val_dir="data/val",
    test_dir="",
    batch_size=128
)


def preprocess_frame(frame):
    """预处理图像（完全复用原服务端逻辑）"""
    # 假设输入 frame 是 RGB 格式（PIL 读取的 numpy 数组为 RGB）
    img = cv2.resize(frame, (224, 224))
    transform = torchvision.transforms.Compose([
        torchvision.transforms.ToTensor(),
        torchvision.transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
    ])
    return transform(img).unsqueeze(0)  # 添加 batch 维度


def detect_local_image(image_path):
    """直接检测本地图片"""
    try:
        since = time()
        # 读取图片（使用 PIL 确保正确处理各种格式）
        image = Image.open(image_path)
        frame = np.asarray(image)  # PIL 转 numpy 数组（RGB 格式）

        # 预处理
        input_tensor = preprocess_frame(frame)

        # 预测
        class_name, probability = predict_maturity(input_tensor, model, class_to_idx)

        # 显示结果
        img_show = cv2.cvtColor(frame, cv2.COLOR_RGB2BGR)  # 转为 BGR 显示
        img_show = cv2.resize(img_show, (600, 600))
        end_predict = time()
        fps = round(1 / (end_predict - since))

        # 使用 PIL 绘制中文
        img_pil = Image.fromarray(img_show)
        draw = ImageDraw.Draw(img_pil)
        font = ImageFont.truetype("simhei.ttf", 20)  # 请确保 simhei.ttf 字体文件存在

        draw.text((10, 30), f"菜品: {class_name}", font=font, fill=(0, 0, 255))
        draw.text((10, 60), f"概率: {probability:.2f}", font=font, fill=(0, 255, 0))
        draw.text((10, 90), f"帧率: {fps}", font=font, fill=(255, 0, 0))

        img_show = np.array(img_pil)

        cv2.imshow("Local Image Detection", img_show)
        cv2.waitKey(0)  # 按任意键关闭窗口
        cv2.destroyAllWindows()

    except FileNotFoundError:
        print(f"错误：未找到图片文件 {image_path}")
    except Exception as e:
        print(f"检测失败：{str(e)}")


if __name__ == "__main__":
    # 待检测的本地图片路径（可替换为你的图片路径）
    IMAGE_PATH = "./img.png"
    detect_local_image(IMAGE_PATH)