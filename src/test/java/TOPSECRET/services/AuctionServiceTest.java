package TOPSECRET.services;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Auction.AuctionFactory;
import TOPSECRET.domain.Auction.IAuctionRepository;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test suite for {@link AuctionService}, validating its behaviour as an
 * application‑layer component responsible for delegating auction creation
 * to the {@link AuctionFactory}.
 *
 * <p>These tests ensure that the service:
 * <ul>
 *     <li>rejects invalid dependencies;</li>
 *     <li>correctly delegates creation to the factory;</li>
 *     <li>returns the factory‑produced {@link Auction} instance;</li>
 * </ul>
 */
class AuctionServiceTest {

    // ======================================
    // ========== Set Up ====================
    // ======================================

    private AuctionFactory auctionFactoryDouble;
    private IAuctionRepository auctionRepoDouble;

    private ListingId listingIdDouble;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Price initialPrice;
    private Price buyOutPrice;
    private Auction auctionDouble;

    @BeforeEach
    void setUp() throws InstanceAlreadyExistsException {

        listingIdDouble = mock(ListingId.class);
        startDate = LocalDateTime.now().plusDays(1);
        endDate = startDate.plusDays(5);
        initialPrice = new Price(new BigDecimal("10.00"));
        buyOutPrice = new Price(new BigDecimal("100.00"));

        auctionDouble = mock(Auction.class);

        auctionFactoryDouble = mock(AuctionFactory.class);
        when(auctionFactoryDouble.create(listingIdDouble, startDate, endDate, initialPrice, buyOutPrice)).thenReturn(auctionDouble);

        auctionRepoDouble = mock(IAuctionRepository.class);
        when(auctionRepoDouble.save(auctionDouble)).thenReturn(auctionDouble);
    }

    // =============================================
    // ========== constructor ======================
    // =============================================

    @Test
    void constructor_shouldReturnServiceWhenDependenciesAreValid() {
        // Act + Assert
        assertDoesNotThrow(() -> new AuctionService(auctionFactoryDouble, auctionRepoDouble));
    }

    @Test
    void constructor_shouldThrowErrorWhenAuctionRepositoryIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new AuctionService(null, auctionRepoDouble));
    }

    @Test
    void constructor_shouldThrowErrorWhenAuctionFactoryIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new AuctionService(auctionFactoryDouble, null));
    }

    // =============================================
    // ========== createAndSave ====================
    // =============================================

    @Test
    void creatAndSave_shouldCreateAndSaveAuctionSuccessfully() throws Exception {
        // Arrange
        AuctionService auctionService = new AuctionService(auctionFactoryDouble, auctionRepoDouble);

        // Act
        Auction result = auctionService.createAndSave(listingIdDouble, startDate, endDate, initialPrice, buyOutPrice);

        // Assert
        assertEquals(auctionDouble, result);
    }

    @Test
    void createAndSave_shouldThrowExceptionWhenAuctionAlreadyExists() throws Exception {
        // Arrange
        when(auctionRepoDouble.save(auctionDouble)).thenThrow(new InstanceAlreadyExistsException());

        AuctionService auctionService = new AuctionService(auctionFactoryDouble, auctionRepoDouble);

        // Act + Assert
        assertThrows(InstanceAlreadyExistsException.class, () ->
                auctionService.createAndSave(listingIdDouble, startDate, endDate, initialPrice, buyOutPrice)
        );
    }
}