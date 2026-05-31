<template>
  <div ref="chartRef" :style="{ width: propsWidth, height: propsHeight }" />
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { echarts } from '@/utils/echarts'
import type { EChartsOption, ECElementEvent } from '@/utils/echarts'

// ==================== Props 定义 ====================
const props = withDefaults(
  defineProps<{
    /** 数据：[{ name, value }] */
    data: { name: string; value: number }[]
    /** 主标题 */
    title?: string
    /** 中心文字（不传则不显示） */
    centerText?: string
    /** 中心副文字 */
    centerSubText?: string
    /** 宽度 */
    propsWidth?: string
    /** 高度 */
    propsHeight?: string
    /** 内圆半径 */
    innerRadius?: string
    /** 外圆半径 */
    outerRadius?: string
    /** 圆角大小 */
    borderRadius?: number
    /** 段间距（让圆角更明显） */
    padAngle?: number
  }>(),
  {
    title: '',
    centerText: '',
    centerSubText: '',
    propsWidth: '100%',
    propsHeight: '400px',
    innerRadius: '55%',
    outerRadius: '78%',
    borderRadius: 12,
    padAngle: 1,
  },
)

// ==================== 响应式状态 ====================
const chartRef = ref<HTMLDivElement>()
let instance: ReturnType<typeof echarts.init> | null = null

/** hover 时记录当前扇区名称 */
const hoverName = ref('')
/** hover 时记录当前扇区百分比（数值） */
const hoverPercent = ref<number | null>(null)

// ==================== 中间文字逻辑 ====================
/** 根据 hover 状态，返回实际显示的主文字 */
function displayText(): string {
  if (hoverName.value) return hoverName.value
  return props.centerText
}

/** 根据 hover 状态，返回实际显示的副文字 */
function displaySubText(): string {
  if (hoverPercent.value !== null) return hoverPercent.value + '%'
  return props.centerSubText
}

// ==================== 构建 ECharts 配置 ====================
/** 根据当前 props 和 hover 状态，动态生成 option */
function buildOption(): EChartsOption {
  const option: EChartsOption = {}

  // 主标题
  if (props.title) {
    option.title = {
      text: props.title,
      left: 'center',
      top: 10,
      textStyle: { fontSize: 16, fontWeight: 600 },
    }
  }

  // 颜色
  option.color = ['#a66cff', '#f9a11b', '#ffa1cf', '#8aabcc', '#9c9efe']

  // 提示框
  option.tooltip = {
    trigger: 'item',
    formatter: '{b}: {c} ({d}%)',
  }

  // 图例
  option.legend = {
    orient: 'horizontal',
    bottom: 10,
    left: 'center',
    itemWidth: 10,
    itemHeight: 10,
    itemGap: 16,
    icon: 'circle',
    textStyle: { fontSize: 13 },
  }

  // 环形图
  option.series = [
    {
      type: 'pie',
      radius: [props.innerRadius, props.outerRadius],
      center: ['50%', props.centerText ? '45%' : '50%'],
      avoidLabelOverlap: false,
      padAngle: props.padAngle,
      itemStyle: {
        borderRadius: props.borderRadius,
        borderColor: '#fff',
        borderWidth: 3,
      },
      label: {
        show: true,
        formatter: '{b}\n{d}%',
        fontSize: 12,
      },
      emphasis: {
        scaleSize: 6,
        label: {
          fontSize: 16,
          fontWeight: 'bold',
        },
      },
      data: props.data,
    },
  ]

  // 中心文字（通过 graphic 绘制，hover 时动态更新）
  if (props.centerText) {
    option.graphic = [
      {
        type: 'text',
        left: 'center',
        top: props.title ? '44%' : '42%',
        style: {
          text: displayText(),
          align: 'center',
          fill: '#333',
          fontSize: 22,
          fontWeight: 700,
        },
      },
      {
        type: 'text',
        left: 'center',
        top: props.title ? '51%' : '49%',
        style: {
          text: displaySubText(),
          align: 'center',
          fill: '#999',
          fontSize: 13,
        },
      },
    ]
  }

  return option
}

// ==================== 渲染与事件绑定 ====================
/** 重新渲染图表 */
function renderChart(): void {
  if (!instance) return
  instance.setOption(buildOption(), true)
}

/** 绑定鼠标 hover 事件 */
function bindEvents(): void {
  if (!instance) return

  // 鼠标移入扇区：更新中间文字为扇区名称 + 百分比
  instance.on('mouseover', (params: ECElementEvent) => {
    if (params.seriesType === 'pie') {
      hoverName.value = (params.name as string) ?? ''
      hoverPercent.value = params.percent ?? null
      renderChart()
    }
  })

  // 鼠标移出：恢复默认中间文字
  instance.on('mouseout', () => {
    hoverName.value = ''
    hoverPercent.value = null
    renderChart()
  })
}

// ==================== 生命周期 ====================
onMounted(() => {
  if (!chartRef.value) return
  instance = echarts.init(chartRef.value)
  bindEvents()
  renderChart()

  // 自适应容器大小
  const resizeObserver = new ResizeObserver(() => {
    instance?.resize()
  })
  resizeObserver.observe(chartRef.value)

  // 组件卸载时清理
  onUnmounted(() => {
    resizeObserver.disconnect()
    instance?.dispose()
    instance = null
  })
})

/** 监听 props 变化，重置 hover 状态并重新渲染 */
watch(
  () => [props.data, props.title, props.centerText, props.centerSubText],
  () => {
    hoverName.value = ''
    hoverPercent.value = null
    renderChart()
  },
  { deep: true },
)
</script>
