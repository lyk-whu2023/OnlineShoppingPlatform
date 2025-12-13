<template>
  <div class="container">
    <div style="margin-bottom:12px">
      <el-button @click="reload" type="primary">刷新</el-button>
      <el-button @click="clearAll" type="danger">清空购物车</el-button>
    </div>
    <el-table :data="items" style="width:100%" @selection-change="onSelChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column label="商品" width="140">
        <template #default="scope">
          <img :src="img(scope.row)" class="thumb" />
        </template>
      </el-table-column>
      <el-table-column label="名称" prop="name" />
      <el-table-column label="数量" width="180">
        <template #default="scope">
          <el-input-number v-model="scope.row.qty" @change="q=>update(scope.row.productId,q)" :min="1" />
        </template>
      </el-table-column>
      <el-table-column label="单价" width="120">
        <template #default="scope">
          <span>¥{{ scope.row.price }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button type="danger" size="small" @click="remove(scope.row.productId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="bulk-actions">
      <el-button type="success" :disabled="selected.length===0" @click="buySelected">购买选中商品</el-button>
    </div>
  </div>
  </template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCart, updateCartItem, removeCartItem, clearCart } from '../api/cart'
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'

const items = ref([])
const selected = ref([])
const router = useRouter()
async function reload() { items.value = await getCart() }
onMounted(reload)
async function update(productId, qty) { items.value = await updateCartItem(productId, qty) }
async function remove(productId) { 
  try {
    await ElMessageBox.confirm('确认删除该商品？', '确认操作', { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' })
    items.value = await removeCartItem(productId) 
  } catch (e) {}
}
async function clearAll() { 
  try {
    await ElMessageBox.confirm('确认清空购物车？', '确认操作', { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' })
    await clearCart(); 
    await reload() 
  } catch (e) {}
}
function onSelChange(rows) { selected.value = rows }
function img(it) {
  const v = it.image
  if (typeof v === 'string' && (/^https?:\/\//.test(v) || v.startsWith('data:'))) return v
  const base = (() => {
    const envBase = import.meta.env.VITE_API_BASE
    if (envBase) return envBase
    const { hostname, port } = window.location
    if (port === '10001' || hostname === 'localhost') return 'http://localhost:10002/api'
    return '/api'
  })().replace(/\/api$/, '')
  if (typeof v === 'string' && v && v.startsWith('/')) return base + v
  const seed = v || ('p'+it.productId)
  return 'https://picsum.photos/seed/' + seed + '/240/160'
}
async function buySelected() {
  const payload = { items: selected.value.map(r => ({ productId: r.productId, qty: r.qty })) }
  localStorage.setItem('pendingPurchase', JSON.stringify(payload))
  router.push('/order/submit')
}
</script>

<style scoped>
.container { max-width: 1080px; margin: 0 auto }
.thumb { width: 120px; height: 80px; object-fit: cover }
.bulk-actions { margin-top: 12px; display: flex; justify-content: flex-end }
</style>
