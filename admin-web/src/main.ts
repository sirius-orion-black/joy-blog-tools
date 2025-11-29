import './assets/styles/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { createI18n } from 'vue-i18n'
import Antd from 'ant-design-vue'

import App from './App.vue'
import router from './router'
import en from './locales/en'
import zh from './locales/zh'
import 'ant-design-vue/dist/reset.css'

const messages = {
  en,
  zh,
}

// 创建i18n实例
const i18n = createI18n({
  locale: 'zh',
  legacy: false, //Composition API 模式
  globalInjection: true, //是否全局注册
  messages,
})

const app = createApp(App)

app.use(createPinia())
app.use(i18n)
app.use(router)
app.use(Antd)

app.mount('#app')
