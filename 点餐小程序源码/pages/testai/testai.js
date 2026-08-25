// Page({
//   data: {
//     inputValue: '', // 用户输入内容
//     responseText: '' // 模型返回内容
//   },
//   // 处理用户输入框内容变化
//   handleInputChange: function (e) {
//     this.setData({
//       inputValue: e.detail.value
//     });
//   },
//   // 调用豆包模型的函数
//   sendRequestToDoubao: function () {
//     const input = this.data.inputValue;
//     if (!input) return;
//     const apiUrl = 'https://ark.cn-beijing.volces.com/api/v3/chat/completions'; 
//     const apiKey = 'a94524dd-3ca1-406d-a43b-0def7535ce16'; 
//     const modelId = 'ep-20250426223357-8r228'; 
//     wx.request({
//       method: 'POST',
//       url: apiUrl,
//       header: {
//         'Content-Type': 'application/json',
//         'Authorization': `Bearer ${apiKey}` // 按要求设置认证头
//       },
//       data: {
//         "model": modelId, 
//         "messages": [
//           {
//             "role": "user",
//             "content": input
//           }
//         ]
//       },
//       success: (res) => {
//         if (res.statusCode === 200) {
//           const response = res.data.choices[0].message.content;
//           this.setData({
//             responseText: response
//           });
//         } else {
//           console.error('请求失败，状态码：', res.statusCode);
//         }
//       },
//       fail: (err) => {
//         console.error('请求出错：', err);
//       }
//     });
//   }
// });