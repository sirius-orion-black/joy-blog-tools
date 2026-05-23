import createNextIntlPlugin from 'next-intl/plugin';
import type { NextConfig } from "next";

const withNextIntl = createNextIntlPlugin();

const nextConfig: NextConfig = {
  /* config options here */
  images: {
    unoptimized: process.env.NODE_ENV === 'development',
    remotePatterns: [
      // 开发环境 - localhost
      {
        protocol: 'http',
        hostname: 'localhost',
        port: '8000',
        pathname: '/**',
      },
      // 生产环境 - 您的域名
      {
        protocol: 'https',
        hostname: 'joyimg.lexujia.com',
        port: '',
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

