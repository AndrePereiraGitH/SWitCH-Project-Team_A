package TOPSECRET.dto;

import TOPSECRET.domain.Enum.AuctionStatus;
import TOPSECRET.domain.ValueObjects.AuctionId;


/**
 * Data Transfer Object for US026 that represents an auction item.
 * <p>
 * This DTO is used to transfer auction data between layers without exposing
 * the domain entity directly.
 *
 * @param id the unique identifier of the auction
 * @param listingTitle the title of the listing associated with the auction
 * @param auctionStatus the current status of the auction
 */
public record US026AuctionDTO(
        AuctionId id,
        String listingTitle,
        AuctionStatus auctionStatus

) {

}
