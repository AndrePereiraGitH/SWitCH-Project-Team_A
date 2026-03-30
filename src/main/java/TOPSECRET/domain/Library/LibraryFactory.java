package TOPSECRET.domain.Library;

import TOPSECRET.domain.ValueObjects.UserId;

/**
 * Factory responsible for creating {@link Library} instances.
 * <p>
 * Centralizes the instantiation logic for {@link Library} objects,
 * ensuring consistent creation throughout the application.
 * </p>
 */
public class LibraryFactory {

    /**
     * Creates and returns a new {@link Library} instance with the given name and owner.
     *
     * @param libraryName the name to assign to the new library; must not be {@code null} or empty
     * @param userId       the {@link UserId} who owns the library; must not be {@code null}
     * @return a new {@link Library} instance initialized with the provided name and owner
     */
    public Library create(String libraryName, UserId userId) {

        return new Library(libraryName, userId);
    }
}