var UserCenter = {
  template:
    '<div class=container>' +
    '<el-container>' +
    '<el-aside width=220px>' +
    '<el-menu default-active=1>' +
    '<el-menu-item index=1 @click="$router.push(\'/user/orders\')">' +
    '我的订单</el-menu-item>' +
    '<el-menu-item index=2 @click="$router.push(\'/user/address\')">' +
    '地址管理</el-menu-item>' +
    '<el-menu-item index=3>个人信息</el-menu-item>' +
    '<el-menu-item index=4 @click="logout">退出登录</el-menu-item>' +
    '</el-menu>' +
    '</el-aside>' +
    '<el-main>' +
    '<el-card>' +
    '<div class=page-title>基本信息</div>' +
    '<el-descriptions :column=2>' +
    '<el-descriptions-item label=姓名>' +
    '{{u&&u.username}}' +
    '</el-descriptions-item>' +
    '<el-descriptions-item label=手机号>' +
    '{{u&&u.phone}}' +
    '</el-descriptions-item>' +
    '<el-descriptions-item label=注册时间>' +
    '{{u&&u.createdAt}}' +
    '</el-descriptions-item>' +
    '</el-descriptions>' +
    '</el-card>' +
    '</el-main>' +
    '</el-container>' +
    '</div>',
  data: function () { return { u: store.currentUser() } },
  methods: {
    logout: function () {
      store.logout()
      this.$message.success('已退出')
      this.$router.push('/')
    }
  }
}
