export interface LabelTypeSearchState {
  name: string
  state: number | null
  type?: number | null
  page: number
  size: number
}

export interface LabelTypeState {
  id: number | null
  name: string
  state: number | null
  type?: number | null
  description: string
  createTime?: string
}
