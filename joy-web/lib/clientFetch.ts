// 纯粹给客户端用的请求封装
// 不依赖 next/headers，不写 'use server'

type ClientFetchOptions = RequestInit & {
  baseURL?: string;
};

/**
 * 客户端请求封装
 * - 自动拼接 baseURL（from 环境变量）
 * - 统一加公共请求头
 * - 浏览器端相对路径会自动拼当前域名，不需要手动拼
 */
export async function clientFetch(
  pathOrURL: string,
  options?: ClientFetchOptions
) {
  // ---- 获取 baseURL ----
  // 客户端只能从环境变量获取，不能掉用 headers()
  const baseURL =
    options?.baseURL ||
    process.env.NEXT_PUBLIC_API_BASE_URL ||
    ''; // 留空，浏览器自动用当前域名

  const url = pathOrURL.startsWith('http')
    ? pathOrURL
    : `${baseURL}${pathOrURL}`;

  // ---- 公共请求头 ----
  const defaultHeaders: Record<string, string> = {
    'Content-Type': 'application/json',
    'X-Requested-With': 'XMLHttpRequest',
    // 如果有公共 token 或 api key，也从环境变量拿
    ...(process.env.NEXT_PUBLIC_API_KEY
      ? { 'X-API-Key': process.env.NEXT_PUBLIC_API_KEY }
      : {}),
  };

  const mergedHeaders = {
    ...defaultHeaders,
    ...(options?.headers as Record<string, string>),
  };

  // ---- 发起请求 ----
  const { baseURL: _, ...restOptions } = options || {};
  const res = await fetch(url, {
    ...restOptions,
    headers: mergedHeaders,
  });

  // 简单错误处理
  if (!res.ok) {
    const errorText = await res.text();
    throw new Error(
      `Client fetch failed: ${res.status} ${res.statusText} - ${errorText}`
    );
  }

  return res;
}
