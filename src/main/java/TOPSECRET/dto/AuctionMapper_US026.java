package TOPSECRET.dto;

import TOPSECRET.domain.Auction;

public class AuctionMapper_US026 {

    public static AuctionDTO_US026 fromAuction(Auction auction) {
        if (auction == null)
            throw new IllegalArgumentException("Auction cannot be null.");

        return new AuctionDTO_US026(auction.getListing());
    }

}
