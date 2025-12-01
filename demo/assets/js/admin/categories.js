var AdminCategories = {
  template:
    '<div>' +
    '<div style=margin-bottom:12px>' +
    '<el-button type=primary @click="openAdd">新增分类</el按钮>' +
    '</div>' +
    '<el-table :data="rows" style=width:100%>' +
    '<el-table-column label=ID width=120 prop=id></el-table-column>' +
    '<el-table-column label=分类名 prop=name></el-table-column>' +
    '<el-table-column label=操作 width=220>' +
    '<template slot-scope=scope>' +
    '<div class=table-actions>' +
    '<el-button type=primary size=mini @click="edit(scope.row)">编辑</el按钮>' +
    '<el-button type=danger size=mini @click="del(scope.row.id)">删除</el按钮>' +
    '</div>' +
    '</template>' +
    '</el-table-column>' +
    '</el-table>' +
    '<el-dialog title=新增/编辑分类 :visible.sync=visible width=420px>' +
    '<el-form label-width=100px>' +
    '<el-form-item label=分类名>' +
    '<el-input v-model="form.name"></el-input>' +
    '</el-form-item>' +
    '<span slot=footer class=dialog-footer>' +
    '<el-button @click="visible=false">取消</el按钮>' +
    '<el-button type=primary @click="save">保存</el按钮>' +
    '</span>' +
    '</el-form>' +
    '</el-dialog>' +
    '</div>',
  data: function () { return { rows: [], visible: false, form: { id: null, name: '' } } },
  mounted: function () { this.load() },
  methods: {
    load: function () { this.rows = store.cats() },
    openAdd: function () { this.form = { id: null, name: '' }; this.visible = true },
    edit: function (row) { this.form = Object.assign({}, row); this.visible = true },
    save: function () {
      if (this.form.id) { store.updateCategory(this.form) }
      else { store.createCategory({ name: this.form.name }) }
      this.visible = false
      this.load()
    },
    del: function (id) { store.deleteCategory(id); this.load() }
  }
}

