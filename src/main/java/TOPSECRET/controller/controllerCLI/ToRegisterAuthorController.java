package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.Author.Author;
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
 */
public class ToRegisterAuthorController {
    private final AuthorService _authorService;

    /**
     * Constructs a controller for registering authors.
     *
     * @param authorService the service responsible for author creation and persistence
     * @throws IllegalArgumentException if authorService is null
     */
    public ToRegisterAuthorController(AuthorService authorService) {
        if (authorService == null) {
            throw new IllegalArgumentException("AuthorService cannot be null");
        }

        this._authorService = authorService;
    }

    /**
     * Registers a new author in the system.
     *
     * <p>This method delegates the creation and persistence of the author
     * to the {@link AuthorService}.</p>
     *
     * @param name the name of the author
     * @param countryId the country identifier of the author
     * @param dateOfBirth the author's date of birth
     * @param biography a short biography of the author
     * @return the created and persisted {@link Author}
     */
    public Author register(String name, CountryId countryId, LocalDate dateOfBirth, String biography) throws InstanceAlreadyExistsException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (countryId == null) {
            throw new IllegalArgumentException("CountryId cannot be null");
        }
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("DateOfBirth cannot be null");
        }
        if (biography == null || biography.isBlank()) {
            throw new IllegalArgumentException("Biography cannot be null or blank");
        }
        return _authorService.createAndSave(name, countryId, dateOfBirth, biography);
    }
}
