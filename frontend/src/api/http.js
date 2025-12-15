import axios from 'axios'
import router from '../router'
import { ElMessage } from 'element-plus'

const apiBase = (() => {
  const envBase = import.meta.env.VITE_API_BASE
  if (envBase) return envBase
  const { hostname, port } = window.location
  if (port === '10001') return 'http://localhost:10002/api'
  if (hostname === 'localhost') return 'http://localhost:10002/api'
  return '/api'
})()

const http = axios.create({ baseURL: apiBase, timeout: 10000 })

http.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) config.headers['Authorization'] = `Bearer ${token}`
  return config
})

http.interceptors.response.use(
  r => r,
  e => {
    const status = e && e.response && e.response.status
    if (status === 401) {
      const path = window.location.pathname + window.location.search
      router.push({ path: '/login', query: { redirect: path } })
    }
    if (status === 403) {
      ElMessage.error('无权限访问该资源')
      const path = window.location.pathname + window.location.search
      if (path.startsWith('/admin')) router.push({ path: '/admin/login' })
    }
    const msg = e && e.response && e.response.data && e.response.data.message ? e.response.data.message : '请求失败'
    return Promise.reject(new Error(msg))
  }
)

export default http
