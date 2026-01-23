package TOPSECRET.controller;


import TOPSECRET.domain.Item;
import TOPSECRET.domain.Publication;
import TOPSECRET.dto.PublicationDTO_US012;
import TOPSECRET.dto.PublicationMapper_US012;
import TOPSECRET.dto.ItemDTO_US024;
import TOPSECRET.repository.AuctionRepository;
import TOPSECRET.repository.PublicationRepository;

import java.util.List;

public class GetTheListOfItemsOnAuctionControllerOfAGivenPublication {


    private  PublicationRepository _pubRepo;
    private  AuctionRepository _aucRepo;

    public GetTheListOfItemsOnAuctionControllerOfAGivenPublication(PublicationRepository pubRepo, AuctionRepository aucRepo) {
        _pubRepo = pubRepo;
        _aucRepo = aucRepo;
    }

    public List<PublicationDTO_US012> getAllPublications(){

        List<Publication> listOfAllPublication = _pubRepo.getAllPublications();

        List<PublicationDTO_US012> listOfAllPublicationDTOUS012 = PublicationMapper_US012.getListOfPublicationsDTO(listOfAllPublication);

        return listOfAllPublicationDTOUS012;
    }


    // In this part, a dedicated mapper was not used. US024ItemDTO implements the DTO
    // conversion logic and is therefore used directly as a mapper.

    public List<ItemDTO_US024> getListOfItemsOnAuctionDTO(Publication publication){

        List<Item> listOfItemsOnAuction = _aucRepo.getListOfItemsOnAuctionByGivenPublication(publication);

        List<ItemDTO_US024> listOfAuctionsDTO = ItemDTO_US024.getListOfItemsAuctionDTO(listOfItemsOnAuction);

        return listOfAuctionsDTO;
    }






}
