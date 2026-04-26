package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
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
}
