import service from '../utils/request'

export default {
  getDashBlog: () => service.get('/admin/dash/blog'),
}
