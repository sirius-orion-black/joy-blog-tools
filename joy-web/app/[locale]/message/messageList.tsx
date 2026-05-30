import Image from "next/image";

import { MessageState } from "@/types/message";

import ReplyBox from "./replyBox";

import { formatTime } from "@/lib/favourUtil";

interface Props {
  message: MessageState;
}

//留言列表
export default function MessageList({ message }: Props) {
  const { id, content, createTime, children, location } = message;
  return (
    <div className="bg-white rounded-2xl p-3 md:p-6 shadow-[0_4px_24px_black/0.06]">
      <div className="flex gap-3.5">
        <span>
          <Image
            className="shrink-0 rounded-full object-cover border border-[rgba(208,221,239,0.6)] transition-colors duration-200 hover:border-cd0d-bg"
            src="https://joyimg.lexujia.com/static/avatar-images/2d7d40c4-c62f-4db8-b1ef-cb33ef13698e.png"
            width={52}
            height={52}
            alt="测试"
          />
        </span>
        <div className="flex-1 min-w-0">
          <div className="font-semibold text-sm text-cffa mb-0.5 ffa1cf">
            来自{location}游客
          </div>
          <div className="text-xs text-c5f5 my-2">{formatTime(createTime)}</div>
          <div className="text-sm text-c444 leading-[1.7] wrap-break-word">
            {content}
          </div>

          {/* 二级评论（无表情） */}
          {children && children.length > 0 ? (
            <div className="mt-3.5 px-4 py-3.5 bg-cf8f-bg rounded-2.5">
              {children.map((item) => (
                <div
                  className="flex py-2.5 gap-2.5 [&+&]:border-t [&+&]:border-t-c8aa"
                  key={item.id}
                >
                  <span>
                    <Image
                      className="shrink-0 rounded-full object-cover border border-cebe-bd"
                      src="https://joyimg.lexujia.com/static/avatar-images/2d7d40c4-c62f-4db8-b1ef-cb33ef13698e.png"
                      width={36}
                      height={36}
                      alt=""
                    />
                  </span>
                  <div className="flex-1 min-w-0 leading-[1.6]">
                    <span className="font-semibold text-xs text-cffa mr-2.5">
                      来自{item.location}游客的评论：
                    </span>
                    <span className="text-xs text-ca5f">
                      {formatTime(item.createTime)}
                    </span>
                    <p className="mt-1 text-xs text-c444 wrap-break-word">{item.content}</p>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            ""
          )}

          <ReplyBox parentId={id} />
        </div>
      </div>
    </div>
  );
}
