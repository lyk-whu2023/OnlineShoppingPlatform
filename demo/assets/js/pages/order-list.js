var OrderList = {
  template:
    '<div class=container>' +
    '<el-tabs v-model="tab">' +
    '<el-tab-pane label=全部 name=all></el-tab-pane>' +
    '<el-tab-pane label=待支付 name=pending></el-tab-pane>' +
    '<el-tab-pane label=已支付 name=paid></el-tab-pane>' +
    '<el-tab-pane label=已发货 name=已发货></el-tab-pane>' +
    '<el-tab-pane label=已完成 name=已完成></el-tab-pane>' +
    '</el-tabs>' +
    '<el-card v-for="o in view" :key="o.id" ' +
    'style=margin-bottom:12px>' +
    '<div style=display:flex;align-items:center;justify-content:space-between>' +
    '<div>订单号 {{o.id}}</div>' +
    '<div>' +
    '<span style=color:#F56C6C;margin-right:12px>¥{{o.amount}}</span>' +
    '<el-tag>{{o.status}}</el-tag>' +
    '<el-button type=primary style=margin-left:12px ' +
    '@click="$router.push(\'/order/\'+o.id)">查看详情</el-button>' +
    '</div>' +
    '</div>' +
    '</el-card>' +
    '</div>',
  data: function () { return { tab: 'all', orders: [] } },
  mounted: function () {
    var u = store.currentUser()
    if (!u) { this.$router.push('/login'); return }
    this.orders = store.getUserOrders(u.id)
  },
  computed: {
    view: function () {
      var t = this.tab
      return this.orders.filter(function (o) {
        if (t === 'all') { return true }
        return o.status === t
      })
    }
  }
}

