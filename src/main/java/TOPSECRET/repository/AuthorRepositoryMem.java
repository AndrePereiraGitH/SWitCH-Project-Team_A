package TOPSECRET.repository;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.AuthorFactory;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.CountryID;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory repository responsible for storing and managing {@link Author} entities.
 *
 * <p>This class is an in-memory implementation of {@link IAuthorRepository}.
 * It follows the Repository pattern by providing a collection-like abstraction
 * over stored {@link Author} entities.</p>
 *
 * <p>Author creation is delegated to {@link AuthorFactory}, while this repository
 * is responsible for storing created authors and preventing duplicates from being
 * added.</p>
 *
 * <p>This implementation is intended for testing or simple application contexts
 * where persistent storage is not required.</p>
 */
public class AuthorRepositoryMem implements IAuthorRepository {

    private final List<Author> _authors = new ArrayList<>();
    private final AuthorFactory _authorFactory;

    // -------------------------------------------------------------------------
    // Construction
    // -------------------------------------------------------------------------

    /**
     * Creates a new in-memory author repository.
     *
     * @param authorFactory the factory used to create {@link Author} instances
     * @throws IllegalArgumentException if {@code authorFactory} is {@code null}
     */
    public AuthorRepositoryMem(AuthorFactory authorFactory) {
        if (authorFactory == null) {
            throw new IllegalArgumentException("AuthorFactory cannot be null");
        }
        this._authorFactory = authorFactory;
    }

    // -------------------------------------------------------------------------
    // Construction (createAndAdd)
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if an author with the same identity already exists
     */
    public Author createAndAddAuthor(String name, CountryID countryID, LocalDate dateOfBirth, String biography) {
        // Creates Author
        Author author = _authorFactory.create(name, countryID, dateOfBirth, biography);
        if (!exists(author)) {
            this._authors.add(author);  // Add the author that was passed in
            return author;
        }
        throw new IllegalArgumentException("Author already exists");
    }

    /**
     * Checks whether the given author already exists in the repository.
     *
     * <p>An author is considered to exist if another stored {@link Author}
     * is equal to it according to {@link Author#equals(Object)}.</p>
     *
     * @param author the author to search for
     * @return {@code true} if the author already exists in the repository;
     *         {@code false} otherwise
     */
    private boolean exists(Author author) {
        for (Author newAuthor : this._authors) {
            if (author.equals(newAuthor)) {
                return true;
            }
        }
        return false;
    }

    // -------------------------------------------------------------------------
    // Identity Lookup
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public Optional<Author> findAuthorById(AuthorId authorId){
        if(authorId == null){
            throw new NullPointerException("AuthorId must not be null");
        }

        for(Author author : _authors){
            if(author.id().equals(authorId)){
                return Optional.of(author);
            }
        }

        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    public List<Author> getAllAuthors() {
        return List.copyOf(this._authors);
    }
}