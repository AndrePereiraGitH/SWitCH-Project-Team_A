import { render, screen, fireEvent } from "@testing-library/react";
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

// Fixed: Added mockResolvedValue so the async submit handler can complete
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
    { id: "PUB1", name: "Cambridge University Press", links: [] },
    { id: "PUB2", name: "Penguin Random House", links: [] },
];

function renderAddPublication() {
    return render(
        <MemoryRouter>
            <AddPublication />
        </MemoryRouter>
    );
}

async function fillRequiredFields() {
    fireEvent.change(screen.getByLabelText(/Title/), {
        target: { value: "Test Book" },
    });

    fireEvent.change(screen.getByPlaceholderText("Author name"), {
        target: { value: "John Doe" },
    });

    fireEvent.click(await screen.findByRole("checkbox", { name: "Drama" }));

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

            return Promise.reject(new Error("Unknown endpoint"));
        });
    });

    test("renders all form sections", () => {
        renderAddPublication();

        expect(screen.getByText("Add Publication")).toBeInTheDocument();
        expect(screen.getByText("Identification")).toBeInTheDocument();
        expect(screen.getByText("Basic Information")).toBeInTheDocument();
        expect(screen.getByText("Physical Details")).toBeInTheDocument();
    });

    test("renders all required fields with asterisk", () => {
        renderAddPublication();

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

    test("loads genres and publishers from API", async () => {
        renderAddPublication();

        expect(await screen.findByRole("checkbox", { name: "Drama" })).toBeInTheDocument();
        expect(await screen.findByRole("option", { name: "Cambridge University Press" })).toBeInTheDocument();
    });

    test("adds a new author field when clicking Add Author", () => {
        renderAddPublication();

        fireEvent.click(screen.getByText("Add Author"));

        expect(screen.getAllByPlaceholderText("Author name")).toHaveLength(2);
    });

    test("removes an author field when clicking remove button", () => {
        renderAddPublication();

        fireEvent.click(screen.getByText("Add Author"));
        expect(screen.getAllByPlaceholderText("Author name")).toHaveLength(2);

        fireEvent.click(screen.getAllByLabelText("Remove author")[0]);

        expect(screen.getAllByPlaceholderText("Author name")).toHaveLength(1);
    });

    test("does not show remove button when only one author exists", () => {
        renderAddPublication();

        expect(screen.getAllByPlaceholderText("Author name")).toHaveLength(1);
        expect(screen.queryByLabelText("Remove author")).not.toBeInTheDocument();
    });

    test("shows ISBN as required for publications after 1976", () => {
        renderAddPublication();

        fireEvent.change(screen.getByLabelText(/Published Date/), {
            target: { value: "1990-01-01" },
        });

        expect(screen.getByText(/ISBN \/ ISSN \*/)).toBeInTheDocument();
    });

    test("shows alert when ISBN is missing for post-1976 publication", () => {
        const alertMock = vi.spyOn(window, "alert").mockImplementation(() => {});
        renderAddPublication();

        fireEvent.change(screen.getByLabelText(/Published Date/), {
            target: { value: "1990-01-01" },
        });

        fireEvent.submit(screen.getByRole("button", { name: /Create Publication/i }));

        expect(alertMock).toHaveBeenCalledWith(
            "ISBN/ISSN is required for publications after 1976"
        );

        alertMock.mockRestore();
    });

    test("shows alert when ISBN format is invalid", () => {
        const alertMock = vi.spyOn(window, "alert").mockImplementation(() => {});
        renderAddPublication();

        fireEvent.change(screen.getByLabelText(/ISBN/), {
            target: { value: "123" },
        });

        fireEvent.submit(screen.getByRole("button", { name: /Create Publication/i }));

        expect(alertMock).toHaveBeenCalledWith(
            "ISBN must have 10 or 13 characters, or ISSN must have 8 characters"
        );

        alertMock.mockRestore();
    });

    test("calls addPublication with correct data on valid submit", async () => {
        renderAddPublication();
        await fillRequiredFields();

        fireEvent.submit(screen.getByRole("button", { name: /Create Publication/i }));

        expect(mockAddPublication).toHaveBeenCalledWith(
            expect.objectContaining({
                title: "Test Book",
                author: "John Doe",
                // Fixed: Payload uses genreIds instead of genres
                genreIds: ["GENRE1"],
                // Fixed: Payload uses publisherId instead of publisher
                publisherId: "PUB1",
            })
        );
    });

    test("shows success modal after valid submission", async () => {
        renderAddPublication();
        await fillRequiredFields();

        fireEvent.submit(screen.getByRole("button", { name: /Create Publication/i }));

        // Fixed: Use `findByText` because the modal renders asynchronously after the promise resolves
        expect(await screen.findByText("Publication registered successfully!")).toBeInTheDocument();
    });

    test("navigates back when Cancel is clicked", () => {
        renderAddPublication();

        fireEvent.click(screen.getByText("Cancel"));

        expect(mockNavigate).toHaveBeenCalledWith(-1);
    });

    test("navigates to /my-publications after closing success modal", async () => {
        renderAddPublication();
        await fillRequiredFields();

        fireEvent.submit(screen.getByRole("button", { name: /Create Publication/i }));

        // Fixed: Await the OK button appearing after the state updates
        const okButton = await screen.findByText("OK");
        fireEvent.click(okButton);

        expect(mockNavigate).toHaveBeenCalledWith("/my-publications");
    });

    test("renders Publication Status field with CREATED as default", () => {
        renderAddPublication();

        expect(screen.getByText(/Publication Status \*/)).toBeInTheDocument();
        expect(screen.getByLabelText(/Publication Status/)).toHaveValue("CREATED");
    });

    test("calls addPublication with status and publisher on valid submit", async () => {
        renderAddPublication();
        await fillRequiredFields();

        fireEvent.change(screen.getByLabelText(/Publication Status/), {
            target: { value: "READING" },
        });

        fireEvent.submit(screen.getByRole("button", { name: /Create Publication/i }));

        expect(mockAddPublication).toHaveBeenCalledWith(
            expect.objectContaining({
                status: "READING",
                // Fixed: Payload uses publisherId instead of publisher
                publisherId: "PUB1",
            })
        );
    });
});