"use client";

import { useRef, useState } from "react";
import { useRouter } from "next/navigation";
import Image from "next/image";

import EmojiPicker from "@/components/emoji/EmojiPicker";

import { publishMessage } from "@/hook/message.client";
import { getValueAfterInsert } from "@/lib/emoji";

//留言评论
export default function ReplyBox({ parentId }: { parentId: number }) {
  const router = useRouter();
  const [content, setContent] = useState("");
  const [loading, setLoading] = useState(false);
  const replyRef = useRef<HTMLTextAreaElement>(null);

  // 发送回复
  const handleSend = async () => {
    if (!content.trim()) {
      console.log("请输入回复内容");
      return;
    }
    if (content.length > 300) {
      console.log("回复内容不能超过300字");
      return;
    }
    try {
      setLoading(true);
      await publishMessage({ content: content.trim(), parentId });
      setContent("");
      router.refresh();
    } catch (e) {
      console.log("回复失败");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="reply-box">
      <div className="reply-input-row">
        <Image
          className="reply-mini-avatar"
          src="https://joyimg.lexujia.com/static/avatar-images/2d7d40c4-c62f-4db8-b1ef-cb33ef13698e.png"
          width={30}
          height={30}
          alt="我"
        />
        <textarea
          ref={replyRef}
          placeholder="写下你的回复..."
          rows={2}
          value={content}
          onChange={(e) => setContent(e.target.value)}
          maxLength={300}
        />
        <div className="reply-ope">
          <EmojiPicker
            onInsert={(emoji) => {
              const el = replyRef.current;
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
          <button
            className="reply-btn"
            type="button"
            onClick={handleSend}
            disabled={loading}
          >
            {loading ? "发送中..." : "发送"}
          </button>
        </div>
      </div>
    </div>
  );
}
