"use client";

import { useEffect } from "react";
import { usePathname } from "next/navigation";

import { clientFetch } from "@/lib/clientFetch";

function stripLocale(pathname: string): string {
  const segments = pathname.split("/");
  if (segments.length <= 2) return "/";
  return "/" + segments.slice(2).join("/");
}

export default function Tracker() {
  const pathname = usePathname();

  useEffect(() => {
    const realPath = stripLocale(pathname);
    clientFetch("/api/infra/stat/track", {
      method: "POST",
      body: JSON.stringify({ path: realPath, terminalType: "PC" }),
    }).catch(() => {
      // 上报失败静默处理，不影响页面
    });
  }, [pathname]);

  return null; // 不渲染任何东西
}
