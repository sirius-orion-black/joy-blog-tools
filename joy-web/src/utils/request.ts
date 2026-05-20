import axios from "axios";
import { localCache } from "@/utils/storage";

const service = axios.create({
  baseURL: window.location.origin + "/api/",
  timeout: 30000,
});

// 获取或生成设备ID
const getDeviceId = (): string => {
  let id: string | undefined = localCache.getCache("device_id");
  if (!id) {
    id = crypto.randomUUID();
    localCache.setCache("device_id", id);
  }
  return id;
};

// 拦截器示例
service.interceptors.request.use((config) => {
  const now = Date.now();
  config.headers["X-Timestamp"] = now;
  config.headers["X-Device-Id"] = getDeviceId();
  return config;
});

export default service;
