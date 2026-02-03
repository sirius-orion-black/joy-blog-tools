import service from '../utils/request'
import type { AxiosRequestConfig } from 'axios'

export default {
  singUpliad: (params: unknown, fun: AxiosRequestConfig) => service.post('/file/manage/upload', params, fun),
}
