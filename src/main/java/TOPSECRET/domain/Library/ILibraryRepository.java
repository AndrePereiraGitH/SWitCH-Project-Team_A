package TOPSECRET.domain.Library;

import TOPSECRET.domain.ValueObjects.UserId;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;

/**
 * Contract for managing {@link Library} objects.
 * <p>
 * Defines operations for creating, retrieving, and verifying user libraries.
 * </p>
 */
public interface ILibraryRepository {

    /**
     * Retrieves the library associated with the specified username.
     *
     * @param userId the username of the user whose library is to be retrieved;
     *                 must not be null or empty
     * @return the {@link Library} associated with the given username
     * @throws IllegalArgumentException  if {@code username} is null or empty
     * @throws InstanceNotFoundException if no library is found for the specified username
     */
    Library getLibraryByUserId(UserId userId) throws InstanceNotFoundException;

    /**
     * Adds a {@link Library}   to the repository.
     *
     * @throws InstanceAlreadyExistsException if the Library already exists.
     */
    Library save(Library library) throws InstanceAlreadyExistsException;

    /**
     * Verifies whether the specified user already has a library.
     *
     * @param userId the user to check for library ownership; must not be null
     * @return {@code true} if the user has a library, {@code false} otherwise
     * @throws IllegalArgumentException if {@code user} is null
     */
    boolean verifyIfUserHasALibrary(UserId userId);
}
