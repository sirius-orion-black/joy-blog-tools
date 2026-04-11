// 使用 const 对象 + as const 替代 enum
const CacheType = {
  Local: "local",
  Session: "session",
} as const;

// 定义类型以供使用
type CacheType = (typeof CacheType)[keyof typeof CacheType];

class Cache {
  storage: Storage;

  constructor(type: CacheType) {
    this.storage = type === CacheType.Local ? localStorage : sessionStorage;
  }

  setCache<T = unknown>(key: string, value: T): boolean {
    try {
      // 字符串类型直接存储，其他类型 JSON 序列化
      const val = typeof value === "string" ? value : JSON.stringify(value);
      this.storage.setItem(key, val);
      return true;
    } catch (error) {
      console.log(error);
      return false;
    }
  }

  getCache<T = unknown>(key: string): T | undefined {
    const value = this.storage.getItem(key);
    if (value === null) return undefined;
    try {
      return JSON.parse(value) as T; // 尝试解析JSON
    } catch {
      return value as T; // 解析失败返回原始字符串
    }
  }

  removeCache(key: string): void {
    this.storage.removeItem(key);
  }

  clear(): void {
    this.storage.clear();
  }
}

export const localCache = new Cache(CacheType.Local);
export const sessionCache = new Cache(CacheType.Session);
