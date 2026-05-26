package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.AppraisalEntityAuthorizationPolicy;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntityFactory;
import TOPSECRET.domain.AppraisalEntity.IAppraisalEntityRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.*;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

/**
 * Application service responsible for AppraisalEntity use cases.
 */
@Service
public class AppraisalEntityService {

    private final IAppraisalEntityRepository repository;
    private final AppraisalEntityFactory factory;
    private final AppraisalEntityAuthorizationPolicy policy;
    private final IUserRepository userRepository;

    /**
     * Constructs the service with required dependencies.
     *
     * @param repository the repository used for persistence operations
     * @param factory the factory used to create AppraisalEntity instances
     */
    public AppraisalEntityService(
            IAppraisalEntityRepository repository,
            AppraisalEntityFactory factory,
            AppraisalEntityAuthorizationPolicy policy,
            IUserRepository userRepository

    ) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository cannot be null");
        }
        if (factory == null) {
            throw new IllegalArgumentException("Factory cannot be null");
        }
        if (policy == null) {
            throw new IllegalArgumentException("Policy cannot be null");
        }
        if (userRepository == null) {
            throw new IllegalArgumentException("User Repository cannot be null");
        }
        this.repository = repository;
        this.factory = factory;
        this.policy = policy;
        this.userRepository = userRepository;
    }

    /**
     * Creates and persists a new AppraisalEntity.
     */
    public AppraisalEntity createAndSave(String name, Contact contact, List<PublicationTypeId> publicationTypes, List<GenreId> genres, UserId userId)
                        throws InstanceAlreadyExistsException, AccessDeniedException {

        User user = userRepository.findByID(userId).get();

        if(!policy.canRegisterAppraisalEntity(user)) {
            throw new AccessDeniedException ("You do not have permission to register an Appraisal Entity");
        }

        AppraisalEntity entity = factory.create(
                name,
                contact,
                publicationTypes,
                genres
        );

        return repository.save(entity);
    }

    /**
     * Updates an existing AppraisalEntity.
     */
    public AppraisalEntity update(
            AppraisalEntityId id,
            String name,
            Contact contact,
            List<PublicationTypeId> publicationTypes,
            List<GenreId> genres,
            UserId userId
    ) throws InstanceAlreadyExistsException, AccessDeniedException {

        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        User user = userRepository.findByID(userId).get();

        if(!policy.canRegisterAppraisalEntity(user)) {
            throw new AccessDeniedException ("You do not have permission to create a library");
        }

        AppraisalEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("AppraisalEntity not found with id: " + id));

        entity.update(name, contact, publicationTypes, genres);

        return repository.save(entity);
    }

    /**
     * Finds an AppraisalEntity by its identifier.
     */
    public Optional<AppraisalEntity> findById(AppraisalEntityId id) {

        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        return repository.findById(id);
    }

    /**
     * Retrieves all AppraisalEntities.
     */
    public List<AppraisalEntity> findAll() {
        return repository.findAll();
    }

    /**
     * Checks if an entity exists by its identifier.
     */
    public boolean existsById(AppraisalEntityId id) {

        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        return repository.existsById(id);
    }
}
