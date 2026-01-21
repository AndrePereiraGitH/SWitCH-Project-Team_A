package TOPSECRET.repository;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.Country;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AuthorRepository {
    private final List<Author> _authors = new ArrayList<>();
    private static AuthorRepository INSTANCE;

    private AuthorRepository() {
    }

    public static AuthorRepository getInstance() {     // Singleton
        if (INSTANCE == null) {
            INSTANCE = new AuthorRepository();
        }
        return INSTANCE;
    }

    public Author createAndAddAuthor(String name, Country country, LocalDate dateOfBirth, String biography) {
        // Creates Author
        Author author = new Author(name, country, dateOfBirth, biography);

        // Validate if already exists
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
}
