package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.ValueObjects.PublisherID;
import TOPSECRET.persistence.repositoryjpa.datamodel.PublisherDM;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link PublisherDM}.
 *
 * <p>Extends {@link JpaRepository} to inherit standard CRUD operations.
 * The Spring framework automatically generates the implementation at runtime.</p>
 */
public interface IPublisherRepositoryJpa extends JpaRepository<PublisherDM, PublisherID> {

}