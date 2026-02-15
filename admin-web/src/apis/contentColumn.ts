import service from '../utils/request'
import type { LabelTypeSearchState, ColumnState } from '@/types/labelType'

export default {
  getList: (data: LabelTypeSearchState) => service.get('/admin/content/column/getList', { params: data }),
  addColumn: (data: ColumnState) => service.post('/admin/content/column/add', data),
  editColumn: (data: ColumnState) => service.post('/admin/content/column/edit', data),
  delColumn: (data: ColumnState) => service.post('/admin/content/column/delete', data),
  reviewColumn: (data: ColumnState) => service.post('/admin/content/column/review', data),
}
