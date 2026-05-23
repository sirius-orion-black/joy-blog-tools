//朋友圈
export interface MomentsState {
  id: number;
  userId: number;
  content: string;
  imageUrls: string;
  videoUrl: string;
  location: string;
  createTime: string;
  labelNames: string[];
  userName: string;
  userAvatar: string;
}