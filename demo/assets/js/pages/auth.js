var Auth = {
  template:
    '<div class=container style=max-width:520px>' +
    '<el-tabs type=card>' +
    '<el-tab-pane label=登录>' +
    '<el-form label-width=100px @submit.native.prevent>' +
    '<el-form-item label=账号>' +
    '<el-input v-model="user"></el-input>' +
    '</el-form-item>' +
    '<el-form-item label=密码>' +
    '<el-input type=password v-model="pwd"></el-input>' +
    '</el-form-item>' +
    '<div style=display:flex;justify-content:space-between;align-items:center>' +
    '<el-link type=primary>忘记密码</el-link>' +
    '<el-button type=primary @click="login">登录</el-button>' +
    '</div>' +
    '</el-form>' +
    '</el-tab-pane>' +
    '<el-tab-pane label=注册>' +
    '<el-form label-width=100px @submit.native.prevent>' +
    '<el-form-item label=用户名>' +
    '<el-input v-model="newUser"></el-input>' +
    '</el-form-item>' +
    '<el-form-item label=密码>' +
    '<el-input type=password v-model="newPwd"></el-input>' +
    '</el-form-item>' +
    '<el-form-item label=邮箱/手机号>' +
    '<el-input v-model="newPhone"></el-input>' +
    '</el-form-item>' +
    '<el-button type=primary style=width:100% ' +
    '@click="register">注册</el-button>' +
    '</el-form>' +
    '</el-tab-pane>' +
    '</el-tabs>' +
    '</div>',
  data: function () {
    return {
      user: 'customer',
      pwd: '',
      newUser: '',
      newPwd: '',
      newPhone: ''
    }
  },
  methods: {
    login: function () {
      var ok = store.login(this.user)
      if (!ok) {
        this.$message.error('账号不存在')
        return
      }
      var u = store.currentUser()
      if (u && u.role === 'admin') { this.$router.push('/admin') }
      else { this.$router.push('/') }
    },
    register: function () {
      if (!this.newUser) {
        this.$message.error('请输入用户名')
        return
      }
      store.register(this.newUser, this.newPwd, this.newPhone)
      this.$message.success('注册成功')
      this.$router.push('/')
    }
  }
}

