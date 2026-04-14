package TOPSECRET.domain.PublicationSample;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.EditionID;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.dto.PublicationSampleDTO_US018;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface responsible for managing {@link PublicationSample} objects.
 * Provides operations to retrieve all publications, create and add new ones,
 * add existing publication samples, and verify if a publication already exists
 * in the repository.
 * <p>
 */
public interface    IPublicationSampleRepository {

    // -------------------------------------------------------------------------
    // Construction
    // -------------------------------------------------------------------------

    /**
     * Creates a new {@link PublicationSample} from the provided attributes,
     * persists it in the repository, and returns the created instance.
     *
     * @param bibliographicalDataId the ID of the bibliographical metadata of the publication; must not be null
     * @param userId                the ID of the user who owns this copy; must not be null
     * @param status                the initial publication status; must not be null
     * @param editionId             the ID of the edition of the publication; must not be null
     * @param condition             the physical condition of this copy; must not be null
     * @return the newly created and persisted {@link PublicationSample}
     * @throws InstanceAlreadyExistsException if an equivalent publication sample
     *                                        already exists in the repository
     */
    PublicationSample createAndAdd(BibliographicalDataID bibliographicalDataId, UserId userId, PublicationStatus status,
                                   EditionID editionId, Condition condition) throws InstanceAlreadyExistsException;

    // -------------------------------------------------------------------------
    // Persistence
    // -------------------------------------------------------------------------

    /**
     * Saves the given {@link PublicationSample} to the repository.
     *
     * @param publicationSample the publication sample to save; must not be {@code null}
     * @return the saved {@link PublicationSample}
     * @throws IllegalArgumentException if {@code publicationSample} is {@code null} or already exists in the repository
     */
    PublicationSample save(PublicationSample publicationSample);

    /**
     * Checks whether the given {@link PublicationSample} exists in the repository.
     *
     * @param publicationSample the sample to check; must not be {@code null}
     * @return {@code true} if the sample exists, {@code false} otherwise
     * @throws IllegalArgumentException if {@code publicationSample} is {@code null}
     */
    boolean isExistent(PublicationSample publicationSample);

    // -------------------------------------------------------------------------
    // Identity Lookup
    // -------------------------------------------------------------------------

    /**
     * Finds a {@link PublicationSample} by its unique identifier.
     *
     * @param id the ID to search for; must not be {@code null}
     * @return an {@link Optional} containing the matching sample, or empty if none found
     * @throws IllegalArgumentException if {@code auctionId} is {@code null}
     */
    Optional<PublicationSample> findPublicationSampleById(PublicationSampleId id);

    /**
     * Retrieves all publication sample IDs associated with a given bibliographical data ID.
     *
     * @param bdId the bibliographical data ID to search for; must not be {@code null}
     * @return a {@link List} of {@link PublicationSampleId} associated with the given ID;
     *         returns an empty list if no matches are found
     * @throws NullPointerException if {@code bdId} is {@code null}
     */
    List<PublicationSampleId> findPubSampleIdsByBibliographicalDataId(BibliographicalDataID bdId);

    // -------------------------------------------------------------------------
    // Specific Queries
    // -------------------------------------------------------------------------

    /**
     * Retrieves all {@link PublicationSample} instances stored in the repository.
     *
     * @return a {@link List} of all publication samples; never {@code null},
     *         but may be empty if none exist
     */
    List<PublicationSample> getAllPublications();

    List<PublicationSampleId> findAllItemsIdByBibliographicalDataId(List<BibliographicalDataID> bdIds);

    /**
     * Retrieves all publications in the system that are owned by the given user
     * and have been added to a library.
     *
     * @param userId the ID of the user whose library publications are to be retrieved;
     *               must not be {@code null}
     * @return a list of {@link PublicationSample} objects owned by the user that are
     *         currently in a library; never {@code null}, may be empty
     * @throws NullPointerException if {@code userId} is {@code null}
     */
    List<PublicationSample> getAllPublicationsInUserLibrary(UserId userId);

    /**
     * Retrieves the identifiers of publication samples whose bibliographical data ID
     * is contained in the given list.
     *
     * @param bds the list of bibliographical data identifiers to search for
     * @return a list of publication sample identifiers associated with the given
     *         bibliographical data identifiers; returns an empty list if no matches are found
     * @throws IllegalArgumentException if {@code bds} is {@code null}
     */
    List<PublicationSampleId> findPublicationSamplesIdsByBiblioDataIds(List<BibliographicalDataID> bds);

    /**
     * Retrieves all publication samples that belong to the specified library.
     *
     * @param libraryID the identifier of the library whose publication samples are to be retrieved
     * @return a list containing all publication samples associated with the given library;
     * returns an empty list if no publication samples are found
     */
    List<PublicationSample> getPublicationsByLibraryID(LibraryId libraryID);

    /**
     * Returns the subset of {@link PublicationSample} instances from the repository
     * that are not present in the provided list.
     * <p>
     * Useful for determining which publications are available outside of a
     * specific library's collection.
     * </p>
     *
     * @param listOfPublicationsInLibrary the list of publications already held in a library;
     *                                    must not be null
     * @return a {@link List} of publication samples not present in the given list;
     *         never {@code null}, but may be empty
     */
//    List<PublicationSample> getListOfDifferentPublications(List<PublicationSample> listOfPublicationsInLibrary);


    /**
     * Finds all {@link PublicationSample} instances associated with any of the
     * provided {@link Listing} objects and belonging to the specified {@link Publisher}.
     *
     * @param listings  the list of listings to filter by; must not be null
     * @param publisher the publisher to match against; must not be null
     * @return a {@link List} of matching {@link PublicationSample} instances;
     *         never {@code null}, but may be empty if no matches are found
     */
//    List<PublicationSample> findItemsByListingAndPublisher(List<Listing> listings, Publisher publisher);

    /**
     * Converts a {@link PublicationSampleDTO_US018} transfer object into a domain
     * {@link PublicationSample}, resolved in the context of the given {@link Library}.
     *
     * @param userLibrary the library context used to resolve the publication sample;
     *                    must not be null
     * @param dto         the DTO carrying the data to convert; must not be null
     * @return the corresponding {@link PublicationSample} domain object
     */
//    PublicationSample PublicationSampleDtoToDomain(Library userLibrary, PublicationSampleDTO_US018 dto);

    /**
     * Finds all {@link PublicationSample} instances associated with any of the
     * provided {@link Listing} objects and matching the specified
     * {@link BibliographicalData}.
     *
     * @param listings    the list of listings to filter by; must not be null
     * @param publication the bibliographical data identifying the target publication;
     *                    must not be null
     * @return a {@link List} of matching {@link PublicationSample} instances;
     *         never {@code null}, but may be empty if no matches are found
     */
//    List<PublicationSample> findItemsByListingAndPublication(List<Listing> listings, BibliographicalData publication);
}