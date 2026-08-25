import os
import sys
import requests
import cv2
import numpy as np
import json
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt

from dataset import get_data_loaders
from predict import load_model, predict_maturity
from preprocess import preprocess_image

# 获取项目根目录
project_root = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
# 将项目根目录添加到 sys.path
sys.path.append(project_root)

# 加载模型和类别映射
_, _, _, class_to_idx = get_data_loaders(
    train_dir="data/train",
    val_dir="data/val",
    test_dir="",
    batch_size=128
)
model = load_model(model_path="logs/best_model.pth", model_type="resnet50", num_classes=len(class_to_idx))

# 默认值
DEFAULT_CLASS_NAME = "未知"
DEFAULT_PROBABILITY = 0.0

@csrf_exempt
def detect_dish(request):
    try:
        if request.method in ['POST', 'GET']:
            if request.method == 'POST':
                if request.content_type == 'application/json':
                    data = json.loads(request.body)
                    image_url = data.get('image_url')
                    image_file = None
                else:
                    image_file = request.FILES.get('image')
                    image_url = request.POST.get('image_url')
            elif request.method == 'GET':
                image_file = None
                image_url = request.GET.get('image_url')

            if not image_file and not image_url:
                return JsonResponse({'cook': DEFAULT_CLASS_NAME, 'probability': DEFAULT_PROBABILITY})

            if image_file:
                # 读取上传的图片
                image = cv2.imdecode(np.frombuffer(image_file.read(), np.uint8), cv2.IMREAD_COLOR)
            elif image_url:
                # 下载图片
                response = requests.get(image_url, timeout=10)
                if response.status_code != 200:
                    return JsonResponse({
                        'cook': DEFAULT_CLASS_NAME,
                        'probability': DEFAULT_PROBABILITY,
                        'error': f'图片下载失败，状态码：{response.status_code}'
                    }, status=400)
                # 将下载的图片字节数据转换为 OpenCV 图像
                image_bytes = np.frombuffer(response.content, np.uint8)
                image = cv2.imdecode(image_bytes, cv2.IMREAD_COLOR)

            if image is None:
                return JsonResponse({
                    'cook': DEFAULT_CLASS_NAME,
                    'probability': DEFAULT_PROBABILITY,
                    'error': '无效的图片格式，无法解码'
                }, status=400)

            # 预处理图片
            input_tensor = preprocess_image(image)

            # 预测
            class_name, probability = predict_maturity(input_tensor, model, class_to_idx)

            return JsonResponse({'cook': class_name, 'probability': float(probability)})
        return JsonResponse({'error': 'Invalid request method'}, status=405)
    except requests.exceptions.RequestException as e:
        # 处理网络请求异常（超时、无效URL等）
        return JsonResponse({
            'error': f'图片链接请求异常：{str(e)}'
        }, status=500)
    except Exception as e:
        # 处理其他异常（预处理错误、模型预测错误等）
        return JsonResponse({
            'error': f'服务器处理异常：{str(e)}'
        }, status=500)