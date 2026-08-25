// pages/address/address.js
const app = getApp()
Page({
  data: {
    addressList: [],
  },
  onShow() {
    this.getAllAddress()
  },
  // 设置默认地址
  setDefault(e) {
    const index = e.currentTarget.dataset.index;
    let addressList = this.data.addressList;
    addressList.forEach(item => item.isDefault = false);
    addressList[index].isDefault = true;
    this.setData({ addressList });
  },

  // 编辑地址
  editAddress(e) {
    const id = e.currentTarget.dataset.id;
    console.log(id)
    wx.navigateTo({
      url: `/pages/address/edit?id=${id}`
    });
  },

  // 删除地址
  deleteAddress(e) {
    const id = e.currentTarget.dataset.id;
    wx.showModal({
      title: '提示',
      content: '确定要删除该地址吗？',
      success: (res) => {
        if (res.confirm) {
          let addressList = this.data.addressList.filter(item => item.id !== id);
          this.setData({ addressList });
        }
      }
    });
  },
  //获取用户的所有地址信息
  getAllAddress() {
    console.log(app.globalData.loginuser.id)
    wx.request({
      url: 'http://localhost:3000/user/address/getall',
      method: "get",
      data: {
        id: app.globalData.loginuser.id,
      },
      success: (resp) => {
        console.log(resp.data)
        this.setData({
          addressList: resp.data.data
        })
      },
      fail(error) {
        console.log(error)
      }
    })
  },
  // 新增地址
  addAddress() {
    wx.navigateTo({
      url: '/pages/address/address'
    });
  }
});