//文章列表
export interface ArticleListState {
  id: number;
  cover: string;
  labelNames: string[];
  createTime: string;
  userAvatar: string;
  title: string;
  userName: string;
  introduction: string;
  columnName: string;
}
//文章详情
export interface ArticleDetailsState {
  id: number;
  title: string;
  introduction: string;
  cover: string;
  classifyId: number;
  columnId: null;
  userId: number;
  content: string;
  readType: number;
  isStick: number;
  state: number;
  isOriginal: number;
  reprintAddress: null;
  isRecommend: number;
  readership: number;
  keywords: string;
  userSource: number;
  createTime: string;
  updateTime: string;
  likeCount: number;
  classifyName: string;
  labels: number[];
  labelNames: string[];
  userName: string;
  userAvatar: string;
  columnName: string;
}
