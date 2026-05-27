package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.PublicationTypeAuthorizationPolicy;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.PublicationType.PublicationTypeFactory;
import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.nio.file.AccessDeniedException;
import java.util.List;
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

        if (repository == null) {
            throw new IllegalArgumentException("Repository cannot be null");
        }

        if (factory == null) {
            throw new IllegalArgumentException("Factory cannot be null");
        }

        this.repository = repository;
        this.factory = factory;
        this.userRepository = userRepository;
        this.policy = policy;
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
    public Optional<PublicationType> findById(PublicationTypeId id) {

        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }

        return repository.findById(id);
    }

    /**
     * Finds a PublicationType by media format.
     *
     * @param mediaFormat publication media format
     * @return matching PublicationType if found
     */
    public Optional<PublicationType> findByMediaFormat(String mediaFormat) {

        if (mediaFormat == null) {
            throw new IllegalArgumentException(
                    "mediaFormat cannot be null"
            );
        }

        return repository.findByMediaFormat(mediaFormat);
    }
}