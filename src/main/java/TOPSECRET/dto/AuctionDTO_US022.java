package TOPSECRET.dto;

import TOPSECRET.domain.*;

import java.util.ArrayList;
import java.util.List;
/**
 * Data Transfer Object used in US022.
 *
 * Represents one item that is currently on auction,
 * containing only the information needed by the UI
 * to display auctions filtered by genre.
 */
public class AuctionDTO_US022 {

    public String title;
    public List<String> genres;

    public AuctionDTO_US022(String title, List<String> genres) {
        this.title = title;
        this.genres = genres;
    }

    // Creates a list of AuctionDTO from a list of Auction domain
    public static List<AuctionDTO_US022> fromDomainList(List<Auction> auctions) {
        // Creates an empty DTO List
        List<AuctionDTO_US022> result = new ArrayList<>();

        // Iterates over all Auction domain
        for (Auction auction : auctions) {
            // Gets the listing associated with the auction
            Listing listing = auction.getListing();
            // Iterates over all items on listing
            // Each item represents a publication being auctioned
            for (PublicationSample publicationSample : listing.getItems()) {
                // Gets the publication associated with the item
                // Gets the Bibliographical Data of the publication
                BibliographicalData bibliographicalData = publicationSample.getBibliographicalData();
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

