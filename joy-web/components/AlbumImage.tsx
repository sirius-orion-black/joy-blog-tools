"use client";

import Zoom from "react-medium-image-zoom";
import Image from "next/image";

import "react-medium-image-zoom/dist/styles.css";

import type { AlbumImageProps } from "@/types/image";

//图片可放大
const AlbumImage = ({ src, alt = "图片预览", size = 200 }: AlbumImageProps) => {
  return (
    <div style={{ display: "inline-block" }}>
      <Zoom
        zoomImg={{
          src: src,
          alt: alt,
        }}
      >
        <Image
          src={src}
          alt="Moment image"
          width={size}
          height={size}
          style={
            {
              objectFit: "cover",
              objectPosition: "center",
              cursor: "zoom-in",
              display: "block",
              borderRadius: "4px",
              ["imageRendering" as string]: "high-quality",
            } as React.CSSProperties & { [key: string]: string }
          }
        />
      </Zoom>
    </div>
  );
};

export default AlbumImage;
