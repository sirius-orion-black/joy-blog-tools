import { ref } from 'vue'
import { defineStore } from 'pinia'

import menuApi from '@/apis/menu'
import type { MenuTypeState, MenuStackItemState, MenuStackState } from '@/types/menuType'
import { sessionCache } from '@/utils/storage'

export const menuStore = defineStore('menu', () => {
  const menuList = ref<MenuTypeState[]>()
  const menuDraw = ref<boolean>(false)
  const menuIcons = ref<[]>()
  const menuStack = ref<MenuStackState>({
    current: null,
    items: [],
  })
  const menuStackStatus = ref<boolean>(true)

  function getMenu() {
    menuApi.getList().then((rs) => {
      menuList.value = rs.data
    })
  }

  function getMenuIcons() {
    //获取菜单icon列表
    menuApi.getMenuIcons().then((rs) => {
      menuIcons.value = rs.data
    })
  }

  function setMenuDraw(vl: boolean) {
    //是否显示菜单编辑
    menuDraw.value = vl
  }

  function addMenu(menu: MenuTypeState) {
    //新增菜单
    menuApi.addMenu(menu).then(() => {
      getMenu()
    })
  }

  function editMenu(menu: MenuTypeState) {
    //编辑菜单
    menuApi.editMenu(menu).then(() => {
      getMenu()
    })
  }

  function delMenu(menu: number[]) {
    //批量删除菜单
    if (menu.length > 0) {
      menuApi.delMenu(menu).then(() => {
        getMenu()
      })
    }
  }

  //设置顶部菜单
  function setMenuStack(item: MenuStackItemState) {
    if (!!item.key && item.path != 'undefined' && item.name != 'undefined') {
      if (menuStack.value && menuStack.value.items && menuStack.value.items.length > 0) {
        const items: MenuStackItemState[] = menuStack.value.items
        const index = items.findIndex((menu) => menu.key === item.key)
        if (index !== -1)
          // 覆盖现有项 (保留响应性)
          items.splice(index, 1, item)
        else
          // 添加新项
          items.push(item)
        menuStack.value.items = items
      } else {
        menuStack.value.items?.push(item)
      }
      menuStack.value.current = item
      sessionCache.setCache('menuItem', menuStack.value)
    }
  }
  //获取顶部菜单
  function getMenuStack() {
    const menuItem = sessionCache.getCache('menuItem') as MenuStackState
    if (menuItem) menuStack.value = menuItem
  }

  // 递归查与现在path相同 的菜单项
  function findPathMenu(itemList: MenuTypeState[], path: string): MenuTypeState | null {
    for (const item of itemList) {
      if (item.path === path) return item // 找到目标菜单项

      if (item.children?.length) {
        const result = findPathMenu(item.children, path)
        if (result) return result // 在子菜单中找到目标
      }
    }
    return null
  }

  //获取顶部菜单
  function getCurrentMenuStack(menus: MenuTypeState[], path: string) {
    if (menuStackStatus.value) {
      if (menuStack.value.current || menuStack.value.items) {
        menuStackStatus.value = false
        const menuList: MenuTypeState[] = menus || []
        if (menuList.length > 0) {
          const item: MenuTypeState = findPathMenu(menuList, path) || {}
          setMenuStack({
            path: item.path + '',
            name: item.name + '',
            key: item.id as number,
          })
        }
        setTimeout(() => {
          menuStackStatus.value = true
        }, 1000)
      }
    }
  }

  //删除顶部菜单
  function delMenuStack(item: MenuStackItemState) {
    if (menuStack.value && menuStack.value.items && menuStack.value.items.length > 0) {
      const items: MenuStackItemState[] = menuStack.value.items
      const index = items.findIndex((menu) => menu.key === item.key)
      if (index > -1) items.splice(index, 1)
      menuStack.value.items = items
      sessionCache.setCache('menuItem', menuStack.value)
    }
  }

  return {
    menuList,
    menuDraw,
    menuIcons,
    menuStack,
    getMenu,
    setMenuDraw,
    getMenuIcons,
    addMenu,
    editMenu,
    delMenu,
    setMenuStack,
    getMenuStack,
    getCurrentMenuStack,
    delMenuStack,
  }
})
