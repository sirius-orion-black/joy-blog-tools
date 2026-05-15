import service from '../utils/request'

import type { LoginState, EmailState } from '@/types/loginType'

export default {
  login: (data: LoginState | EmailState) => service.post('/admin/auth/login', data, { headers: { 'Content-Type': 'multipart/form-data' } }),
  getCaptcha: () => service.get('/admin/auth/getCaptcha'),
  sendEmailCode: (data: EmailState) => service.post('/admin/auth/emailVerifyCode', data, { headers: { 'Content-Type': 'multipart/form-data' } }),
  emailLogin: (data: EmailState) => service.post('/admin/auth/emailLogin', data, { headers: { 'Content-Type': 'multipart/form-data' } }),
  logout: () => service.post('/admin/auth/logout'),
}
