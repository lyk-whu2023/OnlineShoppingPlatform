<template>
  <div>
    <div class="section-title">评论管理</div>
    <el-card>
      <div style="margin-bottom:12px; display:flex; gap:12px; align-items:center">
        <el-input v-model="keyword" placeholder="按内容搜索" style="width:260px" />
        <el-button type="primary" @click="onSearch">搜索</el-button>
        <el-button @click="reset">重置</el-button>
      </div>
      <el-table :data="displayRows" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="productId" label="商品ID" width="100" />
        <el-table-column prop="userId" label="用户ID" width="100" />
        <el-table-column prop="content" label="内容" />
        <el-table-column prop="status" label="状态" width="120" />
        <el-table-column label="操作" width="220">
          <template #default="{ row }">
            <el-select v-model="row.status" placeholder="状态" style="width:120px" @change="changeStatus(row)">
              <el-option label="可见" value="visible" />
              <el-option label="隐藏" value="hidden" />
            </el-select>
            <el-button type="danger" size="small" @click="remove(row.id)" style="margin-left:8px">删除</el-button>
          </template>
        </el-table-column>
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
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { adminListComments, deleteComment, updateCommentStatus } from '../../api/comments'
import { ElMessage } from 'element-plus'

const list = ref([])
const keyword = ref('')
const page = ref(1)
const size = ref(10)
const total = ref(0)
const filtered = computed(() => list.value.filter(x => !keyword.value || (x.content||'').includes(keyword.value)))
const displayRows = computed(() => {
  const start = (page.value - 1) * size.value
  const end = start + size.value
  return filtered.value.slice(start, end)
})

onMounted(async () => { await load() })

async function load() {
  try { list.value = await adminListComments() } catch { list.value = [] } finally { total.value = filtered.value.length }
}

async function remove(id) {
  try { await deleteComment(id); ElMessage.success('已删除'); await load() } catch (e) { ElMessage.error(String(e.message || '删除失败')) }
}

async function changeStatus(row) {
  try { await updateCommentStatus(row.id, row.status); ElMessage.success('状态已更新'); await load() } catch (e) { ElMessage.error(String(e.message || '更新失败')) }
}
function onSearch() { page.value = 1; total.value = filtered.value.length }
function reset() { keyword.value = ''; page.value = 1; total.value = filtered.value.length }
function onSize(val) { size.value = val; page.value = 1 }
function onPage(val) { page.value = val }
</script>

<style scoped>
.section-title { margin: 8px 0 12px; font-weight: 600 }
</style>
