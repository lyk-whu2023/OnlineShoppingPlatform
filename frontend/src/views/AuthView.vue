<template>
  <div class="container small">
    <el-card>
      <div class="title">登录 / 注册</div>
      <el-tabs v-model="tab">
        <el-tab-pane label="登录" name="login">
          <el-form label-width="100px">
            <el-form-item label="登录方式">
              <el-radio-group v-model="loginMode">
                <el-radio label="account">账号登录</el-radio>
                <el-radio label="phone">手机号登录</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item v-if="loginMode==='account'" label="用户名">
              <el-input v-model="user" placeholder="请输入用户名" />
            </el-form-item>
            <el-form-item v-else label="手机号">
              <el-input v-model="phoneLogin" placeholder="请输入手机号" />
            </el-form-item>
            <el-form-item label="密码">
              <el-input v-model="pwd" type="password" placeholder="请输入密码" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="login" :loading="loading" :disabled="loading">登录</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="注册" name="register">
          <el-form label-width="100px">
            <el-form-item label="注册用户名">
              <el-input v-model="regUser" placeholder="请输入用户名" />
            </el-form-item>
            <el-form-item label="注册手机号">
              <el-input v-model="regPhone" placeholder="请输入手机号" />
            </el-form-item>
            <el-form-item label="设置密码">
              <el-input v-model="regPwd" type="password" placeholder="请设置密码" />
            </el-form-item>
            <el-form-item label="确认密码">
              <el-input v-model="regPwd2" type="password" placeholder="请再次输入密码" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="register" :loading="loading" :disabled="loading">注册</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter, useRoute } from 'vue-router'
import { login as loginApi, register as registerApi } from '../api/auth'

const tab = ref('login')
const loginMode = ref('account')
const user = ref('')
const phoneLogin = ref('')
const pwd = ref('')
const regUser = ref('')
const regPhone = ref('')
const regPwd = ref('')
const regPwd2 = ref('')
const loading = ref(false)
const router = useRouter()
const route = useRoute()

async function login() {
  loading.value = true
  try {
    let res
    if (loginMode.value === 'account') {
      if (!user.value || !pwd.value) throw new Error('请输入用户名和密码')
      res = await loginApi({ username: user.value, password: pwd.value })
    } else {
      if (!phoneLogin.value || !pwd.value) throw new Error('请输入手机号和密码')
      res = await loginApi({ username: phoneLogin.value, password: pwd.value })
    }
    if (res && res.token && res.user) {
      const role = res.user.role || ''
      if (role === 'admin') router.push('/admin')
      else {
        const redirect = route.query.redirect || '/'
        router.push(String(redirect))
      }
    } else {
      ElMessage.error('登录失败：账号/手机号或密码错误')
    }
  } catch (e) {
    ElMessage.error(String(e.message || '请求失败'))
  } finally {
    loading.value = false
  }
}
async function register() {
  loading.value = true
  try {
    if (!regUser.value || !regPhone.value || !regPwd.value || !regPwd2.value) throw new Error('请完整填写注册信息')
    if (regPwd.value !== regPwd2.value) throw new Error('两次输入的密码不一致')
    const res = await registerApi({ username: regUser.value, password: regPwd.value, phone: regPhone.value })
    if (res && res.token && res.user) {
      const redirect = route.query.redirect || '/'
      router.push(String(redirect))
    } else {
      ElMessage.error('注册失败或用户已存在')
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
.title { text-align: center; margin-bottom: 16px; font-size: 18px }
</style>
