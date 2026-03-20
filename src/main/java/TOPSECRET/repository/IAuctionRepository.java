package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.dto.AuthorDTO_US026;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDateTime;
import java.util.List;

public interface IAuctionRepository {

    List<Auction> getAllAuctions();
    Auction createAndAdd(Listing listing, LocalDateTime initialDate, LocalDateTime finalDate, Price buyoutPrice) throws InstanceAlreadyExistsException;
    List<Auction> getListOfItemsOnAuctionGivenGenre(Genre genre);
    List<Auction> getListOfItemsOnAuctionGivenAuthor(AuthorDTO_US026 author);
    List<PublicationSample> getListOfItemsOnAuctionByGivenPublication(String publicationDTO);
}
