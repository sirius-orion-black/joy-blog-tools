import { defineStore } from 'pinia'
import { ref } from 'vue'

import blogpostApi from '@/apis/contentBlogpost'

import type { ArticleSearchState, ArticleState, ArticleDrawerState, ArticleUpdateState } from '@/types/articleType'
import type { PageDataState } from '@/types/resultType'
import type { LabelTypeState, ColumnState } from '@/types/labelType'

export const blogpostStore = defineStore('blogpost', () => {
  const list = ref<PageDataState>()
  const searchParams = ref<ArticleSearchState>({
    title: '',
    type: null,
    state: null,
    page: 1,
    size: 10,
  })
  const classify = ref<LabelTypeState>()
  const label = ref<LabelTypeState[]>()
  const column = ref<ColumnState>()
  const drawInfo = ref<ArticleDrawerState>()

  //获取列表
  function getList(params: ArticleSearchState) {
    searchParams.value = params
    blogpostApi.getList(params).then((rs) => (list.value = rs.data))
  }

  //获取分类
  function getClassify() {
    blogpostApi.getClassify().then((rs) => {
      classify.value = rs.data
    })
  }

  //获取标签
  function getLabel() {
    blogpostApi.getLabel().then((rs) => {
      label.value = rs.data
    })
  }

  //获取专栏
  function getColumn() {
    blogpostApi.getColumn().then((rs) => {
      column.value = rs.data
    })
  }

  function setDrawInfo(params: ArticleDrawerState) {
    drawInfo.value = params
  }

  function addArticle(params: ArticleState) {
    blogpostApi.addArticle(params).then(() => {
      getList(searchParams.value)
    })
  }

  function editArticle(params: ArticleState) {
    blogpostApi.editArticle(params).then(() => {
      getList(searchParams.value)
    })
  }

  function delArticle(params: ArticleState) {
    blogpostApi.delArticle(params).then(() => {
      getList(searchParams.value)
    })
  }

  function updArticle(params: ArticleUpdateState) {
    blogpostApi.updArticle(params).then(() => {
      getList(searchParams.value)
    })
  }

  return {
    list,
    classify,
    label,
    column,
    drawInfo,
    getList,
    getClassify,
    getLabel,
    getColumn,
    setDrawInfo,
    addArticle,
    editArticle,
    delArticle,
    updArticle,
  }
})
