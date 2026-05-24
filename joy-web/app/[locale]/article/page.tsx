import PageTop from '@/components/PageTop';
import ArticleList from './articleList';
import { getArticles } from '@/hook/article.server';

import './index.scss'

export const dynamic = 'force-static'; // 强制静态化
export const revalidate = 7200;

export const metadata = {
  title: '技术文章列表',
  description: '最新的文章和技术分享',
};
//文章列表
export default async function ArticlesPage() {
  // 在服务端获取数据
  const articles = await getArticles();

  return (
    <div className="page">
      <PageTop classStr="article-top" />
      <ArticleList articles={articles} />
    </div>
  );
}
