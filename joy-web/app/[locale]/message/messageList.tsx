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
    <div className="message-list">
      <div className="message-detail">
        <Image
          className="message-avatar"
          src="https://joyimg.lexujia.com/static/avatar-images/2d7d40c4-c62f-4db8-b1ef-cb33ef13698e.png"
          width={52}
          height={52}
          alt="测试"
        />
        <div className="message-body">
          <div className="message-author ffa1cf">来自{location}游客</div>
          <div className="message-date a5f5f5f">{formatTime(createTime)}</div>
          <div className="message-text">{content}</div>

          {/* 二级评论（无表情） */}
          {children && children.length > 0 ? (
            <div className="message-comments">
              {children.map((item) => (
                <div className="comment-item" key={item.id}>
                  <Image
                    className="comment-avatar"
                    src="https://joyimg.lexujia.com/static/avatar-images/2d7d40c4-c62f-4db8-b1ef-cb33ef13698e.png"
                    width={36}
                    height={36}
                    alt=""
                  />
                  <div className="comment-body">
                    <span className="comment-author">
                      来自{item.location}游客的评论：
                    </span>
                    <span className="comment-date">
                      {formatTime(item.createTime)}
                    </span>
                    <p className="comment-text">{item.content}</p>
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
