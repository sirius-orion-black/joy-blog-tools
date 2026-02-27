import { ref } from 'vue'
import { defineStore } from 'pinia'

import typeApi from '@/apis/contentClassify'

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
    searchParams.value = params
    typeApi.getList(params).then((rs) => (typeList.value = rs.data))
  }

  function addClassify(params: LabelTypeState) {
    typeApi.addClassify(params).then(() => {
      getList(searchParams.value)
    })
  }

  function editClassify(params: LabelTypeState) {
    typeApi.editClassify(params).then(() => {
      getList(searchParams.value)
    })
  }

  function delClassify(params: number[]) {
    typeApi.delClassify(params).then(() => {
      getList(searchParams.value)
    })
  }

  return { typeList, getList, addClassify, editClassify, delClassify }
})
