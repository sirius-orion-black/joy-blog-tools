import * as echarts from 'echarts/core'

// 图表类型
import { PieChart, LineChart } from 'echarts/charts'

// 组件
import { TitleComponent, TooltipComponent, LegendComponent, GridComponent, GraphicComponent } from 'echarts/components'

import { CanvasRenderer } from 'echarts/renderers'

echarts.use([PieChart, LineChart, TitleComponent, TooltipComponent, LegendComponent, GridComponent, GraphicComponent, CanvasRenderer])

export type { EChartsOption, ECElementEvent } from 'echarts'
export { echarts }
