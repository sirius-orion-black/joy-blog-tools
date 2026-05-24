import createNextIntlPlugin from 'next-intl/plugin';
import type { NextConfig } from "next";

const withNextIntl = createNextIntlPlugin();

const nextConfig: NextConfig = {
  /* config options here */
  output: 'standalone',//Standalone 模式
  experimental: {
    // 减少并发
    workerThreads: false,
  },
  images: {
    // unoptimized: process.env.NODE_ENV === 'development',
    unoptimized: true,
    remotePatterns: [
      // 开发环境 - localhost
      {
        protocol: 'http',
        hostname: 'localhost',
        port: '8000',
        pathname: '/**',
      },
    ],
  },
  async rewrites() {
    return [
      {
        source: '/api/:path*',          // 前端请求的路径
        destination: 'http://localhost:8081/api/:path*',  // 实际转发的地址
      },
    ];
  },
};

export default withNextIntl(nextConfig);

