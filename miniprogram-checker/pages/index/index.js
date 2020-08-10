//index.js
//获取应用实例
const app = getApp()

Page({
  record(){
    wx.navigateTo({
      url: '/pages/list/list',
    })
  },
  toScanCode(){
    wx.scanCode({
      success: res => {
        let code = res.result
        let code2 = code.split("&")
        let first = code2[1].replace(/-/g,"/");
        let dateEnd = new Date()
        let dateBegin = new Date(first)
        let different = dateEnd.getTime() - dateBegin.getTime();
        let minute = different/(60*1000)
        if(minute > 3){
          wx.showModal({
            title: '提示',
            content: '该人员二维码过期，请提示他刷新',
            showCancel: false,
            confirmText: '返回'
          });
        }else{
          wx.getStorage({
            key: 'token',
            success:res=>{
              wx.request({
                url: 'https://www.weiweiwi.top/scanCode',
                header:{
                  "Content-Type": "application/json",
                  'Authentication':res.data
                },
                method:"POST",
                data:{
                  residentId:code2[0]
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
                  }else if(res.data.code == 401){
                    wx.showModal({
                      title: '警告',
                      content: res.data.msg,
                      showCancel: false,
                      confirmText: '确定'
                    });
                  }else if(res.data.locked == 1){
                    wx.showModal({
                      title: '警告',
                      content: '该用户已被管理员锁定',
                      showCancel: false,
                      confirmText: '确定'
                    });
                  }
                  else{
                    let data = escape(JSON.stringify(res.data))
                    wx.navigateTo({
                      url: '/pages/user/user?data='+data,
                    })
                  }
                }
              })
            }
          })
          
          
        }
      }
    })
  },

  data: {
    
  },
  onLoad: function () {
    wx.getStorage({
      key: 'token',
      success:res=>{
        wx.request({
          url: 'https://www.weiweiwi.top/getChecker',
          header:{
            'Authentication':res.data
          },
          success:res=>{
            app.globalData.user = res.data.data
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
  }
})
