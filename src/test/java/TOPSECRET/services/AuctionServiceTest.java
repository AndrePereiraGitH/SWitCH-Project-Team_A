package TOPSECRET.services;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Auction.AuctionFactory;
import TOPSECRET.domain.Auction.IAuctionRepository;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
@ExtendWith(MockitoExtension.class)
class AuctionServiceTest {

    // ======================================
    // ========== Set Up ====================
    // ======================================

    @Mock
    private AuctionFactory auctionFactoryDouble;

    @Mock
    private IAuctionRepository auctionRepoDouble;

    @Mock
    private ListingId listingIdDouble;

    @Mock
    private Auction auctionDouble;

    @InjectMocks
    private AuctionService auctionService;

    private final LocalDateTime startDate = LocalDateTime.now().plusDays(1);
    private final LocalDateTime endDate = startDate.plusDays(5);
    private final Price initialPrice = new Price(new BigDecimal("10.00"));
    private final Price buyOutPrice = new Price(new BigDecimal("100.00"));

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
        when(auctionFactoryDouble.create(listingIdDouble, startDate, endDate, initialPrice, buyOutPrice)).thenReturn(auctionDouble);

        when(auctionRepoDouble.save(auctionDouble)).thenReturn(auctionDouble);

        // Act
        Auction result = auctionService.createAndSave(listingIdDouble, startDate, endDate, initialPrice, buyOutPrice);

        // Assert
        assertEquals(auctionDouble, result);
    }

    @Test
    void createAndSave_shouldThrowExceptionWhenAuctionAlreadyExists() throws Exception {
        // Arrange
        when(auctionFactoryDouble.create(listingIdDouble, startDate, endDate, initialPrice, buyOutPrice)).thenReturn(auctionDouble);

        when(auctionRepoDouble.save(auctionDouble)).thenThrow(new InstanceAlreadyExistsException());

        // Act + Assert
        assertThrows(InstanceAlreadyExistsException.class, () ->
                auctionService.createAndSave(listingIdDouble, startDate, endDate, initialPrice, buyOutPrice)
        );
    }
}