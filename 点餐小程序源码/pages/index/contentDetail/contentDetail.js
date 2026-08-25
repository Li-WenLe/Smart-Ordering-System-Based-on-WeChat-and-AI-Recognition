const app = getApp()
Page({

  data: {

  },

  onLoad(options) {
    let id = options.id;
    console.log(id)
    wx.request({
      url: 'http://localhost:3000/user/dish/getbyid',
      method: "get",
      header: { 'content-type': 'application/x-www-form-urlencoded' },
      data: {
        dishId:parseInt(id),
      },
      success: (resp) => {
        console.log(resp.data.data)
        this.setData({
          contentList: resp.data.data
        })
      }
    })
    //设置购物车商品数量
    console.log(app.globalData.cartList)
    this.setData({
      cartList: app.globalData.cartList
    })
  },
  onShareAppMessage() {
    return {
      title: this.data.contentList.title,
      path: '/pages/index/contentDetail/contentDetail?id=' + this.data.contentList._id,
      imageUrl: this.data.contentList.cover
    }
  },
  //添加商品到购物车
  addCart() {
    let cartList = app.globalData.cartList
    let index = -1
    if (cartList.length == 0) {
      this.data.contentList.number = 1;
      this.data.contentList.choose = false//默认是选中状态
      app.globalData.cartList.push(this.data.contentList)
      wx.setStorageSync('cartList', app.globalData.cartList)
    } else {
      for (let idx in cartList) {
        console.log(idx)
        if (cartList[idx].id == this.data.contentList.id) {
          index = idx
        }
      }
      if (index != -1) {
        cartList[index].number = cartList[index].number + 1
        app.globalData.cartList = cartList
        wx.setStorageSync('cartList', app.globalData.cartList)
      } else {
        this.data.contentList.choose = true
        //默认是选中状态
        this.data.contentList.number = 1
        app.globalData.cartList.push(this.data.contentList)
        wx.setStorageSync('cartList', app.globalData.cartList)
      }
    }
    wx.showToast({
      title: '添加成功',
    })
    this.setData({
      cartList: app.globalData.cartList
    })
  },
  toOrderDetail() {
    let reorderList = [this.data.contentList];  // 将 contentList 包装成一个数组
    // 如果没有数量字段，默认为 1
    if (!reorderList[0].number) {
      reorderList[0].number = 1;
    }
    // 将 reorderList 存储到全局数据
    app.globalData.reorderList = reorderList;
    // 更新页面的数据，确保重新渲染
    this.setData({
      reorderList: app.globalData.reorderList
    });
    // 跳转到订单页面
    wx.navigateTo({
      url: '/pages/reorder/reorder',
    });
  },
  turntocart() {
    /*wx.navigateTo({
      url: '/pages/cart/cart', 
    });*/
    wx.switchTab({
      url: '/pages/cart/cart',
      success: () => {
        console.log('页面跳转成功');
      },
      fail: (err) => {
        console.error('页面跳转失败:', err);
        wx.showToast({ title: '页面跳转失败', icon: 'none' });
      }
    });
  }

})