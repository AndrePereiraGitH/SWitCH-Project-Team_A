package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.PublisherAuthorizationPolicy;
import TOPSECRET.domain.Publisher.IPublisherRepository;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.Publisher.PublisherFactory;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.PublisherID;
import TOPSECRET.domain.ValueObjects.UserId;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Application service responsible for orchestrating the creation and persistence
 * of {@link Publisher} aggregates.
 *
 * <p>Coordinates between the domain layer, authorization policies, and persistence
 * to ensure that publishers are created only by authorized users.</p>
 */
@Service
public class PublisherService {

    private final PublisherFactory factory;
    private final IPublisherRepository repository;
    private final IUserRepository userRepository;
    private final PublisherAuthorizationPolicy authPolicy;

    /**
     * Constructs a {@code PublisherService} with the required dependencies.
     *
     * @param factory        the factory used to create publisher instances
     * @param repository     the repository for publisher persistence
     * @param userRepository the repository to validate user existence
     * @param authPolicy     the policy to validate creation permissions
     */
    @Autowired
    public PublisherService(PublisherFactory factory,
                            IPublisherRepository repository,
                            IUserRepository userRepository,
                            PublisherAuthorizationPolicy authPolicy) {
        this.factory = Objects.requireNonNull(factory, "PublisherFactory cannot be null");
        this.repository = Objects.requireNonNull(repository, "IPublisherRepository cannot be null");
        this.userRepository = Objects.requireNonNull(userRepository, "IUserRepository cannot be null");
        this.authPolicy = Objects.requireNonNull(authPolicy, "PublisherAuthorizationPolicy cannot be null");
    }

    /**
     * Creates a new {@link Publisher} and persists it to the repository.
     *
     * <p>Delegates creation to {@link PublisherFactory#create} and
     * persistence to {@link IPublisherRepository#save}.
     * All parameters are mandatory — passing {@code null} for any argument
     * will throw a {@link NullPointerException}.</p>
     *
     * @param name      the publisher's name; must not be {@code null}
     * @param vatNumber the publisher's VAT number; must not be {@code null}
     * @param contact   the publisher's contact information; must not be {@code null}
     * @return the saved {@link Publisher} with its persisted identity
     * @throws NullPointerException     if any parameter is {@code null}
     * @throws IllegalArgumentException if a publisher with the same identity already exists
     */
    @Transactional
    public Publisher createAndSave(String name, String vatNumber, Contact contact, UserId userId)
            throws AccessDeniedException {

        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(vatNumber, "VAT number cannot be null");
        Objects.requireNonNull(contact, "Contact cannot be null");
        Objects.requireNonNull(userId, "UserId cannot be null");

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        if (!authPolicy.canCreatePublisher(user)) {
            throw new AccessDeniedException("User does not have permission to create a publisher");
        }

        Publisher publisher = factory.create(name, vatNumber, contact);
        return repository.save(publisher);
    }

    /**
     * Retrieves all registered {@link Publisher} entities.
     *
     * <p>This method delegates directly to the repository and returns all
     * persisted publishers without applying business filters.</p>
     *
     * @return a list of all publishers in the system
     */
    public List<Publisher> findAll() {
        return repository.findAll();
    }

    /**
     * Retrieves a {@link Publisher} by its unique identifier.
     *
     * @param id the {@link PublisherID} to search for; must not be {@code null}
     * @return an {@link Optional} containing the Publisher if found; empty otherwise
     */
    public Publisher findById(PublisherID id) {
        Objects.requireNonNull(id, "PublisherID cannot be null");
        return repository.findById(id)
                .orElseThrow(() ->
                new IllegalArgumentException("Publisher not found eith id: " + id));
    }

}