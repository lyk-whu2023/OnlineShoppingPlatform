import http from './http'

export function getProducts(params) {
  return http.get('/products', { params }).then(r => r.data)
}

export function getProduct(id) {
  return http.get(`/products/${id}`).then(r => r.data)
}

export function createProduct(data) {
  return http.post('/products', data).then(r => r.data)
}

export function updateProduct(id, data) {
  return http.put(`/products/${id}`, data).then(r => r.data)
}

export function deleteProduct(id) {
  return http.delete(`/products/${id}`).then(r => r.data)
}
