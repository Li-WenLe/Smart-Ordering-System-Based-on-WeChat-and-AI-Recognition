const app = getApp();
Page({
  /**
   * 页面的初始数据
   */
  data: {
    voucherInfo: [],
    buttonDisabled: {} // 新增：记录按钮禁用状态
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    this.getVoucherInfo();
  },

  // 获取优惠券信息
  getVoucherInfo() {
    const that = this; // 保存this引用
    wx.request({
      url: 'http://localhost:3000/voucher',
      method: "get",
      success: (resp) => {
        console.log(resp);
        that.setData({
          voucherInfo: resp.data.data
        });
      },
      fail: (error) => {
        console.log(error);
        wx.showToast({
          title: '获取优惠券信息失败',
          icon: 'none'
        });
      }
    });
  },

  // 抢购优惠券
  grabVoucher(e) {
    const that = this; // 保存this引用
    const voucherId = e.currentTarget.dataset.id;
    const userId = app.globalData.loginuser.id;

    // 设置按钮禁用状态
    that.setData({
      [`buttonDisabled.${voucherId}`]: true
    });

    // 设置3秒后恢复按钮
    setTimeout(() => {
      that.setData({
        [`buttonDisabled.${voucherId}`]: false
      });
    }, 3000);

    wx.request({
      url: `http://localhost:3000/user/seckill/seckill`,
      method: "post",
      data: {
        userId: userId,
        voucherId: voucherId
      },
      success: (resp) => {
        console.log(resp)
        if (resp.data.code === 1) {
          wx.showToast({
            title: '抢购成功',
            icon: 'success'
          });
          that.getVoucherInfo();
        } else if (resp.data.msg != null) {
          wx.showToast({
            title: resp.data.msg,
            icon: "error"
          });
        } else {
          wx.showToast({
            title: resp.data.msg || '抢购失败',
            icon: 'none'
          });
        }
      },
      fail: (error) => {
        console.log(error);
        wx.showToast({
          title: '网络错误，抢购失败',
          icon: 'none'
        });
      }
    });
  }
});