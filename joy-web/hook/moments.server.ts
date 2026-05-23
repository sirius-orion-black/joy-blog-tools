'use server';

import { serverFetch } from "@/lib/serverFetch";
import { MomentsState } from "@/types/moments";

export async function getMoments(): Promise<MomentsState[]> {
  const {data} = await serverFetch<MomentsState[]>('/api/blog/v1/moments/getList', {
    next: { revalidate: 7200 },
  });

  return data;
}
