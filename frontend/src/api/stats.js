import http from './http'

export function getStats() {
  return http.get('/stats').then(r => r.data)
}
