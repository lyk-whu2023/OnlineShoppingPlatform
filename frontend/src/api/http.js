import axios from 'axios'

const http = axios.create({ baseURL: '/api', timeout: 10000 })

http.interceptors.response.use(
  r => r,
  e => {
    const msg = e && e.response && e.response.data && e.response.data.message ? e.response.data.message : '请求失败'
    return Promise.reject(new Error(msg))
  }
)

export default http
