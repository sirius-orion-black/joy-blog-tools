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
  id?: number
  state?: number
  email?: string
  phone?: string
  nickname?: string
  username?: string
  password?: string
}

export interface UserDrawerState {
  type: string
  show: boolean
  width: number
  text?: UserTypeState
}

export interface UserMenuState {
  userId: number
  menuId?: number[]
}
