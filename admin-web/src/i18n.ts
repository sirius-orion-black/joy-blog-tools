import { createI18n } from 'vue-i18n'
import en from './locales/en'
import zh from './locales/zh'

const messages = {
  en,
  zh,
}
// 创建i18n实例
export const i18n = createI18n({
  locale: 'zh',
  legacy: false, //Composition API 模式
  globalInjection: true, //是否全局注册
  messages,
})
