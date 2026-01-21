package TOPSECRET.controller;

import TOPSECRET.domain.DirectSale;
import TOPSECRET.domain.Publication;
import TOPSECRET.dto.DirectSaleDTO;
import TOPSECRET.dto.DirectSaleMapper;
import TOPSECRET.repository.DirectSaleRepository;
import TOPSECRET.repository.PublicationRepository;

import java.util.List;

public class ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController {

    // attributes
    private final PublicationRepository _publicationRepo;
    private final DirectSaleRepository _directSaleRepo;
    private final DirectSaleMapper _directSaleMapper;

    // constructor
    public ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(PublicationRepository publicationRepo, DirectSaleRepository dsRepo, DirectSaleMapper dsMapper) {
        this._publicationRepo = publicationRepo;
        this._directSaleRepo = dsRepo;
        this._directSaleMapper = dsMapper;
    }

    // methods

    // "UI" requests all publications to the PublicationRepository (step 2)
    public List<Publication> getAllPublications() {
        return _publicationRepo.getAllPublications();
    }


    // "UI" provides a publication, controller retrieves DS (step 8)
    public List<DirectSaleDTO> getListOfItemsOnDirectSale(Publication publication) {

        // Ask DS repo for domain objects (step 9)
        List<DirectSale> listOfDirectSale = _directSaleRepo.getListOfItemsOnDirectSale(publication);

        // Convert domain → DTO (step 13)
        return _directSaleMapper.toDTO(listOfDirectSale);
    }
}
