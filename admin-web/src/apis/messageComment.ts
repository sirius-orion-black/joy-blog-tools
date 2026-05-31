import service from '../utils/request'
import type { CommentSearchState, CommentState } from '@/types/commentType'

export default {
  getList: (data: CommentSearchState) => service.get('/admin/message/comment/getList', { params: data }),
  revComment: (data: CommentState) => service.post('/admin/message/comment/review', data),
}
