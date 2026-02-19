package TOPSECRET.repository;

import TOPSECRET.domain.Library;
import TOPSECRET.domain.User;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class LibraryRepository {

    //Guided by User ID (the username)
    private final Map<String, Library> libraries; //Each user has 1 library and lookup should be fast

    public LibraryRepository(Map<String, Library> libraries) {
        this.libraries = new HashMap<>(libraries);
    }

    public Library getLibraryOfUser(String _username) throws InstanceNotFoundException {

        Library library = libraries.get(_username);

        if (library == null) {
            throw new InstanceNotFoundException("This user does not have a library");
        }
        return library;
    }

    /**
     * Creates a new library for the specified user and adds it to the collection.
     * <p>
     * This method validates that the library name is not empty and ensures the user
     * does not already have an existing library before creating a new one.
     * </p>
     *
     * @param libraryName the name of the library to create;
     * @param owner the user who will own the library; must not already have a library
     * @return the newly created {@link Library} instance
     * @throws IllegalArgumentException if the library name is empty
     * @throws IllegalArgumentException if the owner is null
     * @throws InstanceAlreadyExistsException if the user already has a library
     */

    public Library createAndAdd(String libraryName, User owner) throws InstanceAlreadyExistsException {
        if(libraryName.isEmpty()){
            throw new IllegalArgumentException("Library name must have one or more characters");
        }

        if(owner==null){
            throw new IllegalArgumentException("Owner can not be null");
        }

        if(verifyIfUserHasALibrary(owner)){
            throw new InstanceAlreadyExistsException("The user already has a library");
        }

        Library newLibrary = new Library(libraryName,owner);
        libraries.put(owner.getUserName(), newLibrary);
        return newLibrary;
    }

    /**
     * Verifies whether the specified user already has a library.
     * <p>
     * This method iterates through all existing libraries to check if any
     * library is owned by the given user.
     * </p>
     *
     * @param user the user to check for library ownership
     * @return {@code true} if the user has a library, {@code false} otherwise
     */

    public boolean verifyIfUserHasALibrary(User user){
        for(Library library: libraries.values()){
            if(library.getUser().equals(user)){
                return true;
            }
        }

        return false;
    }

}
