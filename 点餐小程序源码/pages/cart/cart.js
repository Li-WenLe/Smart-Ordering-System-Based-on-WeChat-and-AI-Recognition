const app=getApp()
Page({
  data: {
    choose:false,
    cartList:app.globalData.cartList,
    sum: 0
  },

  onLoad:function(options) {
    this.setData({
      cartList: app.globalData.cartList,
      sum: app.globalData.sum || 0
    });
    this.allprice();
  },
  onShow(){
    console.log(this.data.cartList);
    this.setData({
      cartList:app.globalData.cartList,
      sum:app.globalData.sum,
    })
  },
  // add(event){
  //   console.log(event.currentTarget.dataset.index)
  //   let index=event.currentTarget.dataset.index
  //   this.data.cartList[index].number=this.data.cartList[index].number+1
  //   this.setData({
  //     cartList:this.data.cartList,
  //   })
  //   this.allprice()
  //   //更新数据到全局和缓存里的购物车列表数据
  //   app.globalData.cartList=this.data.cartList
  //   wx.setStorageSync('cartList', this.data.cartList)
  //   app.globalData.sum=this.data.sum
  //   wx.setStorageSync('sum', this.data.sum)
  // },
  add(event) {
    console.log(event.currentTarget.dataset.index);
    let index = event.currentTarget.dataset.index;
    let newCartList = [...this.data.cartList];
    newCartList[index].number = newCartList[index].number + 1;
    this.setData({
      cartList: newCartList
    });
    this.allprice();
    // 更新数据到全局和缓存里的购物车列表数据
    app.globalData.cartList = newCartList;
    wx.setStorageSync('cartList', newCartList);
    app.globalData.sum = this.data.sum;
    wx.setStorageSync('sum', this.data.sum);
  },
  reduce(event){
    console.log(event.currentTarget.dataset.index)
    let index=event.currentTarget.dataset.index
    //如果购买量是0，点击之后数量会一直保持在0
    if(this.data.cartList[index].number==0){
      this.data.cartList[index].number=0;
    }else{
      this.data.cartList[index].number=this.data.cartList[index].number-1
    }
    this.setData({
      cartList:this.data.cartList
    })
    this.allprice()
    //更新数据到全局和缓存里的购物车列表数据
    app.globalData.cartList=this.data.cartList
    wx.setStorageSync('cartList', this.data.cartList)
    app.globalData.sum=this.data.sum
    wx.setStorageSync('sum', this.data.sum)
  },
  chooseturn(event){
    let index=event.currentTarget.dataset.index
    this.data.cartList[index].choose=!this.data.cartList[index].choose
    this.setData({
      cartList:this.data.cartList
    })
    // 重新计算总价
    this.allprice();
     //更新数据到全局和缓存里的购物车列表数据
     app.globalData.cartList=this.data.cartList
     wx.setStorageSync('cartList', this.data.cartList)
     app.globalData.sum=this.data.sum
     wx.setStorageSync('sum', this.data.sum)
  },
  toContentDetail(event) {
    let id = event.currentTarget.dataset.id; // 获取点击元素的id
    wx.navigateTo({
      url: '/pages/index/contentDetail/contentDetail?id=' + id, // 跳转到目标页面
    })
  },
  turn(event) {
    let chooseok =! this.data.chooseok;
    let cartList = this.data.cartList.map(item => {
      item.choose = chooseok;
      return item;
    });
    this.setData({
      cartList: cartList,
      chooseok: chooseok
    });
    // 重新计算总价
    this.allprice();
    //存储总价到缓存
    app.globalData.sum=this.data.sum
    wx.setStorageSync('sum', this.data.sum)
  },
  allprice() {
    let sum = 0;
    // 遍历购物车列表，计算总价
    for (let index = 0; index < this.data.cartList.length; index++) {
      let item = this.data.cartList[index];
      if(item.choose==true){
        sum += item.number * item.price;
      }
  }
    // 更新总价
    this.setData({
      sum: sum.toFixed(2)
    });
    //存储总价到缓存
    app.globalData.sum=this.data.sum
    wx.setStorageSync('sum', this.data.sum)
  },
  toOrder() {
    const orderList = this.data.cartList.filter(item => item.choose);
    app.globalData.orderList = orderList;
    app.globalData.sum = this.data.sum;
    
    wx.navigateTo({
      url: '/pages/order/order',
    });
  },
  getGoodsImage(item) {
    // 如果是套餐且有cover属性
    if (item.isSetMeal && item.cover) {
      return item.cover;
    }
    // 普通商品或有image属性的商品
    return item.image || '/images/default-goods.png'; // 默认图片
  }
})