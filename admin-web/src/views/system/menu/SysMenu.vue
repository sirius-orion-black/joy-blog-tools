<template>
  <div class="base-main">
    <div class="base-container">
      <div class="base-operate">
        <div class="base-operate-label">
          <a-button @click="showDrawer('drawer.new_menu')"><IconFont type="icon-add" />{{ $t('menu.add') }}</a-button>
        </div>
        <div class="base-operate-label">
          <a-button @click="batchDeletionMenu()"><IconFont type="icon-delete" />{{ $t('menu.batch_deletion') }}</a-button>
        </div>
      </div>
      <a-table :columns="columns" :data-source="menu.menuList" :row-selection="rowSelection" rowKey="id" :pagination="false">
        <template #bodyCell="{ column, text, record }">
          <template v-if="column.key === 'name'">
            <IconFont :type="record.icon" />
            <span style="margin-left: 5px">{{ $t(`menu.${text}`) }} </span>
          </template>
          <template v-else-if="column.key === 'type'">
            <span class="a66cff">{{ $t(getType(text)) }}</span>
          </template>
          <template v-else-if="column.key === 'state'">
            <span class="a66cff">{{ $t(text === 1 ? 'columns.normal' : 'columns.disabled') }}</span>
          </template>
          <template v-else-if="column.key === 'operation'">
            <div class="ase-btn-a font-size-16">
              <a class="f9a11b" @click="showDrawer('drawer.new_menu', { parentId: text.id })"><IconFont type="icon-menu-plus" /></a>
              <a class="ffa1cf" @click="showDrawer('drawer.edit_menu', text)"><IconFont type="icon-edit" /></a>
              <a class="c9c9efe" @click="deleteMenu(text)"><IconFont type="icon-delete" /></a>
            </div>
          </template>
        </template>
      </a-table>
      <MenuDrawer :title="drawTitle" :list="list" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { Modal } from 'ant-design-vue'

import { menuStore } from '@/stores/menu'

import type { MenuTypeState } from '@/types/MenuType'

import MenuDrawer from './MenuDrawer.vue'
import { IconFont } from '@/utils/iconfont'

const { t } = useI18n()
const menu = menuStore()
onMounted(() => {
  menu.getMenu() // 获取菜单列表
  menu.getMenuIcons() // 获取菜单icon列表
})

const getType = (val: number) => {
  switch (val) {
    case 1:
      return 'columns.directory'
    case 2:
      return 'columns.menu'
    default:
      return 'columns.permission'
  }
}

const drawTitle = ref<string>('drawer.new_menu')

const list = ref<MenuTypeState>({
  id: 0,
  name: '',
  icon: '',
  permission: '',
  router: '',
  component: '',
  sort: 0,
  isExternal: 2,
  parentId: 0,
  path: '',
  type: 1,
  state: 1,
  description: '',
})
// 初始化为空数组
const selectMenus = ref<MenuTypeState[]>([])
// 菜单编辑页面
const showDrawer = (key: string = '', val: MenuTypeState = {}) => {
  drawTitle.value = key
  val.id = val.id ?? 0
  if (!val.parentId) val.parentId = 0
  list.value = {
    ...list.value,
    ...val,
  }
  menu.setMenuDraw(true)
}
// 菜单删除
const deleteInfo = (param: number[], title: string) => {
  Modal.confirm({
    title: title,
    onOk() {
      menu.delMenu(param)
    },
    onCancel() {},
  })
}
const deleteMenu = (text: MenuTypeState) => {
  if (text.id !== undefined) {
    const param: number[] = []
    param.push(text.id)
    deleteInfo(param, t('base.sure_deleted_it'))
  }
}
const batchDeletionMenu = () => {
  const param: number[] = []
  if (selectMenus.value.length > 0) {
    selectMenus.value.forEach((rs) => {
      if (rs.id) param.push(rs.id)
    })
    deleteInfo(param, t('base.sure_deleted_it'))
  }
}
// 表格列表
const columns = [
  {
    title: t('columns.name'),
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: t('columns.component'),
    dataIndex: 'component',
    key: 'component',
  },
  {
    title: t('columns.router'),
    dataIndex: 'router',
    key: 'router',
  },
  {
    title: t('columns.path'),
    dataIndex: 'path',
    key: 'path',
  },
  {
    title: t('columns.permission'),
    dataIndex: 'permission',
    key: 'permission',
  },
  {
    title: t('columns.state'),
    dataIndex: 'state',
    key: 'state',
  },
  {
    title: t('columns.type'),
    dataIndex: 'type',
    key: 'type',
  },
  {
    title: t('columns.sort'),
    dataIndex: 'sort',
    key: 'sort',
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
    width: 100,
  },
]
const rowSelection = ref({
  checkStrictly: false,
  onChange: (selectedRowKeys: (string | number)[], selectedRows: MenuTypeState[]) => {
    selectMenus.value = selectedRows
  },
})
</script>
<style lang="scss" scoped></style>
