package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
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
 * The method {@code getAllPublications} returns a list of all
 * {@link PublicationSample} stored.
 * <p>
 * The method {@code createAndAdd} creates a new {@link PublicationSample}
 * using the provided {@link BibliographicalData},
 * {@link User}, {@link PublicationStatus},
 * {@link Edition}, and {@link Condition},
 * and adds it to the repository, returning the created object.
 * <p>
 * The method {@code add} allows adding an existing
 * {@link PublicationSample} to the repository.
 * <p>
 * The method {@code isExistent} checks whether a given
 * {@link PublicationSample} is already stored,
 * returning {@code true} if it exists and {@code false} otherwise.
 * </p>
 */

public interface IPublicationSampleRepository {

    List<PublicationSample> getAllPublications();
    PublicationSample createAndAdd(BibliographicalData bibliographicalData, User user, PublicationStatus status, Edition edition, Condition condition) throws InstanceAlreadyExistsException;
    void add(PublicationSample publicationSample);
    boolean isExistent(PublicationSample publicationSample);
    List<PublicationSample> getListOfDifferentPublications(List<PublicationSample> listOfPublicationsInLibrary);
    List<PublicationSample> getPublicationsByDTO(List<PublicationSampleDTO_US016> pubsDto);
    List<PublicationSample> findItemsByListingAndPublisher(List<Listing> listings, Publisher publisher);
    PublicationSample PublicationSampleDtoToDomain(Library userLibrary, PublicationSampleDTO_US018 dto);
    List<PublicationSample> findItemsByListingAndPublication(List<Listing> listings, BibliographicalData publication);

}
