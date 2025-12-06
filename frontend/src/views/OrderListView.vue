<template>
  <div class="container">
    <el-tabs v-model="tab">
      <el-tab-pane label="全部" name="all" />
      <el-tab-pane label="待支付" name="pending" />
      <el-tab-pane label="已支付" name="paid" />
      <el-tab-pane label="已发货" name="已发货" />
      <el-tab-pane label="已完成" name="已完成" />
    </el-tabs>
    <el-empty description="订单列表暂未接入后端" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
const tab = ref('all')
</script>

<style scoped>
.container { max-width: 1080px; margin: 0 auto }
</style>
