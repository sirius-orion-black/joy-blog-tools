"use client";

import { useTheme } from "@/components/ThemeProvider";

const modelStyle: React.CSSProperties = {
  fontSize: "26px",
  cursor: "pointer",
  transition: "transform 0.2s ease, color 0.2s ease",
};

export default function ThemeToggle() {
  const { isDark, toggleTheme } = useTheme();
  const iconClass = `header-title-theme iconfont ${isDark ? 'icon-sun' : 'icon-moon'}`;
  
  return (
    <span
      suppressHydrationWarning
      style={modelStyle}
      className={iconClass}
      aria-label={isDark ? "切换到浅色模式" : "切换到深色模式"}
      onClick={toggleTheme}
      onMouseEnter={(e) => (e.currentTarget.style.transform = "scale(1.1)")}
      onMouseLeave={(e) => (e.currentTarget.style.transform = "scale(1)")}
    />
  );
}
