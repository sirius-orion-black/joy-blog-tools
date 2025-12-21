<template>
  <svg
    t="1766236932564"
    class="icon"
    viewBox="0 0 1024 1024"
    version="1.1"
    xmlns="http://www.w3.org/2000/svg"
    p-id="1425"
    width="200"
    height="200"
  >
    <path
      d="M832 192v640H192V192h640m0-64H192a64 64 0 0 0-64 64v640a64 64 0 0 0 64 64h640a64 64 0 0 0 64-64V192a64 64 0 0 0-64-64z"
      fill="#231815"
      p-id="1426"
    ></path>
    <path
      d="M625.14 489.3c-0.14-0.13-0.32-0.16-0.46-0.3L444.12 308.43a32 32 0 0 0-45.26 0 32 32 0 0 0 0 45.25L557.18 512 398.86 670.32a32 32 0 0 0 0 45.25 32 32 0 0 0 45.26 0L624.68 535c0.14-0.13 0.32-0.18 0.46-0.3a32.22 32.22 0 0 0 0-45.41z"
      fill="#231815"
      p-id="1427"
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
