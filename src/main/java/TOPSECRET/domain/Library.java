package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

public class Library {

    private List<Publication> myPublications;
    private final String libraryName;
    private final User user;

    //Constructor
    public Library(String libraryName, User user) {
        this(List.of(), libraryName, user);
    }

    public Library(List<Publication> myPublications, String libraryName, User user){

        if (libraryName == null || libraryName.isBlank()){
            throw new IllegalArgumentException("Library Name cannot be null or blank");
        }
        if (user == null){
            throw new IllegalArgumentException("User cannot be Null");
        }
        this.libraryName = libraryName;
        this.user = user;
        this.myPublications = myPublications;
    }

    //Get publications in the Library
    public List<Publication> getAllPublications(){
        return List.copyOf(myPublications); //read-only, so there are no external changes
    }

    public String getLibraryName(){
        return libraryName;
    }

    public User getUser(){
        return user;
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
