import { useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";

import { useIcon } from "@/utils/iconfont";
import { articleStore } from "@/store/articleStore";

import { formatTime } from "@/utils/favourUtil";
import CodeBlock from "@/components/CodeBlock";
import "./detail.scss";

const ArticleDetail = () => {
  const { id } = useParams<{ id: string }>();
  const { details, fetchDetails } = articleStore();
  const navigate = useNavigate();
  const { IconFont } = useIcon();
  useEffect(() => {
    if (id) {
      fetchDetails(Number(id));
    }
  }, [id, fetchDetails]);

  const handleBackToList = () => {
    navigate("/article"); // 导航回文章列表
  };

  return (
    <div className="page">
      <div className="page-main article-detail-main">
        <div className="detail-header">
          <div className="header-title">
            <span onClick={handleBackToList} className="c8aabcc">
              <IconFont type="icon-back" />
              返回列表
            </span>
            <h2>{details.title}</h2>
          </div>
          <div className="detail-meta">
            <span className="f9a11b">作者: {details.userName}</span>
            <span className="ffa1cf">关键词: {details.keywords}</span>
            <span className="a66cff">分类: {details.classifyName}</span>
            <span className="c9c9efe">
              标签: {details.labelNames.join(",")}
            </span>
            <span className="a5f5f5f">
              发布日期: {formatTime(details.createTime ?? "")}
            </span>
          </div>
        </div>
        <div className="detail-content">
          <CodeBlock content={details.content} />
        </div>
      </div>
    </div>
  );
};

export default ArticleDetail;
