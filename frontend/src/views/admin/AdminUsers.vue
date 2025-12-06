<template>
  <div>
    <div style="margin-bottom:12px; display:flex; gap:12px">
      <el-input v-model="keyword" placeholder="按用户名搜索" style="width:240px" />
      <el-button type="primary" @click="load">搜索</el-button>
    </div>
    <el-table :data="filtered" style="width:100%">
      <el-table-column label="ID" width="120" prop="id" />
      <el-table-column label="用户名" prop="username" />
      <el-table-column label="手机号" width="160" prop="phone" />
      <el-table-column label="角色" width="140" prop="role" />
      <el-table-column label="状态" width="220">
        <template #default="scope">
          <el-select v-model="scope.row.status" @change="s=>updateStatus(scope.row.id,s)" style="width:160px">
            <el-option label="ACTIVE" value="ACTIVE" />
            <el-option label="DISABLED" value="DISABLED" />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createdAt" />
    </el-table>
  </div>
  </template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { listUsers, updateUserStatus } from '../../api/users'

const rows = ref([])
const keyword = ref('')
const filtered = computed(() => rows.value.filter(u => !keyword.value || (u.username||'').includes(keyword.value)))

async function load() { try { rows.value = await listUsers() } catch { rows.value = [] } }
async function updateStatus(id, status) { await updateUserStatus(id, status) }
onMounted(load)
</script>

<style scoped>
.table-actions { display: flex; gap: 8px }
</style>
