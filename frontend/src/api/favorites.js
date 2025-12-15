import http from './http'

export function getFavorites() {
  return http.get('/favorites').then(r => r.data)
}

export function addFavorite(productId) {
  return http.post('/favorites', { productId }).then(r => r.data)
}

export function removeFavorite(productId) {
  return http.delete(`/favorites/${productId}`).then(r => r.data)
}

export function checkFavorite(productId) {
  return http.get('/favorites/check', { params: { productId } }).then(r => r.data)
}
