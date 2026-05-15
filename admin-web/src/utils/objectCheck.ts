//判断是否为空对象
export function isEmptyObject(obj: unknown): boolean {
  // 1. 排除 null、undefined 和非对象类型
  if (obj == null || typeof obj !== 'object') return false
  // 2. 排除数组（Array 也是 object，但通常不视为普通空对象）
  if (Array.isArray(obj)) return false

  // 3. 判断自身可枚举属性是否为空
  return Object.keys(obj).length === 0
}

//判断对象不为空（内部有属性）
export function isNotEmptyObject(obj: unknown): boolean {
  if (obj == null || typeof obj !== 'object') return false
  if (Array.isArray(obj)) return false // 排除数组
  return Object.keys(obj).length > 0
}
