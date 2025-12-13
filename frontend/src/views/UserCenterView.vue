<template>
  <div class="container">
    <el-card>
      <div class="title">基本信息</div>
      <el-descriptions :column="2">
        <el-descriptions-item label="姓名">{{ u?.username || '未登录' }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ u?.phone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="注册时间">{{ u?.createdAt || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div class="actions">
        <el-button type="primary" size="small" @click="openEdit">编辑信息</el-button>
        <el-button type="primary" size="small" @click="openPwd">修改密码</el-button>
        <el-button size="small" @click="doLogout">退出登录</el-button>
      </div>
    </el-card>
    <el-card style="margin-top:12px">
      <div class="title">地址管理</div>
      <div style="margin-bottom:12px">
        <el-button type="primary" size="small" @click="openAddrAdd">新增地址</el-button>
      </div>
      <el-table :data="addrRows" style="width:100%">
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
              <el-button type="primary" size="small" @click="openAddrEdit(scope.row)">编辑</el-button>
              <el-button type="danger" size="small" @click="delAddr(scope.row.id)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog title="编辑信息" v-model="visible">
      <el-form label-width="100px">
        <el-form-item label="手机号">
          <el-input v-model="form.phone" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="visible=false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
    <el-dialog title="修改密码" v-model="pwdVisible">
      <el-form label-width="100px">
        <el-form-item label="原密码">
          <el-input v-model="pwdForm.oldPassword" type="password" />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="pwdForm.newPassword" type="password" />
        </el-form-item>
        <el-form-item label="确认新密码">
          <el-input v-model="pwdForm.newPassword2" type="password" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="pwdVisible=false">取消</el-button>
        <el-button type="primary" @click="savePwd">重置密码</el-button>
      </template>
    </el-dialog>
    <el-dialog title="编辑地址" v-model="addrVisible">
      <el-form label-width="100px">
        <el-form-item label="收件人">
          <el-input v-model="addrForm.name" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="addrForm.phone" />
        </el-form-item>
        <el-form-item label="地址详情">
          <el-input v-model="addrForm.detail" />
        </el-form-item>
        <el-form-item label="设为默认">
          <el-switch v-model="addrForm.isDefault" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addrVisible=false">取消</el-button>
        <el-button type="primary" @click="saveAddr">保存</el-button>
      </template>
    </el-dialog>
  </div>
  </template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMe, updateMe } from '../api/users'
import { logout } from '../api/auth'
import { changePassword } from '../api/users'
import { getAddresses, createAddress, updateAddress, deleteAddress } from '../api/addresses'

const u = ref(null)
onMounted(async () => {
  try { u.value = await getMe() } catch (e) { u.value = null }
  await loadAddrs()
})

const visible = ref(false)
const form = ref({ phone: '' })
const pwdVisible = ref(false)
const pwdForm = ref({ oldPassword: '', newPassword: '', newPassword2: '' })
function openEdit() {
  form.value = { phone: u.value?.phone || '' }
  visible.value = true
}
function openPwd() { pwdForm.value = { oldPassword: '', newPassword: '', newPassword2: '' }; pwdVisible.value = true }
async function save() {
  try {
    await ElMessageBox.confirm('确认保存个人信息变更？', '确认操作', { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' })
    const updated = await updateMe({ phone: form.value.phone })
    u.value = updated
    visible.value = false
    ElMessage.success('已保存')
  } catch (e) {
    if (e && e !== 'cancel') ElMessage.error(String(e.message || '保存失败'))
  }
}
async function savePwd() {
  try {
    await ElMessageBox.confirm('确认修改密码？', '确认操作', { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' })
    if (!pwdForm.value.oldPassword || !pwdForm.value.newPassword || !pwdForm.value.newPassword2) throw new Error('请完整填写密码信息')
    if (pwdForm.value.newPassword !== pwdForm.value.newPassword2) throw new Error('两次输入的新密码不一致')
    await changePassword({ oldPassword: pwdForm.value.oldPassword, newPassword: pwdForm.value.newPassword })
    pwdVisible.value = false
    ElMessage.success('已重置密码')
  } catch (e) {
    if (e && e !== 'cancel') ElMessage.error(String(e.message || '重置失败'))
  }
}
async function doLogout() {
  try { 
    await ElMessageBox.confirm('确认退出登录？', '确认操作', { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' })
    try { await logout() } catch {}
    localStorage.removeItem('token')
    localStorage.removeItem('userId')
    localStorage.removeItem('user')
    ElMessage.success('已退出登录')
  } catch (e) {}
}

const addrRows = ref([])
const addrVisible = ref(false)
const addrForm = ref({ id: null, name: '', phone: '', detail: '', isDefault: false })
async function loadAddrs() { try { addrRows.value = await getAddresses() } catch (e) { addrRows.value = [] } }
function openAddrAdd() { addrForm.value = { id: null, name: '', phone: '', detail: '', isDefault: false }; addrVisible.value = true }
function openAddrEdit(row) { addrForm.value = { ...row }; addrVisible.value = true }
async function saveAddr() {
  try {
    await ElMessageBox.confirm('确认保存地址信息？', '确认操作', { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' })
    const data = { name: addrForm.value.name, phone: addrForm.value.phone, detail: addrForm.value.detail, isDefault: !!addrForm.value.isDefault }
    if (!addrForm.value.id) await createAddress(data)
    else await updateAddress(addrForm.value.id, data)
    ElMessage.success('地址已保存')
  } catch (e) {
    if (e && e !== 'cancel') ElMessage.error(String(e.message || '保存失败'))
  } finally {
    addrVisible.value = false
    await loadAddrs()
  }
}
async function delAddr(id) { 
  try { 
    await ElMessageBox.confirm('确认删除该地址？', '确认操作', { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' })
    await deleteAddress(id); 
    ElMessage.success('已删除') 
  } catch (e) {} 
  finally { await loadAddrs() } 
}
</script>

<style scoped>
.container { max-width: 1080px; margin: 0 auto }
.title { margin-bottom: 12px; font-weight: 600 }
.actions { margin-top: 12px; display: flex; gap: 8px }
</style>
