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
        <a-upload name="file" action="https://www.mocky.io/v2/5cc8019d300000980a055e76">
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
      <Editor v-model="editorValue" :defaultConfig="editorConfig" :mode="mode" style="height: 500px; overflow-y: hidden" @onCreated="handleCreated" />
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
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import type { IDomEditor } from '@wangeditor/editor'
import type { ArticleState } from '@/types/articleType'

import '@wangeditor/editor/dist/css/style.css'

const formState = reactive<ArticleState>({
  name: '',
  introduction: '',
  cover: '',
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
  ],
}

// 编辑器配置
const editorConfig = {
  placeholder: '请输入内容...',
  MENU_CONF: {
    uploadImage: {
      server: '/api/upload', // 图片上传地址
      fieldName: 'file', // 上传表单字段名
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
