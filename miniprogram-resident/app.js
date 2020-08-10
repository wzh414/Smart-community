//app.js
App({
  onLaunch(){
    let that = this
    wx.getSystemInfo({
      success:res => {
        let modelmes = res.model
        console.log(res.model)
        let iphoneArr = ['iPhone X', 'iPhone 11', 'iPhone 11 Pro Max','iPhone XR','iPhone XS Max']
        iphoneArr.forEach(function (item) {
          if (modelmes.search(item) != -1) {
           that.globalData.isIphoneX = true
          }
        })
      }
    })
    wx.login({
      success: res => {
        wx.request({
          url: "https://www.weiweiwi.top/api/resource/getOpenId?code="+res.code,
          header:{
            'content-type': 'text/json'
          },
          success: res => {
            if(res.data.data){
              this.globalData.openid = res.data.data
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
                  this.globalData.user = res.data.data
                  if (this.userInfoReadyCallback) {
                    this.userInfoReadyCallback(res.data)
                  }
                }
              })
            }
          }
        })
      }
    })
    // 获取用户信息
    wx.getSetting({
      success: res => {
        if (res.authSetting['scope.userInfo']) {
          // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
          wx.getUserInfo({
            success: res => {
              // 可以将 res 发送给后台解码出 unionId
              // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
              // 所以此处加入 callback 以防止这种情况
              let res1 = JSON.parse(res.rawData);
              this.globalData.userInfo = res1
              if (this.userInfoReadyCallback2) {
                this.userInfoReadyCallback2(res1)
              }
            }
          })
        }
      }
    })
  },
  globalData: {
    userInfo:null,
    user:null,
    openid:null,
    isIphoneX:false
  }
})