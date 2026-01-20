/*package TOPSECRET.controller;


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
    public List<Publication> getListOfPublications(){
        //Get publications in user library
        List<Publication> publicationsInLibrary = library.getAll();

        //Get publications not in library
        List<Publication> listOfDifferentPublications =
                publicationRepository.getListOfDifferentPublications(publicationsInLibrary);

        //Return to the UI
        return listOfDifferentPublications;
    }
}

 */
