"use client";

import { useRef, useState } from "react";
import { useRouter } from "next/navigation";

import EmojiPicker from "@/components/emoji/EmojiPicker";

import { publishMessage } from "@/hook/message.client";
import { getValueAfterInsert } from "@/lib/emoji";

//顶部：发表新留言（含表情）
export default function NewMessage() {
  const router = useRouter();
  const newMsgRef = useRef<HTMLTextAreaElement>(null);
  const [content, setContent] = useState("");
  const [loading, setLoading] = useState(false);
  const handlePost = async () => {
    if (!content.trim())  {
      console.log("请输入回复内容");
      return;
    }
    if (content.length > 300) {
      console.log("回复内容不能超过300字");
      return;
    }
    try {
      setLoading(true);
      await publishMessage({ content: content.trim() });
      setContent("");
      router.refresh(); // 👈 就这么简单，让服务端组件重新拿数据
    } catch (e) {
      console.log("发布失败");
    } finally {
      setLoading(false);
    }
  };
  return (
    <div className="new-message">
      <div className="new-message-header">
        <span className="new-message-label">发表留言</span>
        <EmojiPicker
          onInsert={(emoji) => {
            const el = newMsgRef.current;
            if (!el) return;
            const { newValue, newCursorPos } = getValueAfterInsert(
              content,
              el.selectionStart,
              el.selectionEnd,
              emoji,
            );
            setContent(newValue);
            // 等 React 用新值重新渲染 textarea 后，恢复光标位置
            requestAnimationFrame(() => {
              el.focus();
              el.setSelectionRange(newCursorPos, newCursorPos);
            });
          }}
        />
      </div>
      <textarea
        ref={newMsgRef}
        placeholder="写下你想说的，新的一天新的开始…"
        rows={3}
        value={content}
        onChange={(e) => setContent(e.target.value)}
        maxLength={300}
      />
      <div className="new-message-footer">
        <span className="char-count">{content.length} / 300</span>
        <button
          className="publish-btn"
          type="button"
          onClick={handlePost}
          disabled={loading}
        >
          {loading ? "发布中..." : "发布留言"}
        </button>
      </div>
    </div>
  );
}
