var OrderSubmit = {
  template:
    '<div class=container>' +
    '<div class=page-title>配送地址</div>' +
    '<el-radio-group v-model="addrId">' +
    '<el-radio v-for="a in addrs" :label="a.id" :key="a.id">' +
    '{{a.name}} {{a.phone}} {{a.detail}}' +
    '</el-radio>' +
    '</el-radio-group>' +
    '<div style=margin:12px 0>' +
    '<el-button @click="visible=true">新增地址</el-button>' +
    '</div>' +
    '<div class=section-title>商品结算</div>' +
    '<el-table :data="items" style=width:100%>' +
    '<el-table-column label=商品 prop=name></el-table-column>' +
    '<el-table-column label=数量 width=120 prop=qty></el-table-column>' +
    '<el-table-column label=价格 width=120 prop=price></el-table-column>' +
    '<el-table-column label=小计 width=120>' +
    '<template slot-scope=scope>' +
    '{{scope.row.qty*scope.row.price}}' +
    '</template>' +
    '</el-table-column>' +
    '</el-table>' +
    '<div class=form-actions>' +
    '<div>合计 ¥{{total}}</div>' +
    '<el-button type=primary @click="submit">提交订单</el-button>' +
    '</div>' +
    '<el-dialog title=新增地址 :visible.sync=visible>' +
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
    '<el-button type=primary @click="saveAddr">保存</el-button>' +
    '</span>' +
    '</el-form>' +
    '</el-dialog>' +
    '</div>',
  data: function () {
    return {
      addrs: [],
      addrId: null,
      items: [],
      visible: false,
      form: { name: '', phone: '', detail: '' }
    }
  },
  mounted: function () {
    var u = store.currentUser()
    if (!u) {
      this.$message.info('请先登录')
      this.$router.push('/login')
      return
    }
    this.addrs = store.getUserAddrs(u.id)
    var d = this.addrs.find(function (a) { return a.isDefault })
    this.addrId = d ? d.id : (this.addrs[0] ? this.addrs[0].id : null)
    var c = store.getCart()
    var ps = store.prods()
    this.items = c.map(function (i) {
      var p = ps.find(function (pp) { return pp.id == i.productId })
      return { name: p ? p.name : '', price: p ? p.price : 0, qty: i.qty }
    })
  },
  computed: {
    total: function () {
      return this.items
        .reduce(function (s, i) { return s + i.price * i.qty }, 0)
    }
  },
  methods: {
    saveAddr: function () {
      var u = store.currentUser()
      if (!u) { return }
      store.addAddr(u.id, {
        name: this.form.name,
        phone: this.form.phone,
        detail: this.form.detail,
        isDefault: false
      })
      this.visible = false
      this.addrs = store.getUserAddrs(u.id)
    },
    submit: function () {
      if (!this.addrId) {
        this.$message.warning('请选择配送地址')
        return
      }
      var o = store.createOrder(this.addrId)
      if (!o) { return }
      this.$message.success('创建订单成功')
      this.$router.push('/order/' + o.id)
    }
  }
}

