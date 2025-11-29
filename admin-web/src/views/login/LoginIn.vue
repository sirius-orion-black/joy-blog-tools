<template>
  <div class="login-main">
    <div class="login-body">
      <div class="sigin-in">
        <a-form
          :model="formState"
          name="normal_login"
          class="login-form"
          hideRequiredMark=""
          @finish="onFinish"
          @finishFailed="onFinishFailed"
        >
          <a-form-item
            name="Username"
            :rules="[{ required: true, message: 'Please input your username!' }]"
          >
            <a-input v-model:value="formState.username" placeholder="Username">
              <template #prefix>
                <UserOutlined class="site-form-item-icon" />
              </template>
            </a-input>
          </a-form-item>

          <a-form-item
            name="Password"
            :rules="[{ required: true, message: 'Please input your password!' }]"
          >
            <a-input-password v-model:value="formState.password" placeholder="Password">
              <template #prefix>
                <LockOutlined class="site-form-item-icon" />
              </template>
            </a-input-password>
          </a-form-item>

          <a-form-item>
            <span class="forgot-password" href="">Forgot password</span>
          </a-form-item>

          <a-form-item>
            <a-form-item name="remember" no-style>
              <a-checkbox v-model:checked="formState.remember"
                ><span class="rember-me">Remember me</span></a-checkbox
              >
            </a-form-item>
          </a-form-item>

          <a-form-item>
            <a-button
              :disabled="disabled"
              type="primary"
              html-type="submit"
              class="login-form-button"
            >
              {{ $t('login.login_in') }}
            </a-button>
          </a-form-item>
        </a-form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useI18n } from 'vue-i18n'
const { locale, t } = useI18n() // 使用组合式API获取i18n实例的方法和属性
const changeLanguage = () => {
  locale.value = locale.value === 'zh' ? 'zh' : 'en' // 切换语言逻辑
}
import { reactive, computed } from 'vue'
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'
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
  console.log('Success:', values)
}

const onFinishFailed = (errorInfo: unknown) => {
  console.log('Failed:', errorInfo)
}
const disabled = computed(() => {
  return !(formState.username && formState.password)
})
</script>

<style lang="scss" scoped>
.login-main {
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  .login-body {
    width: 100%;
    height: 100%;
    position: relative;
    &::after {
      display: block;
      content: ' ';
      background-image: url('@/assets/img/login/login_bg.jpg');
      z-index: 1;
      background-size: 100%;
      background-repeat: no-repeat;
      opacity: 0.2;
      position: absolute;
      left: 0;
      top: 0;
      right: 0;
      bottom: 0;
    }
    .sigin-in {
      width: 360px;
      height: 520px;
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
      margin-left: -280px;
      margin-top: -280px;
      .ant-input-affix-wrapper {
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
      :deep(.ant-form-item .ant-input-affix-wrapper-status-error .ant-form-item-explain-error),
      :deep(.ant-input-affix-wrapper-status-error .ant-input-prefix),
      :deep(.ant-input-affix-wrapper-status-error .anticon.ant-input-password-icon) {
        color: #ffffff;
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
      .forgot-password {
        color: #000000;
      }
      .rember-me {
        color: #000000;
      }

      .login-form-button {
        width: 100%;
      }
    }
  }
}

// #544f4b
</style>
