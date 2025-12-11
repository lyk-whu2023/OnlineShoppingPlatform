import http from './http'

export function getPayment(orderId) {
  return http.get(`/payments/${orderId}`).then(r => r.data)
}

export function upsertPayment(orderId, data) {
  return http.put(`/payments/${orderId}`, data).then(r => r.data)
}

