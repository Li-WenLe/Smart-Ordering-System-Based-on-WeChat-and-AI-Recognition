// 页面 JS 文件
const app = getApp();

Page({
  data: {
    username: '', // 用户名
    password: '', // 密码
    userAvatar: '/images/未登录.png', // 默认头像
  },

  // 绑定用户名输入
  bindUsernameInput(e) {
    this.setData({
      username: e.detail.value,
    });
  },

  // 绑定密码输入
  bindPasswordInput(e) {
    this.setData({
      password: e.detail.value,
    });
  },

  // 表单提交
  formSubmit() {
    const { username, password } = this.data;

    if (!username || !password) {
      wx.showToast({
        title: '用户名和密码不能为空',
        icon: 'none',
      });
      return;
    }
    wx.request({
      url: 'http://localhost:3000/user/login',
      method: "post",
      data: {
        username: username,
        password: password
      },
      success(resp) {
        console.log(resp)
        const userInfo = resp.data.data;
        app.globalData.loginuser = userInfo;
        console.log(app.globalData.loginuser)
        app.globalData.booleanlogin = true;
        if (resp.data.data != null) {
          wx.showToast({
            title: '登录成功',
            icon: 'success',
          });
          wx.switchTab({
            url: '/pages/index/index',
          });
        } else {
          wx.showToast({
            title: '用户名或密码错误',
            icon: 'none',
          });
        }
      },
    })
    /*wx.cloud.database().collection('user')
      .where({
        username: username,
        password: password,
      })
      .get()
      .then(res => {
        if (res.data.length > 0) {
          const userInfo = res.data[0];
          app.globalData.loginuser = userInfo; // 存储用户信息到全局变量
          app.globalData.booleanlogin=true;
          console.log(userInfo)
          // 更新页面数据，显示用户头像
          this.setData({
            userAvatar: userInfo.avatar || '/images/未登录.png', // 假设用户信息中有 avatar 字段
          });

          wx.showToast({
            title: '登录成功',
            icon: 'success',
          });

          // 登录成功后跳转到首页或其他页面
          wx.switchTab({
            url: '/pages/index/index',
          });
        } else {
          wx.showToast({
            title: '用户名或密码错误',
            icon: 'none',
          });
        }
      })
      .catch(err => {
        console.error('获取数据失败', err);
        wx.showToast({
          title: '登录失败，请重试',
          icon: 'none',
        });
      });*/
  },
  turntoregist() {
    wx.navigateTo({
      url: '/pages/regist/regist',
    })
  }
});