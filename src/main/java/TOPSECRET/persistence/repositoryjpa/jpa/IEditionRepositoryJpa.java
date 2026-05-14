package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.ValueObjects.EditionId;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.domain.ValueObjects.PublisherID;
import TOPSECRET.persistence.repositoryjpa.datamodel.EditionDM;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link EditionDM} entities.
 *
 * <p>
 * This interface provides CRUD operations and query capabilities for
 * {@link EditionDM} using Spring Data JPA.
 * </p>
 *
 * <p>
 * It extends {@link JpaRepository}, inheriting standard data access methods
 * such as save, findById, findAll, and delete.
 * </p>
 *
 * <p>
 * This repository operates on persistence data models and should not be used
 * directly by the domain layer. Conversion between {@link EditionDM} and
 * domain objects is handled by the assembler.
 * </p>
 */
public interface IEditionRepositoryJpa extends JpaRepository<EditionDM, EditionId> {

    /**
     * Finds all editions with the given publication type.
     *
     * @param publicationTypeId the publication type identifier; must not be null
     * @return a list of matching {@link EditionDM}; never null, may be empty
     */
    List<EditionDM> findByPublicationTypeId(PublicationTypeId publicationTypeId);

    /**
     * Finds all editions associated with the given publisher.
     *
     * @param publisherId the publisher identifier; must not be null
     * @return a list of matching {@link EditionDM}; never null, may be empty
     */
    List<EditionDM> findByPublisherId(PublisherID publisherId);

}
