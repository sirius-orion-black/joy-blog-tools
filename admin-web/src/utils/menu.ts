import type { MenuTypeState } from '@/types/menuType'

// 递归查找首个 type=2 的菜单项
export function findFirstMenu(menuList: MenuTypeState[]): MenuTypeState | null {
  for (const item of menuList) {
    if (item.type === 2) return item // 找到目标菜单项

    if (item.children?.length) {
      const result = findFirstMenu(item.children)
      if (result) return result // 在子菜单中找到目标
    }
  }
  return null
}
