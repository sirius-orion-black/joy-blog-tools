'use server';

import { serverFetch } from "@/lib/serverFetch";

import type { MessageState } from "@/types/message";


export async function getMessages(): Promise<MessageState[]> {

  const {data} = await serverFetch<MessageState[]>("/api/blog/v1/message/getList");
  
  return data;
}
