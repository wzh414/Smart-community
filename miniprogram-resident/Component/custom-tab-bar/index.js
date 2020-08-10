const app = getApp()

Component({
  options:{
    multipleSlots: true
  },
  properties:{
    bottom:{
      type:Boolean,
      value:false
    },
  },
  data: {
    selected: 0,
    color: "#000000",
    selectedColor: "#000000",
    list: [
      {
        "pagePath": "/pages/account/account",
        "iconPath": "/images/icon/first.png",
        "selectedIconPath": "/images/icon/selectFirst.png",
        "text": "我的通行码",
      },
      {
        "iconPath": "/images/icon/refresh.png",
        "text": "刷新",
      },
      {
        "pagePath": "/pages/index/index",
        "iconPath": "/images/icon/user.png",
        "selectedIconPath": "/images/icon/selectUser.png",
        "text": "个人",
      }
    ],
  },
  attached() {
  },
  methods: {
    switchTab(e) {
      let that = this
      const data = e.currentTarget.dataset
      if(data.index == 1){
        wx.request({
          url: 'https://www.weiweiwi.top/api/resource/getInfoByOpenid?openid=' + app.globalData.openid,
          success: res => {
            if(res.data.data !='fail'){
              var obj1 = JSON.parse(res.data.data.user)
              res.data.data.user = obj1
              var base64 = res.data.data.image
              base64 = base64.replace(/[\r\n]/g, "")
              res.data.data.image = base64
            }
            app.globalData.user = res.data.data
            const pages = getCurrentPages()
            const perpage = pages[pages.length - 1]
            perpage.onShow()
          }
        })
      }else{
        const url = data.path
        wx.switchTab({url})
        this.setData({
          selected: data.index
        })
      }
      
    }
  }
})