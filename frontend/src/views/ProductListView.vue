<template>
  <div class="container">
    <el-card>
      <div class="filters">
        <el-select v-model="q.categoryId" placeholder="分类" style="width:160px">
          <el-option v-for="c in cats" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
        <el-input v-model="q.keyword" placeholder="关键词" style="width:240px" />
        <el-select v-model="q.sort" placeholder="排序" style="width:160px">
          <el-option label="价格升序" value="priceAsc" />
          <el-option label="价格降序" value="priceDesc" />
          <el-option label="销量优先" value="sales" />
        </el-select>
        <el-button type="primary" @click="load">搜索</el-button>
      </div>
    </el-card>
    <div class="grid">
      <el-card v-for="p in list" :key="p.id" class="card">
        <img :src="img(p)" class="pic" />
        <div class="card-footer">
          <span>{{ p.name }}</span>
          <span class="price">¥{{ p.price }}</span>
        </div>
        <el-button type="primary" style="width:100%" @click="$router.push('/product/'+p.id)">查看详情</el-button>
      </el-card>
    </div>
    <div class="pagination">
      <el-pagination layout="prev, pager, next" :current-page="q.page" :page-size="q.size" :total="total" @current-change="page" />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getProducts } from '../api/products'
import { getCategories } from '../api/categories'

const q = reactive({ categoryId: undefined, keyword: '', sort: 'sales', page: 1, size: 8 })
const list = ref([])
const total = ref(0)
const cats = ref([])

onMounted(async () => {
  try { cats.value = await getCategories() } catch (e) { cats.value = [] }
  load()
})

async function load() {
  try {
    const params = { ...q }
    if (params.sort === 'sales') delete params.sort
    const res = await getProducts(params)
    list.value = res.list || []
    total.value = res.total || 0
  } catch (e) {
    list.value = []
    total.value = 0
  }
}

function page(p) { q.page = p; load() }

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
.filters { display: flex; gap: 12px; align-items: center }
.grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 12px; margin-top: 12px }
.card { padding: 0 }
.pic { width: 100%; height: 240px; object-fit: cover }
.card-footer { padding: 12px; display: flex; justify-content: space-between }
.price { color: #F56C6C }
.pagination { display: flex; justify-content: center; margin: 16px 0 }
</style>
