const app = getApp();
Page({
  data: {
    orderList: null, // 订单商品列表
    sum: Number(app.globalData.sum), // 订单总金额
    status: false, // 订单状态
    nameandphonenumber: null, // 收货人的姓名和电话
    note: null, // 订单备注
    addressInfo: [], // 收货地址信息
    validVouchers: [], // 可用优惠券列表
    selectedVoucher: null, // 当前选中的优惠券
    showVoucherPopup: false, // 是否显示优惠券弹窗
    finalAmount: 0, // 实际支付金额
    isDataLoaded: false, // 数据是否加载完成的标志位
    orderId: '' // 订单id
  },

  onLoad(options) {
    //this. getAddressLocation();
    this.getInfo();
    this.getAllVoucherData();
    const orderList = app.globalData.cartList.filter(item => item.choose);
    const originalSum = orderList.reduce((total, item) => total + (item.price * item.number), 0);
    this.setData({
      sum: originalSum,
      finalAmount: (originalSum + 4).toFixed(2),
      orderList
    });
    console.log(this.data.finalAmount);
  },

  // 获取收货地址
  getInfo() {
    wx.request({
      url: 'http://localhost:3000/user/address/get',
      data: { id: app.globalData.loginuser.id },
      success: (resp) => {
        this.setData({ addressInfo: resp.data.data });
      },
      fail: (error) => {
        wx.showToast({ title: '获取地址失败', icon: 'none' });
      }
    });
  },

  // 合并请求获取优惠券数据
  getAllVoucherData() {
    return new Promise((resolve, reject) => {
      wx.showLoading({ title: '加载中...' });
      Promise.all([
        this.getUserVouchers(app.globalData.loginuser.id),
        this.getAllVoucherInfo()
      ]).then(() => {
        this.combineData();
        wx.hideLoading();
        this.setData({ isDataLoaded: true });
        resolve();
      }).catch(err => {
        console.error(err);
        wx.hideLoading();
        reject(err);
      });
    });
  },

  // 获取用户拥有的优惠券数量
  getUserVouchers(userId) {
    return new Promise((resolve, reject) => {
      wx.request({
        url: 'http://localhost:3000/user/voucher',
        method: "POST",
        data: { userId },
        success: (res) => {
          this.setData({ voucherList: res.data.data });
          resolve();
        },
        fail: reject
      });
    });
  },

  // 获取所有优惠券详情
  getAllVoucherInfo() {
    return new Promise((resolve, reject) => {
      wx.request({
        url: 'http://localhost:3000/user/voucher/getall',
        method: "POST",
        success: (res) => {
          this.setData({ voucherInfo: res.data.data });
          resolve();
        },
        fail: reject
      });
    });
  },

  // 合并数据并判断过期状态
  combineData() {
    const { voucherList, voucherInfo } = this.data;
    const combined = voucherList.map(item => {
      const detail = voucherInfo.find(v => v.id === item.voucherId) || {};
      const amount = detail.title ? detail.title.match(/\d+/)?.[0] : "0";
      const isExpired = this.checkExpired(detail.usedTime);

      return {
        ...item,
        ...detail,
        amount,
        isExpired
      };
    });

    this.setData({
      validVouchers: combined.filter(item => !item.isExpired),
      expiredVouchers: combined.filter(item => item.isExpired)
    });
  },

  // 检查是否过期
  checkExpired(expiryDate) {
    if (!expiryDate) return true;
    return new Date(expiryDate).getTime() < Date.now();
  },

  // 选择优惠券
  selectVoucher(e) {
    const voucher = e.currentTarget.dataset.voucher;
    const discount = parseFloat(voucher.amount);
    const finalAmount = (this.data.sum + 4 - discount).toFixed(2);

    this.setData({
      selectedVoucher: voucher,
      finalAmount: finalAmount > 0 ? finalAmount : 0,
      showVoucherPopup: false
    });
  },

  // 取消选择优惠券
  cancelVoucher() {
    this.setData({
      selectedVoucher: null,
      finalAmount: (this.data.sum + 4).toFixed(2),
      showVoucherPopup: false
    });
  },

  openVoucherSelect() {
    this.setData({ showVoucherPopup: true });
  },

  // 提交订单
  addOrderandpay() {
    if (!app.globalData.loginuser) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }

    if (!this.data.isDataLoaded) {
      wx.showToast({ title: '数据加载中，请稍后...', icon: 'none' });
      return;
    }
    this.pay();
  },

  // 支付逻辑
  pay() {
    wx.showModal({
      title: '支付确认',
      content: `合计支付：¥${this.data.finalAmount}`,
      success: (res) => {
        if (res.confirm) {
          this.addOrder();
        } else if (res.cancel) {
          this.addReOrder();
        }
      }
    });
  },

  // 添加订单
  async addOrder() {
    try {
      const addOrderRes = await new Promise((resolve, reject) => {
        wx.request({
          url: 'http://localhost:3000/user/order/add',
          method: "post",
          data: {
            name: app.globalData.loginuser.name,
            phone: app.globalData.loginuser.phone,
            total: Number((this.data.sum + 4).toFixed(2)),
            payedTotal: Number(this.data.finalAmount),
            userId: app.globalData.loginuser.id,
            address: this.data.addressInfo.region + this.data.addressInfo.detailAddress,
            status: 2,
            orderTime: new Date().toISOString().split('.')[0],
          },
          success: (res) => resolve(res),
          fail: reject
        });
      });

      if (this.data.selectedVoucher) {
        await this.updateVoucherStatus();
      }
      wx.showToast({ title: '支付成功', icon: 'success' });

      // 获取最新订单id
      const orderId = await new Promise((resolve, reject) => {
        wx.request({
          url: 'http://localhost:3000/user/order/getnew',
          method: "POST",
          data: { userId: app.globalData.loginuser.id },
          success: (res) => resolve(res.data.data),
          fail: reject
        });
      });
      this.setData({ orderId });
      this.addOrderDetail(orderId);
      this.clearCart();
    } catch (err) {
      wx.showToast({ title: '支付失败', icon: 'none' });
      console.error('支付失败:', err);
    }
  },
  async addReOrder() {
    try {
      // 先保存订单列表，防止后续被清空
      const orderListCopy = [...this.data.orderList];

      const res = await new Promise((resolve, reject) => {
        const now = new Date();
        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0');
        const day = String(now.getDate()).padStart(2, '0');
        const hours = String(now.getHours()).padStart(2, '0');
        const minutes = String(now.getMinutes()).padStart(2, '0');
        const seconds = String(now.getSeconds()).padStart(2, '0');
        const formattedDate = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;

        wx.request({
          url: 'http://localhost:3000/user/order/add',
          method: "post",
          data: {
            name: app.globalData.loginuser.name,
            phone: app.globalData.loginuser.phone,
            total: Number((this.data.sum + 4).toFixed(2)),
            payedTotal: Number(this.data.finalAmount),
            userId: app.globalData.loginuser.id,
            address: this.data.addressInfo.region + this.data.addressInfo.detailAddress,
            status: 1, // 1表示未支付状态
            orderTime: new Date().toISOString().split('.')[0],
          },
          success: resolve,
          fail: reject
        });
      });

      // 获取最新订单id
      const orderId = await new Promise((resolve, reject) => {
        wx.request({
          url: 'http://localhost:3000/user/order/getnew',
          method: "POST",
          data: { userId: app.globalData.loginuser.id },
          success: (res) => resolve(res.data.data),
          fail: reject
        });
      });

      this.setData({ orderId });

      // 添加订单详情
      const orderDetails = orderListCopy.map(item => ({
        orderId: orderId,
        dishId: item.id,
        dishname: item.name,
        image: item.image,
        number: item.number,
        acount: item.price
      }));

      await new Promise((resolve, reject) => {
        wx.request({
          url: 'http://localhost:3000/user/order/addorderdetail',
          method: 'POST',
          data: orderDetails,
          success: resolve,
          fail: reject
        });
      });

      // 清空购物车
      this.clearCart();
      wx.showToast({ title: '已取消支付订单', icon: 'success' });

    } catch (err) {
      wx.showToast({ title: '操作失败', icon: 'none' });
      console.error('操作失败:', err);
    }
  },
  // async addReOrder() {
  //   try {
  //     // 先保存订单列表，防止后续被清空
  //     const orderListCopy = [...this.data.orderList];

  //     const res = await new Promise((resolve, reject) => {
  //       const now = new Date();
  //       const year = now.getFullYear();
  //       const month = String(now.getMonth() + 1).padStart(2, '0');
  //       const day = String(now.getDate()).padStart(2, '0');
  //       const hours = String(now.getHours()).padStart(2, '0');
  //       const minutes = String(now.getMinutes()).padStart(2, '0');
  //       const seconds = String(now.getSeconds()).padStart(2, '0');
  //       const formattedDate = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
  //       console.log(formattedDate)
  //       wx.request({
  //         url: 'http://localhost:3000/user/order/add',
  //         method: "post",
  //         data: {
  //           name: app.globalData.loginuser.name,
  //           phone: app.globalData.loginuser.phone,
  //           total: Number((this.data.sum + 4).toFixed(2)),
  //           payedTotal: Number(this.data.finalAmount),
  //           userId: app.globalData.loginuser.id,
  //           address: this.data.addressInfo.region + this.data.addressInfo.detailAddress,
  //           status: 1,
  //           orderTime: formattedDate
  //         },
  //         success: resolve,
  //         fail: reject
  //       });
  //     });

  //     if (this.data.selectedVoucher) {
  //       await this.updateVoucherStatus();
  //     }

  //     // 获取最新订单id
  //     const orderId = await new Promise((resolve, reject) => {
  //       wx.request({
  //         url: 'http://localhost:3000/user/order/getnew',
  //         method: "POST",
  //         data: { userId: app.globalData.loginuser.id },
  //         success: (res) => resolve(res.data.data),
  //         fail: reject
  //       });
  //     });

  //     this.setData({ orderId });

  //     // 使用之前保存的orderListCopy
  //     const orderDetails = orderListCopy.map(item => ({
  //       orderId: orderId,
  //       dishId: item.id,
  //       dishname: item.name,
  //       image: item.image,
  //       number: item.number,
  //       acount: item.price
  //     }));

  //     // 添加订单详情
  //     await new Promise((resolve, reject) => {
  //       wx.request({
  //         url: 'http://localhost:3000/user/order/addorderdetail',
  //         method: 'POST',
  //         data: orderDetails,
  //         success: resolve,
  //         fail: reject
  //       });
  //     });

  //     // 最后清空购物车
  //     this.clearCart();
  //     wx.showToast({ title: '已取消支付', icon: 'success' });

  //   } catch (err) {
  //     wx.showToast({ title: '操作失败', icon: 'none' });
  //     console.error('操作失败:', err);
  //   }
  // },

  // 添加订单详情
  addOrderDetail(orderId) {
    // Prepare order details data
    console.log(this.data.orderList);
    console.log(this.data.orderId);
    const orderDetails = this.data.orderList.map(item => ({
      orderId: orderId,
      dishId: item.id,
      dishname: item.name,
      image: item.image,
      number: item.number,
      acount: item.price
    }));
    console.log(orderDetails);
    wx.request({
      url: 'http://localhost:3000/user/order/addorderdetail',
      method: 'POST',
      data: orderDetails,
      success: (res) => {
        console.log('订单详情添加成功', res);
      },
      fail: (err) => {
        console.error('订单详情添加失败', err);
      }
    });
  },

  // 更新优惠券状态
  updateVoucherStatus() {
    return new Promise((resolve, reject) => {
      wx.request({
        url: 'http://localhost:3000/user/voucher/use',
        method: 'POST',
        data: {
          voucherId: this.data.selectedVoucher.id,
          userId: app.globalData.loginuser.id
        },
        success: (res) => {
          console.log('优惠券状态更新成功', res);
          resolve(res);
        },
        fail: (err) => {
          console.error('优惠券状态更新失败', err);
          reject(err);
        }
      });
    });
  },
  // 清空购物车
  clearCart() {
    app.globalData.cartList = [];
    app.globalData.sum = 0;
    this.setData({
      orderList: [],
      sum: 0
    });
  },
  /*// 获取地址的经纬度
  getAddressLocation() {
   const address = this.addressInfo.region+this.data.addressInfo.detailAddress;
   qqmapsdk.geocoder({
       address: address,
       complete: (res) => {
           if (res.status === 0) {
               const addressLat = res.result.location.lat;
               const addressLng = res.result.location.lng;
               // 假设店铺位于河南工学院内，获取其经纬度（可通过地图工具获取固定值）
               const shopLat = 35.2090, shopLng = 113.8744; // 这里是示例经纬度，你需要替换为实际的河南工学院内店铺经纬度
               console.log(addressLat);
               console.log(addressLng);
           } else {
               console.error('获取地址经纬度失败', res);
           }
       }
   });
},*/
});