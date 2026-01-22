import { defineStore } from 'pinia'
import { ref } from 'vue'

import labelApi from '@/apis/contentLabel'

import type { LabelTypeSearchState, LabelTypeState } from '@/types/labelType'

export const contentLabelStore = defineStore('contentLabel', () => {
  const labelList = ref<LabelTypeState[]>()

  function getList(params: LabelTypeSearchState) {
    labelApi.getList(params).then((rs) => (labelList.value = rs.data))
  }

  return { labelList, getList }
})
