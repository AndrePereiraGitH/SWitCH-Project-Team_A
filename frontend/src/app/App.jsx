import {BrowserRouter, Routes, Route, Navigate} from "react-router-dom";
import {Navbar} from "./components/Navbar";
import {AddPublication} from "./pages/AddPublication";
import {MyPublications} from "./pages/MyPublications";
import {LandingPage} from "./pages/LandingPage";
import {Dashboard} from "./pages/Dashboard";
import {Library} from "./pages/Library";
import {ManageLists} from "./pages/ManageLists";
import {ListDetail} from "./pages/ListDetail";
import {SharedLists} from "./pages/SharedLists";
import {SharedListDetail} from "./pages/SharedListDetail";
import {SellPublication} from "./pages/SellPublication";
import {Explore} from "./pages/Explore";
import {ProductDetail} from "./pages/ProductDetail";
import {CartProvider} from "./components/CartContext";
import {PurchasesProvider} from "./components/PurchasesContext";
import {Cart} from "./pages/Cart";
import {MyPurchases} from "./pages/MyPurchases";
import {ListsProvider} from "./components/ListsContext";
import {PublicationsProvider} from "./components/PublicationsContext";
import {LibraryProvider} from "./components/LibraryContext";
import {AuthProvider} from "./components/AuthContext";
import {LinksProvider} from "./components/LinksContext";
import { TransactionsProvider } from "./components/TransactionsContext";

export default function App() {
    return (
        <AuthProvider>
            <LinksProvider>
                <TransactionsProvider>
                    <PublicationsProvider>
                        <LibraryProvider>
                            <PurchasesProvider>
                                <CartProvider>
                                    <ListsProvider>
                                        <BrowserRouter>
                                            <div className="min-h-screen bg-background">
                                                <Navbar/>
                                                <Routes>
                                                    <Route path="/" element={<LandingPage/>}/>
                                                    <Route path="/home" element={<Navigate to="/" replace/>}/>
                                                    <Route path="/dashboard" element={<Dashboard/>}/>
                                                    <Route path="/add-publication" element={<AddPublication/>}/>
                                                    <Route path="/my-publications" element={<MyPublications/>}/>
                                                    <Route path="/library" element={<Library/>}/>
                                                    <Route path="/lists" element={<ManageLists/>}/>
                                                    <Route path="/lists/:listId" element={<ListDetail/>}/>
                                                    <Route path="/shared-lists" element={<SharedLists/>}/>
                                                    <Route path="/shared-lists/:listId" element={<SharedListDetail/>}/>
                                                    <Route path="/sell/:bookId" element={<SellPublication/>}/>
                                                    <Route path="/explore" element={<Explore/>}/>
                                                    <Route path="/product/:productId" element={<ProductDetail/>}/>
                                                    <Route path="/cart" element={<Cart/>}/>
                                                    <Route path="/my-purchases" element={<MyPurchases/>}/>
                                                    <Route path="*" element={<Navigate to="/" replace/>}/>
                                                </Routes>
                                            </div>
                                        </BrowserRouter>
                                    </ListsProvider>
                                </CartProvider>
                            </PurchasesProvider>
                        </LibraryProvider>
                    </PublicationsProvider>
                </TransactionsProvider>
            </LinksProvider>
        </AuthProvider>
    );
}