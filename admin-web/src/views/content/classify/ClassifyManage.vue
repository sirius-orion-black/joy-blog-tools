<template>
  <div class="base-main">
    <div class="base-search">
      <div class="base-search-label">
        <span class="base-search-title">{{ $t('columns.label_name') }}:</span>
        <a-input v-model:value="searchParam.name" class="base-search-height" />
      </div>
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
      <div class="base-operate">
        <div class="base-operate-label">
          <a-button @click="changeDrawer('add')"><IconFont type="icon-add" />{{ $t('menu.add') }}</a-button>
        </div>
        <div class="base-operate-label">
          <a-button @click="batchDeletion"><IconFont type="icon-delete" />{{ $t('menu.batch_deletion') }}</a-button>
        </div>
      </div>
      <a-table
        :columns="columns"
        :data-source="contentType.typeList?.records ?? []"
        :row-selection="{ selectedRowKeys: tableState.selectedRowKeys, onChange: onSelectChange }"
        rowKey="id"
        :pagination="pagination"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, text }">
          <template v-if="column.key === 'state'">
            {{ $t(text === 1 ? 'columns.normal' : 'columns.disabled') }}
          </template>
          <template v-else-if="column.key === 'type'">
            {{ $t(text === 1 ? 'columns.blog' : 'columns.moments') }}
          </template>
          <template v-else-if="column.key === 'operation'">
            <a class="ffa1cf" @click="changeDrawer('edit', text)"><IconFont type="icon-edit" /></a>
            <a class="c9c9efe" @click="delLabel(text)"><IconFont type="icon-delete" /></a>
          </template>
        </template>
      </a-table>
    </div>

    <a-drawer :open="showDrawer" :title="$t(drawerTitle)" @close="onClose" :closable="false">
      <a-form :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item :label="$t('columns.classify_name')" :rules="[{ required: true, message: '' }]">
          <a-input v-model:value="typeInfo.name" :placeholder="$t('columns.classify_name')" />
        </a-form-item>

        <a-form-item :label="$t('columns.state')" :rules="[{ required: true, message: '' }]">
          <a-radio-group v-model:value="typeInfo.state">
            <a-radio-button :value="1">{{ $t('columns.normal') }}</a-radio-button>
            <a-radio-button :value="2">{{ $t('columns.disabled') }}</a-radio-button>
          </a-radio-group>
        </a-form-item>

        <a-form-item :label="$t('columns.description')">
          <a-input v-model:value="typeInfo.description" :placeholder="$t('columns.description')" />
        </a-form-item>
      </a-form>

      <template #extra>
        <a-space>
          <a-button type="primary" @click="onSubmit">{{ $t('menu.submit') }}</a-button>
        </a-space>
      </template>
    </a-drawer>
  </div>
</template>
<script lang="ts" setup>
import { reactive, computed, ref, onMounted, nextTick } from 'vue'
import { useI18n } from 'vue-i18n'
import { message, Modal } from 'ant-design-vue'

import type { Ref } from 'vue'
import type { LabelTypeSearchState, LabelTypeState } from '@/types/labelType'
import type { PageTableState } from '@/types/resultType'

import { contentTypeStore } from '@/stores/contentClassify'

const contentType = contentTypeStore()
const showDrawer = ref<boolean>(false)
const typeInfo = ref<LabelTypeState>({
  id: null,
  name: '',
  state: 1,
  description: '',
})
const drawerTitle = ref<string>('drawer.new_type')

// 获取列表参数
const searchParam = ref<LabelTypeSearchState>({
  name: '',
  state: null,
  page: 1,
  size: 10,
})
onMounted(() => {
  contentType.getList(searchParam.value)
})
const resetSearch = () => {
  searchParam.value = { name: '', state: null, page: 1, size: 10 }
  contentType.getList(searchParam.value)
}
const { t } = useI18n()

const options = computed(() => [
  {
    label: t('columns.normal'),
    value: 1,
  },
  {
    label: t('columns.disabled'),
    value: 2,
  },
])

const serchLabelList = () => {
  contentType.getList(searchParam.value)
}

const changeDrawer = (type: string, item?: LabelTypeState) => {
  if (type === 'edit' && item) {
    const { createTime: _, ...rest } = item
    typeInfo.value = rest
    drawerTitle.value = 'drawer.edit_type'
  } else {
    typeInfo.value = { id: null, name: '', state: 1, description: '' }
    drawerTitle.value = 'drawer.new_type'
  }
  showDrawer.value = true
}

const onClose = () => {
  showDrawer.value = false
}
const onSubmit = () => {
  if (!typeInfo.value.name) {
    message.error(t('drawer.type_name_empty'))
    return
  }
  if (!typeInfo.value.id) contentType.addClassify(typeInfo.value)
  else contentType.editClassify(typeInfo.value)
  showDrawer.value = false
}

const columns = computed(() => [
  {
    title: t('columns.label_name'),
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: t('columns.state'),
    dataIndex: 'state',
    key: 'state',
  },
  {
    title: t('columns.description'),
    dataIndex: 'description',
    key: 'description',
  },
  {
    title: t('columns.action'),
    key: 'operation',
    fixed: 'right',
    width: 105,
  },
])
// 多选
const selectLabbel: Ref<LabelTypeState[]> = ref([])
// 表格行选择配置
type tableKey = string | number
const tableState = reactive<{
  selectedRowKeys: tableKey[]
}>({
  selectedRowKeys: [],
})
const onSelectChange = (selectedRowKeys: tableKey[], selectedRows: LabelTypeState[]) => {
  selectLabbel.value = selectedRows
  tableState.selectedRowKeys = selectedRowKeys
}
// 分页
const pagination = computed(() => {
  return {
    current: contentType.typeList?.current,
    total: contentType.typeList?.total,
    pageSize: contentType.typeList?.size,
  }
})
// 表格分页触发事件
const handleTableChange = (pagination: PageTableState) => {
  searchParam.value.page = pagination.current || 1
  contentType.getList(searchParam.value)
}
// 删除标签
const deleteLabel = (param: number[]) => {
  Modal.confirm({
    title: t('base.sure_deleted_it'),
    onOk() {
      contentType.delClassify(param)
      nextTick(() => {
        selectLabbel.value = [] // 确保在DOM更新后执行
        tableState.selectedRowKeys = []
      })
    },
  })
}
const delLabel = (text: LabelTypeState) => {
  const param: number[] = []
  param.push(text?.id || 0)
  deleteLabel(param)
}
const batchDeletion = () => {
  if (selectLabbel.value.length > 0) {
    const param: number[] = []
    selectLabbel.value.forEach((rs) => {
      param.push(rs.id || 0)
    })
    deleteLabel(param)
  }
}
</script>
