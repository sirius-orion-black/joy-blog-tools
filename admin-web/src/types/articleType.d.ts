export interface ArticleState {
  //文章
  id?: number | null
  title?: string | undefined //文章标题
  introduction?: string | undefined //文章简介
  cover?: string | undefined //文章封面
  classifyId?: number | null | undefined //类型id
  columnId?: number | null | undefined //专栏id
  labels?: string[] //标签 最多五个
  keywords?: string | undefined //关键词
  state?: number | undefined //状态
  readType?: number | undefined //阅读方式:1 免费阅读 2 付费阅读 3 会员阅读 4 私有 5 登录阅读
  isOriginal?: number | undefined //是否原创:  1 转载 2 原创
  reprintAddress?: string | undefined //转载地址
  content?: string //文章内容
  userId?: number | null //用户id
  userName?: string //用户名
  classifyName?: string
  createTime?: string
  updateTime?: string
  columnName?: string
  labelNames?: string[]
  likeCount?: number //点赞
  readership?: number //阅读量
  isRecommend?: number //是否推荐: 1 推荐 2 不推荐
  isStick?: number
}

export interface ArticleBooleanState {
  isStick: boolean
  isOriginal: boolean
  isRecommend: boolean
}

export interface ArticleSearchState {
  title: string //文章标题
  type: number | null //类型id
  state: number | null //状态
  page: number
  size: number
}

export interface ArticleDrawerState {
  title: string //弹出层标题
  show: false | true
  type: string
  article: ArticleState
}

export interface ArticleUpdateState {
  id: number
  action: string //操作
}
