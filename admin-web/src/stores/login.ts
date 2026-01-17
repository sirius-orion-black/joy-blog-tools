import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

import loginApi from '@/apis/login'

import { localCache, sessionCache } from '@/utils/storage'
import type { LoginState } from '@/types/LoginType'

export const userLoginStore = defineStore('userLogin', () => {
  const user = ref<Record<string, string>>()
  const captcha = ref<Record<string, string>>()
  const showCaptcha = ref<boolean>(false)
  const isShowCaptcha = computed(() => showCaptcha.value)
  const isCaptchaState = ref<boolean>(false)
  const token = ref<string>()

  function getCaptcha() {
    //获取滑块
    showCaptcha.value = true
    loginApi
      .getCaptcha()
      .then((rs) => {
        captcha.value = rs.data
        isCaptchaState.value = true
      })
      .catch(() => {
        showCaptcha.value = false
      })
  }

  function setIsCaptchaState(value: boolean) {
    //设置滑块重置
    isCaptchaState.value = value
  }

  async function signin(formState: LoginState): Promise<Record<string, string>> {
    //登录
    showCaptcha.value = false
    const res = await loginApi.login(formState)
    return res.data
  }

  function setUser(remember: boolean, useInfo: Record<string, string>) {
    //存储用户信息
    user.value = useInfo
    token.value = user.value!.token
    if (remember) localCache.setCache('user', useInfo)
    else sessionCache.setCache('user', useInfo)
    localCache.setCache('remember', remember)
  }

  function getUser() {
    //获取用户信息
    user.value = localCache.getCache<Record<string, string>>('user') ?? sessionCache.getCache<Record<string, string>>('user')
    token.value = user.value?.token ?? undefined
  }
  function cleanUserInfo() {
    token.value = ''
    localCache.clear()
    sessionCache.clear()
  }
  async function signout() {
    await loginApi.logout()
    cleanUserInfo()
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
    signout,
    cleanUserInfo,
  }
})
