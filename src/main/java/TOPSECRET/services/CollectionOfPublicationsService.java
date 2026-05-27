package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.CollectionOfPublicationsAuthorizationPolicy;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import java.nio.file.AccessDeniedException;


/**
 * Application service responsible for creating and persisting
 * {@link CollectionOfPublications} aggregates.
 *
 * <p>This service coordinates the interaction between the domain factory,
 * the repository, the authorization policy, and the user repository,
 * ensuring that new collections are created according to domain rules,
 * authorized correctly, and then persisted.</p>
 *
 * <p>The creation logic itself is delegated to
 * {@link CollectionOfPublicationsFactory}, while persistence is handled by
 * {@link ICollectionOfPublicationsRepository}. Authorization is enforced via
 * {@link CollectionOfPublicationsAuthorizationPolicy}. This keeps the service
 * focused on orchestration rather than domain construction or infrastructure
 * concerns.</p>
 *
 * <p>All mandatory input parameters are validated before delegation.</p>
 */
@Service
public class CollectionOfPublicationsService {


    /**
     * Repository responsible for persisting
     * {@link CollectionOfPublications} aggregates.
     */
    private final ICollectionOfPublicationsRepository collectionOfPublicationsRepository;

    /**
     * Factory responsible for creating new
     * {@link CollectionOfPublications} domain objects.
     */
    private final CollectionOfPublicationsFactory factory;

    /**
     * Authorization policy used to determine whether a user is permitted
     * to perform collection of publications-related operations.
     */
    private final CollectionOfPublicationsAuthorizationPolicy policy;

    /**
     * Repository used to retrieve {@link User} aggregates by their identifier.
     */
    private final IUserRepository userRepo;

    /**
     * Creates a new service with the required dependencies.
     *
     * @param collectionOfPublicationsRepository repository used to persist collections;
     *                                           must not be {@code null}
     * @param factory                            factory used to create new domain objects;
     *                                           must not be {@code null}
     * @param policy                             authorization policy used to verify user permissions;
     *                                           must not be {@code null}
     * @param userRepo                           repository used to retrieve users by identifier;
     *                                           must not be {@code null}
     * @throws NullPointerException if any argument is {@code null}
     */
    public CollectionOfPublicationsService(
            ICollectionOfPublicationsRepository collectionOfPublicationsRepository,
            CollectionOfPublicationsFactory factory,
            CollectionOfPublicationsAuthorizationPolicy policy,
            IUserRepository userRepo) {

        Objects.requireNonNull(collectionOfPublicationsRepository,
                "collectionOfPublicationsRepository cannot be null");

        Objects.requireNonNull(factory,
                "CollectionOfPublicationsFactory cannot be null");

        Objects.requireNonNull(policy,
                "CollectionOfPublicationsAuthorizationPolicy cannot be null");

        Objects.requireNonNull(userRepo,
                "IUserRepository cannot be null");

        this.collectionOfPublicationsRepository = collectionOfPublicationsRepository;
        this.factory = factory;
        this.policy = policy;
        this.userRepo = userRepo;
    }

    /**
     * Creates and persists a new {@link CollectionOfPublications}.
     *
     * <p>This method retrieves the user, verifies they are authorized to create
     * a collection, delegates domain object creation to the
     * {@link CollectionOfPublicationsFactory}, and persists the result via the
     * {@link ICollectionOfPublicationsRepository}.</p>
     *
     * @param collectionName    the name of the collection; must not be {@code null}
     * @param collectionGenreId the genre associated with the collection; must not be {@code null}
     * @param userId            the owner of the collection; must not be {@code null}
     * @return the persisted {@link CollectionOfPublications} instance
     * @throws IllegalArgumentException       if no user is found with the given {@code userId}
     * @throws AccessDeniedException          if the user does not have permission to create a collection
     * @throws InstanceAlreadyExistsException if a collection with the same identity already exists
     */
    public CollectionOfPublications createAndSave(
            String collectionName, GenreId collectionGenreId, UserId userId)
            throws InstanceAlreadyExistsException, AccessDeniedException {

        User user = userRepo.findByID(userId).orElseThrow(() ->
                new IllegalArgumentException("User with id " + userId + " not found"));

        if (!policy.canCreateCollectionOfPublications(user)) {
            throw new AccessDeniedException("You do not have permission to create a Collection");
        }

        CollectionOfPublications createdCollection = factory.create(
                collectionName, collectionGenreId, userId);

        CollectionOfPublications savedCollection =
                collectionOfPublicationsRepository.save(createdCollection);

        return savedCollection;
    }

    /**
     * Makes the collection of publications with the given identifier public.
     *
     * <p>The user must be authorized to share collections. If the collection
     * is already public, no update is performed and {@code false} is returned.</p>
     *
     * @param collectionId the identifier of the collection to make public; must not be {@code null}
     * @param userId       the identifier of the user requesting the operation; must not be {@code null}
     * @return {@code true} if the collection was changed to public,
     * {@code false} if it was already public
     * @throws IllegalArgumentException  if no user is found with the given {@code userId}
     * @throws AccessDeniedException     if the user does not have permission to share a collection
     * @throws InstanceNotFoundException if no collection is found with the given {@code collectionId}
     */
    public boolean makeCollectionPublic(CollectionOfPublicationsId collectionId, UserId userId)
            throws InstanceNotFoundException, AccessDeniedException {

        Objects.requireNonNull(collectionId, "CollectionOfPublications Id cannot be null");

        CollectionOfPublications collection = collectionOfPublicationsRepository.findById(collectionId);

        User user = userRepo.findByID(userId).orElseThrow(() ->
                new IllegalArgumentException("User with id " + userId + " not found"));

        if (!policy.canShareCollectionOfPublications(user)) {
            throw new AccessDeniedException("You do not have permission to share a Collection");
        }

        boolean changed = collection.makePublic();

        if (changed) {
            collectionOfPublicationsRepository.update(collection);
        }

        return changed;
    }

    /**
     * Makes a collection of publications public until the specified date and time.
     *
     * <p>The user must be authorized to perform this operation. If the collection
     * is already public until the same date and time, no update is performed and
     * {@code false} is returned.</p>
     *
     * @param collectionId the identifier of the collection to update; must not be {@code null}
     * @param publicUntil  the date and time until which the collection should remain public;
     *                     must not be {@code null} and must be in the future
     * @param userId       the identifier of the user requesting the operation; must not be {@code null}
     * @return {@code true} if the collection was changed and updated; {@code false} otherwise
     * @throws IllegalArgumentException  if no user is found with the given {@code userId}
     * @throws AccessDeniedException     if the user does not have permission to perform this operation
     * @throws InstanceNotFoundException if no collection is found with the given {@code collectionId}
     * @throws NullPointerException      if {@code collectionId} or {@code publicUntil} is {@code null}
     */
    public boolean makeCollectionPublicUntil(
            CollectionOfPublicationsId collectionId, LocalDateTime publicUntil, UserId userId)
            throws InstanceNotFoundException, AccessDeniedException {

        Objects.requireNonNull(collectionId, "CollectionOfPublications Id cannot be null");
        Objects.requireNonNull(publicUntil, "Public until date cannot be null");

        User user = userRepo.findByID(userId).orElseThrow(() ->
                new IllegalArgumentException("User with id " + userId + " not found"));

        if (!policy.canMakeCollectionPublicUntil(user)) {
            throw new AccessDeniedException
                    ("You do not have permission to make this collection public until a given date");
        }

        CollectionOfPublications collection =
                collectionOfPublicationsRepository.findById(collectionId);

        boolean changed = collection.makePublicUntil(publicUntil);

        if (changed) {
            collectionOfPublicationsRepository.update(collection);
        }

        return changed;
    }

    /**
     * Retrieves a {@link CollectionOfPublications} by its unique identifier.
     *
     * <p>This method delegates directly to the repository and returns the
     * persisted collection without applying any business filters.</p>
     *
     * @param collectionId the identifier of the collection to retrieve; must not be {@code null}
     * @return the {@link CollectionOfPublications} matching the given identifier
     * @throws InstanceNotFoundException if no collection is found with the given identifier
     */
    public CollectionOfPublications findById(CollectionOfPublicationsId collectionId)
            throws InstanceNotFoundException {
        return collectionOfPublicationsRepository.findById(collectionId);
    }

    /**
     * Retrieves all {@link CollectionOfPublications} instances that belong to a given user.
     *
     * @param ownerId the identifier of the user whose collections are to be retrieved;
     *                must not be {@code null}
     * @return a list of {@link CollectionOfPublications} owned by the given user;
     * may be empty if the user has no collections
     */
    public List<CollectionOfPublications> findAll(UserId ownerId) {
        return collectionOfPublicationsRepository.findAllByOwnerId(ownerId);
    }
}