package TOPSECRET.repository;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.Country;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for creating Authors and storing them in memory.
 * NOTE: This repository is a Singleton because the application should have ONE shared "source of truth"
 * for authors. If different parts of the system created different repository objects, each one would
 * have its own list and the data would become inconsistent (duplicates could appear and reads would
 * return different results depending on which repository you used).
 */
public class AuthorRepository {
    private final List<Author> _authors = new ArrayList<>();
    private static AuthorRepository INSTANCE;       // Singleton instance (static = belongs to the class, not to an object)


    private AuthorRepository() {        // Private constructor: prevents other classes from doing: new AuthorRepository()
    }

    public static AuthorRepository getInstance() {
        if (INSTANCE == null) {     // - First time someone calls getInstance(), INSTANCE is null, so we create the repository.

            INSTANCE = new AuthorRepository();
        }
        return INSTANCE;             // - Next calls return the same object, so everyone shares the same author list.

    }

    public Author createAndAddAuthor(String name, Country country, LocalDate dateOfBirth, String biography) {
        // Creates Author
        Author author = new Author(name, country, dateOfBirth, biography);

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

    /**
     * Clears repository state.
     * Intended for testing purposes only.
     */
    public void clear() {
        _authors.clear();
    }
}