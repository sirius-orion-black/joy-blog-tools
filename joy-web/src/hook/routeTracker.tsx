import { useEffect } from "react";
import { useLocation } from "react-router-dom";
import service from "@/utils/request";

export function useRouteStat() {
  const location = useLocation();
  // const hasReported = useRef(false);

  useEffect(() => {
    // 每次 location 变化时执行
    const report = async () => {
      try {
        // 异步请求上报
        // 注意：这里用 axios 并设置短超时
        await service.get("infra/stat/track", {
          params: {
            path: location.pathname,
            terminalType: "PC",
          },
          timeout: 2000,
        });
      } catch (e) {
        console.warn("Stat report failed", e);
      }
    };

    report();
  }, [location.pathname]); // 路径变化即重新统计
}
