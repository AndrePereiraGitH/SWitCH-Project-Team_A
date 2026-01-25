package TOPSECRET.dto;

import TOPSECRET.domain.*;

import java.util.ArrayList;
import java.util.List;
/**
 * Mapper for US022 – Converts Auction domain objects into AuctionDTO_US022.
 *
 * Each Auction may contain multiple Items; therefore, this mapper
 * produces one DTO per Item.
 */
public class AuctionMapper_US022 {
    // Creates a list of AuctionDTO from a list of Auction domain
    public List<AuctionDTO_US022> toDTO(List<Auction> auctions) {
        // Creates an empty DTO List
        List<AuctionDTO_US022> result = new ArrayList<>();

        // Iterates over all Auction domain
        for (Auction auction : auctions) {
            // Gets the listing associated with the auction
            Listing listing = auction.getListing();
            // Iterates over all items on listing
            // Each item represents a publication being auctioned
            for (Item item : listing.getItems()) {
                // Gets the publication associated with the item
                // Gets the Bibliographical Data of the publication
                Publication publication = item.getPublication();
                BibliographicalData bibliographicalData = publication.getBibliographicalData();
                // Gets the title of the publication
                String title = bibliographicalData.getTitle();
                // Creates a list of genres names
                List<String> genreNames = new ArrayList<>();
                for (Genre genre : bibliographicalData.getGenres()) {
                    // Converts each genre domain object into string
                    genreNames.add(genre.getGenreType());
                }
                // Creates a DTO for this item on auction
                AuctionDTO_US022 dto =
                        new AuctionDTO_US022(title, genreNames);
                // adds the DTO to the AuctionDTO list
                result.add(dto);
            }
        }
        // returns the list of AuctionDTO
        return result;
    }
}