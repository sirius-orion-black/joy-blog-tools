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
    <div className="reply-box mt-3.5">
      <div
        className="flex items-start gap-2.5 py-2.5 px-3 bg-cf7f-bg rounded-lg border border-transparent
      transition-all duration-200 focus-within:bg-white focus-within:border-cffa focus-within:shadow-[0_0_0_3px_rgba(255,161,206,0.2)]"
      >
        <span>
          <Image
            className="shrink-0 rounded-full object-cover mt-1"
            src="https://joyimg.lexujia.com/static/avatar-images/2d7d40c4-c62f-4db8-b1ef-cb33ef13698e.png"
            width={30}
            height={30}
            alt="我"
          />
        </span>
        <textarea
          className="flex-1 min-h-9 max-h-22.5 py-1.5 text-[13px] text-cffa bg-transparent border-none outline-none
          resize-none leading-normal placeholder:text-cffa"
          ref={replyRef}
          placeholder="写下你的回复..."
          rows={2}
          value={content}
          onChange={(e) => setContent(e.target.value)}
          maxLength={300}
        />
        <div className="shrink-0 self-end flex flex-col justify-between items-end">
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
            className="mt-1.25 px-3.25 py-0.75 text-xs font-medium text-cffa bg-transparent border
            border-cffa rounded-md cursor-pointer transition-all duration-200 whitespace-nowrap hover:text-white
            hover:bg-cffa active:bg-cffa active:border-cffa"
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
