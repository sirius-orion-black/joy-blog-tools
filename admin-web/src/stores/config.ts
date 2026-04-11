import { defineStore } from 'pinia'
import configApi from '@/apis/config'

import type { ConfigState } from '@/types/configType'

export const configStore = defineStore('config', () => {
  async function getConfig() {
    const res = await configApi.getConfig()
    return res.data
  }

  async function editConfig(params: ConfigState) {
    return await configApi.editConfig(params)
  }

  return { getConfig, editConfig }
})
