import { useEffect } from "react";
import { Link } from "react-router-dom";

import { webConfigStore } from "@/store/webConfigStore";
import { articleStore } from "@/store/articleStore";

import { formatTime, domClass } from "@/utils/favourUtil";

import "./index.scss";

const Article = () => {
  const { webConfig } = webConfigStore();
  const { article, fetchArticle } = articleStore();

  useEffect(() => {
    fetchArticle();
  }, [fetchArticle]);

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
      <div className="page-main article-main">
        {article.map((item) => {
          const { cover, title, introduction, createTime, labelNames } = item;
          return (
            <Link
              to={`/article/${item.id}`}
              className="article-title-link"
              key={item.id}
            >
              <div className="article-list">
                <div
                  className="article-cover"
                  style={{
                    backgroundImage: `url(${cover})`,
                  }}
                ></div>
                <div className="article-detail">
                  <div className="detail-list detail-title a66cff">{title}</div>
                  <div className="detail-list detail-introduction">
                    {introduction}
                  </div>
                  <div className="detail-list detail-time a5f5f5f">
                    {formatTime(createTime)}
                  </div>
                  <div className="detail-list detail-label bgd0ddef">
                    {labelNames.map((em: string, index: number) => (
                      <span className={domClass[index]} key={index}>
                        {em}
                      </span>
                    ))}
                  </div>
                </div>
              </div>
            </Link>
          );
        })}
      </div>
    </div>
  );
};

export default Article;
