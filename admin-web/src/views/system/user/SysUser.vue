<template>
  <div class="base-main">
    <div class="base-search">
      <div class="base-search-label">
        <span class="base-search-title">用户名:</span>
        <a-input v-model:value="value" placeholder="Basic usage" style="width: 200px; height: 32px" />
      </div>
      <div class="base-search-label">
        <span class="base-search-title">状态:</span>
        <a-select v-model:value="value1" :size="size" style="width: 200px; height: 32px" :options="options"></a-select>
      </div>
      <div class="base-search-label">
        <a-button type="primary">查询</a-button>
      </div>
      <div class="base-search-label">
        <a-button>重置</a-button>
      </div>
    </div>
    <div class="base-container">
      <a-table :columns="columns" :data-source="data">
        <template #bodyCell="{ column, text }">
          <template v-if="column.dataIndex === 'name'">
            <a>{{ text }}</a>
          </template>
        </template>
      </a-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import type { SelectProps } from 'ant-design-vue'
const size = ref<SelectProps['size']>('middle')
const value = ref<string>('')
const value1 = ref('a1')
const options = [...Array(25)].map((_, i) => ({ value: (i + 10).toString(36) + (i + 1) }))

const columns = [
  {
    title: 'Name',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: 'Age',
    dataIndex: 'age',
    key: 'age',
    width: 80,
  },
  {
    title: 'Address',
    dataIndex: 'address',
    key: 'address 1',
    ellipsis: true,
  },
  {
    title: 'Long Column Long Column Long Column',
    dataIndex: 'address',
    key: 'address 2',
    ellipsis: true,
  },
  {
    title: 'Long Column Long Column',
    dataIndex: 'address',
    key: 'address 3',
    ellipsis: true,
  },
  {
    title: 'Long Column',
    dataIndex: 'address',
    key: 'address 4',
    ellipsis: true,
  },
]
const data = [
  {
    key: '1',
    name: 'John Brown',
    age: 32,
    address: 'New York No. 1 Lake Park, New York No. 1 Lake Park',
    tags: ['nice', 'developer'],
  },
  {
    key: '2',
    name: 'Jim Green',
    age: 42,
    address: 'London No. 2 Lake Park, London No. 2 Lake Park',
    tags: ['loser'],
  },
  {
    key: '3',
    name: 'Joe Black',
    age: 32,
    address: 'Sidney No. 1 Lake Park, Sidney No. 1 Lake Park',
    tags: ['cool', 'teacher'],
  },
]
</script>
<style lang="scss" scoped></style>
