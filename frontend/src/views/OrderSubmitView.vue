<template>
  <div class="container">
    <el-card>
      <div style="margin-bottom:12px">选择一个地址后提交订单</div>
      <el-select v-model="addressId" placeholder="请选择地址" style="width:100%">
        <el-option v-for="a in addresses" :key="a.id" :label="a.detail" :value="a.id" />
      </el-select>
      <div style="margin-top:12px">
        <el-button type="primary" @click="submit" :disabled="!addressId">提交订单</el-button>
      </div>
      <div v-if="order" style="margin-top:12px">订单提交成功，订单号：{{ order.id }}，金额：{{ order.amount }}</div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAddresses } from '../api/addresses'
import { createOrder, createOrderFromItems } from '../api/orders'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

const addresses = ref([])
const addressId = ref(null)
const order = ref(null)
const router = useRouter()

async function load() { 
  addresses.value = await getAddresses()
  const d = addresses.value.find(a => a.isDefault)
  if (d) addressId.value = d.id
}
onMounted(load)

async function submit() { 
  try {
    const raw = localStorage.getItem('pendingPurchase')
    if (raw) {
      const payload = JSON.parse(raw)
      order.value = await createOrderFromItems(payload.items || [], addressId.value)
      localStorage.removeItem('pendingPurchase')
    } else {
      order.value = await createOrder(addressId.value) 
    }
    ElMessage.success('订单提交成功')
    if (order.value && order.value.id) router.push('/order/' + order.value.id)
  } catch (e) {
    ElMessage.error(String(e.message || '提交失败'))
  }
}
</script>

<style scoped>
.container { max-width: 1080px; margin: 0 auto }
</style>
