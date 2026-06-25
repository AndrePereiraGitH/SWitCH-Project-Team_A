package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.PublicationTypeAuthorizationPolicy;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.PublicationType.PublicationTypeFactory;
import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Service responsible for PublicationType operations.
 */
@Service
public class PublicationTypeService {

    private final IPublicationTypeRepository repository;
    private final PublicationTypeFactory factory;
    private final IUserRepository userRepository;
    private final PublicationTypeAuthorizationPolicy policy;

    /**
     * Creates the service with required dependencies.
     *
     * @param repository PublicationType repository
     * @param factory PublicationType factory
     * @param userRepository User repository
     * @param policy authorization policy
     */
    public PublicationTypeService(
            IPublicationTypeRepository repository,
            PublicationTypeFactory factory,
            IUserRepository userRepository,
            PublicationTypeAuthorizationPolicy policy
    ) {

        this.repository = Objects.requireNonNull(repository, "Repository cannot be null");
        this.factory = Objects.requireNonNull(factory, "Factory cannot be null");
        this.userRepository = Objects.requireNonNull(userRepository, "User repository cannot be null");
        this.policy = Objects.requireNonNull(policy, "Policy cannot be null");
    }

    /**
     * Creates and saves a new PublicationType.
     *
     * @param mediaFormat publication media format
     * @param userId user performing the operation
     *
     * @return the created PublicationType
     *
     * @throws IllegalArgumentException if the user does not exist
     * @throws AccessDeniedException if the user has no permission
     * @throws InstanceAlreadyExistsException if the PublicationType already exists
     */
    public PublicationType createAndSave(String mediaFormat, UserId userId)
            throws InstanceAlreadyExistsException, AccessDeniedException {

        User user = userRepository.findByID(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        if (!policy.canCreatePublicationType(user)) {
            throw new AccessDeniedException("Access denied");
        }

        Optional<PublicationType> existing =
                repository.findByMediaFormat(mediaFormat);

        if (existing.isPresent()) {
            throw new InstanceAlreadyExistsException(
                    "PublicationType already exists"
            );
        }

        PublicationType publicationType =
                factory.create(mediaFormat);

        return repository.save(publicationType);
    }

    /**
     * Returns all PublicationTypes.
     *
     * @return list of PublicationTypes
     */
    public List<PublicationType> findAll() {
        return repository.findAll();
    }

    /**
     * Finds a PublicationType by id.
     *
     * @param id PublicationType id
     * @return matching PublicationType if found
     */
    public PublicationType findById(PublicationTypeId id) {

        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }

        return repository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("PublicationType not found"));
    }

    /**
     * Finds a PublicationType by media format.
     *
     * @param mediaFormat publication media format
     * @return matching PublicationType if found
     */
    public PublicationType findByMediaFormat(String mediaFormat) {

        if (mediaFormat == null) {
            throw new IllegalArgumentException(
                    "mediaFormat cannot be null"
            );
        }

        return repository.findByMediaFormat(mediaFormat).orElseThrow(() ->
                new IllegalArgumentException("PublicationType not found"));
    }

    /**
     * Determines whether the specified user is authorized to create
     * {@link PublicationType} resources.
     *
     * <p>The user is resolved from the repository and evaluated against
     * the publication type authorization policy.</p>
     *
     * @param userId the identifier of the user to validate; must not be {@code null}
     * @return {@code true} if the user is allowed to create publication types;
     *         {@code false} otherwise
     * @throws InstanceNotFoundException if no user exists for the given identifier
     */
    public boolean canCreatePublicationType(UserId userId)
            throws InstanceNotFoundException {

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new InstanceNotFoundException("User not found"));

        return policy.canCreatePublicationType(user);
    }
}