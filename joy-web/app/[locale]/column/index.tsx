import { webConfigStore } from "@/store/webConfigStore";

const Column = () => {
  const { webConfig } = webConfigStore();

  return (
    <div className="page">
      <div className="page-top column-top">
        <div className="top-info">
          <span>
            <p>{webConfig.webAuthor}</p>
            <p>{webConfig.authorSignature}</p>
          </span>
          <img className="avatar" src={webConfig.headPortrait} />
        </div>
      </div>
    </div>
  );
};

export default Column;
