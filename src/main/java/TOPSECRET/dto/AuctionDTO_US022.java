package TOPSECRET.dto;

import TOPSECRET.domain.*;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.PublicationSample.PublicationSample;

import java.util.ArrayList;
import java.util.List;
/**
 * Data Transfer Object (DTO) representing auction information
 * required for use case US022.
 * <p>
 * This DTO contains simplified information extracted from
 * {@link Auction} domain objects, namely:
 * <ul>
 *     <li>The publication title</li>
 *     <li>The list of genre names associated with the publication</li>
 * </ul>
 * <p>
 * It is used to decouple the presentation/application layer
 * from the domain layer.
 */
public class AuctionDTO_US022 {
    /** The title of the publication being auctioned. */
    public final String title;
    /** The list of genre names associated with the publication. */
    public final List<String> genres;

    /**
     * Constructs an {@code AuctionDTO_US022}.
     *
     * @param title the title of the publication
     * @param genres the list of genre names
     */
    public AuctionDTO_US022(String title, List<String> genres) {
        this.title = title;
        this.genres = genres;
    }

    /**
     * Converts a list of {@link Auction} domain objects into
     * a list of {@code AuctionDTO_US022}.
     * <p>
     * For each auction, all items in the associated listing are processed.
     * Each {@link PublicationSample} contributes one DTO containing:
     * <ul>
     *     <li>The publication title</li>
     *     <li>The list of genre names</li>
     * </ul>
     *
     * @param auctions the list of auctions to convert
     * @return a list of {@code AuctionDTO_US022} objects
     * @throws NullPointerException if {@code auctions} is null
     */
    // Creates a list of AuctionDTO from a list of Auction domain
//    public static List<AuctionDTO_US022> fromDomainList(List<Auction> auctions) {
//        // Creates an empty DTO List
//        List<AuctionDTO_US022> result = new ArrayList<>();
//
//        // Iterates over all Auction domain
//        for (Auction auction : auctions) {
//            // Gets the listing associated with the auction
//            Listing listing = auction.getListing();
//            // Iterates over all items on listing
//            // Each item represents a publication being auctioned
//            for (PublicationSample publicationSample : listing.getItems()) {
//                // Gets the publication associated with the item
//                // Gets the Bibliographical Data of the publication
//                BibliographicalData bibliographicalData = publicationSample.getBibliographicalData();
//                // Gets the title of the publication
//                String title = bibliographicalData.getTitle();
//                // Creates a list of genres names
//                List<String> genreNames = new ArrayList<>();
//                for (Genre genre : bibliographicalData.getGenres()) {
//                    // Converts each genre domain object into string
//                    genreNames.add(genre.getGenreType());
//                }
//                // Creates a DTO for this item on auction
//                AuctionDTO_US022 dto =
//                        new AuctionDTO_US022(title, genreNames);
//                // adds the DTO to the AuctionDTO list
//                result.add(dto);
//            }
//        }
//        // returns the list of AuctionDTO
//        return result;
//    }
}

