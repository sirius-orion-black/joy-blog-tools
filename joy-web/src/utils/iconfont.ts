import React, { createContext, useContext, type ReactNode } from "react";
import { createFromIconfontCN } from "@ant-design/icons";

// 用 ReturnType 获取 createFromIconfontCN 的返回类型
type IconFontComponentType = ReturnType<typeof createFromIconfontCN>;

// 用 Parameters 获取 IconFontComponentType 的第一个参数即 props
type IconFontProps = Parameters<IconFontComponentType>[0];

// 使用完整的类型
type IconContextType = {
  IconFont: React.ComponentType<IconFontProps>;
};

const iconContext = createContext<IconContextType | undefined>(undefined);

const iconFontUrl = createFromIconfontCN({
  scriptUrl: "http://localhost:8000/js/font_4798384_y8p85u889g.js",
});

interface IconProviderProps {
  children: ReactNode;
}

export const IconProvider: React.FC<IconProviderProps> = ({ children }) => {
  // 在 JSX 外部创建 value 对象
  const value: IconContextType = { IconFont: iconFontUrl };

  // 使用 React.createElement
  return React.createElement(iconContext.Provider, { value }, children);
};

export const useIcon = (): IconContextType => {
  const context = useContext(iconContext);
  if (!context) {
    throw new Error("useIcon must be used within an IconProvider");
  }
  return context;
};
