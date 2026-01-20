package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

public class Library {

    private final List<Publication> publications = new ArrayList<>();

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
}
