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
    <div
      className="bg-white border border-cebe-bd rounded-2xl pt-5 pb-4.5 px-5.5
                    shadow-[0_4px_24px_rgba(255,161,206,0.1)] transition duration-200
                  focus-within:border-cffa focus-within:shadow-[0_0_0_3px_rgba(255,161,206,0.2)]"
    >
      <div className="flex items-center justify-between mb-2.5">
        <span className="text-sm font-semibold text-cffa tracking-wide">
          发表留言
        </span>
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
        className="w-full min-h-18 px-3 py-2.5 text-sm text-cffa bg-cf7f-bg
             border border-transparent rounded-lg outline-none resize-y
             transition-colors duration-200 box-border leading-relaxed
             placeholder:text-cffa placeholder:text-sm
             focus:bg-white focus:border-cffa"
        ref={newMsgRef}
        placeholder="写下你想说的，新的一天新的开始…"
        rows={3}
        value={content}
        onChange={(e) => setContent(e.target.value)}
        maxLength={300}
      />
      <div className="flex justify-between items-center mt-2.5">
        <span className="text-xs text-cf9a">{content.length} / 300</span>
        <button
          className="py-1.75 px-5.5 text-sm font-medium text-white 
             bg-cffa border-none rounded-md cursor-pointer 
             transition-all duration-200
             hover:bg-cffa
             active:bg-cffa active:scale-[0.97]"
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
