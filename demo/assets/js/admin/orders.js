var AdminOrders = {
  template:
    '<div>' +
    '<el-table :data="rows" style=width:100%>' +
    '<el-table-column label=订单号 width=160 prop=id></el-table-column>' +
    '<el-table-column label=用户名 width=160>' +
    '<template slot-scope=scope>{{userName(scope.row.userId)}}</template>' +
    '</el-table-column>' +
    '<el-table-column label=总金额 width=160 prop=amount></el-table-column>' +
    '<el-table-column label=状态 width=160 prop=status></el-table-column>' +
    '<el-table-column label=下单时间 width=200 prop=createdAt></el-table-column>' +
    '<el-table-column label=操作 width=240>' +
    '<template slot-scope=scope>' +
    '<div class=table-actions>' +
    '<el-button size=mini @click="detail(scope.row)">详情</el-button>' +
    '<el-button type=success size=mini @click="ship(scope.row)">发货</el-button>' +
    '<el-button type=danger size=mini @click="cancel(scope.row)">取消</el-button>' +
    '</div>' +
    '</template>' +
    '</el-table-column>' +
    '</el-table>' +
    '<el-dialog title=订单详情 :visible.sync=visible width=720px>' +
    '<div class=container>' +
    '<div class=page-title>用户信息</div>' +
    '<el-descriptions :column=2>' +
    '<el-descriptions-item label=用户名>{{userName(order.userId)}}</el-descriptions-item>' +
    '<el-descriptions-item label=手机号>{{userPhone(order.userId)}}</el-descriptions-item>' +
    '</el-descriptions>' +
    '<div class=section-title>商品信息</div>' +
    '<el-table :data="order.items||[]" style=width:100%>' +
    '<el-table-column label=商品 prop=name></el-table-column>' +
    '<el-table-column label=数量 width=120 prop=qty></el-table-column>' +
    '<el-table-column label=价格 width=120 prop=price></el-table-column>' +
    '</el-table>' +
    '<div style=margin-top:16px;display:flex;gap:12px>' +
    '<el-button type=success @click="ship(order)">发货</el-button>' +
    '<el-button type=danger @click="cancel(order)">取消订单</el-button>' +
    '</div>' +
    '</div>' +
    '</el-dialog>' +
    '</div>',
  data: function () { return { rows: [], visible: false, order: {} } },
  mounted: function () { this.load() },
  methods: {
    load: function () { this.rows = store.orders() },
    userName: function (uid) {
      var u = store.users().find(function (x) { return x.id == uid })
      return u ? u.username : ''
    },
    userPhone: function (uid) {
      var u = store.users().find(function (x) { return x.id == uid })
      return u ? u.phone : ''
    },
    detail: function (row) { this.order = row; this.visible = true },
    ship: function (row) { store.updateOrderStatus(row.id, '已发货'); this.load() },
    cancel: function (row) { store.updateOrderStatus(row.id, '已取消'); this.load() }
  }
}

