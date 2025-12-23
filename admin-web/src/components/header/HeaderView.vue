<template>
  <div class="header-main">
    <div class="header-body">
      <div class="header-search">
        <a-auto-complete
          v-model:value="value"
          :dropdown-match-select-width="252"
          style="width: 300px"
          :options="dataSource"
          @select="onSelect"
          @search="handleSearch"
        >
          <template #option="item">
            <div style="display: flex; justify-content: space-between">
              <span>
                Found {{ item.query }} on
                <a :href="`https://s.taobao.com/search?q=${item.query}`" target="_blank" rel="noopener noreferrer">
                  {{ item.category }}
                </a>
              </span>
              <span>{{ item.count }} results</span>
            </div>
          </template>
          <a-input-search size="large" placeholder="input here" enter-button></a-input-search>
        </a-auto-complete>
      </div>
      <div class="header-personal header-margin-right">
        <div class="header-margin-right">醉酒当歌</div>
        <a-badge :count="99" class="cursor-pointer header-margin-right">
          <MessageIcon width="24px" height="24px" />
        </a-badge>
        <LanguageIcon width="24px" height="24px" class="cursor-pointer header-margin-right" />
        <MoonIcon width="24px" height="24px" class="cursor-pointer header-margin-right" />
        <a-dropdown class="header-personal">
          <img class="header-avatar" src="https://joyimg.lexujia.com/joy/assets/logo.jpg" @click.prevent />
          <DownOutlined />
          <template #overlay>
            <a-menu>
              <a-menu-item>
                <a href="javascript:;">个人设置</a>
              </a-menu-item>
              <a-menu-item>
                <a href="javascript:;">修改密码</a>
              </a-menu-item>
              <a-menu-item>
                <a href="javascript:;">个人资料</a>
              </a-menu-item>
              <a-menu-item>
                <a href="javascript:;">登出</a>
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { DownOutlined } from '@ant-design/icons-vue'
import MoonIcon from '../icons/MoonIcon.vue'
import LanguageIcon from '../icons/LanguageIcon.vue'
import MessageIcon from '../icons/MessageIcon..vue'

interface Option {
  query: string
  category: string
  value: string
  count: number
}
const value = ref('')
const dataSource = ref<Option[]>([])
const onSelect = (value: string) => {
  console.log('onSelect', value)
}
const getRandomInt = (max: number, min = 0) => {
  return Math.floor(Math.random() * (max - min + 1)) + min
}
const searchResult = (query: string): Option[] => {
  return new Array(getRandomInt(5))
    .join('.')
    .split('.')
    .map((_item, idx) => ({
      query,
      category: `${query}${idx}`,
      value: `${query}${idx}`,
      count: getRandomInt(200, 100),
    }))
}
const handleSearch = (val: string) => {
  dataSource.value = val ? searchResult(val) : []
}
</script>

<style lang="scss" scoped>
.header-main {
  z-index: 9;
  height: 62px;
  position: fixed;
  right: 0;
  left: 272px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.2);
  .header-body {
    display: flex;
    align-items: center;
    color: #000000;
    height: 62px;
    justify-content: space-between;
    flex-wrap: nowrap;
    .header-search {
      margin-left: 15px;
      // :deep(.ant-select-dropdown-menu-item-group-title) {
      //   color: #666;
      //   font-weight: bold;
      // }
      // :deep(.ant-select-dropdown-menu-item-group) {
      //   border-bottom: 1px solid #f6f6f6;
      // }

      // :deep(.ant-select-dropdown-menu-item) {
      //   padding-left: 16px;
      // }
      :deep(.ant-input-search .ant-input:focus),
      :deep(.ant-input-search .ant-input:hover) {
        box-shadow: none;
        border: 1px solid rgba(0, 0, 0, 0.2);
      }
      :deep(.ant-btn-primary:focus),
      :deep(.ant-btn-primary:hover) {
        box-shadow: none;
      }
      :deep(.ant-btn-primary) {
        background-color: #ffffff;
        border: 1px solid rgba(0, 0, 0, 0.2);
        box-shadow: none;
        color: #000000;
        height: 39px;
      }
    }
    .header-margin-right {
      margin-right: 15px;
    }
    .header-personal {
      display: flex;
      align-items: center;
      height: 62px;
      justify-content: flex-end;
      flex-wrap: nowrap;
      .header-avatar {
        cursor: pointer;
        width: 36px;
        height: 36px;
        border-radius: 50%;
      }
    }
  }
}
</style>
