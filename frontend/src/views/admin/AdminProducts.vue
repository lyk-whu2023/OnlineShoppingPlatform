<template>
  <div>
    <div style="margin-bottom:12px; display:flex; gap:12px; align-items:center">
      <el-input v-model="keyword" placeholder="按商品名称搜索" style="width:260px" />
      <el-button type="primary" @click="onSearch">搜索</el-button>
      <el-button @click="reset">重置</el-button>
      <el-button type="primary" @click="openAdd">新增商品</el-button>
    </div>
    <el-table :data="rows" style="width:100%">
      <el-table-column label="商品图" width="120">
        <template #default="scope">
          <img :src="img(scope.row)" style="width:120px;height:80px;object-fit:cover" loading="lazy" decoding="async" />
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
    <div style="display:flex; justify-content:flex-end; margin-top:12px">
      <el-pagination
        background
        layout="prev, pager, next, sizes, total"
        :total="total"
        :page-size="size"
        :current-page="page"
        @size-change="onSize"
        @current-change="onPage"
      />
    </div>
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
      </el-form>
      <template #footer>
        <el-button @click="visible=false">取消</el-button>
        <el-button type="primary" @click="save">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import { getProducts, createProduct, updateProduct, deleteProduct } from '../../api/products'
import { getCategories } from '../../api/categories'

const rows = ref([])
const visible = ref(false)
const form = ref({ id: null, name: '', categoryId: null, price: 0, stock: 0, description: '' })
const cats = ref([])
const keyword = ref('')
const page = ref(1)
const size = ref(20)
const total = ref(0)

onMounted(async () => { await load(); try { cats.value = await getCategories() } catch (e) { cats.value = [] } })

async function load() {
  try {
    const res = await getProducts({ page: page.value, size: size.value, keyword: keyword.value })
    rows.value = res.list || []
    total.value = res.total || rows.value.length
  } catch (e) {
    rows.value = []
    total.value = 0
  }
}
function onSearch() { page.value = 1; load() }
function reset() { keyword.value = ''; page.value = 1; load() }
function onSize(val) { size.value = val; page.value = 1; load() }
function onPage(val) { page.value = val; load() }
function img(p) { const seed = (p.images && p.images[0]) || ('p'+p.id); return 'https://picsum.photos/seed/' + seed + '/120/80' }
function catName(cid) { const c = cats.value.find(x => x.id == cid); return c ? c.name : '' }
function openAdd() { form.value = { id: null, name: '', categoryId: null, price: 0, stock: 0, description: '' }; visible.value = true }
function edit(row) { form.value = { ...row }; visible.value = true }
async function save() {
  try {
    if (form.value.id) {
      await updateProduct(form.value.id, form.value)
      ElMessage.success('更新成功')
    } else {
      await createProduct(form.value)
      ElMessage.success('创建成功')
    }
  } finally {
    visible.value = false
    await load()
  }
}
async function del(id) {
  try {
    await ElMessageBox.confirm('确认删除该商品？', '提示', { type: 'warning' })
    await deleteProduct(id)
    ElMessage.success('删除成功')
  } catch (e) {
  } finally {
    await load()
  }
}
</script>

<style scoped>
.table-actions { display: flex; gap: 8px }
</style>
