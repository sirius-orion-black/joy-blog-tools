<template>
  <div class="base-main">
    <div class="base-search">
      <div class="base-search-label">
        <span class="base-search-title">用户名:</span>
        <a-input v-model:value="searchParam.name" class="base-search-height" />
      </div>

      <div class="base-search-label">
        <a-button type="primary" @click="serchMomentsList">{{ $t('menu.search') }}</a-button>
      </div>
      <div class="base-search-label">
        <a-button @click="resetSearch">{{ $t('menu.reset') }}</a-button>
      </div>
    </div>
    <div class="base-container">
      <div class="base-operate">
        <div class="base-operate-label">
          <a-button @click="changeDrawer('new')"><IconFont type="icon-add" />{{ $t('menu.add') }}</a-button>
        </div>
      </div>
      <a-table :columns="options" :data-source="moments.list?.records ?? []" rowKey="id" :pagination="pagination" @change="handleTableChange">
        <template #bodyCell="{ column, text }">
          <template v-if="column.key === 'labelNames'">
            <div class="moments_label">
              <span v-for="(label, idx) in text" :key="idx" :class="classNames[idx]">{{ label }}</span>
            </div>
          </template>
          <template v-else-if="column.key === 'operation'">
            <a class="f9a11b" @click="changeDrawer('review', text)">
              <IconFont type="icon-review" />
            </a>
          </template>
          <template v-if="userLogin.user?.id === text?.userId">
            <a class="c9c9efe" @click="delMoments(text)"><IconFont type="icon-delete" /></a>
          </template>
        </template>
      </a-table>
    </div>
    <a-drawer :open="showDrawer" :title="$t(drawerTitle)" :width="560" @close="onClose" :closable="false">
      <template v-if="showType !== 'review'">
        <a-form :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
          <a-form-item :label="$t('columns.label_name')" name="" :rules="[{ required: false }]">
            <a-select v-model:value="info.labels" mode="multiple" style="width: 100%" :placeholder="$t('columns.label_name')">
              <a-select-option
                v-for="item in moments.label"
                :key="item.id"
                :value="item.id"
                :disabled="info.labels!.length >= 5 && info.labels!.findIndex((o) => Number(o) === item.id) === -1"
              >
                {{ item.name }}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item :label="$t('columns.state')" name="" :rules="[{ required: true }]">
            <a-radio-group v-model:value="info.state">
              <a-radio-button :value="3">{{ $t('columns.draft') }}</a-radio-button>
              <a-radio-button :value="1">{{ $t('columns.publish') }}</a-radio-button>
            </a-radio-group>
          </a-form-item>
          <a-form-item :label="$t('columns.privacy')" name="" :rules="[{ required: true }]">
            <a-radio-group v-model:value="info.privacy">
              <a-radio-button :value="1">{{ $t('columns.public') }}</a-radio-button>
              <a-radio-button :value="2">{{ $t('columns.friend') }}</a-radio-button>
              <a-radio-button :value="3">{{ $t('columns.secret') }}</a-radio-button>
            </a-radio-group>
          </a-form-item>
          <a-form-item :label="$t('columns.content')" :rules="[{ required: false, message: '' }]">
            <a-textarea v-model:value="info.content" :placeholder="$t('columns.content')" />
          </a-form-item>
          <a-form-item :label="$t('columns.file')">
            <a-switch v-model:checked="imgVideo" checked-children="图片" un-checked-children="视频" @change="imgVideoChange" />
          </a-form-item>
          <a-row>
            <a-col :span="6"></a-col>
            <a-col :span="18">
              <a-upload
                v-model:file-list="fileList"
                list-type="picture-card"
                @preview="handlePreview"
                :before-upload="beforeUpload"
                :customRequest="customRequest"
                @change="handleChange"
              >
                <div v-if="fileList.length < fileCount">
                  <plus-outlined />
                  <div style="margin-top: 8px">Upload</div>
                </div>
              </a-upload>
              <a-modal :open="previewVisible" :footer="null" @cancel="handleCancel">
                <img alt="example" style="width: 100%" :src="previewImage" />
              </a-modal>
            </a-col>
          </a-row>
        </a-form>
      </template>
      <template v-else>
        <div class="moments-review" v-if="info.content">{{ info.content }}</div>
        <div class="moments-review" v-if="info.imageUrls && info.imageUrls.length > 0">
          <div class="moments-img" v-for="(value, index) in info.imageUrls.split(',')" :key="index">
            <a-image :width="150" :src="value" />
          </div>
        </div>
        <div class="moments-review" v-if="info.videoUrl && info.videoUrl.length > 0" style="width: 497px; height: 500px">
          <VideoPlayer :src="info.videoUrl" :width="player.width" :height="player.height" :poster-url="player.poster" :auto-play="false" />
        </div>
      </template>
      <template #extra>
        <template v-if="showType !== 'review'">
          <a-button type="primary" @click="onSubmit">{{ $t('menu.submit') }}</a-button>
        </template>
        <template v-if="showType === 'review' && info.state !== 4">
          <a-button type="primary" @click="reviewMoments">{{ $t('columns.violation') }}</a-button>
        </template>
      </template>
    </a-drawer>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { Modal, message, Upload } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'

import { momentsStore } from '@/stores/contentMoments'
import { userLoginStore } from '@/stores/login'
import { filesManageStore } from '@/stores/files'

import type { momentsState, momentsSearchState, momentsVideoState } from '@/types/moments'
import type { PageTableState } from '@/types/resultType'
import type { UploadChangeParam, UploadProps, UploadFile } from 'ant-design-vue'
import type { AxiosRequestConfig } from 'axios'

import VideoPlayer from '@/components/video/VideoPlayer.vue'

const { t } = useI18n()
const moments = momentsStore()
const userLogin = userLoginStore()
const filesManage = filesManageStore()
// 获取用户列表参数
const searchParam = ref<momentsSearchState>({
  name: '',
  page: 1,
  size: 10,
})
//表格列
const options = computed(() => [
  {
    title: t('columns.author'),
    dataIndex: 'userName',
    key: 'userName',
  },
  {
    title: t('columns.content'),
    dataIndex: 'content',
    key: 'content',
    ellipsis: true,
  },
  {
    title: t('columns.label_name'),
    dataIndex: 'labelNames',
    key: 'labelNames',
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
const classNames: string[] = ['a66cff', 'f9a11b', 'ffa1cf', 'c8aabcc', 'c9c9efe', 'c662b2f']
const serchMomentsList = () => {
  moments.getList(searchParam.value)
}
const resetSearch = () => {
  searchParam.value = { name: '', page: 1, size: 10 }
  serchMomentsList()
}
onMounted(() => {
  serchMomentsList()
  moments.getLabel()
})
//朋友圈内容
const info = ref<momentsState>({
  id: null,
  userId: null,
  content: '',
  imageUrls: '',
  videoUrl: '',
  privacy: 1,
  state: 1,
  location: '',
  userSource: 1,
  labels: [],
  labelNames: [],
  userName: '',
})
const imgVideo = ref<boolean>(true)
//弹出层参数
const showDrawer = ref<boolean>(false)
const drawerTitle = ref<string>('drawer.new_moments‌')
const showType = ref<string>('')
const player = ref<momentsVideoState>({
  width: '497px',
  height: '500px',
  poster: '',
})

const onClose = () => {
  showDrawer.value = false
}
//分页
const pagination = computed(() => {
  return {
    current: moments.list?.current,
    total: moments.list?.total,
    pageSize: moments.list?.size,
  }
})
// 表格分页触发事件
const handleTableChange = (pagination: PageTableState) => {
  searchParam.value.page = pagination.current || 1
  moments.getList(searchParam.value)
}
const changeDrawer = (
  type: string,
  text: momentsState = {
    id: null,
    userId: null,
    content: '',
    imageUrls: '',
    videoUrl: '',
    privacy: 1,
    state: 1,
    location: '',
    userSource: 1,
    labels: [],
    labelNames: [],
    userName: '',
  },
) => {
  showType.value = type
  info.value = text
  showDrawer.value = true
  drawerTitle.value = type === 'new' ? 'drawer.new_moments‌' : 'drawer.review_moments‌'
}
const delMoments = (text: momentsState) => {
  Modal.confirm({
    title: t('base.sure_deleted_it'),
    onOk() {
      moments.delmoments({ id: text.id, userId: text.userId })
    },
  })
}
const reviewMoments = () => {
  Modal.confirm({
    title: t('base.sure_violation_it'),
    onOk() {
      moments.revMoments({ id: info.value.id, userId: info.value.userId })
      onClose()
    },
  })
}
//文件上传
const fileList = ref<UploadFile[]>([])
const fileCount = ref<number>(9)

const previewVisible = ref(false)
const previewImage = ref('')
//图图片视频切换
const imgVideoChange = () => {
  fileCount.value = imgVideo.value ? 8 : 1
  fileList.value = []
}
// 获取 base64 数据
function getBase64(img: File): Promise<string> {
  return new Promise((resolve) => {
    const reader = new FileReader()
    reader.addEventListener('load', () => resolve(reader.result as string))
    reader.readAsDataURL(img)
  })
}
// 预览处理
const handlePreview = async (file: UploadFile) => {
  if (!file.url && !file.preview) {
    file.preview = (await getBase64(file.originFileObj!)) as string
  }
  previewImage.value = file.url || file.preview || ''
  previewVisible.value = true
}

// 关闭预览弹窗
const handleCancel = () => {
  previewVisible.value = false
}
// 支持的图片类型
const supportedImageTypes = ['image/jpeg', 'image/png', 'image/webp']

// 支持的视频类型
const supportedVideoTypes = ['video/mp4', 'video/mpeg', 'video/webm', 'video/ogg', 'video/3gpp', 'video/x-matroska', 'video/avi']
// 上传前验证
const beforeUpload: UploadProps['beforeUpload'] = (file) => {
  // 图片上传模式
  if (imgVideo.value) {
    const isImage = supportedImageTypes.includes(file.type)

    if (!isImage) {
      message.error(`只能上传以下格式的图片: ${supportedImageTypes.join(', ')}`)
      return Upload.LIST_IGNORE // 不添加到上传列表
    }

    // 检查文件大小（可选，这里设为10MB）
    const isLt3M = file.size / 1024 / 1024 < 3
    if (!isLt3M) {
      message.error('文件大小不能超过3MB')
      return Upload.LIST_IGNORE // 不添加到上传列表
    }
  }
  // 视频上传模式
  else {
    const isVideo = supportedVideoTypes.includes(file.type)

    if (!isVideo) {
      message.error(`只能上传以下格式的视频: ${supportedVideoTypes.join(', ')}`)
      return Upload.LIST_IGNORE // 不添加到上传列表
    }

    // 检查文件大小（可选，这里设为100MB）
    const isLt5M = file.size / 1024 / 1024 < 5
    if (!isLt5M) {
      message.error('视频大小不能超过 5MB!')
      return Upload.LIST_IGNORE // 不添加到上传列表
    }
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
    formData.append('type', imgVideo.value ? 'moments-img' : 'moments-video')
    formData.append('platform', '1')
    const res = await filesManage.singUpliad(formData, {
      onUploadProgress: (e) => {
        onProgress?.({ percent: Math.round((e.loaded * 100) / (e.total || 1)) })
      },
    } as AxiosRequestConfig)
    // 上传成功回调
    onSuccess?.(res, undefined)
  } catch (error) {
    onError?.(error as Error)
  }
}
const handleChange = (info: UploadChangeParam) => {
  if (info.file.status === 'done') {
    // 上传成功后更新文件列表
    fileList.value = info.fileList.map((file) => ({
      ...file,
      status: 'done',
      url: file.response?.url || file.url,
      thumbUrl: file.response?.thumbUrl || file.response?.url || file.thumbUrl || file.url,
    }))
  } else if (info.file.status === 'error') {
    message.error(`${info.file.name} 上传失败`)
  } else if (info.file.status === 'removed') {
    // 文件被删除时更新列表
    fileList.value = info.fileList
  }
}

const onSubmit = () => {
  const urls = fileList.value
    .filter((file) => file.url) // 过滤出有 url 属性的文件
    .map((file) => file.url) // 提取 url 属性
  if (!info.value.content && !info.value.imageUrls && urls.length < 1) {
    message.info(t('request.friends_pictures_videos'))
  } else {
    const params: momentsState = { labels: info.value.labels, privacy: info.value.privacy, state: info.value.state }
    if (info.value.content) params.content = info.value.content
    if (imgVideo.value && urls.length > 0) params.imageUrls = urls.join(',')
    if (!imgVideo.value && urls.length > 0) params.videoUrl = urls.join(',')
    moments.addmoments(params)
    onClose()
    fileList.value = []
  }
}
</script>
<style lang="scss" scoped>
.moments_label {
  display: flex;
  font-size: 12px;
  flex-direction: row;
  flex-wrap: wrap;
  align-content: center;
  justify-content: flex-start;
  & > span {
    display: block;
    padding: 3px;
  }
}
.moments-review {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: space-between;
  align-items: center;
  align-content: center;
  margin-bottom: 20px;
  .moments-img {
    width: 150px;
  }
}
</style>
