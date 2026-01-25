package TOPSECRET.controller;


import TOPSECRET.domain.Library;
import TOPSECRET.domain.Publication;
import TOPSECRET.repository.PublicationRepository;

import java.util.List;


public class AddPublicationToLibraryController {

    private final Library library; //Vai representar a library do User, este controller não vai criar a library
    private final PublicationRepository publicationRepository; //Vai ter o repositório com todas as publicações do sistema

    //Construtor
    public AddPublicationToLibraryController(Library library, PublicationRepository publicationRepository){ //Recebe tudo o que precisa
        this.library = library;
        this.publicationRepository = publicationRepository; //Associa as dependências ao controller (low coupling - controller não cria nada)
    }

    //Call by the UI and response
    public List<Publication> getListOfOtherPublications(){
        //Get publications in user library
        List<Publication> listOfPublicationsInLibrary = library.getAllPublications(); //Get all Publications

        //Get publications not in library
        List<Publication> listOfDifferentPublications = publicationRepository.getListOfDifferentPublications(listOfPublicationsInLibrary);
        //Vai buscar o méto.do que está no PublicationRepository

        //Return to the UI
        return listOfDifferentPublications;
    }

    //Method for adding a publication to the Library
    public boolean addPublicationToLib(Publication publication){
        if (publication == null)
            throw new IllegalArgumentException("Publication cannot be null");
        return library.addPublication(publication); //The value is "true" if the publication is added
    } //Está a perguntar à Library se conseguiu adicionar e devolve a mesma resposta
}


