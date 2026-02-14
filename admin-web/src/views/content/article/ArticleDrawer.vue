<template>
  <a-drawer :open="true" :width="790">
    <a-form :model="formState" name="basic" :label-col="{ span: 3 }" :wrapper-col="{ span: 21 }" autocomplete="off" @finish="onSubmit">
      <a-form-item :label="$t('columns.article_name')" name="name" :rules="[{ required: true }]">
        <a-input v-model:value="formState.name" :placeholder="$t('columns.article_name')" />
      </a-form-item>
      <a-form-item :label="$t('columns.article_introduction')" name="introduction" :rules="[{ required: true }]">
        <a-textarea v-model:value="formState.introduction" :placeholder="$t('columns.article_introduction')" />
      </a-form-item>
      <a-form-item label="封面" name="">
        <a-upload v-model:file-list="fileList" name="file" :before-upload="beforeUpload" :customRequest="customRequest" @change="handleChange">
          <a-button>
            <upload-outlined></upload-outlined>
            Click to Upload
          </a-button>
        </a-upload>
      </a-form-item>
      <a-row>
        <a-col :span="8">
          <a-form-item :label-col="{ span: 9 }" :wrapper-col="{ span: 15 }" label="类型" name="" :rules="[{ required: true }]">
            <a-select mode="tags" style="width: 100%" placeholder="Tags Mode"></a-select>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item :label-col="{ span: 9 }" :wrapper-col="{ span: 15 }" label="标签" name="" :rules="[{ required: true }]"> </a-form-item>
        </a-col>
      </a-row>
    </a-form>
    <div class="editor-wrapper base-border">
      <!-- 工具栏 -->
      <Toolbar :editor="editorRef" :defaultConfig="toolbarConfig" :mode="mode" style="border-bottom: 1px solid #ddd" />

      <!-- 编辑器 -->
      <Editor
        v-model="editorValue"
        :defaultConfig="editorConfig"
        :bundle="false"
        :mode="mode"
        style="height: 500px; overflow-y: hidden"
        @onCreated="handleCreated"
      />
    </div>

    <!-- 预览区域 -->
    <div class="preview base-border">
      <h3>实时预览：</h3>
      <div v-html="editorValue"></div>
    </div>
  </a-drawer>
</template>

<script lang="ts" setup>
import { ref, shallowRef, onBeforeUnmount, reactive } from 'vue'
import { message } from 'ant-design-vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

import type { IDomEditor } from '@wangeditor/editor'
import type { ArticleState } from '@/types/articleType'
import type { InsertFnType } from '@/types/resultType'
import type { UploadChangeParam, UploadProps } from 'ant-design-vue'
import type { AxiosRequestConfig } from 'axios'

import { filesManageStore } from '@/stores/files'

import '@wangeditor/editor/dist/css/style.css'

const filesManage = filesManageStore()

const formState = reactive<ArticleState>({
  name: '',
  introduction: '',
  cover: '',
  type: 0,
  label: [],
})

// 编辑器实例（必须用 shallowRef）
const editorRef = shallowRef<IDomEditor | null>(null)

// 编辑器内容
const editorValue = ref('<p>欢迎使用富文本编辑器，尝试输入内容...</p>')

// 模式：'default' 或 'simple'
const mode = ref<'default' | 'simple'>('default')

// 工具栏配置
const toolbarConfig = {
  excludeKeys: [
    // 隐藏不需要的工具栏按钮
    'group-video', // 视频组
    'insertVideo', // 插入视频按钮
    'uploadVideo', // 上传视频按钮
  ],
}

// 编辑器配置
const editorConfig = {
  placeholder: '请输入内容...',
  MENU_CONF: {
    uploadVideo: null,
    uploadImage: {
      customUpload: async (file: File, insertFn: InsertFnType) => {
        try {
          const formData = new FormData()
          formData.append('file', file)
          formData.append('type', 'avatar-images')
          formData.append('platform', '1')
          const res = await filesManage.singUpliad(formData, {})
          if (res && res.url) {
            // 插入图片到编辑器
            insertFn(res.url, '图片描述', res.url)
          } else {
            throw new Error(res.message)
          }
        } catch (error) {
          console.error('上传失败:', error)
          // 显示错误提示
          editorRef.value?.alert('图片上传失败', 'error')
        }
      },
    },
  },
}

// 编辑器创建回调
const handleCreated = (editor: IDomEditor) => {
  editorRef.value = editor
}

const onSubmit = () => {}

// 组件销毁时，及时销毁编辑器
onBeforeUnmount(() => {
  if (editorRef.value) {
    editorRef.value.destroy()
  }
})

//
//

// 上传前验证
const beforeUpload: UploadProps['beforeUpload'] = (file) => {
  // 文件类型验证
  const isValidType = ['image/jpeg', 'image/png'].includes(file.type)
  if (!isValidType) {
    console.error('仅支持 JPG/PNG 格式')
    return false
  }

  // 文件大小验证 (5MB)
  const isLt5M = file.size / 1024 / 1024 < 3
  if (!isLt5M) {
    console.error('文件大小不能超过3MB')
    return false
  }

  return true
}
// 自定义上传逻辑
const customRequest: UploadProps['customRequest'] = async (options) => {
  const { file, onSuccess, onProgress, onError } = options
  const formData = new FormData()
  // 添加文件和其他参数
  formData.append('file', file as File)
  formData.append('type', 'avatar-images')
  formData.append('platform', '1')
  const res = await filesManage.singUpliad(formData, {
    onUploadProgress: (e) => {
      onProgress?.({ percent: Math.round((e.loaded * 100) / (e.total || 1)) })
    },
  } as AxiosRequestConfig)
  // 上传成功回调
  onSuccess?.(res, undefined)

  try {
  } catch (error) {
    onError?.(error as Error)
  }
}

const fileList = ref<unknown>([])

const handleChange = (info: UploadChangeParam) => {
  if (info.file.status !== 'uploading') {
    console.log(info.file, info.fileList)
  }
  if (info.file.status === 'done') {
    fileList.value = info.fileList
  } else if (info.file.status === 'error') {
    message.error(`${info.file.name} file upload failed.`)
  }
}
</script>
<style scoped>
.editor-wrapper {
  border-radius: 5px;
  z-index: 100;
}

.preview {
  margin-top: 20px;
  padding: 15px;
  border-radius: 4px;
}
</style>
