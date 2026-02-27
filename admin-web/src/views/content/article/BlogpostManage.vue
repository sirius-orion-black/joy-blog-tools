<template>
  <div class="base-main">
    <div class="base-search">
      <div class="base-search-label">
        <span class="base-search-title">{{ $t('columns.article_title') }}:</span>
        <a-input v-model:value="searchParam.title" class="base-search-height" />
      </div>

      <div class="base-search-label">
        <span class="base-search-title">分类:</span>
        <a-select v-model:value="searchParam.type" style="width: 200px"></a-select>
      </div>

      <div class="base-search-label">
        <span class="base-search-title">状态:</span>
        <a-select v-model:value="searchParam.state" style="width: 200px" :options="stateOptions"></a-select>
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
          <a-button @click="changeDrawer('new')"><IconFont type="icon-add" />{{ $t('menu.add') }}</a-button>
        </div>
      </div>

      <a-table :columns="columns" :data-source="blogpost.list?.records ?? []" rowKey="id" :pagination="pagination" @change="handleTableChange">
        <template #bodyCell="{ column, text }">
          <template v-if="column.key === 'state'">{{ getStateByValue(text) }}</template>
          <template v-else-if="column.key === 'cover'"><a-image :width="50" :src="text" /></template>
          <template v-else-if="column.key === 'readType'">{{ getReadTypeByValue(text) }}</template>
          <template v-else-if="column.key === 'labelNames'">
            <div class="blog_post_label">
              <span v-for="(label, idx) in text" :key="idx" :class="classNames[idx]">{{ label }}</span>
            </div>
          </template>
          <template v-else-if="column.key === 'operation'">
            <a class="f9a11b" @click="changeDrawer('review', text)">
              <IconFont type="icon-review" />
            </a>
            <template v-if="userLogin.user?.id === text.userId">
              <a class="ffa1cf" @click="changeDrawer('edit', text)"><IconFont type="icon-edit" /></a>
              <a class="c9c9efe" @click="delArticle(text)"><IconFont type="icon-delete" /></a>
            </template>
          </template>
        </template>
      </a-table>
    </div>
    <BlogpostDrawer v-if="blogpost.drawInfo?.show" />
  </div>
</template>
<script lang="ts" setup>
import { ref, onMounted, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { Modal } from 'ant-design-vue'

import { blogpostStore } from '@/stores/contentBlogpost'
import { userLoginStore } from '@/stores/login'

import BlogpostDrawer from './BlogpostDrawer.vue'

import type { ArticleSearchState, ArticleState } from '@/types/articleType'
import type { PageTableState } from '@/types/resultType'

import { useDynamicLabels } from './state'

const blogpost = blogpostStore()
const userLogin = userLoginStore()
const { t } = useI18n()
const { columns, stateOptions, classNames, getStateByValue, getReadTypeByValue } = useDynamicLabels()

// 获取用户列表参数
const searchParam = ref<ArticleSearchState>({
  title: '',
  type: null,
  state: null,
  page: 1,
  size: 10,
})

onMounted(() => {
  blogpost.getList(searchParam.value)
  blogpost.getClassify()
  blogpost.getLabel()
  blogpost.getColumn()
})
const serchLabelList = () => {
  blogpost.getList(searchParam.value)
}

const resetSearch = () => {
  searchParam.value = { title: '', type: null, state: null, page: 1, size: 10 }
  blogpost.getList(searchParam.value)
}

//分页
const pagination = computed(() => {
  return {
    current: blogpost.list?.current,
    total: blogpost.list?.total,
    pageSize: blogpost.list?.size,
  }
})

// 表格分页触发事件
const handleTableChange = (pagination: PageTableState) => {
  searchParam.value.page = pagination.current || 1
  blogpost.getList(searchParam.value)
}
//弹出层参数
const changeDrawer = (type: string, item?: ArticleState) => {
  let article: ArticleState = {
    id: null,
    title: '',
    introduction: '',
    cover: '',
    classifyId: null,
    columnId: null,
    labels: [],
    keywords: '',
    state: 4,
    readType: 1,
    isOriginal: 2,
    content: '',
  }
  if ((type === 'edit' || type === 'review') && item) article = item
  blogpost.setDrawInfo({
    title: 'drawer.' + type + '_article',
    show: true,
    type,
    article,
  })
}

// 删除标签
const delArticle = (param: ArticleState) => {
  Modal.confirm({
    title: t('base.sure_deleted_it'),
    onOk() {
      blogpost.delArticle(param)
    },
  })
}
</script>
<style lang="scss" scoped>
.blog_post_label {
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
</style>
