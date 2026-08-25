// pages/index/bannerDetail/bannerDetail.js
Page({
  data: {

  },
  onLoad: function (options) {
    //根据传过来的id来查询轮播图数据
    /*wx.cloud.database().collection('banner').doc(options.id).get()
    .then(res=>{
      this.setData({
        banner:res.data
      })
    })*/
    wx.request({
      url: 'http://localhost:3000/user/banner/getbyid',
      method: "get",
      data: {
        id: options.id
      },
      success: (resp) => {
        console.log(resp.data)
        this.setData({
          banner: resp.data.data
        })
      },
      fail(error) {
        console.log(error);
      }
    })
  },

})