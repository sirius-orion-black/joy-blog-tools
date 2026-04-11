import React from "react";

import { webConfigStore } from "@/store/webConfigStore";

const Footer = () => {
  const linkLeftStyle: React.CSSProperties = {
    marginLeft: "5px",
  };
  const summaryLeftStyle: React.CSSProperties = {
    marginRight: "15px",
  };

  const { webConfig } = webConfigStore();

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
