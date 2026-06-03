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

const mockAddPublication = vi.fn();
vi.mock("../components/PublicationsContext", () => ({
    usePublications: () => ({
        addPublication: mockAddPublication,
    }),
}));

function renderAddPublication() {
    return render(
        <MemoryRouter>
            <AddPublication />
        </MemoryRouter>
    );
}

function fillRequiredFields() {
    fireEvent.change(screen.getByLabelText(/Title/), {
        target: { value: "Test Book" },
    });
    fireEvent.change(screen.getByPlaceholderText("Author name"), {
        target: { value: "John Doe" },
    });
    fireEvent.change(screen.getByLabelText(/Genre/), {
        target: { value: "Fiction" },
    });
    fireEvent.change(screen.getByLabelText(/Publisher/), {
        target: { value: "Cambridge University Press" },
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
        expect(screen.getByText(/Genre \*/)).toBeInTheDocument();
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
        expect(screen.queryByTitle(/remove/i)).not.toBeInTheDocument();
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

    test("calls addPublication with correct data on valid submit", () => {
        renderAddPublication();
        fillRequiredFields();

        fireEvent.submit(screen.getByRole("button", { name: /Create Publication/i }));

        expect(mockAddPublication).toHaveBeenCalledWith(
            expect.objectContaining({
                title: "Test Book",
                author: "John Doe",
                genre: "Fiction",
            })
        );
    });

    test("shows success modal after valid submission", () => {
        renderAddPublication();
        fillRequiredFields();

        fireEvent.submit(screen.getByRole("button", { name: /Create Publication/i }));

        expect(screen.getByText("Publication registered successfully!")).toBeInTheDocument();
    });

    test("navigates back when Cancel is clicked", () => {
        renderAddPublication();

        fireEvent.click(screen.getByText("Cancel"));

        expect(mockNavigate).toHaveBeenCalledWith(-1);
    });

    test("navigates to /my-publications after closing success modal", () => {
        renderAddPublication();
        fillRequiredFields();

        fireEvent.submit(screen.getByRole("button", { name: /Create Publication/i }));
        fireEvent.click(screen.getByText("OK"));

        expect(mockNavigate).toHaveBeenCalledWith("/my-publications");
    });

    test("renders Publication Status field with CREATED as default", () => {
        renderAddPublication();

        expect(screen.getByText(/Publication Status \*/)).toBeInTheDocument();
        expect(screen.getByLabelText(/Publication Status/)).toHaveValue("CREATED");
    });

    test("calls addPublication with status and publisher on valid submit", () => {
        renderAddPublication();
        fillRequiredFields();

        fireEvent.change(screen.getByLabelText(/Publication Status/), {
            target: { value: "READING" },
        });

        fireEvent.submit(screen.getByRole("button", { name: /Create Publication/i }));

        expect(mockAddPublication).toHaveBeenCalledWith(
            expect.objectContaining({
                status: "READING",
                publisher: "Cambridge University Press",
            })
        );
    });
});