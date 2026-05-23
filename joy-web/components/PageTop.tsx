'use client';

import Image from 'next/image';
import { useWebConfig } from './WebConfigContext';

export default function PageTop({ classStr }: { classStr: string }) {
  const webConfig = useWebConfig();

  return (
    <div className={`page-top ${classStr}`}>
      <div className="top-info">
        <span>
          <p>{webConfig.webAuthor}</p>
          <p>{webConfig.authorSignature}</p>
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
