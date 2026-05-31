<template>
  <div class="base-main">
    <a-flex class="dash-blog" gap="large">
      <a-flex class="blog-info base-bg-shadow" align="center" justify="space-between" v-for="(value, key, index) in sumCounts" :key="key + index">
        <div class="blog-layout">
          <div class="title">{{ $t('dash.' + key) }}</div>
          <div class="count ffa1cf">{{ value }}</div>
        </div>
        <div class="blog-icon base-bg-a66"><IconFont :type="dashIcon[key] ?? 'icon-review'" /></div>
      </a-flex>
    </a-flex>

    <a-flex class="dash-recently" gap="large">
      <div class="recently-main recently-vist base-bg-shadow">
        <span class="title">最近一周访问量</span>
        <div>
          <StackedLineChart :categories="dateList" :series-list="lineSeries" smooth />
        </div>
      </div>
      <div class="recently-main recently-classify base-bg-shadow">
        <span class="title">分类统计</span>
        <div class="classify-articles">
          <RoundedRingChart props-height="350px" center-text="技术文章" :data="articles" />
        </div>
      </div>
    </a-flex>
  </div>
</template>

<script setup lang="ts">
import { onMounted, computed, ref } from 'vue'
import { useI18n } from 'vue-i18n'

import { dashBoardStore } from '@/stores/dashBoard'

import type { SeriesItem } from '@/components/chart/StackedLineChart.vue'
import type { ClassifyArticlesChatState } from '@/types/dashType'

import RoundedRingChart from '@/components/chart/RoundedRingChart.vue'
import StackedLineChart from '@/components/chart/StackedLineChart.vue'

const { t } = useI18n()
const dashBoard = dashBoardStore()

onMounted(() => {
  dashBoard.getDashBlog()
})

const dashIcon = ref<Record<string, string>>({
  moments: 'icon-moments-a',
  messages: 'icon-messages-d',
  articles: 'icon-article-b',
  users: 'icon-user-b',
  vists: 'icon-visit',
})

const sumCounts = computed(() => dashBoard.dashBlog?.sumCounts || {})

const articles = computed<ClassifyArticlesChatState[]>(
  () =>
    dashBoard.dashBlog?.classifyArticles.map((item) => ({
      value: item.articleCount,
      name: item.classifyName,
    })) ?? [],
)

const dateList = computed(() => {
  const visits = dashBoard.dashBlog?.visits || []
  const dateList: string[] = visits.map((item) => item.date)
  return dateList
})

const lineSeries = computed<SeriesItem[]>(() => {
  const visits = dashBoard.dashBlog?.visits || []
  const metrics = ['pv', 'uv', 'ipCount'] as const
  const series =
    metrics.map((metric) => ({
      name: t('dash.' + metric),
      data: visits.map((item) => item[metric]),
    })) ?? []
  return series
})
</script>
<style lang="scss" scoped>
.dash-blog {
  .blog-info {
    flex: 1;
    height: 112px;
    padding: 10px 15px;
    .blog-layout {
      .title {
        font-size: 14px;
        line-height: 2em;
      }
      .count {
        font-size: 20px;
        font-weight: bold;
        line-height: 2em;
      }
    }
    .blog-icon {
      font-size: 26px;
      color: #fff;
      width: 40px;
      height: 40px;
      line-height: 40px;
      text-align: center;
      border-radius: 100%;
    }
  }
}
.dash-recently {
  margin-top: 50px;
  .recently-main {
    padding: 20px;
    .title {
      font-size: 14px;
      font-weight: 700;
    }
  }
  .recently-vist {
    flex: 3;
  }
  .recently-classify {
    flex: 2;
  }
}
</style>
