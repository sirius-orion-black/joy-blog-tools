import { webConfigStore } from "@/store/webConfigStore";

const Message = () => {
  const { webConfig } = webConfigStore();

  return (
    <div className="page">
      <div className="page-top message-top">
        <div className="top-info">
          <span>
            <p>{webConfig.webAuthor}</p>
            <p>{webConfig.authorSignature}</p>
          </span>
          <img className="avatar" src={webConfig.headPortrait} />
        </div>
      </div>
      <div className="page-main message-main"></div>
    </div>
  );
};

export default Message;
