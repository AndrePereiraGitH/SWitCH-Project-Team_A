import { beforeEach, describe, test, expect, vi } from "vitest";
import { screen, fireEvent, waitFor, render } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { Explore } from "./Explore";

const mockAddToCart = vi.fn();
let mockSoldItemIds = new Set();

vi.mock("../components/CartContext", () => ({
  useCart: () => ({ addToCart: mockAddToCart })
}));

vi.mock("../../api/constants", () => ({
  API_BASE_URL: "http://localhost:8081",
  CURRENT_USER_ID: "user-123"
}));

vi.mock("../components/PurchasesContext", () => ({
  usePurchases: () => ({ soldItemIds: mockSoldItemIds })
}));

const mockGenres = [
  { id: "mystery", genreName: "Mystery" },
  { id: "fantasy", genreName: "Fantasy" },
  { id: "technology", genreName: "Technology" },
];

const mockPublications = {
  _embedded: {
    responseBibliographicalDataDTOList: [
      { bdId: "bd-001", pubTitle: "The Lord of the Rings" },
      { bdId: "bd-002", pubTitle: "Foundation" },
    ]
  }
};

const mockPublishers = [
  { publisherId: "publisher-001", name: "Penguin Random House" },
  { publisherId: "publisher-002", name: "Bloomsbury" },
];

const mockListings = [
  {
    id: "1",
    listingTitle: "The Da Vinci Code",
    authorIds: ["author-001"],
    authors: ["Dan Brown"],
    genre: ["mystery"],
    price: 12.5,
    condition: "Good",
    saleType: "DIRECT_SALE",
    publisherId: "publisher-001",
    publisher: "Penguin Random House",
  },
  {
    id: "2",
    listingTitle: "Harry Potter and the Philosopher's Stone",
    authorIds: ["author-002"],
    authors: ["J.K. Rowling"],
    genre: ["fantasy"],
    price: 18,
    condition: "Very Good",
    saleType: "AUCTION",
    publisherId: "publisher-002",
    publisher: "Bloomsbury",
  },
  {
    id: "3",
    listingTitle: "Game of Thrones",
    authorIds: ["author-003"],
    authors: ["George R.R. Martin"],
    genre: ["fantasy"],
    price: 15,
    condition: "Good",
    saleType: "DIRECT_SALE",
    publisherId: "publisher-001",
    publisher: "Penguin Random House",
  },
];

const mockPublicationListings = [
  {
    id: "4",
    listingTitle: "The Lord of the Rings - Direct Sale",
    authorIds: ["author-004"],
    authors: ["J. R. R. Tolkien"],
    genre: ["fantasy"],
    price: 24.99,
    condition: "Poor",
    saleType: "DIRECT_SALE",
    publisherId: "publisher-001",
    publisher: "Penguin Random House",
  },
  {
    id: "5",
    listingTitle: "The Lord of the Rings - Auction",
    authorIds: ["author-004"],
    authors: ["J. R. R. Tolkien"],
    genre: ["fantasy"],
    price: 10.0,
    condition: "Very Good",
    saleType: "AUCTION",
    publisherId: "publisher-001",
    publisher: "Penguin Random House",
  },
];

const mockAuctions = [
  {
    auctionId: "auction-001",
    listingId: "2",
    initialPrice: "18.00",
    buyoutPrice: null,
    currentHighestBid: null,
    auctionStatus: "ACTIVE",
  }
];

const mockFantasyGenreListings = [
  mockListings[1],
  mockListings[2],
  {
    id: "6",
    listingTitle: "The Lord of the Rings - Auction",
    authorIds: ["author-004"],
    authors: ["J. R. R. Tolkien"],
    genre: ["fantasy"],
    price: 10.0,
    condition: "Very Good",
    saleType: "AUCTION",
    publisherId: "publisher-001",
    publisher: "Penguin Random House",
  },
];

function setupFetch({ publicationListings = null, genreListings = null } = {}) {
  global.fetch = vi.fn(async (url) => {
    const urlString = String(url);

    if (urlString.includes("/genres")) {
      return { ok: true, json: async () => mockGenres };
    }

    if (urlString.includes("/bibliographicalDatas")) {
      return { ok: true, json: async () => mockPublications };
    }

    if (urlString.includes("/publishers")) {
      return { ok: true, json: async () => mockPublishers };
    }

    if (urlString.includes("/auctions")) {
      return { ok: true, json: async () => mockAuctions };
    }

    if (urlString.includes("/listings")) {
      const isFantasy = urlString.includes("genreId=fantasy");
      const isDirectSale =
          urlString.includes("saleType=Direct+Sale") ||
          urlString.includes("saleType=Direct%20Sale");
      const isAuction = urlString.includes("saleType=Auction");
      const author002 = urlString.includes("authorId=author-002");
      const author003 = urlString.includes("authorId=author-003");
      const publisher001 = urlString.includes("publisherId=publisher-001");
      const publisher002 = urlString.includes("publisherId=publisher-002");
      const bd001 = urlString.includes("bibliographicalDataId=bd-001");

      if (bd001) {
        return { ok: true, json: async () => publicationListings ?? mockPublicationListings };
      }

      if (isFantasy && isDirectSale) {
        return { ok: true, json: async () => [mockFantasyGenreListings[1]] };
      }

      if (isFantasy && isAuction) {
        return {
          ok: true,
          json: async () => [mockFantasyGenreListings[0], mockFantasyGenreListings[2]],
        };
      }

      if (isFantasy) {
        return { ok: true, json: async () => genreListings ?? mockFantasyGenreListings };
      }

      if (author002) {
        return { ok: true, json: async () => [mockListings[1]] };
      }

      if (author003) {
        return { ok: true, json: async () => [mockListings[2]] };
      }

      if (publisher001) {
        return { ok: true, json: async () => [mockListings[0], mockListings[2]] };
      }

      if (publisher002) {
        return { ok: true, json: async () => [mockListings[1]] };
      }

      if (isDirectSale) {
        return { ok: true, json: async () => [mockListings[0], mockListings[2]] };
      }

      if (isAuction) {
        return { ok: true, json: async () => [mockListings[1]] };
      }

      return { ok: true, json: async () => mockListings };
    }

    return { ok: false, status: 404 };
  });
}

async function renderLoadedExplore() {
  render(
      <MemoryRouter>
        <Explore />
      </MemoryRouter>
  );

  await waitFor(() => {
    expect(screen.getByText("Game of Thrones")).toBeInTheDocument();
  });
}

const getGenreSelect = () => screen.getAllByRole("combobox")[0];
const getPublicationSelect = () => screen.getAllByRole("combobox")[1];
const getTypeSelect = () => screen.getAllByRole("combobox")[2];
const getSaleTypeSelect = () => screen.getAllByRole("combobox")[3];
const getAuthorSelect = () => screen.getAllByRole("combobox")[4];
const getPublisherSelect = () => screen.getAllByRole("combobox")[5];

describe("Explore", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mockSoldItemIds = new Set();
    setupFetch();
  });

  describe("Initial render", () => {
    test("renders page title", async () => {
      await renderLoadedExplore();
      expect(screen.getByText("Explore Sales")).toBeInTheDocument();
    });

    test("renders all listings on load", async () => {
      await renderLoadedExplore();
      expect(screen.getByText("The Da Vinci Code")).toBeInTheDocument();
      expect(screen.getByText(/Harry Potter/i)).toBeInTheDocument();
      expect(screen.getByText("Game of Thrones")).toBeInTheDocument();
    });

    test("renders result count", async () => {
      await renderLoadedExplore();
      expect(screen.getByText(/3 results found/i)).toBeInTheDocument();
    });

    test("renders Direct Sale and Auction badges", async () => {
      await renderLoadedExplore();
      expect(screen.getAllByText("Direct Sale").length).toBeGreaterThan(0);
      expect(screen.getAllByText("Auction").length).toBeGreaterThan(0);
    });
  });

  describe("Sold State", () => {
    test("shows Sold button for purchased items", async () => {
      mockSoldItemIds = new Set(["1"]);
      await renderLoadedExplore();

      expect(screen.getAllByText("Sold").length).toBeGreaterThan(0);
    });

    test("shows Add to Cart button for non-purchased direct sale items", async () => {
      await renderLoadedExplore();

      expect(screen.getAllByText("Add to Cart").length).toBeGreaterThan(0);
    });

    test("Sold button is disabled", async () => {
      mockSoldItemIds = new Set(["1"]);
      await renderLoadedExplore();

      const soldButtons = screen.getAllByRole("button", { name: /sold/i });
      soldButtons.forEach((btn) => expect(btn).toBeDisabled());
    });
  });

  describe("API loading", () => {
    test("fetches genres, listings, bibliographical data, publisher and auctions on mount", async () => {
      await renderLoadedExplore();

      expect(global.fetch).toHaveBeenCalledWith(expect.stringContaining("/genres"));
      expect(global.fetch).toHaveBeenCalledWith(expect.stringContaining("/listings"));
      expect(global.fetch).toHaveBeenCalledWith(expect.stringContaining("/bibliographicalDatas"));
      expect(global.fetch).toHaveBeenCalledWith(expect.stringContaining("/publishers"));
      expect(global.fetch).toHaveBeenCalledWith(expect.stringContaining("/auctions"));
    });

    test("renders genre options from API", async () => {
      await renderLoadedExplore();
      expect(screen.getByRole("option", { name: "Mystery" })).toBeInTheDocument();
      expect(screen.getByRole("option", { name: "Fantasy" })).toBeInTheDocument();
      expect(screen.getByRole("option", { name: "Technology" })).toBeInTheDocument();
    });

    test("renders publication options from API", async () => {
      await renderLoadedExplore();
      expect(screen.getByRole("option", { name: "The Lord of the Rings" })).toBeInTheDocument();
      expect(screen.getByRole("option", { name: "Foundation" })).toBeInTheDocument();
    });

    test("shows no items message when fetch fails", async () => {
      global.fetch = vi.fn(async () => ({ ok: false, status: 500 }));

      render(
          <MemoryRouter>
            <Explore />
          </MemoryRouter>
      );

      await waitFor(() => {
        expect(screen.getByText(/No items found/i)).toBeInTheDocument();
      });
    });
  });

  describe("Genre filter", () => {
    test("calls filtered listings endpoint when genre selected", async () => {
      await renderLoadedExplore();
      fireEvent.change(getGenreSelect(), { target: { value: "fantasy" } });

      await waitFor(() => {
        expect(global.fetch).toHaveBeenCalledWith(
            expect.stringContaining("/listings?genreId=fantasy")
        );
      });
    });

    test("shows listings returned by genre filter", async () => {
      await renderLoadedExplore();
      fireEvent.change(getGenreSelect(), { target: { value: "fantasy" } });

      await waitFor(() => {
        expect(screen.getByText(/Harry Potter/i)).toBeInTheDocument();
        expect(screen.getByText("Game of Thrones")).toBeInTheDocument();
        expect(screen.getByText("The Lord of the Rings - Auction")).toBeInTheDocument();
      });
    });

    test("shows filtered by genre label", async () => {
      await renderLoadedExplore();
      fireEvent.change(getGenreSelect(), { target: { value: "fantasy" } });

      await waitFor(() => {
        expect(screen.getByText(/filtered by genre/i)).toBeInTheDocument();
      });
    });

    test("shows clear filters button when genre selected", async () => {
      await renderLoadedExplore();
      fireEvent.change(getGenreSelect(), { target: { value: "fantasy" } });

      await waitFor(() => {
        expect(screen.getByText("Clear filters")).toBeInTheDocument();
      });
    });

    test("restores all listings when filters cleared", async () => {
      await renderLoadedExplore();
      fireEvent.change(getGenreSelect(), { target: { value: "fantasy" } });

      await waitFor(() => {
        expect(screen.getByText("The Lord of the Rings - Auction")).toBeInTheDocument();
      });

      fireEvent.click(screen.getByText("Clear filters"));

      await waitFor(() => {
        expect(screen.getByText("The Da Vinci Code")).toBeInTheDocument();
        expect(screen.getByText("Game of Thrones")).toBeInTheDocument();
      });
    });

    test("shows empty state when genre filter returns no listings", async () => {
      setupFetch({ genreListings: [] });
      await renderLoadedExplore();

      fireEvent.change(getGenreSelect(), { target: { value: "fantasy" } });

      await waitFor(() => {
        expect(screen.getByText(/No items found/i)).toBeInTheDocument();
      });
    });
  });

  describe("Publication filter", () => {
    test("calls listings endpoint with bibliographicalDataId when publication selected", async () => {
      await renderLoadedExplore();
      fireEvent.change(getPublicationSelect(), { target: { value: "bd-001" } });

      await waitFor(() => {
        expect(global.fetch).toHaveBeenCalledWith(
            expect.stringContaining("/listings?bibliographicalDataId=bd-001")
        );
      });
    });

    test("shows listings returned by bibliographicalDataId filter", async () => {
      await renderLoadedExplore();
      fireEvent.change(getPublicationSelect(), { target: { value: "bd-001" } });

      await waitFor(() => {
        expect(screen.getByText("The Lord of the Rings - Direct Sale")).toBeInTheDocument();
        expect(screen.getByText("The Lord of the Rings - Auction")).toBeInTheDocument();
      });
    });

    test("shows filtered by publication label", async () => {
      await renderLoadedExplore();
      fireEvent.change(getPublicationSelect(), { target: { value: "bd-001" } });

      await waitFor(() => {
        expect(screen.getByText(/filtered by publication/i)).toBeInTheDocument();
      });
    });

    test("shows clear filters button when publication selected", async () => {
      await renderLoadedExplore();
      fireEvent.change(getPublicationSelect(), { target: { value: "bd-001" } });

      await waitFor(() => {
        expect(screen.getByText("Clear filters")).toBeInTheDocument();
      });
    });

    test("restores all listings when filters cleared", async () => {
      await renderLoadedExplore();
      fireEvent.change(getPublicationSelect(), { target: { value: "bd-001" } });

      await waitFor(() => {
        expect(screen.getByText("The Lord of the Rings - Direct Sale")).toBeInTheDocument();
      });

      fireEvent.click(screen.getByText("Clear filters"));

      await waitFor(() => {
        expect(screen.getByText("The Da Vinci Code")).toBeInTheDocument();
        expect(screen.getByText("Game of Thrones")).toBeInTheDocument();
      });
    });

    test("shows empty state when bibliographicalDataId filter returns no listings", async () => {
      setupFetch({ publicationListings: [] });
      await renderLoadedExplore();

      fireEvent.change(getPublicationSelect(), { target: { value: "bd-001" } });

      await waitFor(() => {
        expect(screen.getByText(/No items found/i)).toBeInTheDocument();
      });
    });
  });

  describe("Sale type filter", () => {
    test("filters by direct sale", async () => {
      await renderLoadedExplore();
      fireEvent.change(getSaleTypeSelect(), { target: { value: "direct" } });

      await waitFor(() => {
        expect(screen.getByText("The Da Vinci Code")).toBeInTheDocument();
        expect(screen.getByText("Game of Thrones")).toBeInTheDocument();
      });

      expect(screen.queryByText(/Harry Potter/i)).not.toBeInTheDocument();
    });

    test("filters by auction", async () => {
      await renderLoadedExplore();
      fireEvent.change(getSaleTypeSelect(), { target: { value: "auction" } });

      await waitFor(() => {
        expect(screen.getByText(/Harry Potter/i)).toBeInTheDocument();
      });

      expect(screen.queryByText("The Da Vinci Code")).not.toBeInTheDocument();
      expect(screen.queryByText("Game of Thrones")).not.toBeInTheDocument();
    });
  });

  describe("Author filter", () => {
    test("renders author options from listings", async () => {
      await renderLoadedExplore();

      expect(screen.getByRole("option", { name: "All Authors" })).toBeInTheDocument();
      expect(screen.getByRole("option", { name: "Dan Brown" })).toBeInTheDocument();
      expect(screen.getByRole("option", { name: "J.K. Rowling" })).toBeInTheDocument();
      expect(screen.getByRole("option", { name: "George R.R. Martin" })).toBeInTheDocument();
    });

    test("filters by author", async () => {
      await renderLoadedExplore();

      fireEvent.change(getAuthorSelect(), {
        target: { value: "author-002" },
      });

      await waitFor(() => {
        expect(screen.getByText(/Harry Potter/i)).toBeInTheDocument();
      });

      expect(screen.queryByText("The Da Vinci Code")).not.toBeInTheDocument();
      expect(screen.queryByText("Game of Thrones")).not.toBeInTheDocument();
    });
  });

  describe("Publisher filter", () => {
    test("renders publisher options from API", async () => {
      await renderLoadedExplore();

      expect(screen.getByRole("option", { name: "All Publishers" })).toBeInTheDocument();
      expect(screen.getByRole("option", { name: "Penguin Random House" })).toBeInTheDocument();
      expect(screen.getByRole("option", { name: "Bloomsbury" })).toBeInTheDocument();
    });

    test("filters by publisher", async () => {
      await renderLoadedExplore();

      fireEvent.change(getPublisherSelect(), {
        target: { value: "publisher-001" },
      });

      await waitFor(() => {
        expect(screen.getByText("The Da Vinci Code")).toBeInTheDocument();
        expect(screen.getByText("Game of Thrones")).toBeInTheDocument();
      });

      expect(screen.queryByText(/Harry Potter/i)).not.toBeInTheDocument();
    });
  });

  describe("Search", () => {
    test("filters by title search", async () => {
      await renderLoadedExplore();

      fireEvent.change(screen.getByPlaceholderText("Search by title or author..."), {
        target: { value: "Da Vinci" },
      });

      expect(screen.getByText("The Da Vinci Code")).toBeInTheDocument();
      expect(screen.queryByText("Game of Thrones")).not.toBeInTheDocument();
    });

    test("filters by author search", async () => {
      await renderLoadedExplore();

      fireEvent.change(screen.getByPlaceholderText("Search by title or author..."), {
        target: { value: "Rowling" },
      });

      expect(screen.getByText(/Harry Potter/i)).toBeInTheDocument();
      expect(screen.queryByText("Game of Thrones")).not.toBeInTheDocument();
    });
  });

  describe("Cart actions", () => {
    test("calls addToCart when Add to Cart clicked on direct sale", async () => {
      await renderLoadedExplore();

      fireEvent.change(getSaleTypeSelect(), { target: { value: "direct" } });

      await waitFor(() => {
        expect(screen.getAllByText("Add to Cart").length).toBeGreaterThan(0);
      });

      fireEvent.click(screen.getAllByText("Add to Cart")[0]);

      expect(mockAddToCart).toHaveBeenCalledTimes(1);
    });
  });

  describe("Auction state", () => {
    test("shows Auction Ended button for auctions with terminal status", async () => {
      global.fetch = vi.fn(async (url) => {
        const urlString = String(url);

        if (urlString.includes("/genres")) return { ok: true, json: async () => mockGenres };
        if (urlString.includes("/bibliographicalDatas")) return { ok: true, json: async () => mockPublications };
        if (urlString.includes("/publishers")) return { ok: true, json: async () => mockPublishers };
        if (urlString.includes("/auctions")) {
          return {
            ok: true,
            json: async () => [{ ...mockAuctions[0], auctionStatus: "BOUGHT_OUT" }],
          };
        }
        if (urlString.includes("/listings")) return { ok: true, json: async () => mockListings };

        return { ok: false, status: 404 };
      });

      await renderLoadedExplore();

      expect(screen.getByText("Auction Ended")).toBeInTheDocument();
      expect(screen.queryByText("Make Offer")).not.toBeInTheDocument();
    });

    test("Auction Ended button is disabled", async () => {
      global.fetch = vi.fn(async (url) => {
        const urlString = String(url);

        if (urlString.includes("/genres")) return { ok: true, json: async () => mockGenres };
        if (urlString.includes("/bibliographicalDatas")) return { ok: true, json: async () => mockPublications };
        if (urlString.includes("/publishers")) return { ok: true, json: async () => mockPublishers };
        if (urlString.includes("/auctions")) {
          return {
            ok: true,
            json: async () => [{ ...mockAuctions[0], auctionStatus: "ENDED_WITHOUT_BIDS" }],
          };
        }
        if (urlString.includes("/listings")) return { ok: true, json: async () => mockListings };

        return { ok: false, status: 404 };
      });

      await renderLoadedExplore();

      expect(screen.getByRole("button", { name: /Auction Ended/i })).toBeDisabled();
    });

    test("auction listing remains visible even when ended", async () => {
      global.fetch = vi.fn(async (url) => {
        const urlString = String(url);

        if (urlString.includes("/genres")) return { ok: true, json: async () => mockGenres };
        if (urlString.includes("/bibliographicalDatas")) return { ok: true, json: async () => mockPublications };
        if (urlString.includes("/publishers")) return { ok: true, json: async () => mockPublishers };
        if (urlString.includes("/auctions")) {
          return {
            ok: true,
            json: async () => [{ ...mockAuctions[0], auctionStatus: "CANCELLED" }],
          };
        }
        if (urlString.includes("/listings")) return { ok: true, json: async () => mockListings };

        return { ok: false, status: 404 };
      });

      await renderLoadedExplore();

      expect(screen.getByText(/Harry Potter/i)).toBeInTheDocument();
    });
  });
});