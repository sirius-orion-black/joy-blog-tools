import { useEffect } from "react";
import { useLocation } from "react-router-dom";
import { localCache } from "@/utils/storage";
import service from "@/utils/request";
// 获取或生成设备ID

const getDeviceId = (): string => {
  let id: string | undefined = localCache.getCache("device_id");
  if (!id) {
    id = crypto.randomUUID();
    localCache.setCache("device_id", id);
  }
  return id;
};

export function useRouteStat() {
  const location = useLocation();
  // const hasReported = useRef(false);

  useEffect(() => {
    // 每次 location 变化时执行
    const report = async () => {
      const deviceId = getDeviceId();
      try {
        // 异步请求上报
        // 注意：这里用 axios 并设置短超时
        await service.post(
          "infra/stat/track",
          {
            path: location.pathname,
            deviceId,
            terminalType: "PC",
          },
          { timeout: 2000 },
        );
      } catch (e) {
        console.warn("Stat report failed", e);
      }
    };

    report();
  }, [location.pathname]); // 路径变化即重新统计
}
