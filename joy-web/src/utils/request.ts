import axios from "axios";

const service = axios.create({
  baseURL: window.location.origin + "/api/",
  timeout: 30000,
});

// 拦截器示例
service.interceptors.request.use((config) => {
  const now = Date.now();
  config.headers["X-Timestamp"] = now;
  return config;
});

export default service;
