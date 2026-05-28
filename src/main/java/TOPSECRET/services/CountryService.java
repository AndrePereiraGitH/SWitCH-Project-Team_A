package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.CountryAuthorizationPolicy;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.CountryFactory;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Application service responsible for Country use cases.
 */
@Service
public class CountryService {

    private final CountryFactory factory;
    private final ICountryRepository repository;
    private final CountryAuthorizationPolicy policy;
    private final IUserRepository userRepository;

    /**
     * Constructs the service with required dependencies.
     *
     * @param repository repository used for persistence operations
     * @param factory factory used to create Country instances
     * @param policy authorization policy used to validate user permissions
     * @param userRepository repository used to retrieve users
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
}