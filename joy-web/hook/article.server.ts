'use server';

import { serverFetch } from "@/lib/serverFetch";
import { ArticleListState, ArticleDetailsState } from "@/types/article";

export async function getArticles(): Promise<ArticleListState[]> {
  const {data} = await serverFetch<ArticleListState[]>('/api/blog/v1/post/article', {
    next: { revalidate: 7200 },
  });

  return data;
}

export async function getArticleDetail(id: number): Promise<ArticleDetailsState[]> {
  const {data} = await serverFetch<ArticleDetailsState[]>(`/api/blog/v1/post/article?id=${id}`, {
    next: { revalidate: 7200 },
  });

  return data;
}