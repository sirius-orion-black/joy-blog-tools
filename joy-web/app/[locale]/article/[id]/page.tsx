import { Metadata } from 'next';
import { notFound } from 'next/navigation';

import { getArticleDetail } from '@/hook/article.server';

import type { ArticleDetailsState } from '@/types/article';

import { formatTime } from "@/lib/favourUtil";

import CodeBlock from "@/components/codeBlock/CodeBlock";
import BackButton from '@/components/BackButton';

export const dynamic = 'force-static'; // 强制静态化
export const revalidate = 7200;

// 动态生成 SEO 元数据
export async function generateMetadata({ params }: { params: { id: number } }): Promise<Metadata> {
  const { id } = await params;
  const article = await getArticleDetail(id);
  if (!article || article.length < 1) return { title: '文章不存在' };
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

// export async function generateStaticParams() {
//   const articles = await getArticleDetail();
//   return articles.map((article) => ({
//     id: article.id.toString(),
//   }));
// }
//文章详情
export default async function ArticleDetail({ params }: { params: { id: number } }) {

  const { id } = await params;

  const articles = await getArticleDetail(id);

  const details:ArticleDetailsState | undefined = articles.find((a) => a.id.toString() === id.toString());

  // const article = await getArticleDetail(id);
  if (!details) {
    notFound();
  }

  return (
    <div className="page">
      <div className="page-main">
        <div className="detail-header">
          <div className="flex items-center flex-nowrap h-11">
            <BackButton title='返回列表' />
            <span className='flex-1 text-center text-xl'>{details.title}</span>
          </div>
          <div className="mt-5 text-xs">
            <span className="text-cf9a mr-3.5">作者: {details.userName}</span>
            <span className="text-cffa mr-3.5">关键词: {details.keywords}</span>
            <span className="text-ca66 mr-3.5">分类: {details.classifyName}</span>
            <span className="text-c9c9 mr-3.5">
              标签:{" "}
              {details.labelNames?.length ? details.labelNames.join(",") : ""}
            </span>
            <span className="text-ca5f">
              发布日期: {formatTime(details.createTime ?? "")}
            </span>
          </div>
        </div>
        <div className="mt-5">
          <CodeBlock content={details.content} />
        </div>
      </div>
    </div>
  );
};
