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
 * <p>
 * This controller acts as an entry point for author registration requests and delegates
 * all business operations to the {@link AuthorService}.
 * </p>
 *
 * <p>
 * It does not contain business logic or perform domain validation. Its responsibility
 * is strictly to coordinate the request flow between the presentation layer and the
 * service layer, preserving a clear separation of concerns.
 * </p>
 *
 * <p>
 * The {@link AuthorService} encapsulates the creation and persistence of {@link Author}
 * entities, including interaction with the {@link IAuthorRepository} and {@link AuthorFactory}.
 * </p>
 */
public class ToRegisterAuthorController {
    private final AuthorService _authorService;


    public ToRegisterAuthorController(AuthorService authorService) {
        if (authorService == null) {
            throw new IllegalArgumentException("AuthorService cannot be null");
        }

        this._authorService = authorService;
    }

    public Author register(String name, CountryId countryId, LocalDate dateOfBirth, String biography) throws InstanceAlreadyExistsException {
        return _authorService.createAndSave(name, countryId, dateOfBirth, biography);
    }
}
