<script setup lang="ts">
import { RouterView } from 'vue-router'

import HeaderView from './components/header/HeaderView.vue'
import MenuView from './components/menu/MenuView.vue'
</script>

<template>
  <div class="app-main">
    <HeaderView />
    <MenuView />
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
