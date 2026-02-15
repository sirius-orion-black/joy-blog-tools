<template>
  <div class="base-main">
    <div class="base-search">
      <div class="base-search-label">
        <span class="base-search-title">{{ $t('columns.column_name') }}:</span>
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
      </div>
      <a-table
        :columns="columns"
        :data-source="contentColumn.columnList?.records ?? []"
        rowKey="id"
        :pagination="pagination"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, text }">
          <template v-if="column.key === 'state'">{{ getLabelByValue(text) }}</template>
          <template v-if="column.key === 'cover'"><a-image :width="50" :src="text" /></template>
          <template v-else-if="column.key === 'operation'">
            <a class="c9c9efe" @click="changeDrawer('review', text)" v-if="text.state === 2">
              <IconFont type="icon-review" />
            </a>
            <a class="ffa1cf" @click="changeDrawer('edit', text)" v-if="userLogin.user?.id === text.userId"><IconFont type="icon-edit" /></a>
            <a class="c9c9efe" @click="delColumn(text)" v-if="userLogin.user?.id === text.userId"><IconFont type="icon-delete" /></a>
          </template>
        </template>
      </a-table>
    </div>

    <a-drawer :open="showDrawer" :title="$t(drawerTitle)" @close="onClose" :closable="false">
      <a-form :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item :label="$t('columns.column_name')" :rules="[{ required: true, message: '' }]">
          <a-input v-model:value="columnInfo.name" :placeholder="$t('columns.column_name')" :disabled="drawerDisabled" />
        </a-form-item>
        <a-form-item :label="$t('columns.state')" :rules="[{ required: true, message: '' }]">
          <a-select v-model:value="columnInfo.state" style="width: 200px" :options="options" :disabled="true"> </a-select>
        </a-form-item>
        <a-form-item :label="$t('columns.cover')" :rules="[{ required: true, message: '' }]">
          <a-upload
            v-model:file-list="fileList"
            name="cover"
            list-type="picture-card"
            :show-upload-list="false"
            :before-upload="beforeUpload"
            :customRequest="customRequest"
            @change="handleChange"
            :max-count="1"
            :disabled="drawerDisabled"
          >
            <div class="column-drawer-image" v-if="imageUrl" :style="'background-image: url(' + imageUrl + ')'"></div>
            <div v-else>
              <loading-outlined v-if="loading"></loading-outlined>
              <plus-outlined v-else></plus-outlined>
              <div class="ant-upload-text">Upload</div>
            </div>
          </a-upload>
        </a-form-item>
        <a-form-item :label="$t('columns.introduction')" :rules="[{ required: true, message: '' }]">
          <a-textarea v-model:value="columnInfo.introduction" :placeholder="$t('columns.introduction')" :rows="4" :disabled="drawerDisabled" />
        </a-form-item>
        <a-form-item :wrapper-col="{ offset: 6, span: 18 }" v-if="drawerDisabled">
          <a-button type="primary" @click="reviewColumn(1)">上架</a-button>
          <a-button style="margin-left: 10px" @click="reviewColumn(5)">退回</a-button>
        </a-form-item>
      </a-form>

      <template #extra>
        <a-space>
          <a-button type="primary" @click="onSubmit" v-if="!drawerDisabled">{{ $t('menu.submit') }}</a-button>
        </a-space>
      </template>
    </a-drawer>
  </div>
</template>
<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { Modal, message } from 'ant-design-vue'
import { PlusOutlined, LoadingOutlined } from '@ant-design/icons-vue'

import { contentColumnStore } from '@/stores/contentColumn'
import { userLoginStore } from '@/stores/login'
import { filesManageStore } from '@/stores/files'

import type { LabelTypeSearchState, ColumnState } from '@/types/labelType'
import type { PageTableState } from '@/types/resultType'
import type { UploadProps, UploadChangeParam } from 'ant-design-vue'
import type { AxiosRequestConfig } from 'axios'

const contentColumn = contentColumnStore()
const userLogin = userLoginStore()
const filesManage = filesManageStore()

// 获取用户列表参数
const searchParam = ref<LabelTypeSearchState>({
  name: '',
  state: null,
  page: 1,
  size: 10,
})
//弹出层参数
const showDrawer = ref<boolean>(false)
const drawerTitle = ref<string>('drawer.new_label')
const columnInfo = ref<ColumnState>({
  id: null,
  name: '',
  state: 2,
  cover: '',
  introduction: '',
})
const drawerDisabled = ref<boolean>(false)
//封面上传参数
const fileList = ref<unknown>([])
const loading = ref<boolean>(false)
const imageUrl = ref<string>('')

const { t } = useI18n()

const options = computed(() => [
  {
    label: t('columns.shelve'),
    value: 1,
  },
  {
    label: t('columns.reviewing'),
    value: 2,
  },
  {
    label: t('columns.offline'),
    value: 3,
  },
  {
    label: t('columns.deleted'),
    value: 4,
  },
  {
    label: t('columns.returned'),
    value: 5,
  },
])

//根据 value 获取对应的 label
const getLabelByValue = (value: number): string | undefined => {
  const item = options.value.find((item) => item.value === value)
  return item?.label
}

onMounted(() => {
  contentColumn.getList(searchParam.value)
})
const serchLabelList = () => {
  contentColumn.getList(searchParam.value)
}

const resetSearch = () => {
  searchParam.value = { name: '', state: null, page: 1, size: 10 }
  contentColumn.getList(searchParam.value)
}

const changeDrawer = (type: string, item?: ColumnState) => {
  if ((type === 'edit' || type === 'review') && item) {
    columnInfo.value = item
    drawerTitle.value = 'drawer.edit_column'
    imageUrl.value = item.cover!
    drawerDisabled.value = type === 'review' ? true : false
  } else {
    columnInfo.value = { id: null, name: '', state: 2, introduction: '', cover: '' }
    imageUrl.value = ''
    drawerTitle.value = 'drawer.new_column'
  }
  showDrawer.value = true
}

//表格列表
const columns = computed(() => [
  {
    title: t('columns.column_name'),
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: t('columns.state'),
    dataIndex: 'state',
    key: 'state',
  },
  {
    title: t('columns.cover'),
    dataIndex: 'cover',
    key: 'cover',
  },
  {
    title: t('columns.introduction'),
    dataIndex: 'introduction',
    key: 'introduction',
  },
  {
    title: t('columns.action'),
    key: 'operation',
    fixed: 'right',
    width: 105,
  },
])

const pagination = computed(() => {
  return {
    current: contentColumn.columnList?.current,
    total: contentColumn.columnList?.total,
    pageSize: contentColumn.columnList?.size,
  }
})
// 表格分页触发事件
const handleTableChange = (pagination: PageTableState) => {
  searchParam.value.page = pagination.current || 1
  contentColumn.getList(searchParam.value)
}

// 删除标签
const delColumn = (param: ColumnState) => {
  Modal.confirm({
    title: t('base.sure_deleted_it'),
    onOk() {
      contentColumn.delColumn(param)
    },
  })
}

const onClose = () => {
  showDrawer.value = false
}
const onSubmit = () => {
  if (!columnInfo.value.name || !columnInfo.value.cover || !columnInfo.value.introduction || !columnInfo.value.state) {
    message.error(t('drawer.input_empty'))
    return
  }
  if (!columnInfo.value.id) contentColumn.addColumn(columnInfo.value)
  else contentColumn.editColumn(columnInfo.value)
  showDrawer.value = false
}

// 上传前验证
const beforeUpload: UploadProps['beforeUpload'] = (file) => {
  // 文件类型验证
  const isValidType = ['image/jpeg', 'image/png'].includes(file.type)
  if (!isValidType) {
    console.error('仅支持 JPG/PNG 格式')
    return false
  }

  // 文件大小验证 (3MB)
  const isLt3M = file.size / 1024 / 1024 < 3
  if (!isLt3M) {
    console.error(t('request.file_size_exceed_3MB'))
    return false
  }

  return true
}

// 自定义上传逻辑
const customRequest: UploadProps['customRequest'] = async (options) => {
  const { file, onSuccess, onProgress, onError } = options
  try {
    const formData = new FormData()
    // 添加文件和其他参数
    formData.append('file', file as File)
    formData.append('type', 'cover-images')
    formData.append('platform', '1')
    const res = await filesManage.singUpliad(formData, {
      onUploadProgress: (e) => {
        onProgress?.({ percent: Math.round((e.loaded * 100) / (e.total || 1)) })
      },
    } as AxiosRequestConfig)
    // 上传成功回调
    onSuccess?.(res, {
      status: 200,
      response: JSON.stringify(res),
      responseText: JSON.stringify(res),
      getAllResponseHeaders: () => '',
    } as unknown as XMLHttpRequest)
  } catch (error) {
    onError?.(error as Error)
  }
}

const handleChange = (info: UploadChangeParam) => {
  if (info.file.status !== 'uploading') {
    const { response } = info.fileList[0]!
    imageUrl.value = response.url
    columnInfo.value.cover = response.url
  }
  if (info.file.status === 'done') {
    fileList.value = info.fileList
  } else if (info.file.status === 'error') {
    message.error(`${info.file.name} ${t('request.file_upload_failed')}`)
  }
}

//审核
const reviewColumn = (state: number) => {
  const param: ColumnState = {
    id: columnInfo.value.id,
    state: state,
  }
  contentColumn.reviewColumn(param)
  showDrawer.value = false
}
</script>

<style lang="scss" scoped>
.column-drawer-image {
  width: 100px;
  height: 100px;
  background-size: 100px auto;
  background-repeat: no-repeat;
  background-position: center;
}
</style>
