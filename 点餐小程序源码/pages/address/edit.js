const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    id: null,
    addressInfo: {},
    name: '',
    phone: '',
    region: '',
    detailAddress: '',
    isDefault: false,
    tag: ''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.setData({
      id: options.id,
    })
    this.getInfo(options.id);

  },
  getInfo(id) {
    wx.request({
      url: 'http://localhost:3000/user/address/getbyid',
      method: "get",
      data: {
        id: id
      },
      success: (resp) => {
        console.log(resp.data)
        this.setData({
          addressInfo: resp.data.data,
          name: resp.data.data.name,
          phone: resp.data.data.phone,
          region: resp.data.data.region,
          detailAddress: resp.data.data.detailAddress,
          tag: resp.data.data.tag
        })
      }
    })
  },
  // 输入事件处理
  onNameInput(e) {
    this.setData({
      name: e.detail.value
    });
  },

  onPhoneInput(e) {
    this.setData({
      phone: e.detail.value
    });
  },

  onRegionInput(e) {
    this.setData({
      region: e.detail.value
    });
  },

  onDetailAddressInput(e) {
    this.setData({
      detailAddress: e.detail.value
    });
  },

  // 默认地址切换
  onSwitchChange(e) {
    this.setData({
      isDefaultAddress: e.detail.value,
      isDefault: e.detail.value
    });
  },

  // 选择标签
  selectTag(e) {
    const tag = e.currentTarget.dataset.tag;
    this.setData({
      currentTag: tag,
      tag: tag
    });
  },
  saveAddress() {
    wx.request({
      url: 'http://localhost:3000/user/address/updateinfo',
      method: "post",
      data: {
        id: this.data.id,
        userId: app.globalData.loginuser.id,
        phone: this.data.phone,
        name: this.data.name,
        region: this.data.region,
        detailAddress: this.data.detailAddress,
        isDefaultAddress: this.data.isDefaultAddress,
        tag: this.data.tag
      },
      success: (resp) => {
        wx.showModal({
          title: '提示',
          content: '地址修改成功',
        })
      },
      fail() {
        wx.showModal({
          title: '提示',
          content: '地址修改失败，请稍后再试',
        })
      }
    })
  }


})
