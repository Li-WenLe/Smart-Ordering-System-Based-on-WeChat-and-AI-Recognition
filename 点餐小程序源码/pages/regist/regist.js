const app = App()
Page({
  data: {
    username: '',
    password: '',
    repassword: '',
    name: '',
    phone: '',
  },

  turntologin() {
    wx.navigateTo({
      url: '/pages/login/login'
    })
  },
  // 绑定用户名输入
  bindUsernameInput(e) {
    this.setData({
      username: e.detail.value,
    });
  },
  bindNameInput(e) {
    this.setData({
      name: e.detail.value,
    })
  },
  bindPhoneInput(e) {
    this.setData({
      phone: e.detail.value,
    })
  },
  // 绑定密码输入
  bindPasswordInput(e) {
    this.setData({
      password: e.detail.value,
    });
  },

  // 绑定确认密码输入
  bindRepasswordInput(e) {
    this.setData({
      repassword: e.detail.value,
    });
  },

  // 表单提交
  formSubmit() {
    const { username, password, repassword, phone } = this.data;
    console.log(this.data)
    // 检查用户名和密码是否为空
    if (username === '' || password === '') {
      wx.showToast({
        title: '用户名或密码不能为空',
        icon: 'none',
      });
      return;
    }

    // 检查两次输入的密码是否一致
    if (password != repassword) {
      wx.showToast({
        title: '两次输入的密码不一致',
        icon: 'none',
      });
      return;
    }
    if (phone.length != 11) {
      wx.showToast({
        title: '手机号不合法',
        icon: 'none',
      });
      return;
    }

    wx.request({
      url: 'http://localhost:3000/user/regist',
      method: "Post",
      data: {
        name: this.data.name,
        username: this.data.username,
        phone: this.data.phone,
        password: this.data.password,
      },
      success: (resp) => {
        console.log(resp)
        if (resp.data.msg == null) {
          wx.showToast({
            title: '注册成功',
          })
          wx.navigateTo({
            url: '/pages/login/login',
          })
        } else {
          wx.showToast({
            title: resp.data.msg,
            icon: 'none',
          });
        }
      },
    })
  },
});