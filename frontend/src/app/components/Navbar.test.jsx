import { render, screen, fireEvent } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { vi } from "vitest";

// Context mocks
vi.mock("./CartContext", () => ({
    useCart: () => ({ itemCount: 3 }),
}));

vi.mock("./AuthContext", () => ({
    useAuth: () => ({
        isLoggedIn: true,
        logout: vi.fn(),
    }),
}));

// React Router mock
const mockNavigate = vi.fn();
vi.mock("react-router-dom", async (importOriginal) => {
    const actual = await importOriginal();
    return {
        ...actual,
        useNavigate: () => mockNavigate,
        useLocation: () => ({ pathname: "/" }),
    };
});

// Components imports
import { Navbar } from "./Navbar";

function renderNavbar() {
    return render(
        <MemoryRouter>
            <Navbar />
        </MemoryRouter>
    );
}

describe("Navbar", () => {
    test("renders logo and main navigation links", () => {
        renderNavbar();

        expect(screen.getByText("MiteLovers")).toBeInTheDocument();
        expect(screen.getByText("About Us")).toBeInTheDocument();
        expect(screen.getByText("Marketplace")).toBeInTheDocument();
        expect(screen.getByText("Community")).toBeInTheDocument();
    });

    test("displays the number of items in the cart", () => {
        renderNavbar();

        expect(screen.getByText("3")).toBeInTheDocument();
    });

    test("opens and closes the user dropdown", () => {
        renderNavbar();

        const userButton = screen.getByText("U");
        fireEvent.click(userButton);

        expect(screen.getByText("User Account")).toBeInTheDocument();

        fireEvent.mouseDown(document.body);

        expect(screen.queryByText("User Account")).not.toBeInTheDocument();
    });

    test("logs out and navigates to /", () => {
        renderNavbar();

        const userButton = screen.getByText("U");
        fireEvent.click(userButton);

        const logoutButton = screen.getByText("Logout");
        fireEvent.click(logoutButton);

        expect(mockNavigate).toHaveBeenCalledWith("/");
    });

    test("searches and navigates to explore page", () => {
        renderNavbar();

        const input = screen.getByPlaceholderText("Search by title, author, ISBN...");
        fireEvent.change(input, { target: { value: "harry potter" } });

        fireEvent.submit(input.closest("form"));

        expect(mockNavigate).toHaveBeenCalledWith("/explore?search=harry%20potter");
    });

    test("shows My Purchases link in user dropdown when logged in", () => {
        renderNavbar();

        const userButton = screen.getByText("U");
        fireEvent.click(userButton);

        expect(screen.getByText("My Purchases")).toBeInTheDocument();
    });

    test("My Purchases link points to /my-purchases", () => {
        renderNavbar();

        const userButton = screen.getByText("U");
        fireEvent.click(userButton);

        const myPurchasesLink = screen.getByText("My Purchases").closest("a");
        expect(myPurchasesLink).toHaveAttribute("href", "/my-purchases");
    });
});