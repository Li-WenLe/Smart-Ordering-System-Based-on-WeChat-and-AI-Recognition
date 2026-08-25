# -*- coding:utf-8 -*-
# -*- coding:utf-8 -*-
import requests

# 配置参数
URL = "http://127.0.0.1:8000/"  # 请根据实际情况修改为你的视图对应的 URL
IMAGE_URL = "https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/50f08d29-949a-4881-8da7-f35405272bbb_WTuAhSZf1kZC9abe46b5701eaf9ad0e2a7e996a81d38.png"  # 图片链接，需替换为实际链接

try:
    # 下载图片
    image_response = requests.get(IMAGE_URL)
    if image_response.status_code != 200:
        print(f"下载图片失败，状态码: {image_response.status_code}")
    else:
        # 发送 POST 请求
        files = {'image': image_response.content}
        print(files)
        response = requests.post(URL, files=files)

        if response.status_code == 200:
            result = response.json()
            print("请求成功！")
            print(f"菜品成熟度分类: {result['cook']}")
            print(f"概率: {result['probability']}")
        else:
            print(f"请求失败，状态码: {response.status_code}")
            print(f"错误信息: {response.json().get('error', '未知错误')}")

    # # 发送 GET 请求（这里 GET 请求假设没有图片，返回默认值）
    # response = requests.get(URL)
    # if response.status_code == 200:
    #     result = response.json()
    #     print("GET 请求成功！")
    #     print(f"菜品成熟度分类: {result['cook']}")
    #     print(f"概率: {result['probability']}")
    # else:
    #     print(f"GET 请求失败，状态码: {response.status_code}")
    #     print(f"错误信息: {response.json().get('error', '未知错误')}")

except requests.RequestException as e:
    print(f"请求发生异常: {e}")
except ValueError as e:
    print(f"解析响应 JSON 时发生错误: {e}")
except Exception as e:
    print(f"发生其他异常: {e}")