export interface SumCountsState {
  articles: number //文章数
  messages: number //留言数
  moments: number //朋友圈数
  users: number //用户数（有效）
  vists: number //总访问量(ip)
}

export interface ClassifyArticlesState {
  classifyName: string //分类名
  articleCount: number //文章数
}

export interface ClassifyArticlesChatState {
  name: string //分类名
  value: number //文章数
}

export interface VisitsState {
  date: string //日期
  ipCount: number //独立IP数
  pv: number //网站被打开的次数
  uv: number //独立访客数
}

export interface DashBlogState {
  sumCounts: SumCountsState
  classifyArticles: classifyArticlesState[]
  visits: VisitsState[]
}
