<template>
  <div class="container">
    <el-tabs v-model="tab">
      <el-tab-pane label="全部" name="all" />
      <el-tab-pane label="待支付" name="CREATED" />
      <el-tab-pane label="已支付" name="PAID" />
      <el-tab-pane label="已发货" name="SHIPPED" />
      <el-tab-pane label="已完成" name="COMPLETED" />
    </el-tabs>
    <el-table :data="rows" style="width:100%">
      <el-table-column label="订单ID" prop="id" width="160" />
      <el-table-column label="金额" prop="amount" width="160" />
      <el-table-column label="状态" prop="status" width="160" />
      <el-table-column label="创建时间" prop="createdAt" />
    </el-table>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { listOrders } from '../api/orders'

const tab = ref('all')
const rows = ref([])

async function load() {
  let userId = localStorage.getItem('userId')
  const token = localStorage.getItem('token')
  if (!userId && token && token.startsWith('token-')) userId = token.substring(6)
  if (tab.value === 'all') rows.value = await listOrders({ userId })
  else rows.value = await listOrders({ status: tab.value })
}
watch(tab, load)
onMounted(load)
</script>

<style scoped>
.container { max-width: 1080px; margin: 0 auto }
</style>
