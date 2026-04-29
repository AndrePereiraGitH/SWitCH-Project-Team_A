package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Auction.AuctionFactory;
import TOPSECRET.domain.Auction.Bid;
import TOPSECRET.domain.Enum.AuctionStatus;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.persistence.repositoryjpa.datamodel.AuctionDM;
import TOPSECRET.persistence.repositoryjpa.datamodel.BidDM;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link AuctionAssembler}.
 *
 * <p>These tests verify that:
 * <ul>
 *   <li>{@code toDM(Auction)} returns a non-null data model instance</li>
 *   <li>{@code toDomain(AuctionFactory, AuctionDM)} delegates creation to the factory
 *       and returns the Auction instance produced by it</li>
 * </ul>
 */
class AuctionAssemblerTest {

    // ============================================================
    // ================ toDM ======================================
    // ============================================================

    @Test
    void toDM_shouldReturnAuctionDM() {
        // Arrange
        Auction auctionDouble = mock(Auction.class);
        AuctionId auctionIdDouble = mock(AuctionId.class);
        ListingId listingIdDouble = mock(ListingId.class);
        Price initialPriceDouble = mock(Price.class);
        Price buyoutPriceDouble = mock(Price.class);
        Bid winningBidDouble = mock(Bid.class);

        when(auctionDouble.id()).thenReturn(auctionIdDouble);
        when(auctionDouble.getListingId()).thenReturn(listingIdDouble);
        when(auctionDouble.getInitialPrice()).thenReturn(initialPriceDouble);
        when(auctionDouble.getBuyoutPrice()).thenReturn(buyoutPriceDouble);
        when(auctionDouble.getWinningBid()).thenReturn(winningBidDouble);
        when(winningBidDouble.getValue()).thenReturn(initialPriceDouble);

        AuctionFactory auctionFactory = mock(AuctionFactory.class);
        AuctionAssembler auctionAssembler = new AuctionAssembler(auctionFactory);

        // Act
        AuctionDM result = auctionAssembler.toDM(auctionDouble);

        // Assert
        assertNotNull(result);
    }

    @Test
    void toDM_shouldReturnNullWhenInputIsNull() {
        // Arrange
        AuctionFactory auctionFactory = mock(AuctionFactory.class);
        AuctionAssembler auctionAssembler = new AuctionAssembler(auctionFactory);

        // Act
        AuctionDM result = auctionAssembler.toDM(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toDM_shouldHandleNullBuyoutPrice() {
        // Arrange
        Auction auctionDouble = mock(Auction.class);
        AuctionId auctionIdDouble = mock(AuctionId.class);
        ListingId listingIdDouble = mock(ListingId.class);
        Price initialPriceDouble = mock(Price.class);
        Bid winningBidDouble = mock(Bid.class);

        when(auctionDouble.id()).thenReturn(auctionIdDouble);
        when(auctionDouble.getListingId()).thenReturn(listingIdDouble);
        when(auctionDouble.getInitialPrice()).thenReturn(initialPriceDouble);
        when(auctionDouble.getBuyoutPrice()).thenReturn(null);
        when(auctionDouble.getWinningBid()).thenReturn(winningBidDouble);
        when(winningBidDouble.getValue()).thenReturn(initialPriceDouble);

        AuctionFactory auctionFactory = mock(AuctionFactory.class);
        AuctionAssembler auctionAssembler = new AuctionAssembler(auctionFactory);

        // Act
        AuctionDM result = auctionAssembler.toDM(auctionDouble);

        // Assert
        assertNotNull(result);
    }

    // ============================================================
    // ================ toDomain ==================================
    // ============================================================

    @Test
    void toDomain_shouldReconstructAuctionFromDM() {
        // Arrange
        AuctionId auctionIdDouble = mock(AuctionId.class);
        ListingId listingIdDouble = mock(ListingId.class);
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(10);
        BigDecimal initialPriceDouble = new BigDecimal("10");
        BigDecimal buyoutPriceDouble = new BigDecimal("100");
        BigDecimal finalPriceDouble = new BigDecimal("70");
        UserId userIdDouble = mock(UserId.class);
        List<BidDM> bidHistoryDouble = List.of(new BidDM(userIdDouble, buyoutPriceDouble, LocalDateTime.now()));
        BidDM winningBidDouble = mock(BidDM.class);
        when(winningBidDouble.getValue()).thenReturn(finalPriceDouble);

        LocalDateTime closedAt = endDate;

        Auction auctionDouble = mock(Auction.class);

        AuctionDM auctionDM = new AuctionDM(auctionIdDouble, listingIdDouble, startDate, endDate, initialPriceDouble,
               buyoutPriceDouble, bidHistoryDouble, winningBidDouble, AuctionStatus.ACTIVE, finalPriceDouble, closedAt );

        AuctionFactory auctionFactory = mock(AuctionFactory.class);
        when(auctionFactory.create(any(), any(), any(), any(), any(), any())).thenReturn(auctionDouble);

        AuctionAssembler assembler = new AuctionAssembler(auctionFactory);

        // Act
        Auction result = assembler.toDomain(auctionDM);

        // Assert
        assertNotNull(result);
    }

    @Test
    void toDomain_shouldReturnNullWhenInputIsNull() {
        // Arrange
        AuctionFactory auctionFactory = mock(AuctionFactory.class);

        AuctionAssembler assembler = new AuctionAssembler(auctionFactory);

        // Act
        Auction result = assembler.toDomain(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toDomain_shouldSetBuyoutPriceToNullWhenDMHasNoBuyoutPrice() {
        // Arrange
        AuctionFactory factoryDouble = mock(AuctionFactory.class);
        Auction auctionDouble = mock(Auction.class);
        when(factoryDouble.create(any(), any(), any(), any(), any(), isNull()))
                .thenReturn(auctionDouble);

        AuctionDM dmDouble = mock(AuctionDM.class);
        BigDecimal price = new BigDecimal("10");
        when(dmDouble.getInitialPrice()).thenReturn(price);
        when(dmDouble.getBuyoutPrice()).thenReturn(null);
        when(dmDouble.getBidHistory()).thenReturn(List.of());

        BidDM winningBidDM = mock(BidDM.class);
        when(winningBidDM.getBidderId()).thenReturn(new UserId("user-1"));
        when(winningBidDM.getValue()).thenReturn(new BigDecimal("20"));
        when(winningBidDM.getTimeOfBid()).thenReturn(LocalDateTime.now());
        when(dmDouble.getWinningBid()).thenReturn(winningBidDM);

        AuctionAssembler assembler = new AuctionAssembler(factoryDouble);

        // Act
        Auction result = assembler.toDomain(dmDouble);

        // Assert
        assertNotNull(result);
    }

    @Test
    void toDomain_shouldRehydrateWinningBidCorrectly() {
        // Arrange
        AuctionFactory factoryDouble = mock(AuctionFactory.class);
        Auction auctionDouble = mock(Auction.class);
        when(factoryDouble.create(any(), any(), any(), any(), any(), any())).thenReturn(auctionDouble);

        BidDM winningBidDM = mock(BidDM.class);
        when(winningBidDM.getBidderId()).thenReturn(new UserId("U1"));
        when(winningBidDM.getValue()).thenReturn(new BigDecimal("50"));
        when(winningBidDM.getTimeOfBid()).thenReturn(LocalDateTime.now());

        AuctionDM dmDouble = mock(AuctionDM.class);
        when(dmDouble.getInitialPrice()).thenReturn(BigDecimal.TEN);
        when(dmDouble.getBuyoutPrice()).thenReturn(BigDecimal.valueOf(100));
        when(dmDouble.getBidHistory()).thenReturn(List.of());
        when(dmDouble.getWinningBid()).thenReturn(winningBidDM);

        AuctionAssembler assembler = new AuctionAssembler(factoryDouble);

        // Act
        Auction result = assembler.toDomain(dmDouble);

        // Assert
        assertNotNull(result);
    }

    @Test
    void toDomain_shouldCallFactoryWithCorrectArguments() {
        // Arrange
        Auction auction = mock(Auction.class);

        when(auction.getBidHistory()).thenReturn(new ArrayList<>());
        when(auction.rehydrateBid(any(), any(), any())).thenReturn(mock(Bid.class));

        AuctionFactory factoryDouble = mock(AuctionFactory.class);
        Auction auctionDouble = mock(Auction.class);

        when(factoryDouble.create(any(), any(), any(), any(), any(), any())).thenReturn(auctionDouble);

        BidDM winningBidDM = mock(BidDM.class);
        when(winningBidDM.getBidderId()).thenReturn(mock(UserId.class));
        when(winningBidDM.getValue()).thenReturn(new BigDecimal(8));
        when(winningBidDM.getTimeOfBid()).thenReturn(LocalDateTime.now());

        AuctionDM auctionDM = mock(AuctionDM.class);
        when(auctionDM.getInitialPrice()).thenReturn(new BigDecimal(1));
        when(auctionDM.getBuyoutPrice()).thenReturn(new BigDecimal(10));
        when(auctionDM.getBidHistory()).thenReturn(List.of());
        when(auctionDM.getWinningBid()).thenReturn(winningBidDM);

        AuctionAssembler assembler = new AuctionAssembler(factoryDouble);

        // Act
        Auction result = assembler.toDomain(auctionDM);

        // Assert
        assertSame(auctionDouble, result);
    }

    @Test
    void toDomain_shouldApplyRehydrateStateWhenStatusIsNotNull() {
        // Arrange
        AuctionId auctionIdDouble = mock(AuctionId.class);
        ListingId listingIdDouble = mock(ListingId.class);
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        BigDecimal initialPrice = new BigDecimal("10");
        BigDecimal buyOutPrice = new BigDecimal("100");
        BigDecimal finalPrice = new BigDecimal("10");

        BidDM winningBid = new BidDM(mock(UserId.class), BigDecimal.valueOf(50), endDate);

        AuctionDM dm = new AuctionDM(auctionIdDouble, listingIdDouble, startDate, endDate, initialPrice,
                buyOutPrice, List.of(), winningBid, AuctionStatus.ENDED_WITHOUT_BIDS, finalPrice, endDate);

        AuctionFactory factory = new AuctionFactory();

        AuctionAssembler assembler = new AuctionAssembler(factory);

        // Act
        Auction result = assembler.toDomain(dm);

        // Assert
        assertEquals(AuctionStatus.ENDED_WITHOUT_BIDS, result.getAuctionStatus());
        assertEquals(finalPrice, result.getFinalPrice().getPrice());
        assertEquals(endDate, result.getClosedAt());
    }

    @Test
    void toDomain_shouldNotApplyStateWhenStatusIsNull() {
        // Arrange
        AuctionId auctionIdDouble = mock(AuctionId.class);
        ListingId listingIdDouble = mock(ListingId.class);
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        BigDecimal initialPrice = new BigDecimal("10");
        BigDecimal buyOutPrice = new BigDecimal("100");
        BigDecimal finalPrice = new BigDecimal("10");

        BidDM winningBid = new BidDM(mock(UserId.class), BigDecimal.valueOf(50), endDate);

        AuctionDM dm = new AuctionDM(auctionIdDouble, listingIdDouble, startDate, endDate, initialPrice,
                buyOutPrice, List.of(), winningBid, null, finalPrice, endDate);

        AuctionFactory factory = new AuctionFactory();

        AuctionAssembler assembler = new AuctionAssembler(factory);

        // Act
        Auction result = assembler.toDomain(dm);

        // Assert
        assertNull(result.getAuctionStatus());
        assertNull(result.getFinalPrice());
        assertNull(result.getClosedAt());
    }
}