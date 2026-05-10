import React from "react";
import { Routes, Route, Navigate, Outlet } from "react-router-dom";

import { useRouteStat } from "@/hook/routeTracker";

import Moments from "@/pages/Moments";
import Article from "@/pages/Article";
import ArticleDetail from "@/pages/Article/detail";
import Column from "@/pages/Column";
import Message from "@/pages/Message";
import About from "@/pages/About";

function ProtectedRoute({ children }: { children?: React.ReactNode }) {
  // 执行访问统计
  useRouteStat();

  return children ? <>{children}</> : <Outlet />;
}

export default function AppRoutes() {
  return (
    <Routes>
      <Route element={<ProtectedRoute />}>
        <Route path="/" element={<Moments />} />
        <Route path="/article" element={<Article />} />
        <Route path="/article/:id" element={<ArticleDetail />} />
        <Route path="/column" element={<Column />} />
        <Route path="/message" element={<Message />} />
        <Route path="/about" element={<About />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Route>
    </Routes>
  );
}
