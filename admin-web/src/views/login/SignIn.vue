<template>
  <div class="sign-in">
    <a-form :model="formState" name="normal_login" class="login-form" hideRequiredMark="" @finish="onFinish">
      <a-form-item name="username" :rules="[{ required: true, message: $t('base.input_username'), pattern: '^[A-Za-z]{5,16}$' }]">
        <a-input v-model:value="formState.username" :placeholder="$t('base.user_name')">
          <template #prefix>
            <UserOutlined class="site-form-item-icon" />
          </template>
        </a-input>
      </a-form-item>

      <a-form-item name="password" :rules="[{ required: true, message: $t('base.input_password') }]">
        <a-input-password v-model:value="formState.password" :placeholder="$t('base.user_password')">
          <template #prefix>
            <LockOutlined class="site-form-item-icon" />
          </template>
        </a-input-password>
      </a-form-item>

      <a-form-item>
        <div class="forgot-language">
          <span class="forgot-password" @click="forgotPassword()">{{ $t('base.forgot_password') }}</span>
          <span class="login-language" @click="changeLanguage()">
            <IconFont type="icon-language" class="font-size-16" />
          </span>
        </div>
      </a-form-item>

      <a-form-item>
        <a-form-item name="remember" no-style>
          <a-checkbox v-model:checked="formState.remember"
            ><span class="rember-me">{{ $t('base.remember_me') }}</span></a-checkbox
          >
        </a-form-item>
      </a-form-item>

      <a-form-item>
        <a-button :disabled="disabled" type="primary" html-type="submit" class="login-form-button">
          {{ $t('base.login_in') }}
        </a-button>
      </a-form-item>
    </a-form>
    <ShowCaptcha @update:move="handleMoveUpdate" />
  </div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n'
import { reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'

import { userLoginStore } from '@/stores/login'
import { userStore } from '@/stores/user'
import { menuStore } from '@/stores/menu'

import type { LoginState } from '@/types/LoginType'
import type { MenuTypeState, MenuStackItemState } from '@/types/MenuType'

import ShowCaptcha from './ShowCaptcha.vue'

// 跳转到忘记密码页面
const emit = defineEmits<{
  'update:page': [value: string]
}>()
const forgotPassword = () => emit('update:page', 'email')
// 多语系处理
const { locale } = useI18n()
const changeLanguage = () => {
  locale.value = locale.value === 'zh' ? 'en' : 'zh' // 切换语言逻辑
}

const login = userLoginStore()
const user = userStore()
const menu = menuStore()

const router = useRouter()

const formState = reactive<LoginState>({
  username: 'admin',
  password: 'Aa123123?',
  remember: true,
  move: 0,
  loginType: 1,
  nonceStr: '',
})
const onFinish = () => {
  login.getCaptcha()
}
const disabled = computed(() => {
  return !(formState.username && formState.password)
})

// 递归查找首个 type=2 的菜单项
function findFirstMenu(menuList: MenuTypeState[]): MenuTypeState | null {
  for (const item of menuList) {
    if (item.type === 2) return item // 找到目标菜单项

    if (item.children?.length) {
      const result = findFirstMenu(item.children)
      if (result) return result // 在子菜单中找到目标
    }
  }
  return null
}
const handleMoveUpdate = async (value: number) => {
  formState.move = value
  formState.nonceStr = login.captcha!.nonceStr + ''
  const res = await login.signin(formState) // 使用 await 等待 Promise 完成
  login.setUser(formState.remember, res)
  const menus = await user.getMenuList(formState.remember)
  const firstMenu = findFirstMenu(menus)
  const item: MenuStackItemState = {
    path: firstMenu?.path + '',
    name: firstMenu?.name + '',
    key: firstMenu?.id as number,
  }
  menu.setMenuStack(item)
  router.push(firstMenu?.path + '')
}
</script>
<style lang="scss" scoped>
.sign-in {
  width: 360px;
  height: 360px;
  padding: 30px;
  box-shadow: 0px -6px 10px #ffffff;
  border-top: 3px solid #aba29b;
  border-bottom: 3px solid #2a2d2c;
  border-radius: 5px;
  background: linear-gradient(to bottom, #9e8a80, #555756);
  z-index: 9;
  position: absolute;
  left: 50%;
  top: 50%;
  margin-left: -180px;
  margin-top: -180px;
  :deep(.ant-input-affix-wrapper) {
    border-color: #514c4a;
    background-color: #514c4a;
    padding: 8px 11px;
  }
  :deep(input.ant-input) {
    background-color: #514c4a;
    color: #ffffff;
  }
  :deep(.ant-input)::placeholder {
    color: #000000;
  }
  :deep(.ant-form-item .ant-form-item-explain-error),
  :deep(.ant-input-prefix),
  :deep(.ant-input-affix-wrapper .anticon.ant-input-password-icon) {
    color: #000000;
  }
  :deep(.ant-input-affix-wrapper-status-error) {
    border-color: #ffffff !important;
  }
  :deep(.ant-form-item .ant-input-affix-wrapper-status-error .ant-form-item-explain-error),
  :deep(.ant-input-affix-wrapper-status-error .ant-input-prefix),
  :deep(.ant-input-affix-wrapper-status-error .anticon.ant-input-password-icon) {
    color: #ffffff !important;
  }

  :deep(.ant-checkbox-wrapper .ant-checkbox-inner) {
    border-color: #544e4c;
    padding: 5px;
    border-radius: 3px;
  }
  :deep(.ant-radio-wrapper-checked .ant-checkbox-inner),
  :deep(.ant-checkbox-wrapper-checked .ant-checkbox-inner) {
    background-color: #544e4c;
    color: #ffffff;
    border-color: #544e4c;
    border: 1px solid #544e4c;
  }
  :deep(.ant-checkbox-wrapper:hover .ant-checkbox-inner),
  :deep(.ant-checkbox:hover .ant-checkbox-inner) {
    background-color: transparent !important;
    border-color: #d9d9d9 !important;
  }
  :deep(.ant-checkbox-checked:after) {
    border: 1px solid #d9d9d9 !important;
  }
  .forgot-language {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    .forgot-password {
      color: #000000;
      cursor: pointer;
    }
    .login-language {
      cursor: pointer;
      padding-left: 20px;
    }
  }
  .rember-me {
    color: #000000;
  }
  .login-form-button {
    width: 100%;
    height: 46px;
    border-radius: 5px;
    font-size: 18px;
    background-color: #d2d2d2;
    box-shadow: 0px -1px 5px #d2d2d2;
    color: #000000;
  }
  .ant-btn-primary {
    background-color: #dee0e0;
    box-shadow: 0px -1px 5px #dee0e0;
    color: #000000;
  }
}
</style>
