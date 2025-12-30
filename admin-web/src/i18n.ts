import { createI18n } from 'vue-i18n'
import { localCache } from './utils/storage'
import enUS from './locales/en'
import zhCN from './locales/zh'

const messages = {
  enUS,
  zhCN,
}

interface LocaleType {
  locale: string
  country: string
}

export const getLocale = (): string => {
  const cash: LocaleType = localCache.getCache('locale') ?? { locale: 'zh', country: 'CN' }
  return cash.locale + cash.country
}
// 创建i18n实例
export const i18n = createI18n({
  locale: getLocale(),
  legacy: false, //Composition API 模式
  globalInjection: true, //是否全局注册
  messages,
})
