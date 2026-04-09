import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'
import vueDevTools from 'vite-plugin-vue-devtools'

import prismjs from 'vite-plugin-prismjs'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueJsx(),
    vueDevTools(),
    prismjs({
      // 按需添加支持的高亮语言，如只需要js、css、html
      languages: ['javascript', 'css', 'html'],
      // 配置prism插件，如添加复制到剪贴板、显示语言名等功能
      plugins: ['copy-to-clipboard', 'show-language'],
      // 选择主题，可在node_modules中prismjs目录下themes文件夹查看可选主题
      theme: 'okaidia',
      css: true, // 自动引入主题CSS
    }),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  css: {
    preprocessorOptions: {
      scss: {
        additionalData: `@use "@/assets/styles/variables.scss" as *;`,
      },
    },
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:8081/',
        changeOrigin: true,
      },
    },
  },
  optimizeDeps: {
    include: ['vue-i18n'], // 根据需要包含其他依赖项或翻译文件路径
  },
})
