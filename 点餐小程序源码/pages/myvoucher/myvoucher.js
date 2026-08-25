Page({
  data: {
    id: null,
    voucherList: [],      // 用户拥有的优惠券数量
    voucherInfo: [],      // 所有优惠券详情
    validVouchers: [],    // 未过期优惠券
    expiredVouchers: []   // 已过期优惠券
  },

  onLoad(options) {
    this.setData({ id: options.id });
    this.getAllData(options.id);
  },

  // 合并请求
  getAllData(userId) {
    wx.showLoading({ title: '加载中...' });
    Promise.all([
      this.getUserVouchers(userId),
      this.getAllVoucherInfo()
    ]).then(() => {
      this.combineData();
      wx.hideLoading();
    }).catch(err => {
      console.error(err);
      wx.hideLoading();
    });
  },

  // 获取用户拥有的优惠券数量
  getUserVouchers(userId) {
    return new Promise((resolve, reject) => {
      wx.request({
        url: 'http://localhost:3000/user/voucher',
        method: "POST",
        data: { 
          userId:app.globalData.loginuser.id
        },
        success: (res) => {
          console.log(res)
          this.setData({ voucherList: res.data.data });
          resolve();
        },
        fail: reject
      });
    });
  },

  // 获取所有优惠券详情
  getAllVoucherInfo() {
    return new Promise((resolve, reject) => {
      wx.request({
        url: 'http://localhost:3000/user/voucher/getall',
        method: "POST",
        success: (res) => {
          console.log(res)
          this.setData({ voucherInfo: res.data.data });
          resolve();
        },
        fail: reject
      });
    });
  },

  // 合并数据并判断过期状态
  combineData() {
    const { voucherList, voucherInfo } = this.data;
    const combined = voucherList.map(item => {
      const detail = voucherInfo.find(v => v.id === item.voucherId) || {};
      const amount = detail.title ? detail.title.match(/\d+/)?.[0] : "0";
      const isExpired = this.checkExpired(detail.usedTime);

      return {
        ...item,
        ...detail,
        amount,
        isExpired
      };
    });

    this.setData({
      validVouchers: combined.filter(item => !item.isExpired),
      expiredVouchers: combined.filter(item => item.isExpired)
    });
  },

  // 检查是否过期
  checkExpired(expiryDate) {
    if (!expiryDate) return true;
    return new Date(expiryDate).getTime() < Date.now();
  }
});