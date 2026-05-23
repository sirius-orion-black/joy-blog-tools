'use server';

import { serverFetch } from "@/lib/serverFetch";
import { WebConfigState } from "@/types/webConfig";

export async function getWebConfig(): Promise<WebConfigState> {
  const {data} = await serverFetch<WebConfigState>('/api/blog/v1/web/getInfo', {
    next: { revalidate: 7200 },
  });

  return data;
}
