//app.js
const app = getApp();  
App({
  //创建towxml对象，供小程序页面使用
  globalData: {
    tmplIds: { 
      quhao: 'txbpCYdX_pjw0PXsOJrbCKixdHtJSUr4uyhQQEzAz_4', //取号模板
    },
    adminId:0,//看看点进去哪个商家
    suserInfo: {},
    userInfo: {},
    openid: 'wx999ad287ff832f38',
   
    //cmd+ipconfig，找到无线局域网适配器 WLAN: IPv4 地址的值，修改localhost，注意电脑和手机同一个wifi下！才能在真机调试中渲染出来
   //  baseUrl: 'http://172.20.10.6:8080/diancan' //真机调试
   baseUrl: 'http://localhost:8080/diancan' //本地调试
  
  },
  onLaunch: function () {
    //云开发初始化
    wx.cloud.init({
      env: 'cloud1-3g02ketzf0e2aa52', //这里一定要换成你自己的云开发环境id
      traceUser: true,
    })
    this.getOpenid();
  },


  // 获取用户openid
  getOpenid: function () {
    var app = this;
    var openidStor = wx.getStorageSync('openid');

    // if (openidStor) {
    //   console.log('本地获取openid:' + openidStor);
    //   app.globalData.openid = openidStor;
    //   app._getMyUserInfo();
    // } else {
    wx.cloud.callFunction({
      name: 'getOpenid',
      success(res) {
        console.log('云函数获取openid成功', res.result.openid)
        var openid = res.result.openid;
        wx.setStorageSync('openid', openid)
        app.globalData.openid = openid;
        app._getMySUserInfo();
        app._getMyUserInfo();
      },
      fail(res) {
        console.log('云函数获取失败', res)
      }
    })
    // }
  },
  //获取自己后台的user信息
  _getMyUserInfo() {
    let app = this
    var userStor = wx.getStorageSync('user');
    // if (userStor) {
    //   console.log('本地获取user', userStor)
    //   app.globalData.userInfo = userStor;
    // }
    wx.request({
      url: app.globalData.baseUrl + '/user/getUserInfo',
      data: {
        openid: app.globalData.openid
      },
      success: function (res) {
        console.log("Java后台返回的用户信息", res.data)
        if (res && res.data && res.data.data) {
          app.globalData.userInfo.nickName = res.data.data.username;
          app.globalData.userInfo.realphone = res.data.data.phone;
          app.globalData.userInfo.realzhuohao = res.data.data.zhuohao;
          app.globalData.userInfo.realrenshu = res.data.data.renshu;
          console.log("===app.globalData===", app.globalData.userInfo)
          //缓存到sd卡里
          app._saveUserInfo(app.globalData.userInfo);
        }
      }
    })
  },

  //获取自己后台的suser信息
  _getMySUserInfo() {
    let app = this
    var suserStor = wx.getStorageSync('suser');
   if (suserStor) {
    console.log('本地获取suser', suserStor)
   app.globalData.suserInfo = suserStor;
 }
    wx.request({
      url: app.globalData.baseUrl + '/SUser/getSUserInfo',
      data: {
        openid: app.globalData.openid
      },
      success: function (res) {
        console.log("Java后台返回的用户信息", res.data)
        if (res && res.data && res.data.data) {
          app.globalData.suserInfo.nickName = res.data.data.susername;
          app.globalData.suserInfo.realphone = res.data.data.phone;
        
          console.log("===app.globalData===", app.globalData.suserInfo)
          //缓存到sd卡里
          app._saveSUserInfo(app.globalData.suserInfo);
        }
      }
    })
  },



  _checkOpenid() {
    let app = this
    let openid = this.globalData.openid;
    if (!openid) {
      app.getOpenid();
      wx.showLoading({
        title: 'openid不能为空，请重新登录',
      })
      return null;
    } else {
      return openid;
    }
  },
  // 保存userinfo
  _saveUserInfo: function (user) {
    this.globalData.userInfo = user;
    wx.setStorageSync('user', user)
  },


   // 保存suserinfo
   _saveSUserInfo: function (suser) {
    this.globalData.suserInfo = suser;
    wx.setStorageSync('suser', suser)
  },



  //获取今天是本月第几周
  _getWeek: function () {
    // 将字符串转为标准时间格式
    let date = new Date();
    let month = date.getMonth() + 1;
    let week = this.getWeekFromDate(date);
    if (week === 0) { //第0周归于上月的最后一周
      month = date.getMonth();
      let dateLast = new Date();
      let dayLast = new Date(dateLast.getFullYear(), dateLast.getMonth(), 0).getDate();
      let timestamp = new Date(new Date().getFullYear(), new Date().getMonth() - 1, dayLast);
      week = this.getWeekFromDate(new Date(timestamp));
    }
    let time = month + "月第" + week + "周";
    return time;
  },

  getWeekFromDate: function (date) {
    // 将字符串转为标准时间格式
    let w = date.getDay(); //周几
    if (w === 0) {
      w = 7;
    }
    let week = Math.ceil((date.getDate() + 6 - w) / 7) - 1;
    return week;
  },
  // 获取当前时间
  _getCurrentTime() {
    var d = new Date();
    var month = d.getMonth() + 1;
    var date = d.getDate();
    var day = d.getDay();
    var hours = d.getHours();
    var minutes = d.getMinutes();
    var seconds = d.getSeconds();
    var ms = d.getMilliseconds();

    var curDateTime = d.getFullYear() + '年';
    if (month > 9)
      curDateTime += month + '月';
    else
      curDateTime += month + '月';

    if (date > 9)
      curDateTime = curDateTime + date + "日";
    else
      curDateTime = curDateTime + date + "日";
    if (hours > 9)
      curDateTime = curDateTime + hours + "时";
    else
      curDateTime = curDateTime + hours + "时";
    if (minutes > 9)
      curDateTime = curDateTime + minutes + "分";
    else
      curDateTime = curDateTime + minutes + "分";
    return curDateTime;
  }
})