import { defineStore } from 'pinia'

import filesApi from '@/apis/files'
import type { AxiosRequestConfig } from 'axios'

export const filesManageStore = defineStore('filesManage', () => {
  async function singUpliad(params: unknown, fun: AxiosRequestConfig) {
    const res = await filesApi.singUpliad(params, fun)
    return res.data
  }
  return { singUpliad }
})
