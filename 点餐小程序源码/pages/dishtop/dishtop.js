// pages/dishtop/dishtop.js
Page({


  data: {
    weekdishtopList: [],
  },


  onLoad(options) {
    this.getTodayDishTop()
  },

  getTodayDishTop() {
    wx.request({
      url: 'http://localhost:3000/admin/order/todaytop',
      method: "get",
      success: (resp) => {
        console.log(resp)
        this.setData({
          weekdishtopList: resp.data.data
        })
      }
    })
  }

})