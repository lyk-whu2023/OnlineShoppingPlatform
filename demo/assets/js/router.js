var routes = [
  { path: '/', component: Home },
  { path: '/products', component: ProductList },
  { path: '/product/:id', component: ProductDetail },
  { path: '/login', component: Auth },
  { path: '/register', component: Auth },
  { path: '/cart', component: Cart },
  { path: '/order/submit', component: OrderSubmit },
  { path: '/user', component: UserCenter },
  { path: '/user/address', component: Address },
  { path: '/user/orders', component: OrderList },
  { path: '/order/:id', component: OrderDetail },
  { path: '/admin/login', component: AdminLogin },
  {
    path: '/admin',
    component: AdminLayout,
    children: [
      { path: '', component: Dashboard },
      { path: 'products', component: AdminProducts },
      { path: 'categories', component: AdminCategories },
      { path: 'users', component: AdminUsers },
      { path: 'orders', component: AdminOrders },
      { path: 'stats', component: AdminStats }
    ]
  }
]

var router = new VueRouter({ routes: routes })

