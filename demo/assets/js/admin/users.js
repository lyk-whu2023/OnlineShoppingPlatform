var AdminUsers = {
  template:
    '<div>' +
    '<el-table :data="rows" style=width:100%>' +
    '<el-table-column label=账号 prop=username></el-table-column>' +
    '<el-table-column label=手机号 width=160 prop=phone></el-table-column>' +
    '<el-table-column label=状态 width=160>' +
    '<template slot-scope=scope>' +
    '<el-switch :value="scope.row.status===\'enabled\'" ' +
    'active-text=启用 inactive-text=禁用 ' +
    '@change="toggle(scope.row)"></el-switch>' +
    '</template>' +
    '</el-table-column>' +
    '<el-table-column label=注册时间 width=200 prop=createdAt></el-table-column>' +
    '<el-table-column label=操作 width=160>' +
    '<template slot-scope=scope>' +
    '<el-button size=mini @click="detail(scope.row)">详情</el-button>' +
    '</template>' +
    '</el-table-column>' +
    '</el-table>' +
    '<el-drawer title=用户详情 :visible.sync=visible size=30%>' +
    '<div class=container>' +
    '<el-descriptions :column=1>' +
    '<el-descriptions-item label=账号>{{user.username}}</el-descriptions-item>' +
    '<el-descriptions-item label=手机号>{{user.phone}}</el-descriptions-item>' +
    '<el-descriptions-item label=状态>{{user.status}}</el-descriptions-item>' +
    '<el-descriptions-item label=角色>{{user.role}}</el-descriptions-item>' +
    '</el-descriptions>' +
    '</div>' +
    '</el-drawer>' +
    '</div>',
  data: function () { return { rows: [], visible: false, user: {} } },
  mounted: function () { this.rows = store.users() },
  methods: {
    toggle: function (row) { store.toggleUserStatus(row.id); this.rows = store.users() },
    detail: function (row) { this.user = row; this.visible = true }
  }
}

