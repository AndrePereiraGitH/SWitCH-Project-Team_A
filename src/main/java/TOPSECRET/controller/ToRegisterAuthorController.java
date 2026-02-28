package TOPSECRET.controller;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.AuthorFactory;
import TOPSECRET.domain.Country;
import TOPSECRET.repository.AuthorRepository;
import java.time.LocalDate;
/**
 * Controller responsible for registering a new Author in the system.
 * NOTE: This controller only validates the repository dependency and delegates
 * the creation and storage of the Author to the AuthorRepository.
 */
public class ToRegisterAuthorController {
    private final AuthorRepository _authorRepository;

    public ToRegisterAuthorController(AuthorRepository authorRepository) {
        if (authorRepository == null) {
            throw new IllegalArgumentException("AuthorRepository cannot be null");
        }

        this._authorRepository = authorRepository;
    }

    public Author registerAuthor(String name, Country country, LocalDate dateOfBirth, String biography) {
        // Delegates author creation and persistence to the repository
        return _authorRepository.createAndAddAuthor(name, country, dateOfBirth, biography);
    }

}
