<!-- NativeVideoPlayer.vue -->
<template>
  <div class="video-player-container" :style="{ width: width, height: height }">
    <video
      ref="videoRef"
      :src="src"
      :poster="posterUrl"
      :preload="preload"
      :controls="controls"
      :loop="loop"
      :muted="muted"
      :style="{ width: '100%', height: '100%', objectFit: 'cover' }"
      @loadstart="onLoadStart"
      @canplay="onCanPlay"
      @play="onPlay"
      @pause="onPause"
      @ended="onEnded"
      @error="onError"
      @waiting="onWaiting"
      @playing="onPlaying"
    >
      <track v-for="track in tracks" :key="track.src" :src="track.src" :kind="track.kind" :srclang="track.srclang" :label="track.label" />
      <p class="fallback-message">您的浏览器不支持视频播放。</p>
    </video>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

import type { PropsVideoState } from '@/types/video'

const props = withDefaults(defineProps<PropsVideoState>(), {
  width: '100%',
  height: '360px',
  preload: 'metadata',
  autoPlay: false,
  controls: true,
  loop: false,
  muted: false,
  tracks: () => [],
})

const emit = defineEmits<{
  loadstart: []
  canplay: []
  play: []
  pause: []
  ended: []
  error: [event: Event]
  waiting: []
  playing: []
}>()

const videoRef = ref<HTMLVideoElement>()

const onLoadStart = () => emit('loadstart')
const onCanPlay = () => emit('canplay')
const onPlay = () => emit('play')
const onPause = () => emit('pause')
const onEnded = () => emit('ended')
const onError = (event: Event) => emit('error', event)
const onWaiting = () => emit('waiting')
const onPlaying = () => emit('playing')

onMounted(() => {
  if (videoRef.value) {
    // 尝试自动播放（在支持的情况下）
    if (props.autoPlay) {
      videoRef.value.play().catch((error) => {
        console.warn('自动播放被阻止:', error)
        // 自动播放失败时，让用户手动点击播放
      })
    }
  }
})

// 导出方法供父组件调用
defineExpose({
  play: () => videoRef.value?.play(),
  pause: () => videoRef.value?.pause(),
  setCurrentTime: (time: number) => {
    if (videoRef.value) videoRef.value.currentTime = time
  },
  getCurrentTime: (): number => {
    return videoRef.value ? videoRef.value.currentTime : 0
  },
  getDuration: (): number => {
    return videoRef.value ? videoRef.value.duration : 0
  },
  setVolume: (volume: number) => {
    if (videoRef.value) videoRef.value.volume = volume
  },
  getVolume: (): number => {
    return videoRef.value ? videoRef.value.volume : 0
  },
})
</script>

<style scoped>
.video-player-container {
  position: relative;
  background-color: #000;
  overflow: hidden;
}

.fallback-message {
  color: #fff;
  text-align: center;
  padding: 20px;
  margin: 0;
  font-family: Arial, sans-serif;
}
</style>
