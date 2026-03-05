package TOPSECRET.repository;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.AuthorFactory;
import TOPSECRET.domain.Country;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository responsible for storing and managing {@link Author} instances.
 *
 * <p>This class follows the Repository pattern: it acts as an in-memory collection
 * of {@link Author} objects, delegating creation to an {@link AuthorFactory} and
 * enforcing uniqueness before persisting.
 *
 * <p><b>Responsibilities:</b>
 * <ul>
 *   <li>Delegate author creation to {@link AuthorFactory}</li>
 *   <li>Enforce that no duplicate authors are added</li>
 *   <li>Provide read access to the current collection of authors</li>
 * </ul>
 *
 * <p><b>What this class does NOT do:</b> it does not validate the arguments passed
 * to {@link #createAndAddAuthor} — that responsibility belongs to {@link AuthorFactory}
 * and the {@link Author} constructor.
 */
public class AuthorRepository {
    private final List<Author> _authors;
    private final AuthorFactory _authorFactory;

    public AuthorRepository(List<Author> authors, AuthorFactory authorFactory) {
        if (authors == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }
        if (authorFactory == null) {
            throw new IllegalArgumentException("AuthorFactory cannot be null");
        }
        this._authors = authors;
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