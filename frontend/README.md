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
        ├── api/
        │   └── constants.js            # API base URL and shared constants
        ├── components/
        │   ├── AuthContext.jsx          # Authentication state (login/logout)
        │   ├── AuthContext.test.jsx
        │   ├── CartContext.jsx          # Shopping cart state
        │   ├── CartContext.test.jsx
        │   ├── LibraryContext.jsx       # Personal library state (fetches from /libraries/by-user)
        │   ├── LibraryContext.test.jsx
        │   ├── ListsContext.jsx         # Publication lists state (fetches from /collection-of-publications)
        │   ├── ListsContext.test.jsx
        │   ├── PublicationsContext.jsx  # Publications state (fetches from /publicationSamples)
        │   ├── PublicationsContext.test.jsx
        │   ├── Navbar.jsx               # Navigation bar
        │   ├── Navbar.test.jsx
        │   ├── BookCard.jsx             # Publication card component
        │   ├── BookCard.test.jsx
        │   ├── BidModal.jsx             # Bid/offer modal
        │   ├── BidModal.test.jsx
        │   ├── AddToListModal.jsx       # Modal to add a publication to a collection
        │   ├── AddToListModal.test.jsx
        │   ├── SuccessModal.jsx         # Generic success confirmation modal
        │   ├── ImageWithFallback.jsx    # Image with icon fallback on error
        │   └── ImageWithFallback.test.jsx
        └── pages/
            ├── LandingPage.jsx          # Home page (/)
            ├── LandingPage.test.jsx
            ├── Dashboard.jsx            # User dashboard (/dashboard)
            ├── Dashboard.test.jsx
            ├── AddPublication.jsx       # Register new publication (/add-publication)
            ├── AddPublication.test.jsx
            ├── MyPublications.jsx       # User's publications (/my-publications)
            ├── MyPublications.test.jsx
            ├── Library.jsx              # Personal library (/library)
            ├── Library.test.jsx
            ├── ManageLists.jsx          # Manage publication lists (/lists)
            ├── ManageLists.test.jsx
            ├── ListDetail.jsx           # Single list detail (/lists/:listId)
            ├── ListDetail.test.jsx
            ├── SellPublication.jsx      # Put publication for sale (/sell/:bookId)
            ├── SellPublication.test.jsx
            ├── Explore.jsx              # Browse marketplace (/explore)
            ├── Explore.test.jsx
            ├── ProductDetail.jsx        # Single product detail (/product/:productId)
            └── ProductDetail.test.jsx
```

---

## Architecture

The application uses the **React Context API** for global state management with no external state library. All contexts are wired to the Spring Boot backend via `fetch`. Contexts are nested in the following order inside `App.jsx`:

```
AuthProvider
  └── PublicationsProvider
        └── LibraryProvider
              └── CartProvider
                    └── ListsProvider
                          └── BrowserRouter
```

Each context exposes a custom hook (`useAuth`, `usePublications`, etc.) that any child component can call. If a component calls a hook outside its provider it throws an error — which is also covered by the test suite.

### Backend integration

All contexts communicate with the Spring Boot backend at `http://localhost:8081`. The base URL is defined in `src/app/api/constants.js`. Authentication is currently mocked via `CURRENT_USER_ID = "user-001"` in each context — to be replaced with real auth in a future sprint.

Key endpoints used:

| Context | Endpoints |
|---|---|
| `PublicationsContext` | `GET /publicationSamples/not-in-library`, `GET /publicationSamples/in-library`, `POST /bibliographicalDatas`, `POST /editions`, `POST /publicationSamples`, `PATCH /publicationSamples/{id}/library`, `PATCH /publicationSamples/{id}/collection` |
| `LibraryContext` | `GET /libraries/by-user/{userId}` |
| `ListsContext` | `GET /collection-of-publications/owner`, `POST /collection-of-publications/`, `PATCH /collection-of-publications/{id}/make-public`, `PATCH /collection-of-publications/{id}/make-private`, `PATCH /collection-of-publications/{id}/make-public-until` |

---

## Implemented User Stories

### US039 — Register a Publication in the Library

**Goal:** As a user, I want to register a new publication (book or magazine) so that it appears in my personal publication list.

**Implementation:**

Registration is split across three pages and one context: `AddPublication.jsx` collects the form data, `PublicationsContext.jsx` orchestrates the backend calls, and `MyPublications.jsx` displays the result.

The form (`/add-publication`) fetches authors, genres, and publishers dynamically from the backend on mount:

```
GET /authors?userId=user-001
GET /genres
GET /publishers
```

On submit, `PublicationsContext.addPublication` runs a three-step sequential chain:

```
POST /bibliographicalDatas?userId=...   →  creates BD (title, authors, genres, language, date)
POST /editions?userId=...               →  creates Edition (ISBN/ISSN, dimensions, binding, publisher)
POST /publicationSamples?userId=...     →  creates PublicationSample (links BD + Edition, sets condition)
```

Each step uses the ID returned by the previous one. If any step fails, the chain throws and an error alert is shown.

**Form fields:**
- ISBN / ISSN (optional for publications before 1977; validated as 8, 10, or 13 digits)
- Publication type (Book / Magazine)
- Genre (multi-select checkboxes, from backend)
- Title, Author(s) (multi-select dropdown, from backend), Publisher (dropdown, from backend)
- Published date, Edition, Language
- Dimensions (W × H × D in cm), Weight (g), Number of pages, Binding type
- Condition (New / Like New / Very Good / Good / Acceptable / Poor)

**Known workaround — author and genre resolution:**
The backend's `BibliographicalDataMapper.toAuthorId()` and `toGenreId()` wrap the input string directly as a value object ID (`new AuthorId(name)`) instead of looking up the author by name. This means the frontend must send real database IDs (e.g. `AUTH1`, `GENRE3`) in the `authorName` / `genreName` fields. The form checkboxes store and submit these IDs; display names are kept separately as `authorDisplayNames` / `genreDisplayNames` for local enrichment after creation.

This is documented inline as a TODO and should be fixed on the backend by resolving names via `AuthorService.findByName`.

---

### US041 — View Publications in Personal Library

**Goal:** As a user, I want to see the publications I have added to my library, so I can manage my personal collection.

**Implementation:**

`LibraryContext.jsx` fetches the user's library on mount:

```
GET /libraries/by-user/user-001   →  { libraryId, libraryName }
```

`PublicationsContext.jsx` fetches publications in parallel from two endpoints:

```
GET /publicationSamples/not-in-library?userId=user-001
GET /publicationSamples/in-library?userId=user-001
```

Both results are merged and enriched (authors and genres joined from arrays, condition prettified, language code mapped to display name). Publications with `libraryId != null` appear in `Library.jsx`; those without appear in `MyPublications.jsx` with an "Add to Library" button.

`Library.jsx` (`/library`) renders the user's library with:
- The library name from `LibraryContext`
- Grid and list view toggle
- Filter by genre and type
- Click any card to open a detail modal showing author, genre, condition, language, ISBN/ISSN, type, and collection name
- "Add Publications" modal for publications not yet in the library

Adding a publication to the library calls:

```
PATCH /publicationSamples/{id}/library?userId=user-001
```

Local state is updated immediately with the backend response (which includes the populated `libraryId`), so the card moves from "not in library" to "in library" without a full re-fetch.

---

### US039 (cont.) / US041 — MyPublications

`MyPublications.jsx` (`/my-publications`) shows all publications owned by the user regardless of library status. Each card shows:
- Title, author, genre, condition
- Click to open a detail modal (author, genre, condition, language, ISBN/ISSN, type, library status, collection name)
- **Sell** button → `/sell/:bookId`
- **Add to List** button → opens `AddToListModal`
- **Add to Library** / **✓ In Library** badge

---

### US040 — Create a Private Publication List

**Goal:** As a user, I want to create a private list of my publications, give it a name, set a genre, and eventually share it.

**Implementation:**

`ListsContext.jsx` fetches the user's collections on mount:

```
GET /collection-of-publications/owner?userId=user-001   →  [{ collectionId, collectionName, private }, ...]
```

Creating a list from `ManageLists.jsx` calls:

```
POST /collection-of-publications/?ownerId=user-001
Body: { collectionName, genreId }
```

The genre dropdown is populated from `GET /genres`, so genre IDs (e.g. `GENRE4`) are sent directly to the backend — no hardcoding.

Every new collection starts as **private** (enforced by the domain). The created collection is appended to local state immediately using the backend response.

The "Create New List" modal additionally supports:
- **Share publicly on create:** if the "Share publicly" checkbox is ticked, a second call is made after creation — `PATCH /make-public` for indefinite sharing, or `PATCH /make-public-until?publicUntil=YYYY-MM-DD` for a fixed duration in days.

`ManageLists.jsx` (`/lists`) shows all collections as cards with three action buttons each:
- **View** → navigates to `/lists/:collectionId`
- **Lock/Unlock** → toggles public/private (see US043)
- **+** → opens "Add Publications" modal

---

### US042 — Add a Publication to a List

**Goal:** As a user, I want to add publications from my library to a list so I can organize them by topic or theme.

**Implementation:**

Adding a publication to a collection calls:

```
PATCH /publicationSamples/{sampleId}/collection?collectionId={id}&userId=user-001
```

This is enforced by two domain rules:
1. A publication must be **in the library** before it can be added to a collection.
2. A publication can only belong to **one collection at a time** (single `collection_of_publications_id` column on the `publication_samples` table).

The frontend enforces both rules before showing options:

- In `AddToListModal.jsx`: if `pub.libraryId == null`, a warning is shown instead of the list options ("Add this publication to your library first").
- In `ManageLists.jsx` and `ListDetail.jsx`: the "Add Publications" modal only shows publications where `libraryId != null && collectionId == null`.

After a successful add, `PublicationsContext.refresh()` is called to re-fetch both endpoints so `collectionId` is updated on the publication in local state.

`ListDetail.jsx` (`/lists/:collectionId`) shows all publications in a collection by filtering `publications` where `p.collectionId === list.collectionId`. Clicking a card opens a detail modal showing all publication fields plus the collection name (resolved from `ListsContext`).

---

### US043 — Share a List Publicly

**Goal:** As a user, I want to share my publication list with other users, either indefinitely or for a limited time.

**Implementation:**

Three backend endpoints cover the sharing lifecycle:

```
PATCH /collection-of-publications/{id}/make-public?userId=...           →  share indefinitely
PATCH /collection-of-publications/{id}/make-public-until?publicUntil=YYYY-MM-DD&userId=...  →  share for a fixed period
PATCH /collection-of-publications/{id}/make-private?userId=...          →  revoke sharing
```

`ListsContext` exposes `makePublic`, `makePublicUntil`, and `makePrivate`. Each updates local state with the returned DTO so the badge on the card reflects the change immediately.

`ManageLists.jsx` shows a Lock/Unlock toggle button per list:
- If `list.private === true` → teal Unlock button → calls `makePublic`
- If `list.private === false` → grey Lock button → calls `makePrivate`

The "Create New List" modal lets the user opt into sharing at creation time. If the "Share publicly" checkbox is ticked:
- With a duration (e.g. 30 days) → `makePublicUntil` is called with `today + N days`
- Without a duration → `makePublic` is called for permanent sharing

`ListDetail.jsx` shows a "Public" badge in the header when `list.private === false`.

The `isPrivate` field was added to `ResponseCollectionOfPublicationsDTO` specifically to support this — previously the DTO only returned `collectionId` and `collectionName`. The `CollectionOfPublicationsId.toString()` bug (returning `ClassName@hashCode` instead of the actual ID) was also fixed as part of this story, as it was causing the PATCH URL to be malformed.

---

## Pages

### `LandingPage.jsx` — `/`
Public home page. Shows a hero section, featured items for sale, and navigation to the marketplace.

### `Dashboard.jsx` — `/dashboard`
The logged-in user's home. Displays a summary of library publications, active lists, and items for sale, with quick-action links to each section.

### `AddPublication.jsx` — `/add-publication` — **US039**
See [US039](#us039--register-a-publication-in-the-library) above.

### `MyPublications.jsx` — `/my-publications` — **US039, US041, US042**
See [US039 (cont.)](#us039-cont--us041--mypublications) above.

### `Library.jsx` — `/library` — **US041**
See [US041](#us041--view-publications-in-personal-library) above.

### `ManageLists.jsx` — `/lists` — **US040, US042, US043**
See [US040](#us040--create-a-private-publication-list), [US042](#us042--add-a-publication-to-a-list), and [US043](#us043--share-a-list-publicly) above.

### `ListDetail.jsx` — `/lists/:listId` — **US042, US043**
See [US042](#us042--add-a-publication-to-a-list) and [US043](#us043--share-a-list-publicly) above.

### `SellPublication.jsx` — `/sell/:bookId` — **US044**
Form to put a publication up for sale. Supports direct sale (fixed price) and auction modes.

### `Explore.jsx` — `/explore` — **US045**
Public marketplace. Lists all items for sale with filters for genre, type, sale type (direct/auction), and price range.

### `ProductDetail.jsx` — `/product/:productId`
Detail page for a single item for sale. Shows full publication info, price, condition, seller details, and buy/bid actions.

---

## Components

### `Navbar.jsx`
Fixed top navigation bar. Shows logo, search, main links, cart icon with item count, and user login/logout dropdown.

### `BookCard.jsx`
Card component displaying title, author, genre, condition, and price. Clickable in `Library`, `MyPublications`, and `ListDetail` to open a detail modal.

### `AddToListModal.jsx`
Modal for adding a publication to one of the user's collections. Guards against adding publications not yet in the library (shows a warning instead of list options). A publication can only belong to one collection at a time — lists already containing the publication are shown as disabled.

### `BidModal.jsx`
Modal for placing a bid on an auction item. Shows current bid, optional buyout price, and a number input.

### `SuccessModal.jsx`
Generic success confirmation modal with a green checkmark. Used after form submissions (e.g. after registering a publication).

---

## Known Workarounds and TODOs

| Area | Issue | Workaround | Status |
|---|---|---|---|
| Author/genre resolution | `BibliographicalDataMapper` wraps input strings as IDs instead of resolving by name | Frontend sends real DB IDs (`AUTH1`, `GENRE3`) in name fields | TODO: backend fix |
| Publication type IDs | No `/publication-types` endpoint used | `publicationTypeId` hardcoded as `pub-type-001` / `pub-type-002` | TODO: fetch from `/publication-types` |
| Authentication | No real auth system | `CURRENT_USER_ID = "user-001"` hardcoded in all contexts | TODO: replace with real auth |
| CORS port switching | Vite sometimes picks port 5174 instead of 5173 | Backend uses `allowedOriginPatterns("http://localhost:*")` | Fixed |
| `PublisherID` serialization | `PublisherID.toString()` returned `ClassName@hashCode` | Added `@JsonValue` to `PublisherID.getId()` | Fixed |
| `CollectionOfPublicationsId` serialization | Same `toString()` bug causing malformed PATCH URLs | Added `toString()` override returning the raw ID string | Fixed |
| `UserId` serialization | Same bug leaking into HATEOAS self-links | Added `toString()` override | Fixed |
| Edition code in DTO | `PublicationDTO` did not include ISBN/ISSN | Added `code` field to DTO and mapper | Fixed |

---

## Getting Started

### Prerequisites

- Node.js v18 or higher
- npm (included with Node.js)
- Spring Boot backend running on port 8081

### Installation

```bash
# Install dependencies
npm install

# Start the development server
npm run dev
```

The application will be available at **http://localhost:5173**

> If Vite picks port 5174 instead, ensure `CorsConfig.java` on the backend uses `allowedOriginPatterns("http://localhost:*")` to avoid CORS errors.

---

## Testing

The project uses **Vitest** and **React Testing Library** with **jsdom**.

```bash
npm test              # run once
npm run test:watch    # watch mode during development
```

### Test utilities — `src/test-utils.jsx`

Every test file imports from `test-utils` instead of directly from `@testing-library/react`. This wraps components in the full provider tree so individual tests do not need to repeat the setup.

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

One commit per user story. Each commit contains only the files relevant to that story.

| US | Description | Key files |
|---|---|---|
| **BASE** | App skeleton, contexts, shared components | `App.jsx`, `*Context.jsx`, `Navbar.jsx`, `BookCard.jsx`, `BidModal.jsx`, `ImageWithFallback.jsx` |
| **US039** | Register publication, wire BD→Edition→Sample chain | `AddPublication.jsx`, `SuccessModal.jsx`, `PublicationsContext.jsx`, `MyPublications.jsx` |
| **US040** | Create private publication list | `ManageLists.jsx`, `ListDetail.jsx`, `ListsContext.jsx`, `AddToListModal.jsx` |
| **US041** | View and manage personal library | `Library.jsx`, `LibraryContext.jsx` |
| **US042** | Add publication to list from library | `AddToListModal.jsx`, `MyPublications.jsx`, `ListDetail.jsx` |
| **US043** | Share a list publicly (with optional expiry) | `ManageLists.jsx`, `ListsContext.jsx` |
| **US044** | Put publication for sale | `SellPublication.jsx` |
| **US045** | Browse marketplace | `Explore.jsx`, `ProductDetail.jsx` |