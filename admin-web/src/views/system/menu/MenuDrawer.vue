<template>
  <a-drawer
    :title="$t(props.title)"
    :width="520"
    :open="menu.menuDraw"
    :closable="false"
    :body-style="{ paddingBottom: '80px' }"
    :footer-style="{ textAlign: 'right' }"
    @close="onClose"
  >
    <a-row align="middle" :gutter="[16, 16]" style="margin-bottom: 15px">
      <a-col :span="5" style="text-align: right">{{ $t('drawer.superior_menu') }}</a-col>
      <a-col :span="19">
        <a-tree-select
          v-model:value="menuData.parentId"
          show-search
          style="width: 100%"
          :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
          placeholder="Please select"
          allow-clear
          tree-default-expand-all
          :tree-data="treeData"
          tree-node-filter-prop="label"
          :fieldNames="{ children: 'children', label: 'name', value: 'id' }"
        >
          <template #title="{ name }"> {{ $t(`menu.${name}`) }} </template>
        </a-tree-select>
      </a-col>
    </a-row>

    <a-row align="middle" :gutter="[16, 16]" style="margin-bottom: 15px">
      <a-col :span="5" style="text-align: right">{{ $t('drawer.menu_type') }}</a-col>
      <a-col :span="19">
        <a-radio-group v-model:value="menuData.type">
          <a-radio-button :value="1">{{ $t('columns.directory') }}</a-radio-button>
          <a-radio-button :value="2">{{ $t('columns.menu') }}</a-radio-button>
          <a-radio-button :value="3">{{ $t('columns.permission') }}</a-radio-button>
        </a-radio-group>
      </a-col>
    </a-row>

    <a-row align="middle" :gutter="[16, 16]" style="margin-bottom: 15px" v-show="menuData.type !== 3">
      <a-col :span="5" style="text-align: right">{{ $t('drawer.menu_icon') }}</a-col>
      <a-col :span="19">
        <div class="menu-draw-icon base-day">
          <a-button @click="showIcons()">
            <IconFont v-if="menuData.icon" :type="menuData.icon" />{{ $t('drawer.icon_select') }}<DownOutlined />
          </a-button>
          <div class="icon-list base-bg-shadow" v-show="showIconSelect">
            <a-flex wrap="wrap" gap="small" justify="space-between">
              <div class="icon-content base-bg" v-for="value in menuIcoons" :key="value.configValue" @click="setIcon(value.configValue + '')">
                <IconFont :type="value.configValue" style="font-size: 32px" />
              </div>
            </a-flex>
          </div>
        </div>
      </a-col>
    </a-row>

    <a-row align="middle" :gutter="[16, 16]" style="margin-bottom: 15px">
      <a-col :span="5" style="text-align: right">{{ $t('drawer.menu_name') }}</a-col>
      <a-col :span="19">
        <a-input v-model:value="menuData.name" placeholder="exp:system_manage" />
      </a-col>
    </a-row>

    <div v-show="menuData.type === 2">
      <a-row align="middle" :gutter="[16, 16]" style="margin-bottom: 15px">
        <a-col :span="5" style="text-align: right">{{ $t('drawer.component_name') }}</a-col>
        <a-col :span="19">
          <a-input v-model:value="menuData.router" placeholder="exp:systemManage" />
        </a-col>
      </a-row>

      <a-row align="middle" :gutter="[16, 16]" style="margin-bottom: 15px">
        <a-col :span="5" style="text-align: right">{{ $t('drawer.component_path') }}</a-col>
        <a-col :span="19">
          <a-input v-model:value="menuData.component" placeholder="exp:/home/DashBoard" />
        </a-col>
      </a-row>

      <a-row align="middle" :gutter="[16, 16]" style="margin-bottom: 15px">
        <a-col :span="5" style="text-align: right">{{ $t('drawer.component_link') }}</a-col>
        <a-col :span="19">
          <a-input v-model:value="menuData.path" placeholder="exp:/home/DashBoard" />
        </a-col>
      </a-row>
    </div>

    <a-row align="middle" :gutter="[16, 16]" style="margin-bottom: 15px">
      <a-col :span="5" style="text-align: right">{{ $t('drawer.component_description') }}</a-col>
      <a-col :span="19">
        <a-input v-model:value="menuData.description" :placeholder="`exp:${$t('menu.system_manage')}`" />
      </a-col>
    </a-row>

    <a-row align="middle" :gutter="[16, 16]" style="margin-bottom: 15px" v-show="menuData.type === 3">
      <a-col :span="5" style="text-align: right">{{ $t('drawer.component_permission') }}</a-col>
      <a-col :span="19">
        <a-radio-group v-model:value="menuData.permission">
          <a-radio-button value="add">{{ $t('menu.add') }}</a-radio-button>
          <a-radio-button value="edit">{{ $t('menu.edit') }}</a-radio-button>
          <a-radio-button value="delete">{{ $t('menu.delete') }}</a-radio-button>
        </a-radio-group>
      </a-col>
    </a-row>

    <a-row align="middle" :gutter="[16, 16]" style="margin-bottom: 15px">
      <a-col :span="5" style="text-align: right">{{ $t('drawer.component_state') }}</a-col>
      <a-col :span="19">
        <a-radio-group v-model:value="menuData.state">
          <a-radio-button :value="1">{{ $t('columns.normal') }}</a-radio-button>
          <a-radio-button :value="2">{{ $t('columns.disabled') }}</a-radio-button>
        </a-radio-group>
      </a-col>
    </a-row>

    <a-row align="middle" :gutter="[16, 16]" style="margin-bottom: 15px" v-show="menuData.type === 2">
      <a-col :span="5" style="text-align: right">{{ $t('drawer.is_external') }}</a-col>
      <a-col :span="19">
        <a-radio-group v-model:value="menuData.isExternal">
          <a-radio-button :value="1">{{ $t('columns.yes') }}</a-radio-button>
          <a-radio-button :value="2">{{ $t('columns.no') }}</a-radio-button>
        </a-radio-group>
      </a-col>
    </a-row>

    <a-row align="middle" :gutter="[16, 16]" style="margin-bottom: 15px">
      <a-col :span="5" style="text-align: right">{{ $t('drawer.component_sorting') }}</a-col>
      <a-col :span="19">
        <a-input-number v-model:value="menuData.sort" />
      </a-col>
    </a-row>

    <template #extra>
      <a-space>
        <a-button type="primary" @click="onSubmit">{{ $t('menu.submit') }}</a-button>
      </a-space>
    </template>
  </a-drawer>
</template>
<script lang="ts" setup>
import { reactive, computed, ref } from 'vue'
import { DownOutlined } from '@ant-design/icons-vue'

import { menuStore } from '@/stores/menu'

import type { MenuTypeState, MenuParentState, MenuIconState } from '@/types/menuType'

const menu = menuStore()

const props = defineProps<{
  title: string
  list: MenuTypeState
}>()

const menuIcoons = computed(() => {
  return menu.menuIcons?.map(
    (icon: { configValue: string }): MenuIconState => ({
      configValue: icon.configValue, // 保留 configValue
    }),
  )
})

const menuData = computed(() => {
  return reactive<MenuTypeState>({
    ...props.list,
  })
})

const treeData = computed(() => {
  // 递归处理函数
  const menuNode = (menu: MenuTypeState): MenuParentState => {
    const result: MenuParentState = {
      name: menu.name ?? '',
      id: menu.id ?? 0,
    }
    // 递归处理非空子节点
    if (Array.isArray(menu.children) && menu.children.length > 0) {
      result.children = menu.children.map(menuNode)
    }
    return result
  }
  const cleanList = (menu.menuList || []).map(menuNode)
  console.log(cleanList, menu.menuList)
  return [
    {
      id: 0,
      name: 'root',
      children: cleanList,
    },
  ]
})

const showIconSelect = ref<boolean>(false)

const showIcons = () => {
  showIconSelect.value = !showIconSelect.value
}

const setIcon = (val: string) => {
  showIconSelect.value = false
  menuData.value.icon = val
}

const onClose = () => {
  menu.setMenuDraw(false)
  showIconSelect.value = false
}
const onSubmit = () => {
  if (!menuData.value.id || menuData.value.id === 0) menu.addMenu(menuData.value)
  else menu.editMenu(menuData.value)
  menu.setMenuDraw(false)
}
</script>
<style lang="scss" scoped>
.menu-draw-icon {
  position: relative;
  .icon-list {
    padding: 8px;
    z-index: 9;
    left: 0;
    right: 0;
    position: absolute;
    transition: transform 0.5s ease;
    .icon-content {
      cursor: pointer;
      padding: 15px;
    }
  }
}
</style>
