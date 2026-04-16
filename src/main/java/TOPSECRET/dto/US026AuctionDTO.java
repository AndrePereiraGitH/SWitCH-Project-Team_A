package TOPSECRET.dto;


/**
 * Data Transfer Object for US026 that represents an auction item.
 * <p>
 * This DTO is used to transfer auction data between layers without exposing
 * the domain entity directly.
 *
 * @param auctionId the unique identifier of the auction
 * @param listingTitle the title of the listing associated with the auction
 * @param auctionStatus the current status of the auction
 */
public record US026AuctionDTO(
        String auctionId,
        String listingTitle,
        String auctionStatus

) {

    public US026AuctionDTO {
        if (auctionId == null || auctionId.isBlank()) {
            throw new IllegalArgumentException("auctionId cannot be null");
        }

        if (listingTitle == null) {
            throw new IllegalArgumentException("listing title cannot be null");
        }

        if (auctionStatus == null) {
            throw new IllegalArgumentException("auction status cannot be null");
        }
    }


}
