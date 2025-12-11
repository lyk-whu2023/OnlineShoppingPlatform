import http from './http'

export function getMe() {
  return http.get('/users/me').then(r => r.data)
}

export function listUsers() {
  return http.get('/users').then(r => r.data)
}

export function updateUserStatus(id, status) {
  return http.patch(`/users/${id}/status`, { status }).then(r => r.data)
}

export function updateMe(data) {
  return http.post('/users/me', data).then(r => r.data)
}

export function changePassword(data) {
  return http.post('/users/me/password', data).then(r => r.data)
}
