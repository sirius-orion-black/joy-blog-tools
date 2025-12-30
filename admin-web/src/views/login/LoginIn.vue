<template>
  <div class="login-main">
    <div class="canvas-body">
      <canvas ref="flowersRef" class="canvas-flowers" :width="screenWidth" :height="screenHeight"></canvas>
    </div>
    <div class="login-body">
      <SignIn v-if="page == 'sigin'" :page="page" @update:page="handlePageUpdate" />
      <VerifyEmail v-else :page="page" @update:page="handlePageUpdate" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue'

import type { FlowerState } from '@/types/LoginType'

import SignIn from './SignIn.vue'
import VerifyEmail from './VerifyEmail.vue'

// 忘记密码和登录切换email sigin
const page = ref('sigin')
const handlePageUpdate = (pageId: string) => {
  page.value = pageId
}
// 梅花类型定义

const flowersRef = ref<HTMLCanvasElement | null>(null)
const flowers = ref<FlowerState[]>([])
const screenWidth = ref(window.innerWidth)
const screenHeight = ref(window.innerHeight)
const speed = ref(3)

// 梅花的颜色
const flowerColors = [
  'rgba(255, 192, 203, 0.8)', // 宫粉梅‌，‌桃红梅，‌台阁梅
  'rgba(255, 0, 0, 0.8)', // ‌朱砂梅‌
  'rgba(255, 255, 255, 0.8)', // ‌白梅
  'rgba(0, 128, 0, 0.8)', // ‌绿萼梅‌
  'rgba(255, 255, 0, 0.8)', // ‌黄香梅‌
  'rgba(128, 0, 128, 0.8)', // ‌紫玉兰梅‌
  'rgba(128, 0, 0, 0.8)', // ‌乌羽玉‌
  'rgba(255, 215, 0, 0.8)', // ‌洒金梅‌
  'rgba(0, 0, 0, 0.8)', // ‌墨梅‌
  'rgba(255, 192, 203, 0.8)', // 宫粉梅‌
  'rgba(255, 192, 203, 0.8)', // 宫粉梅‌
]

// 初始化花,因为冬天，暂选梅花，喜欢梅花寓意
const initFlower = () => {
  flowers.value = []
  for (let i = 0; i < 100; i++) {
    //设定梅花为100朵,可改为用参数控制
    flowers.value.push({
      x: Math.random() * screenWidth.value,
      y: Math.random() * screenHeight.value,
      size: Math.random() * 2 + 4,
      speed: Math.random() * 1.2 + 1,
      rotation: Math.random() * Math.PI * 2,
      rotationSpeed: (Math.random() - 0.5) * 0.05,
      swing: Math.random() * Math.PI * 2,
      swingSpeed: Math.random() * 0.02 + 0.01,
      swingRange: Math.random() * 2 + 1,
      color: flowerColors[Math.floor(Math.random() * flowerColors.length)] + '',
      opacity: Math.random() * 0.5 + 0.3,
    })
  }
}

// 绘制梅花花瓣
const drawFlower = (ctx: CanvasRenderingContext2D, flower: FlowerState) => {
  ctx.save()
  ctx.translate(flower.x, flower.y)
  ctx.rotate(flower.rotation)

  // 绘制五瓣梅花
  ctx.beginPath()
  for (let i = 0; i < 5; i++) {
    const angle = (i * Math.PI * 2) / 5
    const petalLength = flower.size * 1.5

    // 花瓣形状（心形近似）
    ctx.moveTo(0, 0)
    ctx.bezierCurveTo(
      Math.cos(angle) * flower.size,
      Math.sin(angle) * flower.size,
      Math.cos(angle + Math.PI / 5) * petalLength,
      Math.sin(angle + Math.PI / 5) * petalLength,
      Math.cos(angle + Math.PI / 2.5) * flower.size * 0.8,
      Math.sin(angle + Math.PI / 2.5) * flower.size * 0.8,
    )
  }
  ctx.closePath()

  // 填充颜色
  ctx.fillStyle = flower.color
  ctx.globalAlpha = flower.opacity
  ctx.fill()

  // 绘制花蕊
  // ‌粉红色‌：rgba(255, 192, 203, 1)
  // ‌桃红色‌：rgba(255, 105, 180, 1)
  // ‌深玫瑰红‌：rgba(220, 20, 60, 1)
  // ‌淡乳黄色‌：rgba(255, 250, 240, 1)
  // ‌极淡黄色‌：rgba(255, 255, 240, 1)
  ctx.beginPath()
  ctx.arc(0, 0, flower.size * 0.3, 0, Math.PI * 2)
  ctx.fillStyle = 'rgba(255, 192, 203, 1)' //花蕊颜色可以根据不同花色来调整，现在懒得搞，以后有时间再说
  ctx.fill()

  ctx.restore()
}

// 动画循环
let animatioLoop: number

const animate = () => {
  if (!flowersRef.value) return

  const ctx = flowersRef.value.getContext('2d')
  if (!ctx) return

  // 清除画布
  ctx.clearRect(0, 0, screenWidth.value, screenHeight.value)

  // 绘制背景
  const gradient = ctx.createLinearGradient(0, 0, screenWidth.value, screenHeight.value)

  // gradient.addColorStop(0, '#ffffff')
  // gradient.addColorStop(1, '#000000')
  ctx.fillStyle = gradient
  ctx.fillRect(0, 0, screenWidth.value, screenHeight.value)

  // 更新和绘制梅花
  flowers.value.forEach((flower) => {
    // 更新位置
    flower.y += flower.speed * (speed.value / 3)
    flower.x += Math.sin(flower.swing) * flower.swingRange
    flower.swing += flower.swingSpeed
    flower.rotation += flower.rotationSpeed

    // 如果梅花飘出屏幕底部，重新从顶部开始
    if (flower.y > screenHeight.value) {
      flower.y = -flower.size
      flower.x = Math.random() * screenWidth.value
    }

    // 如果梅花飘出屏幕左右边界，调整位置
    if (flower.x < -flower.size) flower.x = screenWidth.value + flower.size
    if (flower.x > screenWidth.value + flower.size) flower.x = -flower.size

    drawFlower(ctx, flower)
  })

  animatioLoop = requestAnimationFrame(animate)
}

// 响应式监听
watch(speed, () => {
  flowers.value.forEach((flower) => {
    flower.speed = Math.random() * 2 + 1
  })
})

// 生命周期
onMounted(() => {
  initFlower()
  animate()

  // 窗口大小变化时调整canvas大小
  const handleResize = () => {
    screenWidth.value = window.innerWidth
    screenHeight.value = window.innerHeight
    initFlower()
  }

  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  cancelAnimationFrame(animatioLoop)
})
</script>
<style lang="scss" scoped>
.login-main {
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  .canvas-body {
    z-index: 1;
    pointer-events: none;
    position: fixed;
    top: 0;
    left: 0;
    bottom: 0;
    right: 0;
    .canvas-flowers {
      width: 100%;
      height: 100%;
    }
  }
  .login-body {
    width: 100%;
    height: 100%;
    z-index: 9;
    position: relative;
    &::after {
      display: block;
      content: ' ';
      background-image: url('@/assets/img/login/login_bg.jpg');
      z-index: 1;
      background-size: 100% 100%;
      background-repeat: no-repeat;
      opacity: 0.2;
      position: absolute;
      left: 0;
      top: 0;
      right: 0;
      bottom: 0;
    }
  }
}

// #544f4b
</style>
