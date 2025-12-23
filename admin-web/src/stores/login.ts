import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

import loginApi from '@/apis/login'
import { localCache, sessionCache } from '@/untils/storage'

export const userLoginStore = defineStore('userLogin', () => {
  const user = ref<Record<string, string>>()
  const captcha = ref<Record<string, string>>()
  const showCaptcha = ref<boolean>(false)
  const isShowCaptcha = computed(() => showCaptcha.value)
  const isCaptchaState = ref<boolean>(false)
  const token = ref<string>()

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

  async function signin(formState: object): Promise<Record<string, string>> {
    showCaptcha.value = false
    const res = await loginApi.login(formState)
    return res.data
  }

  function setUser(remember: boolean, useInfo: Record<string, string>) {
    user.value = useInfo
    token.value = user.value!.token
    if (remember) localCache.setCache('user', useInfo)
    else sessionCache.setCache('user', useInfo)
  }

  function getUser() {
    user.value = localCache.getCache<Record<string, string>>('user') ?? sessionCache.getCache<Record<string, string>>('user')
    token.value = user.value?.token ?? undefined
  }

  return {
    user,
    captcha,
    isShowCaptcha,
    showCaptcha,
    isCaptchaState,
    token,
    getCaptcha,
    setIsCaptchaState,
    signin,
    setUser,
    getUser,
  }
})
