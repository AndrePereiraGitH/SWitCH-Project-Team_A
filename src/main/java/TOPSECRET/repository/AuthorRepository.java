package TOPSECRET.repository;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.Country;
import TOPSECRET.domain.Enum.Countries;
import TOPSECRET.domain.Profile;
import TOPSECRET.domain.Country;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AuthorRepository {
    public List<Author> _authors;


    public AuthorRepository() {
        this._authors = new ArrayList<>();
    }

    public Author addAuthor(String name, Country country, LocalDate dateOfBirth) {
        // Creates Author
        Author author = new Author(new Profile(name, country, dateOfBirth), "valid bio");

        // Validate if already exists
            if (!existsByName(author)){
                this._authors.add(author);  // Add the author that was passed in
                return author;
            }
            throw new IllegalArgumentException("Author already exists");
        }

    private boolean existsByName(Author author) {
        for (Author newAuthor : this._authors) {
            if (author.equals(newAuthor)) {
                return true;
            }
        }
        return false;

    }
}
