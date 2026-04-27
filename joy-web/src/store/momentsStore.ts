import { create } from "zustand";

import type { MomentsState, MomentsResultState } from "@/types/moments";

export const momentsStore = create<MomentsResultState>((set) => ({
  moments: [] as MomentsState[],
  error: null,
  fetchMoments: async () => {
    try {
      const response = await fetch("/json/momentsContent.json");
      if (!response.ok) throw new Error("网络故障");
      const data: MomentsState[] = await response.json();
      set({ moments: data });
    } catch (error) {
      set({ error: (error as Error).message });
    }
  },
}));
