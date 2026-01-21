import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/auth': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
      '/pets': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
      '/species': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
      '/breeds': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
      '/users': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
    }
  }
})
