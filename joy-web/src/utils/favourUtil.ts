export const formatTime = (input: string): string => {
  // 匹配格式: YYYY-MM-DD HH:mm:ss
  const regex = /^(\d{4})-(\d{2})-(\d{2})\s(\d{2}:\d{2}:\d{2})$/;
  const match = input.match(regex);

  if (match) {
    const [, year, month, day, time] = match;
    return `${year}年${month}月${day}日 ${time}`;
  } else {
    return input;
  }
};

// 检查字段是否为空
export const isEmpty = (value: unknown): boolean => {
  if (value === undefined || value === null) return true;
  if (typeof value === "string") return value.trim() === "";
  if (Array.isArray(value)) return value.length === 0;
  return false;
};

//多种标签色
export const domClass: string[] = [
  "a66cff",
  "f9a11b",
  "ffa1cf",
  "c8aabcc",
  "c9c9efe",
  "c662b2f",
];
