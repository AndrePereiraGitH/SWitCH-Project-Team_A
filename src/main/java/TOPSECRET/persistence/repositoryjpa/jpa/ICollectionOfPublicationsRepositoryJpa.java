package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.persistence.repositoryjpa.datamodel.CollectionOfPublicationsDM;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link CollectionOfPublicationsDM}.
 *
 * <p>This interface provides persistence operations for
 * {@link CollectionOfPublicationsDM} entities using
 * {@link CollectionOfPublicationsId} as the primary key type.</p>
 *
 * <p>By extending {@link JpaRepository}, it inherits standard CRUD
 * operations such as save, findById, existsById, findAll, and delete,
 * without requiring manual implementation.</p>
 *
 * <p>This repository operates only at the persistence layer and should
 * be accessed through the JPA repository implementation
 * ({@code CollectionOfPublicationsRepositoryJpaImpl}), which acts as
 * the anti-corruption layer between persistence and the domain model.</p>
 */
public interface ICollectionOfPublicationsRepositoryJpa
        extends JpaRepository<CollectionOfPublicationsDM, CollectionOfPublicationsId> {
}