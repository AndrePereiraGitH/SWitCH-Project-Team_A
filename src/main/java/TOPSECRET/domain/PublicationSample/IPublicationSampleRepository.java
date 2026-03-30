package TOPSECRET.domain.PublicationSample;

import TOPSECRET.domain.*;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.EditionID;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.PublicationSampleDTO_US016;
import TOPSECRET.dto.PublicationSampleDTO_US018;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;

/**
 * Repository interface responsible for managing {@link PublicationSample} objects.
 * Provides operations to retrieve all publications, create and add new ones,
 * add existing publication samples, and verify if a publication already exists
 * in the repository.
 * <p>
 */
public interface IPublicationSampleRepository {

    /**
     * Retrieves all {@link PublicationSample} instances stored in the repository.
     *
     * @return a {@link List} of all publication samples; never {@code null},
     *         but may be empty if none exist
     */
    List<PublicationSample> getAllPublications();

    /**
     * Creates a new {@link PublicationSample} from the provided attributes,
     * persists it in the repository, and returns the created instance.
     *
     * @param bibliographicalDataID the ID of the bibliographical metadata of the publication; must not be null
     * @param userID                the ID of the user who owns this copy; must not be null
     * @param status                the initial publication status; must not be null
     * @param editionID             the ID of the edition of the publication; must not be null
     * @param condition             the physical condition of this copy; must not be null
     * @return the newly created and persisted {@link PublicationSample}
     * @throws InstanceAlreadyExistsException if an equivalent publication sample
     *                                        already exists in the repository
     */
    PublicationSample createAndAdd(BibliographicalDataID bibliographicalDataID, UserId userID, PublicationStatus status,
                                   EditionID editionID, Condition condition) throws InstanceAlreadyExistsException;

    /**
     * Adds an already constructed {@link PublicationSample} to the repository.
     *
     * @param publicationSample the publication sample to persist; must not be null
     */
    void add(PublicationSample publicationSample);

    /**
     * Checks whether the given {@link PublicationSample} is already present
     * in the repository, based on its identity.
     *
     * @param publicationSample the publication sample to look up; must not be null
     * @return {@code true} if the publication sample exists in the repository;
     *         {@code false} otherwise
     */
    boolean isExistent(PublicationSample publicationSample);

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
    List<PublicationSample> getListOfDifferentPublications(List<PublicationSample> listOfPublicationsInLibrary);

    /**
     * Retrieves the {@link PublicationSample} instances that correspond to
     * the given list of {@link PublicationSampleDTO_US016} transfer objects.
     *
     * @param pubsDto the list of DTOs identifying the desired publication samples;
     *                must not be null
     * @return a {@link List} of matching {@link PublicationSample} instances;
     *         never {@code null}, but may be empty if no matches are found
     */
  //  List<PublicationSample> getPublicationsByDTO(List<PublicationSampleDTO_US016> pubsDto);

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
   // PublicationSample PublicationSampleDtoToDomain(Library userLibrary, PublicationSampleDTO_US018 dto);

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
    List<PublicationSample> findItemsByListingAndPublication(List<Listing> listings, BibliographicalData publication);
}