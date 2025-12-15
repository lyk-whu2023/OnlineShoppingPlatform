<template>
  <div>
    <div style="margin-bottom:12px; display:flex; gap:12px; align-items:center">
      <el-input v-model="keyword" placeholder="按用户ID/订单ID搜索" style="width:260px" />
      <el-button type="primary" @click="onSearch">搜索</el-button>
      <el-button @click="reset">重置</el-button>
    </div>
    <el-tabs v-model="tab" style="margin-bottom:12px">
      <el-tab-pane label="全部" name="all" />
      <el-tab-pane label="待支付" name="CREATED" />
      <el-tab-pane label="已支付" name="PAID" />
      <el-tab-pane label="已发货" name="SHIPPED" />
      <el-tab-pane label="已完成" name="COMPLETED" />
      <el-tab-pane label="已取消" name="CANCELLED" />
    </el-tabs>
    <el-table :data="rows" style="width:100%">
      <el-table-column label="订单编号" width="120" prop="id" />
      <el-table-column label="用户编号" width="120" prop="userId" />
      <el-table-column label="金额" width="160" prop="amount" />
      <el-table-column label="状态" width="220">
        <template #default="scope">
          <el-select v-model="scope.row.status" @change="s=>update(scope.row.id,s)" style="width:160px">
            <el-option label="待支付" value="CREATED" />
            <el-option label="已支付" value="PAID" />
            <el-option label="已发货" value="SHIPPED" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </template>
      </el-table-column>
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
import { ref, onMounted, watch } from 'vue'
import { listOrdersAdmin, listOrders, updateOrderStatus } from '../../api/orders'

const tab = ref('all')
const rows = ref([])
const keyword = ref('')
const page = ref(1)
const size = ref(20)
const total = ref(0)

async function load() {
  try {
    const q = { page: page.value, size: size.value, keyword: keyword.value }
    if (tab.value !== 'all') q.status = tab.value
    const res = await listOrdersAdmin(q)
    rows.value = res.list || []
    total.value = res.total || 0
  } catch (e) {
    try {
      let all = []
      if (tab.value === 'all') all = await listOrders({})
      else all = await listOrders({ status: tab.value })
      total.value = Array.isArray(all) ? all.length : 0
      const start = (page.value - 1) * size.value
      const filtered = (Array.isArray(all) ? all : []).filter(x => !keyword.value || String(x.userId||'').includes(keyword.value) || String(x.id||'').includes(keyword.value))
      rows.value = filtered.slice(start, start + size.value)
    } catch {
      rows.value = []
      total.value = 0
    }
  }
}
async function update(id, status) { await updateOrderStatus(id, status) }
watch(tab, load)
onMounted(load)
function onSearch() { page.value = 1; load() }
function reset() { keyword.value = ''; page.value = 1; load() }
function onSize(val) { size.value = val; page.value = 1; load() }
function onPage(val) { page.value = val; load() }
</script>

<style scoped>
</style>
