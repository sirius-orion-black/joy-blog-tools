import { defineStore } from 'pinia'
import { ref } from 'vue'

import dashApi from '@/apis/dashBoard'

import type { DashBlogState } from '@/types/dashType'

export const dashBoardStore = defineStore('dashBoard', () => {
  const dashBlog = ref<DashBlogState>()

  function getDashBlog() {
    dashApi.getDashBlog().then((rs) => (dashBlog.value = rs.data))
  }

  return { dashBlog, getDashBlog }
})
