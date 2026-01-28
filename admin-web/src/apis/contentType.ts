import service from '../utils/request'
import type { LabelTypeSearchState, LabelTypeState } from '@/types/labelType'

export default {
  getList: (data: LabelTypeSearchState) => service.get('/admin/content/type/getList', { params: data }),
  addType: (data: LabelTypeState) => service.post('/admin/content/type/add', data),
  editType: (data: LabelTypeState) => service.post('/admin/content/type/edit', data),
  delType: (data: number[]) => service.post('/admin/content/type/delete', data),
}
