import service from '../utils/request'

export default {
  login: (data: object) => service.post('/admin/auth/login', data),
  getCaptcha: () => service.get('/admin/auth/getCaptcha'),
  emailVerify: (data: object) => service.post('/admin/auth/emailVerify', data),
}
