package TOPSECRET.domain.PublicationSample;


import TOPSECRET.domain.ValueObjects.*;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface responsible for managing {@link PublicationSample} aggregates.
 *
 * <p>Provides operations grouped into four categories:</p>
 * <ul>
 *   <li><b>Construction</b> — factory-style creation and persistence in a single step.</li>
 *   <li><b>Persistence</b> — low-level save and existence checks.</li>
 *   <li><b>Lookup</b> — queries by identity or associated identifiers.</li>
 *   <li><b>Domain Operations</b> — operations that involve domain logic beyond simple retrieval.</li>
 * </ul>
 */
public interface IPublicationSampleRepository {


    // =========================================================================
    // Persistence
    // =========================================================================

    /**
     * Saves the given {@link PublicationSample} to the repository.
     *
     * @param publicationSample the sample to save; must not be {@code null}
     * @return the saved {@link PublicationSample}
     */
    PublicationSample save(PublicationSample publicationSample);

    /**
     * Checks whether a {@link PublicationSample} with the given identity exists.
     *
     * @param id the identity to check; must not be {@code null}
     * @return {@code true} if a matching sample exists, {@code false} otherwise
     */
    boolean existsById(PublicationSampleId id);

    /**
     * Updates an existing {@link PublicationSample} in the persistence layer.
     *
     * <p>
     * This operation is intended for aggregates that already exist and whose
     * state has changed during the execution of a use case.
     * </p>
     *
     * <p>
     * The repository converts the domain object into its persistence
     * representation, delegates the update operation to the JPA repository,
     * and reconstructs the persisted aggregate before returning it.
     * </p>
     *
     * @param publicationSample the publication sample to update
     * @return the updated and persisted {@link PublicationSample}
     *
     * @throws NullPointerException if publicationSample is null
     */
    PublicationSample update(PublicationSample publicationSample);

    // =========================================================================
    // Lookup — by identity
    // =========================================================================

    /**
     * Finds a {@link PublicationSample} by its unique identifier.
     *
     * @param id the ID to search for; must not be {@code null}
     * @return an {@link Optional} containing the matching sample, or empty if none found
     */
    Optional<PublicationSample> findById(PublicationSampleId id);

    /**
     * Retrieves all {@link PublicationSample} instances stored in the repository.
     *
     * @return a list of all publication samples; never {@code null}, but may be empty
     */
    List<PublicationSample> findAll();

    // =========================================================================
    // Lookup — by associated identifiers
    // =========================================================================

    /**
     * Retrieves all publication sample IDs associated with the given bibliographical data ID.
     *
     * @param bdId the bibliographical data ID; must not be {@code null}
     * @return a list of matching {@link PublicationSampleId}; never {@code null}, may be empty
     */
    List<PublicationSampleId> findIdsByBibliographicalDataId(BibliographicalDataId bdId);

    /**
     * Retrieves all publication sample IDs whose bibliographical data ID is in the given list.
     *
     * @param bdIds the list of bibliographical data IDs; must not be {@code null}
     * @return a list of matching {@link PublicationSampleId}; never {@code null}, may be empty
     */
    List<PublicationSampleId> findIdsByBibliographicalDataIdIn(List<BibliographicalDataId> bdIds);

    /**
     * Retrieves all publication sample IDs whose bibliographical data ID is in the given list.
     *
     * @param bds the list of bibliographical data IDs; must not be {@code null}
     * @return a list of matching {@link PublicationSampleId}; never {@code null}, may be empty
     */
    List<PublicationSampleId> findPublicationSamplesIdsByBiblioDataIds(List<BibliographicalDataId> bds);

    /**
     * Retrieves all publication sample IDs associated with any of the given edition IDs.
     *
     * @param listOfEditionIds the list of edition IDs; must not be {@code null}
     * @return a list of matching {@link PublicationSampleId}; never {@code null}, may be empty
     */
    List<PublicationSampleId> findIdsByEditionIdIn(List<EditionId> listOfEditionIds);

    /**
     * Retrieves all publication samples that belong to the specified library.
     *
     * @param libraryId the library identifier; must not be {@code null}
     * @return a list of matching {@link PublicationSample}; never {@code null}, may be empty
     */
    List<PublicationSample> findByLibraryId(LibraryId libraryId);

    /**
     * Retrieves all publication samples that belong to the specified collection.
     *
     * @param collectionId the collection identifier; must not be {@code null}
     * @return a list of matching {@link PublicationSample}; never {@code null}, may be empty
     */
    List<PublicationSample> findByCollectionOfPublicationsId(CollectionOfPublicationsId collectionId);

    /**
     * Retrieves all publications owned by the given user that are assigned to a library.
     *
     * @param userId the user ID; must not be {@code null}
     * @return a list of {@link PublicationSample} in a library; never {@code null}, may be empty
     */
    List<PublicationSample> findByUserIdAndLibraryIdIsNotNull(UserId userId);

    /**
     * Retrieves all publications owned by the given user that are not assigned to any library.
     *
     * @param userId the user ID; must not be {@code null}
     * @return a list of {@link PublicationSample} without a library; never {@code null}, may be empty
     */
    List<PublicationSample> findByUserIdAndLibraryIdIsNull(UserId userId);

    // =========================================================================
    // Domain Operations
    // =========================================================================

    /**
     * Associates the given {@link PublicationSample} with a library and persists the change.
     *
     * @param publicationSample the sample to update; must not be {@code null}
     * @param libraryId         the library to associate; must not be {@code null}
     * @return {@code true} if the association was successfully persisted
     * @throws IllegalArgumentException if the sample is already assigned to a library
     */
    boolean addPublicationToLibrary(PublicationSample publicationSample, LibraryId libraryId);

    /**
     * Retrieves all publication samples in the given library that are eligible for sale.
     *
     * @param libraryId the library identifier; must not be {@code null}
     * @return a list of sale-eligible {@link PublicationSample}; never {@code null}, may be empty
     */
    List<PublicationSample> findAvailableForSaleByLibraryId(LibraryId libraryId);

    /**
     * Assigns the given collection to the specified publication samples and persists the change.
     *
     * @param collectionId         the collection identifier; must not be {@code null}
     * @param publicationSampleIds the list of sample IDs to assign; must not be {@code null}
     * @return {@code true} if the operation succeeded
     */
    boolean assignPublicationsToCollection(CollectionOfPublicationsId collectionId,
                                           List<PublicationSampleId> publicationSampleIds);

    /**
     * Retrieves publications owned by the given user that are not yet assigned to the given collection.
     *
     * <p>Computes the difference between all publications in the user's library
     * and those already assigned to the collection.</p>
     *
     * @param userId       the user identifier; must not be {@code null}
     * @param collectionId the collection identifier; must not be {@code null}
     * @return a list of available {@link PublicationSample}; never {@code null}, may be empty
     */
    List<PublicationSample> findAvailableForCollection(UserId userId,
                                                       CollectionOfPublicationsId collectionId);

}