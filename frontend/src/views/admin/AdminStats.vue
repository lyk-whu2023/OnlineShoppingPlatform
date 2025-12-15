<template>
  <div>
    <div class="section-title">数据统计</div>
    <el-card>
      <div class="stats">
        <div class="stat"><div class="num">{{ s.productCount }}</div><div class="label">商品数</div></div>
        <div class="stat"><div class="num">{{ s.userCount }}</div><div class="label">用户数</div></div>
        <div class="stat"><div class="num">{{ s.orderCount }}</div><div class="label">订单数</div></div>
        <div class="stat"><div class="num">¥{{ s.totalSales }}</div><div class="label">总销售额</div></div>
      </div>
    </el-card>
    <el-card style="margin-top:12px">
      <div class="section-title">热销商品TOP5</div>
      <div id="chart" style="height:360px"></div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { getStats } from '../../api/stats'

const s = ref({ productCount: 0, userCount: 0, orderCount: 0, totalSales: 0, topProducts: [] })

onMounted(async () => {
  try {
    s.value = await getStats()
    renderChart()
  } catch (e) {
    s.value = { productCount: 0, userCount: 0, orderCount: 0, totalSales: 0, topProducts: [] }
  }
})

function renderChart() {
  const el = document.getElementById('chart')
  if (!el) return
  const chart = echarts.init(el)
  const names = (s.value.topProducts || []).map(i => i.name)
  const sales = (s.value.topProducts || []).map(i => i.sales)
  chart.setOption({
    tooltip: {},
    xAxis: { type: 'category', data: names },
    yAxis: { type: 'value' },
    series: [{ type: 'bar', data: sales }]
  })
}
</script>

<style scoped>
.section-title { margin: 8px 0 12px; font-weight: 600 }
.stats { display: grid; grid-template-columns: repeat(4,1fr); gap: 12px }
.stat { background: #f5f7fa; padding: 12px; border-radius: 8px; text-align: center }
.num { font-weight: 700; font-size: 20px }
.label { color: #909399; margin-top: 6px }
</style>
