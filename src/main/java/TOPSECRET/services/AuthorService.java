package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.AuthorAuthorizationPolicy;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.AuthorFactory;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Application service responsible for managing {@link Author} creation and persistence.
 *
 * <p>Coordinates the interaction between the domain layer and the persistence layer.
 * Delegates creation of {@link Author} aggregates to {@link AuthorFactory} and ensures
 * their persistence through {@link IAuthorRepository}. Authorization is enforced via
 * {@link AuthorAuthorizationPolicy} before any state-changing operation.</p>
 */
@Service
public class AuthorService {

    private final AuthorFactory authorFactory;
    private final IAuthorRepository authorRepo;
    private final AuthorAuthorizationPolicy policy;
    private final IUserRepository userRepository;
    private final ICountryRepository countryRepository;

    /**
     * Constructs an {@code AuthorService} with the required collaborators.
     *
     * @param authorFactory     the factory used to create {@link Author} domain objects
     * @param authorRepo        the repository used to persist authors
     * @param policy            the authorization policy for author operations
     * @param userRepository    the repository used to retrieve users
     * @param countryRepository the repository used to resolve countries
     */
    public AuthorService(AuthorFactory authorFactory,
                         IAuthorRepository authorRepo,
                         AuthorAuthorizationPolicy policy,
                         IUserRepository userRepository,
                         ICountryRepository countryRepository) {
        this.authorFactory = Objects.requireNonNull(authorFactory, "Author factory must not be null.");
        this.authorRepo = Objects.requireNonNull(authorRepo, "Author Repository must not be null.");
        this.policy = Objects.requireNonNull(policy, "Authorization policy must not be null.");
        this.userRepository = Objects.requireNonNull(userRepository, "User repository must not be null.");
        this.countryRepository = Objects.requireNonNull(countryRepository, "Country repository must not be null.");
    }

    /**
     * Creates a new {@link Author} and persists it in the repository.
     *
     * @param name        the author's name
     * @param countryID   the identifier of the author's country
     * @param dateOfBirth the author's date of birth
     * @param biography   the author's biography
     * @param userId      the ID of the user performing the operation
     * @return the persisted {@link Author}
     * @throws AccessDeniedException          if the user does not have ADMIN role
     * @throws InstanceAlreadyExistsException if an author already exists
     */
    public Author createAndSave(String name, CountryId countryID, LocalDate dateOfBirth,
                                String biography, UserId userId)
            throws InstanceAlreadyExistsException, AccessDeniedException {

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!policy.canCreateAuthor(user)) {
            throw new AccessDeniedException(
                    "You do not have permission to register an author.");
        }

        Author author = authorFactory.create(name, countryID, dateOfBirth, biography);
        return authorRepo.save(author);
    }

    /**
     * Resolves a {@link Country} by its identifier.
     *
     * @param countryId the identifier of the country to resolve
     * @return the resolved {@link Country}
     * @throws IllegalArgumentException if the country is not found
     */
    public Country resolveCountry(CountryId countryId) {
        return countryRepository.findByID(countryId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Country not found for ID: " + countryId.getCountryID()));
    }
}