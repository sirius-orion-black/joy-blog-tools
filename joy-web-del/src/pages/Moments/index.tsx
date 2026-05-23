import { useEffect } from "react";

import { webConfigStore } from "@/store/webConfigStore";
import { momentsStore } from "@/store/momentsStore";
import { useIcon } from "@/utils/iconfont";

import VideoPlayer from "@/components/VideoPlayer";
import AlbumImage from "@/components/AlbumImage";

import { formatTime, isEmpty, domClass } from "@/utils/favourUtil";

import "./index.scss";

const Moments = () => {
  const { webConfig } = webConfigStore();

  const { moments, fetchMoments } = momentsStore();
  const { IconFont } = useIcon();

  useEffect(() => {
    fetchMoments();
  }, [fetchMoments]);

  return (
    <div className="page">
      <div className="page-top moments-top">
        <div className="top-info">
          <span>
            <p>{webConfig.webAuthor}</p>
            <p>{webConfig.authorSignature}</p>
          </span>
          <img className="avatar" src={webConfig.headPortrait} />
        </div>
      </div>
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
              <div
                className="list-avatar"
                style={{
                  backgroundImage: `url(${userAvatar ?? webConfig.headPortrait})`,
                }}
              ></div>
              <div className="moments-detail">
                <div className="detail-list detail-name ffa1cf">
                  {userName ?? webConfig.webAuthor}
                </div>
                <div className="detail-list detail-time a5f5f5f">
                  {formatTime(createTime)}
                </div>
                <div className="detail-list detail-place">
                  <span className="f9a11b">
                    <IconFont type="icon-place" />
                  </span>
                  <span style={{ marginLeft: "5px" }}>{location}</span>
                </div>
                {!isEmpty(content) && (
                  <div className="detail-list detail-content">{content}</div>
                )}
                {!isEmpty(imageUrls) && (
                  <div className="detail-list detail-img">
                    {imageUrls.split(",").map((url, index) => (
                      <div className="detail-img-content" key={index}>
                        <AlbumImage src={url} size={280} />
                      </div>
                    ))}
                  </div>
                )}
                {!isEmpty(videoUrl) && (
                  <div className="detail-list detail-video">
                    <VideoPlayer
                      src={videoUrl}
                      width={"300px"}
                      height={"200px"}
                    />
                  </div>
                )}
                {!isEmpty(labelNames) && (
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
    </div>
  );
};

export default Moments;
