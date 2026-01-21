package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

public class Library {

    private final List<Publication> publications = new ArrayList<>();

    User username;



    //Get publications in the Library
    public List<Publication> getAll(){
        return List.copyOf(publications); //read-only, so there are no external changes
    }

    //Add publication
    public void addPublication(Publication publication){
        if (!publications.contains(publication)){
            publications.add(publication);
        }
    }

    // US0018

    // Returns a defensive copy of publications
    public List<Publication> getPublications() {
        return new ArrayList<>(publications);
    }

    public boolean containsPublication(Publication publication) {
        return publications.contains(publication);
    }

    // Necessary methods to compare the objects
    // Allows Library objects to be compared by content instead of reference.
    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;
        if (!(obj instanceof Library)) return false;

        Library other = (Library) obj;

        return this.getPublications().equals(other.getPublications());
    }

    // Ensures consistency with equals().
    @Override
    public int hashCode() {
        return getPublications().hashCode();
    }

}
