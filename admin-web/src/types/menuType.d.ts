import type { JSX } from 'vue/jsx-runtime'

export interface MenuTypeState {
  //菜单类型
  id?: number
  name?: string //菜单名称
  icon?: string //icon
  permission?: string //权限标识符
  router?: string //路由
  component?: string //组件
  sort?: number //排序
  isExternal?: number //外链： 1 是 2 否
  parentId?: number //父级id
  path?: string //url
  type?: number //类型 1 directory 2 menu 3 permission
  state?: number //状态 1 正常 2 禁用
  description?: string //描述
  children?: MenuTypeState[]
}
export interface MenuParentState {
  // 定义节点类型
  name: string
  id: number
  children?: MenuParentState[] // 可选，因为可能没有children
}

export interface MenuIconState {
  //menu icon
  id?: number
  name?: string
  iconKey?: string
}

export interface MenuItemState {
  //左侧菜单类型
  key: number
  icon: () => JSX.Element
  label: string
  path: string
  title?: string
  name: string
  children?: MenuItemState[]
}

export interface MenuStackItemState {
  //顶部菜单类型
  path: string
  name: string
  key: number
}
export interface MenuStackState {
  //顶部菜单类型
  current: MenuStackItemState | null
  items: MenuStackItemState[] | null | undefined
}
