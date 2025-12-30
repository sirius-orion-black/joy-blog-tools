import service from '../utils/request'

import type { LoginState, EmailState } from '@/types/LoginType'

export default {
  login: (data: LoginState) => service.post('/admin/auth/login', data, { headers: { 'Content-Type': 'multipart/form-data' } }),
  getCaptcha: () => service.get('/admin/auth/getCaptcha'),
  emailVerify: (data: EmailState) => service.post('/admin/auth/emailVerify', data, { headers: { 'Content-Type': 'multipart/form-data' } }),
}
