import axios from 'axios'
import { message } from 'ant-design-vue'
import { i18n } from '@/i18n'
import router from '@/router'

import { userLoginStore } from '@/stores/login'
import { localCache, sessionCache } from './storage'

const service = axios.create({
  baseURL: window.location.origin + '/api/',
  timeout: 9000,
})

const { t } = i18n.global

// 添加请求拦截器
service.interceptors.request.use(
  // 在发送请求之前做些什么
  function (config) {
    config.headers['X-Timestamp'] = Date.now()
    if (!config.url?.includes('/admin/auth/')) {
      const user = localCache.getCache<Record<string, string>>('user') ?? sessionCache.getCache<Record<string, string>>('user')
      const token = user?.token ?? undefined
      config.headers['X-Auth-Token'] = token
    }

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
    // 超出 2xx 范围的状态码都会触发该函数。
    if (error.message.includes('timeout')) message.error(t('request.system_timed_out'))
    if (error.status === 404 || error.status === 400 || error.status === 500) message.error(t('request.server_issue_again'))
    console.log(33332123123123, error)
    return Promise.reject(error)
  },
)

export default service
