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
import TOPSECRET.dto.collectionOfPublications.CollectionOfPublicationsMapper;
import TOPSECRET.dto.collectionOfPublications.response.PublicCollectionOfPublicationsDTO;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.security.access.AccessDeniedException;


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

    private final ICollectionOfPublicationsRepository collectionOfPublicationsRepository;
    private final CollectionOfPublicationsFactory factory;
    private final CollectionOfPublicationsAuthorizationPolicy policy;
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
     * Retrieves a {@link User} by their identifier.
     *
     * <p>This is a private helper method used internally to avoid repeating
     * the user lookup and exception handling logic across service methods.</p>
     *
     * @param userId the identifier of the user to retrieve; must not be {@code null}
     * @return the {@link User} matching the given identifier
     * @throws UsernameNotFoundException if no user is found with the given {@code userId}
     */
    private User getUser(UserId userId) {
        return userRepo.findByID(userId).orElseThrow(() ->
                new UsernameNotFoundException("User with id " + userId + " not found"));
    }

    /**
     * Retrieves a {@link CollectionOfPublications} by its identifier, or throws if not found.
     *
     * <p>This is a private helper used internally to avoid repeating the Optional
     * unwrap and exception handling logic across service methods.</p>
     *
     * @param collectionId the identifier of the collection to retrieve; must not be {@code null}
     * @return the matching {@link CollectionOfPublications}
     * @throws InstanceNotFoundException if no collection exists with the given identifier
     */
    private CollectionOfPublications getCollection(CollectionOfPublicationsId collectionId)
            throws InstanceNotFoundException {
        return collectionOfPublicationsRepository.findById(collectionId)
                .orElseThrow(() -> new InstanceNotFoundException(
                        "Collection not found: " + collectionId));
    }

    /**
     * Retrieves the username associated with the given user identifier.
     *
     * <p>This method delegates user lookup to the underlying user repository
     * and returns the username of the matching {@link User}.</p>
     *
     * @param userId the identifier of the user whose username is to be retrieved;
     *               must not be {@code null}
     * @return the username of the corresponding user
     * @throws UsernameNotFoundException if no user exists with the given identifier
     */
    public String getUserName(UserId userId) {

        Objects.requireNonNull(userId, "userId cannot be null");

        return getUser(userId).getUserName();
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
     * @throws NullPointerException           if {@code collectionName}, {@code collectionGenreId}
     *                                        or {@code userId} is {@code null}
     * @throws UsernameNotFoundException      if no user is found with the given {@code userId}
     * @throws AccessDeniedException          if the user does not have permission to create a collection
     * @throws InstanceAlreadyExistsException if a collection with the same identity already exists
     */
    public CollectionOfPublications createAndSave(
            String collectionName, GenreId collectionGenreId, UserId userId)
            throws InstanceAlreadyExistsException, AccessDeniedException {

        Objects.requireNonNull(collectionName, "collectionName cannot be null");
        Objects.requireNonNull(collectionGenreId, "collectionGenreId cannot be null");
        Objects.requireNonNull(userId, "userId cannot be null");

        User user = getUser(userId);

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
     * <p>The user must be authorized to share collections and must own the collection.
     * If the collection is already public, no update is performed and {@code false}
     * is returned.</p>
     *
     * @param collectionId the identifier of the collection to make public; must not be {@code null}
     * @param userId       the identifier of the user requesting the operation; must not be {@code null}
     * @return {@code true} if the collection was changed to public,
     * {@code false} if it was already public
     * @throws UsernameNotFoundException if no user is found with the given {@code userId}
     * @throws AccessDeniedException     if the user does not have permission to share a collection
     *                                   or does not own the collection
     * @throws InstanceNotFoundException if no collection is found with the given {@code collectionId}
     * @throws NullPointerException      if {@code collectionId} or {@code userId} is {@code null}
     */
    public boolean makeCollectionPublic(CollectionOfPublicationsId collectionId, UserId userId)
            throws InstanceNotFoundException, AccessDeniedException {

        Objects.requireNonNull(collectionId, "CollectionOfPublicationsId cannot be null");
        Objects.requireNonNull(userId, "userId cannot be null");

        User user = getUser(userId);

        if (!policy.canShareCollectionOfPublications(user)) {
            throw new AccessDeniedException("You do not have permission to share a Collection");
        }

        CollectionOfPublications collection = getCollection(collectionId);

        if (!collection.getOwnerId().equals(userId)) {
            throw new AccessDeniedException(
                    "You do not have permission to modify a collection you do not own");
        }
        boolean changed = collection.makePublic();

        if (changed) {
            collectionOfPublicationsRepository.save(collection);
        }

        return changed;
    }

    /**
     * Sets a collection of publications as private for its owner.
     * <p>
     * This method validates the input parameters, checks whether the user has
     * permission to modify the collection, verifies ownership, and then attempts
     * to change the collection's visibility to private.
     * </p>
     *
     * <p>
     * If the collection is already private, no changes are persisted and the method
     * returns {@code false}. If the state changes successfully, the updated entity
     * is saved and the method returns {@code true}.
     * </p>
     *
     * @param collectionId the identifier of the collection of publications to be modified
     * @param userId the identifier of the user attempting the operation
     * @return {@code true} if the collection visibility was changed to private,
     *         {@code false} if no change was needed
     *
     * @throws NullPointerException if {@code collectionId} or {@code userId} is null
     * @throws InstanceNotFoundException if the collection or user cannot be found
     * @throws AccessDeniedException if the user does not have permission to modify
     *         the collection or is not its owner
     */

    public boolean makeCollectionPrivate(CollectionOfPublicationsId collectionId, UserId userId)
            throws InstanceNotFoundException, AccessDeniedException {

        Objects.requireNonNull(collectionId, "CollectionOfPublicationsId cannot be null");
        Objects.requireNonNull(userId, "userId cannot be null");

        User user = getUser(userId);

        if (!policy.canShareCollectionOfPublications(user)) {
            throw new AccessDeniedException("You do not have permission to modify the access to this Collection");
        }

        CollectionOfPublications collection = collectionOfPublicationsRepository.findById(collectionId);

        if (!collection.getOwnerId().equals(userId)) {
            throw new AccessDeniedException(
                    "You do not have permission to modify a collection you do not own");
        }
        boolean changed = collection.makePrivate();

        if (changed) {
            collectionOfPublicationsRepository.save(collection);
        }

        return changed;
    }

    /**
     * Makes a collection of publications public until the specified date and time.
     *
     * <p>The user must be authorized to perform this operation and must own the collection.
     * If the collection is already public until the same date and time, no update is
     * performed and {@code false} is returned.</p>
     *
     * @param collectionId the identifier of the collection to update; must not be {@code null}
     * @param publicUntil  the date and time until which the collection should remain public;
     *                     must not be {@code null} and must be in the future
     * @param userId       the identifier of the user requesting the operation; must not be {@code null}
     * @return {@code true} if the collection was changed and updated; {@code false} otherwise
     * @throws NullPointerException      if {@code collectionId}, {@code publicUntil} or
     *                                   {@code userId} is {@code null}
     * @throws IllegalArgumentException  if {@code publicUntil} is not in the future
     * @throws UsernameNotFoundException if no user is found with the given {@code userId}
     * @throws AccessDeniedException     if the user does not have permission to perform this operation
     *                                   or does not own the collection
     * @throws InstanceNotFoundException if no collection is found with the given {@code collectionId}
     */
    public boolean makeCollectionPublicUntil(
            CollectionOfPublicationsId collectionId, LocalDate publicUntil, UserId userId)
            throws InstanceNotFoundException, AccessDeniedException {

        Objects.requireNonNull(collectionId, "CollectionOfPublications Id cannot be null");
        Objects.requireNonNull(publicUntil, "Public until date cannot be null");
        Objects.requireNonNull(userId, "userId cannot be null");


        if (!publicUntil.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Public until date must be in the future");
        }

        User user = getUser(userId);

        if (!policy.canMakeCollectionPublicUntil(user)) {
            throw new AccessDeniedException
                    ("You do not have permission to make this collection public until a given date");
        }

        CollectionOfPublications collection = getCollection(collectionId);

        if (!collection.getOwnerId().equals(userId)) {
            throw new AccessDeniedException(
                    "You do not have permission to modify a collection you do not own");
        }

        boolean changed = collection.makePublicUntil(publicUntil);

        if (changed) {
            collectionOfPublicationsRepository.save(collection);
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
        return getCollection(collectionId);
    }

    /**
     * Retrieves all {@link CollectionOfPublications} instances that belong to a given user.
     *
     * @param ownerId the identifier of the user whose collections are to be retrieved;
     *                must not be {@code null}
     * @return a list of {@link CollectionOfPublications} owned by the given user;
     * may be empty if the user has no collections
     * @throws NullPointerException if {@code ownerId} is {@code null}
     */
    public List<CollectionOfPublications> findAllByOwnerId(UserId ownerId) {

        Objects.requireNonNull(ownerId, "ownerId cannot be null");

        return collectionOfPublicationsRepository.findAllByOwnerId(ownerId);
    }

    /**
     * Checks whether a {@link CollectionOfPublications} with the given identifier exists.
     *
     * @param collectionOfPublicationsId the identifier to check; must not be {@code null}
     * @return {@code true} if a collection with the given identifier exists;
     * {@code false} otherwise
     * @throws NullPointerException if {@code collectionOfPublicationsId} is {@code null}
     */
    public boolean existsById(CollectionOfPublicationsId collectionOfPublicationsId) {

        Objects.requireNonNull(collectionOfPublicationsId, "collectionOfPublicationsId cannot be null");

        return collectionOfPublicationsRepository.existsById(collectionOfPublicationsId);
    }

    /**
     * Retrieves all public {@link CollectionOfPublications} associated with the given genre.
     *
     * @param genreId the identifier of the genre to filter by; must not be {@code null}
     * @return a list of public {@link CollectionOfPublications} belonging to the given genre;
     * may be empty if none exist
     * @throws NullPointerException if {@code genreId} is {@code null}
     */
    public List<CollectionOfPublications> findPublicCollectionsByGenreId(GenreId genreId) {

        Objects.requireNonNull(genreId, "genreId cannot be null");

        return collectionOfPublicationsRepository.findPublicCollectionsByGenreId(genreId);
    }

    /**
     * Retrieves all collections that are currently public and maps them into
     * {@link PublicCollectionOfPublicationsDTO}.
     *
     * <p>A collection is considered public if it satisfies the domain rule
     * defined in {@link CollectionOfPublications#isCurrentlyPublic()}.</p>
     *
     * <p>This method performs full orchestration:</p>
     * <ul>
     *     <li>retrieves all collections from the repository</li>
     *     <li>filters only currently public collections</li>
     *     <li>resolves the owner's username</li>
     *     <li>maps the result into DTOs for API exposure</li>
     * </ul>
     *
     * @return a list of {@link PublicCollectionOfPublicationsDTO}; may be empty
     * @throws NullPointerException if repository returns null (defensive check)
     */
    public List<PublicCollectionOfPublicationsDTO> findAllPublicCollections() {
        return collectionOfPublicationsRepository.findAll()
                .stream()
                .filter(CollectionOfPublications::isCurrentlyPublic)
                .map(c -> {
                    String ownerName = getUserName(c.getOwnerId());
                    return CollectionOfPublicationsMapper.toPublicDTO(c, ownerName);
                })
                .toList();
    }

    /**
     * Retrieves all {@link CollectionOfPublications} in the system.
     *
     * <p>This operation is restricted to administrative users only. Authorization
     * is enforced via {@link CollectionOfPublicationsAuthorizationPolicy#canViewAllCollections(User)}.</p>
     *
     * <p>No filtering is applied; all persisted collections are returned.</p>
     *
     * @param requesterId the identifier of the user requesting the operation; must not be {@code null}
     * @return a list of all collections in the system; may be empty
     * @throws NullPointerException if {@code requesterId} is {@code null}
     * @throws AccessDeniedException if the user is not authorized to view all collections
     */
    public List<CollectionOfPublications> findAllCollections(UserId requesterId) {

        Objects.requireNonNull(requesterId, "requesterId cannot be null");

        User user = getUser(requesterId);

        if (!policy.canViewAllCollections(user)) {
            throw new AccessDeniedException("User not authorized to view all collections");
        }

        return collectionOfPublicationsRepository.findAll();
    }
}