import { defineStore } from 'pinia'
import { ref } from 'vue'
import momentsApi from '@/apis/contentMoments'

import type { momentsState, momentsSearchState } from '@/types/moments'
import type { PageDataState } from '@/types/resultType'
import type { LabelTypeState } from '@/types/labelType'

export const momentsStore = defineStore('moments', () => {
  const list = ref<PageDataState>()
  const searchParams = ref<momentsSearchState>({
    name: '',
    page: 1,
    size: 10,
  })
  const label = ref<LabelTypeState[]>()

  function getList(params: momentsSearchState) {
    searchParams.value = params
    momentsApi.getList(params).then((rs) => (list.value = rs.data))
  }

  //获取标签
  function getLabel() {
    momentsApi.getLabel().then((rs) => {
      label.value = rs.data
    })
  }

  //发表朋友圈
  function addmoments(params: momentsState) {
    momentsApi.addmoments(params).then(() => {
      getList(searchParams.value)
    })
  }

  //删除朋友圈
  function delmoments(params: momentsState) {
    momentsApi.delmoments(params).then(() => {
      getList(searchParams.value)
    })
  }

  //朋友圈违规处理
  function revMoments(params: momentsState) {
    momentsApi.revMoments(params).then(() => {
      getList(searchParams.value)
    })
  }

  return { list, label, getList, getLabel, addmoments, delmoments, revMoments }
})
