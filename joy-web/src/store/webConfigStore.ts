import { create } from "zustand";

import type { WebConfigState, webConfigResultState } from "@/types/webConfig";

export const webConfigStore = create<webConfigResultState>((set) => ({
  webConfig: {} as WebConfigState,
  error: null,
  fetchConfig: async () => {
    try {
      const response = await fetch("/json/webConfig.json");
      if (!response.ok) throw new Error("网络故障");
      const data: WebConfigState = await response.json();
      set({ webConfig: data });
    } catch (error) {
      set({ error: (error as Error).message });
    }
  },
}));
