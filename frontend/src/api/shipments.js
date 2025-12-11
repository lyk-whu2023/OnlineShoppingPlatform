import http from './http'

export function getShipment(orderId) {
  return http.get(`/shipments/${orderId}`).then(r => r.data)
}

export function upsertShipment(orderId, data) {
  return http.put(`/shipments/${orderId}`, data).then(r => r.data)
}

