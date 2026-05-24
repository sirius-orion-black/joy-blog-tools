export interface MessageParamsState {
  content: string;
  parentId?: number;
}

export interface MessageState {
  id: number;
  parentId: number;
  userId: number | null;
  content: string;
  state: number;
  createTime: string;
  children: MessageItem[];
}