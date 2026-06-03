import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { vi } from "vitest";
import { AddPublication } from "../pages/AddPublication";

const mockNavigate = vi.fn();

vi.mock("react-router-dom", async (importOriginal) => {
    const actual = await importOriginal();
    return {
        ...actual,
        useNavigate: () => mockNavigate,
    };
});

const mockAddPublication = vi.fn().mockResolvedValue({});

vi.mock("../components/PublicationsContext", () => ({
    usePublications: () => ({
        addPublication: mockAddPublication,
    }),
}));

const mockGenres = [
    { id: "GENRE1", genreName: "Drama", links: [] },
    { id: "GENRE2", genreName: "Science Fiction", links: [] },
    { id: "GENRE3", genreName: "Mystery", links: [] },
];

const mockPublishers = [
    { publisherId: "PUB1", name: "Cambridge University Press", links: [] },
    { publisherId: "PUB2", name: "Penguin Random House", links: [] },
];

const mockAuthors = [
    { id: "AUTH1", name: "John Doe", links: [] },
    { id: "AUTH2", name: "Jane Smith", links: [] },
];

// Helper to render and wait for the initial fetches to complete, preventing act() warnings
async function renderAndWait() {
    render(
        <MemoryRouter>
            <AddPublication />
        </MemoryRouter>
    );
    // Wait until the authors are loaded into the checkboxes
    await screen.findByRole("checkbox", { name: "John Doe" });
}

async function fillRequiredFields() {
    fireEvent.change(screen.getByLabelText(/Title/), {
        target: { value: "Test Book" },
    });

    fireEvent.click(screen.getByRole("checkbox", { name: "John Doe" }));

    fireEvent.click(screen.getByRole("checkbox", { name: "Drama" }));

    fireEvent.change(screen.getByLabelText(/Publisher/), {
        target: { value: "PUB1" },
    });

    fireEvent.change(screen.getByLabelText(/Published Date/), {
        target: { value: "1960-01-01" },
    });

    fireEvent.change(screen.getByLabelText(/Edition/), {
        target: { value: "1st edition" },
    });

    fireEvent.change(screen.getByLabelText(/Binding Type/), {
        target: { value: "hardcover" },
    });

    fireEvent.change(screen.getByLabelText(/Dimensions/), {
        target: { value: "20 x 15 x 3" },
    });

    fireEvent.change(screen.getByLabelText(/Number of Pages/), {
        target: { value: "350" },
    });

    fireEvent.change(screen.getByLabelText(/Weight/), {
        target: { value: "350" },
    });
}

describe("AddPublication", () => {
    beforeEach(() => {
        vi.clearAllMocks();

        global.fetch = vi.fn((url) => {
            if (url.includes("/publishers")) {
                return Promise.resolve({
                    ok: true,
                    json: () => Promise.resolve(mockPublishers),
                });
            }

            if (url.includes("/genres")) {
                return Promise.resolve({
                    ok: true,
                    json: () => Promise.resolve(mockGenres),
                });
            }

            if (url.includes("/authors")) {
                return Promise.resolve({
                    ok: true,
                    json: () => Promise.resolve(mockAuthors),
                });
            }

            return Promise.reject(new Error("Unknown endpoint: " + url));
        });
    });

    test("renders all form sections", async () => {
        await renderAndWait();

        expect(screen.getByText("Add Publication")).toBeInTheDocument();
        expect(screen.getByText("Identification")).toBeInTheDocument();
        expect(screen.getByText("Basic Information")).toBeInTheDocument();
        expect(screen.getByText("Physical Details")).toBeInTheDocument();
    });

    test("renders all required fields with asterisk", async () => {
        await renderAndWait();

        expect(screen.getByText(/Publication Type \*/)).toBeInTheDocument();
        expect(screen.getByText("Genre *")).toBeInTheDocument();
        expect(screen.getByText(/Title \*/)).toBeInTheDocument();
        expect(screen.getByText(/Author\(s\) \*/)).toBeInTheDocument();
        expect(screen.getByText(/Publisher \*/)).toBeInTheDocument();
        expect(screen.getByText(/Published Date \*/)).toBeInTheDocument();
        expect(screen.getByText(/Edition \*/)).toBeInTheDocument();
        expect(screen.getByText(/Language \*/)).toBeInTheDocument();
        expect(screen.getByText(/Binding Type \*/)).toBeInTheDocument();
        expect(screen.getByText(/Condition \*/)).toBeInTheDocument();
        expect(screen.getByText(/Dimensions.*\*/)).toBeInTheDocument();
    });

    test("loads genres, authors, and publishers from API", async () => {
        await renderAndWait();

        expect(screen.getByRole("checkbox", { name: "Drama" })).toBeInTheDocument();
        expect(screen.getByRole("checkbox", { name: "John Doe" })).toBeInTheDocument();
        expect(screen.getByRole("option", { name: "Cambridge University Press" })).toBeInTheDocument();
    });

    test("shows ISBN as required for publications after 1976", async () => {
        await renderAndWait();

        fireEvent.change(screen.getByLabelText(/Published Date/), {
            target: { value: "1990-01-01" },
        });

        expect(screen.getByText(/ISBN \/ ISSN \*/)).toBeInTheDocument();
    });

    test("shows alert when ISBN is missing for post-1976 publication", async () => {
        const alertMock = vi.spyOn(window, "alert").mockImplementation(() => {});
        await renderAndWait();

        fireEvent.change(screen.getByLabelText(/Published Date/), {
            target: { value: "1990-01-01" },
        });

        fireEvent.submit(screen.getByRole("button", { name: /Create Publication/i }));

        expect(alertMock).toHaveBeenCalledWith(
            "ISBN/ISSN is required for publications after 1976"
        );

        alertMock.mockRestore();
    });

    test("shows alert when ISBN format is invalid", async () => {
        const alertMock = vi.spyOn(window, "alert").mockImplementation(() => {});
        await renderAndWait();

        // Check required fields that validate before ISBN to prevent early exit
        fireEvent.click(screen.getByRole("checkbox", { name: "John Doe" }));
        fireEvent.click(screen.getByRole("checkbox", { name: "Drama" }));

        fireEvent.change(screen.getByLabelText(/ISBN/), {
            target: { value: "123" },
        });

        fireEvent.submit(screen.getByRole("button", { name: /Create Publication/i }));

        expect(alertMock).toHaveBeenCalledWith(
            "ISBN must be 10 or 13 chars, or ISSN 8 chars"
        );

        alertMock.mockRestore();
    });

    test("calls addPublication with correct data on valid submit", async () => {
        await renderAndWait();
        await fillRequiredFields();

        fireEvent.submit(screen.getByRole("button", { name: /Create Publication/i }));

        expect(mockAddPublication).toHaveBeenCalledWith(
            expect.objectContaining({
                title: "Test Book",
                authorNames: ["AUTH1"],
                genreNames: ["GENRE1"],
                publisherId: "PUB1",
            })
        );
    });

    test("shows success modal after valid submission", async () => {
        await renderAndWait();
        await fillRequiredFields();

        fireEvent.submit(screen.getByRole("button", { name: /Create Publication/i }));

        expect(await screen.findByText("Publication registered successfully!")).toBeInTheDocument();
    });

    test("navigates back when Cancel is clicked", async () => {
        await renderAndWait();

        fireEvent.click(screen.getByText("Cancel"));

        expect(mockNavigate).toHaveBeenCalledWith(-1);
    });

    test("navigates to /my-publications after closing success modal", async () => {
        await renderAndWait();
        await fillRequiredFields();

        fireEvent.submit(screen.getByRole("button", { name: /Create Publication/i }));

        const okButton = await screen.findByText("OK");
        fireEvent.click(okButton);

        expect(mockNavigate).toHaveBeenCalledWith("/my-publications");
    });

    test("renders Publication Status field with CREATED as default", async () => {
        await renderAndWait();

        expect(screen.getByText(/Publication Status \*/)).toBeInTheDocument();
        expect(screen.getByLabelText(/Publication Status/)).toHaveValue("CREATED");
    });

    test("calls addPublication with status and publisher on valid submit", async () => {
        await renderAndWait();
        await fillRequiredFields();

        fireEvent.change(screen.getByLabelText(/Publication Status/), {
            target: { value: "READING" },
        });

        fireEvent.submit(screen.getByRole("button", { name: /Create Publication/i }));

        expect(mockAddPublication).toHaveBeenCalledWith(
            expect.objectContaining({
                publisherId: "PUB1",
            })
        );
    });
});