package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.persistence.repositoryjpa.datamodel.PublicationSampleDM;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link PublicationSampleDM}.
 *
 * <p>Extends {@link JpaRepository} to inherit standard CRUD operations.
 * Custom queries specific to this aggregate are declared below.</p>
 */
public interface IPublicationSampleRepositoryJpa extends JpaRepository<PublicationSampleDM, PublicationSampleId> {

    /**
     * Returns all {@link PublicationSampleDM} associated with the given bibliographical data ID.
     *
     * @param bdId the bibliographical data ID to filter by; must not be {@code null}
     * @return a list of matching data models; never {@code null}, may be empty
     */
    List<PublicationSampleDM> findAllByBdId(BibliographicalDataId bdId);

    /**
     * Retrieves all publication samples owned by the given user.
     *
     * @param ownerId the ID of the user whose publications are to be retrieved; must not be null
     * @return a list of {@link PublicationSampleDM} owned by the given user;
     *         returns an empty list if no publications are found
     */
    List<PublicationSampleDM> findAllByOwnerId(UserId ownerId);

    /**
     * Retrieves all {@link PublicationSampleDM} entities associated with the given library
     * whose publication status is not in the provided exclusion list.
     *
     * @param libraryId the library identifier to filter by; must not be {@code null}
     * @param statuses  the list of {@link PublicationStatus} values to exclude; must not be {@code null}
     * @return a list of matching {@link PublicationSampleDM} entities; never {@code null}, may be empty
     */
    List<PublicationSampleDM> findAllByLibraryIdAndPublicationStatusNotIn(LibraryId libraryId, List<PublicationStatus> statuses);
}
