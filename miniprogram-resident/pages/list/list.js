// pages/list/list.js
const app = getApp()

Page({
  data: {
    listData:[]
  },

  onLoad: function () {
    let that = this
    wx.request({
      url:'https://www.weiweiwi.top/health/data/getResidentDetail?residentId='+app.globalData.user.user.residentId,
      success:res => {
        that.setData({
          listData:res.data
        })
      }
    })
  }

})