export interface momentsState {
  //朋友圈
  id?: number | null
  userId?: number | null //用户id
  content?: string | undefined //内容
  imageUrls?: string | undefined //图片URL列表
  videoUrl?: string | undefined //视频URL
  privacy?: number | null //隐私: 1公开,2好友,3私密
  state?: number | null //状态: 1 上架 2 删除 3 草稿 4 违规
  location?: string | undefined //地理位置
  userSource?: number | null //创建者来源 1: 前端 2：后台管理者
  labels?: string[] //标签 最多五个
  createTime?: string
  updateTime?: string
  labelNames?: string[] //标签名
  userName?: string //用户名
}

export interface momentsSearchState {
  name: string //用户名
  page: number //当前页
  size: number //页数
}

export interface momentsVideoState {
  width: string
  height: string
  poster: string
}
