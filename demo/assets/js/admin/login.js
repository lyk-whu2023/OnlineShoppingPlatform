var AdminLogin = {
  template:
    '<div class=container style=max-width:520px>' +
    '<div style=text-align:center;margin-bottom:16px;' +
    'font-size:20px;color:#409EFF>Admin</div>' +
    '<el-form label-width=100px>' +
    '<el-form-item label=用户名>' +
    '<el-input v-model="user"></el-input>' +
    '</el-form-item>' +
    '<el-form-item label=密码>' +
    '<el-input type=password v-model="pwd"></el-input>' +
    '</el-form-item>' +
    '<el-button type=primary style=width:100% ' +
    '@click="login">登录</el-button>' +
    '</el-form>' +
    '</div>',
  data: function () { return { user: 'admin', pwd: '' } },
  methods: {
    login: function () {
      var ok = store.login(this.user)
      if (!ok) { this.$message.error('账号不存在'); return }
      this.$router.push('/admin')
    }
  }
}

