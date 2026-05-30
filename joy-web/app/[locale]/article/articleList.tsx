import { Link } from "@/i18n/routing";

import type { ArticleListState } from "@/types/article";

import { formatTime, domClass } from "@/lib/favourUtil";

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
    <div className="page-main">
      {articles.map((item) => {
        const { cover, title, introduction, createTime, labelNames } = item;
        return (
          <Link href={`/article/${item.id}`} key={item.id}>
            <div className="w-full min-w-0 flex overflow-hidden text-black dark:text-white">
              <div
                className="bg-center bg-no-repeat bg-size-[120px] w-31 h-31 mr-3.5"
                style={{
                  backgroundImage: `url(${cover})`,
                }}
              ></div>
              <div className="flex-1 w-0 min-w-0 mb-6.25">
                <div className="text-base text-ca66 leading-[1.8]">{title}</div>
                <div className="truncate w-full h-7.25 text-sm leading-[1.8]">
                  {introduction}
                </div>
                <div className="text-xs text-ca5f dark:text-ccfc leading-[1.8]">
                  {formatTime(createTime)}
                </div>
                <div className="mt-3.75 text-xs rounded py-1 px-2.5 bg-cd0d-bg">
                  {labelNames.map((em: string, index: number) => (
                    <span className={`text-${domClass[index]} mr-1`} key={index}>
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
  );
}
