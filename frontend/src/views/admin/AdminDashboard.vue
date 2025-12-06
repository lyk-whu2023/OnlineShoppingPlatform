<template>
  <div>
    <el-row :gutter="24">
      <el-col :span="6"><el-card><div>订单数量 {{ orders }}</div></el-card></el-col>
      <el-col :span="6"><el-card><div>用户数量 {{ users }}</div></el-card></el-col>
      <el-col :span="6"><el-card><div>待发货 {{ pending }}</div></el-card></el-col>
      <el-col :span="6"><el-card><div>今日销售 ¥{{ today }}</div></el-card></el-col>
    </el-row>
    <el-row :gutter="24" style="margin-top:24px">
      <el-col :span="12"><el-card><div id="chart1" class="chart"></div></el-card></el-col>
      <el-col :span="12"><el-card><div id="chart2" class="chart"></div></el-card></el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'

const orders = ref(0)
const users = ref(0)
const pending = ref(0)
const today = ref(0)

onMounted(() => {
  const c1 = echarts.init(document.getElementById('chart1'))
  c1.setOption({ xAxis: { type: 'category', data: ['Mon','Tue','Wed','Thu','Fri','Sat','Sun'] }, yAxis: { type: 'value' }, series: [{ type: 'line', data: [120,200,150,80,70,110,130] }] })
  const c2 = echarts.init(document.getElementById('chart2'))
  c2.setOption({ xAxis: { type: 'category', data: ['A','B','C','D'] }, yAxis: { type: 'value' }, series: [{ type: 'bar', data: [5,20,36,10] }] })
})
</script>

<style scoped>
.chart { width: 100%; height: 300px }
</style>
