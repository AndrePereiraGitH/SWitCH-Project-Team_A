import { render } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import { AuthProvider } from "./app/components/AuthContext";
import { LinksProvider } from "./app/components/LinksContext";
import { PublicationsProvider } from "./app/components/PublicationsContext";
import { LibraryProvider } from "./app/components/LibraryContext";
import { CartProvider } from "./app/components/CartContext";
import { ListsProvider } from "./app/components/ListsContext";
import { PurchasesProvider } from "./app/components/PurchasesContext";

function AllProviders({ children }) {
    return (
        <AuthProvider>
            <LinksProvider>
                <PublicationsProvider>
                    <LibraryProvider>
                        <PurchasesProvider>
                            <CartProvider>
                                <ListsProvider>
                                    <BrowserRouter>
                                        {children}
                                    </BrowserRouter>
                                </ListsProvider>
                            </CartProvider>
                        </PurchasesProvider>
                    </LibraryProvider>
                </PublicationsProvider>
            </LinksProvider>
        </AuthProvider>
    );
}

export function renderWithProviders(ui) {
    return render(ui, { wrapper: AllProviders });
}

export * from "@testing-library/react";