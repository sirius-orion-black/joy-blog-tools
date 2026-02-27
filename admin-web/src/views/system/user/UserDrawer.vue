<template>
  <a-drawer :title="getTitle(user.userDrawer.type)" :open="user.userDrawer.show" :width="user.userDrawer.width" :closable="false" @close="onClose">
    <template v-if="user.userDrawer.type === 'add'">
      <a-form :model="formState" name="basic" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }" autocomplete="off" @finish="onSubmit">
        <a-form-item :label="$t('base.user_nickname')" name="nickname" :rules="[{ required: true }]">
          <a-input v-model:value="formState.nickname" :placeholder="$t('base.input_user_nickname')" />
        </a-form-item>
        <a-form-item
          :label="$t('base.user_name')"
          name="username"
          :rules="[{ required: true, message: $t('request.username_between_5_16'), pattern: '^[A-Za-z]{5,16}$' }]"
        >
          <a-input v-model:value="formState.username" :placeholder="$t('base.input_username')" />
        </a-form-item>

        <a-form-item
          :label="$t('base.user_phone')"
          name="phone"
          :rules="[{ required: true, message: $t('request.phone_number_incorrect'), pattern: '^1[3-9]\\d{9}$' }]"
        >
          <a-input v-model:value="formState.phone" :placeholder="$t('base.input_user_phone')" />
        </a-form-item>

        <a-form-item
          :label="$t('base.user_email')"
          name="email"
          :rules="[{ required: true, message: $t('request.email_format_incorrect'), validator: validateEmail, trigger: 'change' }]"
        >
          <a-input v-model:value="formState.email" :placeholder="$t('base.input_email')" />
        </a-form-item>
        <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
          <a-button type="primary" html-type="submit">{{ $t('menu.submit') }}</a-button>
        </a-form-item>
      </a-form>
    </template>

    <template v-else-if="user.userDrawer.type === 'edit'">
      <a-row align="middle" :gutter="[16, 16]" style="margin-bottom: 15px">
        <a-col :span="7" style="text-align: right">{{ $t('base.user_name') }}</a-col>
        <a-col :span="17">
          {{ editUserInfo.username }}
        </a-col>
      </a-row>
      <a-row align="middle" :gutter="[16, 16]" style="margin-bottom: 15px">
        <a-col :span="7" style="text-align: right">{{ $t('base.user_nickname') }}</a-col>
        <a-col :span="17">
          {{ editUserInfo.nickname }}
        </a-col>
      </a-row>

      <a-row align="middle" :gutter="[16, 16]" style="margin-bottom: 15px">
        <a-col :span="7" style="text-align: right">{{ $t('base.user_email') }}</a-col>
        <a-col :span="17">
          <a-input v-model:value="editUserInfo.email" />
        </a-col>
      </a-row>
      <a-row align="middle" :gutter="[16, 16]" style="margin-bottom: 15px">
        <a-col :span="7" style="text-align: right">{{ $t('base.user_phone') }}</a-col>
        <a-col :span="17">
          <a-input v-model:value="editUserInfo.phone" />
        </a-col>
      </a-row>
    </template>

    <template v-else>
      <a-tree
        v-if="!!menu.menuList && menu.menuList.length > 0"
        :checked-keys="checkedKeys"
        checkable
        :tree-data="menu.menuList"
        :fieldNames="{ title: 'name', key: 'id' }"
        :defaultExpandAll="true"
        :checkStrictly="true"
        @check="onCheck"
      >
        <template #title="{ name, parentId }">
          <span v-if="parentId === 0" style="color: #1890ff">{{ $t('menu.' + name) }}</span>
          <template v-else>{{ $t('menu.' + name) }}</template>
        </template>
      </a-tree>
    </template>
    <template #extra>
      <a-space>
        <a-button v-if="user.userDrawer.type !== 'add'" type="primary" @click="onSubmit">{{ $t('menu.submit') }}</a-button>
      </a-space>
    </template>
  </a-drawer>
</template>

<script lang="ts" setup>
import { computed, nextTick, reactive, h, ref, watch } from 'vue'
import { userStore } from '@/stores/user'
import { menuStore } from '@/stores/menu'
import { message, Modal } from 'ant-design-vue'
import { useI18n } from 'vue-i18n'

import type { TreeProps } from 'ant-design-vue'
import type { UserTypeState, UserMenuState } from '@/types/userType'
import type { MenuTypeState } from '@/types/menuType'

const user = userStore()
const menu = menuStore()
const { t } = useI18n()

const emit = defineEmits<{ 'update:list': [] }>()
//编辑管理人员数据
const editUserInfo = computed(() => {
  return {
    id: user.userDrawer.text?.id,
    email: user.userDrawer.text?.email,
    phone: user.userDrawer.text?.phone,
    nickname: user.userDrawer.text?.nickname,
    username: user.userDrawer.text?.username,
  } as UserTypeState
})

const getTitle = (vl: string) => {
  switch (vl) {
    case 'add':
      return t('drawer.new_user')
    case 'edit':
      return t('drawer.edit_user')
    default:
      return t('drawer.edit_user_permission')
  }
}

const onClose = () => {
  user.setUserDrawer('', false, 0)
}
//新增管理人员表单数据
const formState = reactive<UserTypeState>({
  username: '',
  nickname: '',
  phone: '',
  email: '',
})
//邮箱校验
const validateEmail = async () => {
  if (formState.email !== '' && formState.email !== undefined) {
    const emailRegex = /^[a-zA-Z0-9_+&*-]+(?:\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,7}$/
    const qqEmailRegex = /^[a-zA-Z0-9_+&*-]+@qq\.com$/
    if (emailRegex.test(formState.email) || qqEmailRegex.test(formState.email)) return Promise.resolve()
    else return Promise.reject(t('request.email_format_incorrect'))
  } else return Promise.reject(t('request.email_format_incorrect'))
}

//权限选中列表
const checkedKeys = ref<number[]>([])

// 构建父节点映射（计算属性自动更新）
const parentMap = computed(() => {
  const map = new Map<number, number>()
  const traverse = (nodes: MenuTypeState[]) => {
    nodes.forEach((node) => {
      if (node.children) {
        node.children.forEach((child) => map.set(child.id!, node.id!))
        traverse(node.children)
      }
    })
  }
  traverse(menu.menuList || [])
  return map
})

const onCheck: TreeProps['onCheck'] = (_, { node, checked }) => {
  const nodeId = node.id as number
  const newKeys = new Set(checkedKeys.value)

  if (checked) {
    // 选中节点及父链
    newKeys.add(nodeId)
    let current = nodeId
    while (parentMap.value.has(current)) {
      current = parentMap.value.get(current)!
      newKeys.add(current)
    }
  } else {
    // 移除节点及子树
    const removeIds = getSubtreeIds(menu.menuList || [], nodeId)
    removeIds.forEach((id) => newKeys.delete(id))
  }

  checkedKeys.value = Array.from(newKeys)
}
// 按需获取子树ID（递归实现）
const getSubtreeIds = (nodes: MenuTypeState[], targetId: number): number[] => {
  for (const node of nodes) {
    if (node.id === targetId) {
      return collectIds(node)
    }
    if (node.children) {
      const found = getSubtreeIds(node.children, targetId)
      if (found.length) return found
    }
  }
  return []
}
// 收集节点所有后代ID
const collectIds = (node: MenuTypeState): number[] => {
  return [node.id!, ...(node.children?.flatMap((child) => collectIds(child)) || [])]
}

const onSubmit = async () => {
  if (user.userDrawer.type === 'add') {
    const res: UserTypeState = await user.addUser(formState)
    nextTick(() => {
      emit('update:list')
    })
    Modal.info({
      title: t('base.copy_initial_password'),
      content: h('div', {}, [h('p', `${t('base.user_nickname')}:${res.nickname}`), h('p', `${t('base.user_password')}:${res.password}`)]),
      onOk() {},
    })
    console.log(res, 'successs')
  } else if (user.userDrawer.type === 'edit') {
    if (editUserInfo.value.email === user.userDrawer.text?.email && editUserInfo.value.phone === user.userDrawer.text?.phone) {
      message.warn(t('drawer.content_bee_modified'))
    } else {
      await user.editUser(editUserInfo.value)
      nextTick(() => {
        emit('update:list')
      })
    }
  } else {
    const params: UserMenuState = {
      userId: user.userDrawer.text?.id!,
      menuId: checkedKeys.value,
    }
    user.editPermission(params)
    checkedKeys.value = []
  }
  user.setUserDrawer('', false, 0)
}

// 监听API数据变化并初始化选中项
watch(
  () => user.permission,
  (newPermission) => {
    if (newPermission?.menuId) checkedKeys.value = newPermission.menuId
    else checkedKeys.value = []
  },
  { immediate: true },
)
</script>
