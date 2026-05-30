import { useTranslations } from 'next-intl';

//暂无数据
export default function NoData() {
    const t = useTranslations('common');
    return (
      <div className="no-data w-full h-62.5 pt-50 bg-top bg-no-repeat text-center text-black dark:text-white">
        {t('no_data')}
      </div>
    );
}