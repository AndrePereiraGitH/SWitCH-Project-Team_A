package TOPSECRET.controller;


import TOPSECRET.domain.Library;
import TOPSECRET.domain.PublicationSample;
import TOPSECRET.repository.PublicationSampleRepository;

import java.util.List;


public class US011AddPublicationToLibraryController {

    private final Library library; //Vai representar a library do User, este controller não vai criar a library
    private final PublicationSampleRepository publicationSampleRepository; //Vai ter o repositório com todas as publicações do sistema

    //Construtor
    public US011AddPublicationToLibraryController(Library library, PublicationSampleRepository publicationSampleRepository){ //Recebe tudo o que precisa
        this.library = library;
        this.publicationSampleRepository = publicationSampleRepository; //Associa as dependências ao controller (low coupling - controller não cria nada)
    }

    // Call by the UI and response
    public List<PublicationSample> getListOfOtherPublications(){
        //Get publications in user library
        List<PublicationSample> listOfPublicationsInLibrary = library.getAllPublications(); //Get all Publications

        //Get publications not in library
        List<PublicationSample> listOfDifferentPublications = publicationSampleRepository.getListOfDifferentPublications(listOfPublicationsInLibrary);
        //Vai buscar o méto.do que está no PublicationRepository

        //Return to the UI
        return listOfDifferentPublications;
    }

    //Method for adding a publication to the Library
    public boolean addPublicationToLib(PublicationSample publicationSample){
        if (publicationSample == null)
            throw new IllegalArgumentException("Publication cannot be null");
        library.addPublication(publicationSample); //The value is "true" if the publication is added
        return true;
    } //Está a perguntar à Library se conseguiu adicionar e devolve a mesma resposta
}


