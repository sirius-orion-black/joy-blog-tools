<template>
  <svg t="1766473965847" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1428" width="200" height="200">
    <path
      d="M768 832h-128a128 128 0 1 1-256 0H160v-64h64v-288a288 288 0 0 1 576 0v288h64v64h-96z m-192 0h-128a64 64 0 1 0 128 0z m160-64v-288a224 224 0 0 0-448 0v288h448zM416 64h192v64h-192V64z"
      fill="#595959"
      p-id="1429"
    ></path>
  </svg>
</template>

<script setup lang="ts">
defineProps({
  color: {
    type: String,
    default: 'currentColor',
  },
  width: {
    type: [Number, String],
    default: 24,
  },
  height: {
    type: [Number, String],
    default: 24,
  },
})
</script>

<style scoped>
svg {
  fill: v-bind(color);
  width: v-bind(width);
  height: v-bind(height);
}
</style>
