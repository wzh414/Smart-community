//index.js
//获取应用实例
const app = getApp()

Page({
  data: {
    userInfo: null,
    user:null,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    bottom:false
  },
  sendRequest(res) {
    let that = this;
    wx.request({
      url: "https://www.weiweiwi.top/api/resource/getOpenId?code="+res.code,
      success: res => {
        wx.request({
          url: 'https://www.weiweiwi.top/api/resource/getInfoByOpenid?openid=' + res.data.data,
          success: res => {
            if(res.data.data !='fail'){
              var obj1 = JSON.parse(res.data.data.user)
              res.data.data.user = obj1
              var base64 = res.data.data.image
              base64 = base64.replace(/[\r\n]/g, "")
              res.data.data.image = base64
            }
            app.globalData.user = res.data.data
            that.setData({
              user: res.data.data
            })
            
          }
        })
      }
    })
  },
  toAbout(){
    wx.navigateTo({
      url: '/pages/article/article',
    })
  },
  toRecord(){
    wx.navigateTo({
      url: '/pages/list/list',
    })
  },

  onLoad: function () {
    let that = this
    wx.getSystemInfo({
      success: (res) => {
        that.setData({
          bottom:app.globalData.isIphoneX
        })
      },
    })
    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo,
        user: app.globalData.user
      })
    } else if (this.data.canIUse) {
      app.userInfoReadyCallback = res => {
        this.setData({
          user: res.data
        })
      }
      app.userInfoReadyCallback2 = res => {
        this.setData({
          userInfo: res,
        })
      }
    }
  },

  bindGetUserInfo: function (e) {
    if (e.detail.userInfo) {
      let that = this;
      that.setData({
        userInfo: e.detail.userInfo,
      });
      app.globalData.userInfo = e.detail.userInfo
      if(app.globalData.user == null){
        wx.login({
          success: res => {
            that.sendRequest(res.code)
          }
        });
      }
    } else {
      //用户按了拒绝按钮
      wx.showModal({
        title: '警告',
        content: '您点击了拒绝授权，将无法进入小程序，请授权之后再进入!!!',
        showCancel: false,
        confirmText: '返回授权',
      });
    }
  },

  toRegister(){
    wx.navigateTo({
      url: '../register/register',
    })
  },

  toMessage(){
    wx.navigateTo({
      url: '../message/message',
    })
  },

  onShow(){
    this.setData({
      userInfo: app.globalData.userInfo,
      user: app.globalData.user
    })
  },

})
