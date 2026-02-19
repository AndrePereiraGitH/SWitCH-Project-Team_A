package TOPSECRET.controller;


import TOPSECRET.domain.Item;
import TOPSECRET.domain.Publication;
import TOPSECRET.dto.PublicationDetailsDTO;
import TOPSECRET.dto.PublicationDetailsMapper;
import TOPSECRET.dto.ItemDetailsDTO;
import TOPSECRET.repository.AuctionRepository;
import TOPSECRET.repository.PublicationRepository;

import java.util.List;

public class GetTheListOfItemsOnAuctionOfAGivenPublicationController {

    private  PublicationRepository _pubRepo;
    private  AuctionRepository _aucRepo;

    public GetTheListOfItemsOnAuctionOfAGivenPublicationController(PublicationRepository pubRepo, AuctionRepository aucRepo) {
        _pubRepo = pubRepo;
        _aucRepo = aucRepo;
    }

    public List<PublicationDetailsDTO> getAllPublications(){

        List<Publication> listOfAllPublication = _pubRepo.getAllPublications();

        List<PublicationDetailsDTO> listOfAllPublicationDTOUS012 = PublicationDetailsMapper.toDTOs(listOfAllPublication);

        return listOfAllPublicationDTOUS012;
    }

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
