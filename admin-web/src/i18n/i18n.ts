import { createI18n } from 'vue-i18n'
import { localCache } from '../utils/storage'
import enUS from './locales/enUS'
import zhCN from './locales/zhCN'

const messages = {
  enUS,
  zhCN,
}

export const getLocale = (): string => {
  console.log(localCache.getCache('locale'))
  const locale: string = localCache.getCache('locale') ?? 'zhCN'
  return locale
}

export const setLocale = (locale: string) => {
  localCache.setCache('locale', locale)
}
// 创建i18n实例
export const i18n = createI18n({
  locale: getLocale(),
  legacy: false, //Composition API 模式
  globalInjection: true, //是否全局注册
  messages,
})
