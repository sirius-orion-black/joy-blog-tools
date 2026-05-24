import { Link } from '@/i18n/routing';

import type { ArticleListState } from "@/types/article";

import { formatTime ,domClass } from '@/lib/favourUtil';

import NoData from "@/components/NoData";

interface ArticleListProps {
  articles: ArticleListState[];
}

//技术文章列表
export default function ArticleList({ articles }: ArticleListProps) {

  if (!articles || articles.length === 0) {
    return <NoData />;
  }

  return (
      <div className="page-main article-main">
        {articles.map((item) => {
          const { cover, title, introduction, createTime, labelNames } = item;
          return (
            <Link
              href={`/article/${item.id}`}
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
      </div>)
}
