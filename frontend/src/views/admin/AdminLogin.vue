<template>
  <div class="container small">
    <el-card>
      <div class="title">Admin</div>
      <el-form label-width="100px">
        <el-form-item label="用户名">
          <el-input v-model="user" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="pwd" type="password" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
const user = ref('admin')
const pwd = ref('')
</script>

<style scoped>
.container { max-width: 1080px; margin: 0 auto }
.small { max-width: 520px }
.title { text-align: center; margin-bottom: 16px; font-size: 18px; color: #409EFF }
</style>
