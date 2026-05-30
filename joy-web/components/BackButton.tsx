"use client";

import { useRouter } from "next/navigation";

//返回按钮
export default function BackButton({ title }: { title: string }) {
  const router = useRouter();

  return (
    <span onClick={() => router.back()} className="text-c8aa cursor-pointer">
      <i className="iconfont icon-back" />
      {title}
    </span>
  );
}
