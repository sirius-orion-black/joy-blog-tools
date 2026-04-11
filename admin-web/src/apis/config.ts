import service from '../utils/request'
import type { ConfigState } from '@/types/configType'

export default {
  getConfig: () => service.get('/admin/config/web/get'),
  editConfig: (data: ConfigState) => service.post('/admin/config/web/edit', data),
}
