'use client';

import { localCache } from "./storage";

// 定义主题类型
export type ThemeType = "light" | "dark";

// 初始化主题配置
export const initTheme = (): ThemeType => {
  // 优先读取用户本地存储的主题偏好
  const theme = localCache.getCache("theme") as ThemeType;
  if (theme) {
    return theme;
  }
  // 服务端降级：无 window 时直接返回默认主题
  if (typeof window === 'undefined') {
    return 'light';
  }
  // 无存储则根据系统偏好自动判断
  return window.matchMedia("(prefers-color-scheme: dark)").matches
    ? "dark"
    : "light";
};

// 应用主题并设置body class
export const applyTheme = (theme: ThemeType) => {
  // 保存主题到本地存储（localCache 内部已有 SSR 降级，服务端静默跳过）
  localCache.setCache("theme", theme);
  // 服务端降级：无 document 时跳过 DOM 操作
  if (typeof document === 'undefined') return;
  // 移除主题类名
  document.body.classList.remove("theme-light", "theme-dark");
  // 添加对应主题类名
  document.body.classList.add(`theme-${theme}`);
};

// 切换主题
export const changeTheme = () => {
  // 获取当前主题
  const currentTheme = localCache.getCache("theme") as ThemeType | null;

  // 如果当前有主题，则切换到相反的主题；如果没有则初始化
  let newTheme: ThemeType;
  if (currentTheme) {
    newTheme = currentTheme === "light" ? "dark" : "light";
  } else {
    // 服务端降级：无 window 时默认 light
    if (typeof window === 'undefined') {
      newTheme = 'dark'; // light 的相反
    } else {
      // 如果没有缓存的主题，根据系统偏好初始化
      newTheme = window.matchMedia("(prefers-color-scheme: dark)").matches
        ? "dark"
        : "light";
      // 然后切换到相反的主题
      newTheme = newTheme === "light" ? "dark" : "light";
    }
  }

  // 应用新主题
  applyTheme(newTheme);
};

// 获取当前主题
export const getCurrentTheme = (): ThemeType => {
  // 检查是否在浏览器环境中
  if (typeof window === 'undefined') {
    // 服务端：返回默认主题，例如 light
    return 'light';
  }
  return (localCache.getCache("theme") as ThemeType) || initTheme();
};
