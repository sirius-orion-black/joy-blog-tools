<template>
  <div ref="chartRef" :style="{ width: propsWidth, height: propsHeight }" />
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { echarts } from '@/utils/echarts'
import type { EChartsOption } from '@/utils/echarts'

export interface SeriesItem {
  name: string
  data: number[]
}

const props = withDefaults(
  defineProps<{
    /** x 轴类目数据，如 ['1月', '2月', ...] */
    categories: string[]
    /** 系列数据，多个系列叠加显示 */
    seriesList: SeriesItem[]
    /** 图表标题 */
    title?: string
    /** x 轴名称 */
    xAxisName?: string
    /** y 轴名称 */
    yAxisName?: string
    /** 宽度 */
    propsWidth?: string
    /** 高度 */
    propsHeight?: string
    /** 折线是否平滑 */
    smooth?: boolean
    /** 是否显示面积 */
    showArea?: boolean
  }>(),
  {
    title: '',
    xAxisName: '',
    yAxisName: '',
    propsWidth: '100%',
    propsHeight: '400px',
    smooth: false,
    showArea: false,
  },
)

const chartRef = ref<HTMLDivElement>()
let instance: ReturnType<typeof echarts.init> | null = null

function buildOption(): EChartsOption {
  const option: EChartsOption = {}

  // 标题
  if (props.title) {
    option.title = {
      text: props.title,
      left: 'center',
      top: 10,
      textStyle: { fontSize: 16, fontWeight: 600 },
    }
  }

  // 提示框
  option.tooltip = {
    trigger: 'axis',
  }

  // 图例
  option.legend = {
    bottom: 0,
    itemWidth: 14,
    itemHeight: 10,
    icon: 'rect',
    textStyle: { fontSize: 13 },
  }

  // 网格
  option.grid = {
    left: '3%',
    right: '4%',
    bottom: '12%',
    containLabel: true,
  }

  // x 轴
  option.xAxis = {
    type: 'category',
    data: props.categories,
    boundaryGap: false,
    name: props.xAxisName || undefined,
  }

  // y 轴
  option.yAxis = {
    type: 'value',
    name: props.yAxisName || undefined,
  }

  // 系列 —— 关键：stack 设为同一值即为堆叠
  option.series = props.seriesList.map((item) => ({
    name: item.name,
    type: 'line',
    // stack: 'total', // 同一 stack 值 = 堆叠
    smooth: props.smooth,
    areaStyle: props.showArea ? {} : undefined,
    emphasis: {
      focus: 'series',
    },
    data: item.data,
  }))

  return option
}

function renderChart() {
  if (!instance) return
  instance.setOption(buildOption(), true)
}

onMounted(() => {
  if (!chartRef.value) return
  instance = echarts.init(chartRef.value)
  renderChart()

  const ro = new ResizeObserver(() => instance?.resize())
  ro.observe(chartRef.value)

  onUnmounted(() => {
    ro.disconnect()
    instance?.dispose()
    instance = null
  })
})

watch(() => [props.categories, props.seriesList], renderChart, { deep: true })
</script>
