package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.CountryAuthorizationPolicy;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.CountryFactory;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

/**
 * Application service responsible for Country-related use cases.
 *
 * <p>
 * This service coordinates the creation and retrieval of {@link Country}
 * entities, while enforcing authorization and business validation rules.
 * </p>
 *
 * <p>
 * Responsibilities include:
 * <ul>
 *     <li>Validating user permissions through {@link CountryAuthorizationPolicy}</li>
 *     <li>Creating Country instances using {@link CountryFactory}</li>
 *     <li>Persisting and retrieving Countries through {@link ICountryRepository}</li>
 *     <li>Retrieving users through {@link IUserRepository}</li>
 * </ul>
 * </p>
 */
@Service
public class CountryService {

    private final CountryFactory factory;
    private final ICountryRepository repository;
    private final CountryAuthorizationPolicy policy;
    private final IUserRepository userRepository;

    /**
     * Constructs the service with required dependencies.
     */
    public CountryService(
            CountryFactory factory,
            ICountryRepository repository,
            CountryAuthorizationPolicy policy,
            IUserRepository userRepository
    ) {

        if (factory == null) {
            throw new IllegalArgumentException("Factory cannot be null");
        }

        if (repository == null) {
            throw new IllegalArgumentException("Repository cannot be null");
        }

        if (policy == null) {
            throw new IllegalArgumentException("Policy cannot be null");
        }

        if (userRepository == null) {
            throw new IllegalArgumentException("User Repository cannot be null");
        }

        this.factory = factory;
        this.repository = repository;
        this.policy = policy;
        this.userRepository = userRepository;
    }

    /**
     * Creates and persists a new Country.
     */
    public Country createAndSave(String countryName, UserId userId)
            throws InstanceAlreadyExistsException, AccessDeniedException {

        User user = getUser(userId);

        if (!policy.canRegisterCountry(user)) {
            throw new AccessDeniedException(
                    "You do not have permission to register a Country");
        }

        Country country = factory.create(countryName);

        if (repository.findByID(country.id()).isPresent()) {
            throw new InstanceAlreadyExistsException(
                    "Country already exists in the system.");
        }

        return repository.save(country);
    }

    /**
     * Retrieves all Countries.
     */
    public List<Country> findAll() {

        return repository.findAll();
    }

    /**
     * Retrieves a {@link User} by their identifier.
     */
    private User getUser(UserId id) {

        return userRepository.findByID(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "User not found with ID: " + id));
    }

    /**
     * Retrieves a {@link Country} by its unique identifier.
     *
     * @param id the {@link CountryId} to search for; must not be {@code null}
     * @return an {@link Optional} containing the Country if found; empty otherwise
     */
    public Optional<Country> findById(CountryId id) {

        Objects.requireNonNull(id, "CountryId cannot be null");

        return repository.findByID(id);
    }
}