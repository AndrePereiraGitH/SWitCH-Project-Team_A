package TOPSECRET.controller;

import TOPSECRET.domain.DirectSale;
import TOPSECRET.domain.Publication;
import TOPSECRET.dto.DirectSaleDTO_US023;
import TOPSECRET.dto.DirectSaleMapper_US023;
import TOPSECRET.repository.DirectSaleRepository;
import TOPSECRET.repository.PublicationRepository;

import java.util.List;

public class ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController {

    // attributes
    private final PublicationRepository _publicationRepo;
    private final DirectSaleRepository _directSaleRepo;
    private final DirectSaleMapper_US023 _DirectSaleMapper_US023;

    // constructor
    public ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(PublicationRepository publicationRepo, DirectSaleRepository dsRepo, DirectSaleMapper_US023 dsMapper) {
        this._publicationRepo = publicationRepo;
        this._directSaleRepo = dsRepo;
        this._DirectSaleMapper_US023 = dsMapper;
    }

    // methods

    // Requests all publications to the PublicationRepository (step 2)
    public List<Publication> getAllPublications() {
        return _publicationRepo.getAllPublications();
    }


    // Provides a publication, user chooses a publication and retrieves a list of items on DS (step 11)
    public List<DirectSaleDTO_US023> getListOfItemsOnDirectSale(Publication publication) {

        if (publication == null) {
            throw new IllegalArgumentException("Publication cannot be null");
        }

        // Asks DS repo for domain objects
        List<DirectSale> listOfDirectSale = _directSaleRepo.getListOfItemsOnDirectSale(publication);

        // Converts domain objects to DTO, using DSMapper
        return _DirectSaleMapper_US023.toDTO(listOfDirectSale);
    }
}
