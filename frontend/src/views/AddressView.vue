<template>
  <div class="container">
    <div style="margin-bottom:12px">
      <el-button type="primary" @click="openAdd">新增地址</el-button>
    </div>
    <el-table :data="rows" style="width:100%">
      <el-table-column label="收件人" width="160" prop="name" />
      <el-table-column label="手机号" width="160" prop="phone" />
      <el-table-column label="地址详情" prop="detail" />
      <el-table-column label="默认" width="120">
        <template #default="scope">
          <el-tag type="success" v-if="scope.row.isDefault">默认</el-tag>
        </template>
      </el-table-column>
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
        <el-form-item label="设为默认">
          <el-switch v-model="form.isDefault" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="visible=false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAddresses, createAddress, updateAddress, deleteAddress } from '../api/addresses'
import { ElMessage, ElMessageBox } from 'element-plus'

const rows = ref([])
const visible = ref(false)
const form = ref({ id: null, name: '', phone: '', detail: '', isDefault: false })

async function load() { rows.value = await getAddresses() }
onMounted(load)

function openAdd() { form.value = { id: null, name: '', phone: '', detail: '', isDefault: false }; visible.value = true }
function edit(row) { form.value = { ...row }; visible.value = true }
async function save() {
  try {
    await ElMessageBox.confirm('确认保存地址信息？', '确认操作', { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' })
    const data = { name: form.value.name, phone: form.value.phone, detail: form.value.detail, isDefault: !!form.value.isDefault }
    if (!form.value.id) await createAddress(data)
    else await updateAddress(form.value.id, data)
    ElMessage.success('地址已保存')
  } catch (e) {
  } finally {
    visible.value = false
    await load()
  }
}
async function del(id) { 
  try {
    await ElMessageBox.confirm('确认删除该地址？', '确认操作', { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' })
    await deleteAddress(id); 
    ElMessage.success('已删除')
  } catch (e) {
  } finally {
    await load()
  }
}
</script>

<style scoped>
.container { max-width: 1080px; margin: 0 auto }
.table-actions { display: flex; gap: 8px }
</style>
