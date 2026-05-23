"use client";

import { useState } from "react";
import { getCurrentTheme, changeTheme } from "@/lib/theme";
import type { ThemeType } from "@/lib/theme";

const DEFAULT_THEME: ThemeType = "light";

const modelStyle: React.CSSProperties = {
  fontSize: "26px",
  cursor: "pointer",
  transition: 'transform 0.2s ease, color 0.2s ease',
};

export default function ThemeToggle() {
  // 初始值设为默认主题，确保服务端和客户端一致
  const [theme, setTheme] = useState<ThemeType>(DEFAULT_THEME);

  const handleChangeTheme = () => {
    changeTheme();
    // 在事件处理程序中更新状态
    setTheme(getCurrentTheme());
  };

  return theme === "light" ? (
    <span
      style={modelStyle}
      className="header-title-theme iconfont icon-sun"
      onClick={handleChangeTheme}
      onMouseEnter={(e) => e.currentTarget.style.transform = 'scale(1.1)'}
      onMouseLeave={(e) => e.currentTarget.style.transform = 'scale(1)'}
    />
  ) : (
    <span
      style={modelStyle}
      className="header-title-theme iconfont icon-moon"
      onClick={handleChangeTheme}
      onMouseEnter={(e) => e.currentTarget.style.transform = 'scale(1.1)'}
      onMouseLeave={(e) => e.currentTarget.style.transform = 'scale(1)'}
    />
  );
}
