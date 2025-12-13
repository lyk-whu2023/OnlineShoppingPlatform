<template>
  <div class="container">
    <div class="detail">
      <div class="left">
        <el-carousel height="360px">
          <el-carousel-item v-for="seed in seeds" :key="seed">
            <img :src="url(seed)" class="bigpic" />
          </el-carousel-item>
        </el-carousel>
      </div>
      <div class="right">
        <div class="title">{{ p && p.name }}</div>
        <div class="price">¥{{ p && p.price }}</div>
        <div class="stock">库存 {{ p && p.stock }}</div>
        <div class="desc">{{ p && p.description }}</div>
        <el-input-number v-model="num" :min="1" :max="5" />
        <div class="actions">
          <el-button type="primary" @click="add">加入购物车</el-button>
          <el-button type="success" @click="buyNow">立即购买</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getProduct } from '../api/products'
import { addCartItem } from '../api/cart'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const p = ref(null)
const seeds = ref([])
const num = ref(1)
const router = useRouter()

onMounted(async () => {
  const id = route.params.id
  try {
    const data = await getProduct(id)
    p.value = data
    seeds.value = data && data.images && data.images.length ? data.images : ['p'+id]
  } catch (e) {
    p.value = null
    seeds.value = ['p'+id]
  }
})

function url(v) {
  if (typeof v === 'string' && (/^https?:\/\//.test(v) || v.startsWith('data:'))) return v
  const base = (() => {
    const envBase = import.meta.env.VITE_API_BASE
    if (envBase) return envBase
    const { hostname, port } = window.location
    if (port === '10001' || hostname === 'localhost') return 'http://localhost:10002/api'
    return '/api'
  })().replace(/\/api$/, '')
  if (typeof v === 'string' && v.startsWith('/')) return base + v
  const seed = v || (p.value ? ('p'+p.value.id) : 'p1')
  return 'https://picsum.photos/seed/' + seed + '/720/360'
}

async function add() {
  if (!p.value || !p.value.id) return
  try {
    await ElMessageBox.confirm('确认将该商品加入购物车？', '确认操作', { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' })
    await addCartItem({ productId: p.value.id, qty: num.value })
    ElMessage.success('已加入购物车')
  } catch (e) {
    if (e && e !== 'cancel') ElMessage.error(String(e.message || '加入购物车失败'))
  }
}

async function buyNow() {
  if (!p.value || !p.value.id) return
  try {
    await ElMessageBox.confirm('确认立即购买该商品？', '确认操作', { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' })
    const pending = { items: [{ productId: p.value.id, qty: num.value }] }
    localStorage.setItem('pendingPurchase', JSON.stringify(pending))
    router.push('/order/submit')
  } catch (e) {
    if (e && e !== 'cancel') ElMessage.error(String(e.message || '立即购买失败'))
  }
}
</script>

<style scoped>
.container { max-width: 1080px; margin: 0 auto }
.detail { display: flex; gap: 24px }
.left { flex: 1 }
.right { flex: 1 }
.bigpic { width: 100%; height: 360px; object-fit: cover }
.title { font-size: 20px; margin-bottom: 8px }
.price { color: #F56C6C; font-size: 24px; margin-bottom: 8px }
.stock { margin-bottom: 8px }
.desc { margin-bottom: 16px; color: #606266 }
.actions { margin-top: 16px; display: flex; gap: 12px }
</style>
