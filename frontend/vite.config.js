import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
    plugins: [vue()],
    server: {
        host: '0.0.0.0',
        port: 3000,
        allowedHosts: ['liajay.cn'],
        proxy: {
            '/api': {
                target: 'http://localhost:8080',  // 指向网关
                changeOrigin: true,
                secure: false,
                rewrite: (path) => path.replace(/^\/api/, '/api')  // 保持/api前缀
            }
        }
    }
})
