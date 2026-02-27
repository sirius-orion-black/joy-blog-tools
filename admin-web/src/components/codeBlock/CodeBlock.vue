<template>
  <div class="code-block-container">
    <div ref="contentRef" v-html="content" class="code-block-content"></div>
  </div>
</template>
<script lang="ts" setup>
import { ref, onMounted, onUpdated, watch } from 'vue'
import Prism from 'prismjs'

// 仅解构出需要的content属性，未使用的props会被自动忽略
const { content } = defineProps({
  content: { type: String, default: '' },
})

// 存储解析后的DOM元素
const contentRef = ref<HTMLDivElement | null>(null)

onMounted(() => {
  highlightCode()
})

onUpdated(() => {
  highlightCode()
})

// 高亮所有代码块
const highlightCode = () => {
  if (!contentRef.value) return
  // 选择所有代码块元素
  const codeBlocks = contentRef.value.querySelectorAll('pre code')
  if (codeBlocks.length === 0) return

  // 对每个代码块进行高亮处理
  codeBlocks.forEach((block) => {
    // 确保元素是Prism.js支持的类型
    Prism.highlightElement(block)
    // if (block.classList.contains('language-')) {
    // }
  })
}
// 监听props.content变化，内容更新时触发高亮
watch(
  () => content,
  () => {
    // 延迟100ms执行，确保DOM完全渲染完成
    setTimeout(highlightCode, 100)
  },
)
</script>
<style lang="scss" scoped>
.code-block-container {
  max-width: 100%;
  overflow: hidden;
}

.code-block-content {
  font-size: 14px;
  line-height: 1.6;
  padding: 1rem;
  background-color: #f7f8fa;
  border-radius: 8px;
  border: 1px solid #e2e2e3;
}

/* 代码块特殊样式 */
pre {
  position: relative;
  background: #f7f8fa;
  border-radius: 8px;
  overflow-x: auto;
  border: 1px solid #e2e2e3;
  padding: 0;
}

.language-javascript,
.language-css,
.language-html {
  padding: 0.5rem;
}
</style>
