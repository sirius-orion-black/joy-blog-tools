import service from '../utils/request'
import type { LabelTypeSearchState, LabelTypeState } from '@/types/labelType'

export default {
  getList: (data: LabelTypeSearchState) => service.get('/admin/content/label/getList', { params: data }),
  editMenu: (data: LabelTypeState) => service.post('/admin/content/label/edit', data),
  delMenu: (data: number[]) => service.post('/admin/content/label/delete', data),
}
