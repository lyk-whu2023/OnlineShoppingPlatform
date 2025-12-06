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
import { createOrder } from '../api/orders'

const addresses = ref([])
const addressId = ref(null)
const order = ref(null)

async function load() { addresses.value = await getAddresses() }
onMounted(load)

async function submit() { order.value = await createOrder(addressId.value) }
</script>

<style scoped>
.container { max-width: 1080px; margin: 0 auto }
</style>
