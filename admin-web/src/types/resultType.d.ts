export interface PageDataState {
  //返回分页数据
  current: number
  size: number
  total: number
  pages: number
  records: []
}

export interface PageTableState {
  //表格分页
  current?: number
  pageSize?: number
  total?: number
  defaultPageSize?: number
  disabled?: boolean
}
