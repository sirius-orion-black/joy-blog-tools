import service from '../utils/request'
import type { LabelTypeSearchState, LabelTypeState } from '@/types/labelType'

export default {
  getList: (data: LabelTypeSearchState) => service.get('/admin/content/classify/getList', { params: data }),
  addClassify: (data: LabelTypeState) => service.post('/admin/content/classify/add', data),
  editClassify: (data: LabelTypeState) => service.post('/admin/content/classify/edit', data),
  delClassify: (data: number[]) => service.post('/admin/content/classify/delete', data),
}
