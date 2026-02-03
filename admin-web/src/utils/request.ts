import axios from 'axios'
import type { AxiosRequestConfig } from 'axios'
import { message } from 'ant-design-vue'
import { i18n } from '@/i18n/i18n'
import router from '@/router'

import { userLoginStore } from '@/stores/login'
import { localCache, sessionCache } from './storage'

const service = axios.create({
  baseURL: window.location.origin + '/api/',
  timeout: 9000,
})

const { t } = i18n.global

// 存储节流状态
const throttleStore = new Map<string, number>()

// 高效生成请求标识
const generateRequestKey = (config: AxiosRequestConfig): string => {
  const { method, url, params, data } = config
  let key = `${method}-${url}`

  // 处理查询参数
  if (params) {
    const paramsStr = Object.keys(params)
      .sort()
      .map((k) => `${k}=${params[k]}`)
      .join('&')
    key += `?${paramsStr}`
  }

  // 处理请求体（仅处理文本数据）
  if (data && typeof data === 'string') {
    key += `|${data}`
  }

  return key
}
// 动态节流时间配置
const apiThrottleTimes: Record<string, number> = {
  //这里面可以放指定链接并设置相对应时间
  default: 2000, // 默认2秒
}

// 添加请求拦截器
service.interceptors.request.use(
  // 在发送请求之前做些什么
  function (config) {
    const now = Date.now()
    config.headers['X-Timestamp'] = now
    if (!config.url?.includes('/admin/auth/')) {
      const user = localCache.getCache<Record<string, string>>('user') ?? sessionCache.getCache<Record<string, string>>('user')
      const token = user?.token ?? undefined
      config.headers['X-Auth-Token'] = token
    }
    const key = generateRequestKey(config)
    // 获取该API的节流时间
    const throttleTime = config.url && apiThrottleTimes[config.url] ? apiThrottleTimes[config.url] : apiThrottleTimes.default
    // 检查节流状态
    if (throttleStore.has(key) && now - throttleStore.get(key)! < (throttleTime as number)) {
      return Promise.reject({
        message: `${t('request.request_been_throttled')}: ${key}`,
        config,
        isThrottled: true,
      })
    }
    // 更新节流时间戳
    throttleStore.set(key, now)
    return config
  },
  function (error) {
    // 对请求错误做些什么
    return Promise.reject(error)
  },
)

// 添加响应拦截器
service.interceptors.response.use(
  function (response) {
    // 2xx 范围内的状态码都会触发该函数。
    const { data } = response
    const login = userLoginStore()
    if (data.code >= 200 && data.code < 300) message.success(t('request.' + data.message))
    else if (data.code === 401) {
      router.push('/login')
      login.cleanUserInfo()
      window.location.reload()
      console.log('请重新登录')
    } else if (data.code >= 402 && data.code < 500) message.error(t('request.' + data.message))
    else message.warning(t('request.' + data.message))
    return data
  },
  function (error) {
    if (error.isThrottled) {
      //节流请求处理error.message
      message.warning(t('request.operation_too_frequent'))
      return Promise.resolve({
        data: { message: t('request.operation_too_frequent') },
        status: 429,
      })
    }
    // 超出 2xx 范围的状态码都会触发该函数。
    if (error.message.includes('timeout')) message.error(t('request.system_timed_out'))
    if (error.status === 404 || error.status === 400 || error.status === 500) message.error(t('request.server_issue_again'))
    console.log(33332123123123, error)
    return Promise.reject(error)
  },
)

// 定时清理过期节流记录（防止内存泄漏）
setInterval(
  () => {
    const now = Date.now()
    const cleanupThreshold = 5 * 60 * 1000 // 5分钟

    for (const [key, timestamp] of throttleStore) {
      if (now - timestamp > cleanupThreshold) {
        throttleStore.delete(key)
      }
    }
  },
  10 * 60 * 1000,
) // 每10分钟清理一次

export default service
