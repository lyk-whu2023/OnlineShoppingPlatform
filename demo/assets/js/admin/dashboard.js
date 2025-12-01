var Dashboard = {
  template:
    '<div>' +
    '<el-row :gutter=24>' +
    '<el-col :span=6><el-card><div>' +
    '订单数量 {{orders}}' +
    '</div></el-card></el-col>' +
    '<el-col :span=6><el-card><div>' +
    '用户数量 {{users}}' +
    '</div></el-card></el-col>' +
    '<el-col :span=6><el-card><div>' +
    '待发货 {{pending}}' +
    '</div></el-card></el-col>' +
    '<el-col :span=6><el-card><div>' +
    '今日销售 ¥{{today}}' +
    '</div></el-card></el-col>' +
    '</el-row>' +
    '<el-row :gutter=24 style=margin-top:24px>' +
    '<el-col :span=12><el-card>' +
    '<div id=chart1 class=chart></div>' +
    '</el-card></el-col>' +
    '<el-col :span=12><el-card>' +
    '<div id=chart2 class=chart></div>' +
    '</el-card></el-col>' +
    '</el-row>' +
    '</div>',
  data: function () {
    return {
      orders: store.orders().length,
      users: store.users().length,
      pending: store.orders().filter(function (o) {
        return o.status === '已创建'
      }).length,
      today: store.orders().reduce(function (s, o) {
        var d = o.createdAt && o.createdAt.slice(0, 10)
        var t = new Date().toISOString().slice(0, 10)
        return s + (d === t ? o.amount : 0)
      }, 0)
    }
  },
  mounted: function () {
    var c1 = echarts.init(document.getElementById('chart1'))
    c1.setOption({
      xAxis: { type: 'category', data: ['Mon','Tue','Wed','Thu','Fri','Sat','Sun'] },
      yAxis: { type: 'value' },
      series: [{ type: 'line', data: [120,200,150,80,70,110,130] }]
    })
    var c2 = echarts.init(document.getElementById('chart2'))
    c2.setOption({
      xAxis: { type: 'category', data: ['A','B','C','D'] },
      yAxis: { type: 'value' },
      series: [{ type: 'bar', data: [5,20,36,10] }]
    })
  }
}

