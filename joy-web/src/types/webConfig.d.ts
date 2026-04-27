export interface WebConfigState {
  githubUrl: string;
  webAuthor: string;
  authorSignature: string;
  giteeUrl: string;
  webName: string;
  webSummary: string;
  webRegistration: string;
  headPortrait: string;
}

export interface webConfigResultState {
  webConfig: WebConfig;
  error: string | null;
  fetchConfig: () => Promise<void>;
}
