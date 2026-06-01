package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.GenreId;
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


    /**
     * Retrieves all {@link CollectionOfPublicationsDM} entities belonging to the given owner.
     *
     * @param ownerId the identifier of the owner whose collections are to be retrieved;
     *                must not be {@code null}
     * @return a list of {@link CollectionOfPublicationsDM} owned by the given user;
     * may be empty if the user has no collections
     */
    List<CollectionOfPublicationsDM> findAllByOwnerId(UserId ownerId);


    /**
     * Retrieves all public {@link CollectionOfPublicationsDM} entities associated with
     * the given genre.
     *
     * @param genreId the identifier of the genre to filter by; must not be {@code null}
     * @return a list of public {@link CollectionOfPublicationsDM} belonging to the given genre;
     * may be empty if none exist
     */
    List<CollectionOfPublicationsDM> findByGenreIdAndIsPrivateFalse(GenreId genreId);
}