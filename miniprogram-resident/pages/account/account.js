// pages/account/account.js
const app = getApp()

Page({
  refresh(){
    let that = this
    console.log(that.data.user.user.residentId)
    wx.request({
      url: 'https://www.weiweiwi.top/api/resource/refresh?id='+that.data.user.user.residentId,
      success: res => {
        console.log(res)
        if(res.data == 'fail'){
          wx.showModal({
            title: '警告',
            content: '出错了！！',
            showCancel: false,
            confirmText: '返回'
          });
        }else if(res.data == 'locked'){
          wx.showModal({
            title: '警告',
            content: '刷新失败，账号被锁定',
            showCancel: false,
            confirmText: '返回',
            success:res=>{
              that.setData({
                ["user.user.locked"]:1
              })
            }
          });
        }
        else{
          var base64 = res.data
          base64 = base64.replace(/[\r\n]/g, "")
          res.data = base64
          that.setData({
            ["user.image"]:res.data
          })
        }
      }
    })
  },
  closeTarget(){
    this.setData({
      targetHiden:true
    })
  },
  /**
   * 页面的初始数据
   */
  data: {
    userInfo: null,
    user: null,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    targetHiden:true,
    bottom:false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function () {
    let that = this
    wx.getSystemInfo({
      success: (res) => {
        that.setData({
          bottom:app.globalData.isIphoneX
        })
      },
    })
    if (app.globalData.userInfo&&app.globalData.user) {
      this.setData({
        userInfo: app.globalData.userInfo,
        user:app.globalData.user
      })
    } else if (this.data.canIUse) {
      app.userInfoReadyCallback = res => {
        if(res.data != 'fail'){
          this.setData({
            targetHiden:false
          })
        }
        this.setData({
          user:res.data,
        })
      }
      app.userInfoReadyCallback2 = res => {
        this.setData({
          userInfo: res,
        })
      }
    }
  },
  onShow: function () {
    if(app.globalData.user !='fail'&&app.globalData.user !=null){
      this.setData({
        targetHiden:false
      })
    }else{
      this.setData({
        targetHiden:true
      })
    }
    this.setData({
      userInfo: app.globalData.userInfo,
      user: app.globalData.user
    })
  },
  refreshUser(e){
    console.log(e)
  }
})