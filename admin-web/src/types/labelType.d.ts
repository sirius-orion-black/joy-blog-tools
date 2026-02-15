export interface LabelTypeSearchState {
  name: string //标签名
  state: number | null //标签状态
  type?: number | null //标签所属
  page: number //当前页
  size: number //页数
}

export interface LabelTypeState {
  id: number | null //id
  name: string //标签名
  state: number | null //标签状态
  type?: number | null //标签所属
  description: string //描述
  createTime?: string //创建时间
}

export interface ColumnState {
  id: number | null //id
  name?: string //标签名
  state: number | null //标签状态
  cover?: string //专栏封面
  introduction?: string //简介
  createTime?: string //创建时间
  updateTime?: string //更新时间
}
