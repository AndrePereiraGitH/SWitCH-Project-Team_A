package TOPSECRET.controller;

import TOPSECRET.domain.DirectSale;
import TOPSECRET.domain.Publication;
import TOPSECRET.dto.DirectSaleDTO_US023;
import TOPSECRET.dto.DirectSaleMapper_US023;
import TOPSECRET.dto.PublicationDTO_US023;
import TOPSECRET.dto.PublicationMapper_US023;
import TOPSECRET.repository.DirectSaleRepository;
import TOPSECRET.repository.PublicationRepository;

import java.util.List;

public class ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController {

    // attributes
    private final PublicationRepository _publicationRepo;
    private final PublicationMapper_US023 _publicationMapper_US023;
    private final DirectSaleRepository _directSaleRepo;
    private final DirectSaleMapper_US023 _DirectSaleMapper_US023;

    // constructor
    public ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(PublicationRepository publicationRepo, PublicationMapper_US023 _publicationMapper_US023, DirectSaleRepository dsRepo, DirectSaleMapper_US023 dsMapper) {
        this._publicationRepo = publicationRepo;
        this._publicationMapper_US023 = _publicationMapper_US023;
        this._directSaleRepo = dsRepo;
        this._DirectSaleMapper_US023 = dsMapper;
    }

    // methods

    // Requests all publications to the mapper
    public List<PublicationDTO_US023> getAllPublications() {
        List<Publication> publications = _publicationRepo.getAllPublications();
        return _publicationMapper_US023.getListOfPublicationsDTO(publications);
    }


//    // Provides a publication, user chooses a publication and retrieves a list of items on DS
//    public List<DirectSaleDTO_US023> getListOfItemsOnDirectSale(Publication publication) {
//
//        if (publication == null) {
//            throw new IllegalArgumentException("Publication cannot be null");
//        }
//
//        // Asks DS repo for domain objects
//        List<DirectSale> listOfDirectSale = _directSaleRepo.getListOfItemsOnDirectSale(publication);
//
//        // Converts domain objects to DTO, using DSMapper
//        return _DirectSaleMapper_US023.toDTO(listOfDirectSale);
//    }
}
