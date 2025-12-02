var Address = {
  template:
    '<div class=container>' +
    '<div style=margin-bottom:12px>' +
    '<el-button type=primary @click="openAdd">新增地址</el-button>' +
    '</div>' +
    '<el-table :data="rows" style=width:100%>' +
    '<el-table-column label=收件人 width=160 prop=name></el-table-column>' +
    '<el-table-column label=手机号 width=160 prop=phone></el-table-column>' +
    '<el-table-column label=地址详情 prop=detail></el-table-column>' +
    '<el-table-column label=操作 width=220>' +
    '<template slot-scope=scope>' +
    '<div class=table-actions>' +
    '<el-button type=primary size=mini @click="edit(scope.row)">编辑</el-button>' +
    '<el-button type=danger size=mini @click="del(scope.row.id)">删除</el-button>' +
    '</div>' +
    '</template>' +
    '</el-table-column>' +
    '</el-table>' +
    '<el-dialog title=编辑地址 :visible.sync=visible>' +
    '<el-form label-width=100px>' +
    '<el-form-item label=收件人>' +
    '<el-input v-model="form.name"></el-input>' +
    '</el-form-item>' +
    '<el-form-item label=手机号>' +
    '<el-input v-model="form.phone"></el-input>' +
    '</el-form-item>' +
    '<el-form-item label=地址详情>' +
    '<el-input v-model="form.detail"></el-input>' +
    '</el-form-item>' +
    '<span slot=footer class=dialog-footer>' +
    '<el-button @click="visible=false">取消</el-button>' +
    '<el-button type=primary @click="save">保存</el-button>' +
    '</span>' +
    '</el-form>' +
    '</el-dialog>' +
    '</div>',
  data: function () {
    return { rows: [], visible: false, form: { id: null, name: '', phone: '', detail: '' } }
  },
  mounted: function () { this.load() },
  methods: {
    load: function () {
      var u = store.currentUser()
      if (!u) { this.$router.push('/login'); return }
      this.rows = store.getUserAddrs(u.id)
    },
    openAdd: function () {
      this.form = { id: null, name: '', phone: '', detail: '' }
      this.visible = true
    },
    edit: function (row) {
      this.form = Object.assign({}, row)
      this.visible = true
    },
    save: function () {
      var u = store.currentUser()
      if (!u) { return }
      if (this.form.id) {
        store.updateAddr({
          id: this.form.id,
          userId: u.id,
          name: this.form.name,
          phone: this.form.phone,
          detail: this.form.detail,
          isDefault: false
        })
      } else {
        store.addAddr(u.id, {
          name: this.form.name,
          phone: this.form.phone,
          detail: this.form.detail,
          isDefault: false
        })
      }
      this.visible = false
      this.load()
    },
    del: function (id) {
      store.deleteAddr(id)
      this.load()
    }
  }
}

