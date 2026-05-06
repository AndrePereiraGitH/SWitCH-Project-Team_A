package TOPSECRET.domain.CollectionOfPublications;

import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.stereotype.Service;

/**
 * Factory for creating {@link CollectionOfPublications} domain objects.
 *
 * <p>This factory encapsulates the instantiation of the {@link CollectionOfPublications}
 * aggregate, centralising object creation and reconstruction logic.</p>
 *
 * <p>In the current architecture, this factory is primarily used by the
 * application layer (services) to create new domain objects, and by
 * repository implementations/assemblers to reconstruct existing ones
 * from persisted data.</p>
 *
 * <p>This separation improves testability and maintains persistence ignorance:
 * the Domain Layer does not depend on infrastructure concerns, and object
 * creation can be controlled or replaced with test doubles in unit tests.</p>
 *
 * <p>All validation is delegated to the {@link CollectionOfPublications}
 * constructors, ensuring that domain invariants are always enforced in a
 * single, authoritative location.</p>
 */
@Service
public class CollectionOfPublicationsFactory {

    /**
     * Creates a new {@link CollectionOfPublications}.
     *
     * <p>This method is used in the creation flow (e.g., by application services).
     * It delegates object instantiation to the domain constructor, which is
     * responsible for enforcing all invariants and applying default state
     * (e.g., collections start as private).</p>
     *
     * <p>This method is intentionally non-static to allow dependency injection
     * and replacement with test doubles in unit tests.</p>
     *
     * @param name    the name chosen by the user; must not be null or blank
     * @param genreId the genre associated with the collection; must not be null
     * @param userId  the owner of the collection; must not be null
     * @return a new {@link CollectionOfPublications} instance
     * @throws IllegalArgumentException if any argument is invalid
     */
    public CollectionOfPublications create
    (String name,
     GenreId genreId,
     UserId userId) {

        return new CollectionOfPublications(
                name, genreId, userId);
    }

    /**
     * Recreates an existing {@link CollectionOfPublications} from persisted data.
     *
     * <p>This method is used during reconstitution (typically by repository
     * implementations or assemblers) when rebuilding a domain object from
     * the persistence layer. It preserves both identity and state, including
     * the privacy flag.</p>
     *
     * <p>This ensures that the reconstructed aggregate faithfully reflects
     * the stored data while still enforcing domain invariants through the
     * constructor.</p>
     *
     * @param collectionOfPublicationsId the existing identity; must not be null
     * @param name                       the collection name; must not be null or blank
     * @param genreId                    the genre; must not be null
     * @param userId                     the owner; must not be null
     * @param isPrivate                  the persisted privacy state
     * @return the reconstructed {@link CollectionOfPublications}
     * @throws IllegalArgumentException if any argument is invalid
     */
    public CollectionOfPublications create
    (CollectionOfPublicationsId collectionOfPublicationsId,
     String name,
     GenreId genreId,
     UserId userId,
     boolean isPrivate) {

        return new CollectionOfPublications(
                collectionOfPublicationsId, name, genreId, userId, isPrivate);
    }
}