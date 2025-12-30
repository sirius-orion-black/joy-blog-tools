import axios from 'axios'
import { localCache, sessionCache } from './storage'

const service = axios.create({
  baseURL: window.location.origin + '/api/',
  timeout: 9000,
})

// 添加请求拦截器
service.interceptors.request.use(
  function (config) {
    config.headers['X-Timestamp'] = Date.now()
    // if (config.method === 'post') config.headers['Content-Type'] = 'multipart/form-data'
    // if (config.method === 'post') config.headers['Content-Type'] = 'application/json'
    console.log(7777777777777, config, config.url)
    if (!config.url?.includes('/admin/auth/')) {
      const user = localCache.getCache<Record<string, string>>('user') ?? sessionCache.getCache<Record<string, string>>('user')
      const token = user?.token ?? undefined
      config.headers['X-Auth-Token'] = token
    }
    // 在发送请求之前做些什么
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
    // console.log(3123123123, response)
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
    return response.data
  },
  function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    return Promise.reject(error)
  },
)

export default service
