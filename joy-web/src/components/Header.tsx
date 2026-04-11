import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";

import { useIcon } from "@/utils/iconfont";
import { webConfigStore } from "@/store/webConfigStore";

import { NavLink } from "react-router-dom";

import { getCurrentTheme, changeTheme } from "@/utils/theme";
import type { ThemeType } from "@/utils/theme";

const Header: React.FC = () => {
  const { t } = useTranslation();

  const { IconFont } = useIcon();
  const { webConfig, fetchConfig } = webConfigStore();
  useEffect(() => {
    fetchConfig();
  }, [fetchConfig]);

  const iconStyle: React.CSSProperties = {
    fontSize: "40px",
  };

  const modelStyle: React.CSSProperties = {
    fontSize: "26px",
    cursor: "pointer",
  };

  const [theme, setTheme] = useState<ThemeType>(() => {
    // 在初始渲染时获取当前主题
    return getCurrentTheme();
  });

  const handChangeTheme = () => {
    changeTheme();
    const newTheme = getCurrentTheme();
    setTheme(newTheme);
  };

  return (
    <header className="header">
      <nav className="header-main">
        <div className="header-top">
          <IconFont style={iconStyle} type="icon-run" />
          <span>{webConfig.webName}</span>
          <IconFont
            style={modelStyle}
            type={theme === "light" ? "icon-sun" : "icon-moon"}
            onClick={handChangeTheme}
          />
        </div>
        <div className="header-nav">
          <NavLink className="nav-link" to="/">
            <IconFont type="icon-moments" />
            <span className="nav-link-title">{t("nav.moments")}</span>
          </NavLink>
          <NavLink className="nav-link" to="/article">
            <IconFont type="icon-article" />
            <span className="nav-link-title">{t("nav.article")}</span>
          </NavLink>
          <NavLink className="nav-link" to="/column">
            <IconFont type="icon-column" />
            <span className="nav-link-title">{t("nav.column")}</span>
          </NavLink>
          <NavLink className="nav-link" to="/message">
            <IconFont type="icon-message" />
            <span className="nav-link-title">{t("nav.message")}</span>
          </NavLink>
          <NavLink className="nav-link" to="/about">
            <IconFont type="icon-about" />
            <span className="nav-link-title">{t("nav.about")}</span>
          </NavLink>
        </div>
        <div className="header-info">
          <div className="info-list">
            <span>{t("nav.author") + webConfig.webAuthor}</span>
          </div>
          <a className="info-list" href={webConfig.giteeUrl} target="_blank">
            <IconFont type="icon-gitee" />
            <span className="info-val">gitee</span>
          </a>
          <a className="info-list" href={webConfig.githubUrl} target="_blank">
            <IconFont type="icon-github" />
            <span className="info-val">github</span>
          </a>
        </div>
      </nav>
    </header>
  );
};

export default Header;
