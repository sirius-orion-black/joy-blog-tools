"use client";

import { clientFetch } from "@/lib/clientFetch";

import type { MessageParamsState } from "@/types/message";


export async function publishMessage({ content, parentId = 0 }: MessageParamsState) {
  const res = await clientFetch("/api/blog/v1/message/post", {
    method: "POST",
    body: JSON.stringify({ content, parentId }),
  });
  
  return res.json();
}