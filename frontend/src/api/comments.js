import http from './http'

export function getComments(productId) {
  return http.get('/comments', { params: { productId } }).then(r => r.data)
}

export function createComment({ productId, content, parentId }) {
  return http.post('/comments', { productId, content, parentId }).then(r => r.data)
}

export function adminListComments() {
  return http.get('/comments/admin').then(r => r.data)
}

export function deleteComment(id) {
  return http.delete(`/comments/${id}`).then(r => r.data)
}

export function updateCommentStatus(id, status) {
  return http.patch(`/comments/${id}/status`, { status }).then(r => r.data)
}
