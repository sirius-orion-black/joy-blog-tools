import PageTop from '@/components/PageTop'; // 假设你已有这个组件
import ArticleList from './articleList';
import { getArticles } from '@/hook/article.server';

import './index.scss'

export const metadata = {
  title: '技术文章列表',
  description: '最新的文章和技术分享',
};

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
