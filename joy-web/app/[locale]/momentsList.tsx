"use client";

import { useWebConfig } from "@/components/WebConfigContext";
import { MomentsState } from "@/types/moments";

import VideoPlayer from "@/components/VideoPlayer";
import AlbumImage from "@/components/AlbumImage";
import NoData from "@/components/NoData";

import { formatTime, isEmpty, domClass } from "@/lib/favourUtil";

interface MomentsListProps {
  moments: MomentsState[];
}

export default function MomentsList({ moments }: MomentsListProps) {
  const webConfig = useWebConfig();

  if (!moments || moments.length === 0) {
    return <NoData />;
  }

  return (
    <div className="page-main">
      {moments.map((item) => {
        const {
          content,
          userAvatar,
          imageUrls,
          videoUrl,
          userName,
          createTime,
          location,
          labelNames,
        } = item;

        return (
          <div key={item.id} className="flex">
            {/* 头像 */}
            <div
              className="size-15 bg-center bg-size-[60px] rounded-lg"
              style={{
                backgroundImage: `url(${userAvatar || webConfig.headPortrait})`,
              }}
              aria-label="User Avatar"
            />

            <div className="flex-1 mb-6.25 ml-1.5 text-black dark:text-white">
              {/* 用户名 */}
              <div className="text-base text-cffa leading-[1.8em]">
                {userName || webConfig.webAuthor}
              </div>

              {/* 时间 */}
              <div className="text-xs leading-[1.8em] text-ca5f dark:text-ccfc">{formatTime(createTime)}</div>

              {/* 地点 */}
              {!isEmpty(location) && (
                <div className="text-xs leading-[1.8em]">
                  <span className="text-cf9a">
                    <i className="iconfont icon-place" aria-hidden="true" />
                  </span>
                  <span className="ml-1.25">{location}</span>
                </div>
              )}

              {/* 内容文本 */}
              {!isEmpty(content) && <div className="text-sm leading-[1.8em] mt-1">{content}</div>}

              {/* 图片列表 */}
              {!isEmpty(imageUrls) && typeof imageUrls === "string" && (
                <div className="grid grid-cols-3 gap-2 md:gap-4 md:w-230 mt-1.5">
                  {imageUrls.split(",").map((url, index) => {
                    const trimmedUrl = url.trim();
                    if (!trimmedUrl) return null; // 跳过空字符串
                    return (
                      <div
                        className="aspect-square overflow-hidden relative rounded-lg"
                        key={`${item.id}-img-${index}`}
                      >
                        <AlbumImage src={trimmedUrl} size={280} />
                      </div>
                    );
                  })}
                </div>
              )}

              {/* 视频 */}
              {!isEmpty(videoUrl) && (
                <div className="w-4/5 md:w-90 h-50 overflow-hidden">
                  <VideoPlayer
                    src={videoUrl}
                    height={"200px"}
                  />
                </div>
              )}

              {/* 标签 */}
              {!isEmpty(labelNames) && Array.isArray(labelNames) && (
                <div className="mt-3.75 text-xs rounded py-1 px-2.5 bg-cd0d-bg">
                  {labelNames.map((label, index) => (
                    <span className={`text-${domClass[index]} mr-1`} key={index}>
                      {label}
                    </span>
                  ))}
                </div>
              )}
            </div>
          </div>
        );
      })}
    </div>
  );
}
