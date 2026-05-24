'use client';

import { usePathname, useRouter, useParams } from 'next/navigation';

const iconStyle: React.CSSProperties = {
  fontSize: '22px',
  cursor: 'pointer',
  color: '#333',
  transition: 'transform 0.2s ease, color 0.2s ease',
};

//语言切换
export default function LocaleSwitcher() {
  const pathname = usePathname();
  const router = useRouter();
  const { locale } = useParams<{ locale: string }>();

  const toggleLocale = () => {
    const nextLocale = locale === 'zh' ? 'en' : 'zh';
    const newPath = pathname.replace(`/${locale}`, `/${nextLocale}`);
    router.push(newPath);
  };

  return (
    <span 
      style={iconStyle}
      className="iconfont icon-language"
      onClick={toggleLocale}
      title={locale === 'zh' ? 'Switch to English' : '切换到中文'}
      onMouseEnter={(e) => e.currentTarget.style.transform = 'scale(1.1)'}
      onMouseLeave={(e) => e.currentTarget.style.transform = 'scale(1)'}
    />
  );
}
