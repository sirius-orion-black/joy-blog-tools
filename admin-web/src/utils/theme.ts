import { localCache } from './storage'
// 定义主题类型
export type ThemeType = 'light' | 'dark'

// 初始化主题配置
export const initTheme = (): ThemeType => {
  // 优先读取用户本地存储的主题偏好
  const theme = localCache.getCache('theme') as ThemeType
  if (theme) {
    return theme
  }
  // 无存储则根据系统偏好自动判断
  return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'
}
// 应用主题并设置body class
export const applyTheme = (theme: ThemeType) => {
  // 移除主题类名
  document.body.classList.remove('theme-light', 'theme-dark')
  // 添加对应主题类名
  document.body.classList.add(`theme-${theme}`)
  // 保存主题到本地存储
  localCache.setCache('theme', theme)
}
// 切换主题
export const changeTheme = () => {
  // 获取当前主题
  const currentTheme = localCache.getCache('theme') as ThemeType | null

  // 如果当前有主题，则切换到相反的主题；如果没有则初始化
  let newTheme: ThemeType
  if (currentTheme) {
    newTheme = currentTheme === 'light' ? 'dark' : 'light'
  } else {
    // 如果没有缓存的主题，根据系统偏好初始化
    newTheme = window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'
    // 然后切换到相反的主题
    newTheme = newTheme === 'light' ? 'dark' : 'light'
  }

  // 应用新主题
  applyTheme(newTheme)
}
// 获取当前主题
export const getCurrentTheme = (): ThemeType => {
  return (localCache.getCache('theme') as ThemeType) || initTheme()
}
