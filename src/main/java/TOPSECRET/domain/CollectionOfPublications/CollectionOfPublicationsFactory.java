package TOPSECRET.domain.CollectionOfPublications;

import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsID;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.stereotype.Service;

/**
 * Factory for creating {@link CollectionOfPublications} objects — US017.
 *
 * <p>The Factory pattern is used here so that the repository does not call
 * {@code new CollectionOfPublications(...)} directly. Instead, it asks this factory to do it.
 *
 * <p>This separation is important for testing: in unit tests, the factory can be replaced
 * with a double, which means the test only checks the repository logic, not the domain class.
 *
 * <p>Validation (null checks, blank name) is not done here — it is handled by the
 * {@code CollectionOfPublications} constructor, which is the right place for those rules.
 */

@Service
public class CollectionOfPublicationsFactory {

    /**
     * Creates a new {@link CollectionOfPublications} — US017.
     *
     * <p>The factory just passes the data to the constructor and returns the result.
     * The constructor is responsible for validating the data and setting the default values
     * (private flag = true, empty publications list).
     *
     * <p>This method is an instance method (not static) so the factory can be injected
     * into the repository via the constructor and replaced with a double in unit tests.
     * A static method cannot be doubled, so making it an instance method is a deliberate choice.
     *
     * @param name    name chosen by the user for the collection; must not be null or blank
     * @param genreId genre chosen by the user; must not be null
     * @param userId  the user who owns the collection; must not be null
     * @return a new private {@code CollectionOfPublications} with no publications
     * @throws IllegalArgumentException if any argument is null or if name is blank
     */
    public CollectionOfPublications create
    (String name,
     GenreID genreId,
     UserId userId) {

        return new CollectionOfPublications(
                name, genreId, userId);
    }

    /**
     * Recreates an existing {@link CollectionOfPublications} from persisted data.
     *
     * <p>This method is used when rebuilding a domain object from the database,
     * where the identity already exists and must be preserved. Unlike the standard
     * creation method for new collections, this version receives an existing
     * {@link CollectionOfPublicationsID} and restores the aggregate with that identity.
     *
     * <p>This is typically used by repository implementations and assemblers when
     * converting persistence data models back into domain objects.
     *
     * <p>Validation is still delegated to the
     * {@link CollectionOfPublications} constructor to ensure all domain
     * invariants remain enforced.
     *
     * @param collectionOfPublicationsId the existing identity of the collection;
     *                                   must not be null
     * @param name                       the name of the collection; must not be null or blank
     * @param genreId                    the genre associated with the collection; must not be null
     * @param userId                     the owner of the collection; must not be null
     * @return the reconstructed {@link CollectionOfPublications} domain object
     * @throws IllegalArgumentException if any argument is invalid
     */
    public CollectionOfPublications create
    (CollectionOfPublicationsID collectionOfPublicationsId,
     String name,
     GenreID genreId,
     UserId userId) {

        return new CollectionOfPublications(
                collectionOfPublicationsId, name, genreId, userId);
    }
}