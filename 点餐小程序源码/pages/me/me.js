const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userAvatar: '/images/未登录.png', // 默认头像
    username: null,
    booleanlogin: false,
    userId: null,
    historyOrderList: [],
    recentOrderList: [], // 新增最近订单列表
    currentOrderType: 'history' // 当前显示的订单类型，默认为历史订单
  },

  onShow() {
    // 每次页面显示时检查全局变量中的用户信息
    if (app.globalData.loginuser) {
      this.setData({
        userAvatar: app.globalData.loginuser.photo,
        username: app.globalData.loginuser.username,
        booleanlogin: app.globalData.booleanlogin,
        name: app.globalData.loginuser.name,
        userId: app.globalData.loginuser.id
      });
    }
    this.historyOrder();
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.historyOrder();
  },

  turntomyorder() {
    wx.navigateTo({
      url: '/pages/me/myorder/myorder',
    });
  },

  turntomyvoucher(event) {
    let userId = event.currentTarget.dataset.id;
    console.log(userId)
    wx.navigateTo({
      url: '/pages/myvoucher/myvoucher?id=' + userId,
    })
  },

  turntologin() {
    wx.navigateTo({
      url: '/pages/login/login',
    })
  },
  turntochat(event) {
    let userId = event.currentTarget.dataset.id;
    wx.navigateTo({
      url: '/pages/chat/chat?id=' + userId,
    })
  },
  wxLogin(e) {
    console.log(e);
    this.setData({
      userAvatar:e.detail.avatarUrl
    })
    // wx.login({
    //   // success: (res) => {
    //   //   if (res.code) {
    //   //     // wx.getUserProfile({
    //   //     //   desc: "登录使用",
    //   //     //   success: (info) => {
    //   //     //     wx.setStorageSync("userInfo", info.userInfo);
    //   //     //     this.setData({ userInfo: info.userInfo });
    //   //     //     wx.showToast({ title: "登录成功" });
    //   //     //   },
    //   //     //   fail: () => {
    //   //     //     wx.showToast({ title: "取消登录", icon: "none" });
    //   //     //   }
    //   //     // })
    //   //     console.log(res.code);
        
    // })
  },
  getNickname(e){
    console.log(e);
  },
  turntoregister() {
    wx.navigateTo({
      url: '/pages/regist/regist',
    })
  },

  turntomyaddress() {
    wx.navigateTo({
      url: '/pages/addressmanger/addressmanger',
    })
  },
  turntousertaste() {
    wx.navigateTo({
      url: '/pages/usertaste/usertaste',
    })
  },

  // 退出登录
  logout() {
    app.globalData.loginuser = null; // 清空全局变量中的用户信息
    this.setData({
      userAvatar: '/images/未登录.png', // 重置头像
      booleanlogin: false
    });

    wx.showToast({
      title: '退出成功',
      icon: 'success',
    });
  },

  getStatusColor(status) {
    const colorMap = {
      0: '#07C160', // 已完成-绿色
      1: '#FF976A', // 待付款-橙色
      2: '#1989FA', // 待接单-蓝色
      3: '#909399', // 已取消-灰色
      4: '#FF6146', // 配送中-红色
      5: '#909399'  // 已退款-灰色
    };
    return colorMap[status] || '#909399';
  },

  // 历史订单回显
  // 历史订单回显
  historyOrder() {
    console.log(this.data.userId);
    wx.request({
      url: 'http://localhost:3000/user/order/history',
      method: "post",
      data: {
        userId: app.globalData.loginuser.id
      },
      success: (resp) => {
        console.log(resp);
        // 对订单数据按照 orderId 从大到小排序
        const sortedOrders = resp.data.data.sort((a, b) => {
          return parseInt(b.orderId) - parseInt(a.orderId);
        });
        this.setData({
          historyOrderList: sortedOrders,
          recentOrderList: sortedOrders.slice(0, 4), // 截取前4个最近的订单
          currentOrderType: 'history'
        });
      }
    });
  },
  // 再来一单
  reorder(e) {
    const orderId = e.currentTarget.dataset.id;
    wx.showToast({ title: `重新下单: ${orderId}`, icon: 'none' });
    // 根据订单号获取订单购买商品和数量
    wx.request({
      url: 'http://localhost:3000/user/order/get',
      method: "post",
      data: {
        orderId: orderId,
      },
      success: (resp) => {
        console.log(resp);
        this.setData({
          reOrderList: resp.data.data
        });
        console.log(app.globalData.cartList)
        const cartData = resp.data.data.map(item => ({
          choose: true,
          id: item.dishId,
          image: item.image,
          number: item.number,
          price: item.acount,
          name: item.dishname,
        }));
        app.globalData.cartList = cartData
        // 跳转到购物车页面
        wx.switchTab({
          url: '/pages/cart/cart',
          success: () => {
            console.log('页面跳转成功');
          },
          fail: (err) => {
            console.error('页面跳转失败:', err);
            wx.showToast({ title: '页面跳转失败', icon: 'none' });
          }
        });
      },
      fail: (err) => {
        console.error('获取订单详情失败:', err);
        wx.showToast({ title: '获取订单详情失败', icon: 'none' });
      }
    });
  },
  // 计算总金额
  calculateTotal: function (items) {
    return items.reduce((total, product) => total + (product.acount * product.number), 0).toFixed(2);
  },

  // 点击切换订单类型
  switchOrderType(e) {
    const type = e.currentTarget.dataset.type;
    this.setData({ currentOrderType: type });
  },
  turntovoucher() {
    wx.navigateTo({
      url: '../voucher/voucher',
      success() {
        console.log("跳转成功")
      },
      error() {
        console.log("跳转失败")
      }
    })
  },
  repay(e) {
    const { orderId, totalPrice } = e.currentTarget.dataset;
    wx.showModal({
      title: '支付确认',
      content: `合计支付：¥${totalPrice.toFixed(2)}`,
      success: (res) => {
        if (res.confirm) {
          this.updateOrder(orderId);
        } else {
          wx.showToast({ title: '已取消支付', icon: 'none' });
        }
      }
    });
  },
  updateOrder(orderId) {
    wx.request({
      url: 'http://localhost:3000/user/order/updatestatus',
      method: 'POST',
      data: {
        orderId: orderId,
      },
      success: (resp) => {
        if (resp.data.code) {
          wx.showToast({ title: '支付成功', icon: 'success' });
          this.historyOrder();
        } else {
          wx.showToast({ title: resp.data.message || '支付失败', icon: 'none' });
        }
      },
      fail: (e) => {
        console.error(e);
        wx.showToast({ title: '网络错误，请重试', icon: 'none' });
      }
    });
  }
})