const app = getApp();

Page({
  //页面的初始数据
  data: {
    susername: '',
    phone: '',
    susertype: 3,
  },

  bindinputsusername: function(e) {
    this.setData({
      susername: e.detail.value
    })
  },

  bindinputphone: function(e) {
    this.setData({
      phone: e.detail.value
    })
  },

  //修改个人信息
  formSubmit: function() {
    var that = this;
    //如果openid不存在，就重新请求接口获取openid
    var openid = app.globalData.openid;
    if (openid === null || openid === undefined) {
      app.getOpenid();
      wx.showToast({ //这里提示失败原因
        title: 'openid为空！',
        duration: 1500
      })
      return;
    }

    let susername = that.data.susername;
    let phone = that.data.phone;
    let susertype = 3;

    if (susername == '' || susername == undefined) {
      wx.showToast({
        title: '用户名不能为空',
        icon: 'none'
      })
      return;
    }
    if (phone == '' || phone == undefined) {
      wx.showToast({
        title: '手机号不能为空',
        icon: 'none'
      })
      return;
    }
    wx.request({
      url: app.globalData.baseUrl + '/SUser/save',
      method: "POST",
      header: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      data: {
        openid: openid,
        susername: susername,
        phone: phone,
        susertype: 3
      },
      success(res) {
        console.log("提交susername:  ",susername)
        console.log("提交susertype:  ", susertype)
        if (res && res.data && res.data.data) {
          wx.showToast({
            title: '修改成功',
          })
          app._getMySUserInfo();
        }
        wx.navigateBack();
        // wx.switchTab({
        //  url: '../index/index'
        // })
      },
      fail(res) {
        console.log("提交失败", res)
      }
    })

  },

  //生命周期函数--监听页面加载
  onLoad: function(options) {
    let that = this;
    var openid = app.globalData.openid;
    if (openid === null || openid === undefined) {
      app.getOpenid();
      wx.showToast({ //这里提示失败原因
        title: 'openid为空！',
        duration: 1500
      })
      return;
    }
    console.log("修改个人信息页", app.globalData.suserInfo)
    if (app.globalData.suserInfo) {
      that.setData({
        susername: app.globalData.suserInfo.nickName,
        phone: app.globalData.suserInfo.realphone
      })
    }

  },
})