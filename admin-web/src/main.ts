import './assets/styles/main.scss'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import Antd from 'ant-design-vue'
import { IconFont } from '@/utils/iconfont'

import App from './App.vue'
import router from './router'
import { i18n } from './i18n/i18n'
import 'ant-design-vue/dist/reset.css'

const app = createApp(App)
const pinia = createPinia()
// 添加插件：为所有 store 实现 $reset()
pinia.use(({ store }) => {
  const initialState = JSON.parse(JSON.stringify(store.$state)) // 深拷贝初始状态
  store.$reset = () => {
    store.$patch(JSON.parse(JSON.stringify(initialState))) // 重置状态
  }
})
// 全站禁用右键菜单
// document.addEventListener('contextmenu', (event) => {
//   event.preventDefault()
// })

app.use(pinia)
app.use(i18n)
app.use(router)
app.use(Antd)
app.component('IconFont', IconFont)
app.mount('#app')
