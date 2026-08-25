// pages/index/searchDetail/searchDetail.js
Page({

  data: {

  },

  onLoad(options) {

  },
  getvalue(event) {
    console.log(event.detail.value)
    let inputvalue = event.detail.value
    this.setData({
      inputvalue
    })
  },
  search() {
    //创建正则表达式，支持大小写不敏感匹配。
    wx.cloud.database().collection('goods').where({
      title: wx.cloud.database().RegExp({
        regexp: this.data.inputvalue,
        options: 'i' // 不区分大小写
      })
    }).get().then(res => {
      this.setData({
        searchgoodsList: res.data
      })
    })
  },
  searchTest() {
    wx.request({
      url: 'http://localhost:3000/user/dish/getdishbydishname',
      method: "post",
      data: {
        name: this.data.inputvalue,
      },
      success: (resp) => {
        console.log(resp.data)
        this.setData({
          searchgoodsList: resp.data.data
        })
      },
      fail(error) {
        console.log(error)
      }
    })
  },
  //跳转购物界面
  toContentDetail(event) {
    let id = event.currentTarget.dataset.id; // 获取点击元素的id
    wx.navigateTo({
      url: '/pages/index/contentDetail/contentDetail?id=' + id, // 跳转到目标页面
    })
  },
})