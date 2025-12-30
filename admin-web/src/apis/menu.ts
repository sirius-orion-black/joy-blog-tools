import service from '../utils/request'
import type { MenuType } from '@/types/MenuType'

export default {
  getList: () => service.get('/admin/menu/getList'),
  getMenuIcons: () => service.get('/admin/menu/icons'),
  addMenu: (data: MenuType) => service.post('/admin/menu/add', data),
  editMenu: (data: MenuType) => service.post('/admin/menu/edit', data),
  delMenu: (data: number[]) => service.post('/admin/menu/delete', data),
}
