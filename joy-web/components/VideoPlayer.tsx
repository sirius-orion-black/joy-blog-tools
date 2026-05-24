import React, { useRef, useState, useEffect } from "react";

import type { VideoPlayerProps } from "@/types/video";

//视频
const VideoPlayer = ({
  src,
  poster = "",
  width = "100%",
  height = "auto",
  showControls = true,
  autoPlay = false,
  loop = false,
  muted = false,
  className = "",
  onError,
  onPlayStatusChange,
}: VideoPlayerProps) => {
  const videoRef = useRef<HTMLVideoElement>(null);
  const [isPlaying, setIsPlaying] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // 处理播放/暂停状态同步
  const handlePlay = () => {
    setIsPlaying(true);
    onPlayStatusChange?.(true);
    console.log(isPlaying);
  };

  const handlePause = () => {
    setIsPlaying(false);
    onPlayStatusChange?.(false);
  };

  const handleError = (e: React.SyntheticEvent<HTMLVideoElement, Event>) => {
    setError("视频加载失败");
    onError?.(e);
  };

  // 暴露播放/暂停方法给父组件（可选，通过ref转发实现更复杂控制）
  useEffect(() => {
    if (videoRef.current) {
      if (autoPlay) {
        videoRef.current.play().catch((err) => {
          console.warn("Auto-play failed:", err);
        });
      }
    }
  }, [autoPlay, src]);

  return (
    <div
      className={`relative overflow-hidden rounded-lg bg-black ${className}`}
      style={{ width, height }}
    >
      <video
        width={"100%"}
        height={"100%"}
        ref={videoRef}
        src={src}
        poster={poster}
        controls={showControls}
        autoPlay={autoPlay}
        loop={loop}
        muted={muted}
        playsInline
        className="h-full w-full object-contain"
        onPlay={handlePlay}
        onPause={handlePause}
        onError={handleError}
      >
        您的浏览器不支持 HTML5 video 标签。
      </video>

      {/* 错误提示层 */}
      {error && (
        <div className="absolute inset-0 flex items-center justify-center bg-black/80 text-white">
          <div className="text-center">
            <p className="mb-2 text-sm">{error}</p>
            <button
              onClick={() => {
                setError(null);
                videoRef.current?.load();
                videoRef.current?.play();
              }}
              className="rounded bg-blue-600 px-4 py-1 text-xs hover:bg-blue-700"
            >
              重试
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default VideoPlayer;
