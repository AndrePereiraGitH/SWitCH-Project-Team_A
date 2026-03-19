package TOPSECRET.repository;

import TOPSECRET.domain.Library;
import TOPSECRET.domain.PublicationSample;
import TOPSECRET.domain.User;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.List;

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
     * @param username the username of the user whose library is to be retrieved;
     *                 must not be null or empty
     * @return the {@link Library} associated with the given username
     * @throws IllegalArgumentException  if {@code username} is null or empty
     * @throws InstanceNotFoundException if no library is found for the specified username
     */
    Library getLibraryOfUser(String username) throws InstanceNotFoundException;

    /**
     * Creates a new library for the specified user and adds it to the repository.
     *
     * @param libraryName the name of the library to create; must not be null or empty
     * @param owner       the user who will own the library; must not be null
     * @return the newly created {@link Library} instance
     * @throws IllegalArgumentException       if {@code libraryName} is null or empty,
     *                                        or if {@code owner} is null
     * @throws InstanceAlreadyExistsException if the user already has a library
     */
    Library createAndAdd(String libraryName, User owner) throws InstanceAlreadyExistsException;

    /**
     * Retrieves all publications from the library associated with the given username.
     *
     * @param username the username of the user whose publications are to be retrieved;
     *                 must not be null or empty
     * @return a list of {@link PublicationSample} objects; never null, but may be empty
     * @throws IllegalArgumentException  if {@code username} is null or empty
     * @throws InstanceNotFoundException if no library is found for the specified username
     */
    List<PublicationSample> getAllPublicationsFromUserLibrary(String username) throws InstanceNotFoundException;

    /**
     * Verifies whether the specified user already has a library.
     *
     * @param user the user to check for library ownership; must not be null
     * @return {@code true} if the user has a library, {@code false} otherwise
     * @throws IllegalArgumentException if {@code user} is null
     */
    boolean verifyIfUserHasALibrary(User user);
}
