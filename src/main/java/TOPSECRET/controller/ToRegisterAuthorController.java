package TOPSECRET.controller;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.AuthorFactory;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.services.AuthorService;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDate;

/**
 * Application controller responsible for registering a new {@link Author} in the system.
 *
 * <p>This controller coordinates access to the {@link IAuthorRepository}.
 * The {@link AuthorFactory} is responsible for creating {@link Author} instances,
 * while the {@link IAuthorRepository} enforces uniqueness and handles persistence.
 *
 * <p>It does not contain business logic and does not validate input data beyond
 * its own constructor dependency check. Its responsibility is limited to delegation,
 * preserving separation of concerns between the domain and presentation layers.
 */

public class ToRegisterAuthorController {
    private final AuthorService _authorService;


    public ToRegisterAuthorController(AuthorService authorService) {
        if (authorService == null) {
            throw new IllegalArgumentException("AuthorRepository cannot be null");
        }

        this._authorService = authorService;
    }

    public Author register(String name, CountryId countryId, LocalDate dateOfBirth, String biography) throws InstanceAlreadyExistsException {
        return _authorService.createAndSave(name, countryId, dateOfBirth, biography);
    }
}
