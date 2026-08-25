const app = getApp()

Page({
  data: {
    activeTab: 'unpaid', // 当前激活的标签
    historyList: [],    // 所有订单
    unpaidOrders: [],   // 待支付订单(status=1)
    completedOrders: [], // 已完成订单(status=0)
    waitingOrders: [],  // 待接单订单(status=2)
    deliveringOrders: [], // 待送达订单(status=4)
    timeoutOrders: [],  // 已取消/超时订单(status=3)
    onshowList: [],     // 当前显示的订单列表
    reOrderList: [],//再次购买订单信息
  },

  onLoad() {
    this.loadOrderData()
    this.getUnpaidOrders()
  },

  // 加载订单数据
  loadOrderData() {
    wx.showLoading({ title: '加载中...' })

    wx.request({
      url: 'http://localhost:3000/user/order/history',
      method: "POST",
      data: { userId: app.globalData.loginuser.id },
      success: (res) => {
        console.log(res)
        console.log('status=1的订单:', res.data.data.filter(order => order.status === 1));
        wx.hideLoading()
        if (res.data && res.data.data) {
          this.processOrderData(res.data.data)
        }
      },
      fail: (err) => {
        wx.hideLoading()
        console.error('加载订单失败:', err)
        wx.showToast({ title: '加载失败', icon: 'none' })
      }
    })
  },

  //获取待支付订单
  getUnpaidOrders() {
    wx.request({
      url: 'http://localhost:3000/user/order/getupay',
      method: "POST",
      data: {
        userId: app.globalData.loginuser.id
      },
      success: (resp) => {
        console.log(resp.data)
      }
    })
  },
  // 处理订单数据
  processOrderData(orders) {
    orders.sort((a, b) => new Date(b.orderId) - new Date(a.orderId));
    const unpaid = []
    const completed = []
    const waiting = []
    const delivering = []
    const timeout = []

    orders.forEach(order => {
      switch (order.status) {
        case 0: completed.push(order); break
        case 1: unpaid.push(order); break
        case 2: waiting.push(order); break
        case 3: timeout.push(order); break
        case 4: delivering.push(order); break
      }
    })

    this.setData({
      historyList: orders,
      unpaidOrders: unpaid,
      completedOrders: completed,
      waitingOrders: waiting,
      deliveringOrders: delivering,
      timeoutOrders: timeout,
      onshowList: unpaid // 默认显示待支付订单
    })
  },

  // 切换标签
  switchTab(e) {
    const type = e.currentTarget.dataset.type
    let currentList = []

    switch (type) {
      case 'unpaid': currentList = this.data.unpaidOrders; break
      case 'completed': currentList = this.data.completedOrders; break
      case 'waiting': currentList = this.data.waitingOrders; break
      case 'delivering': currentList = this.data.deliveringOrders; break
      case 'timeout': currentList = this.data.timeoutOrders; break
    }

    this.setData({
      activeTab: type,
      onshowList: currentList
    })
    console.log(this.data.unpaidOrders)
  },

  // 获取状态文本
  getStatusText(status) {
    const statusMap = {
      0: '已完成',
      1: '待支付',
      2: '待接单',
      3: '已取消',
      4: '待送达'
    }
    return statusMap[status] || ''
  },

  // 获取状态颜色
  getStatusColor(status) {
    const colorMap = {
      0: '#07C160', // 绿色
      1: '#FF976A', // 橙色
      2: '#1989FA', // 蓝色
      3: '#909399', // 灰色
      4: '#FF6146'  // 红色
    }
    return colorMap[status] || '#909399'
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
  }
})