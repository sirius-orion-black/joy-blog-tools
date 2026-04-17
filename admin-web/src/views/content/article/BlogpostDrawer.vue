<template>
  <a-drawer :open="blogpost.drawInfo?.show" :title="$t(blogpost.drawInfo?.title! + '')" :width="860" :closable="false" @close="onClose">
    <template v-if="blogpost.drawInfo?.type !== 'review'">
      <a-form :model="formState" name="basic" :label-col="{ span: 5 }" :wrapper-col="{ span: 19 }" autocomplete="off" @finish="onSubmit">
        <a-form-item :label="$t('columns.article_title')" name="title" :rules="[{ required: true }]">
          <a-input v-model:value="formState.title" :placeholder="$t('columns.article_title')" />
        </a-form-item>
        <a-form-item :label="$t('columns.article_introduction')" name="introduction" :rules="[{ required: true }]">
          <a-textarea v-model:value="formState.introduction" :placeholder="$t('columns.article_introduction')" />
        </a-form-item>
        <a-form-item :label="$t('columns.cover')" name="" :rules="[{ required: true }]">
          <a-upload
            v-model:file-list="fileList"
            name="cover"
            list-type="picture-card"
            :show-upload-list="false"
            :before-upload="beforeUpload"
            :customRequest="customRequest"
            @change="handleChange"
            :max-count="1"
          >
            <div class="column-drawer-image" v-if="formState.cover" :style="'background-image: url(' + formState.cover + ')'"></div>
            <div v-else>
              <loading-outlined v-if="loading"></loading-outlined>
              <plus-outlined v-else></plus-outlined>
              <div class="ant-upload-text">Upload</div>
            </div>
          </a-upload>
        </a-form-item>
        <a-form-item :label-col="{ span: 5 }" :wrapper-col="{ span: 19 }" :label="$t('columns.label_name')" name="" :rules="[{ required: true }]">
          <a-select v-model:value="formState.labels" mode="multiple" style="width: 100%" :placeholder="$t('columns.label_name')">
            <a-select-option
              v-for="item in blogpost.label"
              :key="item.id"
              :value="item.id"
              :disabled="formState.labels!.length >= 5 && formState.labels!.findIndex((o) => Number(o) === item.id) === -1"
            >
              {{ item.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-row>
          <a-col :span="12">
            <a-form-item
              :label-col="{ span: 10 }"
              :wrapper-col="{ span: 14 }"
              :label="$t('columns.classify_name')"
              name=""
              :rules="[{ required: true }]"
            >
              <a-select
                v-model:value="formState.classifyId"
                style="width: 100%"
                :placeholder="$t('columns.classify_name')"
                :options="blogpost.classify"
                :field-names="{ label: 'name', value: 'id' }"
              ></a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item
              :label-col="{ span: 10 }"
              :wrapper-col="{ span: 14 }"
              :label="$t('columns.column_name')"
              name=""
              :rules="[{ required: false }]"
            >
              <a-select
                v-model:value="formState.columnId"
                style="width: 100%"
                :placeholder="$t('columns.column_name')"
                :options="blogpost.column"
                :field-names="{ label: 'name', value: 'id' }"
                :allowClear="true"
              ></a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item :label-col="{ span: 10 }" :wrapper-col="{ span: 14 }" :label="$t('columns.keywords')" name="" :rules="[{ required: true }]">
              <a-input v-model:value="formState.keywords" :placeholder="$t('columns.keywords')" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item :label-col="{ span: 10 }" :wrapper-col="{ span: 14 }" :label="$t('columns.state')" name="" :rules="[{ required: true }]">
              <a-radio-group v-model:value="formState.state">
                <a-radio-button :value="3">{{ $t('columns.draft') }}</a-radio-button>
                <a-radio-button :value="4">{{ $t('columns.publish') }}</a-radio-button>
              </a-radio-group>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item :label-col="{ span: 10 }" :wrapper-col="{ span: 14 }" :label="$t('columns.read_type')" name="" :rules="[{ required: true }]">
              <a-select
                v-model:value="formState.readType"
                style="width: 100%"
                :placeholder="$t('columns.read_type')"
                :options="readTypeOptions"
              ></a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item
              :label-col="{ span: 10 }"
              :wrapper-col="{ span: 14 }"
              :label="$t('columns.originality')"
              name=""
              :rules="[{ required: true }]"
            >
              <a-radio-group v-model:value="formState.isOriginal">
                <a-radio-button :value="1">{{ $t('columns.is_reprint') }}</a-radio-button>
                <a-radio-button :value="2">{{ $t('columns.is_original') }}</a-radio-button>
              </a-radio-group>
            </a-form-item>
          </a-col>
        </a-row>
        <a-form-item :label="$t('columns.reprint_address')" name="reprintAddress" :rules="[{ required: true }]" v-if="formState.isOriginal === 1">
          <a-input v-model:value="formState.reprintAddress" :placeholder="$t('columns.is_reprint')" />
        </a-form-item>
      </a-form>
      <div class="editor-wrapper">
        <!-- 工具栏 -->
        <Toolbar :editor="editorRef" :defaultConfig="toolbarConfig" :mode="mode" style="border-bottom: 1px solid #ddd" />

        <!-- 编辑器 -->
        <Editor
          v-model="formState.content!"
          :defaultConfig="editorConfig"
          :bundle="false"
          :mode="mode"
          :style="{ height: '500px', overfloY: 'hidden' }"
          @onCreated="handleCreated"
        />
      </div>
      <div class="preview base-border">
        <h3>{{ $t('columns.real_time_preview') }}：</h3>
        <CodeBlock :content="formState.content" />
      </div>
    </template>
    <template #extra>
      <a-space>
        <template v-if="blogpost.drawInfo?.type !== 'review'">
          <a-button type="primary" @click="onSubmit">{{ $t('menu.submit') }}</a-button>
        </template>
        <template v-if="blogpost.drawInfo?.type === 'review' && formState.state === 4">
          <a-button type="primary" @click="updateArticle('publish')">{{ $t('columns.shelve') }}</a-button>
          <a-button @click="updateArticle('return')">{{ $t('columns.return') }}</a-button>
        </template>
        <template v-if="blogpost.drawInfo?.type === 'review' && formState.state === 1">
          <a-button type="primary" @click="updateArticle('recommend')">
            {{ $t('columns.is_recommend') }}({{ $t(blogpost.drawInfo?.article.isRecommend === 1 ? 'columns.yes' : 'columns.no') }})
          </a-button>
          <a-button @click="updateArticle('stick')">
            {{ $t('columns.is_stick') }}({{ $t(blogpost.drawInfo?.article.isStick === 1 ? 'columns.yes' : 'columns.no') }})
          </a-button>
        </template>
      </a-space>
    </template>
    <!-- 预览区域 -->

    <div v-if="blogpost.drawInfo?.type === 'review'">
      <h2>
        {{ $t('columns.article_title') }}:<span class="f9a11b review-left">{{ formState.title }}</span>
      </h2>
      <h3>
        {{ $t('columns.article_introduction') }}:<span class="ffa1cf review-left">{{ formState.introduction }}</span>
      </h3>
      <div class="review-cover">
        {{ $t('columns.cover') }}:
        <span class="review-left">
          <a-image :width="80" :src="blogpost.drawInfo?.article.cover" />
        </span>
      </div>
      <a-row :gutter="16">
        <a-col :span="8">
          {{ $t('columns.author') }}:<span class="a66cff review-span">{{ blogpost.drawInfo?.article.userName }}</span>
        </a-col>
        <a-col :span="8">
          {{ $t('columns.classify_name') }}:<span class="f9a11b review-span">{{ blogpost.drawInfo?.article.classifyName }}</span>
        </a-col>
        <a-col :span="8">
          {{ $t('columns.read_type') }}:<span class="c8aabcc review-span">{{
            getReadTypeByValue(blogpost.drawInfo?.article.readType ? blogpost.drawInfo?.article.readType : 1)
          }}</span>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="8">
          {{ $t('columns.column_name') }}:<span class="a66cff review-span">{{ blogpost.drawInfo?.article.columnName }}</span>
        </a-col>
        <a-col :span="8">
          {{ $t('columns.keywords') }}:<span class="f9a11b review-span">{{ blogpost.drawInfo?.article.keywords }}</span>
        </a-col>
        <a-col :span="8">
          {{ $t('columns.state') }}:<span class="c8aabcc review-span">{{ getStateByValue(blogpost.drawInfo?.article.state ?? 4) }}</span>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="8">
          {{ $t('columns.like_count') }}:<span class="a66cff review-span">{{ blogpost.drawInfo?.article.likeCount }}</span>
        </a-col>
        <a-col :span="8">
          {{ $t('columns.readership') }}:<span class="f9a11b review-span">{{ blogpost.drawInfo?.article.readership }}</span>
        </a-col>
        <a-col :span="8">
          {{ $t('columns.is_recommend') }}:<span class="c8aabcc review-span">{{
            $t(blogpost.drawInfo?.article.isRecommend === 1 ? 'columns.yes' : 'columns.no')
          }}</span>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="8">
          {{ $t('columns.is_original') }}:
          <span class="a66cff review-span">{{ $t(blogpost.drawInfo?.article.isOriginal === 2 ? 'columns.yes' : 'columns.no') }}</span>
        </a-col>
        <a-col :span="16">
          {{ $t('columns.label_name') }}:<span class="f9a11b review-span">{{ blogpost.drawInfo?.article.labelNames }}</span>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="8">
          {{ $t('columns.is_stick') }}:<span class="c8aabcc review-span">{{
            $t(blogpost.drawInfo?.article.isStick === 1 ? 'columns.yes' : 'columns.no')
          }}</span>
        </a-col>
        <a-col :span="8">
          {{ $t('columns.create_time') }}:<span class="a66cff review-span">{{ blogpost.drawInfo?.article.createTime }}</span>
        </a-col>
        <a-col :span="8">
          {{ $t('columns.update_time') }}:<span class="f9a11b review-span">{{ blogpost.drawInfo?.article.updateTime }}</span>
        </a-col>
      </a-row>
      <CodeBlock :content="formState.content" />
      <div v-if="blogpost.drawInfo?.article.reprintAddress">
        {{ $t('columns.reprint_address') }}:<span class="f9a11b review-span">{{ blogpost.drawInfo?.article.reprintAddress }}</span>
      </div>
    </div>
  </a-drawer>
</template>

<script lang="ts" setup>
import { ref, shallowRef, onBeforeUnmount, computed, reactive } from 'vue'
import { message, Upload } from 'ant-design-vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { useI18n } from 'vue-i18n'
import { PlusOutlined, LoadingOutlined } from '@ant-design/icons-vue'

import type { IDomEditor } from '@wangeditor/editor'
import type { ArticleState, ArticleUpdateState } from '@/types/articleType'
import type { InsertFnType } from '@/types/resultType'
import type { UploadChangeParam, UploadProps } from 'ant-design-vue'
import type { AxiosRequestConfig } from 'axios'

import { filesManageStore } from '@/stores/files'
import { blogpostStore } from '@/stores/contentBlogpost'

import { useDynamicLabels } from './state'

import CodeBlock from '@/components/codeBlock/CodeBlock.vue'

import '@wangeditor/editor/dist/css/style.css'

const filesManage = filesManageStore()
const blogpost = blogpostStore()
const { t } = useI18n()
const { readTypeOptions, getReadTypeByValue, getStateByValue } = useDynamicLabels()

const formState = computed(() => {
  return reactive<ArticleState>({
    id: blogpost.drawInfo?.article.id,
    title: blogpost.drawInfo?.article.title,
    introduction: blogpost.drawInfo?.article.introduction,
    cover: blogpost.drawInfo?.article.cover,
    classifyId: blogpost.drawInfo?.article.classifyId,
    columnId: blogpost.drawInfo?.article.columnId,
    labels: blogpost.drawInfo?.article.labels!,
    keywords: blogpost.drawInfo?.article.keywords,
    state: blogpost.drawInfo?.article.state,
    readType: blogpost.drawInfo?.article.readType,
    isOriginal: blogpost.drawInfo?.article.isOriginal,
    reprintAddress: blogpost.drawInfo?.article.reprintAddress,
    content: blogpost.drawInfo?.article.content ?? '',
    userId: blogpost.drawInfo?.article.userId ?? null,
  })
})

const onClose = () => {
  // nextTick(() => {
  //   editorRef.value?.setHtml('')
  // })
  blogpost.setDrawInfo({
    title: 'drawer.new_article',
    show: false,
    type: '',
    article: formState.value,
  })
}

// 编辑器实例（必须用 shallowRef）
const editorRef = shallowRef<IDomEditor | null>(null)

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
          formData.append('type', 'article-content')
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

const onSubmit = () => {
  if (blogpost.drawInfo?.type === 'new') blogpost.addArticle(formState.value)
  else if (blogpost.drawInfo?.type === 'edit') blogpost.editArticle(formState.value)
  onClose()
}

// 组件销毁时，及时销毁编辑器
onBeforeUnmount(() => {
  if (editorRef.value) {
    editorRef.value.destroy()
  }
})

const updateArticle = (action: string) => {
  const param: ArticleUpdateState = {
    id: formState.value.id ?? 0,
    action,
  }
  blogpost.updArticle(param)
  onClose()
}

// 上传前验证
const beforeUpload: UploadProps['beforeUpload'] = (file) => {
  // 文件类型验证
  const isValidType = ['image/jpeg', 'image/png'].includes(file.type)
  if (!isValidType) {
    message.error('仅支持 JPG/PNG 格式')
    return Upload.LIST_IGNORE
  }

  // 文件大小验证 (3MB)
  const isLt5M = file.size / 1024 / 1024 < 3
  if (!isLt5M) {
    message.error('文件大小不能超过3MB')
    return Upload.LIST_IGNORE
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
    formData.append('type', 'article-cover')
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

//封面上传参数
const fileList = ref<unknown>([])
const loading = ref<boolean>(false)

const handleChange = (info: UploadChangeParam) => {
  if (info.file.status !== 'uploading') {
    const { response } = info.fileList[0]!
    formState.value.cover = response.url
  }
  if (info.file.status === 'done') {
    fileList.value = info.fileList
  } else if (info.file.status === 'error') {
    message.error(`${info.file.name} ${t('request.file_upload_failed')}`)
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

.column-drawer-image {
  width: 100px;
  height: 100px;
  background-size: 100px auto;
  background-repeat: no-repeat;
  background-position: center;
}
.review-left {
  margin-left: 10px;
}
.review-span {
  margin-left: 5px;
}
</style>
