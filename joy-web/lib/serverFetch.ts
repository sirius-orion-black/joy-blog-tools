import type { ApiResponse } from '@/types/request';

type FetchOptions = RequestInit & {
  baseURL?: string;
  skipResponseParse?: boolean;
};

/**
 * 统一请求工具
 */
export async function serverFetch<T = unknown>(
  pathOrURL: string,
  options?: FetchOptions
): Promise<ApiResponse<T>> {
  // 获取 baseURL
  let baseURL = options?.baseURL;
  
  if (!baseURL) {
    // 1. 优先使用环境变量
    if (process.env.API_BASE_URL) {
      baseURL = process.env.API_BASE_URL;
    } 
    // 2. 如果没有环境变量，使用硬编码的默认值，或者根据 NODE_ENV 判断
    else {
      // 在本地开发时，可以直接写死 localhost
      const host = process.env.NEXT_PUBLIC_HOST || 'localhost:3000';
      const protocol = process.env.NODE_ENV === 'production' ? 'https' : 'http';
      baseURL = `${protocol}://${host}`;
    }
  }

  // 拼接完整 URL
  const url = pathOrURL.startsWith('http')
    ? pathOrURL
    : `${baseURL}${pathOrURL}`;

  // 公共请求头
  const defaultHeaders: Record<string, string> = {
    'X-Timestamp': Date.now() + '',
    'X-Device-Id': 'server-fixed-id-007',
    'Content-Type': 'application/json',
  };
  
  const mergedHeaders = {
    ...defaultHeaders,
    ...(options?.headers as Record<string, string>),
  };

  // 发起请求
  const { baseURL: _, skipResponseParse, ...restOptions } = options || {};
  
  // 确保 fetch 的 cache 策略正确传递
  const res = await fetch(url, {
    ...restOptions,
    headers: mergedHeaders,
  });

  if (!res.ok) {
    throw new Error(`HTTP error: ${res.status} ${res.statusText}`);
  }

  // 解析响应
  const json: ApiResponse<T> = await res.json();

  // 如果当前接口不需要统一解析，直接返回原始 json
  if (skipResponseParse) {
    return json as unknown as ApiResponse<T>;
  }

  // 业务状态码检查
  if (json.code !== 200) {
    throw new Error(`[${json.code}] ${json.message}`);
  }

  return json;
}
