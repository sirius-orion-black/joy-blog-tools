import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import LanguageDetector from 'i18next-browser-languagedetector';

// 语言资源
const resources = {
  en: {
    common: {
      welcome: "Welcome to our application!",
    }
  },
  zh: {
    common: {
      welcome: "欢迎使用我们的应用！",
    }
  }
};

i18n
  .use(LanguageDetector)  // 自动检测浏览器语言
  .use(initReactI18next)
  .init({
    resources,
    fallbackLng: 'en',    // 默认语言
    interpolation: {
      escapeValue: false   // 允许HTML标签
    },
    detection: {
      order: ['querystring', 'cookie', 'localStorage', 'navigator'],
      caches: ['cookie', 'localStorage']
    }
  });

export default i18n;
