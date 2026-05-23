//api 返回参数
export interface ApiResponse<T = unknown> {
  code: number;
  state: string;
  message: string;
  data: T;
}