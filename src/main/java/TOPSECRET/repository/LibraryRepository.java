package TOPSECRET.repository;

import TOPSECRET.domain.Library;
import java.util.HashMap;
import java.util.Map;


public class LibraryRepository {

    //Guided by User ID (the username)
    private final Map<String, Library> libraries = new HashMap<>(); //Each user has 1 library and lookup should be fast

    public Library getLibraryOfUser(String _username){

        //Create Library if the user doesn't have one yet
        return libraries.computeIfAbsent(_username, name -> new Library());
    }
}
