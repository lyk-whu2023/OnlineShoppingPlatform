import http from './http'

export function getAddresses() {
  return http.get('/addresses').then(r => r.data)
}

export function createAddress(data) {
  return http.post('/addresses', data).then(r => r.data)
}

export function updateAddress(id, data) {
  return http.put(`/addresses/${id}`, data).then(r => r.data)
}

export function deleteAddress(id) {
  return http.delete(`/addresses/${id}`).then(r => r.data)
}
