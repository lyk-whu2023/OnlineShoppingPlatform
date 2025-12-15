import http from './http'

export function login(data) {
  return http.post('/auth/login', data).then(r => {
    const token = r.data.token
    const user = r.data.user
    if (user && user.id) localStorage.setItem('userId', String(user.id))
    if (token) localStorage.setItem('token', token)
    if (user) localStorage.setItem('user', JSON.stringify(user))
    return r.data
  })
}

export function register(data) {
  return http.post('/auth/register', data).then(r => {
    const token = r.data.token
    const user = r.data.user
    if (user && user.id) localStorage.setItem('userId', String(user.id))
    if (token) localStorage.setItem('token', token)
    if (user) localStorage.setItem('user', JSON.stringify(user))
    return r.data
  })
}

export function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('userId')
  localStorage.removeItem('user')
  return http.post('/auth/logout').then(() => {
    if (typeof window !== 'undefined' && window.location) window.location.reload()
  })
}
