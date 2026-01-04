export interface PageDataState {
  current: number
  size: number
  total: number
  pages: number
  records: []
}

export interface PageTableState {
  current?: number
  pageSize?: number
  total?: number
  defaultPageSize?: number
  disabled?: boolean
}
