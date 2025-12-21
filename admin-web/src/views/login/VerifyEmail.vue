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
      <a-form-item name="Username" :rules="[{ required: true, message: $t('input_username') }]">
        <a-input v-model:value="formState.username" :placeholder="$t('user_name')">
          <template #prefix>
            <UserOutlined class="site-form-item-icon" />
          </template>
        </a-input>
      </a-form-item>

      <a-form-item name="Email" :rules="[{ required: true, message: $t('input_email') }]">
        <a-input v-model:value="formState.email" :placeholder="$t('user_email')">
          <template #prefix>
            <EmailIcon class="site-form-item-icon" width="14px" height="14px" />
          </template>
        </a-input>
      </a-form-item>

      <a-form-item>
        <div class="sigin-language">
          <span class="sign-in" @click="signIn()">{{ $t('login_in') }}</span>
          <span class="login-language" @click="changeLanguage()">
            <LanguageIcon width="16px" height="16px" />
          </span>
        </div>
      </a-form-item>
      <a-form-item>
        <a-button :disabled="disabled" type="primary" html-type="submit" class="verify-form-button">
          {{ $t('verify_email') }}
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from 'vue-i18n'
import { reactive, computed } from 'vue'
import { UserOutlined } from '@ant-design/icons-vue'
import LanguageIcon from '@/components/icons/LanguageIcon.vue'
import EmailIcon from '@/components/icons/EmailIcon.vue'

// interface Props {
//   page: string
// }
//跳转到忘记密码页面
const emit = defineEmits<{
  'update:page': [value: string]
}>()
const signIn = () => emit('update:page', 'sigin')
//多语系处理
const { locale } = useI18n()
const changeLanguage = () => {
  locale.value = locale.value === 'zh' ? 'en' : 'zh' // 切换语言逻辑
}

interface FormState {
  username: string
  email: string
}

const formState = reactive<FormState>({
  username: '',
  email: '',
})
const onFinish = (values: unknown) => {
  console.log('Success:', values)
}
const onFinishFailed = (errorInfo: unknown) => {
  console.log('Failed:', errorInfo)
}
const disabled = computed(() => {
  return !(formState.username && formState.email)
})
</script>
<style lang="scss" scoped>
.sigin-in {
  width: 360px;
  height: 300px;
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

  .sigin-language {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    .sign-in {
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
