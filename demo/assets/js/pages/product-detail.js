var ProductDetail = {
  template:
    '<div class=container>' +
    '<div style=display:flex;gap:24px>' +
    '<div style=flex:1>' +
    '<el-carousel height=360px>' +
    '<el-carousel-item v-for="seed in seeds" :key="seed">' +
    '<img :src="url(seed)" ' +
    'style=width:100%;height:360px;object-fit:cover>' +
    '</el-carousel-item>' +
    '</el-carousel>' +
    '</div>' +
    '<div style=flex:1>' +
    '<div style=font-size:20px;margin-bottom:8px>' +
    '{{p.name}}' +
    '</div>' +
    '<div style=color:#F56C6C;font-size:24px;margin-bottom:8px>' +
    '¥{{p.price}}' +
    '</div>' +
    '<div style=margin-bottom:8px>库存 {{p.stock}}</div>' +
    '<div style=margin-bottom:16px;color:#606266>' +
    '{{p.desc}}' +
    '</div>' +
    '<el-input-number min=1 max=5 v-model="num"></el-input-number>' +
    '<div style=margin-top:16px;display:flex;gap:12px>' +
    '<el-button type=primary @click="addCart">加入购物车</el-button>' +
    '<el-button type=success @click="$router.push(\'/order/submit\')">' +
    '立即购买</el-button>' +
    '</div>' +
    '</div>' +
    '</div>' +
    '</div>',
  data: function () {
    return { p: {}, num: 1, seeds: [] }
  },
  mounted: function () {
    var id = this.$route.params.id
    var pr = store.getProduct(id)
    this.p = pr || {}
    this.seeds = (pr && pr.images && pr.images.length)
      ? pr.images
      : ['p' + id]
  },
  methods: {
    url: function (seed) {
      return 'https://picsum.photos/seed/' + seed + '/720/360'
    },
    addCart: function () {
      if (!this.p.id) { return }
      store.addToCart(this.p.id, this.num)
      this.$message.success('已加入购物车')
    }
  }
}

