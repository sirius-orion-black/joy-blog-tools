import type { Metadata } from "next";
import { NextIntlClientProvider } from "next-intl";
import { getMessages } from "next-intl/server";
import { notFound } from "next/navigation";
import { Inter } from "next/font/google";

import { routing, type Locale } from "@/i18n/routing";

import { getWebConfig } from "@/hook/webConfig.server";

import { WebConfigProvider } from "../../components/WebConfigContext";
import { ThemeProvider } from "@/components/ThemeProvider";

import Footer from "@/components/Footer";
import Header from "@/components/header/Header";
import Tracker from "@/components/Tracker";

import "@/app/[locale]/styles/globals.css";

const inter = Inter({ subsets: ["latin"], variable: "--font-inter" });

// 全局 SEO 元数据
export const metadata: Metadata = {
  title: {
    default: "徐徐乐之的博客与小工具",
    template: "%s | 徐徐乐之的博客与小工具",
  },
  applicationName: "徐徐乐之的博客与小工具",
  description:
    "徐徐乐之博客与工具集合，一个很懒的全栈程序猿，偶尔空闲下敲的项目,主要以blog和自己常用的小工具为主的个人小站点",
  viewport:
    "width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no",
  keywords: [
    "AI",
    "大模型",
    "大模型本地运行，数据不外流",
    "OpenClaw",
    "智能体",
    "Next.js",
    "龙虾",
    "React",
    "TypeScript",
    "Vue3",
    "Spring Boot",
    "MySQL",
    "全栈",
  ],
  referrer: "origin-when-cross-origin",
  robots: {
    index: true, // 允许收录
    follow: true, // 允许抓取页面上的链接
    nocache: false, // 允许缓存
    googleBot: {
      index: true,
      follow: true,
      noimageindex: false, // 允许 Google 索引图片
      "max-video-preview": -1, // 视频预览最大时长，-1 表示无限制
      "max-snippet": -1, // 搜索结果摘要最大长度
      "max-image-preview": "large", // 允许大图预览
    },
  },
};

type Props = {
  children: React.ReactNode;
  params: Promise<{ locale: string }>;
};


export default async function LocaleLayout({ children, params }: Props) {
  const { locale } = await params;

  if (!routing.locales.includes(locale as Locale)) {
    notFound();
  }

  const [messages, webConfig] = await Promise.all([
    getMessages(),
    getWebConfig(),
  ]);

  return (
    <html lang={locale} suppressHydrationWarning>
      <head>
        {/* 防闪烁脚本 - 在 HTML 解析前设置主题 */}
        <script
          dangerouslySetInnerHTML={{
            __html: `
              (function() {
                try {
                  var theme = localStorage.getItem('theme');
                  if (theme === 'dark' || (!theme && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
                    document.documentElement.classList.add('dark');
                  } else {
                    document.documentElement.classList.remove('dark');
                  }
                } catch(e) {}
              })();
            `,
          }}
        />
      </head>
      <body className={`${inter.variable} font-sans antialiased`}>
        <NextIntlClientProvider messages={messages}>
          <ThemeProvider>
            <div className="app min-h-screen bg-center bg-cover bg-no-repeat">
              <div className="content min-h-[calc(100vh-32px)] md:p-8.75 pb-0 flex">
                <Header webConfig={webConfig} />
                <WebConfigProvider webConfig={webConfig}>
                  <main className="main flex-1">
                    <Tracker />
                    {children}
                  </main>
                </WebConfigProvider>
              </div>
              <Footer webConfig={webConfig} />
            </div>
          </ThemeProvider>
        </NextIntlClientProvider>
      </body>
    </html>
  );
}
