package TOPSECRET.dto;

import TOPSECRET.domain.Auction;
import TOPSECRET.domain.Listing;

public record AuctionDTO_US026(
        Listing listing
) {

    public static AuctionDTO_US026 fromAuction(Auction auction) {
        if (auction == null)
            throw new IllegalArgumentException("Auction cannot be null.");

        return new AuctionDTO_US026(auction.getListing());
    }
}
