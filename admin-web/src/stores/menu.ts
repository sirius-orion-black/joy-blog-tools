import { ref } from 'vue'
import { defineStore } from 'pinia'

import menuApi from '@/apis/menu'
import type { MenuType } from '@/types/MenuType'

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

  function addMenu(menu: MenuType) {
    //新增菜单
    menuApi.addMenu(menu).then((rs) => {
      getMenu()
      console.log('add menu', rs)
    })
  }

  function editMenu(menu: MenuType) {
    //编辑菜单
    menuApi.editMenu(menu).then((rs) => {
      getMenu()
      console.log('edit menu', rs)
    })
  }

  function delMenu(menu: number[]) {
    //批量删除菜单
    if (menu.length > 0) {
      console.log(menu, '==========删除了==========')
      menuApi.delMenu(menu).then((rs) => {
        getMenu()
        console.log('edit menu', rs)
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
