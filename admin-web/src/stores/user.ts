import { defineStore } from 'pinia'
import { ref } from 'vue'

import userApi from '@/apis/user'
import type { UserSearchTypeState, UserTypeState, UserDrawerState, UserMenuState } from '@/types/UserType'
import type { PageDataState } from '../types/result'

export const userStore = defineStore('user', () => {
  const list = ref<PageDataState>()
  const permission = ref<UserMenuState>()
  const userDrawer = ref<UserDrawerState>({
    type: '',
    show: false,
    width: 0,
    text: {},
  })

  function getList(params: UserSearchTypeState) {
    userApi.getList(params).then((rs) => {
      list.value = rs.data
    })
  }

  function setUserDrawer(type: string, show: boolean, width: number, text: UserTypeState = {}) {
    userDrawer.value = {
      type,
      show,
      width,
    }
    if (JSON.stringify(text) !== '{}') userDrawer.value.text = text
  }

  async function addUser(params: UserTypeState) {
    const res = await userApi.add(params)
    return res.data
  }
  async function editUser(params: UserTypeState) {
    const res = await userApi.edit(params)
    return res
  }
  async function delUser(params: number[]) {
    const res = await userApi.delete(params)
    return res
  }
  async function isBannedUser(params: UserTypeState[]) {
    const res = await userApi.isBanned(params)
    return res
  }

  function getPermission(params: UserMenuState) {
    userApi.getPermission(params).then((rs) => {
      permission.value = rs.data
    })
  }

  function editPermission(params: UserMenuState) {
    userApi.editPermission(params)
  }

  return {
    list,
    permission,
    userDrawer,
    setUserDrawer,
    getList,
    addUser,
    editUser,
    delUser,
    isBannedUser,
    getPermission,
    editPermission,
  }
})
