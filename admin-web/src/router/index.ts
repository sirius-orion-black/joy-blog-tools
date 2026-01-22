import { createRouter, createWebHistory } from 'vue-router'
import { userLoginStore } from '@/stores/login'

import type { RouteRecordRaw } from 'vue-router'
import type { MenuTypeState } from '@/types/menuType'

import { localCache, sessionCache } from '@/utils/storage'

const modules = import.meta.glob('@/views/**/*.vue') // 获取所有视图组件

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/login/LoginIn.vue'),
    },
    {
      path: '/email',
      name: 'email',
      component: () => import('../views/email/EmailView.vue'),
    },
  ],
})
// 动态添加路由
export const addDynamicRoutes = async (menus: MenuTypeState[] = []) => {
  // console.log(modules, 123123123, Object.keys(modules))
  const routes = generateRoutes(menus)
  routes.forEach((route) => router.addRoute(route))
  // 最后添加404路由
  router.addRoute({
    path: '/:pathMatch(.*)*',
    component: () => import('@/views/other/NotFound.vue'),
  })
}
// 菜单数据生成路由配置
const generateRoutes = (menus: MenuTypeState[]): RouteRecordRaw[] => {
  return menus.flatMap((menu) => {
    const routes: RouteRecordRaw[] = []
    // 整理菜单项（type=2）
    if (menu.type === 2) {
      // 构建组件路径
      const componentPath = `/src/views${menu.component!.startsWith('/') ? menu.component : '/' + menu.component}.vue`
      // 获取组件
      const component = modules[componentPath]
      // console.log(777777, componentPath, component)
      if (!component) {
        console.error(`Component not found: ${componentPath}`)
        return []
      }

      routes.push({
        path: menu.path!,
        name: menu.router!,
        component,
        meta: { title: menu.name },
      } as RouteRecordRaw)
    }

    // 递归处理子项
    if (menu.children && menu.children.length > 0) {
      routes.push(...generateRoutes(menu.children))
    }

    return routes
  })
}

// 应用启动时恢复路由
const initDynamicRoutes = () => {
  const cached = localCache.getCache<Record<string, string>>('menuList') ?? sessionCache.getCache<Record<string, string>>('menuList')
  const menus = Array.isArray(cached) ? cached : []
  addDynamicRoutes(menus)
}

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

initDynamicRoutes()
