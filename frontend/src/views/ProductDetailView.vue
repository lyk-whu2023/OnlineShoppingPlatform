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
          <el-button type="primary">加入购物车</el-button>
          <el-button type="success" @click="$router.push('/order/submit')">立即购买</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getProduct } from '../api/products'

const route = useRoute()
const p = ref(null)
const seeds = ref([])
const num = ref(1)

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

function url(seed) {
  return 'https://picsum.photos/seed/' + seed + '/720/360'
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
