import { describe, test, expect } from "vitest";
import { screen } from "@testing-library/react";
import { renderWithProviders } from "../../test-utils";
import { BookCard } from "./BookCard";

function renderBookCard(props = {}) {
    const defaultProps = {
        title: "The Da Vinci Code",
        author: "Dan Brown",
        genre: "Mystery",
        price: 12.50,
        condition: "Good",
        isAuction: false,
        auctionEndDate: null,
    };
    return renderWithProviders(<BookCard {...defaultProps} {...props} />);
}

// ── Rendering ─────────────────────────────────────────────────────────────────

describe("BookCard — Rendering", () => {

    test("renders the book title", () => {
        renderBookCard();
        expect(screen.getByText("The Da Vinci Code")).toBeInTheDocument();
    });

    test("renders the author", () => {
        renderBookCard();
        expect(screen.getByText("Dan Brown")).toBeInTheDocument();
    });

    test("renders the genre", () => {
        renderBookCard();
        expect(screen.getByText("Mystery")).toBeInTheDocument();
    });

    test("renders the condition", () => {
        renderBookCard();
        expect(screen.getByText("Good")).toBeInTheDocument();
    });

    test("renders the price formatted to 2 decimal places", () => {
        renderBookCard({ price: 12.5 });
        expect(screen.getByText("12.50")).toBeInTheDocument();
    });

    test("renders different title when prop changes", () => {
        renderBookCard({ title: "Harry Potter" });
        expect(screen.getByText("Harry Potter")).toBeInTheDocument();
    });

    test("renders different author when prop changes", () => {
        renderBookCard({ author: "J.K. Rowling" });
        expect(screen.getByText("J.K. Rowling")).toBeInTheDocument();
    });

});

// ── Auction ───────────────────────────────────────────────────────────────────

describe("BookCard — Auction", () => {

    test("shows days left when isAuction is true and auctionEndDate is set", () => {
        const futureDate = new Date(Date.now() + 5 * 24 * 60 * 60 * 1000);
        renderBookCard({ isAuction: true, auctionEndDate: futureDate });
        expect(screen.getByText(/d left/i)).toBeInTheDocument();
    });

    test("does not show days left when isAuction is false", () => {
        renderBookCard({ isAuction: false, auctionEndDate: null });
        expect(screen.queryByText(/d left/i)).not.toBeInTheDocument();
    });

    test("does not show days left when auctionEndDate is null", () => {
        renderBookCard({ isAuction: true, auctionEndDate: null });
        expect(screen.queryByText(/d left/i)).not.toBeInTheDocument();
    });

    test("shows 5 days left for auction ending in 5 days", () => {
        const futureDate = new Date(Date.now() + 5 * 24 * 60 * 60 * 1000);
        renderBookCard({ isAuction: true, auctionEndDate: futureDate });
        expect(screen.getByText("5d left")).toBeInTheDocument();
    });

});

// ── Price ─────────────────────────────────────────────────────────────────────

describe("BookCard — Price", () => {

    test("renders price when provided", () => {
        renderBookCard({ price: 22.00 });
        expect(screen.getByText("22.00")).toBeInTheDocument();
    });

    test("does not render price section when price is not provided", () => {
        renderBookCard({ price: undefined });
        expect(screen.queryByText(/\.00/)).not.toBeInTheDocument();
    });

    test("renders correct price for different values", () => {
        renderBookCard({ price: 9.99 });
        expect(screen.getByText("9.99")).toBeInTheDocument();
    });

});