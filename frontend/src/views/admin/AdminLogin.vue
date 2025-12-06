<template>
  <div class="container small">
    <el-card>
      <div class="title">管理后台登录</div>
      <el-form label-width="100px">
        <el-form-item label="用户名">
          <el-input v-model="user" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="pwd" type="password" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="login" :loading="loading" :disabled="loading">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login as loginApi } from '../../api/auth'

const router = useRouter()
const user = ref('admin')
const pwd = ref('')
const loading = ref(false)

async function login() {
  loading.value = true
  try {
    if (!user.value || !pwd.value) throw new Error('请输入用户名和密码')
    const res = await loginApi({ username: user.value, password: pwd.value })
    if (res && res.user) {
      const role = res.user.role || ''
      if (role === 'admin') router.push('/admin')
      else {
        ElMessage.error('非管理员账户，无法进入管理后台')
        router.push('/')
      }
    } else {
      ElMessage.error('登录失败：账号或密码错误')
    }
  } catch (e) {
    ElMessage.error(String(e.message || '请求失败'))
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.container { max-width: 1080px; margin: 0 auto }
.small { max-width: 520px }
.title { text-align: center; margin-bottom: 16px; font-size: 18px; color: #409EFF }
</style>
