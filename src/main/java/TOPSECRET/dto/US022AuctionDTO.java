package TOPSECRET.dto;
/**
 * DTO representing an Auction for US022.
 *
 * @param auctionId the auction identifier (must not be null)
 * @param listingId the associated listing identifier (must not be null)
 *
 * @throws IllegalArgumentException if any field is null
 */
public record US022AuctionDTO(
        String auctionId,
        String listingId
) {

    public US022AuctionDTO {
        if(auctionId == null){
            throw new IllegalArgumentException("auctionId is null");
        }
        if(listingId == null){
            throw new IllegalArgumentException("listingId can't be null");
        }
    }
}
