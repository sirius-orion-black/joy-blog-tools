'use client';

import { usePathname, useParams } from 'next/navigation';
import { useTranslations } from 'next-intl';
import Link from 'next/link';

export default function HeaderNav() {
  const t = useTranslations('nav');
  const pathname = usePathname();
  const { locale } = useParams<{ locale: string }>();

  const normalizedPath = pathname.replace(`/${locale}`, '') || '/';

  const links = [
    { href: '/', label: t('moments'), icon: 'icon-moments' },
    { href: '/article', label: t('article'), icon: 'icon-article' },
    { href: '/message', label: t('message'), icon: 'icon-message' },
  ];

  return (
    <div className="header-nav">
      {links.map((link) => {
        const fullHref = `/${locale}${link.href === '/' ? '' : link.href}`;
        return (
          <Link
            key={link.href}
            href={fullHref}
            className={`nav-link ${normalizedPath === link.href ? 'active' : ''}`}
          >
            <span className={`iconfont ${link.icon}`} />
            <span className="nav-link-title">{link.label}</span>
          </Link>
        );
      })}
    </div>
  );
}
