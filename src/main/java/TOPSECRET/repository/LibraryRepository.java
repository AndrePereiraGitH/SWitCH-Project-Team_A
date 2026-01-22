package TOPSECRET.repository;

import TOPSECRET.domain.Library;
import TOPSECRET.domain.User;

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

    public boolean create(String username, Library library) {
        return libraries.put(username, library) == null;
    }
}
