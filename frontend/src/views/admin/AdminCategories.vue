<template>
  <div>
    <div style="margin-bottom:12px">
      <el-button type="primary" @click="openAdd">新增分类</el-button>
    </div>
    <el-table :data="rows" style="width:100%">
      <el-table-column label="ID" width="120" prop="id" />
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
    <el-dialog title="新增/编辑分类" v-model="visible" width="420px">
      <el-form label-width="100px">
        <el-form-item label="分类名">
          <el-input v-model="form.name" />
        </el-form-item>
        <template #footer>
          <el-button @click="visible=false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </template>
      </el-form>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCategories, createCategory, updateCategory, deleteCategory } from '../../api/categories'

const rows = ref([])
const visible = ref(false)
const form = ref({ id: null, name: '' })

onMounted(load)

async function load() { try { rows.value = await getCategories() } catch (e) { rows.value = [] } }
function openAdd() { form.value = { id: null, name: '' }; visible.value = true }
function edit(row) { form.value = { ...row }; visible.value = true }
async function save() { try { if (form.value.id) { await updateCategory(form.value.id, form.value) } else { await createCategory(form.value) } } finally { visible.value = false; await load() } }
async function del(id) { try { await deleteCategory(id) } finally { await load() } }
</script>

<style scoped>
.table-actions { display: flex; gap: 8px }
</style>
