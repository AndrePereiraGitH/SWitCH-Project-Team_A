package TOPSECRET.controller;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.AuthorFactory;
import TOPSECRET.domain.Country;
import TOPSECRET.repository.AuthorRepository;
import java.time.LocalDate;

/**
 * Application controller responsible for registering a new {@link Author} in the system.
 *
 * <p>This controller coordinates access to the {@link AuthorRepository}.
 * The {@link AuthorFactory} is responsible for creating {@link Author} instances,
 * while the {@link AuthorRepository} enforces uniqueness and handles persistence.
 *
 * <p>It does not contain business logic and does not validate input data beyond
 * its own constructor dependency check. Its responsibility is limited to delegation,
 * preserving separation of concerns between the domain and presentation layers.
 */

public class ToRegisterAuthorController {
    private final AuthorRepository _authorRepository;

    public ToRegisterAuthorController(AuthorRepository authorRepository) {
        if (authorRepository == null) {
            throw new IllegalArgumentException("AuthorRepository cannot be null");
        }

        this._authorRepository = authorRepository;
    }

    public Author register(String name, Country country, LocalDate dateOfBirth, String biography) {
        // Delegates author creation and persistence to the repository
        return _authorRepository.createAndAddAuthor(name, country, dateOfBirth, biography);
    }

}
