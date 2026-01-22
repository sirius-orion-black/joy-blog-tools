<template>
  <div class="menu-main">
    <div class="menu-pannel">
      <div class="menu-logo">
        <img src="@/assets/img/logo.png" />
        徐徐乐之
      </div>
      <div class="menu-container">
        <a-menu style="width: 271px" mode="inline" :selectedKeys="selectedMenu" :items="menuItems" @click="handleClick"></a-menu>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { h, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'

import type { ComputedRef } from 'vue'
import type { MenuProps } from 'ant-design-vue'
import type { MenuItemState, MenuTypeState, MenuStackItemState } from '@/types/menuType'

import { userStore } from '@/stores/user'
import { menuStore } from '@/stores/menu'
import { IconFont } from '@/utils/iconfont'

import { useI18n } from 'vue-i18n'

const router = useRouter()
const user = userStore()
const menu = menuStore()
const { t } = useI18n()

const selectedMenu = computed(() => {
  return [menu.menuStack.current?.key]
})

onMounted(() => {
  user.getCacheMenu()
  setTimeout(() => {
    menu.getMenuStack()
    const path: string = router.currentRoute.value.path
    menu.getCurrentMenuStack(user.menuList ?? [], path)
  }, 100)
})

function goPage(path: string) {
  router.push(path)
}

function convertMenu(data: MenuTypeState[]): MenuItemState[] {
  return data
    .filter((menu) => menu.type !== 3) // 过滤 type=3 的节点
    .map((menu) => {
      const children = menu.children?.length ? convertMenu(menu.children) : undefined
      const item: MenuItemState = {
        key: menu.id ?? 0,
        icon: () => h(IconFont, { type: menu.icon ?? 'icon-system' }),
        label: t('menu.' + menu.name) ?? '',
        path: menu.path ?? '',
        name: menu.name ?? '',
      }
      // 转换后若子项不为空则添加 children 属性
      if (children?.length) {
        item.children = children
      }
      return item
    })
}

const menuItems: ComputedRef<MenuItemState[]> = computed(() => {
  const menus: MenuTypeState[] = Array.isArray(user.menuList) ? user.menuList : []
  return convertMenu(menus)
})
const handleClick: MenuProps['onClick'] = (e) => {
  goPage(e.item.path)
  const item: MenuStackItemState = {
    key: e.key as number,
    name: e.item.name,
    path: e.item.path,
  }
  menu.setMenuStack(item)
}
</script>
<style lang="scss" scoped>
.menu-main {
  width: 272px;
  padding-top: 10px;
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  .menu-pannel {
    width: 100%;
    height: 100%;
    position: relative;
    overflow: hidden;
    .menu-logo {
      position: absolute;
      height: 52px;
      left: 15px;
      cursor: pointer;
      img {
        width: 42px;
        height: 42px;
      }
    }
    .menu-container {
      margin-top: 52px;
      height: 100%;
      overflow: auto;
    }
  }
}
</style>
