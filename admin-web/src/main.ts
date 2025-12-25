import './assets/styles/main.scss'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import Antd from 'ant-design-vue'
import { IconFont } from '@/utils/iconfont'

import App from './App.vue'
import router from './router'
import { i18n } from './i18n'
import 'ant-design-vue/dist/reset.css'

const app = createApp(App)

app.use(createPinia())
app.use(i18n)
app.use(router)
app.use(Antd)
app.component('IconFont', IconFont)
app.mount('#app')
