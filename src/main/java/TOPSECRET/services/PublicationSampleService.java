package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.PublicationSampleAuthorizationPolicy;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.PublicationSample.PublicationSampleFactory;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Objects;

/**
 * Service layer responsible for managing {@link PublicationSample} aggregates.
 *
 * <p>Acts as the primary entry point for all business logic related to publication samples,
 * coordinating between the repository, factory, and authorization policy.</p>
 *
 * <p>Operations are grouped into the following categories:</p>
 * <ul>
 *   <li><b>Persistence</b> — creation and storage of publication samples.</li>
 *   <li><b>Lookup — by identity</b> — queries by unique identifier.</li>
 *   <li><b>Lookup — by associated identifiers</b> — queries filtered by related entity IDs.</li>
 *   <li><b>Lookup — by owner</b> — queries scoped to a specific user.</li>
 *   <li><b>Domain Operations</b> — higher-level operations involving domain-specific logic.</li>
 * </ul>
 *
 * <p>Each operation that mutates state or requires authorization is paired with a corresponding
 * {@code can*} method that exposes the authorization decision for use by the REST layer
 * (e.g. to populate {@code Allow} headers on OPTIONS responses) without coupling the
 * transport layer to HTTP concepts.</p>
 */
@Service
public class PublicationSampleService {

    private final IPublicationSampleRepository publicationSampleRepository;
    private final ICollectionOfPublicationsRepository collectionOfPublicationsRepository;
    private final IUserRepository userRepository;
    private final PublicationSampleFactory factory;
    private final PublicationSampleAuthorizationPolicy policy;
    private final ILibraryRepository libraryRepository;

    /**
     * Constructs a {@code PublicationSampleService} with its required dependencies.
     *
     * @param publicationSampleRepository        the repository for publication sample persistence; must not be {@code null}
     * @param collectionOfPublicationsRepository the repository for collection lookups; must not be {@code null}
     * @param factory                            the factory used to create new publication samples; must not be {@code null}
     * @param policy                             the authorization policy governing access; must not be {@code null}
     * @param userRepository                     the repository for user lookups; must not be {@code null}
     * @param libraryRepository                  the repository for library lookups; must not be {@code null}
     * @throws NullPointerException if any argument is {@code null}
     */
    public PublicationSampleService(IPublicationSampleRepository publicationSampleRepository,
                                    ICollectionOfPublicationsRepository collectionOfPublicationsRepository,
                                    PublicationSampleFactory factory,
                                    PublicationSampleAuthorizationPolicy policy,
                                    IUserRepository userRepository,
                                    ILibraryRepository libraryRepository) {
        this.publicationSampleRepository = Objects.requireNonNull(publicationSampleRepository, "Repository cannot be null");
        this.collectionOfPublicationsRepository = Objects.requireNonNull(collectionOfPublicationsRepository, "Repository cannot be null");
        this.factory = Objects.requireNonNull(factory, "Factory cannot be null");
        this.policy = Objects.requireNonNull(policy, "Policy cannot be null");
        this.userRepository = Objects.requireNonNull(userRepository, "UserRepository cannot be null");
        this.libraryRepository = Objects.requireNonNull(libraryRepository, "LibraryRepository cannot be null");
    }

    // =========================================================================
    // Persistence
    // =========================================================================

    /**
     * Creates a new {@link PublicationSample} and persists it.
     *
     * <p>Verifies that the requesting user exists and is authorized to create a publication sample
     * before delegating creation to the factory. Throws if a sample with the same identity
     * already exists.</p>
     *
     * @param bibliographicalDataId the bibliographical data identifier to associate; must not be {@code null}
     * @param userId                the ID of the user creating the sample; must not be {@code null}
     * @param editionId             the edition identifier to associate; must not be {@code null}
     * @param condition             the physical condition of the publication sample; must not be {@code null}
     * @return the newly created and persisted {@link PublicationSample}
     * @throws EntityNotFoundException        if no user is found for the given {@code userId}
     * @throws AccessDeniedException          if the user is not authorized to create a publication sample
     * @throws InstanceAlreadyExistsException if a publication sample with the same identity already exists
     */
    public PublicationSample createAndSave(
            BibliographicalDataId bibliographicalDataId,
            UserId userId,
            EditionId editionId,
            Condition condition) throws InstanceAlreadyExistsException {

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!policy.canCreatePublicationSample(user)) {
            throw new AccessDeniedException("Access denied");
        }

        PublicationSample created = factory.create(bibliographicalDataId, userId, editionId, condition);

        boolean alreadyExists = publicationSampleRepository.existsById(created.id());

        if (alreadyExists) {
            throw new InstanceAlreadyExistsException("PublicationSample already exists");
        }

        return publicationSampleRepository.save(created);
    }

    /**
     * Returns whether the given user is authorized to create a publication sample.
     *
     * @param userId the ID of the requesting user; must not be {@code null}
     * @return {@code true} if the user may create a publication sample, {@code false} otherwise
     * @throws EntityNotFoundException if no user is found for the given {@code userId}
     * @throws NullPointerException    if {@code userId} is {@code null}
     */
    public boolean canCreatePublicationSample(UserId userId) {

        Objects.requireNonNull(userId, "UserId cannot be null");

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        boolean isAuthorized = policy.canCreatePublicationSample(user);

        return isAuthorized;
    }

    // =========================================================================
    // Lookup — by identity
    // =========================================================================

    /**
     * Finds a {@link PublicationSample} by its unique identifier.
     *
     * @param publicationSampleId the identifier to search for; must not be {@code null}
     * @return the matching {@link PublicationSample}
     * @throws EntityNotFoundException if no sample is found for the given identifier
     */
    public PublicationSample findById(PublicationSampleId publicationSampleId) {

        PublicationSample publicationSample = publicationSampleRepository.findById(publicationSampleId)
                .orElseThrow(() -> new EntityNotFoundException("PublicationSample not found"));

        return publicationSample;
    }

    /**
     * Retrieves all {@link PublicationSample} instances stored in the repository.
     *
     * @return a list of all publication samples; never {@code null}, may be empty
     */
    public List<PublicationSample> findAll() {

        List<PublicationSample> allPublicationSamples = publicationSampleRepository.findAll();

        return allPublicationSamples;
    }

    // =========================================================================
    // Lookup — by associated identifiers
    // =========================================================================

    /**
     * Retrieves all {@link PublicationSample} instances associated with the given bibliographical data ID.
     *
     * @param bdId the bibliographical data identifier; must not be {@code null}
     * @return a list of matching {@link PublicationSample}; never {@code null}, may be empty
     * @throws NullPointerException if {@code bdId} is {@code null}
     */
    public List<PublicationSample> findByBibliographicalDataId(BibliographicalDataId bdId) {
        Objects.requireNonNull(bdId, "BibliographicalDataId cannot be null");

        List<PublicationSample> publicationSamples = publicationSampleRepository.findByBibliographicalDataId(bdId);

        return publicationSamples;
    }

    /**
     * Retrieves all {@link PublicationSample} instances whose bibliographical data ID is in the given list.
     *
     * @param bdIds the list of bibliographical data identifiers; must not be {@code null}
     * @return a list of matching {@link PublicationSample}; never {@code null}, may be empty
     * @throws NullPointerException if {@code bdIds} is {@code null}
     */
    public List<PublicationSample> findByBibliographicalDataIdIn(List<BibliographicalDataId> bdIds) {
        Objects.requireNonNull(bdIds, "BibliographicalDataIds cannot be null");

        List<PublicationSample> publicationSamples = publicationSampleRepository.findByBibliographicalDataIdIn(bdIds);

        return publicationSamples;
    }

    /**
     * Retrieves the identifiers of all {@link PublicationSample} instances whose
     * bibliographical data ID is in the given list.
     *
     * <p>Projects directly to {@link PublicationSampleId} at the repository level,
     * avoiding the cost of hydrating full {@link PublicationSample} entities when
     * only their identifiers are needed.</p>
     *
     * @param bdIds the list of bibliographical data identifiers; must not be {@code null}
     * @return a list of matching {@link PublicationSampleId}; never {@code null}, may be empty
     * @throws NullPointerException if {@code bdIds} is {@code null}
     */
    public List<PublicationSampleId> findIdsByBibliographicalDataIdIn(List<BibliographicalDataId> bdIds) {
        Objects.requireNonNull(bdIds, "BibliographicalDataIds cannot be null");

        List<PublicationSampleId> publicationSampleIds = publicationSampleRepository.findIdsByBibliographicalDataIdIn(bdIds);

        return publicationSampleIds;
    }

    /**
     * Retrieves all {@link PublicationSample} instances associated with any of the given edition IDs.
     *
     * @param listOfEditionIds the list of edition identifiers; must not be {@code null}
     * @return a list of matching {@link PublicationSample}; never {@code null}, may be empty
     * @throws NullPointerException if {@code listOfEditionIds} is {@code null}
     */
    public List<PublicationSample> findByEditionIdIn(List<EditionId> listOfEditionIds) {
        Objects.requireNonNull(listOfEditionIds, "Edition Ids cannot be null");

        List<PublicationSample> publicationSamples = publicationSampleRepository.findByEditionIdIn(listOfEditionIds);

        return publicationSamples;
    }

    /**
     * Retrieves the identifiers of all {@link PublicationSample} instances associated
     * with any of the given edition IDs.
     *
     * <p>Projects directly to {@link PublicationSampleId} at the repository level,
     * avoiding the cost of hydrating full {@link PublicationSample} entities when
     * only their identifiers are needed.</p>
     *
     * @param listOfEditionIds the list of edition identifiers; must not be {@code null}
     * @return a list of matching {@link PublicationSampleId}; never {@code null}, may be empty
     * @throws NullPointerException if {@code listOfEditionIds} is {@code null}
     */
    public List<PublicationSampleId> findIdsByEditionIdIn(List<EditionId> listOfEditionIds) {
        Objects.requireNonNull(listOfEditionIds, "Edition Ids cannot be null");

        List<PublicationSampleId> publicationSampleIds = publicationSampleRepository.findIdsByEditionIdIn(listOfEditionIds);

        return publicationSampleIds;
    }

    /**
     * Retrieves all {@link PublicationSample} instances that belong to the specified collection.
     *
     * @param collectionId the collection identifier; must not be {@code null}
     * @param userId       the identifier of the requesting user; must not be {@code null}
     * @return a list of matching {@link PublicationSample}; never {@code null}, may be empty
     * @throws EntityNotFoundException   if no user is found for the given {@code userId}
     * @throws AccessDeniedException     if the user is not authorized or does not own the collection
     * @throws InstanceNotFoundException if no collection is found for the given {@code collectionId}
     * @throws NullPointerException      if {@code collectionId} is {@code null}
     */
    public List<PublicationSample> findByCollectionOfPublicationsId(CollectionOfPublicationsId collectionId, UserId userId) throws InstanceNotFoundException {
        Objects.requireNonNull(collectionId, "CollectionId cannot be null");

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!policy.canFindByCollectionOfPublicationsId(user)) {
            throw new AccessDeniedException("Access denied");
        }

        CollectionOfPublications collectionOfPublications = collectionOfPublicationsRepository.findById(collectionId)
                .orElseThrow(() -> new InstanceNotFoundException("Collection not found: " + collectionId));

        boolean isOwner = collectionOfPublications.getOwnerId().equals(userId);

        if (!isOwner) {
            throw new AccessDeniedException("Access denied");
        }

        List<PublicationSample> publicationSamples = publicationSampleRepository.findByCollectionOfPublicationsId(collectionId);

        return publicationSamples;
    }

    /**
     * Returns whether the given user is authorized to query publications by the specified collection.
     *
     * <p>Requires the user to have the appropriate role and to own the collection.</p>
     *
     * @param collectionId the unique identifier of the collection; must not be {@code null}
     * @param userId       the ID of the requesting user; must not be {@code null}
     * @return {@code true} if the user may query publications by the collection, {@code false} otherwise
     * @throws NullPointerException    if {@code collectionId} or {@code userId} is {@code null}
     * @throws EntityNotFoundException if no user or collection is found for the given identifiers
     */
    public boolean canQueryByCollection(CollectionOfPublicationsId collectionId, UserId userId) {

        Objects.requireNonNull(collectionId, "CollectionId cannot be null");
        Objects.requireNonNull(userId, "UserId cannot be null");

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        CollectionOfPublications collection = collectionOfPublicationsRepository.findById(collectionId)
                .orElseThrow(() -> new EntityNotFoundException("Collection not found"));

        boolean isAuthorized = policy.canFindByCollectionOfPublicationsId(user);
        boolean isOwner = collection.getOwnerId().equals(userId);

        return isAuthorized && isOwner;
    }

    // =========================================================================
    // Lookup — by owner
    // =========================================================================

    /**
     * Retrieves all {@link PublicationSample} instances owned by the given user.
     *
     * @param userId the identifier of the user whose publications are to be retrieved; must not be {@code null}
     * @return a list of {@link PublicationSample} owned by the user; never {@code null}, may be empty
     * @throws EntityNotFoundException if no user is found for the given {@code userId}
     * @throws AccessDeniedException   if the user is not authorized
     * @throws NullPointerException    if {@code userId} is {@code null}
     */
    public List<PublicationSample> findAllPublicationsOwnedByUser(UserId userId) {
        Objects.requireNonNull(userId, "UserId cannot be null");

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!policy.canFindPublicationsOwnedByUserId(user)) {
            throw new AccessDeniedException("Access denied");
        }

        List<PublicationSample> publicationSamples = publicationSampleRepository.findAllByOwnerId(userId);

        return publicationSamples;
    }

    /**
     * Returns whether the given user is authorized to retrieve their own publication samples.
     *
     * @param userId the ID of the requesting user; must not be {@code null}
     * @return {@code true} if the user may retrieve their own publications, {@code false} otherwise
     * @throws NullPointerException    if {@code userId} is {@code null}
     * @throws EntityNotFoundException if no user is found for the given {@code userId}
     */
    public boolean canFindOwnPublications(UserId userId) {

        Objects.requireNonNull(userId, "UserId cannot be null");

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        boolean isAuthorized = policy.canFindPublicationsOwnedByUserId(user);

        return isAuthorized;
    }

    /**
     * Retrieves all publications owned by the given user that are assigned to a library.
     *
     * @param userId the user identifier; must not be {@code null}
     * @return a list of {@link PublicationSample} assigned to a library; never {@code null}, may be empty
     * @throws EntityNotFoundException if no user is found for the given {@code userId}
     * @throws AccessDeniedException   if the user is not authorized
     * @throws NullPointerException    if {@code userId} is {@code null}
     */
    public List<PublicationSample> findPublicationsInLibrary(UserId userId) {
        Objects.requireNonNull(userId, "UserId cannot be null");

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!policy.canFindPublicationsInLibrary(user)) {
            throw new AccessDeniedException("Access denied");
        }

        List<PublicationSample> publicationSamples = publicationSampleRepository.findPublicationsInLibrary(userId);

        return publicationSamples;
    }

    /**
     * Returns whether the given user is authorized to retrieve their publication samples
     * assigned to a library.
     *
     * @param userId the ID of the requesting user; must not be {@code null}
     * @return {@code true} if the user is authorized and has a library, {@code false} otherwise
     * @throws NullPointerException    if {@code userId} is {@code null}
     * @throws EntityNotFoundException if no user is found for the given {@code userId}
     */
    public boolean canFindPublicationsInLibrary(UserId userId) {

        Objects.requireNonNull(userId, "UserId cannot be null");

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        boolean isAuthorized = policy.canFindPublicationsInLibrary(user);
        boolean hasLibrary = libraryRepository.findByUserId(userId).isPresent();

        return isAuthorized && hasLibrary;
    }

    /**
     * Retrieves all publications owned by the given user that are not assigned to any library.
     *
     * @param userId the user identifier; must not be {@code null}
     * @return a list of {@link PublicationSample} not assigned to any library; never {@code null}, may be empty
     * @throws EntityNotFoundException if no user is found for the given {@code userId}
     * @throws AccessDeniedException   if the user is not authorized
     * @throws NullPointerException    if {@code userId} is {@code null}
     */
    public List<PublicationSample> findPublicationsNotInLibrary(UserId userId) {
        Objects.requireNonNull(userId, "UserId cannot be null");

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!policy.canFindPublicationsNotInLibrary(user)) {
            throw new AccessDeniedException("Access denied");
        }

        List<PublicationSample> publicationSamples = publicationSampleRepository.findPublicationsNotInLibrary(userId);

        return publicationSamples;
    }

    /**
     * Returns whether the given user is authorized to retrieve their publication samples
     * not assigned to any library.
     *
     * @param userId the ID of the requesting user; must not be {@code null}
     * @return {@code true} if the user may retrieve their not-in-library publications, {@code false} otherwise
     * @throws NullPointerException    if {@code userId} is {@code null}
     * @throws EntityNotFoundException if no user is found for the given {@code userId}
     */
    public boolean canFindPublicationsNotInLibrary(UserId userId) {

        Objects.requireNonNull(userId, "UserId cannot be null");

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        boolean isAuthorized = policy.canFindPublicationsNotInLibrary(user);

        return isAuthorized;
    }

    /**
     * Retrieves all publication samples in the user's library that are neither sold nor listed for sale.
     *
     * <p>Excludes samples with statuses {@link PublicationStatus#SOLD} and
     * {@link PublicationStatus#FOR_SALE}. The library is resolved internally from
     * the user's identity.</p>
     *
     * @param userId the identifier of the requesting user; must not be {@code null}
     * @return a list of available {@link PublicationSample}; never {@code null}, may be empty
     * @throws EntityNotFoundException if no user or library is found for the given {@code userId}
     * @throws AccessDeniedException   if the user is not authorized
     * @throws NullPointerException    if {@code userId} is {@code null}
     */
    public List<PublicationSample> findByLibraryIdAndPublicationStatusNotIn(UserId userId) {

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!policy.canFindByLibraryIdAndPublicationStatusNotIn(user)) {
            throw new AccessDeniedException("Access denied");
        }

        LibraryId libraryId = libraryRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Library not found"))
                .id();

        List<PublicationStatus> excluded = List.of(PublicationStatus.SOLD, PublicationStatus.FOR_SALE);

        List<PublicationSample> publicationSamples = publicationSampleRepository.findByLibraryIdAndPublicationStatusNotIn(libraryId, excluded);

        return publicationSamples;
    }

    /**
     * Returns whether the given user is authorized to retrieve their available publication
     * samples — those in their library that are neither sold nor listed for sale.
     *
     * @param userId the ID of the requesting user; must not be {@code null}
     * @return {@code true} if the user is authorized and has a library, {@code false} otherwise
     * @throws NullPointerException    if {@code userId} is {@code null}
     * @throws EntityNotFoundException if no user is found for the given {@code userId}
     */
    public boolean canFindAvailableInLibrary(UserId userId) {

        Objects.requireNonNull(userId, "UserId cannot be null");

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        boolean isAuthorized = policy.canFindByLibraryIdAndPublicationStatusNotIn(user);
        boolean hasLibrary = libraryRepository.findByUserId(userId).isPresent();

        return isAuthorized && hasLibrary;
    }

    // =========================================================================
    // Domain Operations
    // =========================================================================

    /**
     * Returns all publications owned by the given user that are eligible to be added to the specified collection.
     *
     * <p>A publication is considered eligible if it is assigned to a library and not already
     * present in the target collection.</p>
     *
     * @param userId       the identifier of the user whose publications are evaluated; must not be {@code null}
     * @param collectionId the identifier of the target collection; must not be {@code null}
     * @return a list of {@link PublicationSample} eligible for addition to the collection;
     *         never {@code null}, may be empty
     * @throws EntityNotFoundException   if no user is found for the given {@code userId}
     * @throws AccessDeniedException     if the user is not authorized or does not own the collection
     * @throws InstanceNotFoundException if no collection is found for the given {@code collectionId}
     * @throws NullPointerException      if {@code userId} or {@code collectionId} is {@code null}
     */
    public List<PublicationSample> findAvailableForCollection(UserId userId, CollectionOfPublicationsId collectionId) throws InstanceNotFoundException {
        Objects.requireNonNull(userId, "UserId cannot be null");
        Objects.requireNonNull(collectionId, "CollectionId cannot be null");

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!policy.canFindAvailableForCollection(user)) {
            throw new AccessDeniedException("Access denied");
        }

        CollectionOfPublications collectionOfPublications = collectionOfPublicationsRepository.findById(collectionId)
                .orElseThrow(() -> new InstanceNotFoundException("Collection not found: " + collectionId));

        boolean isOwner = collectionOfPublications.getOwnerId().equals(userId);

        if (!isOwner) {
            throw new AccessDeniedException("Access denied");
        }

        List<PublicationSample> allUserPublications = publicationSampleRepository.findPublicationsInLibrary(userId);

        List<PublicationSample> publicationsInCollection = publicationSampleRepository.findByCollectionOfPublicationsId(collectionId);

        List<PublicationSample> availablePublications = allUserPublications.stream()
                .filter(p -> !publicationsInCollection.contains(p))
                .toList();

        return availablePublications;
    }

    /**
     * Returns whether the given user is authorized to retrieve publication samples
     * eligible for addition to a collection they own.
     *
     * @param userId       the identifier of the requesting user; must not be {@code null}
     * @param collectionId the identifier of the target collection; must not be {@code null}
     * @return {@code true} if the user may query available publications for the collection,
     *         {@code false} otherwise
     * @throws NullPointerException      if {@code userId} or {@code collectionId} is {@code null}
     * @throws EntityNotFoundException   if no user is found for the given {@code userId}
     * @throws InstanceNotFoundException if no collection is found for the given {@code collectionId}
     */
    public boolean canFindAvailableForCollection(UserId userId, CollectionOfPublicationsId collectionId)
            throws InstanceNotFoundException {

        Objects.requireNonNull(userId, "UserId cannot be null");
        Objects.requireNonNull(collectionId, "CollectionId cannot be null");

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        CollectionOfPublications collection = collectionOfPublicationsRepository.findById(collectionId)
                .orElseThrow(() -> new InstanceNotFoundException("Collection not found: " + collectionId));

        boolean isAuthorized = policy.canFindAvailableForCollection(user);
        boolean isOwner = collection.getOwnerId().equals(userId);

        return isAuthorized && isOwner;
    }

    /**
     * Assigns a {@link PublicationSample} to the requesting user's library and persists the change.
     *
     * <p>The library is resolved internally from the user's identity. Requires the requesting
     * user to be authorized and to own the publication sample.</p>
     *
     * @param publicationSampleId the identifier of the publication sample to assign; must not be {@code null}
     * @param userId              the identifier of the requesting user; must not be {@code null}
     * @return the updated {@link PublicationSample} with the library assignment applied
     * @throws EntityNotFoundException if no user, library, or publication sample is found
     * @throws AccessDeniedException   if the user is not authorized or does not own the publication sample
     * @throws NullPointerException    if {@code publicationSampleId} or {@code userId} is {@code null}
     */
    @Transactional
    public PublicationSample addPublicationToLibrary(PublicationSampleId publicationSampleId, UserId userId) {

        Objects.requireNonNull(publicationSampleId, "PublicationSampleId cannot be null.");

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!policy.canAddPublicationToLibrary(user)) {
            throw new AccessDeniedException("Access denied");
        }

        LibraryId libraryId = libraryRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Library not found"))
                .id();

        PublicationSample publicationSample = publicationSampleRepository.findById(publicationSampleId)
                .orElseThrow(() -> new EntityNotFoundException("PublicationSample not found"));

        boolean isOwner = publicationSample.isOwnedByUser(userId);

        if (!isOwner) {
            throw new AccessDeniedException("User does not own this publication sample");
        }

        publicationSample.addPublicationToLibrary(libraryId);

        return publicationSampleRepository.save(publicationSample);
    }

    /**
     * Returns whether the given user is authorized to add a publication sample to their library.
     *
     * @param userId the ID of the requesting user; must not be {@code null}
     * @return {@code true} if the user is authorized and has a library, {@code false} otherwise
     * @throws NullPointerException    if {@code userId} is {@code null}
     * @throws EntityNotFoundException if no user is found for the given {@code userId}
     */
    public boolean canAddPublicationToLibrary(UserId userId) {

        Objects.requireNonNull(userId, "UserId cannot be null");

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        boolean isAuthorized = policy.canAddPublicationToLibrary(user);
        boolean hasLibrary = libraryRepository.findByUserId(userId).isPresent();

        return isAuthorized && hasLibrary;
    }

    /**
     * Assigns a {@link PublicationSample} to the specified collection and persists the change.
     *
     * <p>Requires the requesting user to be authorized, to own the publication sample,
     * and to own the target collection.</p>
     *
     * @param collectionId the identifier of the target collection; must not be {@code null}
     * @param pubSampleId  the identifier of the publication sample to assign; must not be {@code null}
     * @param userId       the identifier of the requesting user; must not be {@code null}
     * @return the updated {@link PublicationSample} with the collection assignment applied
     * @throws EntityNotFoundException if no user, publication sample, or collection is found
     * @throws AccessDeniedException   if the user is not authorized, does not own the publication sample,
     *                                 or does not own the target collection
     * @throws NullPointerException    if {@code collectionId} or {@code pubSampleId} is {@code null}
     */
    @Transactional
    public PublicationSample assignPublicationToCollection(CollectionOfPublicationsId collectionId, PublicationSampleId pubSampleId, UserId userId) {
        Objects.requireNonNull(collectionId, "CollectionId cannot be null.");
        Objects.requireNonNull(pubSampleId, "PublicationSampleId cannot be null.");

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!policy.canAssignPublicationToCollection(user)) {
            throw new AccessDeniedException("Access denied");
        }

        PublicationSample publicationSample = publicationSampleRepository.findById(pubSampleId)
                .orElseThrow(() -> new EntityNotFoundException("PublicationSample not found"));

        boolean ownsSample = publicationSample.isOwnedByUser(userId);

        if (!ownsSample) {
            throw new AccessDeniedException("User does not own this publication sample");
        }

        CollectionOfPublications collection = collectionOfPublicationsRepository.findById(collectionId)
                .orElseThrow(() -> new EntityNotFoundException("Collection not found"));

        boolean ownsCollection = collection.getOwnerId().equals(userId);

        if (!ownsCollection) {
            throw new AccessDeniedException("User does not own this collection");
        }

        publicationSample.addPublicationToCollection(collectionId);

        return publicationSampleRepository.save(publicationSample);
    }

    /**
     * Returns whether the given user is authorized to assign a publication sample to a collection.
     *
     * @param userId the ID of the requesting user; must not be {@code null}
     * @return {@code true} if the user is authorized and owns at least one collection,
     *         {@code false} otherwise
     * @throws NullPointerException    if {@code userId} is {@code null}
     * @throws EntityNotFoundException if no user is found for the given {@code userId}
     */
    public boolean canAssignPublicationToCollection(UserId userId) {

        Objects.requireNonNull(userId, "UserId cannot be null");

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        boolean isAuthorized = policy.canAssignPublicationToCollection(user);
        boolean hasCollections = !collectionOfPublicationsRepository.findAllByOwnerId(userId).isEmpty();

        return isAuthorized && hasCollections;
    }

    /**
     * Retrieves all {@link PublicationSample} instances belonging to a public collection.
     *
     * <p>Unlike {@link #findByCollectionOfPublicationsId}, this method does not enforce
     * ownership — any registered user may access the publications of a collection that
     * is currently public.</p>
     *
     * @param collectionId the identifier of the collection to retrieve publications from;
     *                     must not be {@code null}
     * @param userId       the identifier of the requesting user; must not be {@code null}
     * @return a list of {@link PublicationSample} instances belonging to the collection;
     *         may be empty
     * @throws EntityNotFoundException   if no user is found for the given {@code userId}
     * @throws AccessDeniedException     if the user lacks the required role or the collection is not public
     * @throws InstanceNotFoundException if no collection is found for the given {@code collectionId}
     */
    public List<PublicationSample> findPublicationsInPublicCollection(CollectionOfPublicationsId collectionId, UserId userId)
            throws InstanceNotFoundException {

        Objects.requireNonNull(collectionId, "CollectionId cannot be null");

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!policy.canFindPublicationsInPublicCollection(user)) {
            throw new AccessDeniedException("Access denied");
        }

        CollectionOfPublications collection = collectionOfPublicationsRepository.findById(collectionId)
                .orElseThrow(() -> new InstanceNotFoundException("Collection not found: " + collectionId));

        boolean isPublic = collection.isCurrentlyPublic();

        if (!isPublic) {
            throw new AccessDeniedException("Collection is not public");
        }

        List<PublicationSample> publicationSamples = publicationSampleRepository.findByCollectionOfPublicationsId(collectionId);

        return publicationSamples;
    }

    /**
     * Returns whether the given user is authorized to retrieve publications from a public collection.
     *
     * @param userId the ID of the requesting user; must not be {@code null}
     * @return {@code true} if the user may view shared collection publications, {@code false} otherwise
     * @throws NullPointerException    if {@code userId} is {@code null}
     * @throws EntityNotFoundException if no user is found for the given {@code userId}
     */
    public boolean canFindPublicationsInPublicCollection(UserId userId) {

        Objects.requireNonNull(userId, "UserId cannot be null");

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        boolean isAuthorized = policy.canFindByCollectionOfPublicationsId(user);

        return isAuthorized;
    }
}