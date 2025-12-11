import http from './http'

export function createOrder(addressId) {
  return http.post('/orders', { addressId }).then(r => r.data)
}

export function getOrder(id) {
  return http.get(`/orders/${id}`).then(r => r.data)
}

export function listOrders(query = {}) {
  return http.get('/orders', { params: query }).then(r => r.data)
}

export function updateOrderStatus(id, status) {
  return http.patch(`/orders/${id}/status`, { status }).then(r => r.data)
}

