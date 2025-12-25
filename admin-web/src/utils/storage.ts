enum CacheType {
  Local,
  Session,
}

class Cache {
  storage: Storage

  constructor(type: CacheType) {
    this.storage = type === CacheType.Local ? localStorage : sessionStorage
  }

  setCache<T = unknown>(key: string, value: T): boolean {
    try {
      const val = JSON.stringify(value)
      this.storage.setItem(key, val)
      return true
    } catch (error) {
      console.log(error)
      return false
    }
  }

  getCache<T = unknown>(key: string): T | undefined {
    const value = this.storage.getItem(key)
    return value ? JSON.parse(value) : undefined
  }

  removeCache(key: string): void {
    this.storage.removeItem(key)
  }

  clear(): void {
    this.storage.clear()
  }
}

export const localCache = new Cache(CacheType.Local)
export const sessionCache = new Cache(CacheType.Session)
