import { getMessages } from "@/hook/message.server";

import PageTop from "@/components/PageTop";
import NewMessage from "./newMessage";
import MessageList from "./messageList";
import NoData from "@/components/NoData";

import "./index.css";

export const dynamic = "force-dynamic"; 

export const metadata = {
  title: '留言-让我们互相交流交流吧',
  description: '你有什么想和我说的吗？或者你有什么想让其他人看到的吗？',
};

// 留言
export default async function Message() {
  const messages = await getMessages();

  return (
    <div className="page">
      <PageTop classStr="message-top" />
      <div className="page-main">
        <div className="message-page mx-auto p-6 flex flex-col gap-6">
          <NewMessage />
          {messages.length === 0 ? (
          <NoData />
        ) : (
          messages.map((msg) => <MessageList key={msg.id} message={msg} />)
        )}
        </div>
      </div>
    </div>
  );
};

