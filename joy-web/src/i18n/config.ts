import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import LanguageDetector from "i18next-browser-languagedetector";

// 定义资源类型
type ResourceKey = string | { [key: string]: unknown };

interface ResourceObject {
  [key: string]: ResourceKey | ResourceObject;
}

interface ResourceLanguage {
  [namespace: string]: ResourceObject;
}

interface Resource {
  [language: string]: ResourceLanguage;
}

// 使用通配符导入语言资源
const enFiles = import.meta.glob("./locales/en/*.json", {
  eager: true,
}) as Record<string, { default: ResourceObject }>;
const zhFiles = import.meta.glob("./locales/zh/*.json", {
  eager: true,
}) as Record<string, { default: ResourceObject }>;

// 处理导入的语言文件
const processLocales = (
  files: Record<string, { default: ResourceObject }>,
): ResourceLanguage => {
  const result: ResourceLanguage = {};
  Object.keys(files).forEach((key) => {
    const fileName = key.split("/").pop()?.replace(".json", "");
    if (fileName) {
      result[fileName] = files[key].default;
    }
  });
  return result;
};

// 创建 i18next 资源对象
const resources: Resource = {
  en: processLocales(enFiles),
  zh: processLocales(zhFiles),
};

i18n
  .use(LanguageDetector) // 自动检测浏览器语言
  .use(initReactI18next)
  .init({
    resources,
    fallbackLng: "en", // 默认语言
    ns: ["common"], // 命名空间
    defaultNS: "common",
    interpolation: {
      escapeValue: false, // 允许HTML标签
    },
    detection: {
      order: ["querystring", "cookie", "localStorage", "navigator"],
      caches: ["cookie", "localStorage"],
    },
  });

export default i18n;
