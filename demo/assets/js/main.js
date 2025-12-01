new Vue({
  el: '#app',
  router: router,
  data: { breadcrumb: '' },
  watch: {
    '$route': function (r) {
      var map = {
        '/': '首页',
        '/products': '商品列表',
        '/cart': '购物车',
        '/order/submit': '订单提交'
      }
      this.breadcrumb = map[r.path] || ''
    }
  },
  mounted: function () { this.breadcrumb = '首页' }
})

