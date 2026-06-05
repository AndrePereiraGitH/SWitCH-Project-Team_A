# MiteLovers UI

Web interface for the **MiteLovers** project — a marketplace for buying and selling second-hand books and magazines.

---

## Tech Stack

| Technology | Purpose |
|---|---|
| React 18 | UI framework |
| JavaScript (JSX) | Implementation language |
| Vite | Development server and build tool |
| Tailwind CSS 4 | Styling |
| React Router v7 | Client-side routing |
| Lucide React | Icons |
| Vitest | Test runner |
| React Testing Library | Component testing |

---

## Project Structure

```
frontend/
├── index.html
├── vite.config.js
├── package.json
├── package-lock.json
├── setupTests.js
└── src/
    ├── main.jsx                        # Application entry point
    ├── test-utils.jsx                  # Shared test utilities (renderWithProviders)
    ├── assets/                         # Static assets (images, logos)
    ├── styles/                         # Global CSS, themes, fonts
    └── app/
        ├── App.jsx                     # Main router + provider tree
        ├── components/
        │   ├── AuthContext.jsx          # Authentication state (login/logout)
        │   ├── AuthContext.test.jsx
        │   ├── CartContext.jsx          # Shopping cart state
        │   ├── CartContext.test.jsx
        │   ├── LibraryContext.jsx       # Personal library state
        │   ├── LibraryContext.test.jsx
        │   ├── ListsContext.jsx         # Publication lists state
        │   ├── ListsContext.test.jsx
        │   ├── PublicationsContext.jsx  # Publications state
        │   ├── PublicationsContext.test.jsx
        │   ├── Navbar.jsx               # Navigation bar
        │   ├── Navbar.test.jsx
        │   ├── BookCard.jsx             # Publication card component
        │   ├── BookCard.test.jsx
        │   ├── BidModal.jsx             # Bid/offer modal
        │   ├── BidModal.test.jsx
        │   ├── ImageWithFallback.jsx    # Image with icon fallback on error
        │   └── ImageWithFallback.test.jsx
        └── pages/
            ├── LandingPage.jsx          # Home page (/)
            ├── LandingPage.test.jsx
            ├── Dashboard.jsx            # User dashboard (/dashboard)
            ├── Dashboard.test.jsx
            ├── Library.jsx              # Personal library (/library)
            ├── Library.test.jsx
            ├── Explore.jsx              # Browse marketplace (/explore)
            └── Explore.test.jsx
```

> Pages for US040–US045 are added incrementally per user story commit — see the [Commit Strategy](#commit-strategy) section below.

---

## Architecture

The application uses the **React Context API** for global state management with no external state library. Contexts are nested in the following order inside `App.jsx`:

```
AuthProvider
  └── PublicationsProvider
        └── LibraryProvider
              └── CartProvider
                    └── ListsProvider
                          └── BrowserRouter
```

Each context exposes a custom hook (`useAuth`, `usePublications`, etc.) that any child component can call. If a component calls a hook outside its provider it throws an error — which is also covered by the test suite.

---

## Pages

Each page corresponds to a route defined in `App.jsx`. Pages are in `src/app/pages/`.

### `LandingPage.jsx` — `/`
The public home page. Shows a hero section, featured items for sale, and navigation to the marketplace. Entry point for unauthenticated users.

### `Dashboard.jsx` — `/dashboard`
The logged-in user's home. Displays a summary of their library, lists, and items for sale, with quick-action links to the main sections.

### `AddPublication.jsx` — `/add-publication` — **US039**
Form to register a new publication (book or magazine). Validates ISBN/ISSN format and publication date rules. On submit, adds the publication to the shared `PublicationsContext`.

### `MyPublications.jsx` — `/my-publications`
Lists all publications registered by the user. Allows filtering by genre and type, and provides actions to add to a list or put for sale.

### `Library.jsx` — `/library` — **US039, US041**
The user's personal library. A user can create one library, give it a name, and add publications to it. Supports grid/list view, filtering, and deletion.

### `ManageLists.jsx` — `/lists` — **US040, US042, US043**
Create and manage private reading lists. Each list has a name, genre, and optional public sharing. Publications can be added or removed from each list.

### `ListDetail.jsx` — `/lists/:listId` — **US042, US043**
Shows all publications in a specific list. Allows removing publications and toggling the list's public/private status.

### `SellPublication.jsx` — `/sell/:bookId` — **US044**
Form to put a publication from the library up for sale. Supports direct sale (fixed price) and auction modes. Navigated to from `MyPublications`.

### `Explore.jsx` — `/explore` — **US045**
Public marketplace page. Lists all items for sale with filters for genre, publication type, sale type (direct/auction), and price range. Includes a search bar.

### `ProductDetail.jsx` — `/product/:productId`
Detail page for a single item for sale. Shows full publication info, price, condition, seller details, and buy/bid actions.

---

## Components

Reusable components are in `src/app/components/`. They are either UI building blocks or modal dialogs.

### `Navbar.jsx`
Fixed top navigation bar. Shows logo, search, main links (Marketplace, Community), cart icon with item count, and user login/logout dropdown when authenticated. Uses `AuthContext` and `CartContext`.

### `BookCard.jsx`
Card component for displaying a publication. Shows title, author, genre, condition, price, and auction time remaining. Used in `Explore`, `MyPublications`, and `Library`.

### `BidModal.jsx`
Modal dialog for placing a bid on an auction item. Shows current bid, optional buyout price, and a number input for the user's offer.

### `AddToListModal.jsx`
Modal for adding a publication to one or more of the user's lists. Shows checkboxes for each list and a Done button. Uses `ListsContext`.

### `SuccessModal.jsx`
Generic success confirmation modal with a green checkmark, a message, and an OK button. Used after form submissions.

---

## Getting Started

### Prerequisites

- Node.js v18 or higher
- npm (included with Node.js)

### Installation

```bash
# Install dependencies
npm install

# Start the development server
npm run dev
```

The application will be available at **http://localhost:5173**

---

## Testing

The project uses **Vitest** and **React Testing Library** with **jsdom**.

### Run tests

```bash
npm test              # run once
npm run test:watch    # watch mode during development
```

### Test utilities — `src/test-utils.jsx`

Every test file imports from `test-utils` instead of directly from `@testing-library/react`. This file wraps components in the full provider tree so individual tests do not need to repeat the setup.

```jsx
import { screen, fireEvent, renderWithProviders } from "../../test-utils";
import { MyComponent } from "./MyComponent";

describe("MyComponent", () => {
    test("renders correctly", () => {
        renderWithProviders(<MyComponent />);
        expect(screen.getByText("Expected text")).toBeInTheDocument();
    });
});
```

**Key rules:**

| Rule | Detail |
|---|---|
| Always use `renderWithProviders` | Never use bare `render` from `@testing-library/react` |
| Use `render` directly only | When testing that a hook throws outside its provider |
| Named imports | `import { X } from "./X"` not `import X from "./X"` |
| Path to test-utils | Always `../../test-utils` from `pages/` or `components/` |
| Test file location | Place the test file next to the component it tests |

---

## Commit Strategy

The codebase is built incrementally — one commit per user story. This makes it straightforward to trace which files belong to which requirement and to split the work between team members.

### User Stories — files per commit

| US | Description | Files | App.jsx change |
|---|---|---|---|
| **BASE** | App skeleton, LandingPage, Dashboard, all contexts, shared components | `App.jsx`, `LandingPage.jsx`, `Dashboard.jsx`, `Navbar.jsx`, `BookCard.jsx`, `BidModal.jsx`, `ImageWithFallback.jsx`, all `*Context.jsx` + respective test files | Base routes only (`/`, `/dashboard`) |
| **US039** | Register a new publication and add to library | `AddPublication.jsx`, `SuccessModal.jsx` | Add `/add-publication` |
| **US040** | Create a private publication list | `ManageLists.jsx`, `ListDetail.jsx` | Add `/lists`, `/lists/:listId` |
| **US041** | View publications in personal library | `Library.jsx` | Add `/library` |
| **US042** | Add a publication to a list | `AddToListModal.jsx`, `MyPublications.jsx` | Add `/my-publications` |
| **US043** | Publicly share a list | — (feature inside `ManageLists.jsx` + `ListDetail.jsx`) | — |
| **US044** | Put a publication for sale (direct sale) | `SellPublication.jsx` | Add `/sell/:bookId` |
| **US045** | Browse publications for sale by genre | `Explore.jsx`, `ProductDetail.jsx`, `Cart.jsx` | Add `/explore`, `/product/:productId`, `/cart` |

> US046 (REST API) and US047 (HATEOAS) affect only the `api/` layer and do not require changes to `App.jsx` or new pages.
