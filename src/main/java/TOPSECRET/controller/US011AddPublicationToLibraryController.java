package TOPSECRET.controller;


import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.repository.PublicationSampleRepositoryMem;

import java.util.List;


public class US011AddPublicationToLibraryController {

    private final Library library; //Vai representar a library do User, este controller não vai criar a library
    private final PublicationSampleRepositoryMem publicationSampleRepositoryMem; //Vai ter o repositório com todas as publicações do sistema

    //Construtor
    public US011AddPublicationToLibraryController(Library library, PublicationSampleRepositoryMem publicationSampleRepositoryMem){ //Recebe tudo o que precisa
        this.library = library;
        this.publicationSampleRepositoryMem = publicationSampleRepositoryMem; //Associa as dependências ao controller (low coupling - controller não cria nada)
    }

//    public List<PublicationSample> getListOfOtherPublications(){
//        //Get publications in user library
//        List<PublicationSample> listOfPublicationsInLibrary = publicationSampleRepositoryMem.getAllPublications(); //Get all Publications
//
//        //Get publications not in library
//        List<PublicationSample> listOfDifferentPublications = publicationSampleRepositoryMem.getListOfDifferentPublications(listOfPublicationsInLibrary);
//        //Vai buscar o méto.do que está no PublicationRepository
//
//        //Return to the UI
//        return listOfDifferentPublications;
//    }

//    public boolean addPublicationToLib(PublicationSample publicationSample){
//        if (publicationSample == null)
//            throw new IllegalArgumentException("Publication cannot be null");
//        publicationSampleRepositoryMem.add(publicationSample); //The value is "true" if the publication is added
//        return true;
//    } //Está a perguntar à Library se conseguiu adicionar e devolve a mesma resposta
}


