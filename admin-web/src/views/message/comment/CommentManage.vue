<template>
  <div class="base-main">
    <div class="base-search">
      <div class="base-search-label">
        <span class="base-search-title">{{ $t('columns.state') }}:</span>
        <a-select v-model:value="searchParam.state" class="base-search-height" :options="options"></a-select>
      </div>

      <div class="base-search-label">
        <a-button type="primary" @click="serchLabelList">{{ $t('menu.search') }}</a-button>
      </div>
      <div class="base-search-label">
        <a-button @click="resetSearch">{{ $t('menu.reset') }}</a-button>
      </div>
    </div>
    <div class="base-container">
      <a-table :columns="columns" :data-source="comment.list?.records ?? []" rowKey="id" :pagination="pagination" @change="handleTableChange">
        <template #bodyCell="{ column, text }">
          <template v-if="column.key === 'state'">{{ getCommentByValue(text) }}</template>
          <template v-else-if="column.key === 'operation'">
            <a v-if="text.state !== 1" class="f9a11b" @click="reviewComment(text, 1)" :title="$t('columns.normal')">
              <IconFont type="icon-review" />
            </a>
            <a v-if="text.state !== 3" class="c9c9efe" @click="reviewComment(text, 3)" :title="$t('columns.blocked')">
              <IconFont type="icon-blocked" />
            </a>
            <a v-if="text.state !== 4" class="ffa1cf" @click="reviewComment(text, 4)" :title="$t('columns.violation')">
              <IconFont type="icon-violation" />
            </a>
          </template>
        </template>
      </a-table>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { computed, onMounted, ref } from 'vue'
import { useI18n } from 'vue-i18n'

import { messageCommentStore } from '@/stores/messageComment'

import type { CommentSearchState, CommentState } from '@/types/commentType'
import type { PageTableState } from '@/types/resultType'

const { t } = useI18n()
const comment = messageCommentStore()

const options = computed(() => [
  {
    label: t('columns.normal'),
    value: 1,
  },
  {
    label: t('columns.review'),
    value: 2,
  },
  {
    label: t('columns.blocked'),
    value: 3,
  },
  {
    label: t('columns.violation'),
    value: 5,
  },
])
onMounted(() => {
  serchLabelList()
})
//根据 value 获取对应的 label
const getCommentByValue = (value: number): string | undefined => {
  const item = options.value.find((item) => item.value === value)
  return item?.label
}
// 获取用户列表参数
const searchParam = ref<CommentSearchState>({
  state: 2,
  page: 1,
  size: 10,
})

const serchLabelList = () => {
  comment.getList(searchParam.value)
}

const resetSearch = () => {
  searchParam.value = { state: 2, page: 1, size: 10 }
  comment.getList(searchParam.value)
}

const reviewComment = (text: CommentState, state: number) => {
  comment.review({ id: text.id, state } as CommentState)
}

//表格列表
const columns = computed(() => [
  {
    title: t('columns.create_time'),
    dataIndex: 'createTime',
    key: 'createTime',
  },
  {
    title: t('columns.state'),
    dataIndex: 'state',
    key: 'state',
  },
  {
    title: t('columns.content'),
    dataIndex: 'content',
    key: 'content',
  },
  {
    title: t('columns.location'),
    dataIndex: 'location',
    key: 'location',
  },
  {
    title: t('columns.action'),
    key: 'operation',
    fixed: 'right',
    width: 105,
  },
])

// 表格分页触发事件
const handleTableChange = (pagination: PageTableState) => {
  searchParam.value.page = pagination.current || 1
  comment.getList(searchParam.value)
}

const pagination = computed(() => {
  return {
    current: comment.list?.current,
    total: comment.list?.total,
    pageSize: comment.list?.size,
  }
})
</script>
<style lang="scss" scoped></style>
