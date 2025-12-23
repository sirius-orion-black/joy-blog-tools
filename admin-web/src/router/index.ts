import { createRouter, createWebHistory } from 'vue-router'

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
      path: '/email',
      name: 'email',
      component: () => import('../views/email/EmailView.vue'),
    },
  ],
})
router.beforeEach(async (to, from) => {
  console.log(32131231, to.name)
  // if (to.name !== 'login') {
  //   // 将用户重定向到登录页面
  //   return { name: 'login' }
  // }
})
export default router
