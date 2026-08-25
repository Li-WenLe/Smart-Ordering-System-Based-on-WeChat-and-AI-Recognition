const app = getApp()
Page({
  data: {
    reorderList: [], // 商品列表
    totalPrice: 0,   // 总价格
    status:false,//订单支付状态
    nameandphonenumber:null,
    note:null
  },
  onLoad(options) {
    // 页面加载时设置商品列表
    console.log( app.globalData.reorderList)
    this.setData({
      reorderList: app.globalData.reorderList || [],
    });
    // 初始化总价
    this.calculateTotalPrice();
  },
  onShow() {
    // 页面显示时更新商品列表
    this.setData({
      reorderList: app.globalData.reorderList || [],
    });
    // 重新计算总价
    this.calculateTotalPrice();
    //重新判断订单状态
    this.check()
  },
  // 计算总价
  calculateTotalPrice() {
    let totalPrice = 0;
    this.data.reorderList.forEach(item => {
      totalPrice += item.number * item.price;
    });
    this.setData({
      totalPrice: totalPrice.toFixed(2),
    });
  },
  // 减少商品数量
  reduce(e) {
    const index = e.currentTarget.dataset.index; // 获取商品的索引
    let reorderList = this.data.reorderList;
    if (reorderList[index].number > 0) {
      reorderList[index].number -= 1; 
      this.setData({
        reorderList: reorderList, 
      });
      // 更新全局数据
      app.globalData.reorderList = reorderList;
      // 重新计算总价
      this.calculateTotalPrice();
    }
  },
  add(e) {
    const index = e.currentTarget.dataset.index; // 获取商品的索引
    let reorderList = this.data.reorderList;
    reorderList[index].number += 1; 
    this.setData({
      reorderList: reorderList, 
    });
    // 更新全局数据
    app.globalData.reorderList = reorderList;
    // 重新计算总价
    this.calculateTotalPrice();
  },
  // 提交订单并处理支付
  addOrderandpay() {
    // 触发支付流程，支付完成后执行添加订单的操作
    this.pay(); // 触发支付流程
  },

  // 支付逻辑
  pay() {
    wx.showModal({
      title: '提示',
      content: '是否支付商品合计：' + this.data.totalPrice + '元',
      confirmText: '支付',
      cancelText: '取消',
      success: (res) => {
        if (res.confirm) {
          // 用户点击支付，执行支付逻辑
          console.log('用户点击了支付');
          this.setData({
            status: true  // 支付成功，更新支付状态为 true
          });
          // 执行添加订单
          this.addOrder();
        } else if (res.cancel) {
          // 用户点击取消，支付状态保持 false
          console.log('用户取消了支付');
          this.setData({
            status: false  // 支付取消，保持支付状态为 false
          });
          // 执行添加订单
          this.addOrder();
        }
      },
      fail: (err) => {
        console.error('显示模态框失败:', err);
      }
    });
  },

  // 添加订单到数据库，支付状态是由 `pay` 中设置的 `this.data.status` 决定
  addOrder() {
    wx.cloud.database().collection('emotion').add({
      data: {
        nameandphonenumber: this.data.nameandphonenumber,
        note: this.data.note,
        total: this.data.totalPrice,
        usergoods: this.data.reorderList,
        time: new Date(),
        status: this.data.status,  // 使用最新的支付状态
      },
      success: (res) => {
        console.log('订单添加成功', res);
      },
      fail: (err) => {
        console.error('订单添加失败:', err);
      }
    });
  },

  check(){
    if(this.data.status==true){
      let reorderList=null
      app.globalData.reorderList=reorderList
    }
    console.log(this.data.status)
  },
  // 获取备注信息
  getNote: function (event) {
    this.setData({
      note: event.detail.value
    });
  },
  // 获取姓名和联系方式
  getInformation: function (event) {
    this.setData({
      nameandphonenumber: event.detail.value
    });
  },
});
