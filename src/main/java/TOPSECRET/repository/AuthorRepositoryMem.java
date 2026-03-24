package TOPSECRET.repository;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.AuthorFactory;
import TOPSECRET.domain.Country;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * In-memory repository responsible for storing and managing {@link Author} entities.
 *
 * <p>In the context of Domain-Driven Design (DDD), {@link Author} is an entity,
 * meaning it is uniquely identified by its identity rather than by its attributes.
 *
 * <p>This class follows the Repository pattern, acting as an in-memory collection
 * of {@link Author} entities. It delegates entity creation to an
 * {@link AuthorFactory} and ensures that no duplicate authors are stored.
 *
 * <p><b>Responsibilities:</b>
 * <ul>
 *   <li>Delegate the creation of {@link Author} entities to {@link AuthorFactory}</li>
 *   <li>Ensure uniqueness of {@link Author} entities before adding them to the repository</li>
 *   <li>Provide read-only access to the current collection of authors</li>
 * </ul>
 *
 * <p><b>Non-responsibilities:</b>
 * <ul>
 *   <li>Does not validate input arguments passed to {@link #createAndAddAuthor};
 *       validation is handled by {@link AuthorFactory} and the {@link Author} entity itself</li>
 * </ul>
 *
 * <p>This implementation stores data in memory and is intended for testing
 * or simple application contexts where persistence is not required.
 */
public class AuthorRepositoryMem implements IAuthorRepository {
    private final List<Author> _authors = new ArrayList<>();
    private final AuthorFactory _authorFactory;

    public AuthorRepositoryMem(AuthorFactory authorFactory) {
        if (authorFactory == null) {
            throw new IllegalArgumentException("AuthorFactory cannot be null");
        }
        this._authorFactory = authorFactory;
    }

    public Author createAndAddAuthor(String name, Country country, LocalDate dateOfBirth, String biography) {
        // Creates Author
        Author author = _authorFactory.create(name, country, dateOfBirth, biography);
        if (!exists(author)) {
            this._authors.add(author);  // Add the author that was passed in
            return author;
        }
        throw new IllegalArgumentException("Author already exists");
    }

    private boolean exists(Author author) {
        for (Author newAuthor : this._authors) {
            if (author.equals(newAuthor)) {
                return true;
            }
        }
        return false;
    }

    public List<Author> getAllAuthors() {
        return List.copyOf(this._authors);
    }
}