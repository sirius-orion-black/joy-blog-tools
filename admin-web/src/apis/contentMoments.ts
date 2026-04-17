import service from '../utils/request'
import type { momentsState, momentsSearchState } from '@/types/moments'

export default {
  getList: (data: momentsSearchState) => service.get('/admin/content/moments/getList', { params: data }),
  getLabel: () => service.get('/admin/content/moments/getLabel'),
  addmoments: (data: momentsState) => service.post('/admin/content/moments/add', data),
  delmoments: (data: momentsState) => service.post('/admin/content/moments/delete', data),
  revMoments: (data: momentsState) => service.post('/admin/content/moments/review', data),
}
