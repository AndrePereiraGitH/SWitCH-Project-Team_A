package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

public class Library {

    private final List<Publication> myPublications;
    private String libraryName;
    private User username;

    //Constructor
    public Library(List<Publication> myPublications, String libraryName, User username){

        if (libraryName == null || libraryName.isBlank()){
            throw new IllegalArgumentException("Library Name cannot be null or blank");
        }
        if (username == null){
            throw new IllegalArgumentException("User cannot be Null");
        }
        this.libraryName = libraryName;
        this.username = username;
        this.myPublications = new ArrayList<>();
    }

    //Get publications in the Library
    public List<Publication> getAllPublications(){
        return List.copyOf(myPublications); //read-only, so there are no external changes
    }

    //Add publication
    public boolean addPublication(Publication publication){
        if (!myPublications.contains(publication)){
            myPublications.add(publication);
            return true; //Publication was added
        }
        return false; //Publication was not added (already exists)
    }

    // US0018

    public boolean containsPublication(Publication publication) {
        return myPublications.contains(publication);
    }

    // Necessary methods to co  mpare the objects
    // Allows Library objects to be compared by content instead of reference.
    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;
        if (!(obj instanceof Library)) return false;

        Library other = (Library) obj;

        return this.getAllPublications().equals(other.getAllPublications());
    }

    // Ensures consistency with equals().
    @Override
    public int hashCode() {
        return getAllPublications().hashCode();
    }

}
