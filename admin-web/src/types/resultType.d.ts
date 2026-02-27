export interface PageDataState {
  //返回分页数据
  current: number //当前页
  size: number //每页条数
  total: number //总数
  pages: number //总页数
  records: [] //集合
}

export interface PageTableState {
  //表格分页
  current?: number //当前页
  pageSize?: number //每页条数
  total?: number //总数
  defaultPageSize?: number //默认每页条数
  disabled?: boolean
}

//给文件定义的类型
export type InsertFnType = (url: string, alt: string, href: string) => void

export interface ResultFileSingState {
  url: string //文件返回地址
}

export interface ResultFileState {
  code: number //状态码
  data: ResultFileSingState //返回值
  state: string //状态
  message: string //消息
}

export interface ApiResponse<T = unknown> {
  code: number // 状态码(200=成功)
  message: string // 响应消息
  data: T // 泛型数据载体
  state?: string // 状态
}
