import service from '../utils/request'
import type { ArticleSearchState, ArticleState, ArticleUpdateState } from '@/types/articleType'

export default {
  getList: (data: ArticleSearchState) => service.get('/admin/content/blogpost/getList', { params: data }),
  getClassify: () => service.get('/admin/content/blogpost/getClassify'),
  getLabel: () => service.get('/admin/content/blogpost/getLabel'),
  getColumn: () => service.get('/admin/content/blogpost/getColumn'),
  addArticle: (data: ArticleState) => service.post('/admin/content/blogpost/add', data),
  editArticle: (data: ArticleState) => service.post('/admin/content/blogpost/edit', data),
  delArticle: (data: ArticleState) => service.post('/admin/content/blogpost/delete', data),
  updArticle: (data: ArticleUpdateState) => service.post('/admin/content/blogpost/update', data),
}
