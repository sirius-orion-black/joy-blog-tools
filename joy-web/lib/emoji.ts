export interface EmojiCategory {
  name: string;
  icon: string;
  list: string[];
}

export const EMOJI_DATA: EmojiCategory[] = [
  {
    name: '常用',
    icon: '🕐',
    list: ['😀', '😂', '🤣', '😊', '😍', '🥰', '😘', '😜', '🤔', '😏', '😌', '😴'],
  },
  {
    name: '心情',
    icon: '💭',
    list: ['❤️', '💔', '💖', '👍', '👎', '👏', '🙏', '💪', '🤝', '✌️', '🤞', '👌'],
  },
  {
    name: '日常',
    icon: '🌟',
    list: ['🎉', '🎂', '🔥', '⭐', '💯', '✅', '❌', '❓', '❗', '💡', '📌', '🔔'],
  },
];

/**
 * 计算在光标位置插入文本后的新值和光标位置
 * 不直接操作 DOM，纯函数
 */
export function getValueAfterInsert(
  currentValue: string,
  selectionStart: number,
  selectionEnd: number,
  insertText: string
): { newValue: string; newCursorPos: number } {
  const before = currentValue.slice(0, selectionStart);
  const after = currentValue.slice(selectionEnd);
  return {
    newValue: before + insertText + after,
    newCursorPos: selectionStart + insertText.length,
  };
}

/**
 * 将文本插入到 textarea 光标 / 选区位置，并保持焦点
 */
export function insertAtCursor(el: HTMLTextAreaElement, text: string): void {
  const start = el.selectionStart;
  const end = el.selectionEnd;
  const before = el.value.slice(0, start);
  const after = el.value.slice(end);
  el.value = before + text + after;
  el.selectionStart = el.selectionEnd = start + text.length;
  el.focus();
}
