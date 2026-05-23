//视频
export interface VideoPlayerProps {
  /** 视频地址 */
  src: string;
  /** 视频封面 */
  poster?: string;
  /** 宽度 */
  width?: string | number;
  /** 高度 */
  height?: string | number;
  /** 是否显示控制按钮（原生控制条） */
  showControls?: boolean;
  /** 是否自动播放 */
  autoPlay?: boolean;
  /** 是否循环播放 */
  loop?: boolean;
  /** 是否静音 */
  muted?: boolean;
  /** 自定义类名 */
  className?: string;
  /** 加载失败回调 */
  onError?: (e: React.SyntheticEvent<HTMLVideoElement, Event>) => void;
  /** 播放状态改变回调 */
  onPlayStatusChange?: (isPlaying: boolean) => void;
}
