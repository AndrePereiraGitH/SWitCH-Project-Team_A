package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.SaleType;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;

public interface IListingRepository {

    Listing createAndAdd(String listingTitle,
                         List<PublicationSample> items,
                         SaleType saleType,
                         Price price,
                         String shortDescription,
                         List<Picture> pictures) throws InstanceAlreadyExistsException;
    List<PublicationSample> getListOfItemsOnDirectSaleOfAGivenGenre(Genre genre);
    List<PublicationSample> getListOfItemsOnDirectSaleOfAGivenAuthor(Author author);
    List<Listing> getAllListings();
    List<Listing> findAllDirectSaleListings();
    List<Listing> findAllAuctionListings();


}
