package TOPSECRET.controller;


import TOPSECRET.domain.Publication;
import TOPSECRET.domain.PublicationSample;
import TOPSECRET.dto.PublicationDetailsDTO;
import TOPSECRET.dto.PublicationDetailsMapper;
import TOPSECRET.repository.AuctionRepository;
import TOPSECRET.repository.PublicationSampleRepository;

import java.util.List;

public class GetTheListOfItemsOnAuctionOfAGivenPublicationController {

    private PublicationSampleRepository _pubRepo;
    private  AuctionRepository _aucRepo;

    public GetTheListOfItemsOnAuctionOfAGivenPublicationController(PublicationSampleRepository pubRepo, AuctionRepository aucRepo) {
        _pubRepo = pubRepo;
        _aucRepo = aucRepo;
    }

//    public List<PublicationDetailsDTO> getAllPublications(){
//
//        List<PublicationSample> listOfAllPublication = _pubRepo.getAllPublications();
//
//        List<PublicationDetailsDTO> listOfAllPublicationDTOUS012 = PublicationDetailsMapper.toDTOs(listOfAllPublication);
//
//        return listOfAllPublicationDTOUS012;
//    }

    // In this part, a dedicated mapper was not used. US024ItemDTO implements the DTO
    // conversion logic and is therefore used directly as a mapper.

//    public List<ItemDetailsDTO> getListOfItemsOnAuctionByGivenPublicationDTO(Publication publication){
//
//        List<Item> listOfItemsOnAuctionByPublication = _aucRepo.getListOfItemsOnAuctionByGivenPublication(publication);
//
//        List<ItemDetailsDTO> listOfAuctionsDTO = ItemDetailsDTO.toDTOs(listOfItemsOnAuctionByPublication);
//
//        return listOfAuctionsDTO;
//    }
}
