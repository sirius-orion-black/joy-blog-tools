<template>
  <div class="menu-main">
    <div class="menu-pannel">
      <div class="menu-logo" @click="goPage('/')">
        <img src="@/assets/img/logo.png" />
        徐徐乐之
      </div>
      <div class="menu-container">
        <a-menu
          v-model:selectedKeys="state.selectedKeys"
          style="width: 271px"
          mode="inline"
          :open-keys="state.openKeys"
          :items="items"
          @openChange="onOpenChange"
        ></a-menu>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { VueElement, h, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { MailOutlined } from '@ant-design/icons-vue'
import type { ItemType } from 'ant-design-vue'

const router = useRouter()

function goPage(path: string) {
  router.push(path)
}

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
  getItem('系统管理', 'sub1', () => h(MailOutlined), [
    // getItem('系统用户', '1', () => h(IconFont, { type: 'icon-user-plus' })),
    getItem('系统菜单', '2'),
    getItem('系统日志', '3'),
    getItem('网站配置', '4'),
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
