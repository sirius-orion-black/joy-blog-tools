import { useTranslations } from 'next-intl';

//暂无数据
export default function NoData() {
    const t = useTranslations('common');
    return (
      <div className="no-data">
        {t('no_data')}
      </div>
    );
}