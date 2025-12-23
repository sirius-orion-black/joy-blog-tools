<script setup lang="ts">
import { RouterView } from 'vue-router'
import { userLoginStore } from './stores/login'

import HeaderView from './components/header/HeaderView.vue'
import MenuView from './components/menu/MenuView.vue'

const loginStore = userLoginStore()
</script>

<template>
  <div class="app-main">
    <div v-if="!!loginStore.token">
      <HeaderView />
      <MenuView />
    </div>
    <RouterView />
  </div>
</template>

<style scoped>
.app-main {
  width: 100vw;
  height: 100vh;
  position: relative;
}
</style>
