import { webConfigStore } from "@/store/webConfigStore";

import "./index.scss";

const Moments = () => {
  const { webConfig } = webConfigStore();

  return (
    <div className="page">
      <div className="page-top moments-top">
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

export default Moments;
