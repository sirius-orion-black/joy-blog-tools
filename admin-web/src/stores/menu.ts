import { ref } from 'vue'
import { defineStore } from 'pinia'

import menuApi from '@/apis/menu'
import type { MenuTypeState } from '@/types/MenuType'

export const menuStore = defineStore('menu', () => {
  const menuList = ref<[]>()
  const menuDraw = ref<boolean>(false)
  const menuIcons = ref<[]>()

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

  return {
    menuList,
    menuDraw,
    menuIcons,
    getMenu,
    setMenuDraw,
    getMenuIcons,
    addMenu,
    editMenu,
    delMenu,
  }
})
