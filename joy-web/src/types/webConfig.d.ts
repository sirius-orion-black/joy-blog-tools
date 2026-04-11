export interface WebConfig {
  githubUrl: string;
  webAuthor: string;
  authorSignature: string;
  giteeUrl: string;
  webName: string;
  webSummary: string;
  webRegistration: string;
  headPortrait: string;
}

export interface ConfigStore {
  webConfig: WebConfig;
  error: string | null;
  fetchConfig: () => Promise<void>;
}
