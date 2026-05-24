'use client';

import { createContext, useContext, ReactNode } from 'react';

import { WebConfigState } from "@/types/webConfig";


const WebConfigContext = createContext<WebConfigState | undefined>(undefined);

//通用webconfig
export const WebConfigProvider = ({ children, webConfig }: { children: ReactNode, webConfig: WebConfigState }) => {
  return (
    <WebConfigContext.Provider value={webConfig}>
      {children}
    </WebConfigContext.Provider>
  );
};

export const useWebConfig = () => {
  const context = useContext(WebConfigContext);
  if (context === undefined) {
    throw new Error('useWebConfig must be used within a WebConfigProvider');
  }
  return context;
};
