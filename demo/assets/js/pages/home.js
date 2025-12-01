var Home = {
  template:
    '<div class=container>' +
    '<el-carousel height=360px>' +
    '<el-carousel-item></el-carousel-item>' +
    '<el-carousel-item></el-carousel-item>' +
    '</el-carousel>' +
    '<div class=section-title>分类推荐</div>' +
    '<div class=grid>' +
    '<el-card v-for="c in cats" :key="c.id" class=card>' +
    '<div>{{c.name}}</div>' +
    '</el-card>' +
    '</div>' +
    '<div class=section-title>热销商品</div>' +
    '<div class=grid>' +
    '<el-card v-for="p in hot" :key="p.id" class=card>' +
    '<img :src="img(p)" ' +
    'style=width:100%;height:240px;object-fit:cover>' +
    '<div style=padding:12px>' +
    '{{p.name}} ' +
    '<span style=float:right;color:#F56C6C>' +
    '¥{{p.price}}' +
    '</span></div>' +
    '<el-button type=primary style=width:100% ' +
    '@click="$router.push(\'/product/\'+p.id)"' +
    '>查看详情</el-button>' +
    '</el-card>' +
    '</div>' +
    '</div>',
  data: function () {
    return { cats: store.cats(), hot: store.hot(8) }
  },
  methods: {
    img: function (p) {
      var seed = (p.images && p.images[0]) || ('p' + p.id)
      return 'https://picsum.photos/seed/' + seed + '/640/360'
    }
  }
}

