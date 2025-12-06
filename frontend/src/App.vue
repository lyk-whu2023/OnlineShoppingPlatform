<template>
  <el-container>
    <el-header>
      <div class="nav">
        <div class="brand" @click="$router.push('/')">Shop</div>
        <div class="links">
          <el-link @click="$router.push('/products')">商品</el-link>
          <el-link @click="$router.push('/cart')">购物车</el-link>
          <el-link @click="$router.push('/user')">个人中心</el-link>
          <el-link @click="$router.push('/admin')">Admin</el-link>
        </div>
      </div>
    </el-header>
    <el-main>
      <router-view></router-view>
    </el-main>
    <el-footer>© 2025 Online Shopping Platform</el-footer>
  </el-container>
</template>

<style scoped>
.nav { display: flex; align-items: center; justify-content: space-between }
.brand { font-weight: 600; font-size: 18px; cursor: pointer }
.links { display: flex; gap: 16px }
</style>
