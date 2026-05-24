import { localCache } from "@/lib/storage";

// 获取或生成设备ID
const getDeviceId = (): string => {
  let id: string | undefined = localCache.getCache("device_id");
  if (!id) {
    id = crypto.randomUUID();
    localCache.setCache("device_id", id);
  }
  return id;
};

type ClientFetchOptions = RequestInit & {
  baseURL?: string;
};

/**
 * 客户端请求封装
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
    'X-Timestamp': Date.now() + '',
    'X-Device-Id': getDeviceId(),
    'Content-Type': 'application/json',
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
