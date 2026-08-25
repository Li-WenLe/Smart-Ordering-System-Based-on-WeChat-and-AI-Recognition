const app = getApp();

Page({
  data: {
    cartList: [],
    aiOrderList: [],
    dishInfo: null,
    chatHistory: [],
    inputValue: '',
    relatedQuestions: [],
    isLoading: false,
    previewImage: null, // 预览图片URL
    showPreview: false, // 是否显示预览
    predefinedQA: {
      "饮料推荐": {
        answer: "我们推荐以下饮料：\n1. 经典奶茶\n2. 芝士绿茶\n3. 水果茶\n4. 咖啡系列",
        related: [
          { question: "最受欢迎的饮料是什么？", answer: "芝士绿茶是我们店最畅销的饮品。" },
          { question: "有低糖选项吗？", answer: "所有饮料都可以选择少糖或无糖。" }
        ]
      },
      "营业时间": {
        answer: "我们的营业时间是：\n周一至周五: 10:00-22:00\n周末: 9:00-23:00",
        related: [
          { question: "节假日营业吗？", answer: "节假日正常营业，时间与周末相同。" }
        ]
      },
      "店铺信息": {
        answer: "我们的店铺位置在河南工学院校内具体位置是：\n新乡市，红旗区，河南工学院",
        related: [
          { question: "配送距离多远之后不在接单？", answer: "10公里之内正常接单，超过10公里后不再支持配送，请体谅。" }
        ]
      }
    },
    tempFilePath: null,
    fileType: '',
    src: '',
    cameraActive: false,
    scrollTop: 0
  },

  onLoad() {
    this.initCartData();
  },

  initCartData() {
    const cart = wx.getStorageSync('cartList') || [];
    this.setData({ cartList: cart });
    app.globalData.cartList = cart;
    this.updateCartBadge();
  },

  handleInput: function (e) {
    this.setData({ inputValue: e.detail.value });
  },

  sendMessage: function () {
    const input = this.data.inputValue.trim();
    if (!input) return;

    this.addMessage(input, true);
    this.setData({ inputValue: '' });

    if (this.isPurchaseCommand(input)) {
      this.handlePurchaseCommand(input);
      return;
    }

    if (this.data.predefinedQA[input]) {
      this.showPredefinedAnswer(input);
    } else {
      this.getAIResponse(input);
    }
  },

  addMessage: function (content, isUser) {
    let message;
    if (typeof content === 'string') {
      message = { isUser, content, type: 'text' };
    } else {
      message = { isUser, ...content };
    }

    this.setData({
      chatHistory: [...this.data.chatHistory, message],
      scrollTop: this.data.scrollTop + 10000 // 确保滚动到底部
    });
  },

  showPredefinedAnswer: function (question) {
    const qa = this.data.predefinedQA[question];
    this.addMessage(qa.answer, false);
    this.setData({ relatedQuestions: qa.related || [] });
  },

  getAIResponse: function (input) {
    const apiUrl = 'https://ark.cn-beijing.volces.com/api/v3/chat/completions';
    const apiKey = 'a94524dd-3ca1-406d-a43b-0def7535ce16';
    const modelId = 'doubao-1.5-vision-lite-250315';

    this.setData({ isLoading: true });

    wx.request({
      method: 'POST',
      url: apiUrl,
      header: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${apiKey}`
      },
      data: {
        "model": modelId,
        "messages": [
          {
            "role": "user",
            "content": input
          }
        ]
      },
      timeout: 15000,
      success: (res) => {
        this.setData({ isLoading: false });
        if (res.statusCode === 200) {
          const response = res.data.choices[0].message.content;
          this.addMessage(response, false);
        } else {
          console.error('请求失败，状态码：', res.statusCode);
          this.addMessage("服务暂时不可用，请稍后再试", false);
        }
      },
      fail: (err) => {
        this.setData({ isLoading: false });
        console.error('请求出错：', err);
        this.addMessage("服务暂时不可用，请稍后再试", false);
      }
    });
  },

  selectQuestion: function (e) {
    const { question, answer } = e.currentTarget.dataset;
    this.addMessage(question, true);
    this.addMessage(answer, false);
    this.setData({ relatedQuestions: [] });
  },

  onConfirm: function () {
    this.sendMessage();
  },

  isPurchaseCommand: function (input) {
    return input.startsWith("购买") || input.includes("买") || input.includes("要");
  },

  handlePurchaseCommand: function (input) {
    const regex = /(购买|买|要)(.+?)(\d+)(份|个|杯|盒|瓶)?/;
    const match = input.match(regex);

    if (match) {
      const productName = match[2].trim();
      const quantity = parseInt(match[3]);

      const existingItem = this.data.cartList.find(item => item.title && item.title.includes(productName));
      if (existingItem) {
        wx.showModal({
          title: '提示',
          content: `购物车中已有${existingItem.number}份${existingItem.title}，是否继续添加？`,
          success: (res) => {
            if (res.confirm) {
              this.getDishInfo(productName, quantity);
            }
          }
        });
      } else {
        this.getDishInfo(productName, quantity);
      }
    } else {
      this.addMessage("请使用正确格式，例如：购买奶茶2杯", false);
    }
  },

  getDishInfo: function (productName, quantity) {
    wx.showLoading({
      title: '查询商品中...',
    });

    wx.request({
      url: 'http://localhost:3000/user/dish/getdishbydishname',
      method: "POST",
      data: { name: productName },
      success: (resp) => {
        wx.hideLoading();
        if (resp.data.data) {
          this.processPurchase(resp.data.data[0], quantity);
        } else {
          this.addMessage(`抱歉，没有找到"${productName}"商品`, false);
        }
      },
      fail: (err) => {
        wx.hideLoading();
        console.log(err);
        this.addMessage("网络错误，请稍后再试", false);
      }
    });
  },

  processPurchase: function (dishInfo, quantity) {
    if (quantity <= 0 || quantity > 99) {
      this.addMessage("购买数量必须在1-99之间", false);
      return;
    }

    const cartItem = {
      id: dishInfo.id,
      name: dishInfo.name,
      price: dishInfo.price,
      image: dishInfo.image,
      number: quantity,
      choose: true
    };

    this.updateCart(cartItem);

    wx.showToast({
      title: '已添加到购物车',
      icon: 'success'
    });

    this.addMessage(`已添加 ${quantity} 份 ${cartItem.name} 到购物车`, false);

    setTimeout(() => {
      wx.navigateTo({
        url: '/pages/cart/cart'
      });
    }, 1500);
  },

  updateCart: function (newItem) {
    let cart = [...this.data.cartList];
    let index = -1;

    if (cart.length === 0) {
      cart.push(newItem);
    } else {
      for (let idx in cart) {
        if (cart[idx].id === newItem.id) {
          index = idx;
          break;
        }
      }

      if (index !== -1) {
        cart[index].number += newItem.number;
      } else {
        cart.push(newItem);
      }
    }

    this.setData({ cartList: cart });
    app.globalData.cartList = cart;
    wx.setStorageSync('cartList', cart);
    this.updateCartBadge();
  },

  updateCartBadge: function () {
    const totalItems = this.data.cartList.reduce((sum, item) => sum + (item.number || 0), 0);
    if (totalItems > 0) {
      wx.setTabBarBadge({
        index: 2,
        text: totalItems.toString()
      });
    } else {
      wx.removeTabBarBadge({
        index: 2
      });
    }
  },

  // 图片相关方法
  chooseFile() {
    wx.chooseMedia({
      count: 1,
      mediaType: ['image'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const file = res.tempFiles[0];
        this.setData({
          tempFilePath: file.tempFilePath,
          fileType: file.fileType,
          previewImage: file.tempFilePath,
          showPreview: true
        });
      }
    });
  },

  toggleCamera() {
    this.setData({
      cameraActive: !this.data.cameraActive,
      showPreview: false
    });
  },

  takePhoto() {
    const ctx = wx.createCameraContext();
    ctx.takePhoto({
      quality: 'high',
      success: (res) => {
        this.setData({
          src: res.tempImagePath,
          previewImage: res.tempImagePath,
          showPreview: true,
          cameraActive: false
        });
      },
      fail: (err) => {
        console.error('拍照失败', err);
        wx.showToast({
          title: '拍照失败',
          icon: 'none'
        });
      }
    });
  },

  confirmSendPhoto() {
    if (!this.data.previewImage) return;

    this.setData({ showPreview: false });
    this.sendPhoto(this.data.previewImage);
  },

  cancelSendPhoto() {
    this.setData({
      previewImage: null,
      showPreview: false,
      cameraActive: true
    });
  },

  error(e) {
    console.log(e.detail);
  },

  sendPhoto(filePath) {
    if (!filePath) {
      wx.showToast({ title: '请先选择图片', icon: 'none' });
      return;
    }

    wx.showLoading({
      title: '发送中...',
      mask: true
    });

    wx.uploadFile({
      url: 'http://localhost:3000/upload',
      filePath: filePath,
      name: 'file',
      success: (res) => {
        wx.hideLoading();
        try {
          let imageUrl;
          if (typeof res.data === 'string' && res.data.startsWith('http')) {
            imageUrl = res.data;
          } else {
            const data = JSON.parse(res.data);
            imageUrl = data.imageUrl || data.url || data.data;
            if (!imageUrl) throw new Error('未找到有效图片URL');
          }

          this.addMessage({ type: 'image', content: imageUrl }, true);
          this.sendImageToAI(imageUrl);
        } catch (e) {
          console.error('数据处理错误:', e);
          wx.showToast({
            title: '数据处理失败: ' + e.message,
            icon: 'none',
            duration: 2000
          });
        }
      },
      fail: (err) => {
        wx.hideLoading();
        console.error('上传失败:', err);
        wx.showToast({
          title: '上传失败: ' + err.errMsg,
          icon: 'none',
          duration: 2000
        });
      }
    });
  },

  sendImageToAI(imageUrl) {
    wx.request({
      url: 'http://127.0.0.1:8000',
      method: 'POST',
      header: {
        'Content-Type': 'application/json'
      },
      data: {
        image_url: imageUrl
      },
      success: (res) => {
        if (res.data.cook) {
          const dishname = res.data.cook;
          console.log(dishname);
          wx.request({
            url: 'http://localhost:3000/user/dish/getdishbyname',
            method: "post",
            data: {
              name: res.data.cook,
            },
            success: (resp) => {
              console.log(resp.data);
              const messages = `识别结果: ${res.data.cook}
              商品信息：
              售价：￥${resp.data.data.price}
              商品名称：${resp.data.data.name}
              商品简介：${resp.data.data.description}
              `;
              this.addMessage(messages, false);
              this.addMessage({ type: 'image', content: resp.data.data.image }, false);
            },
            fail(err) {
              console.log(err);
            }
          });
        } else {
          this.addMessage("分析失败: " + (res.data.error || "未知错误"), false);
        }
      },
      fail: (err) => {
        this.addMessage("服务请求失败: " + err.errMsg, false);
      }
    });
  }
});