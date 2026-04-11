import { create } from "zustand";

import type { WebConfig, ConfigStore } from "@/types/webConfig";

export const webConfigStore = create<ConfigStore>((set) => ({
  webConfig: {} as WebConfig,
  error: null,
  fetchConfig: async () => {
    try {
      const response = await fetch("/json/webConfig.json");
      if (!response.ok) throw new Error("网络故障");
      const config: WebConfig = await response.json();
      console.log(response, config);
      set({ webConfig: config });
    } catch (error) {
      set({ error: (error as Error).message });
    }
  },
}));
