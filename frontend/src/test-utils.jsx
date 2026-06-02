import { render } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import { AuthProvider } from "./app/components/AuthContext";
import { PublicationsProvider } from "./app/components/PublicationsContext";
import { LibraryProvider } from "./app/components/LibraryContext";
import { CartProvider } from "./app/components/CartContext";
import { ListsProvider } from "./app/components/ListsContext";

function AllProviders({ children }) {
    return (
        <AuthProvider>
            <PublicationsProvider>
                <LibraryProvider>
                    <CartProvider>
                        <ListsProvider>
                            <BrowserRouter>
                                {children}
                            </BrowserRouter>
                        </ListsProvider>
                    </CartProvider>
                </LibraryProvider>
            </PublicationsProvider>
        </AuthProvider>
    );
}

export function renderWithProviders(ui) {
    return render(ui, { wrapper: AllProviders });
}

export * from "@testing-library/react";