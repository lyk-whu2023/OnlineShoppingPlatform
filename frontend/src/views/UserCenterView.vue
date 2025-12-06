<template>
  <div class="container">
    <el-card>
      <div class="title">基本信息</div>
      <el-descriptions :column="2">
        <el-descriptions-item label="姓名">未登录</el-descriptions-item>
        <el-descriptions-item label="手机号">-</el-descriptions-item>
        <el-descriptions-item label="注册时间">-</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script setup>
</script>

<style scoped>
.container { max-width: 1080px; margin: 0 auto }
.title { margin-bottom: 12px; font-weight: 600 }
</style>
