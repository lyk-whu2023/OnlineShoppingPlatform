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
const AdminDashboard = () => import('../views/admin/AdminDashboard.vue')
const AdminProducts = () => import('../views/admin/AdminProducts.vue')
const AdminCategories = () => import('../views/admin/AdminCategories.vue')
const AdminUsers = () => import('../views/admin/AdminUsers.vue')
const AdminOrders = () => import('../views/OrderListView.vue')
const AdminStats = () => import('../views/admin/AdminDashboard.vue')

const routes = [
  { path: '/', component: HomeView },
  { path: '/products', component: ProductListView },
  { path: '/product/:id', component: ProductDetailView },
  { path: '/login', component: AuthView },
  { path: '/register', component: AuthView },
  { path: '/cart', component: CartView },
  { path: '/order/submit', component: OrderSubmitView },
  { path: '/user', component: UserCenterView },
  { path: '/user/address', component: AddressView },
  { path: '/user/orders', component: OrderListView },
  { path: '/order/:id', component: OrderDetailView },
  { path: '/admin/login', component: AdminLogin },
  {
    path: '/admin',
    component: AdminLayout,
    children: [
      { path: '', component: AdminDashboard },
      { path: 'products', component: AdminProducts },
      { path: 'categories', component: AdminCategories },
      { path: 'users', component: AdminUsers },
      { path: 'orders', component: AdminOrders },
      { path: 'stats', component: AdminStats }
    ]
  }
]

const router = createRouter({ history: createWebHistory(), routes })
export default router
