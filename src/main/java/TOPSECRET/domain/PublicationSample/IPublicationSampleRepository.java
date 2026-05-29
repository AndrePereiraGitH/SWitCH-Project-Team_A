package TOPSECRET.domain.PublicationSample;

import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.ValueObjects.*;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface responsible for managing {@link PublicationSample} aggregates.
 *
 * <p>Provides operations grouped into three categories:</p>
 * <ul>
 *   <li><b>Persistence</b> — save and existence checks.</li>
 *   <li><b>Lookup</b> — queries by identity or associated identifiers.</li>
 *   <li><b>Domain Operations</b> — queries that involve domain-specific filtering.</li>
 * </ul>
 */
public interface IPublicationSampleRepository {

    // =========================================================================
    // Persistence
    // =========================================================================

    /**
     * Persists the given {@link PublicationSample}.
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
     * @return a list of all publication samples; never {@code null}, may be empty
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
     * @return a list of {@link PublicationSample} assigned to a library; never {@code null}, may be empty
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
     * Retrieves all publication samples in the given library whose status is not in the excluded list.
     *
     * @param libraryId        the library identifier; must not be {@code null}
     * @param excludedStatuses the statuses to exclude; must not be {@code null}
     * @return a list of matching {@link PublicationSample}; never {@code null}, may be empty
     */
    List<PublicationSample> findByLibraryIdAndPublicationStatusNotIn(LibraryId libraryId,
                                                                     List<PublicationStatus> excludedStatuses);
}