import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 10001,
    proxy: {
      '/api': {
        target: 'http://localhost:10002',
        changeOrigin: true
      }
    }
  }
})
