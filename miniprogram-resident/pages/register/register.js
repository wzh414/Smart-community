// pages/login/login.js
const app = getApp()

Page({
  formSubmit(e){
    console.log(e)
    var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
    if(reg.test(e.detail.value.idCardNumber)===false){
      wx.showModal({
        title: '提示',
        content: '身份证输入不合法',
        showCancel: false,
        confirmText: '返回注册',
      });
      return;
    }
    var regName =/^[\u4e00-\u9fa5]{2,4}$/;  
    if(!regName.test(e.detail.value.residentName)){  
      wx.showModal({
        title: '提示',
        content: '真是姓名填写有误',
        showCancel: false,
        confirmText: '返回注册',
      });
      return;  
    }
    if(e.detail.value.homeNumber == ""){
      wx.showModal({
        title: '提示',
        content: '请填写所在单元',
        showCancel: false,
        confirmText: '返回注册',
      });
      return;
    }
    wx.request({
      url: 'https://www.weiweiwi.top/api/resource/addOneResident',
      data:e.detail.value,
      header:{
        'content-type':'application/json'
      },
      method:"POST",
      success:res=>{
        console.log(res.data)
        if(res.data.data != 'fail'){
          var obj1 = JSON.parse(res.data.data.user)
          res.data.data.user = obj1
          var base64 = res.data.data.image
          base64 = base64.replace(/[\r\n]/g, "")
          res.data.data.image = base64
          app.globalData.user = res.data.data
          wx.showModal({
            title: '成功',
            content: '注册成功',
            showCancel: false,
            confirmText: '返回',
            success:res=>{
              wx.navigateBack()
            }
          });
        }
      },
      fail:res=>{
        console.log(res)
      }

    })
  },
  /**
   * 页面的初始数据
   */
  data: {
    userInfo:null,
    openid:null,
    hideToast: false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function () {
    this.setData({
      userInfo:app.globalData.userInfo,
      openid:app.globalData.openid
    })
  }
})