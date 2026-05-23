//代码板块
export interface CodeBlockProps {
  content: string;
  showLineNumbers?: boolean;
  copyable?: boolean;
}
//代码板块
export interface ContentElement {
  type: "text" | "code";
  content: string;
  language?: string;
  key: string;
}
