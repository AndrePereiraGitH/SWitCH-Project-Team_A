package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Auction.AuctionFactory;
import TOPSECRET.domain.Auction.Bid;
import TOPSECRET.domain.Enum.AuctionStatus;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.persistence.repositoryjpa.datamodel.AuctionDM;
import TOPSECRET.persistence.repositoryjpa.datamodel.BidDM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
@ExtendWith(MockitoExtension.class)
class AuctionAssemblerTest {

    @Mock
    private AuctionFactory auctionFactory;

    @Mock
    private Auction auction;

    @InjectMocks
    private AuctionAssembler auctionAssembler;

    // ============================================================
    // ================ Set Up ====================================
    // ============================================================

    private Auction auctionDouble;
    private AuctionId auctionIdDouble;
    private AuctionDM auctionDMDouble;
    private ListingId listingIdDouble;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime closedAt;
    private Price initialPriceDouble;
    private Price buyoutPriceDouble;
    private Price finalPriceDouble;
    private Bid bidDouble;
    private BidDM bidDMDouble;

    @BeforeEach
    void setUp() {
        auctionDouble = mock(Auction.class);
        auctionIdDouble = mock(AuctionId.class);
        auctionDMDouble = mock(AuctionDM.class);
        listingIdDouble = mock(ListingId.class);
        startDate = LocalDateTime.now();
        endDate = startDate.plusDays(10);
        closedAt = endDate;
        initialPriceDouble = mock(Price.class);
        buyoutPriceDouble = mock(Price.class);
        finalPriceDouble = mock(Price.class);
        bidDouble = mock(Bid.class);
        bidDMDouble = mock(BidDM.class);
    }

    // ============================================================
    // ================ toDM ======================================
    // ============================================================

    @Test
    void toDM_shouldReturnAuctionDM() {
        // Arrange
        when(auctionDouble.id()).thenReturn(auctionIdDouble);
        when(auctionDouble.getListingId()).thenReturn(listingIdDouble);
        when(auctionDouble.getInitialPrice()).thenReturn(initialPriceDouble);
        when(auctionDouble.getBuyoutPrice()).thenReturn(buyoutPriceDouble);
        when(auctionDouble.getWinningBid()).thenReturn(bidDouble);
        when(bidDouble.getValue()).thenReturn(initialPriceDouble);

        // Act
        AuctionDM result = auctionAssembler.toDM(auctionDouble);

        // Assert
        assertNotNull(result);
    }

    @Test
    void toDM_shouldThrowExceptionWhenInputIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> auctionAssembler.toDM(null));
    }

    @Test
    void toDM_shouldHandleNullBuyoutPrice() {
        // Arrange
        when(auctionDouble.id()).thenReturn(auctionIdDouble);
        when(auctionDouble.getListingId()).thenReturn(listingIdDouble);
        when(auctionDouble.getInitialPrice()).thenReturn(initialPriceDouble);
        when(auctionDouble.getBuyoutPrice()).thenReturn(null);
        when(auctionDouble.getWinningBid()).thenReturn(bidDouble);
        when(bidDouble.getValue()).thenReturn(initialPriceDouble);

        // Act
        AuctionDM result = auctionAssembler.toDM(auctionDouble);

        // Assert
        assertNotNull(result);
    }

    // ============================================================
    // ================ toDomain ==================================
    // ============================================================

    @Test
    void toDomain_shouldThrowExceptionWhenInputIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> auctionAssembler.toDomain(null));
    }

    @Test
    void toDomain_shouldCorrectlyRehydrateFullAuctionState() {

        // Arrange
        BidDM bidDM = new BidDM(mock(UserId.class), new BigDecimal("20"), endDate);

        AuctionDM dm = new AuctionDM(auctionIdDouble, listingIdDouble, startDate, endDate, new BigDecimal("10"),
                new BigDecimal("100"), List.of(bidDM), bidDM, AuctionStatus.ENDED_WITH_WINNER, new BigDecimal("50"), closedAt);

        when(auctionFactory.create(any(), any(), any(), any(), any(), any())).thenCallRealMethod();

        // Act
        Auction result = auctionAssembler.toDomain(dm);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getAuctionStatus());
        assertNotNull(result.getClosedAt());
    }


    @Test
    void toDomain_shouldSetBuyoutPriceToNullWhenDMHasNoBuyoutPrice() {
        // Arrange
        AuctionDM auctionDMDouble = mock(AuctionDM.class);

        when(auctionDMDouble.getInitialPrice()).thenReturn(new BigDecimal("10"));

        when(auctionFactory.create(any(), any(), any(), any(), any(), isNull())).thenReturn(auction);

        // Act
        Auction result = auctionAssembler.toDomain(auctionDMDouble);

        // Assert
        assertNotNull(result);
        assertNull(result.getBuyoutPrice());
    }

    @Test
    void toDomain_shouldNotRehydrateStateWhenStatusIsNull() {
        // Arrange
        AuctionDM auctionDM = new AuctionDM(auctionIdDouble, listingIdDouble, startDate, endDate, new BigDecimal("10"),
                new BigDecimal("100"), new ArrayList<>(), null, null, null, null);

        when(auctionFactory.create(any(), any(), any(), any(), any(), any())).thenReturn(auction);

        // Act
        Auction result = auctionAssembler.toDomain(auctionDM);

        // Assert
        assertNotNull(result);
        assertNull(result.getAuctionStatus());
        assertNull(result.getFinalPrice());
        assertNull(result.getClosedAt());
    }
}