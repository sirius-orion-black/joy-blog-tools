import { ref } from 'vue'
import { defineStore } from 'pinia'

import typeApi from '@/apis/contentType'

import type { LabelTypeSearchState, LabelTypeState } from '@/types/labelType'
import type { PageDataState } from '@/types/resultType'

export const contentTypeStore = defineStore('contentType', () => {
  const typeList = ref<PageDataState>()
  const searchParams = ref<LabelTypeSearchState>({
    name: '',
    state: null,
    page: 1,
    size: 10,
  })

  function getList(params: LabelTypeSearchState) {
    typeApi.getList(params).then((rs) => (typeList.value = rs.data))
  }

  function addType(params: LabelTypeState) {
    typeApi.addType(params).then(() => {
      getList(searchParams.value)
    })
  }

  function editType(params: LabelTypeState) {
    typeApi.editType(params).then(() => {
      getList(searchParams.value)
    })
  }

  function delType(params: number[]) {
    typeApi.delType(params).then(() => {
      getList(searchParams.value)
    })
  }

  return { typeList, getList, addType, editType, delType }
})
