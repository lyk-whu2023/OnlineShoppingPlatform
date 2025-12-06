<template>
  <el-container>
    <el-header>
      <div class="nav">
        <div class="brand" @click="$router.push('/')">商城</div>
        <div class="links">
          <el-link @click="$router.push('/products')">商品</el-link>
          <el-link @click="$router.push('/cart')">购物车</el-link>
          <el-link @click="$router.push('/user')">个人中心</el-link>
          <el-link v-if="isAdmin" @click="$router.push('/admin')">后台</el-link>
        </div>
      </div>
    </el-header>
    <el-main>
      <router-view></router-view>
    </el-main>
    <el-footer>© 2025 Online Shopping Platform</el-footer>
  </el-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import router from './router'
const isAdmin = ref(false)
onMounted(() => {
  try {
    const raw = localStorage.getItem('user')
    const u = raw ? JSON.parse(raw) : null
    isAdmin.value = !!u && u.role === 'admin'
  } catch (e) {
    isAdmin.value = false
  }
  router.afterEach(() => {
    try {
      const raw = localStorage.getItem('user')
      const u = raw ? JSON.parse(raw) : null
      isAdmin.value = !!u && u.role === 'admin'
    } catch (e) {
      isAdmin.value = false
    }
  })
})
</script>

<style scoped>
.nav { display: flex; align-items: center; justify-content: space-between }
.brand { font-weight: 600; font-size: 18px; cursor: pointer }
.links { display: flex; gap: 16px }
</style>
