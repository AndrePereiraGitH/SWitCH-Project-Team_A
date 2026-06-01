package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.PublicationSampleAuthorizationPolicy;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.Objects;

/**
 * Service layer responsible for managing {@link PublicationSample} aggregates.
 *
 * <p>Acts as the primary entry point for all business logic related to publication samples,
 * coordinating between the repository, factory, and authorization policy.</p>
 *
 * <p>Operations are grouped into three categories:</p>
 * <ul>
 *   <li><b>Persistence</b> — creation and storage of publication samples.</li>
 *   <li><b>Lookup</b> — queries by identity or associated identifiers.</li>
 *   <li><b>Domain Operations</b> — higher-level operations involving domain-specific logic.</li>
 * </ul>
 */
@Service
public class PublicationSampleService {

    private final IPublicationSampleRepository publicationSampleRepository;
    private final IUserRepository userRepository;
    private final PublicationSampleFactory factory;
    private final PublicationSampleAuthorizationPolicy policy;
    private final ILibraryRepository libraryRepository;

    /**
     * Constructs a {@code PublicationSampleService} with its required dependencies.
     *
     * @param publicationSampleRepository the repository for publication sample persistence; must not be {@code null}
     * @param factory                     the factory used to create new publication samples; must not be {@code null}
     * @param policy                      the authorization policy governing access; must not be {@code null}
     * @param userRepository              the repository for user lookups; must not be {@code null}
     * @throws NullPointerException if any argument is {@code null}
     */
    public PublicationSampleService(IPublicationSampleRepository publicationSampleRepository,
                                    PublicationSampleFactory factory,
                                    PublicationSampleAuthorizationPolicy policy,
                                    IUserRepository userRepository,
                                    ILibraryRepository libraryRepository) {
        this.publicationSampleRepository = Objects.requireNonNull(publicationSampleRepository, "Repository cannot be null");
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

        if (publicationSampleRepository.existsById(created.id())) {
            throw new InstanceAlreadyExistsException("PublicationSample already exists");
        }

        PublicationSample saved = publicationSampleRepository.save(created);

        return saved;
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
        List<PublicationSample> allPublications = publicationSampleRepository.findAll();

        return allPublications;
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

        List<PublicationSample> allPublicationsWithSpecificBDId =
                publicationSampleRepository.findByBibliographicalDataId(bdId);

        return allPublicationsWithSpecificBDId;
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

        List<PublicationSample> allPublicationsWithSpecificBDIds =
                publicationSampleRepository.findByBibliographicalDataIdIn(bdIds);

        return allPublicationsWithSpecificBDIds;
    }

    /**
     * Retrieves the identifiers of all {@link PublicationSample} instances whose
     * bibliographical data ID is in the given list.
     *
     * <p>This method projects directly to {@link PublicationSampleId} at the
     * repository level, avoiding the cost of hydrating full {@link PublicationSample}
     * entities when only their identifiers are needed.</p>
     *
     * @param bdIds the list of bibliographical data identifiers; must not be {@code null}
     * @return a list of matching {@link PublicationSampleId}; never {@code null}, may be empty
     * @throws NullPointerException if {@code bdIds} is {@code null}
     */
    public List<PublicationSampleId> findIdsByBibliographicalDataIdIn(List<BibliographicalDataId> bdIds) {
        Objects.requireNonNull(bdIds, "BibliographicalDataIds cannot be null");

        List<PublicationSampleId> allPublicationsIdsWithSpecificBDIds =
                publicationSampleRepository.findIdsByBibliographicalDataIdIn(bdIds);


        return allPublicationsIdsWithSpecificBDIds;
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

        List<PublicationSample> allPublicationsWithSpecificEditionIds =
                publicationSampleRepository.findByEditionIdIn(listOfEditionIds);

        return allPublicationsWithSpecificEditionIds;
    }

    /**
     * Retrieves the identifiers of all {@link PublicationSample} instances associated
     * with any of the given edition IDs.
     *
     * <p>This method projects directly to {@link PublicationSampleId} at the
     * repository level, avoiding the cost of hydrating full {@link PublicationSample}
     * entities when only their identifiers are needed.</p>
     *
     * @param listOfEditionIds the list of edition identifiers; must not be {@code null}
     * @return a list of matching {@link PublicationSampleId}; never {@code null}, may be empty
     * @throws NullPointerException if {@code listOfEditionIds} is {@code null}
     */
    public List<PublicationSampleId> findIdsByEditionIdIn(List<EditionId> listOfEditionIds) {
        Objects.requireNonNull(listOfEditionIds, "Edition Ids cannot be null");

        List<PublicationSampleId> allPublicationsIdsWithSpecificEditionIds =
                publicationSampleRepository.findIdsByEditionIdIn(listOfEditionIds);

        return allPublicationsIdsWithSpecificEditionIds;
    }


    /**
     * Retrieves all {@link PublicationSample} instances that belong to the specified collection.
     *
     * @param collectionId the collection identifier; must not be {@code null}
     * @param userId       the identifier of the requesting user; must not be {@code null}
     * @return a list of matching {@link PublicationSample}; never {@code null}, may be empty
     * @throws EntityNotFoundException if no user is found for the given {@code userId}
     * @throws AccessDeniedException   if the user is not authorized
     * @throws NullPointerException    if {@code collectionId} is {@code null}
     */
    public List<PublicationSample> findByCollectionOfPublicationsId(CollectionOfPublicationsId collectionId, UserId userId) {
        Objects.requireNonNull(collectionId, "CollectionId cannot be null");

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!policy.canFindByCollectionOfPublicationsId(user)) {
            throw new AccessDeniedException("Access denied");
        }

        List<PublicationSample> publicationSamplesFromCollection =
                publicationSampleRepository.findByCollectionOfPublicationsId(collectionId);

        return publicationSamplesFromCollection;
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
    public List<PublicationSample> findByUserIdAndLibraryIdIsNotNull(UserId userId) {
        Objects.requireNonNull(userId, "UserId cannot be null");

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!policy.canFindByUserIdAndLibraryIdIsNotNull(user)) {
            throw new AccessDeniedException("Access denied");
        }

        List<PublicationSample> publicationSamplesOwnedByUserInLibrary =
                publicationSampleRepository.findByUserIdAndLibraryIdIsNotNull(userId);

        return publicationSamplesOwnedByUserInLibrary;
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
    public List<PublicationSample> findByUserIdAndLibraryIdIsNull(UserId userId) {
        Objects.requireNonNull(userId, "UserId cannot be null");

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!policy.canFindByUserIdAndLibraryIdIsNull(user)) {
            throw new AccessDeniedException("Access denied");
        }

        List<PublicationSample> publicationSamplesOwnedByUserNotInLibrary =
                publicationSampleRepository.findByUserIdAndLibraryIdIsNull(userId);

        return publicationSamplesOwnedByUserNotInLibrary;
    }

    /**
     * Retrieves all publication samples in the given library that are neither sold nor listed for sale.
     *
     * <p>Excludes samples with statuses {@link PublicationStatus#SOLD} and
     * {@link PublicationStatus#FOR_SALE}.</p>
     *
     * @param userId    the identifier of the requesting user; must not be {@code null}
     * @return a list of {@link PublicationSample} available; never {@code null}, may be empty
     * @throws EntityNotFoundException if no user is found for the given {@code userId}
     * @throws AccessDeniedException   if the user is not authorized
     * @throws NullPointerException    if {@code libraryId} is {@code null}
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

        List<PublicationSample> publicationSamplesAvailable =
                publicationSampleRepository.findByLibraryIdAndPublicationStatusNotIn(libraryId, excluded);

        return publicationSamplesAvailable;
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
     * @return a list of {@link PublicationSample} available for addition to the collection;
     *         never {@code null}, may be empty
     * @throws EntityNotFoundException if no user is found for the given {@code userId}
     * @throws AccessDeniedException   if the user is not authorized
     * @throws NullPointerException    if {@code userId} or {@code collectionId} is {@code null}
     */
    public List<PublicationSample> findAvailableForCollection(UserId userId, CollectionOfPublicationsId collectionId) {
        Objects.requireNonNull(userId, "UserId cannot be null");
        Objects.requireNonNull(collectionId, "CollectionId cannot be null");

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!policy.canFindAvailableForCollection(user)) {
            throw new AccessDeniedException("Access denied");
        }

        List<PublicationSample> allUserPublications =
                publicationSampleRepository.findByUserIdAndLibraryIdIsNotNull(userId);

        List<PublicationSample> publicationsInCollection =
                publicationSampleRepository.findByCollectionOfPublicationsId(collectionId);

        List<PublicationSample> publicationsAvailableForCollection =
                allUserPublications.stream()
                        .filter(p -> !publicationsInCollection.contains(p))
                        .toList();

        return publicationsAvailableForCollection;
    }

    /**
     * Assigns a {@link PublicationSample} to the specified library and persists the change.
     *
     * @param publicationSampleId the identifier of the publication sample to assign; must not be {@code null}
     * @param userId              the identifier of the requesting user; must not be {@code null}
     * @return the updated {@link PublicationSample} with the library assignment applied
     * @throws EntityNotFoundException if no user or publication sample is found
     * @throws AccessDeniedException   if the user is not authorized
     * @throws NullPointerException    if {@code publicationSampleId} or {@code libraryId} is {@code null}
     */
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

        publicationSample.addPublicationToLibrary(libraryId);

        PublicationSample publicationAssignedToLibrary = publicationSampleRepository.save(publicationSample);

        return publicationAssignedToLibrary;
    }

    /**
     * Assigns a single {@link PublicationSample} to the specified collection and persists the change.
     *
     * @param collectionId the identifier of the target collection; must not be {@code null}
     * @param pubSampleId  the identifier of the publication sample to assign; must not be {@code null}
     * @param userId       the identifier of the requesting user; must not be {@code null}
     * @return the updated {@link PublicationSample} with the collection assignment applied
     * @throws EntityNotFoundException if no user or publication sample is found
     * @throws AccessDeniedException   if the user is not authorized
     * @throws NullPointerException    if {@code collectionId} or {@code pubSampleId} is {@code null}
     */
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

        publicationSample.addPublicationToCollection(collectionId);

        PublicationSample publicationAssignedToCollection = publicationSampleRepository.save(publicationSample);

        return publicationAssignedToCollection;
    }
}