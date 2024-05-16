// pages/me/me.js
const app = getApp();
Page({

  // 页面的初始数据
  data: {   /* 
   isShowUserName: false,
    userInfo: null,
    */
 isShowSUserName: false,
   suserInfo: null,
 //  susername: '',
  },
 /* 
  // button获取用户信息
 onGotUserInfo: function (e) {
   if (e.detail.userInfo) {
      var user = e.detail.userInfo;
      this.setData({
        isShowUserName: true,
        userInfo: e.detail.userInfo,
      })
      user.openid = app.globalData.openid;
      app._saveUserInfo(user);
    } else {
      app._showSettingToast('登陆需要允许授权');
    }

*/
onGotSUserInfo: function (e) {
    if (e.detail.suserInfo) {
        var suser = e.detail.suserInfo;
    //    var Nickname1 =e.detail.suserInfo.nickName;
        this.setData({
          isShowSUserName: true,
          suserInfo: e.detail.suserInfo,
          //susername: e.detail.suserInfo.susername,  
         // susername: app.globalData.suserInfo.susername,
     //    Nickname1: e.detail.suserInfo.nickName,
        })
       // suser.susername= app.globalData.suserInfo.nickName,
        suser.openid = app.globalData.openid;
     //   Nickname1: app.globalData.suserInfo.nickName,
        app._saveSUserInfo(suser);
      } else {
        app._showSettingToast('登陆需要允许授权');
      }



  },

 /*
//新方法
getUserProfile(){
  wx.getUserProfile({
    desc:'用于完善会员资料',
    success:(res) =>{
      console.log("获取用户信息成功",res)
      let user = res.userInfo
      wx.setStorageSync('user', user)
      this.setData({
        isShowUserName: true,
        userInfo: user,
      })
    },
    fail: res => {
       console.log("获取用户信息失败",res)
    }
  })
},

*/
//新方法 getSUserProfile

getUserProfile(){
    wx.getUserProfile({
      desc:'用于完善会员资料',
      success:(res) =>{
        console.log("获取用户信息成功",res)
        let suser = res.suserInfo
        wx.setStorageSync('suser', suser)
        this.setData({
          isShowSUserName: true,
          suserInfo: suser,
        })
      },
      fail: res => {
         console.log("获取用户信息失败",res)
      }
    })
  },

  goToMyOrder: function () {
    wx.navigateTo({
      url: '../myOrder/myOrder',
    })
  },

  goToMyComment: function () {
    wx.navigateTo({
      url: '../mycomment/mycomment?type=1',
    })
  },
  goToPaiHao() {
    wx.navigateTo({
      url: '../paihao/paihao',
    })
  },
  //饭店电话
  goToPhone() {
    wx.makePhoneCall({
      phoneNumber: '17205881919' 
    })
  },
  aboutOut() {
    wx.navigateTo({
      url:' ',
    })
  },
  change() {
    wx.navigateTo({
      url: '../change/change',
    })
  },
  change1() {
    wx.navigateTo({
      url: '../suser/suser',
    })
  },
  change1() {
    wx.navigateTo({
      url: '../suser/suser',
    })
  },
  change1() {
    wx.navigateTo({
      url: '../suser/suser',
    })
  },
  change2() {
    wx.navigateTo({
      url: '../confirmOrder1/confirmOrder1',
    })
  },
  /*
    onShow(options) {
    var user = app.globalData.userInfo;
    if (user && user.nickName) {
      this.setData({
        isShowUserName: true,
        userInfo: user,
      })
    }
  },
  */

  onShow1(options) {
    var suser = app.globalData.suserInfo;
    if (suser && suser.nickName) {
      this.setData({
        isShowSUserName: true,
        suserInfo: suser,
      })
    }
  },

  //生命周期函数--监听页面加载
  onLoad: function (options) {
    var that = this;
   // var user = app.globalData.userInfo;
    var suser = app.globalData.suserInfo;
    
    // if (user) {
    //   // that.setData({
    //   //  isShowUserName: true,
    //   //  userInfo: user,
    //   // })
    // } else {
    //   // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
    //   // 所以此处加入 callback 以防止这种情况
    //   app.userInfoReadyCallback = res => {
    //     that.setData({
    //       userInfo: res.userInfo,
    //       isShowUserName: true
    //     })
    //   }
    // }
  },
})


