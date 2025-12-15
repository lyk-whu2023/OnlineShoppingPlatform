<template>
  <div>
    <div style="margin-bottom:12px; display:flex; gap:12px">
      <el-input v-model="keyword" placeholder="按用户名搜索" style="width:240px" />
      <el-button type="primary" @click="load">搜索</el-button>
    </div>
    <el-table :data="rows" style="width:100%">
      <el-table-column label="编号" width="120" prop="id" />
      <el-table-column label="用户名" prop="username" />
      <el-table-column label="手机号" width="160" prop="phone" />
      <el-table-column label="角色" width="140" prop="role" />
      <el-table-column label="状态" width="220">
        <template #default="scope">
          <el-select v-model="scope.row.status" @change="s=>updateStatus(scope.row.id,s)" style="width:160px">
            <el-option label="启用" value="ACTIVE" />
            <el-option label="停用" value="DISABLED" />
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
import { ref, onMounted } from 'vue'
import { searchUsers, listUsers, updateUserStatus } from '../../api/users'

const rows = ref([])
const keyword = ref('')
const page = ref(1)
const size = ref(20)
const total = ref(0)

async function load() {
  try {
    const res = await searchUsers({ keyword: keyword.value, page: page.value, size: size.value })
    rows.value = res.list || []
    total.value = res.total || 0
  } catch {
    try {
      const all = await listUsers()
      total.value = Array.isArray(all) ? all.length : 0
      const start = (page.value - 1) * size.value
      rows.value = (Array.isArray(all) ? all : []).filter(u => !keyword.value || (u.username||'').includes(keyword.value)).slice(start, start + size.value)
    } catch {
      rows.value = []
      total.value = 0
    }
  }
}
function onSize(val) { size.value = val; page.value = 1; load() }
function onPage(val) { page.value = val; load() }
async function updateStatus(id, status) { await updateUserStatus(id, status) }
onMounted(load)
</script>

<style scoped>
.table-actions { display: flex; gap: 8px }
</style>
