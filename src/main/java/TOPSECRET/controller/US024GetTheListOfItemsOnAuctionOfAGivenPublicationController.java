package TOPSECRET.controller;


import TOPSECRET.domain.BibliographicalData;
import TOPSECRET.domain.PublicationSample;
import TOPSECRET.dto.AuctionItemDTO_US024;
import TOPSECRET.dto.BibliographicalDataDTO_US024;
import TOPSECRET.repository.AuctionRepository;
import TOPSECRET.repository.BibliographicalDataRepository;

import java.util.List;

public class US024GetTheListOfItemsOnAuctionOfAGivenPublicationController {

    private BibliographicalDataRepository _bdRepo;
    private  AuctionRepository _aucRepo;

    public US024GetTheListOfItemsOnAuctionOfAGivenPublicationController(BibliographicalDataRepository _bdRepo, AuctionRepository _aucRepo) {
        this._bdRepo = _bdRepo;
        this._aucRepo = _aucRepo;
    }

    public List<BibliographicalDataDTO_US024> getAllPublicationBD(){

        List<BibliographicalData> listOfAllPublicationBD = _bdRepo.getAllPublicationsBD();

        List<BibliographicalDataDTO_US024> listOfAllBiblioDataDTO = BibliographicalDataDTO_US024.toDTOS(listOfAllPublicationBD);

        return listOfAllBiblioDataDTO;
    }


    public List<AuctionItemDTO_US024> getListOfItemsOnAuctionByGivenPublicationDTO(String bdPublicationDTO){

        List<PublicationSample> listOfItemsOnAuctionByPublication = _aucRepo.getListOfItemsOnAuctionByGivenPublication(bdPublicationDTO);

        List<AuctionItemDTO_US024> listAuctionItemDTO = AuctionItemDTO_US024.toDTOs(listOfItemsOnAuctionByPublication);

        return listAuctionItemDTO;
    }
}
