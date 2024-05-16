let app = getApp();
let payWay = [{
  "id": 1,
  "package": "会员卡支付"
}, {
  "id": 2,
  "package": "微信支付"
}, {
  "id": 3,
  "package": "银行卡支付"
}]
Page({
  //页面的初始数据
  data: {
    tableNum: "",
    confirmOrder: [],
    green: "",// 是否是制定化
    green_F: false,    // 用餐人数输入框获取焦点

    // 输入框中的用餐人数
    diner_num: 0,
    // 用餐人数输入框获取焦点
    diner_numF: false,
    tases: "",//口味
    tases_F: false,// 口味输入框获取焦点
    dateTime: "", // 初始日期时间  
    // 备注信息
    remarks: "",
    //支付方式列表
    payWayList: [],
    // 购物车数据
    cartList: [],
    totalPrice: 0,
    totalNum: 0,
    // 遮罩
    maskFlag: true,
  
  },
  // 生命周期函数--监听页面加载
  onLoad: function(Options) {
    var that = this;
    let tableNum = Options.tableNum;
    var arr = wx.getStorageSync('cart') || [];
    for (var i in arr) {
      this.data.totalPrice += arr[i].quantity * arr[i].price;
      this.data.totalNum += arr[i].quantity
    }
    this.setData({
      tableNum: tableNum,
      cartList: arr,
      totalPrice: this.data.totalPrice.toFixed(2),
      totalNum: this.data.totalNum
    })
  },






  // 点击制定化逻辑按钮，输入框出现对应是否
  getGreenT: function(e) {
    var green1 = e.currentTarget.dataset.num;
    var green = this.data.green;
    // 点击“输”，获取焦点，
    if (green1 == -1) {
      this.setData({
        green_F: true,
      })
      this.getGreen();
    } else {
      this.setData({
        green: green1
      });
    }
  },

  // 点击口味，输入框出现对应口味
  getTasteT: function(e) {
    var taste1 = e.currentTarget.dataset.num;
    var taste = this.data.taste;
    // 点击“输”，获取焦点，
    if (taste1 == -1) {
      this.setData({
        taste_F: true,
      })
      this.getGreen();
    } else {
      this.setData({
        taste: taste1
      });
    }
  },











  // 点击数字，输入框出现对应数字
  getDinnerNUM: function(e) {
    var dinnerNum = e.currentTarget.dataset.num;
    var diner_num = this.data.diner_num;
    // 点击“输”，获取焦点，
    if (dinnerNum == 0) {
      this.setData({
        diner_numF: true,
      })
      this.getDinerNum();
    } else {
      this.setData({
        diner_num: dinnerNum
      });
    }
  },
  //打开支付方式弹窗
  choosePayWay: function() {
    var payWayList = this.data.payWayList
    var that = this;
    var rd_session = wx.getStorageSync('rd_session') || [];
    that.setData({
      payWayList: payWay
    });

    // 支付方式打开动画
    var animation = wx.createAnimation({
      duration: 200,
      timingFunction: 'ease-in-out',
      delay: 0
    });
    that.animation = animation;
    animation.translate(0, -285).step();
    that.setData({
      animationData: that.animation.export(),
    });
    that.setData({
      maskFlag: false,
    });
  },
  // 支付方式关闭方法
  closePayWay: function() {
    var that = this
    // 支付方式关闭动画
    that.animation.translate(0, 285).step();
    that.setData({
      animationData: that.animation.export()
    });
    that.setData({
      maskFlag: true
    });
  },

  // 指定信息
  getGreen: function(e) {
    var green = this.data.green;
    this.setData({
        green: green
    })
  },



  // 获取输入的用餐人数
  getDinerNum: function(e) {
    var diner_num = this.data.diner_num;
    this.setData({
      diner_num: diner_num
    })
  },

    // 获口味信息
    getTaste: function(e) {
        var taste = this.data.taste;
        this.setData({
            taste: e.detail.value
        })
      },
       // 获预定时间信息
       getTime: function(e) {
        var dateTime = this.data.dateTime;
        this.setData({
            dateTime: e.detail.value
        })
      },
 
  // 获取备注信息
  getRemark: function(e) {
    var remarks = this.data.remarks;
    this.setData({
      remarks: e.detail.value
    })
  },
  //提交订单
  submitOrder: function(e) {
    let that = this;
    let tableNum = that.data.tableNum;

    //校验是否填写手机号
    let phone = app.globalData.suserInfo.realphone
    console.log("手机号" + phone)
    if (phone == '' || phone == undefined) {
      wx.showModal({
        title: '提示',
        content: '请填写手机号',
        success: (res => {
          if (res.confirm) {
            wx.navigateTo({
              url: '../suser/suer',
            })
          }
        })
      })
      return
    }


    let arr = wx.getStorageSync('cart') || [];

    let goods_arr = [];
    arr.forEach(order => {
      console.log(order);
      var goods = new Object();
      goods.foodId = order.id;
      goods.foodQuantity = order.quantity;
      goods_arr.push(goods)
    })

    let goods_josn = JSON.stringify(goods_arr);
    console.log(goods_josn)
    let  green = this.data.green; //是否制定化
    let diner_num = this.data.diner_num; //用餐人数
    let dinerNum;
    let taste = this.data.taste;//口味
    let dateTime = this.data.dateTime;//时间
    let remarks = this.data.remarks; //备注信息
    let adminId = app.globalData.adminId; //商家信息
    let payId = e.currentTarget.dataset.id;
    let rd_session = wx.getStorageSync('rd_session') || [];
    if (diner_num == 0) {
      that.setData({
        diner_num: 1
      })
    }
      // 检查taste是否为空或未定义，如果是，则设置为“原味”  
  if (taste === null || taste === undefined || taste === '') {  
    taste = '原味'; // 设置默认口味为“原味”  
  }  
    var peoples = this.data.diner_num
    console.log("是否为制定化餐品：" + green)
    console.log("用餐人数：" + peoples)
    console.log("口味：" + taste)
    console.log("时间：" + dateTime)
    console.log("备注：" + remarks)
    console.log("商家：" + adminId)


    wx.request({
      url: app.globalData.baseUrl + '/userOrder/create',
      method: "POST",
      header: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      data: {
        openid: app.globalData.openid,
        name: app.globalData.suserInfo.nickName,
        phone: app.globalData.suserInfo.realphone,
        address: tableNum,
        green: green,
        taste: taste,//口味
        dateTime: dateTime,//时间
        remarks:remarks,
        adminId:app.globalData.adminId,
        items: goods_josn,
        
      },
      success: function(res) {
        // var rescode = res.data.code
        console.log("支付成功", res.data)
        if (res && res.data && res.data.data) {
          // 支付方式关闭动画
          that.animation.translate(0, 285).step();
          that.setData({
            animationData: that.animation.export()
          });
          that.setData({
            maskFlag: true
          });
          wx.showToast({
            title: '下单成功！',
          })
          wx.setStorageSync('cart', "")
          wx.switchTab({
            url: '../me/me',
          })
        } else {
          // 支付方式关闭动画
          that.animation.translate(0, 285).step();
          that.setData({
            animationData: that.animation.export()
          });
          that.setData({
            maskFlag: true
          });
        }

      }
    })

  },


})