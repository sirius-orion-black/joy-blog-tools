<template>
  <div class="base-main">
    <div class="base-search">
      <div class="base-search-label">
        <span class="base-search-title">{{ $t('base.user_name') }}:</span>
        <a-input v-model:value="searchParam.username" style="width: 150px; height: 32px" />
      </div>
      <div class="base-search-label">
        <span class="base-search-title">{{ $t('base.user_phone') }}:</span>
        <a-input v-model:value="searchParam.phone" style="width: 150px; height: 32px" />
      </div>
      <div class="base-search-label">
        <span class="base-search-title">{{ $t('base.user_email') }}:</span>
        <a-input v-model:value="searchParam.email" style="width: 150px; height: 32px" />
      </div>
      <div class="base-search-label">
        <span class="base-search-title">{{ $t('columns.state') }}:</span>
        <a-select v-model:value="searchParam.state" style="width: 100px; height: 32px" :options="options"></a-select>
      </div>
      <div class="base-search-label">
        <a-button type="primary" @click="serchUserList">{{ $t('menu.search') }}</a-button>
      </div>
      <div class="base-search-label">
        <a-button @click="restSearch">{{ $t('menu.reset') }}</a-button>
      </div>
    </div>
    <div class="base-container">
      <div class="base-operate">
        <div class="base-operate-label">
          <a-button @click="showUserDrawer('add', 360)"><IconFont type="icon-add" />{{ $t('menu.add') }}</a-button>
        </div>
        <div class="base-operate-label">
          <a-button @click="batchIsbanned"><IconFont type="icon-is-banned" />{{ $t('menu.batch_is_banned') }}</a-button>
        </div>
        <div class="base-operate-label">
          <a-button @click="batchDeletion"><IconFont type="icon-delete" />{{ $t('menu.batch_deletion') }}</a-button>
        </div>
      </div>
      <a-table
        :columns="columns"
        :data-source="user.list?.records"
        :row-selection="{ selectedRowKeys: tableState.selectedRowKeys, onChange: onSelectChange }"
        rowKey="id"
        :pagination="pagination"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, text }">
          <template v-if="column.key === 'sex'">
            {{ $t(text === 1 ? 'base.sex_m' : 'base.sex_f') }}
          </template>
          <template v-if="column.key === 'state'">
            {{ $t(getState(text)) }}
          </template>
          <template v-else-if="column.key === 'operation'">
            <div class="base-btn-a font-size-16">
              <a class="a66cff" @click="showUserDrawer('permission', 360, text)"><IconFont type="icon-permission" /></a>
              <a class="ffa1cf" @click="showUserDrawer('edit', 360, text)"><IconFont type="icon-edit" /></a>
              <a class="c662b2f" @click="isBannedUser(text)"><IconFont :type="text.state !== 2 ? 'icon-batch-banned' : 'icon-batch-unbanned'" /></a>
              <a class="c9c9efe" @click="delUser(text)"><IconFont type="icon-delete" /></a>
            </div>
          </template>
        </template>
      </a-table>
    </div>
    <UserDrawer @update:list="serchUserList" />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, reactive, onMounted, nextTick } from 'vue'
import { useI18n } from 'vue-i18n'
import { Modal } from 'ant-design-vue'

import { userStore } from '@/stores/user'
import { menuStore } from '@/stores/menu'

import type { Ref } from 'vue'
import type { UserSearchTypeState, UserTypeState } from '@/types/UserType'
import type { PageTableState } from '@/types/result'

import UserDrawer from './UserDrawer.vue'

const user = userStore()
const menu = menuStore()

// 获取用户列表参数
const searchParam = reactive<UserSearchTypeState>({
  username: '',
  email: '',
  phone: '',
  state: null,
  page: 1,
  size: 10,
})

onMounted(() => {
  user.getList(searchParam)
})

const getState = (state: number) => {
  switch (state) {
    case 2:
      return 'columns.disabled'
    case 3:
      return 'columns.unverified'
    case 4:
      return 'columns.demo_account'
    case 5:
      return 'columns.account_deletion'
    default:
      return 'columns.normal'
  }
}
// 用户列表
const serchUserList = () => {
  console.log('fu zu jian lai lee ')
  user.getList(searchParam)
}
// 重置
const restSearch = () => {
  searchParam.username = ''
  searchParam.email = ''
  searchParam.phone = ''
  searchParam.page = 1
  searchParam.size = 10
  user.getList(searchParam)
}

const { t } = useI18n()

// 表格数据
const options = [
  {
    label: t('columns.normal'),
    value: 1,
  },
  {
    label: t('columns.disabled'),
    value: 2,
  },
  {
    label: t('columns.unverified'), //邮箱或手机号未验证
    value: 3,
  },
  {
    label: t('columns.demo_account'),
    value: 4,
  },
]
const columns = [
  {
    title: t('base.user_nickname'),
    dataIndex: 'nickname',
    key: 'nickname',
  },
  {
    title: t('base.user_name'),
    dataIndex: 'username',
    key: 'username',
  },
  {
    title: t('base.user_phone'),
    dataIndex: 'phone',
    key: 'phone',
  },
  {
    title: t('base.user_email'),
    dataIndex: 'email',
    key: 'email',
  },
  {
    title: t('base.sex'),
    dataIndex: 'sex',
    key: 'sex',
  },
  {
    title: t('columns.state'),
    dataIndex: 'state',
    key: 'state',
  },
  {
    title: t('columns.action'),
    key: 'operation',
    fixed: 'right',
    width: 165,
  },
]
// 分页
const pagination = computed(() => {
  return {
    current: user.list?.current,
    total: user.list?.total,
    pageSize: user.list?.size,
  }
})
// 表格分页触发事件
const handleTableChange = (pagination: PageTableState) => {
  searchParam.page = pagination.current || 1
  user.getList(searchParam)
}

// 多选
const selectUsers: Ref<UserTypeState[]> = ref([])
// 表格行选择配置
type tableKey = string | number
const tableState = reactive<{
  selectedRowKeys: tableKey[]
}>({
  selectedRowKeys: [],
})
const onSelectChange = (selectedRowKeys: tableKey[], selectedRows: UserTypeState[]) => {
  selectUsers.value = selectedRows
  tableState.selectedRowKeys = selectedRowKeys
}
// 显示弹出层
const showUserDrawer = (type: string, width: number, text?: UserTypeState) => {
  if (type === 'permission') {
    menu.getMenu()
    user.getPermission({ userId: text?.id! })
  }
  user.setUserDrawer(type, true, width, text)
}

// 封禁or解封用户
const bannedUser = (param: UserTypeState[]) => {
  Modal.confirm({
    title: t('base.confirmed_is_banned'),
    async onOk() {
      await user.isBannedUser(param)
      user.getList(searchParam)
      nextTick(() => {
        selectUsers.value = [] // 确保在DOM更新后执行
        tableState.selectedRowKeys = []
      })
    },
    onCancel() {},
  })
}
const isBannedUser = (text: UserTypeState) => {
  const param: UserTypeState[] = []
  param.push({ id: text.id, state: text.state === 2 ? 1 : 2 })
  bannedUser(param)
}
const batchIsbanned = () => {
  if (selectUsers.value.length > 0) {
    const param: UserTypeState[] = []
    selectUsers.value.forEach((rs) => {
      param.push({ id: rs.id, state: rs.state === 2 ? 1 : 2 })
    })
    bannedUser(param)
  }
}
// 删除用户
const deleteUser = (param: number[]) => {
  Modal.confirm({
    title: t('base.sure_deleted_it'),
    async onOk() {
      await user.delUser(param)
      user.getList(searchParam)
      nextTick(() => {
        selectUsers.value = [] // 确保在DOM更新后执行
        tableState.selectedRowKeys = []
      })
    },
    onCancel() {},
  })
}
const delUser = (text: UserTypeState) => {
  const param: number[] = []
  param.push(text?.id || 0)
  deleteUser(param)
}
const batchDeletion = () => {
  if (selectUsers.value.length > 0) {
    const param: number[] = []
    selectUsers.value.forEach((rs) => {
      param.push(rs.id || 0)
    })
    deleteUser(param)
  }
}
</script>
<style lang="scss" scoped></style>
