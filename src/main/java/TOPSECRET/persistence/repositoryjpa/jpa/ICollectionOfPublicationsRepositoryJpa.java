package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.persistence.repositoryjpa.datamodel.CollectionOfPublicationsDM;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

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
    CollectionOfPublicationsDM update(CollectionOfPublicationsDM collectionDMtOUpdate);
    List<CollectionOfPublicationsDM> findAllByOwnerId(UserId ownerId);


    /**
     * Retrieves all collections owned by a specific user.
     *
     * <p>
     * This query operation searches the persistence layer for every
     * {@link CollectionOfPublicationsDM} associated with the given owner.
     * </p>
     *
     * @param ownerId identifier of the collection owner
     * @return list of collections belonging to the specified user;
     * returns an empty list if none are found
     */
    List<CollectionOfPublicationsDM> findByOwnerId(UserId ownerId);
}