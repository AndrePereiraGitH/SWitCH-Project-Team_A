import { describe, test, expect, vi, beforeEach } from "vitest";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { renderWithProviders } from "../../test-utils";
import { usePublications } from "./PublicationsContext";
import { useLinks } from "./LinksContext";

const mockBookA = {
    title: "Book A",
    authorNames: ["AUTH1"],
    genreNames: ["GENRE1"],
    authorDisplayNames: ["Author A"],
    genreDisplayNames: ["Fiction"],
    mediaFormat: "Book",
    condition: "New",
    publisherName: "Pub A",
    originalLanguage: "en",
    publishedDate: "2024-01-01",
    editionNumber: "1",
    height: 10, width: 10, depth: 10, weight: 100, numberOfPages: 100,
    publicationTypeId: "pt-1",
    publisherId: "pub-1",
    editionLanguage: "en",
    typeOfBinding: "Paperback"
};

const mockBookB = { ...mockBookA, title: "Book B" };

const rootResponse = {
    _links: {
        "find-in-library": {
            href: "http://localhost:8081/publicationSamples/in-library?userId={userId}",
            templated: true
        },
        "create-bibliographical-data": {
            href: "http://localhost:8081/bibliographicalDatas?userId={userId}",
            templated: true
        },
        "create-edition": {
            href: "http://localhost:8081/editions?userId={userId}",
            templated: true
        },
        "create-publication-sample": {
            href: "http://localhost:8081/publicationSamples?userId={userId}",
            templated: true
        },
        "add-to-library": {
            href: "http://localhost:8081/publicationSamples/{publicationSampleId}/library?userId={userId}",
            templated: true
        }
    }
};

// Exposes the loading state of LinksContext to wait until links are ready
function LinksReady({ children }) {
    const { loading } = useLinks();
    return loading ? <p data-testid="links-loading">true</p> : children;
}

function TestPublications() {
    const { publications, addPublication, getPublicationsForSale, getAllPublications } = usePublications();
    return (
        <div>
            <p data-testid="count">{publications.length}</p>
            <ul>
                {publications.map(pub => (
                    <li key={pub.id} data-testid={`pub-${pub.id}`}>
                        <span data-testid={`title-${pub.id}`}>{pub.title}</span>
                        <span data-testid={`forsale-${pub.id}`}>{pub.forSale ? "for-sale" : "not-for-sale"}</span>
                    </li>
                ))}
            </ul>
            <button onClick={() => addPublication(mockBookA)}>Add Book A</button>
            <button onClick={() => addPublication(mockBookB)}>Add Book B</button>
            <p data-testid="all-count">{getAllPublications().length}</p>
            <p data-testid="sale-count">{getPublicationsForSale().length}</p>
        </div>
    );
}

function TestPublicationsWithActions() {
    const { publications, addPublication, setPublicationForSale } = usePublications();
    const first = publications[0];
    return (
        <div>
            <p data-testid="count">{publications.length}</p>
            {first && (
                <>
                    <span data-testid="first-title">{first.title}</span>
                    <span data-testid="first-forsale">{first.forSale ? "for-sale" : "not-for-sale"}</span>
                    <button onClick={() => setPublicationForSale(first.id, 15)}>Sell First</button>
                </>
            )}
            <button onClick={() => addPublication(mockBookA)}>Add Book A</button>
        </div>
    );
}

function renderPublications() {
    return renderWithProviders(
        <LinksReady>
            <TestPublications />
        </LinksReady>
    );
}

function renderPublicationsWithActions() {
    return renderWithProviders(
        <LinksReady>
            <TestPublicationsWithActions />
        </LinksReady>
    );
}

async function waitForLinks() {
    await waitFor(() =>
        expect(screen.queryByTestId("links-loading")).not.toBeInTheDocument()
    );
}

describe("PublicationsContext", () => {

    beforeEach(() => {
        vi.clearAllMocks();

        global.fetch = vi.fn(async (url, options) => {
            if (url.includes("/?userId=") || url.match(/^http:\/\/localhost:8081\/\?/)) {
                return { ok: true, status: 200, json: async () => rootResponse };
            }
            if (url.includes("/in-library") || (url.includes("/publicationSamples") && (!options || options.method === "GET"))) {
                return { ok: true, json: async () => [] };
            }
            if (url.includes("/bibliographicalDatas")) {
                return { ok: true, json: async () => ({ bdId: "bd-mock" }) };
            }
            if (url.includes("/editions")) {
                return { ok: true, json: async () => ({ editionId: "ed-mock" }) };
            }
            if (url.includes("/publicationSamples") && options?.method === "POST") {
                return { ok: true, json: async () => ({ id: Math.random().toString() }) };
            }
            return { ok: false, status: 404, text: async () => "Not found" };
        });
    });

    describe("Initial State", () => {
        test("publications list starts empty", async () => {
            renderPublications();
            await waitForLinks();
            await waitFor(() => expect(screen.getByTestId("count").textContent).toBe("0"));
        });

        test("getAllPublications returns empty array by default", async () => {
            renderPublications();
            await waitForLinks();
            await waitFor(() => expect(screen.getByTestId("all-count").textContent).toBe("0"));
        });

        test("getPublicationsForSale returns empty array by default", async () => {
            renderPublications();
            await waitForLinks();
            await waitFor(() => expect(screen.getByTestId("sale-count").textContent).toBe("0"));
        });
    });

    describe("Add Publication", () => {
        test("adding a publication increments the count to 1", async () => {
            renderPublications();
            await waitForLinks();
            fireEvent.click(screen.getByText("Add Book A"));
            await waitFor(() => expect(screen.getByTestId("count").textContent).toBe("1"));
        });

        test("added publication appears with correct title", async () => {
            renderPublications();
            await waitForLinks();
            fireEvent.click(screen.getByText("Add Book A"));
            expect(await screen.findByText("Book A")).toBeInTheDocument();
        });

        test("added publication is not for sale by default", async () => {
            renderPublications();
            await waitForLinks();
            fireEvent.click(screen.getByText("Add Book A"));
            const pub = await screen.findAllByText("not-for-sale");
            expect(pub.length).toBeGreaterThan(0);
        });

        test("adding two publications increments count to 2", async () => {
            renderPublications();
            await waitForLinks();
            fireEvent.click(screen.getByText("Add Book A"));
            fireEvent.click(screen.getByText("Add Book B"));
            await waitFor(() => expect(screen.getByTestId("count").textContent).toBe("2"));
        });

        test("getAllPublications reflects the added publications", async () => {
            renderPublications();
            await waitForLinks();
            fireEvent.click(screen.getByText("Add Book A"));
            fireEvent.click(screen.getByText("Add Book B"));
            await waitFor(() => expect(screen.getByTestId("all-count").textContent).toBe("2"));
        });
    });

    describe("Set For Sale", () => {
        test("setPublicationForSale marks the publication as for sale", async () => {
            renderPublicationsWithActions();
            await waitForLinks();
            fireEvent.click(screen.getByText("Add Book A"));

            const sellButton = await screen.findByText("Sell First");
            fireEvent.click(sellButton);

            await waitFor(() => expect(screen.getByTestId("first-forsale").textContent).toBe("for-sale"));
        });

        test("getPublicationsForSale count increases after setting for sale", async () => {
            renderPublications();
            await waitForLinks();
            await waitFor(() => expect(screen.getByTestId("sale-count").textContent).toBe("0"));
        });
    });

    describe("Error Handling", () => {
        test("usePublications throws when used outside PublicationsProvider", () => {
            const consoleSpy = vi.spyOn(console, "error").mockImplementation(() => {});

            function ComponentWithoutProvider() {
                usePublications();
                return null;
            }

            expect(() => render(<ComponentWithoutProvider />)).toThrow(
                "usePublications must be used within PublicationsProvider"
            );

            consoleSpy.mockRestore();
        });
    });
});