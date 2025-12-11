<template>
  <div>
    <el-tabs v-model="tab" style="margin-bottom:12px">
      <el-tab-pane label="全部" name="all" />
      <el-tab-pane label="待支付" name="CREATED" />
      <el-tab-pane label="已支付" name="PAID" />
      <el-tab-pane label="已发货" name="SHIPPED" />
      <el-tab-pane label="已完成" name="COMPLETED" />
      <el-tab-pane label="已取消" name="CANCELLED" />
    </el-tabs>
    <el-table :data="rows" style="width:100%">
      <el-table-column label="订单ID" width="120" prop="id" />
      <el-table-column label="用户ID" width="120" prop="userId" />
      <el-table-column label="金额" width="160" prop="amount" />
      <el-table-column label="状态" width="220">
        <template #default="scope">
          <el-select v-model="scope.row.status" @change="s=>update(scope.row.id,s)" style="width:160px">
            <el-option label="CREATED" value="CREATED" />
            <el-option label="PAID" value="PAID" />
            <el-option label="SHIPPED" value="SHIPPED" />
            <el-option label="COMPLETED" value="COMPLETED" />
            <el-option label="CANCELLED" value="CANCELLED" />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createdAt" />
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { listOrders, updateOrderStatus } from '../../api/orders'

const tab = ref('all')
const rows = ref([])

async function load() {
  if (tab.value === 'all') rows.value = await listOrders({})
  else rows.value = await listOrders({ status: tab.value })
}
async function update(id, status) { await updateOrderStatus(id, status) }
watch(tab, load)
onMounted(load)
</script>

<style scoped>
</style>

