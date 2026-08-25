const app = getApp();

Page({
  data: {
    userId: app.globalData.loginuser.id,
    name: '',          // 联系人姓名
    phone: '',         // 手机号
    region: '',        // 省/市/区
    detailAddress: '', // 详细地址
    currentTag: '',    // 当前选中的标签（company/home/school）
    isDefaultAddress: false, // 是否设为默认地址
    region: '',
  },

  onLoad() {
    this.loadProvinces();
  },
  onRegionChange(e) {
    const [province, city, district] = e.detail.value;
    this.setData({
      region: `${province} ${city} ${district}`
    });
  },
  // 监听姓名输入
  onNameInput(e) {
    this.setData({ name: e.detail.value });
  },

  // 监听手机号输入
  onPhoneInput(e) {
    this.setData({ phone: e.detail.value });
  },

  // 监听省市区输入
  onRegionInput(e) {
    this.setData({ region: e.detail.value });
  },

  // 监听详细地址输入
  onDetailAddressInput(e) {
    this.setData({ detailAddress: e.detail.value });
  },

  // 监听 Switch 变化
  onSwitchChange(e) {
    this.setData({
      isDefaultAddress: e.detail.value,
    });
  },

  // 选择标签
  selectTag(e) {
    const tag = e.currentTarget.dataset.tag;
    this.setData({ currentTag: tag });
  },

  // 保存地址
  saveAddress() {
    const {
      userId,
      name,
      phone,
      region,
      detailAddress,
      currentTag,
      isDefaultAddress
    } = this.data;

    // 简单校验
    if (!name || !phone || !region || !detailAddress) {
      wx.showToast({ title: '请填写完整信息', icon: 'none' });
      return;
    }

    wx.request({
      url: 'http://localhost:3000/user/address/add',
      method: "POST",
      data: {
        userId: userId,
        name: name,
        phone: phone,
        region: region,
        detailAddress: detailAddress,
        isDefaultAddress: isDefaultAddress,
        tag: currentTag,
      },
      success: (resp) => {
        console.log("保存成功:", resp.data);
        wx.showToast({
          title: '地址保存成功',
          icon: 'success'
        });
        setTimeout(() => wx.navigateBack(), 1500);
      },
      fail: (error) => {
        console.error("保存失败:", error);
        wx.showToast({
          title: '保存失败，请重试',
          icon: 'none'
        });
      },
    });
  },

  // 取消
  cancel() {
    wx.navigateBack();
  },
});