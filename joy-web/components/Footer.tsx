import React from "react";

import type { WebConfigState } from "@/types/webConfig";

type Props = {
  webConfig: WebConfigState;
};

//底部
const Footer = ({ webConfig }: Props) => {
  const linkLeftStyle: React.CSSProperties = {
    marginLeft: "5px",
  };
  const summaryLeftStyle: React.CSSProperties = {
    marginRight: "15px",
  };

  return (
    <footer className="footer hidden md:block text-center bg-[rgba(255,161,206,0.8)] text-white dark:text-black py-2 leading-8 text-[0.85rem]">
      <span style={summaryLeftStyle}>{webConfig.webSummary}</span>
      <span>Copyright©{webConfig.webName}</span>
      <a
        style={linkLeftStyle}
        href="https://beian.miit.gov.cn/"
        target="_blank"
      >
        {webConfig.webRegistration}
      </a>
    </footer>
  );
};

export default Footer;
