<template>
  <div class="container">
    <div class="section-title">我的收藏</div>
    <div class="grid">
      <el-card v-for="p in list" :key="p.id" class="card">
        <img :src="img(p)" class="pic" />
        <div class="card-footer">
          <span>{{ p.name }}</span>
          <span class="price">¥{{ p.price }}</span>
        </div>
        <div class="actions">
          <el-button type="primary" @click="$router.push('/product/'+p.id)">查看详情</el-button>
          <el-button type="danger" @click="remove(p.id)">取消收藏</el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getFavorites, removeFavorite } from '../api/favorites'
import { ElMessage } from 'element-plus'

const list = ref([])

onMounted(async () => { await load() })

async function load() {
  try { list.value = await getFavorites() } catch { list.value = [] }
}

async function remove(id) {
  try {
    await removeFavorite(id)
    await load()
    ElMessage.success('已取消收藏')
  } catch (e) {
    ElMessage.error(String(e.message || '操作失败'))
  }
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
</script>

<style scoped>
.container { max-width: 1080px; margin: 0 auto }
.section-title { margin: 16px 0; font-weight: 600 }
.grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 12px }
.card { padding: 0 }
.pic { width: 100%; height: 240px; object-fit: cover }
.card-footer { padding: 12px; display: flex; justify-content: space-between }
.actions { display: flex; gap: 8px; padding: 12px }
.price { color: #F56C6C }
</style>
