package TOPSECRET.dto;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.ValueObjects.Price;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuctionDTO_US026Test {


//    @Test
//    void fromAuction_shouldThrow_whenGivenAuctionIsNull() {
//
//        // act + assert
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> AuctionDTO_US026.fromAuction(null));
//
//        assertEquals("Auction cannot be null.", exception.getMessage());
//    }
//
//    @Test
//    void fromAuction_shouldReturnAuctionDTO_whenValidAuctionGiven() {
//
//        // arrange
//        Listing listingDouble = createListingDouble("35.00");
//        Auction auction = mock(Auction.class);
//        when(auction.getListing()).thenReturn(listingDouble);
//
//        // act
//        AuctionDTO_US026 auctionDTO = AuctionDTO_US026.fromAuction(auction);
//
//        // assert
//        assertEquals(listingDouble, auctionDTO.listing());
//
//    }
//
//    private Listing createListingDouble(String initialPrice) {
//        Listing listing = mock(Listing.class);
//        when(listing.getSaleType()).thenReturn(SaleType.AUCTION);
//        Price initial = mock(Price.class);
//        when(initial.getPrice()).thenReturn(new BigDecimal(initialPrice));
//        when(listing.getPrice()).thenReturn(initial);
//        return listing;
//    }


}