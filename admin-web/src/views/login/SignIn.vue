<template>
  <div class="sigin-in">
    <a-form
      :model="formState"
      name="normal_login"
      class="login-form"
      hideRequiredMark=""
      @finish="onFinish"
      @finishFailed="onFinishFailed"
    >
      <a-form-item name="username" :rules="[{ required: true, message: $t('input_username') }]">
        <a-input v-model:value="formState.username" :placeholder="$t('user_name')">
          <template #prefix>
            <UserOutlined class="site-form-item-icon" />
          </template>
        </a-input>
      </a-form-item>

      <a-form-item name="password" :rules="[{ required: true, message: $t('input_password') }]">
        <a-input-password v-model:value="formState.password" :placeholder="$t('user_password')">
          <template #prefix>
            <LockOutlined class="site-form-item-icon" />
          </template>
        </a-input-password>
      </a-form-item>

      <a-form-item>
        <div class="forgot-language">
          <span class="forgot-password" @click="forgotPassword()">{{ $t('forgot_password') }}</span>
          <span class="login-language" @click="changeLanguage()">
            <LanguageIcon width="16px" height="16px" />
          </span>
        </div>
      </a-form-item>

      <a-form-item>
        <a-form-item name="remember" no-style>
          <a-checkbox v-model:checked="formState.remember"
            ><span class="rember-me">{{ $t('remember_me') }}</span></a-checkbox
          >
        </a-form-item>
      </a-form-item>

      <a-form-item>
        <a-button :disabled="disabled" type="primary" html-type="submit" class="login-form-button">
          {{ $t('login_in') }}
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n'
import { reactive, computed } from 'vue'
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'
import LanguageIcon from '@/components/icons/LanguageIcon.vue'

interface Props {
  page: string
}
//跳转到忘记密码页面
const emit = defineEmits<{
  'update:page': [value: string]
}>()
const forgotPassword = () => emit('update:page', 'email')
//多语系处理
const { locale, t } = useI18n()
const changeLanguage = () => {
  locale.value = locale.value === 'zh' ? 'en' : 'zh' // 切换语言逻辑
}

interface FormState {
  username: string
  password: string
  remember: boolean
}

const formState = reactive<FormState>({
  username: '',
  password: '',
  remember: true,
})
const onFinish = (values: unknown) => {
  console.log('Success:', values, formState)
}
const onFinishFailed = (errorInfo: unknown) => {
  console.log('Failed:', errorInfo)
}
const disabled = computed(() => {
  return !(formState.username && formState.password)
})
</script>
<style lang="scss" scoped>
.sigin-in {
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
