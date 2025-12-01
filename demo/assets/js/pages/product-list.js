var ProductList = {
  template:
    '<div class=container>' +
    '<div style=display:flex;gap:16px;margin-bottom:16px>' +
    '<el-select v-model="categoryId" placeholder=分类 ' +
    'style=width:160px clearable>' +
    '<el-option v-for="c in cats" :key="c.id" ' +
    ':label="c.name" :value="c.id"></el-option>' +
    '</el-select>' +
    '<el-input v-model.number="minPrice" placeholder=最低价 ' +
    'style=width:120px></el-input>' +
    '<el-input v-model.number="maxPrice" placeholder=最高价 ' +
    'style=width:120px></el-input>' +
    '<el-select v-model="sort" placeholder=排序 ' +
    'style=width:160px clearable>' +
    '<el-option label=综合 value=""></el-option>' +
    '<el-option label=销量优先 value=sales></el-option>' +
    '<el-option label=价格升序 value=price_asc></el-option>' +
    '<el-option label=价格降序 value=price_desc></el-option>' +
    '</el-select>' +
    '</div>' +
    '<div class=grid>' +
    '<el-card v-for="p in list" :key="p.id" class=card>' +
    '<img :src="img(p)" ' +
    'style=width:100%;height:240px;object-fit:cover>' +
    '<div style=padding:12px>' +
    '{{p.name}} ' +
    '<span style=float:right;color:#F56C6C>' +
    '¥{{p.price}}' +
    '</span></div>' +
    '<div class=table-actions>' +
    '<el-button type=primary @click="buy(p)">购买</el-button>' +
    '<el-button @click="$router.push(\'/product/\'+p.id)">详情</el-button>' +
    '</div>' +
    '</el-card>' +
    '</div>' +
    '<div class=pagination-center>' +
    '<el-pagination layout="prev, pager, next" ' +
    ':total="total" :current-page.sync="page" ' +
    ':page-size="size" @current-change="load"></el-pagination>' +
    '</div>' +
    '</div>',
  data: function () {
    return {
      cats: store.cats(),
      categoryId: null,
      minPrice: null,
      maxPrice: null,
      sort: '',
      list: [],
      total: 0,
      page: 1,
      size: 8
    }
  },
  mounted: function () { this.load() },
  methods: {
    img: function (p) {
      var seed = (p.images && p.images[0]) || ('p' + p.id)
      return 'https://picsum.photos/seed/' + seed + '/640/360'
    },
    buy: function (p) {
      store.addToCart(p.id, 1)
      this.$message.success('已加入购物车')
      this.$router.push('/order/submit')
    },
    load: function () {
      var q = {
        categoryId: this.categoryId,
        minPrice: this.minPrice,
        maxPrice: this.maxPrice,
        sort: this.sort,
        page: this.page,
        size: this.size
      }
      var r = store.getProducts(q)
      this.list = r.list
      this.total = r.total
    }
  }
}

