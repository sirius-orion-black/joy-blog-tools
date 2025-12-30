<template>
  <div>
    <a-modal
      width="368px"
      :centered="true"
      v-model:open="loginStore.showCaptcha"
      :title="$t('base.drag_slider_complete_puzzle')"
      :footer="null"
      @cancel="resetSlider"
    >
      <div class="captcha-main" v-if="loginStore.isCaptchaState">
        <div class="captcha-img">
          <img
            :style="{
              width: loginStore.captcha!.canvasWidth + 'px',
              height: loginStore.captcha!.canvasHeight + 'px',
            }"
            class="captcha-bg"
            :src="loginStore.captcha!.canvasSrc"
          />
          <img
            :style="{
              width: loginStore.captcha!.blockWidth + 'px',
              height: loginStore.captcha!.blockHeight + 'px',
              left: captchaState.moveX + 'px',
              top: loginStore.captcha!.blockY + 'px',
            }"
            class="captcha-block"
            :src="loginStore.captcha!.blockSrc"
          />
        </div>
        <div class="captcha-slider">
          <div :style="{ width: captchaState.posationX + 'px' }" class="slider-box"></div>
          <div :style="{ left: captchaState.posationX + 'px' }" @mousedown="startDrag" @touchstart="startDrag" class="slider-button">
            <RightOutlined class="site-form-item-icon" :style="{ fontSize: '18px', color: '#aba29b' }" />
          </div>
          <div class="slider-write">{{ $t('base.slide_to_right') }}</div>
        </div>
      </div>
      <div class="captcha-spin" v-else>
        <a-spin />
      </div>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { userLoginStore } from '@/stores/login'
import { RightOutlined } from '@ant-design/icons-vue'

import type { CaptchaState } from '@/types/LoginType'

//login store
const loginStore = userLoginStore()

//设置captcha参数defualt值
const captchaState = reactive<CaptchaState>({
  moveX: 0,
  startX: 0,
  isMove: false,
  posationX: 0,
  maxPositionX: 255,
})

// 重置滑块位置
const resetSlider = () => {
  captchaState.moveX = 0
  captchaState.posationX = 0
  captchaState.isMove = false
  loginStore.setIsCaptchaState(false)
}

// 提交数据
const emit = defineEmits<{
  'update:move': [value: number]
}>()
const submitMove = () => {
  emit('update:move', captchaState.moveX)
  setTimeout(() => {
    resetSlider()
  }, 200)
}

// 开始拖拽
const startDrag = (e: MouseEvent | TouchEvent) => {
  if (captchaState.isMove) return

  captchaState.isMove = true

  const clientX = 'touches' in e && e.touches.length > 0 ? e.touches[0]!.clientX : (e as MouseEvent).clientX
  captchaState.startX = clientX
  captchaState.maxPositionX = parseInt(loginStore.captcha!.canvasWidth + '') - parseInt(loginStore.captcha!.blockWidth + '')

  document.addEventListener('mousemove', onDrag)
  document.addEventListener('touchmove', onDrag, { passive: false })
  document.addEventListener('mouseup', stopDrag)
  document.addEventListener('touchend', stopDrag)
}
// 拖拽中
const onDrag = (e: MouseEvent | TouchEvent) => {
  if (!captchaState.isMove) return

  e.preventDefault()
  const clientX = 'touches' in e && e.touches.length > 0 ? e.touches[0]!.clientX : (e as MouseEvent).clientX
  const diffX = clientX - captchaState.startX
  console.log(3123123123, diffX)

  if (diffX >= 0 && diffX <= captchaState.maxPositionX) {
    captchaState.moveX = diffX
    const multiple = (captchaState.maxPositionX + parseInt(loginStore.captcha!.blockWidth + '') - 40) / captchaState.maxPositionX
    captchaState.posationX = diffX * multiple
  }
}

// 停止拖拽
const stopDrag = () => {
  if (!captchaState.isMove) return
  submitMove()
  captchaState.isMove = false
  document.removeEventListener('mousemove', onDrag)
  document.removeEventListener('touchmove', onDrag)
  document.removeEventListener('mouseup', stopDrag)
  document.removeEventListener('touchend', stopDrag)
}
</script>

<style lang="scss" scoped>
.captcha-main {
  padding-top: 10px;
  .captcha-img {
    position: relative;
    width: 320px;
    height: 160px;
    .captcha-bg {
      width: 320px;
      height: 160px;
    }
    .captcha-block {
      position: absolute;
    }
  }
  .captcha-slider {
    position: relative;
    text-align: center;
    width: 100%;
    height: 40px;
    line-height: 40px;
    margin-top: 15px;
    background: #f7f5f4;
    color: #000000;
    border: 1px solid #ece8e6;
    border-radius: 5px;
    overflow: hidden;
    .slider-box {
      z-index: 5;
      background-color: #efe8e3;
      position: absolute;
      top: 0;
      left: 0;
      bottom: 0;
    }
    .slider-button {
      z-index: 99;
      position: absolute;
      top: 0;
      width: 40px;
      height: 38px;
      line-height: 38px;
      background: #fff;
      box-shadow: 0 0 3px #0000004d;
      cursor: pointer;
      border-top-right-radius: 5px;
      border-bottom-right-radius: 5px;
    }
    .slider-write {
      width: 100%;
      line-height: 40px;
      z-index: 9;
      position: relative;
      text-align: center;
    }
  }
}
.captcha-spin {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 160px;
}
</style>
