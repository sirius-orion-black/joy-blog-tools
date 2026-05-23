export interface CodeBlockProps {
  content: string;
  showLineNumbers?: boolean;
  copyable?: boolean;
}

export interface ContentElement {
  type: "text" | "code";
  content: string;
  language?: string;
  key: string;
}
