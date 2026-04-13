import service from '../utils/request'
import type { AxiosRequestConfig } from 'axios'

export default {
  singUpliad: (params: unknown, fun: AxiosRequestConfig) => service.post('/infra/manage/upload', params, fun),
}
