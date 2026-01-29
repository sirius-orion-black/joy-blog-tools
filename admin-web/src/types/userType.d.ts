export interface UserSearchTypeState {
  // 后台用户search类型
  username: string
  email: string
  phone: string
  state: number | null
  page: number
  size: number
}

export interface UserTypeState {
  //用户修改类型
  id?: number
  state?: number
  email?: string
  phone?: string
  nickname?: string
  username?: string
  password?: string
}

export interface UserDrawerState {
  //弹窗类型
  type: string
  show: boolean
  width: number
  text?: UserTypeState
}

export interface UserMenuState {
  //用户菜单关联类型
  userId: number
  menuId?: number[]
}
