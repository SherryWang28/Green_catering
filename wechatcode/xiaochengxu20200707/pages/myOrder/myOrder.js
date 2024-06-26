//JS
var app = getApp()
let orderStatus = 1; //0"新订单，未支付;1"新订单，已支付";2, "已取消"；3"待评价"；4“已完成”
Page({
  data: {
    // 顶部菜单切换
    navbar: ["待上餐", "已取消", "待评价", "已完成"],
    // 默认选中菜单
    currentTab: 0,
    isShowComment: false, //是否显示评论框
    list: []
  },
  //顶部tab切换
  navbarTap: function(e) {
    let index = e.currentTarget.dataset.idx;
    this.setData({
      currentTab: index
    })

    //1"新订单，已支付";2, "已取消"；3"待评价"；4“已完成”
    if (index == 1) {
      orderStatus = 2;
    } else if (index == 2) {
      orderStatus = 3;
    } else if (index == 3) {
      orderStatus = 4;
    } else {
      orderStatus = 1;
    }
    this.getMyOrderList();
  },

  onShow: function() {
    this.getMyOrderList();
  },

  getMyOrderList() {
    let that = this;
    let openid = app._checkOpenid();
    if (!openid) {
      return;
    }
    //请求自己后台获取用户openid
    wx.request({
      url: app.globalData.baseUrl + '/userOrder/listByStatus',
      data: {
        openid: openid,
        orderStatus: orderStatus
      },
      success: function(res) {
        console.log(res);
        if (res && res.data && res.data.data && res.data.data.length > 0) {
          let dataList = res.data.data;
          console.log(dataList)
          that.setData({
            list: dataList
          })
        } else {
          that.setData({
            list: []
          })
        }
      }
    })
  },
  //去评论页面
  goCommentPage() {
    wx.navigateTo({
      url: '../mycomment/mycomment?type=' + 1,
    })
  },
  //弹起评论框
  goComment(event) {
    let orderId = event.currentTarget.dataset.orderid;
    this.setData({
      isShowComment: true,
      orderId: orderId
    })
  },
  //隐藏评论框
  cancelComment() {
    this.setData({
      isShowComment: false
    })
  },
  //获取评论内容
  setValue(input) {
    this.setData({
      comment: input.detail.value
    })
  },
  //提交评论
  submitComment() {
    let that = this;
    that.cancelComment();
    let content = that.data.comment;
    let orderId = that.data.orderId;
    if (!content) {
      wx.showToast({
        title: '评论内容为空',
      })
      return;
    }
    let openid = app._checkOpenid();
    if (!openid) {
      return;
    }

    wx.request({
      url: app.globalData.baseUrl + '/comment',
      method: "POST",
      header: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      data: {
        orderId: orderId,
        openid: openid,
        name: app.globalData.suserInfo.nickName,
        avatarUrl: app.globalData.suserInfo.avatarUrl,
        content: content
      },
      success: function(res) {
        that.getMyOrderList();
        wx.showToast({
          title: '评论成功',
        })
      }
    })
  },


  //催单
  clickSure(event) {
    let that = this;
    let item = event.currentTarget.dataset.item;
    console.log("催单的订单号：", item)
    wx.request({
      url: app.globalData.baseUrl + '/userOrder/cuidan',
      header: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      data: {
        orderId: item.orderId,
        zhuoHao: item.buyerAddress
      },
      success: function(res) {
        wx.showToast({
          title: '催单成功',
        })
      }
    })

  },

  //模拟支付
  clickGoPay(event) {
    let that = this;
    let orderId = event.currentTarget.dataset.orderid;
    wx.showModal({
      title: '模拟支付',
      content: '确定去支付吗？',
      success(res) {
        if (res.confirm) {
          that.goPay(orderId)
        } else if (res.cancel) {
          console.log('用户点击取消')
        }
      }
    })
  },
  //去支付
  goPay(orderId) {
    let that = this;
    let openid = app._checkOpenid();
    if (!openid) {
      return;
    }

    //请求自己后台获取用户openid
    wx.request({
      url: app.globalData.baseUrl + '/pay/goPay',
      data: {
        orderId: orderId
      },
      success: function(res) {
        console.log(res);
        if (res && res.data && res.data.data) {
          let dataList = res.data.data;
          that.getMyOrderList();
          wx.showToast({
            title: '支付成功',
          })
        } else {
          wx.showToast({
            title: '支付失败',
          })
        }
      }
    })
  },
  //取消订单
  cancleOrder(event) {
    let that = this;
    let openid = app._checkOpenid();
    if (!openid) {
      return;
    }

    let orderId = event.currentTarget.dataset.orderid;
    wx.request({
      url: app.globalData.baseUrl + '/userOrder/cancel',
      method: "POST",
      header: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      data: {
        openid: "" + openid,
        orderId: orderId
      },
      success: function(res) {
        console.log({
          res
        });
        that.getMyOrderList();
      }
    })
  }
})