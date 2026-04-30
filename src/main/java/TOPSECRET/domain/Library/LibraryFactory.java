package TOPSECRET.domain.Library;

import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.stereotype.Component;

/**
 * Factory responsible for creating {@link Library} instances.
 * <p>
 * Centralizes the instantiation logic for {@link Library} objects,
 * ensuring consistent creation throughout the application.
 * </p>
 */
@Component
public class LibraryFactory {

    /**
     * Creates and returns a new {@link Library} instance with the given name and owner.
     *
     * @param libraryName the name of the library; must not be null or blank
     * @param userId      the owner of the library; must not be null
     * @return a new {@link Library} instance initialized with the provided name and owner
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Library create(String libraryName, UserId userId) {
        if (libraryName == null || libraryName.isBlank()) {
            throw new IllegalArgumentException("Library name cannot be null or blank");
        }
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }

        return new Library(libraryName, userId);
    }

    /**
     * Reconstructs an existing {@link Library} using a known {@link LibraryId}.
     *
     * <p>Typically used when loading a library from persistence.</p>
     *
     * @param libraryId   the existing library ID; must not be null
     * @param libraryName the name of the library; must not be null or blank
     * @param userId      the owner of the library; must not be null
     * @return a reconstructed {@link Library} instance
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Library create(LibraryId libraryId, String libraryName, UserId userId) {
        if (libraryId == null) {
            throw new IllegalArgumentException("LibraryId cannot be null");
        }
        if (libraryName == null || libraryName.isBlank()) {
            throw new IllegalArgumentException("Library name cannot be null or blank");
        }
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }

        return new Library(libraryId, libraryName, userId);
    }
}