import { Metadata } from 'next';
import { notFound } from 'next/navigation';

import { getArticleDetail } from '@/hook/article.server';

import type { ArticleDetailsState } from '@/types/article';

import { formatTime } from "@/lib/favourUtil";

import CodeBlock from "@/components/codeBlock/CodeBlock";
import BackButton from '@/components/BackButton';

import "./detail.scss";

// 动态生成 SEO 元数据
export async function generateMetadata({ params }: { params: { id: number } }): Promise<Metadata> {
  const { id } = await params;
  const article = await getArticleDetail(id);
  if (!article || article.length < 1) {
    notFound();
  }
  const details:ArticleDetailsState = article[0];
  
  return {
    title: details.title,
    description: details.introduction,
    openGraph: {
      title: details.title,
      description: details.introduction,
      images: [details.cover],
    },
  };
}

export default async function ArticleDetail({ params }: { params: { id: number } }) {

  const { id } = await params;

  const article = await getArticleDetail(id);
  if (!article || article.length < 1) return { title: '文章不存在' };
  const details:ArticleDetailsState = article[0];

  return (
    <div className="page">
      <div className="page-main article-detail-main">
        <div className="detail-header">
          <div className="header-title">
            <BackButton title='返回列表' />
            <h2>{details.title}</h2>
          </div>
          <div className="detail-meta">
            <span className="f9a11b">作者: {details.userName}</span>
            <span className="ffa1cf">关键词: {details.keywords}</span>
            <span className="a66cff">分类: {details.classifyName}</span>
            <span className="c9c9efe">
              标签:{" "}
              {details.labelNames?.length ? details.labelNames.join(",") : ""}
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
