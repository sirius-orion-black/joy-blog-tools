import service from '../utils/request'
import type { UserTypeState, UserMenuState, UserSearchTypeState } from '@/types/userType'

export default {
  getList: (data: UserSearchTypeState) => service.get('/admin/user/getList', { params: data }),
  add: (data: UserTypeState) => service.post('/admin/user/add', data),
  edit: (data: UserTypeState) => service.post('/admin/user/edit', data),
  delete: (data: number[]) => service.post('/admin/user/delete', data),
  isBanned: (data: UserTypeState[]) => service.post('/admin/user/isBanned', data),
  getPermission: (data: UserMenuState) => service.get('/admin/user/getPermission', { params: data }),
  editPermission: (data: UserMenuState) => service.post('/admin/user/editPermission', data),
  getMenuList: () => service.get('/admin/user/menuList'),
}
