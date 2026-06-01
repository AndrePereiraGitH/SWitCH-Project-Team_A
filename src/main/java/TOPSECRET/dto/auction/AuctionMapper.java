package TOPSECRET.dto.auction;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.auction.response.AuctionDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Utility class for mapping {@link Auction} domain objects to {@link AuctionDTO} response DTOs,
 * and for converting primitive identifiers to their corresponding Value Objects
 * ({@link AuctionId}, {@link ListingId}, {@link UserId}, {@link GenreId},
 * {@link AuthorId}, {@link PublisherID}, {@link BibliographicalDataId} and {@link Price}).
 *
 * <p>This class is stateless and provides only static methods — it cannot be instantiated.</p>
 *
 * <p>Denormalized fields from the Listing aggregate ({@code listingTitle}, {@code listingDescription})
 * are resolved by the caller and passed explicitly, keeping this mapper free of any repository dependency.</p>
 *
 * <p>Fields {@code buyoutPrice}, {@code finalPrice} and {@code closedAt} may be {@code null} —
 * the former if the auction has no buyout price, the latter two if the auction has not yet ended.</p>
 *
 * @see AuctionDTO
 */
public class AuctionMapper {

    private AuctionMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    // =================================================================================================================
    // =========== toDTO(s) ============================================================================================
    // =================================================================================================================

    /**
     * Converts an {@link Auction} domain object into an {@link AuctionDTO}.
     *
     * <p>Denormalized fields ({@code listingTitle}, {@code listingDescription}) are sourced
     * from the provided {@link Listing} and included in the response for display convenience.</p>
     *
     * @param auction the auction to convert; must not be {@code null}
     * @param listing the listing associated with the auction; must not be {@code null}
     * @return the corresponding {@link AuctionDTO}
     * @throws NullPointerException if {@code auction} or {@code listing} is {@code null}
     */
    public static AuctionDTO toDTO(Auction auction, Listing listing)  {

        Objects.requireNonNull(auction, "Auction can't be null");
        Objects.requireNonNull(listing, "Listing can't be null");

        return new AuctionDTO(
                auction.id().toString(),
                auction.getListingId().toString(),
                listing.getListingTitle(),
                listing.getShortDescription(),
                auction.getAuctionStartDateTime(),
                auction.getAuctionEndDateTime(),
                auction.getClosedAt(),
                auction.getInitialPrice().getPrice(),
                auction.getBuyoutPrice() != null ? auction.getBuyoutPrice().getPrice() : null,
                auction.getFinalPrice() != null ? auction.getFinalPrice().getPrice() : null,
                auction.getAuctionStatus().toString()
        );
    }

    /**
     * Converts a list of {@link Auction} domain objects into a list of {@link AuctionDTO}.
     *
     * <p>Each auction is matched to its corresponding {@link Listing} via the provided
     * {@code listingMap}, keyed by {@link ListingId}.</p>
     *
     * @param auctions   the list of auctions to convert; must not be {@code null}
     * @param listingMap a map of {@link ListingId} to {@link Listing} containing all listings
     *                   referenced by the auctions; must not be {@code null}
     * @return an unmodifiable list of {@link AuctionDTO}
     * @throws NullPointerException     if {@code auctions} or {@code listingMap} is {@code null}
     * @throws IllegalArgumentException if a {@link Listing} is not found for any auction's {@code listingId}
     */
    public static List<AuctionDTO> toDTOs(List<Auction> auctions, Map<ListingId, Listing> listingMap) {

        Objects.requireNonNull(auctions, "Auctions list can't be null");
        Objects.requireNonNull(listingMap, "Listing map can't be null");

        List<AuctionDTO> result = new ArrayList<>();
        for (Auction auction : auctions) {
            Listing listing = listingMap.get(auction.getListingId());
            if (listing == null)
                throw new IllegalArgumentException("Listing not found for auction " + auction.id());
            result.add(toDTO(auction, listing));
        }
        return List.copyOf(result);
    }

    // =================================================================================================================
    // =========== toVOs ===============================================================================================
    // =================================================================================================================

    /**
     * Converts a {@link String} identifier into an {@link AuctionId} Value Object.
     *
     * @param auctionId the string representation of the auction identifier; must not be {@code null}
     * @return the corresponding {@link AuctionId}
     */
    public static AuctionId toAuctionId(String auctionId) {
        return new AuctionId(auctionId);
    }

    /**
     * Converts a {@link String} identifier into a {@link ListingId} Value Object.
     *
     * @param listingId the string representation of the listing identifier; must not be {@code null}
     * @return the corresponding {@link ListingId}
     */
    public static ListingId toListingId(String listingId) {
        return new ListingId(listingId);
    }

    /**
     * Converts a {@link String} identifier into a {@link BibliographicalDataId} Value Object.
     *
     * @param bdId the string representation of the bibliographical data identifier; must not be {@code null}
     * @return the corresponding {@link BibliographicalDataId}
     */
    public static BibliographicalDataId toBibliographicalDataId(String bdId) {
        return new BibliographicalDataId(bdId);
    }

    /**
     * Converts a {@link String} identifier into a {@link GenreId} Value Object.
     *
     * @param genreId the string representation of the genre identifier; must not be {@code null}
     * @return the corresponding {@link GenreId}
     */
    public static GenreId toGenreId(String genreId) {
        return new GenreId(genreId);
    }

    /**
     * Converts a {@link String} identifier into a {@link AuthorId} Value Object.
     *
     * @param authorId the string representation of the author identifier; must not be {@code null}
     * @return the corresponding {@link AuthorId}
     */
    public static AuthorId toAuthorId(String authorId) {
        return new AuthorId(authorId);
    }

    /**
     * Converts a {@link String} identifier into a {@link PublisherID} Value Object.
     *
     * @param publisherId the string representation of the publisher identifier; must not be {@code null}
     * @return the corresponding {@link PublisherID}
     */
    public static PublisherID toPublisherId(String publisherId) {
        return new PublisherID(publisherId);
    }

    /**
     * Converts a {@link String} identifier into a {@link UserId} Value Object.
     *
     * @param userId the string representation of the user identifier; must not be {@code null}
     * @return the corresponding {@link UserId}
     */
    public static UserId toUserId(String userId) {
        return new UserId(userId);
    }

    /**
     * Converts a string value into a {@link Price}.
     *
     * @param price the price value as a string
     * @return a {@link Price} value object
     * @throws NumberFormatException if the price is not a valid decimal number
     */
    public static Price toPrice(String price) {
        return new Price(new BigDecimal(price));
    }
}
