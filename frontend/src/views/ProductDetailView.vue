<template>
  <div class="container">
    <div class="detail">
      <div class="left">
        <el-carousel height="360px">
          <el-carousel-item v-for="seed in seeds" :key="seed">
            <img :src="url(seed)" class="bigpic" loading="lazy" decoding="async" />
          </el-carousel-item>
        </el-carousel>
      </div>
      <div class="right">
        <div class="title">{{ p && p.name }}</div>
        <div class="price">¥{{ p && p.price }}</div>
        <div class="stock">库存 {{ p && p.stock }}</div>
        <div class="desc">{{ p && p.description }}</div>
        <el-input-number v-model="num" :min="1" :max="5" />
        <div class="actions">
          <el-button type="primary" @click="add">加入购物车</el-button>
          <el-button type="success" @click="buyNow">立即购买</el-button>
          <el-button :type="fav ? 'danger' : 'warning'" @click="toggleFav">{{ fav ? '取消收藏' : '收藏' }}</el-button>
        </div>
      </div>
    </div>
    <el-card class="comments">
      <div class="section-title">评论</div>
      <div class="comment-input">
        <el-input v-model="newComment" type="textarea" :rows="3" placeholder="输入评论内容..." />
        <el-button type="primary" style="margin-top:8px" @click="postComment">提交评论</el-button>
      </div>
      <div class="comment-list">
        <div v-for="c in comments" :key="c.id" class="comment-item">
          <div class="comment-content">{{ c.content }}</div>
          <div class="comment-meta">用户 {{ c.userId }} · {{ formatTime(c.createdAt) }}</div>
          <div class="comment-actions">
            <el-button text type="primary" @click="openReply(c)">回复</el-button>
          </div>
          <div v-if="replyTo === c.id" class="reply-input">
            <el-input v-model="replyText" type="textarea" :rows="2" placeholder="输入回复..." />
            <el-button type="primary" style="margin-top:6px" @click="postReply(c)">提交回复</el-button>
            <el-button style="margin-top:6px" @click="cancelReply">取消</el-button>
          </div>
          <div class="reply-list" v-if="c.replies && c.replies.length">
            <div v-for="r in c.replies" :key="r.id" class="reply-item">
              <div class="reply-content">{{ r.content }}</div>
              <div class="reply-meta">用户 {{ r.userId }} · {{ formatTime(r.createdAt) }}</div>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getProduct } from '../api/products'
import { addCartItem } from '../api/cart'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getComments, createComment } from '../api/comments'
import { checkFavorite, addFavorite, removeFavorite } from '../api/favorites'

const route = useRoute()
const p = ref(null)
const seeds = ref([])
const num = ref(1)
const router = useRouter()
const comments = ref([])
const newComment = ref('')
const replyTo = ref(null)
const replyText = ref('')
const fav = ref(false)

onMounted(async () => {
  const id = route.params.id
  try {
    const data = await getProduct(id)
    p.value = data
    seeds.value = data && data.images && data.images.length ? data.images : ['p'+id]
  } catch (e) {
    p.value = null
    seeds.value = ['p'+id]
  }
  await loadComments()
  await loadFav()
})

function url(v) {
  if (typeof v === 'string' && (/^https?:\/\//.test(v) || v.startsWith('data:'))) return v
  const base = (() => {
    const envBase = import.meta.env.VITE_API_BASE
    if (envBase) return envBase
    const { hostname, port } = window.location
    if (port === '10001' || hostname === 'localhost') return 'http://localhost:10002/api'
    return '/api'
  })().replace(/\/api$/, '')
  if (typeof v === 'string' && v.startsWith('/')) return base + v
  const seed = v || (p.value ? ('p'+p.value.id) : 'p1')
  return 'https://picsum.photos/seed/' + seed + '/720/360'
}

async function add() {
  if (!p.value || !p.value.id) return
  try {
    await ElMessageBox.confirm('确认将该商品加入购物车？', '确认操作', { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' })
    await addCartItem({ productId: p.value.id, qty: num.value })
    ElMessage.success('已加入购物车')
  } catch (e) {
    if (e && e !== 'cancel') ElMessage.error(String(e.message || '加入购物车失败'))
  }
}

async function buyNow() {
  if (!p.value || !p.value.id) return
  try {
    await ElMessageBox.confirm('确认立即购买该商品？', '确认操作', { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' })
    const pending = { items: [{ productId: p.value.id, qty: num.value }] }
    localStorage.setItem('pendingPurchase', JSON.stringify(pending))
    router.push('/order/submit')
  } catch (e) {
    if (e && e !== 'cancel') ElMessage.error(String(e.message || '立即购买失败'))
  }
}

async function loadComments() {
  try {
    const id = route.params.id
    comments.value = await getComments(id)
  } catch (e) {
    comments.value = []
  }
}

async function postComment() {
  if (!newComment.value.trim()) return
  try {
    await createComment({ productId: p.value.id, content: newComment.value.trim() })
    newComment.value = ''
    await loadComments()
    ElMessage.success('评论已提交')
  } catch (e) {
    ElMessage.error(String(e.message || '评论失败'))
  }
}

function openReply(c) { replyTo.value = c.id; replyText.value = '' }
function cancelReply() { replyTo.value = null; replyText.value = '' }

async function postReply(c) {
  if (!replyText.value.trim()) return
  try {
    await createComment({ parentId: c.id, content: replyText.value.trim() })
    replyTo.value = null
    replyText.value = ''
    await loadComments()
    ElMessage.success('回复已提交')
  } catch (e) {
    ElMessage.error(String(e.message || '回复失败'))
  }
}

function formatTime(t) {
  try {
    return new Date(t).toLocaleString()
  } catch {
    return ''
  }
}

async function loadFav() {
  try {
    const id = route.params.id
    const ret = await checkFavorite(id)
    fav.value = !!ret.favorite
  } catch {
    fav.value = false
  }
}

async function toggleFav() {
  try {
    const id = route.params.id
    if (fav.value) {
      await removeFavorite(id)
      fav.value = false
      ElMessage.success('已取消收藏')
    } else {
      await addFavorite(id)
      fav.value = true
      ElMessage.success('已收藏')
    }
  } catch (e) {
    ElMessage.error(String(e.message || '操作失败'))
  }
}
</script>

<style scoped>
.container { max-width: 1080px; margin: 0 auto }
.detail { display: flex; gap: 24px }
.left { flex: 1 }
.right { flex: 1 }
.bigpic { width: 100%; height: 360px; object-fit: cover }
.title { font-size: 20px; margin-bottom: 8px }
.price { color: #F56C6C; font-size: 24px; margin-bottom: 8px }
.stock { margin-bottom: 8px }
.desc { margin-bottom: 16px; color: #606266 }
.actions { margin-top: 16px; display: flex; gap: 12px }
.comments { margin-top: 24px }
.section-title { margin-bottom: 8px; font-weight: 600 }
.comment-item { padding: 12px 0; border-bottom: 1px solid #ebeef5 }
.comment-content { margin-bottom: 6px }
.comment-meta { color: #909399; font-size: 12px; margin-bottom: 6px }
.reply-item { padding: 8px 12px; background: #f5f7fa; border-radius: 6px; margin-top: 6px }
.reply-meta { color: #909399; font-size: 12px }
</style>
