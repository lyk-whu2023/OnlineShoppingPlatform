<template>
  <div class="container">
    <div style="margin-bottom:12px">
      <div class="section-title">我的订单</div>
    </div>
    <el-tabs v-model="tab">
      <el-tab-pane label="全部" name="all" />
      <el-tab-pane label="待支付" name="CREATED" />
      <el-tab-pane label="已支付" name="PAID" />
      <el-tab-pane label="已发货" name="SHIPPED" />
      <el-tab-pane label="已完成" name="COMPLETED" />
    </el-tabs>
    <el-table :data="displayRows" style="width:100%">
      <el-table-column label="订单编号" prop="id" width="160" />
      <el-table-column label="金额" prop="amount" width="160" />
      <el-table-column label="状态" prop="status" width="160" />
      <el-table-column label="创建时间" prop="createdAt" />
    </el-table>
    <div style="display:flex; justify-content:flex-end; margin-top:12px">
      <el-pagination
        background
        layout="prev, pager, next, sizes, total"
        :total="total"
        :page-size="size"
        :current-page="page"
        @size-change="onSize"
        @current-change="onPage"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, computed } from 'vue'
import { listOrders } from '../api/orders'

const tab = ref('all')
const rows = ref([])
const page = ref(1)
const size = ref(20)
const total = ref(0)
const filtered = computed(() => {
  if (tab.value === 'all') return rows.value
  return rows.value.filter(x => x.status === tab.value)
})
const displayRows = computed(() => {
  const start = (page.value - 1) * size.value
  return filtered.value.slice(start, start + size.value)
})

async function load() {
  let userId = localStorage.getItem('userId')
  const token = localStorage.getItem('token')
  if (!userId && token && token.startsWith('token-')) userId = token.substring(6)
  rows.value = await listOrders({ userId })
  total.value = filtered.value.length
}
watch(tab, () => { page.value = 1; load() })
onMounted(load)
function onSize(val) { size.value = val; page.value = 1 }
function onPage(val) { page.value = val }
</script>

<style scoped>
.container { max-width: 1080px; margin: 0 auto }
.section-title { font-weight: 600; margin: 8px 0 12px }
</style>
