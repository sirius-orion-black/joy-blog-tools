"use client";

import { usePathname, useParams } from "next/navigation";
import { useTranslations } from "next-intl";
import Link from "next/link";

export default function HeaderNav() {
  const t = useTranslations("nav");
  const pathname = usePathname();
  const { locale } = useParams<{ locale: string }>();

  const normalizedPath = pathname.replace(`/${locale}`, "") || "/";

  const links = [
    { href: "/", label: t("moments"), icon: "icon-moments" },
    { href: "/article", label: t("article"), icon: "icon-article" },
    { href: "/message", label: t("message"), icon: "icon-message" },
  ];

  // 判断当前项是否激活
  const isActive = (href: string) => {
    if (href === "/") {
      return normalizedPath === "/";           // 首页精确匹配
    }
    return normalizedPath.startsWith(href);     // 其余用前缀匹配
  };

  return (
    <div className="header-nav md:pb-2.5 md:px-5 flex md:block">
      {links.map((link) => {
        const fullHref = `/${locale}${link.href === "/" ? "" : link.href}`;
        const active = isActive(link.href);

        return (
          <Link
            key={link.href}
            href={fullHref}
            className={`inline-block md:block md:my-1.25 py-0.75 px-3.75 leading-5 md:leading-9 md:rounded text-center md:text-left flex-1 md:flex-none
              ${active ? "bg-[rgba(255,161,206,0.8)] text-white" : "hover:bg-[rgba(255,161,206,0.8)]"}`}
          >
            <span className={`block md:inline-block iconfont ${link.icon}`} />
            <span className="ml-2">{link.label}</span>
          </Link>
        );
      })}
    </div>
  );
}
