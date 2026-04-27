import { create } from "zustand";

import type {
  ArticleListState,
  ArticleDetailsState,
  ArticleResultState,
} from "@/types/article";

export const articleStore = create<ArticleResultState>((set) => ({
  article: [] as ArticleListState[],
  details: {} as ArticleDetailsState,
  error: null,
  fetchArticle: async () => {
    try {
      const response = await fetch("/json/articleList.json");
      if (!response.ok) throw new Error("网络故障");
      const data: ArticleListState[] = await response.json();
      set({ article: data });
    } catch (error) {
      set({ error: (error as Error).message });
    }
  },
  fetchDetails: async (id: number) => {
    try {
      const response = await fetch(`/json/article/articleDetails-${id}.json`);
      if (!response.ok) throw new Error("网络故障");
      const data: ArticleDetailsState[] = await response.json();
      set({ details: data });
    } catch (error) {
      set({ error: (error as Error).message });
    }
  },
}));
