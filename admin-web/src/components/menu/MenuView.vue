<template>
  <div class="menu-main">
    <div class="menu-logo">
      <img src="@/assets/img/logo.png" />
      徐徐乐之
    </div>
    <a-menu
      v-model:selectedKeys="state.selectedKeys"
      style="width: 271px"
      mode="inline"
      :open-keys="state.openKeys"
      :items="items"
      @openChange="onOpenChange"
    ></a-menu>
  </div>
</template>
<script lang="ts" setup>
import { VueElement, h, reactive } from 'vue'
import { MailOutlined, AppstoreOutlined } from '@ant-design/icons-vue'
import type { ItemType } from 'ant-design-vue'

function getItem(label: VueElement | string, key: string, icon?: unknown, children?: ItemType[], type?: 'group'): ItemType {
  return {
    key,
    icon,
    children,
    label,
    type,
  } as ItemType
}

const items: ItemType[] = reactive([
  getItem('用户管理', 'sub1', () => h(MailOutlined), [
    getItem('用户角色管理', '1'),
    getItem('用户管理', '2'),
    getItem('用户日志', '3'),
    getItem('用户记录', '4'),
  ]),
  getItem('系统管理', 'sub2', () => h(AppstoreOutlined), [
    getItem('菜单管理', '5'),
    getItem('系统日志', '6'),
    getItem('系统设置', '7'),
    getItem('基础配置', '8'),
  ]),
])

const state = reactive({
  rootSubmenuKeys: ['sub1', 'sub2', 'sub4'],
  openKeys: ['sub1'],
  selectedKeys: [],
})
const onOpenChange = (openKeys: string[]) => {
  const latestOpenKey = openKeys.find((key) => state.openKeys.indexOf(key) === -1) as string | undefined
  if (state.rootSubmenuKeys.indexOf(latestOpenKey ?? '') === -1) {
    state.openKeys = openKeys
  } else {
    state.openKeys = latestOpenKey ? [latestOpenKey] : []
  }
}
</script>
<style lang="scss" scoped>
.menu-main {
  width: 272px;
  padding-top: 10px;
  border-right: 1px solid rgba(0, 0, 0, 0.2);
  color: #000000;
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  .menu-logo {
    height: 52px;
    margin-left: 15px;
    cursor: pointer;
    img {
      width: 42px;
      height: 42px;
    }
  }
  :deep(.ant-menu-submenu-selected > .ant-menu-submenu-title) {
    color: #a66cff !important;
  }
  :deep(.ant-menu-light .ant-menu-item-selected) {
    background-color: #a66cff !important;
    color: #ffffff !important;
  }
  :deep(.ant-menu-light .ant-menu-item:hover) {
    background-color: rgba(167, 108, 255, 0.5) !important;
  }
}
// :deep(.ant-menu-light) {
//   background-color: rgba(255, 255, 255, 0) !important;
// }

// :deep(.ant-menu-light .ant-menu-item) {
//   background-color: rgba(255, 255, 255, 0) !important;
//   color: #ffffff !important;
// }

// :deep(.ant-menu-light .ant-menu-submenu-title) {
//   color: #ffffff !important;
// }

// :deep(.ant-menu-light .ant-menu-submenu-selected > .ant-menu-submenu-title) {
//   background-color: rgba(255, 255, 255, 0.05) !important;
// }

// :deep(.ant-menu-light .ant-menu-submenu > .ant-menu) {
//   background-color: (255, 255, 255, 0.1) !important;
// }
</style>
