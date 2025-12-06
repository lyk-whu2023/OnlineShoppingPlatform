<template>
  <div>
    <div style="margin-bottom:12px">
      <el-button type="primary" @click="openAdd">新增商品</el-button>
    </div>
    <el-table :data="rows" style="width:100%">
      <el-table-column label="商品图" width="120">
        <template #default="scope">
          <img :src="img(scope.row)" style="width:120px;height:80px;object-fit:cover" />
        </template>
      </el-table-column>
      <el-table-column label="名称" prop="name" />
      <el-table-column label="分类" width="160">
        <template #default="scope">{{ catName(scope.row.categoryId) }}</template>
      </el-table-column>
      <el-table-column label="单价" width="120" prop="price" />
      <el-table-column label="库存" width="120" prop="stock" />
      <el-table-column label="操作" width="220">
        <template #default="scope">
          <div class="table-actions">
            <el-button type="primary" size="small" @click="edit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="del(scope.row.id)">删除</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog title="新增/编辑商品" v-model="visible" width="600px">
      <el-form label-width="100px">
        <el-form-item label="商品名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.categoryId">
            <el-option v-for="c in cats" :label="c.name" :value="c.id" :key="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格">
          <el-input v-model.number="form.price" />
        </el-form-item>
        <el-form-item label="库存">
          <el-input v-model.number="form.stock" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input type="textarea" v-model="form.description" />
        </el-form-item>
        <template #footer>
          <el-button @click="visible=false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </template>
      </el-form>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getProducts, createProduct, updateProduct, deleteProduct } from '../../api/products'
import { getCategories } from '../../api/categories'

const rows = ref([])
const visible = ref(false)
const form = ref({ id: null, name: '', categoryId: null, price: 0, stock: 0, description: '' })
const cats = ref([])

onMounted(async () => { await load(); try { cats.value = await getCategories() } catch (e) { cats.value = [] } })

async function load() { try { const res = await getProducts({ page: 1, size: 100 }); rows.value = res.list || [] } catch (e) { rows.value = [] } }
function img(p) { const seed = (p.images && p.images[0]) || ('p'+p.id); return 'https://picsum.photos/seed/' + seed + '/120/80' }
function catName(cid) { const c = cats.value.find(x => x.id == cid); return c ? c.name : '' }
function openAdd() { form.value = { id: null, name: '', categoryId: null, price: 0, stock: 0, description: '' }; visible.value = true }
function edit(row) { form.value = { ...row }; visible.value = true }
async function save() { try { if (form.value.id) { await updateProduct(form.value.id, form.value) } else { await createProduct(form.value) } } finally { visible.value = false; await load() } }
async function del(id) { try { await deleteProduct(id) } finally { await load() } }
</script>

<style scoped>
.table-actions { display: flex; gap: 8px }
</style>
