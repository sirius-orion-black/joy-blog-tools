import { defineStore } from 'pinia'
import { ref } from 'vue'

import labelApi from '@/apis/contentLabel'

import type { LabelTypeSearchState, LabelTypeState } from '@/types/labelType'
import type { PageDataState } from '@/types/resultType'

export const contentLabelStore = defineStore('contentLabel', () => {
  const labelList = ref<PageDataState>()
  const searchParams = ref<LabelTypeSearchState>({
    name: '',
    state: null,
    page: 1,
    size: 10,
  })

  function getList(params: LabelTypeSearchState) {
    searchParams.value = params
    labelApi.getList(params).then((rs) => (labelList.value = rs.data))
  }

  function addLabel(params: LabelTypeState) {
    labelApi.addLabel(params).then(() => {
      getList(searchParams.value)
    })
  }
  function editLabel(params: LabelTypeState) {
    labelApi.editLabel(params).then(() => {
      getList(searchParams.value)
    })
  }

  function delLabel(params: number[]) {
    labelApi.delLabel(params).then(() => {
      getList(searchParams.value)
    })
  }

  return { labelList, getList, addLabel, editLabel, delLabel }
})
