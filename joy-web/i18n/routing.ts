import { defineRouting } from "next-intl/routing";
import { createNavigation } from "next-intl/navigation";

export const routing = defineRouting({
  locales: ["en", "zh"] as const,
  defaultLocale: "zh",
  localePrefix: "always", // 始终带语言前缀
});

export type Locale = (typeof routing.locales)[number];

// 导出自动带 locale 的 Link 和 useRouter
export const { Link, redirect, usePathname, useRouter } =
  createNavigation(routing);
