package TOPSECRET.repository;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.AuthorFactory;
import TOPSECRET.domain.Country;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository responsible for creating Authors and storing them in memory.
 * NOTE: This repository is a Singleton because the application should have ONE shared "source of truth"
 * for authors. If different parts of the system created different repository objects, each one would
 * have its own list and the data would become inconsistent (duplicates could appear and reads would
 * return different results depending on which repository you used).
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
        Author author = _authorFactory.createAuthor(name, country, dateOfBirth, biography);
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