<template>
  <div class="container">
    <el-carousel :interval="3000" autoplay height="360px">
      <el-carousel-item v-for="p in carousel" :key="p.id">
        <img :src="img(p)" class="banner-pic" loading="lazy" decoding="async" />
        <div class="banner-caption">
          <div class="banner-title">{{ p.name }}</div>
          <div class="banner-price">¥{{ p.price }}</div>
          <el-button type="primary" size="small" @click="$router.push('/product/'+p.id)">查看详情</el-button>
        </div>
      </el-carousel-item>
    </el-carousel>
    <div class="section-title">分类推荐</div>
    <div class="grid">
      <el-card v-for="c in cats" :key="c.id" class="card" @click="goCategory(c)" style="cursor:pointer">
        <div>{{ c.name }}</div>
      </el-card>
    </div>
    <div class="section-title">热销商品</div>
    <div class="grid">
      <el-card v-for="p in hot" :key="p.id" class="card">
        <img :src="img(p)" class="pic" loading="lazy" decoding="async" />
        <div class="card-footer">
          <span>{{ p.name }}</span>
          <span class="price">¥{{ p.price }}</span>
        </div>
        <el-button type="primary" style="width:100%" @click="$router.push('/product/'+p.id)">查看详情</el-button>
      </el-card>
    </div>
    <div ref="sentinel" style="height:1px"></div>
    <div style="text-align:center;margin:12px 0" v-if="loading">正在加载...</div>
    <div style="text-align:center;margin:12px 0;color:#909399" v-if="finished">已无更多</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getCategories } from '../api/categories'
import { getProducts } from '../api/products'

const cats = ref([])
const hot = ref([])
const carousel = ref([])
const page = ref(1)
const size = ref(8)
const total = ref(0)
const loading = ref(false)
const finished = ref(false)
const sentinel = ref(null)
const router = useRouter()

onMounted(async () => {
  try {
    cats.value = await getCategories()
  } catch (e) {
    cats.value = []
  }
  try {
    const resTop = await getProducts({ page: 1, size: 10 })
    carousel.value = resTop.list || []
  } catch (e) {
    carousel.value = []
  }
  await loadMore()
  setupObserver()
})

async function loadMore() {
  if (loading.value || finished.value) return
  loading.value = true
  try {
    const res = await getProducts({ sort: 'sales', page: page.value, size: size.value })
    const list = res.list || []
    if (page.value === 1) {
      hot.value = list
    } else {
      hot.value = hot.value.concat(list)
    }
    total.value = res.total || (hot.value.length)
    page.value += 1
    if (hot.value.length >= total.value || list.length === 0) {
      finished.value = true
    }
  } catch (e) {
    // swallow error and stop loading
    finished.value = true
  } finally {
    loading.value = false
  }
}

function setupObserver() {
  if (!('IntersectionObserver' in window)) return
  const obs = new IntersectionObserver((entries) => {
    const entry = entries[0]
    if (entry && entry.isIntersecting) {
      loadMore()
    }
  })
  if (sentinel.value) obs.observe(sentinel.value)
}

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

function goCategory(c) {
  const id = c && c.id
  if (!id) return
  window.scrollTo({ top: 0, behavior: 'smooth' })
  router.push({ path: '/products', query: { categoryId: String(id), sort: 'sales', page: '1', size: String(size.value) } })
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
.banner-pic { width: 100%; height: 360px; object-fit: cover }
.banner-caption { position: absolute; left: 24px; bottom: 24px; background: rgba(0,0,0,0.4); color: #fff; padding: 12px 16px; border-radius: 8px; display:flex; gap:12px; align-items:center }
.banner-title { font-weight: 600 }
.banner-price { color: #FFD04B }
</style>
