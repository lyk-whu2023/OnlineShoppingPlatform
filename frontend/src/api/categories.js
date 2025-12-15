import http from './http'

export function getCategories() {
  return http.get('/categories').then(r => r.data)
}

export function searchCategories(params) {
  return http.get('/categories/list-admin', { params }).then(r => r.data)
}

export function createCategory(data) {
  return http.post('/categories', data).then(r => r.data)
}

export function updateCategory(id, data) {
  return http.put(`/categories/${id}`, data).then(r => r.data)
}

export function deleteCategory(id) {
  return http.delete(`/categories/${id}`).then(r => r.data)
}
