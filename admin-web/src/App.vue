<script setup lang="ts">
import { RouterView } from 'vue-router'
import { userLoginStore } from './stores/login'
import zhCN from 'ant-design-vue/es/locale/zh_CN'

import HeaderView from './components/header/HeaderView.vue'
import MenuView from './components/menu/MenuView.vue'

const loginStore = userLoginStore()
</script>

<template>
  <div class="app-main">
    <a-config-provider
      :theme="{
        token: {
          colorPrimary: '#a66cff',
        },
      }"
      :locale="zhCN"
    >
      <div v-if="!!loginStore.token">
        <HeaderView />
        <MenuView />
      </div>
      <RouterView />
    </a-config-provider>
  </div>
</template>

<style scoped>
.app-main {
  position: relative;
}
</style>
