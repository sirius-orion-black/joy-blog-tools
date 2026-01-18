<template>
  <div class="header-main">
    <div class="header-body">
      <div class="header-search"></div>
      <div class="header-personal header-margin-right">
        <div class="header-margin-right">{{ login.user?.nickname }}</div>
        <a-badge :count="99" class="cursor-pointer header-margin-right">
          <IconFont type="icon-message" class="cursor-pointer header-margin-right font-size-24" />
        </a-badge>
        <IconFont type="icon-language" class="cursor-pointer header-margin-right font-size-24" @click="changeLanguage" />
        <IconFont type="icon-moon" class="cursor-pointer header-margin-right font-size-24" />
        <a-dropdown class="header-personal">
          <img class="header-avatar" src="https://joyimg.lexujia.com/joy/assets/logo.jpg" @click.prevent />
          <template #overlay>
            <a-menu>
              <a-menu-item>
                <a href="javascript:;">修改密码</a>
              </a-menu-item>
              <a-menu-item>
                <a href="javascript:;">个人资料</a>
              </a-menu-item>
              <a-menu-item @click="logout">
                <a href="javascript:;">登出</a>
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </div>
    </div>
    <div class="header-pannel">
      <div
        :class="`header-item base-a66cff ${menu.menuStack.current?.key! === item.key ? 'hover' : ''}`"
        v-for="item in menu.menuStack.items"
        :key="item.key"
      >
        <span class="header-item-title" @click="goPage(item)">{{ $t(`menu.${item.name}`) }}</span>
        <CloseOutlined v-if="menu.menuStack.current?.key !== item.key" />
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'

import { userLoginStore } from '@/stores/login'
import { menuStore } from '@/stores/menu'
import { userStore } from '@/stores/user'

import { CloseOutlined } from '@ant-design/icons-vue'

import type { MenuStackItemState } from '@/types/MenuType'

const router = useRouter()

const login = userLoginStore()
const menu = menuStore()
const user = userStore()
// 多语系处理
const { locale } = useI18n()
const changeLanguage = () => {
  locale.value = locale.value === 'zhCN' ? 'enUS' : 'zhCN' // 切换语言逻辑
}

onMounted(() => {
  setTimeout(() => {
    menu.getMenuStack()
    const path: string = router.currentRoute.value.path
    menu.getCurrentMenuStack(user.menuList ?? [], path)
  }, 200)
})
const goPage = (item: MenuStackItemState) => {
  menu.setMenuStack(item)
  router.push(item.path)
  console.log(item)
}
const logout = async () => {
  await login.signout()
  router.push('/login')
}
</script>

<style lang="scss" scoped>
.header-main {
  z-index: 9;
  height: 110px;
  position: fixed;
  right: 0;
  left: 272px;
  .header-body {
    display: flex;
    align-items: center;
    height: 62px;
    justify-content: space-between;
    flex-wrap: nowrap;
    .header-search {
      height: 52px;
      padding-top: 5px;
      margin-left: 15px;
    }
    .header-margin-right {
      margin-right: 15px;
    }
    .header-personal {
      display: flex;
      align-items: center;
      height: 62px;
      justify-content: flex-end;
      flex-wrap: nowrap;
      .header-avatar {
        cursor: pointer;
        width: 36px;
        height: 36px;
        border-radius: 50%;
      }
    }
  }
  .header-pannel {
    padding: 0 15px 0 15px;
    height: 48px;
    display: flex;
    font-size: 14px;
    .header-item {
      height: 30px;
      line-height: 30px;
      padding: 0 5px;
      margin: 9px 10px;
      cursor: pointer;
      border-radius: 5px;
      .header-item-title {
        padding-right: 6px;
      }
    }
  }
}
</style>
