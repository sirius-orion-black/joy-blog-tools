import { useTranslation } from "react-i18next";

import { webConfigStore } from "@/store/webConfigStore";

const Moments = () => {
  const { t } = useTranslation();
  const { webConfig } = webConfigStore();

  return (
    <div className="page">
      <h1>
        {t("welcome")}
        {webConfig.webName}
      </h1>
    </div>
  );
};

export default Moments;
