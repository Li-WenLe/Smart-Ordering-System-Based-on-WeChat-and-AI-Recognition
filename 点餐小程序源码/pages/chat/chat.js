const app = getApp();

Page({
  data: {
    messages: [],          // 存储聊天消息
    inputValue: '',        // 输入框内容
    socketStatus: 'closed', // WebSocket连接状态
    currentUser:  "user-"+app.globalData.loginuser.id, // 当前用户ID
    reconnectAttempts: 0,  // 重连尝试次数
    maxReconnectAttempts: 5 // 最大重连尝试次数
  },

  onLoad(options) {
    this.connectWebSocket();
  },

  onUnload() {
    this.closeWebSocket();
  },

  // 建立WebSocket连接
  connectWebSocket() {
    this.setData({ socketStatus: 'connecting' });
    
    this.socket = wx.connectSocket({
      url: 'ws://localhost:3000/chat',
      success: () => console.log('WebSocket连接建立中...'),
      fail: (err) => {
        console.error('WebSocket连接失败', err);
        this.handleConnectionError();
      }
    });

    this.socket.onOpen(() => {
      console.log('WebSocket连接成功');
      this.setData({ 
        socketStatus: 'connected',
        reconnectAttempts: 0 // 重置重连计数器
      });
    });

    this.socket.onError((err) => {
      console.error('WebSocket发生错误', err);
      this.setData({ socketStatus: 'error' });
      this.handleConnectionError();
    });

    this.socket.onClose(() => {
      console.log('WebSocket连接关闭');
      this.setData({ socketStatus: 'closed' });
      this.handleConnectionError();
    });

    this.socket.onMessage((res) => {
      console.log('收到服务器消息:', res);
      try {
        const message = JSON.parse(res.data);
        this.receiveMessage(message);
      } catch (e) {
        console.error('消息解析错误', e);
        // 如果解析失败，作为普通文本消息处理
        this.receiveMessage({
          sender: 'admin',
          content: res.data,
          timestamp: Date.now()
        });
      }
    });
  },

  // 处理连接错误和重连逻辑
  handleConnectionError() {
    if (this.data.reconnectAttempts < this.data.maxReconnectAttempts) {
      const delay = Math.min(1000 * this.data.reconnectAttempts, 10000);
      console.log(`将在${delay/1000}秒后尝试重新连接...`);
      
      setTimeout(() => {
        this.setData({ reconnectAttempts: this.data.reconnectAttempts + 1 });
        this.connectWebSocket();
      }, delay);
    }
  },

  // 关闭WebSocket连接
  closeWebSocket() {
    if (this.socket) {
      this.socket.close({
        success: () => {
          console.log('WebSocket已关闭');
          this.setData({ socketStatus: 'closed' });
        },
        fail: (err) => {
          console.error('关闭WebSocket失败', err);
        }
      });
    }
  },

  // 处理接收到的消息
  receiveMessage(message) {
    // 确保消息有必要的字段
    const fullMessage = {
      sender: message.sender || 'unknown',
      content: message.content || '',
      timestamp: message.timestamp || Date.now(),
      // 添加一个字段标识是否是自己发送的消息
      isSelf: message.sender === this.data.currentUser
    };
    
    this.setData({
      messages: [...this.data.messages, fullMessage]
    });
    
    wx.nextTick(() => {
      this.scrollToBottom();
    });
  },

  // 发送消息
  sendMessage() {
    const content = this.data.inputValue.trim();
    if (!content) {
      wx.showToast({
        title: '消息不能为空',
        icon: 'none'
      });
      return;
    }
  
    if (!this.socket || this.data.socketStatus !== 'connected') {
      wx.showToast({
        title: '未连接到服务器',
        icon: 'none'
      });
      return;
    }
  
    const message = {
      sender: this.data.currentUser,
      content: content,
      timestamp: Date.now(),
      isSelf: true
    };
  
    this.socket.send({
      data: JSON.stringify(message),
      success: () => {
        console.log('消息发送成功');
        // 不在这里添加消息，等待服务器回传
        this.setData({ inputValue: '' });
      },
      fail: (err) => {
        console.error('消息发送失败', err);
        wx.showToast({
          title: '发送失败',
          icon: 'none'
        });
      }
    });
  },

  // 输入框内容变化
  onInputChange(e) {
    this.setData({ inputValue: e.detail.value });
  },

  // 点击发送按钮
  onSendButtonTap() {
    this.sendMessage();
  },

  // 滚动到底部
  scrollToBottom() {
    wx.createSelectorQuery()
      .select('.message-container')
      .boundingClientRect(rect => {
        if (rect) {
          wx.pageScrollTo({
            scrollTop: rect.height,
            duration: 300
          });
        }
      })
      .exec();
  },

  // 格式化时间显示
  formatTime(timestamp) {
    const date = new Date(timestamp);
    const hour = date.getHours().toString().padStart(2, '0');
    const minute = date.getMinutes().toString().padStart(2, '0');
    return `${hour}:${minute}`;
  }
});