import type { MetadataRoute } from 'next'
import { getArticles } from '@/hook/article.server'

const BASE_URL = 'https://www.lexujia.com'
const locales = ['zh', 'en']

// 获取所有文章的函数
async function getAllArticles() {
  const res = await getArticles()
  return res
}

export default async function sitemap(): Promise<MetadataRoute.Sitemap> {
  const articles = await getAllArticles()

  const blogEntries: MetadataRoute.Sitemap = []

  for (const locale of locales) {
    for (const article of articles) {
      blogEntries.push({
        url: `${BASE_URL}/${locale}/article/${article.id}`,
        lastModified: new Date(article.createTime),
        changeFrequency: 'monthly' as const,
        priority: 0.6,
      })
    }
  }

  // 加上静态路由
  const staticRoutes = [
    { path: '', priority: 1, changeFreq: 'daily' as const },
    { path: '/article', priority: 0.8, changeFreq: 'weekly' as const },
    { path: '/message', priority: 0.5, changeFreq: 'weekly' as const },
  ]

  const staticEntries: MetadataRoute.Sitemap = []

  for (const locale of locales) {
    for (const route of staticRoutes) {
      staticEntries.push({
        url: `${BASE_URL}/${locale}${route.path}`,
        lastModified: new Date(),
        changeFrequency: route.changeFreq,
        priority: route.priority,
      })
    }
  }

  return [...staticEntries, ...blogEntries]
}
