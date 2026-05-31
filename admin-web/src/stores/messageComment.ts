import { ref } from 'vue'
import { defineStore } from 'pinia'

import commentApi from '@/apis/messageComment'

import type { PageDataState } from '@/types/resultType'
import type { CommentSearchState, CommentState } from '@/types/commentType'

export const messageCommentStore = defineStore('comment', () => {
  const list = ref<PageDataState>()

  const searchParams = ref<CommentSearchState>({
    state: 2,
    page: 1,
    size: 10,
  })

  function getList(params: CommentSearchState) {
    searchParams.value = params
    commentApi.getList(params).then((rs) => {
      list.value = rs.data
    })
  }

  function review(params: CommentState) {
    commentApi.revComment(params).then(() => {
      getList(searchParams.value)
    })
  }

  return { list, getList, review }
})
