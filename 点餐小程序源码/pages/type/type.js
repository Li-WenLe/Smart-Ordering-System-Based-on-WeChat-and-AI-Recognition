// pages/type/type.js
const app = getApp();

Page({
  data: {
    currentType: 0,          // 当前选中的分类索引
    typeList: [],            // 商品分类列表
    goodsList: [],           // 商品列表
    setMealList: [],         // 套餐分类列表
    setMealGoodsList: [],    // 套餐包含的菜品列表
    visible: false,          // 弹窗显示控制
    boolSetMeal: false,      // 是否显示套餐
    setMeal: {},             // 当前选中的套餐信息
    cartList: []             // 购物车列表
  },

  onLoad(options) {
    this.initCartData();     // 初始化购物车数据
    this.getTypeListTest();  // 获取商品分类
    this.getSetMeal();       // 获取套餐分类
  },

  // 初始化购物车数据
  initCartData() {
    const cartList = wx.getStorageSync('cartList') || [];
    app.globalData.cartList = cartList;
    this.setData({ cartList });
  },

  // 获取套餐分类列表
  getSetMeal() {
    wx.request({
      url: 'http://localhost:3000/user/setmeal/all',
      method: "post",
      success: (res) => {
        console.log(res.data)
        this.setData({
          setMealList: res.data.data
        });
      },
      fail: (err) => {
        console.error('获取套餐分类失败:', err);
      }
    });
  },

  // 获取商品分类列表
  getTypeListTest() {
    wx.request({
      url: 'http://localhost:3000/user/dishtype/all',
      method: "post",
      success: (res) => {
        this.setData({
          typeList: res.data.data
        });
      },
      fail: (err) => {
        console.error('获取商品分类失败:', err);
      }
    });
  },

  // 获取指定分类的商品列表
  getTypeGoodsListTest(event) {
    const type = event.currentTarget.dataset.type;
    const index = event.currentTarget.dataset.index;

    this.setData({
      currentType: index,
      boolSetMeal: false
    });

    wx.request({
      url: 'http://localhost:3000/user/dish/getdishbytypename',
      method: "post",
      data: { name: type },
      success: (res) => {
        this.setData({
          goodsList: res.data.data
        });
      },
      fail: (err) => {
        console.error('获取商品列表失败:', err);
      }
    });
  },

  // 获取指定分类的套餐列表
  getSetMealGoodsListTest(event) {
    const type = event.currentTarget.dataset.type;
    const index = event.currentTarget.dataset.index;

    this.setData({
      currentType: index,
      boolSetMeal: true
    });

    wx.request({
      url: 'http://localhost:3000/user/setmeal',
      method: "post",
      data: { name: type },
      success: (res) => {
        this.setData({
          setMealGoodsList: res.data.data
        });
      },
      fail: (err) => {
        console.error('获取套餐列表失败:', err);
      }
    });
  },

  // 跳转到商品详情页
  toGoodsDetail(event) {
    const id = event.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/index/contentDetail/contentDetail?id=${id}`,
    });
  },

  // 显示套餐详情弹窗
  toSetMealGoodsDetail(event) {
    const id = event.currentTarget.dataset.id;

    this.setData({
      visible: true
    });

    // 获取套餐包含的菜品
    wx.request({
      url: 'http://localhost:3000/user/setmealdish/getbysetmealid',
      method: "post",
      data: { setmealId: id },
      success: (res) => {
        this.setData({
          setMealGoodsList: res.data.data
        });
        // 获取套餐基本信息
        this.getSetMealById(id);
      },
      fail: (err) => {
        console.error('获取套餐详情失败:', err);
      }
    });
  },

  // 获取套餐基本信息
  getSetMealById(id) {
    wx.request({
      url: 'http://localhost:3000/user/setmeal/getbyid',
      method: "post",
      data: { id },
      success: (res) => {
        this.setData({
          setMeal: res.data.data
        });
      },
      fail: (err) => {
        console.error('获取套餐信息失败:', err);
      }
    });
  },

  // 确认加入购物车
  onConfirm(event) {

    this.addCart(event);
  },

  // 加入购物车
  addCart(event) {
    const id = event.currentTarget.dataset.id;
    const { setMeal, setMealGoodsList, cartList } = this.data;

    // 检查是否已存在该套餐
    const existingIndex = cartList.findIndex(item => item.id === id && item.isSetMeal);

    if (existingIndex >= 0) {
      // 已存在，增加数量
      cartList[existingIndex].number += 1;
    } else {
      // 新套餐，添加到购物车
      const setMealItem = {
        id: setMeal.id,
        name: setMeal.name,
        price: setMeal.price,
        cover: setMeal.cover,
        number: 1,
        isSetMeal: true,
        dishes: setMealGoodsList,
        choose: true
      };
      cartList.push(setMealItem);
    }

    // 更新数据
    this.setData({ cartList });
    app.globalData.cartList = cartList;
    wx.setStorageSync('cartList', cartList);

    // 显示成功提示
    wx.showToast({
      title: '添加成功',
      icon: 'success'
    });

    // 关闭弹窗
    this.setData({ visible: false });
  },

  // 关闭弹窗
  onClose() {
    this.setData({
      visible: false
    });
  }
});