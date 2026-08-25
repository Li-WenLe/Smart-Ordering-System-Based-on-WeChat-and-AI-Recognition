// pages/index/typeDetail/typeDetail.js
Page({

  data: {

  },
  onLoad: function (options) {
    console.log(options)
    //this.getTypeList(options.type)
    this.getTypeListTest(options.type)
  },
  /*getTypeList(typeId){
    console.log(typeId)
    wx.cloud.database().collection('goods')
    .where({
     type:typeId
    })
    .get() 
    .then(res => {
        console.log(res)
        this.setData({
         goodsList: res.data
        });
      })
      .catch(err => {
        console.error('获取数据失败', err);
      });
  },*/
  //从数据库获取菜品信息
  getTypeListTest(typeId) {
    console.log("typeid:", typeId)
    wx.request({
      url: 'http://localhost:3000/user/dish/getdishbytypename',
      method: "post",
      data: {
        name: typeId
      },
      success: (resp) => {
        console.log(resp.data)
        this.setData({
          goodsList: resp.data.data
        });
      }
    })
  },
  toContentDetail(event) {
    let id = event.currentTarget.dataset.id; // 获取点击元素的id
    console.log(id);
    wx.navigateTo({
      url: '/pages/index/contentDetail/contentDetail?id=' + id, // 跳转到目标页面
    })
  },
})