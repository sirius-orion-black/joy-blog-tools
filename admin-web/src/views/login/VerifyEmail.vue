<template>
  <div class="sigin-in">
    <a-form :model="formState" name="normal_login" class="login-form" hideRequiredMark="" @finish="onFinish">
      <a-form-item name="username" :rules="[{ required: true, message: $t('base.input_username'), pattern: '^[A-Za-z]{5,16}$' }]">
        <a-input v-model:value="formState.username" :placeholder="$t('base.user_name')">
          <template #prefix>
            <UserOutlined class="site-form-item-icon" />
          </template>
        </a-input>
      </a-form-item>

      <a-form-item name="email" :rules="[{ required: true, message: $t('base.input_email'), validator: validateEmail, trigger: 'change' }]">
        <a-input v-model:value="formState.email" :placeholder="$t('base.user_email')">
          <template #prefix>
            <IconFont type="icon-email" class="font-size-16" />
          </template>
          <template #suffix>
            <span class="email-code" @click="sendCode" v-show="formState.username && formState.email && isSendCodeTime === 0">
              {{ $t('base.verify_send') }}
            </span>
            <span class="count-down" v-show="isSendCodeTime > 0">{{ isSendCodeTime }}s</span>
          </template>
        </a-input>
      </a-form-item>

      <a-form-item name="code" :rules="[{ required: true, message: $t('base.input_email') }]">
        <a-input v-model:value="formState.code" :placeholder="$t('base.user_captcha')">
          <template #prefix>
            <IconFont type="icon-captcha" class="font-size-16" />
          </template>
        </a-input>
      </a-form-item>

      <a-form-item>
        <div class="sigin-language">
          <span class="login-in" @click="signIn()">{{ $t('base.login_in') }}</span>
          <span class="login-language" @click="changeLanguage()">
            <IconFont type="icon-language" class="font-size-16" />
          </span>
        </div>
      </a-form-item>
      <a-form-item>
        <a-form-item name="remember" no-style>
          <a-checkbox v-model:checked="formState.remember">
            <span class="rember-me">{{ $t('base.remember_me') }}</span>
          </a-checkbox>
        </a-form-item>
      </a-form-item>
      <a-form-item>
        <a-button :disabled="disabled" type="primary" html-type="submit" class="verify-form-button">
          {{ $t('base.email_login_in') }}
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n'
import { reactive, computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { UserOutlined } from '@ant-design/icons-vue'

import type { EmailState } from '@/types/loginType'
import type { MenuStackItemState } from '@/types/menuType'

import { userLoginStore } from '@/stores/login'
import { userStore } from '@/stores/user'
import { menuStore } from '@/stores/menu'
import { setLocale } from '@/i18n/i18n'
import { isNotEmptyObject } from '@/utils/objectCheck'
import { findFirstMenu } from '@/utils/menu'

const login = userLoginStore()
const user = userStore()
const menu = menuStore()
const { t } = useI18n()

const router = useRouter()

// 跳转到忘记密码页面
const emit = defineEmits<{
  'update:page': [value: string]
}>()
const signIn = () => emit('update:page', 'sigin')
// 多语系处理
const { locale } = useI18n()
const changeLanguage = () => {
  locale.value = locale.value === 'zhCN' ? 'enUS' : 'zhCN' // 切换语言逻辑
  setLocale(locale.value)
}
//邮箱校验
const validateEmail = async () => {
  if (formState.email !== '' && formState.email !== undefined) {
    const emailRegex = /^[a-zA-Z0-9_+&*-]+(?:\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,7}$/
    const qqEmailRegex = /^[a-zA-Z0-9_+&*-]+@qq\.com$/
    if (emailRegex.test(formState.email) || qqEmailRegex.test(formState.email)) return Promise.resolve()
    else return Promise.reject(t('request.email_format_incorrect'))
  } else return Promise.reject(t('request.email_format_incorrect'))
}
//邮箱登录表单
const formState = reactive<EmailState>({
  username: '',
  email: '',
  code: '',
  remember: true,
  loginType: 2,
  validTime: 0,
})

const isSendCodeTime = ref<number>(0)
let codeTimer: number | null = null // 用于存储定时器ID
//防止频繁获取邮箱验证码（间隔60秒）
const startCountdown = () => {
  if (isSendCodeTime.value > 0) return // 防止重复点击

  isSendCodeTime.value = 60
  codeTimer = window.setInterval(() => {
    isSendCodeTime.value--
    // 当时间归零时，清除定时器
    if (isSendCodeTime.value <= 0) {
      if (codeTimer) clearInterval(codeTimer)
      codeTimer = null
    }
  }, 1000)
}
//获取邮箱验证码
const sendCode = () => {
  const params: EmailState = {
    username: formState.username,
    email: formState.email,
  }
  login.sendCode(params).then((rs) => {
    if (rs.validTime) {
      formState.validTime = rs.validTime
      startCountdown()
    }
  })
}
//邮箱登录
const onFinish = async (values: EmailState) => {
  values.loginType = 2
  values.validTime = formState.validTime
  const res = await login.signin(formState) // 使用 await 等待 Promise 完成
  if (isNotEmptyObject(res)) {
    login.setUser(formState.remember ?? true, res)
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
  console.log('Success:', values, res, 312321)
}

//登录按钮控制
const disabled = computed(() => {
  return !(formState.username && formState.email && formState.code)
})
</script>
<style lang="scss" scoped>
.sigin-in {
  width: 390px;
  height: 416px;
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
  margin-top: -150px;
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
  :deep(.ant-input-prefix) {
    color: #000000;
  }
  :deep(.ant-input-affix-wrapper-status-error) {
    border-color: #ffffff !important;
  }
  :deep(.ant-form-item .ant-input-affix-wrapper-status-error .ant-form-item-explain-error),
  :deep(.ant-input-affix-wrapper-status-error .ant-input-prefix) {
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

  .email-code {
    cursor: pointer;
    color: #ffffff;
    &:hover {
      color: #ff7875;
    }
  }
  .count-down {
    color: #ffffff;
    width: 28px;
  }
  .sigin-language {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    .login-in {
      color: #000000;
      cursor: pointer;
    }
    .login-language {
      cursor: pointer;
      padding-left: 20px;
    }
  }
  .verify-form-button {
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
