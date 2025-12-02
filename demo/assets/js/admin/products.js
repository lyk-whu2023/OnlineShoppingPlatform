var AdminProducts = {
  template:
    '<div>' +
    '<div style=margin-bottom:12px>' +
    '<el-button type=primary @click="openAdd">新增商品</el-button>' +
    '</div>' +
    '<el-table :data="rows" style=width:100%>' +
    '<el-table-column label=商品图 width=120>' +
    '<template slot-scope=scope>' +
    '<img :src="img(scope.row)" style=width:120px;height:80px;' +
    'object-fit:cover>' +
    '</template>' +
    '</el-table-column>' +
    '<el-table-column label=名称 prop=name></el-table-column>' +
    '<el-table-column label=分类 width=160>' +
    '<template slot-scope=scope>{{catName(scope.row.categoryId)}}</template>' +
    '</el-table-column>' +
    '<el-table-column label=单价 width=120 prop=price></el-table-column>' +
    '<el-table-column label=库存 width=120 prop=stock></el-table-column>' +
    '<el-table-column label=操作 width=220>' +
    '<template slot-scope=scope>' +
    '<div class=table-actions>' +
    '<el-button type=primary size=mini @click="edit(scope.row)">编辑</el-button>' +
    '<el-button type=danger size=mini @click="del(scope.row.id)">删除</el-button>' +
    '</div>' +
    '</template>' +
    '</el-table-column>' +
    '</el-table>' +
    '<el-dialog title=新增/编辑商品 :visible.sync=visible width=600px>' +
    '<el-form label-width=100px>' +
    '<el-form-item label=商品名称>' +
    '<el-input v-model="form.name"></el-input>' +
    '</el-form-item>' +
    '<el-form-item label=分类>' +
    '<el-select v-model="form.categoryId">' +
    '<el-option v-for="c in cats" :label="c.name" :value="c.id" ' +
    ':key="c.id"></el-option>' +
    '</el-select>' +
    '</el-form-item>' +
    '<el-form-item label=价格>' +
    '<el-input v-model.number="form.price"></el-input>' +
    '</el-form-item>' +
    '<el-form-item label=库存>' +
    '<el-input v-model.number="form.stock"></el-input>' +
    '</el-form-item>' +
    '<el-form-item label=描述>' +
    '<el-input type=textarea v-model="form.desc"></el-input>' +
    '</el-form-item>' +
    '<span slot=footer class=dialog-footer>' +
    '<el-button @click="visible=false">取消</el-button>' +
    '<el-button type=primary @click="save">保存</el-button>' +
    '</span>' +
    '</el-form>' +
    '</el-dialog>' +
    '</div>',
  data: function () {
    return {
      rows: [],
      visible: false,
      form: { id: null, name: '', categoryId: null, price: 0, stock: 0, desc: '' },
      cats: store.cats()
    }
  },
  mounted: function () { this.load() },
  methods: {
    img: function (p) {
      var seed = (p.images && p.images[0]) || ('p' + p.id)
      return 'https://picsum.photos/seed/' + seed + '/120/80'
    },
    catName: function (cid) {
      var c = this.cats.find(function (x) { return x.id == cid })
      return c ? c.name : ''
    },
    load: function () { this.rows = store.prods() },
    openAdd: function () {
      this.form = { id: null, name: '', categoryId: null, price: 0, stock: 0, desc: '' }
      this.visible = true
    },
    edit: function (row) {
      this.form = Object.assign({}, row)
      this.visible = true
    },
    save: function () {
      if (this.form.id) { store.updateProduct(this.form) }
      else { store.createProduct(this.form) }
      this.visible = false
      this.load()
    },
    del: function (id) { store.deleteProduct(id); this.load() }
  }
}

