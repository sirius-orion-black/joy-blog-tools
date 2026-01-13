import { defineStore } from 'pinia'
import { ref } from 'vue'

import userApi from '@/apis/user'
import type { UserSearchTypeState, UserTypeState, UserDrawerState, UserMenuState } from '@/types/UserType'
import type { PageDataState } from '@/types/result'
import type { MenuTypeState } from '@/types/MenuType'

import { localCache, sessionCache } from '@/utils/storage'
import { addDynamicRoutes } from '@/router'

export const userStore = defineStore('user', () => {
  const list = ref<PageDataState>()
  const permission = ref<UserMenuState>()
  const userDrawer = ref<UserDrawerState>({
    type: '',
    show: false,
    width: 0,
    text: {},
  })
  const menuList = ref<MenuTypeState[]>()

  function getList(params: UserSearchTypeState) {
    //获取用户列表
    userApi.getList(params).then((rs) => {
      list.value = rs.data
    })
  }

  function setUserDrawer(type: string, show: boolean, width: number, text: UserTypeState = {}) {
    //弹窗设置
    userDrawer.value = {
      type,
      show,
      width,
    }
    if (JSON.stringify(text) !== '{}') userDrawer.value.text = text
  }

  async function addUser(params: UserTypeState) {
    //新增用户
    const res = await userApi.add(params)
    return res.data
  }
  async function editUser(params: UserTypeState) {
    //编辑用户
    const res = await userApi.edit(params)
    return res
  }
  async function delUser(params: number[]) {
    //删除用户
    const res = await userApi.delete(params)
    return res
  }
  async function isBannedUser(params: UserTypeState[]) {
    //封号与解禁
    const res = await userApi.isBanned(params)
    return res
  }

  function getPermission(params: UserMenuState) {
    //获取权限
    userApi.getPermission(params).then((rs) => {
      permission.value = rs.data
    })
  }

  function editPermission(params: UserMenuState) {
    //修改权限
    userApi.editPermission(params)
  }

  async function getMenuList(remember: boolean) {
    //获取用户所有菜单
    // userApi.getMenuList().then((rs) => {
    //   const menus = rs.data
    //   menuList.value = menus
    //   if (remember) localCache.setCache('menuList', rs.data)
    //   else sessionCache.setCache('menuList', rs.data)
    //   if (menus.length > 0) {
    //     addDynamicRoutes(menus)
    //   }
    // })
    const res = await userApi.getMenuList()
    const menus = res.data
    menuList.value = menus
    if (remember) localCache.setCache('menuList', menus)
    else sessionCache.setCache('menuList', menus)
    if (menus.length > 0) {
      addDynamicRoutes(menus)
    }
    return menus
  }

  function getCacheMenu() {
    const cached = localCache.getCache<Record<string, string>>('menuList') ?? sessionCache.getCache<Record<string, string>>('menuList')
    const menus = Array.isArray(cached) ? cached : []
    menuList.value = menus
    // if (menus.length > 0) {
    //   addDynamicRoutes(menus)
    // }
  }

  return {
    list,
    permission,
    userDrawer,
    menuList,
    setUserDrawer,
    getList,
    addUser,
    editUser,
    delUser,
    isBannedUser,
    getPermission,
    editPermission,
    getMenuList,
    getCacheMenu,
  }
})
