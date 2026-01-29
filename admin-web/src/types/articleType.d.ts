export interface ArticleState {
  //文章
  name: string //文章名
  introduction: string //文章简介
  cover: string //文章封面
  type: number //类型id
  label: number[] //标签 最多五个
}
