import type { WebConfigState } from '@/types/webConfig';
import ThemeToggle from '../ThemeToggle';
import HeaderNav from './HeaderNav';
import LocaleSwitcher from '../LocaleSwitcher';

interface Props {
  webConfig: WebConfigState;
}

//左侧导航
export default function Header({ webConfig }: Props) {
  return (
    <header className="header fixed bottom-0 left-0 right-0 z-9 md:static md:bottom-auto md:left-auto md:right-auto md:z-auto bg-white md:bg-transparent">
      <nav className="md:w-70 md:pb-5 text-black dark:text-white bg-white/30 dark:bg-black/30">
        <div className="p-5 h-20 hidden md:flex justify-between flex-nowrap items-center">
          <ThemeToggle />
          <span>{webConfig.webName}</span>
          <LocaleSwitcher />
        </div>
        <HeaderNav />
        <div className="pt-7.5 px-5 pb-5 hidden md:block border-t border-white/50 dark:border-black/50">
          <div className="block leading-9">
            <span>作者：{webConfig.webAuthor}</span>
          </div>
          <a className="block leading-9" href={webConfig.giteeUrl} target="_blank" rel="noopener noreferrer">
            <span className="text-c71d iconfont icon-gitee" />
            <span className="ml-2">gitee</span>
          </a>
          <a className="block leading-9" href={webConfig.githubUrl} target="_blank" rel="noopener noreferrer">
            <span className="text-c221 iconfont icon-github" />
            <span className="ml-2">github</span>
          </a>
        </div>
      </nav>
    </header>
  );
}
