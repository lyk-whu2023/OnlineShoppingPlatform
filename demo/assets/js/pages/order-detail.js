var OrderDetail = {
  template:
    '<div class=container>' +
    '<el-card>' +
    '<div class=page-title>收货地址</div>' +
    '<div>' +
    '{{o&&o.address&&o.address.name}} ' +
    '{{o&&o.address&&o.address.phone}} ' +
    '{{o&&o.address&&o.address.detail}}' +
    '</div>' +
    '</el-card>' +
    '<div class=section-title>商品列表</div>' +
    '<el-table :data="o.items||[]" style=width:100%>' +
    '<el-table-column label=商品 prop=name></el-table-column>' +
    '<el-table-column label=数量 width=120 prop=qty></el-table-column>' +
    '<el-table-column label=价格 width=120 prop=price></el-table-column>' +
    '</el-table>' +
    '<div class=section-title>订单金额</div>' +
    '<el-card>合计 ¥{{o.amount}}</el-card>' +
    '<div class=section-title>订单状态</div>' +
    '<el-tag>{{o.status}}</el-tag>' +
    '</div>',
  data: function () { return { o: {} } },
  mounted: function () {
    var id = this.$route.params.id
    this.o = store.getOrder(Number(id)) || {}
  }
}

