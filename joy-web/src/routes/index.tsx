import { Routes, Route, Navigate } from "react-router-dom";
import Moments from "@/pages/Moments";
import Article from "@/pages/Article";
import ArticleDetail from "@/pages/Article/detail";
import Column from "@/pages/Column";
import Message from "@/pages/Message";
import About from "@/pages/About";

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Moments />} />
      <Route path="/article" element={<Article />} />
      <Route path="/article/:id" element={<ArticleDetail />} />
      <Route path="/column" element={<Column />} />
      <Route path="/message" element={<Message />} />
      <Route path="/about" element={<About />} />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
