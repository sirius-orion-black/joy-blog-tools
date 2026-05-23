'use client';

// 使用 const 对象 + as const 替代 enum
const CacheType = {
  Local: 'local',
  Session: 'session',
} as const;

// 定义类型以供使用
type CacheType = (typeof CacheType)[keyof typeof CacheType];

class Cache {
  private type: CacheType;
  private _storage: Storage | null = null;

  constructor(type: CacheType) {
    // 只存类型，不立即访问 localStorage，避免 SSR 阶段报错
    this.type = type;
  }

  // 懒加载：真正调用 setCache/getCache 时才取 localStorage（此时一定在浏览器端）
  private get storage(): Storage {
    if (this._storage) return this._storage;

    if (typeof window === 'undefined') {
      // 服务端降级：内存 fallback，不持久化但保证不报错
      return this.createFallbackStorage();
    }

    this._storage =
      this.type === CacheType.Local ? localStorage : sessionStorage;
    return this._storage;
  }

  // 服务端兜底存储，SSR 阶段静默通过
  private createFallbackStorage(): Storage {
    const map = new Map<string, string>();
    return {
      getItem: (key: string) => map.get(key) ?? null,
      setItem: (key: string, value: string) => void map.set(key, value),
      removeItem: (key: string) => void map.delete(key),
      clear: () => map.clear(),
      get length() {
        return map.size;
      },
      key: (index: number) => {
        const keys = [...map.keys()];
        return keys[index] ?? null;
      },
    };
  }

  setCache<T = unknown>(key: string, value: T): boolean {
    try {
      // 字符串类型直接存储，其他类型 JSON 序列化
      const val = typeof value === 'string' ? value : JSON.stringify(value);
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
