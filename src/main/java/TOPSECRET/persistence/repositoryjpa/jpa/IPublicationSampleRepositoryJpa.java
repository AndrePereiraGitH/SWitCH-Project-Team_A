package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
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
    List<PublicationSampleDM> findAllByBdId(BibliographicalDataID bdId);

    /**
     * Retrieves all {@link PublicationSampleDM} entities whose bibliographical
     * data identifier is contained in the given list.
     *
     * <p>This method searches for publication samples associated with any of the
     * provided {@link BibliographicalDataID} values.</p>
     *
     * @param bdIds the list of bibliographical data identifiers used for filtering;
     *              must not be {@code null}
     * @return a list of matching {@link PublicationSampleDM} entities
     *         never {@code null}, but may be empty if no matches are found
     */
    List<PublicationSampleDM> findAllByBdIdIn(List<BibliographicalDataID> bdIds);

    /**
     * Retrieves all publication samples owned by the given user.
     *
     * @param ownerId the ID of the user whose publications are to be retrieved; must not be null
     * @return a list of {@link PublicationSampleDM} owned by the given user;
     *         returns an empty list if no publications are found
     */
    List<PublicationSampleDM> findAllByOwnerId(UserId ownerId);

}
