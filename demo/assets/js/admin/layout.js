var AdminLayout = {
  template:
    '<div class=admin-shell>' +
    '<div class=admin-header>' +
    '<div class=brand>商城后台</div>' +
    '<div class=nav-actions>' +
    '<el-input placeholder=搜索... style=width:240px>' +
    '<el-button slot=append icon=el-icon-search></el-button>' +
    '</el-input>' +
    '</div>' +
    '</div>' +
    '<div class=admin-body>' +
    '<div class=admin-sidebar>' +
    '<el-menu :default-active="$route.path">' +
    '<el-menu-item index="/admin" @click="$router.push(\'/admin\')">' +
    '后台首页</el-menu-item>' +
    '<el-menu-item index="/admin/products" ' +
    '@click="$router.push(\'/admin/products\')">商品管理</el-menu-item>' +
    '<el-menu-item index="/admin/categories" ' +
    '@click="$router.push(\'/admin/categories\')">分类管理</el-menu-item>' +
    '<el-menu-item index="/admin/users" ' +
    '@click="$router.push(\'/admin/users\')">用户管理</el-menu-item>' +
    '<el-menu-item index="/admin/orders" ' +
    '@click="$router.push(\'/admin/orders\')">订单管理</el-menu-item>' +
    '<el-menu-item index="/admin/stats" ' +
    '@click="$router.push(\'/admin/stats\')">数据统计</el-menu-item>' +
    '</el-menu>' +
    '</div>' +
    '<div class=admin-main>' +
    '<router-view></router-view>' +
    '<div class=footer>© 2025 商城后台</div>' +
    '</div>' +
    '</div>' +
    '</div>'
}
