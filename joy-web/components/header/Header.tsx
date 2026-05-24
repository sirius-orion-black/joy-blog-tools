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
    <header className="header">
      <nav className="header-main">
        <div className="header-top">
          <ThemeToggle />
          <span>{webConfig.webName}</span>
          <LocaleSwitcher />
        </div>

        <HeaderNav />

        <div className="header-info">
          <div className="info-list">
            <span>作者：{webConfig.webAuthor}</span>
          </div>
          <a className="info-list" href={webConfig.giteeUrl} target="_blank" rel="noopener noreferrer">
            <span className="header-icon-gitee iconfont icon-gitee" />
            <span className="info-val">gitee</span>
          </a>
          <a className="info-list" href={webConfig.githubUrl} target="_blank" rel="noopener noreferrer">
            <span className="header-icon-github iconfont icon-github" />
            <span className="info-val">github</span>
          </a>
        </div>
      </nav>
    </header>
  );
}
