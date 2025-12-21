import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

import loginApi from '@/apis/login'

export const userLoginStore = defineStore('userLogin', () => {
  const user = ref<Record<string, unknown>>()
  const captcha = ref<Record<string, string>>()
  const showCaptcha = ref<boolean>(false)
  const isShowCaptcha = computed(() => showCaptcha.value)
  const isCaptchaState = ref<boolean>(false)
  function getCaptcha() {
    showCaptcha.value = true
    loginApi
      .getCaptcha()
      .then((rs) => {
        captcha.value = rs.data
        isCaptchaState.value = true
        console.log(32131211111, rs, rs.data, captcha.value)
      })
      .catch(() => {
        showCaptcha.value = false
      })
  }

  function setIsCaptchaState(value: boolean) {
    isCaptchaState.value = value
  }

  function signin(formState: object) {
    showCaptcha.value = false
    loginApi
      .login(formState)
      .then((rs) => {
        console.log(22222222222, rs)
      })
      .catch((rs) => {
        console.log(333333333333, rs)
      })
    console.log(11111111111, formState)
  }

  return {
    user,
    captcha,
    isShowCaptcha,
    showCaptcha,
    isCaptchaState,
    getCaptcha,
    setIsCaptchaState,
    signin,
  }
})
