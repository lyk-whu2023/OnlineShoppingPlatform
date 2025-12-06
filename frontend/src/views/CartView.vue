<template>
  <div class="container">
    <div style="margin-bottom:12px">
      <el-button @click="reload" type="primary">刷新</el-button>
      <el-button @click="clearAll" type="danger">清空购物车</el-button>
    </div>
    <el-table :data="items" style="width:100%">
      <el-table-column label="商品ID" prop="productId" width="160" />
      <el-table-column label="数量" width="160">
        <template #default="scope">
          <el-input-number v-model="scope.row.qty" @change="q=>update(scope.row.productId,q)" :min="1" />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button type="danger" size="small" @click="remove(scope.row.productId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
  </template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCart, updateCartItem, removeCartItem, clearCart } from '../api/cart'

const items = ref([])
async function reload() { items.value = await getCart() }
onMounted(reload)
async function update(productId, qty) { items.value = await updateCartItem(productId, qty) }
async function remove(productId) { items.value = await removeCartItem(productId) }
async function clearAll() { await clearCart(); await reload() }
</script>

<style scoped>
.container { max-width: 1080px; margin: 0 auto }
</style>
