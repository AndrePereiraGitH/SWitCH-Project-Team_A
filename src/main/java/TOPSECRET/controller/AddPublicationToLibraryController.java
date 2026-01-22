package TOPSECRET.controller;


import TOPSECRET.domain.Library;
import TOPSECRET.domain.Publication;
import TOPSECRET.repository.PublicationRepository;

import java.util.List;


public class AddPublicationToLibraryController {

    private final Library library;
    private final PublicationRepository publicationRepository;

    public AddPublicationToLibraryController(Library library, PublicationRepository publicationRepository){
        this.library = library;
        this.publicationRepository = publicationRepository;
    }

    //Call by the UI and response
    public List<Publication> getListOfOtherPublications(){
        //Get publications in user library
        List<Publication> listOfPublicationsInLibrary = library.getAllPublications();

        //Get publications not in library
        List<Publication> listOfDifferentPublications = publicationRepository.getListOfDifferentPublications(listOfPublicationsInLibrary);

        //Return to the UI
        return listOfDifferentPublications;
    }

    //Method for adding a publication to the Library
    public boolean addPublicationToLib(Publication publication){
        if (publication == null) throw new IllegalArgumentException("Publication cannot be null");
        return library.addPublication(publication); //The value is "true" if the publication is added
    }
}


