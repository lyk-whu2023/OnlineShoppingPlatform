<template>
  <div>
    <div style="margin-bottom:12px; display:flex; gap:12px; align-items:center">
      <el-input v-model="keyword" placeholder="按分类名搜索" style="width:260px" />
      <el-button type="primary" @click="onSearch">搜索</el-button>
      <el-button @click="reset">重置</el-button>
      <el-button type="primary" @click="openAdd">新增分类</el-button>
    </div>
    <el-table :data="rows" style="width:100%">
      <el-table-column label="编号" width="120" prop="id" />
      <el-table-column label="分类名" prop="name" />
      <el-table-column label="操作" width="220">
        <template #default="scope">
          <div class="table-actions">
            <el-button type="primary" size="small" @click="edit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="del(scope.row.id)">删除</el-button>
          </div>
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
    <el-dialog title="新增/编辑分类" v-model="visible" width="420px">
      <el-form label-width="100px">
        <el-form-item label="分类名">
          <el-input v-model="form.name" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="visible=false">取消</el-button>
        <el-button type="primary" @click="save">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import { searchCategories, getCategories, createCategory, updateCategory, deleteCategory } from '../../api/categories'

const rows = ref([])
const visible = ref(false)
const form = ref({ id: null, name: '' })
const keyword = ref('')
const page = ref(1)
const size = ref(20)
const total = ref(0)

onMounted(load)

async function load() {
  try {
    const res = await searchCategories({ keyword: keyword.value, page: page.value, size: size.value })
    rows.value = res.list || []
    total.value = res.total || 0
  } catch (e) {
    try {
      const all = await getCategories()
      total.value = Array.isArray(all) ? all.length : 0
      const start = (page.value - 1) * size.value
      rows.value = (Array.isArray(all) ? all : []).filter(x => !keyword.value || (x.name||'').includes(keyword.value)).slice(start, start + size.value)
    } catch {
      rows.value = []
      total.value = 0
    }
  }
}
function onSearch() { page.value = 1; load() }
function reset() { keyword.value = ''; page.value = 1; load() }
function onSize(val) { size.value = val; page.value = 1; load() }
function onPage(val) { page.value = val; load() }
function openAdd() { form.value = { id: null, name: '' }; visible.value = true }
function edit(row) { form.value = { ...row }; visible.value = true }
async function save() {
  try {
    if (form.value.id) {
      await updateCategory(form.value.id, form.value)
      ElMessage.success('更新成功')
    } else {
      await createCategory(form.value)
      ElMessage.success('创建成功')
    }
  } finally {
    visible.value = false
    await load()
  }
}
async function del(id) {
  try {
    await ElMessageBox.confirm('确认删除该分类？', '提示', { type: 'warning' })
    await deleteCategory(id)
    ElMessage.success('删除成功')
  } catch (e) {
  } finally {
    await load()
  }
}
</script>

<style scoped>
.table-actions { display: flex; gap: 8px }
</style>
