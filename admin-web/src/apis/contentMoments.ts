import service from '../utils/request'
import type { MomentsState, MomentsSearchState } from '@/types/moments'

export default {
  getList: (data: MomentsSearchState) => service.get('/admin/content/moments/getList', { params: data }),
  getLabel: () => service.get('/admin/content/moments/getLabel'),
  addmoments: (data: MomentsState) => service.post('/admin/content/moments/add', data),
  delmoments: (data: MomentsState) => service.post('/admin/content/moments/delete', data),
  revMoments: (data: MomentsState) => service.post('/admin/content/moments/review', data),
}
