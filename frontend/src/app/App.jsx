import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { Navbar } from "./components/Navbar";
import { LandingPage } from "./pages/LandingPage";
import { Dashboard } from "./pages/Dashboard";
import { AddPublication } from "./pages/AddPublication";
import { MyPublications } from "./pages/MyPublications";
import { Library } from "./pages/Library";
import { ManageLists } from "./pages/ManageLists";
import { ListDetail } from "./pages/ListDetail";
import { SellPublication } from "./pages/SellPublication";
import { Explore } from "./pages/Explore";
import { ProductDetail } from "./pages/ProductDetail";
import { CartProvider } from "./components/CartContext";
import { ListsProvider } from "./components/ListsContext";
import { PublicationsProvider } from "./components/PublicationsContext";
import { LibraryProvider } from "./components/LibraryContext";
import { AuthProvider } from "./components/AuthContext";

export default function App() {
  return (
    <AuthProvider>
      <PublicationsProvider>
        <LibraryProvider>
          <CartProvider>
            <ListsProvider>
              <BrowserRouter>
                <div className="min-h-screen bg-background">
                  <Navbar />
                  <Routes>
                    <Route path="/" element={<LandingPage />} />
                    <Route path="/home" element={<Navigate to="/" replace />} />
                    <Route path="/dashboard" element={<Dashboard />} />
                    <Route path="/add-publication" element={<AddPublication />} />
                    <Route path="/my-publications" element={<MyPublications />} />
                    <Route path="/library" element={<Library />} />
                    <Route path="/lists" element={<ManageLists />} />
                    <Route path="/lists/:listId" element={<ListDetail />} />
                    <Route path="/sell/:bookId" element={<SellPublication />} />
                    <Route path="/explore" element={<Explore />} />
                    <Route path="/product/:productId" element={<ProductDetail />} />
                    <Route path="*" element={<Navigate to="/" replace />} />
                  </Routes>
                </div>
              </BrowserRouter>
            </ListsProvider>
          </CartProvider>
        </LibraryProvider>
      </PublicationsProvider>
    </AuthProvider>
  );
}
