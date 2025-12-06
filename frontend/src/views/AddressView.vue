<template>
  <div class="container">
    <div style="margin-bottom:12px">
      <el-button type="primary" @click="openAdd">新增地址</el-button>
    </div>
    <el-table :data="rows" style="width:100%">
      <el-table-column label="收件人" width="160" prop="name" />
      <el-table-column label="手机号" width="160" prop="phone" />
      <el-table-column label="地址详情" prop="detail" />
      <el-table-column label="操作" width="220">
        <template #default="scope">
          <div class="table-actions">
            <el-button type="primary" size="small" @click="edit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="del(scope.row.id)">删除</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog title="编辑地址" v-model="visible">
      <el-form label-width="100px">
        <el-form-item label="收件人">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="地址详情">
          <el-input v-model="form.detail" />
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
import { ref } from 'vue'

const rows = ref([])
const visible = ref(false)
const form = ref({ id: null, name: '', phone: '', detail: '' })

function openAdd() { form.value = { id: null, name: '', phone: '', detail: '' }; visible.value = true }
function edit(row) { form.value = { ...row }; visible.value = true }
function save() { visible.value = false }
function del(id) {}
</script>

<style scoped>
.container { max-width: 1080px; margin: 0 auto }
.table-actions { display: flex; gap: 8px }
</style>
