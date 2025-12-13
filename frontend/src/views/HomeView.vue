<template>
  <div class="container">
    <el-carousel height="360px">
      <el-carousel-item></el-carousel-item>
      <el-carousel-item></el-carousel-item>
    </el-carousel>
    <div class="section-title">分类推荐</div>
    <div class="grid">
      <el-card v-for="c in cats" :key="c.id" class="card">
        <div>{{ c.name }}</div>
      </el-card>
    </div>
    <div class="section-title">热销商品</div>
    <div class="grid">
      <el-card v-for="p in hot" :key="p.id" class="card">
        <img :src="img(p)" class="pic" />
        <div class="card-footer">
          <span>{{ p.name }}</span>
          <span class="price">¥{{ p.price }}</span>
        </div>
        <el-button type="primary" style="width:100%" @click="$router.push('/product/'+p.id)">查看详情</el-button>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCategories } from '../api/categories'
import { getProducts } from '../api/products'

const cats = ref([])
const hot = ref([])

onMounted(async () => {
  try {
    cats.value = await getCategories()
  } catch (e) {
    cats.value = []
  }
  try {
    const res = await getProducts({ sort: 'sales', page: 1, size: 8 })
    hot.value = res.list || []
  } catch (e) {
    hot.value = []
  }
})

function img(p) {
  const v = (p.images && p.images[0])
  if (typeof v === 'string' && (/^https?:\/\//.test(v) || v.startsWith('data:'))) return v
  const base = (() => {
    const envBase = import.meta.env.VITE_API_BASE
    if (envBase) return envBase
    const { hostname, port } = window.location
    if (port === '10001' || hostname === 'localhost') return 'http://localhost:10002/api'
    return '/api'
  })().replace(/\/api$/, '')
  if (typeof v === 'string' && v.startsWith('/')) return base + v
  const seed = v || ('p'+p.id)
  return 'https://picsum.photos/seed/' + seed + '/640/360'
}
</script>

<style scoped>
.container { max-width: 1080px; margin: 0 auto }
.section-title { margin: 16px 0; font-weight: 600 }
.grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 12px }
.card { padding: 0 }
.pic { width: 100%; height: 240px; object-fit: cover }
.card-footer { padding: 12px; display: flex; justify-content: space-between }
.price { color: #F56C6C }
</style>
