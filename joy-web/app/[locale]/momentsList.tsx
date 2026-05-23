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
    <div className="page-main moments-main">
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
          <div key={item.id} className="moments-list">
            {/* 头像 */}
            <div
              className="list-avatar"
              style={{
                backgroundImage: `url(${userAvatar || webConfig.headPortrait})`,
              }}
              aria-label="User Avatar"
            />

            <div className="moments-detail">
              {/* 用户名 */}
              <div className="detail-list detail-name ffa1cf">
                {userName || webConfig.webAuthor}
              </div>

              {/* 时间 */}
              <div className="detail-list detail-time a5f5f5f">
                {formatTime(createTime)}
              </div>

              {/* 地点 */}
              {!isEmpty(location) && (
                <div className="detail-list detail-place">
                  <span className="f9a11b">
                    <i className="iconfont icon-place" aria-hidden="true" />
                  </span>
                  <span style={{ marginLeft: "5px" }}>{location}</span>
                </div>
              )}

              {/* 内容文本 */}
              {!isEmpty(content) && (
                <div className="detail-list detail-content">{content}</div>
              )}

              {/* 图片列表 */}
              {!isEmpty(imageUrls) && typeof imageUrls === "string" && (
                <div className="detail-list detail-img">
                  {imageUrls.split(",").map((url, index) => {
                    const trimmedUrl = url.trim();
                    if (!trimmedUrl) return null; // 跳过空字符串
                    return (
                      <div
                        className="detail-img-content"
                        key={`${item.id}-img-${index}`}
                      >
                        {/* 建议替换为 next/image 以获得更好的性能优化 */}
                        <AlbumImage src={trimmedUrl} size={280} />
                      </div>
                    );
                  })}
                </div>
              )}

              {/* 视频 */}
              {!isEmpty(videoUrl) && (
                <div className="detail-list detail-video">
                  <VideoPlayer
                    src={videoUrl}
                    width={"300px"}
                    height={"200px"}
                  />
                </div>
              )}

              {/* 标签 */}
              {!isEmpty(labelNames) && Array.isArray(labelNames) && (
                <div className="detail-list detail-label bgd0ddef">
                  {labelNames.map((label, index) => (
                    <span className={domClass[index]} key={index}>
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
