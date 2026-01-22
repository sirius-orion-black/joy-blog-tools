import service from '../utils/request'
import type { LabelTypeSearchState, LabelTypeState } from '@/types/labelType'

export default {
  getList: (data: LabelTypeSearchState) => service.get('/admin/content/type/getList', { params: data }),
  editMenu: (data: LabelTypeState) => service.post('/admin/content/type/edit', data),
  delMenu: (data: number[]) => service.post('/admin/content/type/delete', data),
}
