<template>
  <div class="container">
    <el-card v-if="order">
      <div class="title">订单详情</div>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单ID">{{ order.id }}</el-descriptions-item>
        <el-descriptions-item label="金额">¥{{ order.amount }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ order.status }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ order.createdAt }}</el-descriptions-item>
        <el-descriptions-item label="收件人">{{ order.consigneeName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ order.consigneePhone }}</el-descriptions-item>
        <el-descriptions-item label="收件地址" :span="2">{{ order.consigneeDetail }}</el-descriptions-item>
      </el-descriptions>
      <div class="subtitle">商品明细</div>
      <el-table :data="order.items || []" style="width:100%">
        <el-table-column label="商品ID" prop="productId" width="120" />
        <el-table-column label="名称" prop="name" />
        <el-table-column label="单价" prop="price" width="120" />
        <el-table-column label="数量" prop="qty" width="120" />
      </el-table>
      <div class="actions">
        <el-button @click="$router.push('/user/orders')">返回订单列表</el-button>
      </div>
    </el-card>
    <el-empty v-else description="未找到订单"></el-empty>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getOrder } from '../api/orders'

const route = useRoute()
const order = ref(null)

onMounted(async () => {
  try {
    const id = route.params.id
    order.value = await getOrder(id)
  } catch (e) {
    order.value = null
  }
})
</script>

<style scoped>
.container { max-width: 1080px; margin: 0 auto }
.title { margin-bottom: 12px; font-weight: 600 }
.subtitle { margin: 12px 0; font-weight: 600 }
.actions { margin-top: 12px }
</style>
