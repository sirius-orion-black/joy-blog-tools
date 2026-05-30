import PageTop from "@/components/PageTop";
import { getMoments } from "@/hook/moments.server"; // 确保这是服务端函数
import MomentsList from "./momentsList"; // 引入刚才创建的客户端组件

export const dynamic = 'force-static'; // 强制静态化
export const revalidate = 7200;

export const metadata = {
  title: '朋友圈 | 徐徐乐之的博客与小工具',
  description: '分享生活的点点滴滴',
};

export default async function Moments() {
  // 在服务端组件中获取数据
  const moments = await getMoments();

  return (
    <div className="page">
      <PageTop classStr="moments-top" />
      <MomentsList moments={moments} />
    </div>
  );
}
