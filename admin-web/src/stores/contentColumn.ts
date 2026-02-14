import { defineStore } from 'pinia'
import { ref } from 'vue'

import columnApi from '@/apis/contentColumn'

import type { LabelTypeSearchState, ColumnState } from '@/types/labelType'
import type { PageDataState } from '@/types/resultType'

export const contentColumnStore = defineStore('contentColumn', () => {
  const columnList = ref<PageDataState>()
  const searchParams = ref<LabelTypeSearchState>({
    name: '',
    state: null,
    page: 1,
    size: 10,
  })

  function getList(params: LabelTypeSearchState) {
    columnApi.getList(params).then((rs) => (columnList.value = rs.data))
  }

  function addColumn(params: ColumnState) {
    columnApi.addColumn(params).then(() => {
      getList(searchParams.value)
    })
  }

  function editColumn(params: ColumnState) {
    columnApi.editColumn(params).then(() => {
      getList(searchParams.value)
    })
  }

  function delColumn(params: ColumnState) {
    columnApi.delColumn(params).then(() => {
      getList(searchParams.value)
    })
  }

  return { columnList, getList, addColumn, editColumn, delColumn }
})
