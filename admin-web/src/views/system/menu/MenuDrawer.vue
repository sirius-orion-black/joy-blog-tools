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
    <a-form :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item :label="$t('drawer.superior_menu')" :rules="[{ required: true, message: '' }]">
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
      </a-form-item>

      <a-form-item :label="$t('drawer.menu_type')" :rules="[{ required: true, message: '' }]">
        <a-radio-group v-model:value="menuData.type">
          <a-radio-button :value="1">{{ $t('columns.directory') }}</a-radio-button>
          <a-radio-button :value="2">{{ $t('columns.menu') }}</a-radio-button>
          <a-radio-button :value="3">{{ $t('columns.permission') }}</a-radio-button>
        </a-radio-group>
      </a-form-item>

      <a-form-item :label="$t('drawer.menu_icon')" v-show="menuData.type !== 3">
        <div class="menu-draw-icon">
          <a-button @click="showIcons()">
            <IconFont v-if="menuData.icon" :type="menuData.icon" />{{ $t('drawer.icon_select') }}<DownOutlined />
          </a-button>
          <div class="icon-list base-bg-shadow" v-show="showIconSelect">
            <a-flex wrap="wrap" gap="small" justify="space-between">
              <div class="icon-content base-bg" v-for="value in menuIcons" :key="value.iconKey" @click="setIcon(value.iconKey + '')">
                <IconFont :type="value.iconKey" style="font-size: 32px" />
              </div>
            </a-flex>
          </div>
        </div>
      </a-form-item>

      <a-form-item :label="$t('drawer.menu_name')" :rules="[{ required: true, message: '' }]">
        <a-input v-model:value="menuData.name" placeholder="exp:system_manage" />
      </a-form-item>

      <div v-show="menuData.type === 2">
        <a-form-item :label="$t('drawer.component_name')" :rules="[{ required: true, message: '' }]">
          <a-input v-model:value="menuData.router" placeholder="exp:systemManage" />
        </a-form-item>

        <a-form-item :label="$t('drawer.component_path')" :rules="[{ required: true, message: '' }]">
          <a-input v-model:value="menuData.component" placeholder="exp:/home/DashBoard" />
        </a-form-item>

        <a-form-item :label="$t('drawer.component_link')" :rules="[{ required: true, message: '' }]">
          <a-input v-model:value="menuData.path" placeholder="exp:/home/DashBoard" />
        </a-form-item>
      </div>

      <a-form-item :label="$t('drawer.component_description')">
        <a-input v-model:value="menuData.description" :placeholder="`exp:${$t('menu.system_manage')}`" />
      </a-form-item>

      <a-form-item :label="$t('drawer.component_permission')" :rules="[{ required: true, message: '' }]" v-show="menuData.type === 3">
        <a-radio-group v-model:value="menuData.permission">
          <a-radio-button value="add">{{ $t('menu.add') }}</a-radio-button>
          <a-radio-button value="edit">{{ $t('menu.edit') }}</a-radio-button>
          <a-radio-button value="delete">{{ $t('menu.delete') }}</a-radio-button>
        </a-radio-group>
      </a-form-item>

      <a-form-item :label="$t('drawer.component_state')" :rules="[{ required: true, message: '' }]">
        <a-radio-group v-model:value="menuData.state">
          <a-radio-button :value="1">{{ $t('columns.normal') }}</a-radio-button>
          <a-radio-button :value="2">{{ $t('columns.disabled') }}</a-radio-button>
        </a-radio-group>
      </a-form-item>
      <a-form-item :label="$t('drawer.is_external')" :rules="[{ required: true, message: '' }]" v-show="menuData.type === 2">
        <a-radio-group v-model:value="menuData.isExternal">
          <a-radio-button :value="1">{{ $t('columns.yes') }}</a-radio-button>
          <a-radio-button :value="2">{{ $t('columns.no') }}</a-radio-button>
        </a-radio-group>
      </a-form-item>
      <a-form-item :label="$t('drawer.component_sorting')">
        <a-input-number v-model:value="menuData.sort" />
      </a-form-item>
    </a-form>

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
import { message } from 'ant-design-vue'
import { useI18n } from 'vue-i18n'

import { menuStore } from '@/stores/menu'

import type { MenuTypeState, MenuParentState, MenuIconState } from '@/types/menuType'

const menu = menuStore()
const { t } = useI18n()

const props = defineProps<{
  title: string
  list: MenuTypeState
}>()

const menuIcons = computed(() => {
  return menu.menuIcons?.map(
    (icon: { iconKey: string }): MenuIconState => ({
      iconKey: icon.iconKey, // 保留 configValue
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
  if (menuData.value.type === 1 && !menuData.value.name) {
    message.error(t('drawer.input_empty'))
    return
  } else if (
    menuData.value.type === 2 &&
    (!menuData.value.name || !menuData.value.component || !menuData.value.path || !menuData.value.router || !menuData.value.isExternal)
  ) {
    message.error(t('drawer.input_empty'))
    return
  } else if (menuData.value.type === 3 && (!menuData.value.name || !menuData.value.permission)) {
    message.error(t('drawer.input_empty'))
    return
  }
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
