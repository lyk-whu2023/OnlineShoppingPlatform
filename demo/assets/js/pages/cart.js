var Cart = {
  template:
    '<div class=container>' +
    '<el-table :data="rows" style=width:100%>' +
    '<el-table-column type=selection width=55></el-table-column>' +
    '<el-table-column label=商品 width=120>' +
    '<template slot-scope=scope>' +
    '<img :src="img(scope.row.productId)" ' +
    'style=width:120px;height:80px;object-fit:cover>' +
    '</template>' +
    '</el-table-column>' +
    '<el-table-column label=名称 width=240 prop=name></el-table-column>' +
    '<el-table-column label=单价 width=120 prop=price></el-table-column>' +
    '<el-table-column label=数量 width=160>' +
    '<template slot-scope=scope>' +
    '<el-input-number min=1 max=5 v-model=scope.row.qty ' +
    '@change="chg(scope.row)"></el-input-number>' +
    '</template>' +
    '</el-table-column>' +
    '<el-table-column label=小计 width=120>' +
    '<template slot-scope=scope>' +
    '{{scope.row.price*scope.row.qty}}' +
    '</template>' +
    '</el-table-column>' +
    '<el-table-column label=操作 width=120>' +
    '<template slot-scope=scope>' +
    '<el-button type=danger size=mini ' +
    '@click="del(scope.row.productId)">删除</el-button>' +
    '</template>' +
    '</el-table-column>' +
    '</el-table>' +
    '<div style=display:flex;justify-content:flex-end;align-items:center;' +
    'margin-top:16px;gap:16px>' +
    '<div>总价 ¥{{total}}</div>' +
    '<el-button type=primary ' +
    '@click="$router.push(\'/order/submit\')">去结算</el-button>' +
    '</div>' +
    '</div>',
  data: function () { return { rows: [] } },
  mounted: function () { this.load() },
  computed: {
    total: function () {
      return this.rows
        .reduce(function (s, i) { return s + i.price * i.qty }, 0)
    }
  },
  methods: {
    img: function (pid) {
      return 'https://picsum.photos/seed/p' + pid + '/120/80'
    },
    load: function () {
      var c = store.getCart()
      var ps = store.prods()
      this.rows = c.map(function (i) {
        var p = ps.find(function (pp) { return pp.id == i.productId })
        return {
          productId: i.productId,
          name: p ? p.name : '',
          price: p ? p.price : 0,
          qty: i.qty
        }
      })
    },
    chg: function (row) {
      store.setCartQty(row.productId, row.qty)
      this.load()
    },
    del: function (pid) {
      store.removeCartItem(pid)
      this.load()
    }
  }
}

