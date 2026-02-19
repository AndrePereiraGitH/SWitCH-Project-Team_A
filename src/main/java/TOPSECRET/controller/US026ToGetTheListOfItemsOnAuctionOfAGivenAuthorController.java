package TOPSECRET.controller;

import TOPSECRET.domain.Auction;
import TOPSECRET.domain.Author;
import TOPSECRET.dto.AuctionDTO_US026;
import TOPSECRET.dto.AuctionMapper_US026;
import TOPSECRET.dto.AuthorDTO_US026;
import TOPSECRET.dto.AuthorMapper_US026;
import TOPSECRET.repository.AuctionRepository;
import TOPSECRET.repository.AuthorRepository;

import java.util.ArrayList;
import java.util.List;

public class US026ToGetTheListOfItemsOnAuctionOfAGivenAuthorController {

    private final AuthorRepository authorRepository;
    private final AuctionRepository auctionRepo;

    public US026ToGetTheListOfItemsOnAuctionOfAGivenAuthorController(AuthorRepository authorRepository, AuctionRepository auctionRepo) {
        this.authorRepository = authorRepository;
        this.auctionRepo = auctionRepo;
    }


    public List<AuthorDTO_US026> getAllAuthors() {

        List<Author> authorsList = authorRepository.getAllAuthors();
        List<AuthorDTO_US026> authorsListDTO = new ArrayList<>();

        for (Author a : authorsList) {
            authorsListDTO.add(AuthorMapper_US026.fromAuthor(a));
        }

        return authorsListDTO;
    }


//    public List<AuctionDTO_US026> getListOfItemsOnAuctionByAuthor(AuthorDTO_US026 author) {
//
//        List<Auction> auctions = auctionRepo.getListOfItemsOnAuctionGivenAuthor(author);
//        List<AuctionDTO_US026> auctionsListDto = new ArrayList<>();
//
//        for (Auction a : auctions) {
//            auctionsListDto.add(AuctionMapper_US026.fromAuction(a));
//        }
//
//        return auctionsListDto;
//    }

}
