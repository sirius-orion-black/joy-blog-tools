//工具函数：格式化时间
export const formatCnTime = (input: string): string => {
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

// 工具函数：格式化时间
export const formatTime = (
  time: string | number | null | undefined,
): string => {
  if (!time) return "";
  try {
    return new Date(time).toLocaleString("zh-CN", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
    });
  } catch (e) {
    return "";
  }
};

// 检查字段是否为空
export const isEmpty = (value: unknown): boolean => {
  if (value === undefined || value === null) return true;
  if (typeof value === "string") return value.trim() === "";
  if (Array.isArray(value)) return value.length === 0;
  return false;
};

// 工具函数：判断字符串或数组是否为空
export const isBlank = (
  value: string | unknown[] | null | undefined,
): boolean => {
  if (value === null || value === undefined) return true;
  if (typeof value === "string") return value.trim() === "";
  if (Array.isArray(value)) return value.length === 0;
  return false;
};

//多种标签色
export const domClass: string[] = [
  "ca66",
  "cf9a",
  "cffa",
  "c8aa",
  "c9c9",
  "c662",
];
