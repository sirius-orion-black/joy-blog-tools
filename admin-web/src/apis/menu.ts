import service from '../utils/request'
import type { MenuTypeState } from '@/types/MenuType'

export default {
  getList: () => service.get('/admin/menu/getList'),
  getMenuIcons: () => service.get('/admin/menu/icons'),
  addMenu: (data: MenuTypeState) => service.post('/admin/menu/add', data),
  editMenu: (data: MenuTypeState) => service.post('/admin/menu/edit', data),
  delMenu: (data: number[]) => service.post('/admin/menu/delete', data),
}
