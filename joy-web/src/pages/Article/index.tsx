import { webConfigStore } from "@/store/webConfigStore";

const Article = () => {
  const { webConfig } = webConfigStore();

  return (
    <div className="page">
      <div className="page-top article-top">
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

export default Article;
