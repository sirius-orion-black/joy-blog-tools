import { getMessages } from "@/hook/message.server";

import PageTop from "@/components/PageTop";
import NewMessage from "./newMessage";
import MessageList from "./messageList";
import NoData from "@/components/NoData";

import "./index.scss";

export const dynamic = "force-dynamic"; 

export default async function Message() {
  const messages = await getMessages();

  return (
    <div className="page">
      <PageTop classStr="message-top" />
      <div className="page-main message-main">
        <div className="message-page">
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

