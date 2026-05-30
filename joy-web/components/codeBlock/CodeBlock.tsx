"use client";

import { useMemo, useRef, useEffect } from "react";
import Prism from "prismjs";
import "prismjs/components/prism-bash";
import "prismjs/components/prism-sql";
import "prismjs/components/prism-shell-session";
import "prismjs/themes/prism-tomorrow.css";

import "./codeblock.css";
import type { CodeBlockProps, ContentElement } from "@/types/code";

//代码块
const CodeBlock = ({
  content,
  showLineNumbers = false,
  copyable = true,
}: CodeBlockProps) => {
  const codeRefs = useRef<Map<string, HTMLElement>>(new Map());

  // 解析 HTML 内容，分离文本和代码块（React 官方文档建议：数据可以从 props 直接计算得出时，使用 useMemo ，所以这里没有用 useEffect + useState）
  const parsedElements = useMemo<ContentElement[]>(() => {
    // 服务端渲染时，避免调用浏览器 API
    if (typeof window === 'undefined' || !content) return [];
    
    try {
      const parser = new DOMParser();
      const doc = parser.parseFromString(content, "text/html");
      const body = doc.body;
      
      const elements: ContentElement[] = [];
      let elementIndex = 0;
      
      Array.from(body.childNodes).forEach((node, index) => {
        if (node.nodeType === Node.ELEMENT_NODE) {
          const element = node as HTMLElement;
          
          if (element.tagName === "P") {
            // 处理段落元素
            elements.push({
              type: "text",
              content: element.innerHTML,
              key: `text-${index}`,
            });
          } else if (element.tagName === "PRE") {
            // 处理代码块元素
            const codeElement = element.querySelector("code");
            if (codeElement) {
              const langMatch = codeElement.className.match(/lang-(\w*)/);
              // 正确获取匹配的字符串
              const language = langMatch ? langMatch[1] : "bash";
              const code = codeElement.textContent || "";
              
              elements.push({
                type: "code",
                content: code.trim(),
                language: language,
                key: `code-${elementIndex++}`,
              });
            }
          } else {
            // 处理其他类型的元素
            elements.push({
              type: "text",
              content: element.outerHTML,
              key: `other-${index}`,
            });
          }
        } else if (node.nodeType === Node.TEXT_NODE && node.textContent?.trim()) {
          // 处理纯文本节点
          elements.push({
            type: "text",
            content: node.textContent,
            key: `text-node-${index}`,
          });
        }
      });
      
      return elements;
    } catch (error) {
      console.error("Failed to parse content:", error);
      return [];
    }
  }, [content]);  // 仅当 content 变化时重新计算

  // 应用语法高亮到代码块
  // 这是必要的 Effect，因为需要与外部系统（Prism.js）同步
  useEffect(() => {
    parsedElements.forEach((element) => {
      if (element.type === "code" && element.key) {
        const codeElement = codeRefs.current.get(element.key);
        if (codeElement && Prism) {
          const language = element.language || "bash";
          const grammar = Prism.languages[language] || Prism.languages.bash;
          
          // 使用 Prism 进行语法高亮
          codeElement.innerHTML = Prism.highlight(
            element.content,
            grammar,
            language,
          );
        }
      }
    });
  }, [parsedElements]);

  const handleCopy = (codeToCopy: string) => {
    navigator.clipboard
      .writeText(codeToCopy)
      .then(() => {
        console.log("Code copied to clipboard");
      })
      .catch((err) => {
        console.error("Failed to copy code: ", err);
      });
  };

  const getLanguageClass = (language: string): string => {
    const langMap: Record<string, string> = {
      "": "language-bash",
      bash: "language-bash",
      shell: "language-bash",
      sh: "language-bash",
      sql: "language-sql",
      mysql: "language-sql",
      javascript: "language-javascript",
      js: "language-javascript",
      typescript: "language-typescript",
      ts: "language-typescript",
      jsx: "language-jsx",
      tsx: "language-tsx",
      python: "language-python",
      py: "language-python",
      java: "language-java",
      php: "language-php",
      ruby: "language-ruby",
      go: "language-go",
      rust: "language-rust",
      css: "language-css",
      html: "language-html",
      xml: "language-xml",
      json: "language-json",
    };

    const normalizedLang = language.toLowerCase();
    return langMap[normalizedLang] || `language-${normalizedLang}`;
  };

  return (
    <div className="code-block">
      {parsedElements.map((element, index) => {
        if (element.type === "text") {
          return (
            <div
              key={element.key || index}
              className="text-content"
              dangerouslySetInnerHTML={{ __html: element.content }}
            />
          );
        } else if (element.type === "code") {
          return (
            <div key={element.key} className="code-container">
              <div className="code-header">
                <span className="code-language">
                  {element.language || "bash"}
                </span>
                {copyable && (
                  <button
                    className="copy-button"
                    onClick={() => handleCopy(element.content)}
                    aria-label="Copy code"
                  >
                    Copy
                  </button>
                )}
              </div>
              <pre className={`${showLineNumbers ? "line-numbers" : ""}`}>
                <code
                  ref={(el) => {
                    if (el && element.key) {
                      codeRefs.current.set(element.key, el);
                    } else if (element.key) {
                      codeRefs.current.delete(element.key);
                    }
                  }}
                  className={getLanguageClass(element.language || "bash")}
                >
                  {/* 初始显示纯文本，防止闪烁，随后被 useEffect 替换为高亮 HTML */}
                  {element.content}
                </code>
              </pre>
            </div>
          );
        }
        return null;
      })}
    </div>
  );
};

export default CodeBlock;
