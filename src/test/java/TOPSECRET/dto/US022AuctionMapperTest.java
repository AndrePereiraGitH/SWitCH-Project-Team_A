package TOPSECRET.dto;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.ValueObjects.AuctionId;
import TOPSECRET.domain.ValueObjects.ListingId;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class US022AuctionMapperTest {

    @Test
    void shouldReturnEmptyListWhenInputIsEmpty() {

        // Arrange
        US022AuctionMapper mapper = new US022AuctionMapper();

        List<Auction> auctions = List.of();

        // Act
        List<US022AuctionDTO> result =
                mapper.toDTOList(auctions);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldConvertSingleAuction() {

        // Arrange
        US022AuctionMapper mapper = new US022AuctionMapper();

        Auction auction = mock(Auction.class);

        when(auction.id()).thenReturn(new AuctionId("A1"));
        when(auction.getListingId()).thenReturn(new ListingId("L1"));

        List<Auction> auctions = List.of(auction);

        // Act
        List<US022AuctionDTO> result =
                mapper.toDTOList(auctions);

        // Assert
        assertEquals(1, result.size());
        assertEquals("A1", result.get(0).auctionId());
        assertEquals("L1", result.get(0).listingId());
    }

    @Test
    void shouldConvertMultipleAuctions() {

        // Arrange
        US022AuctionMapper mapper = new US022AuctionMapper();

        Auction auction1 = mock(Auction.class);
        Auction auction2 = mock(Auction.class);

        when(auction1.id()).thenReturn(new AuctionId("A1"));
        when(auction1.getListingId()).thenReturn(new ListingId("L1"));

        when(auction2.id()).thenReturn(new AuctionId("A2"));
        when(auction2.getListingId()).thenReturn(new ListingId("L2"));

        List<Auction> auctions = List.of(auction1, auction2);

        // Act
        List<US022AuctionDTO> result =
                mapper.toDTOList(auctions);

        // Assert
        assertEquals(2, result.size());
        assertEquals("A1", result.get(0).auctionId());
        assertEquals("L1", result.get(0).listingId());
        assertEquals("A2", result.get(1).auctionId());
        assertEquals("L2", result.get(1).listingId());
    }

    @Test
    void shouldPreserveOrder() {

        // Arrange
        US022AuctionMapper mapper = new US022AuctionMapper();

        Auction auction1 = mock(Auction.class);
        Auction auction2 = mock(Auction.class);

        when(auction1.id()).thenReturn(new AuctionId("FIRST"));
        when(auction1.getListingId()).thenReturn(new ListingId("L1"));

        when(auction2.id()).thenReturn(new AuctionId("SECOND"));
        when(auction2.getListingId()).thenReturn(new ListingId("L2"));

        List<Auction> auctions = List.of(auction1, auction2);

        // Act
        List<US022AuctionDTO> result =
                mapper.toDTOList(auctions);

        // Assert
        assertEquals("FIRST", result.get(0).auctionId());
        assertEquals("SECOND", result.get(1).auctionId());
    }

    @Test
    void shouldThrowExceptionWhenInputIsNull() {
        // Arrange
        US022AuctionMapper mapper = new US022AuctionMapper();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                mapper.toDTOList(null)
        );
    }
}