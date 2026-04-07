package TOPSECRET.controller;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.AuthorFactory;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.ValueObjects.CountryID;

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

public class US005ToRegisterAuthorController {
    private final IAuthorRepository _IAuthorRepository;


    public US005ToRegisterAuthorController(IAuthorRepository IAuthorRepository) {
        if (IAuthorRepository == null) {
            throw new IllegalArgumentException("AuthorRepository cannot be null");
        }

        this.IAuthorRepository = _IAuthorRepository;
    }

    public Author register(String name, CountryID countryId, LocalDate dateOfBirth, String biography) {
        // Delegates author creation and persistence to the repository
        return _I_authorRepository.createAndAddAuthor(name, countryId, dateOfBirth, biography);
    }
}
