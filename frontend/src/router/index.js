import { createRouter, createWebHistory } from 'vue-router'

const HomeView = () => import('../views/HomeView.vue')
const ProductListView = () => import('../views/ProductListView.vue')
const ProductDetailView = () => import('../views/ProductDetailView.vue')
const AuthView = () => import('../views/AuthView.vue')
const CartView = () => import('../views/CartView.vue')
const OrderSubmitView = () => import('../views/OrderSubmitView.vue')
const UserCenterView = () => import('../views/UserCenterView.vue')
const AddressView = () => import('../views/AddressView.vue')
const OrderListView = () => import('../views/OrderListView.vue')
const OrderDetailView = () => import('../views/OrderDetailView.vue')
const AdminLogin = () => import('../views/admin/AdminLogin.vue')
const AdminLayout = () => import('../views/admin/AdminLayout.vue')
const AdminProducts = () => import('../views/admin/AdminProducts.vue')
const AdminCategories = () => import('../views/admin/AdminCategories.vue')
const AdminUsers = () => import('../views/admin/AdminUsers.vue')
const AdminOrders = () => import('../views/admin/AdminOrders.vue')
const AdminStats = () => import('../views/admin/AdminStats.vue')

const routes = [
  { path: '/', component: HomeView },
  { path: '/products', component: ProductListView },
  { path: '/product/:id', component: ProductDetailView, meta: { requiresAuth: true } },
  { path: '/login', component: AuthView },
  { path: '/register', component: AuthView },
  { path: '/cart', component: CartView, meta: { requiresAuth: true } },
  { path: '/order/submit', component: OrderSubmitView, meta: { requiresAuth: true } },
  { path: '/user', component: UserCenterView, meta: { requiresAuth: true } },
  { path: '/user/address', component: AddressView, meta: { requiresAuth: true } },
  { path: '/user/orders', component: OrderListView, meta: { requiresAuth: true } },
  { path: '/order/:id', component: OrderDetailView, meta: { requiresAuth: true } },
  { path: '/admin/login', component: AdminLogin },
  {
    path: '/admin',
    component: AdminLayout,
    children: [
      { path: '', redirect: '/admin/products' },
      { path: 'products', component: AdminProducts, meta: { requiresAdmin: true } },
      { path: 'categories', component: AdminCategories, meta: { requiresAdmin: true } },
      { path: 'users', component: AdminUsers, meta: { requiresAdmin: true } },
      { path: 'orders', component: AdminOrders, meta: { requiresAdmin: true } },
      { path: 'stats', component: AdminStats, meta: { requiresAdmin: true } }
    ]
  }
]

const router = createRouter({ history: createWebHistory(), routes })

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const user = localStorage.getItem('user')
  const role = user ? (JSON.parse(user).role || '') : ''
  if (to.meta && to.meta.requiresAuth && !token) {
    next({ path: '/login', query: { redirect: to.fullPath } })
  } else if (to.path.startsWith('/admin') && to.meta && to.meta.requiresAdmin && role !== 'admin') {
    next({ path: '/admin/login' })
  } else {
    next()
  }
})

export default router
