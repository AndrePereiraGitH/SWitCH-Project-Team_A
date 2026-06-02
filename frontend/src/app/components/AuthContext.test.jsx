import { describe, test, expect, vi } from "vitest";
import { render, screen, fireEvent } from "@testing-library/react";
import { AuthProvider, useAuth } from "./AuthContext";

// A simple test component that uses the auth context
function TestComponent() {
    const { isLoggedIn, login, logout } = useAuth();
    return (
        <div>
            <p>{isLoggedIn ? "Logged in" : "Logged out"}</p>
            <button onClick={login}>Login</button>
            <button onClick={logout}>Logout</button>
        </div>
    );
}

function renderWithAuth() {
    return render(
        <AuthProvider>
            <TestComponent />
        </AuthProvider>
    );
}

// ── Initial state ─────────────────────────────────────────────────────────────

describe("AuthContext — Initial State", () => {

    test("user is logged out by default", () => {
        renderWithAuth();
        expect(screen.getByText("Logged out")).toBeInTheDocument();
    });

    test("login button is present", () => {
        renderWithAuth();
        expect(screen.getByText("Login")).toBeInTheDocument();
    });

    test("logout button is present", () => {
        renderWithAuth();
        expect(screen.getByText("Logout")).toBeInTheDocument();
    });

});

// ── Login ─────────────────────────────────────────────────────────────────────

describe("AuthContext — Login", () => {

    test("clicking login sets isLoggedIn to true", () => {
        renderWithAuth();
        fireEvent.click(screen.getByText("Login"));
        expect(screen.getByText("Logged in")).toBeInTheDocument();
    });

    test("isLoggedIn is false before login", () => {
        renderWithAuth();
        expect(screen.queryByText("Logged in")).not.toBeInTheDocument();
    });

});

// ── Logout ────────────────────────────────────────────────────────────────────

describe("AuthContext — Logout", () => {

    test("clicking logout after login sets isLoggedIn to false", () => {
        renderWithAuth();
        fireEvent.click(screen.getByText("Login"));
        expect(screen.getByText("Logged in")).toBeInTheDocument();
        fireEvent.click(screen.getByText("Logout"));
        expect(screen.getByText("Logged out")).toBeInTheDocument();
    });

    test("logout without login keeps user logged out", () => {
        renderWithAuth();
        fireEvent.click(screen.getByText("Logout"));
        expect(screen.getByText("Logged out")).toBeInTheDocument();
    });

});

// ── Error handling ────────────────────────────────────────────────────────────

describe("AuthContext — Error Handling", () => {

    test("useAuth throws error when used outside AuthProvider", () => {
        // Suppress console.error for this test
        const consoleSpy = vi.spyOn(console, "error").mockImplementation(() => {});

        function ComponentWithoutProvider() {
            useAuth();
            return <div>Should not render</div>;
        }

        expect(() => render(<ComponentWithoutProvider />)).toThrow(
            "useAuth must be used within an AuthProvider"
        );

        consoleSpy.mockRestore();
    });

});