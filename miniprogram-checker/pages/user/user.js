// pages/user/user.js
var util = require('../../utils/util.js');
const app = getApp()

Page({
  formSubmit(e){
    console.log(e.detail.value.checkerId)
    var reg = /^\d{2}(\.\d{0,2})?$/
    let tem = parseFloat(e.detail.value.temperature)
    if(reg.test(e.detail.value.temperature)==false||tem<34||tem>45){
      wx.showModal({
        title: '提示',
        content: '请输入合法数字',
        showCancel: false,
        confirmText: '返回',
      });
      return;
    }
    wx.getStorage({
      key: 'token',
      success:res=>{
        wx.request({
          url: 'https://www.weiweiwi.top/enterTem',
          data:e.detail.value,
          header:{
            'Authentication':res.data,
            'content-type':'application/json'
          },
          method:"POST",
          success:res=>{
            if(res.data == 'overdue'){
              wx.showModal({
                title: '警告',
                content: '登录过期，请重新登录',
                showCancel: false,
                confirmText: '去登陆',
                success: function (res) {
                  wx.redirectTo({
                    url: '/pages/login/login',
                  })
                }
              });
            }else{
              wx.showModal({
                title: '成功',
                content: '录入成功',
                showCancel: false,
                confirmText: '回到首页',
                success: function (res) {
                  wx.navigateBack({
                    
                  })
                }
              });
            }
          }
        })

      },
      fail:err=>{
        wx.showModal({
          title: '警告',
          content: '请先进行登录',
          showCancel: false,
          confirmText: '去登陆',
          success: function (res) {
            wx.redirectTo({
              url: '/pages/login/login',
            })
          }
        });
      }
    })


  },
  close2(){
    console.log("1")
    this.setData({
      topTips: true,
      dialog2: false
    })
  },
  open2() {
    this.setData({
      topTips: false,
      dialog2: true
    });
  },
  /**
   * 页面的初始数据
   */
  data: {
    topTips: false,
    hide: false,
    dialog2: false,
    current:util.formatDate(new Date()),
    user:null,
    checker:null,
  },
  close: function() {
    this.setData({
        hide: true
    });
    setTimeout(() => {
        this.setData({
            topTips: false,
            hide: false,
        });
    }, 300);
},
  open: function() {
    this.setData({
        topTips: true,
    });
},

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let data = unescape(options.data)
    const user = JSON.parse(data);
    if(user.tip == "warn"){
      this.setData({
        topTips: true,
        hide:true
      });
    }
    this.setData({
      user:user,
      checker:app.globalData.user
    });
  }
})