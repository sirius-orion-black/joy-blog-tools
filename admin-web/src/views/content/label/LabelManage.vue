<template>
  <div class="base-main">
    <div class="base-search">
      <div class="base-search-label">
        <span class="base-search-title">{{ $t('columns.label_name') }}:</span>
        <a-input v-model:value="searchParam.name" style="width: 150px; height: 32px" />
      </div>
      <div class="base-search-label">
        <span class="base-search-title">{{ $t('columns.state') }}:</span>
        <a-select v-model:value="searchParam.state" style="width: 100px; height: 32px" :options="options"></a-select>
      </div>
      <div class="base-search-label">
        <a-button type="primary" @click="serchLabelList">{{ $t('menu.search') }}</a-button>
      </div>
    </div>
    <div class="base-container">
      <div>label</div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { reactive } from 'vue'
import { useI18n } from 'vue-i18n'

import type { LabelTypeSearchState } from '@/types/labelType'

import { contentLabelStore } from '@/stores/contentLabel'

const contenttLabel = contentLabelStore()

// 获取用户列表参数
const searchParam = reactive<LabelTypeSearchState>({
  name: '',
  state: null,
  page: 1,
  size: 10,
})
const { t } = useI18n()

const options = [
  {
    label: t('columns.normal'),
    value: 1,
  },
  {
    label: t('columns.disabled'),
    value: 2,
  },
]

const serchLabelList = () => {
  contenttLabel.getList(searchParam)
}
</script>
