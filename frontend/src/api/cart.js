import http from './http'

export function getCart() {
  return http.get('/cart').then(r => r.data)
}

export function addCartItem(data) {
  return http.post('/cart/items', data).then(r => r.data)
}

export function updateCartItem(productId, qty) {
  return http.put(`/cart/items/${productId}`, { qty }).then(r => r.data)
}

export function removeCartItem(productId) {
  return http.delete(`/cart/items/${productId}`).then(r => r.data)
}

export function clearCart() {
  return http.delete('/cart').then(r => r.data)
}

