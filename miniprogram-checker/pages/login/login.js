// pages/login/login.js
Page({

  toLogin(e){
    wx.request({
      url: 'https://www.weiweiwi.top/authentication',
      data:{
        username:e.detail.value.username,
        password:e.detail.value.password
      },
      header: {
        "Content-Type": "application/json"
      },
      method:"POST",
      success:res=>{
        console.log("2")
        if(res.data.code == 400){
            wx.showModal({
            title: '提示',
            content: res.data.message,
            showCancel: false,
            confirmText: '重新输入',
          });
        }else{
            wx.setStorage({
            data: res.data.data,
            key: 'token',
            success:res=>{
              wx.redirectTo({
                url: '/pages/index/index',
              })
            }
          })
        }
      },
      fail:res=>{
        console.log("3")
      }
      
    })
  },
  /**
   * 页面的初始数据
   */
  data: {

  }
})