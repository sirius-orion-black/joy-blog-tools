import React from "react";

import type { WebConfigState } from '@/types/webConfig';

type Props = {
  webConfig: WebConfigState;
};

const Footer = ({ webConfig }: Props) => {
  const linkLeftStyle: React.CSSProperties = {
    marginLeft: "5px",
  };
  const summaryLeftStyle: React.CSSProperties = {
    marginRight: "15px",
  };


  return (
    <footer className="footer">
      <div>
        <span style={summaryLeftStyle}>{webConfig.webSummary}</span>
        <span>Copyright©{webConfig.webName}</span>
        <a
          style={linkLeftStyle}
          href="https://beian.miit.gov.cn/"
          target="_blank"
        >
          {webConfig.webRegistration}
        </a>
      </div>
    </footer>
  );
};

export default Footer;
