package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.AuthorFactory;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.ValueObjects.AuthorId;

import javax.management.InstanceAlreadyExistsException;
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

    private final List<Author> _authors;
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
        this._authors = new ArrayList<>();
        this._authorFactory = authorFactory;
    }

    // -------------------------------------------------------------------------
    // Save
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     *
     */
    public Author save(Author author) throws InstanceAlreadyExistsException {

        if (!existsById(author.id())) {
            this._authors.add(author);
            return author;
        }
        throw new InstanceAlreadyExistsException("Author already exists");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(AuthorId authorId) {
        if (authorId == null) {
            throw new IllegalArgumentException("authorId cannot be null");
        }

        for (Author existingAuthor : this._authors) {
            if (authorId.equals(existingAuthor.id())) {
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
    public List<Author> findAll() {
        return List.copyOf(this._authors);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Author> findById(AuthorId id) {
        if (id == null) {
            throw new IllegalArgumentException("AuthorId cannot be null");
        }

        return _authors.stream()
                .filter(author -> author.id().equals(id))
                .findFirst();
    }
}