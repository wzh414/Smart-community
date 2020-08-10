// pages/list/list.js
const app = getApp()

Page({
  data: {
    listData:[]
  },

  onLoad: function () {
    let that = this
    wx.getStorage({
      key: 'token',
      success:res=>{
        wx.request({
          url: 'https://www.weiweiwi.top/todayEnter',
          header:{
            "Content-Type": "application/json",
            'Authentication':res.data
          },
          method:"POST",
          data:{
            checkerId:app.globalData.user.id
          },
          success:res=>{
            if(res.data == 'fail'){
              wx.showModal({
                title: '警告',
                content: '登录状态过期，请重新登录',
                showCancel: false,
                confirmText: '重新登陆',
                success: function (res) {
                  wx.redirectTo({
                    url: '/pages/login/login',
                  })
                }
              });
            }else{
              that.setData({
                listData:res.data.data
              })
            }
          }
        })
      }
    })

  }

})