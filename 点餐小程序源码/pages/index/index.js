
// index.js
Page({
  data: {

  },
  //生命周期函数，监听页面加载
  onLoad: function (options) {
    //this.getBanners()
    this.getBannersTest()
    //this.getContentList()
    //this.getOnshowList()
    this.getOnshowListTest()
    //this.getBannerOnshowList()
    this.getBannerOnshowListTest()

  },

  //获取轮播图数据库记录
  /*getBanners() {
    wx.cloud.database().collection('banner').get()
      .then(res => {
        this.setData({
          bannerList: res.data
        });
      })
      .catch(err => {
        console.error('获取数据失败', err);
      });
  },*/

  getBannersTest() {
    wx.request({
      url: 'http://localhost:3000/user/banner',
      method: "get",
      success: (resp) => {
        console.log(resp.data)
        this.setData({
          bannerList: resp.data.data
        })
      },
      fail(error) {
        console.log(error)
      }
    })
  },
  //点击轮播图跳转至详情页面
  toBannerDetail(event) {
    let id = event.currentTarget.dataset.id; // 获取点击元素的id
    console.log(id)
    wx.navigateTo({
      url: '/pages/index/bannerDetail/bannerDetail?id=' + id, // 跳转到目标页面
    })
  },
  //获取首页商品列表数据
  getContentList() {
    wx.cloud.database().collection('goods').get()
      .then(res => {
        this.setData({
          contentList: res.data
        });
      })
      .catch(err => {
        console.error('获取数据失败', err);
      });
  },
  //点击商品跳转
  toContentDetail(event) {
    let id = event.currentTarget.dataset.id; // 获取点击元素的id
    wx.navigateTo({
      url: '/pages/index/contentDetail/contentDetail?id=' + id, // 跳转到目标页面
    })
  },
  //获取首页商品列表数据
  getOnshowList() {
    wx.cloud.database().collection('goods')
      .where({
        booleanshow: true
      })
      .get()
      .then(res => {
        console.log(res)
        this.setData({
          onshowList: res.data
        });
      })
      .catch(err => {
        console.error('获取数据失败', err);
      });
  },
  getOnshowListTest() {
    wx.request({
      url: 'http://localhost:3000/user/dish/onshow',
      method: "get",
      success: (resp) => {
        console.log(resp.data)
        this.setData({
          onshowList: resp.data.data
        })
      },
      fail(error) {
        console.log(error)
      }
    })
  },
  getBannerOnshowList() {
    wx.cloud.database().collection('goods-type')
      .where({
        booleanshow: true
      })
      .get()
      .then(res => {
        console.log(res)
        this.setData({
          onBannershowList: res.data
        });
      })
      .catch(err => {
        console.error('获取数据失败', err);
      });
  },
  toTypeDetail(event) {
    let type = event.currentTarget.dataset.type; // 获取点击的type
    console.log(type);  // 确认获取的type值是否正确
    wx.navigateTo({
      url: '/pages/index/typeDetail/typeDetail?type=' + type,  // 注意这里的type传递
    });
  },
  tosearchDetail() {
    wx.navigateTo({
      url: '/pages/index/searchDetail/searchDetail',
    })
  },
  turntochatpage() {
    wx.navigateTo({
      url: '/pages/chatAI/chatAI',
    })
  },
  turntophoto() {
    wx.navigateTo({
      url: '/pages/testai/testai'
    })
  },
  turntodishtop() {
    wx.navigateTo({
      url: '/pages/dishtop/dishtop',
    })
  },
  getBannerOnshowListTest() {
    wx.request({
      url: 'http://localhost:3000/user/dishtype',
      method: 'GET',
      success: (res) => {
        this.setData({
          onBannerShowList: res.data.data
        });
        console.log('设置后的数据:', this.data.onBannerShowList);
      },
      fail: (err) => {
        console.error('请求失败:', err);
        wx.showToast({
          title: '网络请求失败',
          icon: 'none'
        });
      }
    });
  }
})
