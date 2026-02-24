package TOPSECRET.controller;

import TOPSECRET.domain.Auction;
import TOPSECRET.domain.Author;
import TOPSECRET.dto.AuctionDTO_US026;
import TOPSECRET.dto.AuthorDTO_US026;
import TOPSECRET.repository.AuctionRepository;
import TOPSECRET.repository.AuthorRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for the use case US026:
 * retrieving the list of items currently on auction for a given author.
 */
public class US026ToGetTheListOfItemsOnAuctionOfAGivenAuthorController {

    private final AuthorRepository authorRepository;
    private final AuctionRepository auctionRepo;

    public US026ToGetTheListOfItemsOnAuctionOfAGivenAuthorController(AuthorRepository authorRepository, AuctionRepository auctionRepo) {
        this.authorRepository = authorRepository;
        this.auctionRepo = auctionRepo;
    }

    /**
     * Retrieves all authors and converts them to DTOs.
     *
     * @return a list of {@link AuthorDTO_US026} representing all authors
     */
    public List<AuthorDTO_US026> getAllAuthors() {

        List<Author> authorsList = authorRepository.getAllAuthors();
        List<AuthorDTO_US026> authorsListDTO = new ArrayList<>();

        for (Author a : authorsList) {
            authorsListDTO.add(AuthorDTO_US026.fromAuthor(a));
        }

        return authorsListDTO;
    }

    /**
     * Retrieves the list of items on auction for the given author and converts them to DTOs.
     *
     * @param author the author DTO used to filter auctions
     * @return a list of {@link AuctionDTO_US026} for the specified author
     */
    public List<AuctionDTO_US026> getListOfItemsOnAuctionByAuthor(AuthorDTO_US026 author) {

        List<Auction> auctions = auctionRepo.getListOfItemsOnAuctionGivenAuthor(author);
        List<AuctionDTO_US026> auctionsListDto = new ArrayList<>();

        for (Auction a : auctions) {
            auctionsListDto.add(AuctionDTO_US026.fromAuction(a));
        }

        return auctionsListDto;
    }

}
