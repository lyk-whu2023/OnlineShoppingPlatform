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
    <el-dialog title="编辑信息" v-model="visible">
      <el-form label-width="100px">
        <el-form-item label="手机号">
          <el-input v-model="form.phone" />
        </el-form-item>
        <template #footer>
          <el-button @click="visible=false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </template>
      </el-form>
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
        <template #footer>
          <el-button @click="pwdVisible=false">取消</el-button>
          <el-button type="primary" @click="savePwd">重置密码</el-button>
        </template>
      </el-form>
    </el-dialog>
  </div>
  </template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getMe, updateMe } from '../api/users'
import { logout } from '../api/auth'
import { changePassword } from '../api/users'

const u = ref(null)
onMounted(async () => {
  try { u.value = await getMe() } catch (e) { u.value = null }
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
    const updated = await updateMe({ phone: form.value.phone })
    u.value = updated
    visible.value = false
    ElMessage.success('已保存')
  } catch (e) {
    ElMessage.error(String(e.message || '保存失败'))
  }
}
async function savePwd() {
  try {
    if (!pwdForm.value.oldPassword || !pwdForm.value.newPassword || !pwdForm.value.newPassword2) throw new Error('请完整填写密码信息')
    if (pwdForm.value.newPassword !== pwdForm.value.newPassword2) throw new Error('两次输入的新密码不一致')
    await changePassword({ oldPassword: pwdForm.value.oldPassword, newPassword: pwdForm.value.newPassword })
    pwdVisible.value = false
    ElMessage.success('已重置密码')
  } catch (e) {
    ElMessage.error(String(e.message || '重置失败'))
  }
}
async function doLogout() {
  try { await logout() } catch {}
  localStorage.removeItem('token')
  localStorage.removeItem('userId')
  localStorage.removeItem('user')
  ElMessage.success('已退出登录')
}
</script>

<style scoped>
.container { max-width: 1080px; margin: 0 auto }
.title { margin-bottom: 12px; font-weight: 600 }
.actions { margin-top: 12px; display: flex; gap: 8px }
</style>
