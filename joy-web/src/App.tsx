import AppRoutes from "@/routes";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import "@/assets/styles/app.scss";

import { IconProvider } from "@/utils/iconfont";

export default function App() {
  return (
    <IconProvider>
      <div className="app">
        <div className="content">
          <Header />
          <main className="main">
            <AppRoutes />
          </main>
        </div>
        <Footer />
      </div>
    </IconProvider>
  );
}
