"use client";

import Image from "next/image";
import { useWebConfig } from "./WebConfigContext";

//🧍各个页面右侧顶部
export default function PageTop({ classStr }: { classStr: string }) {
  const webConfig = useWebConfig();

  return (
    <div
      className={`w-full h-75 bg-cover bg-no-repeat bg-center relative overflow-hidden ${classStr}`}
    >
      <div className="absolute right-5 bottom-5 flex items-center justify-end text-right text-white">
        <span>
          <p className="text-sm">{webConfig.webAuthor}</p>
          <p className="text-xs">{webConfig.authorSignature}</p>
        </span>
        <Image
          className="avatar"
          src={webConfig.headPortrait}
          alt="用户头像"
          width={80}
          height={80}
        />
      </div>
    </div>
  );
}
