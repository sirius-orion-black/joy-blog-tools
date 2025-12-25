import { createRouter, createWebHistory } from 'vue-router'
import { userLoginStore } from '@/stores/login'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/login/LoginIn.vue'),
    },
    {
      path: '/',
      name: 'home',
      component: () => import('../views/home/HomeView.vue'),
    },
    {
      path: '/systemManage/sysUser',
      name: 'sysUser',
      component: () => import('../views/system/user/SysUser.vue'),
    },
    {
      path: '/systemManage/sysMenu',
      name: 'SysMenu',
      component: () => import('../views/system/menu/SysMenu.vue'),
    },
    {
      path: '/email',
      name: 'email',
      component: () => import('../views/email/EmailView.vue'),
    },
  ],
})
router.beforeEach(async (to, from) => {
  const loginStore = userLoginStore()

  // 如果 token 不存在，尝试获取用户信息
  if (!loginStore.token) {
    await loginStore.getUser()
  }
  // 登录状态判断
  const isLogin = to.name !== 'login'
  const hasToken = !!loginStore.token

  // 根据目标路由和登录状态进行重定向
  if (isLogin && !hasToken) {
    // 需要登录但未登录，重定向到登录页
    return { name: 'login' }
  } else if (!isLogin && hasToken) {
    // 访问登录页但已登录，重定向到首页
    return { name: 'home' }
  }
})
export default router
