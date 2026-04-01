package TOPSECRET.domain.CollectionOfPublications;

import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;

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
    // instance method on purpose — so it can be injected and doubled in tests (static methods cannot be doubled)
    public CollectionOfPublications create(String name, GenreID genreId, UserId userId) {
        return new CollectionOfPublications(name, genreId, userId);
    }
}