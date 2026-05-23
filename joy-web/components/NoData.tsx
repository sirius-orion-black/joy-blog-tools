import { useTranslations } from 'next-intl';

export default function NoData() {
    const t = useTranslations('common');
    return (
      <div className="no-data">
        {t('no_data')}
      </div>
    );
}