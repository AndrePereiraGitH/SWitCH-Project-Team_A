package TOPSECRET.dto.Auction;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Enum.AuctionStatus;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.ValueObjects.AuctionId;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.dto.Auction.response.AuctionDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link AuctionMapper}.
 *
 * <p>Verifies that all fields of an {@link Auction} domain object are correctly
 * mapped to an {@link AuctionDTO}, including:</p>
 * <ul>
 *   <li>Conversion of {@link AuctionId} and {@link ListingId} to their {@link String} representation</li>
 *   <li>Correct mapping of auction time window ({@code startDateTime}, {@code endDateTime})</li>
 *   <li>Correct mapping of {@code closedAt} and {@code finalPrice}, including the {@code null} case for open auctions</li>
 *   <li>Extraction of {@link BigDecimal} values from {@code Price} Value Objects</li>
 *   <li>Conversion of {@link AuctionStatus} enum to its {@link String} representation</li>
 *   <li>Correct resolution of denormalized Listing fields ({@code listingTitle}, {@code listingDescription})</li>
 *   <li>Correct conversion of primitive identifiers to Value Objects</li>
 * </ul>
 */

@ExtendWith(MockitoExtension.class)
class AuctionMapperTest {

    @Mock
    private Auction auction;
    @Mock
    private Listing listing;
    @Mock
    private AuctionId auctionId;
    @Mock
    private ListingId listingId;
    @Mock
    private Price initialPrice;
    @Mock
    private Price buyoutPrice;
    @Mock
    private Price finalPrice;

    private final LocalDateTime start = LocalDateTime.of(2026, 5, 1, 10, 0);
    private final LocalDateTime end = LocalDateTime.of(2026, 5, 10, 10, 0);
    private final LocalDateTime closedAt = LocalDateTime.of(2026, 5, 5, 10, 0);

    // =================================================================================================================
    // =========== toDTO ===============================================================================================
    // =================================================================================================================

    @Test
    void shouldMapAllFields_whenAuctionIsClosed() {
        // Arrange
        when(auction.id()).thenReturn(auctionId);
        when(auction.getListingId()).thenReturn(listingId);
        when(auction.getAuctionStartDateTime()).thenReturn(start);
        when(auction.getAuctionEndDateTime()).thenReturn(end);
        when(auction.getClosedAt()).thenReturn(closedAt);
        when(auction.getInitialPrice()).thenReturn(initialPrice);
        when(auction.getBuyoutPrice()).thenReturn(buyoutPrice);
        when(auction.getFinalPrice()).thenReturn(finalPrice);
        when(auction.getAuctionStatus()).thenReturn(AuctionStatus.ENDED_WITH_WINNER);
        when(listing.getListingTitle()).thenReturn("Listing Title");
        when(listing.getShortDescription()).thenReturn("Listing Description");
        when(initialPrice.getPrice()).thenReturn(BigDecimal.valueOf(100));
        when(buyoutPrice.getPrice()).thenReturn(BigDecimal.valueOf(500));
        when(finalPrice.getPrice()).thenReturn(BigDecimal.valueOf(500));

        // Act
        AuctionDTO dto = AuctionMapper.toDTO(auction, listing);

        // Assert
        assertEquals(auctionId.toString(), dto.getAuctionId());
        assertEquals(listingId.toString(), dto.getListingId());
        assertEquals("Listing Title", dto.getListingTitle());
        assertEquals("Listing Description", dto.getListingDescription());
        assertEquals(start, dto.getAuctionStartDateTime());
        assertEquals(end, dto.getAuctionEndDateTime());
        assertEquals(closedAt, dto.getClosedAt());
        assertEquals(BigDecimal.valueOf(100), dto.getInitialPrice());
        assertEquals(BigDecimal.valueOf(500), dto.getBuyoutPrice());
        assertEquals(BigDecimal.valueOf(500), dto.getFinalPrice());
        assertEquals("ENDED_WITH_WINNER", dto.getAuctionStatus());
    }

    @Test
    void shouldMapNullableFields_whenAuctionIsOpen() {
        // Arrange
        when(auction.id()).thenReturn(auctionId);
        when(auction.getListingId()).thenReturn(listingId);
        when(auction.getAuctionStartDateTime()).thenReturn(start);
        when(auction.getAuctionEndDateTime()).thenReturn(end);
        when(auction.getClosedAt()).thenReturn(null);
        when(auction.getInitialPrice()).thenReturn(initialPrice);
        when(auction.getBuyoutPrice()).thenReturn(null);
        when(auction.getFinalPrice()).thenReturn(null);
        when(auction.getAuctionStatus()).thenReturn(AuctionStatus.ACTIVE);
        when(listing.getListingTitle()).thenReturn("Listing Title");
        when(listing.getShortDescription()).thenReturn("Listing Description");
        when(initialPrice.getPrice()).thenReturn(BigDecimal.valueOf(100));

        // Act
        AuctionDTO dto = AuctionMapper.toDTO(auction, listing);

        // Assert
        assertNull(dto.getClosedAt());
        assertNull(dto.getBuyoutPrice());
        assertNull(dto.getFinalPrice());
        assertEquals("ACTIVE", dto.getAuctionStatus());
    }

    @Test
    void shouldMapNullFinalPrice_whenAuctionHasBuyoutButIsStillOpen() {
        // Arrange
        when(auction.id()).thenReturn(auctionId);
        when(auction.getListingId()).thenReturn(listingId);
        when(auction.getAuctionStartDateTime()).thenReturn(start);
        when(auction.getAuctionEndDateTime()).thenReturn(end);
        when(auction.getClosedAt()).thenReturn(null);
        when(auction.getInitialPrice()).thenReturn(initialPrice);
        when(auction.getBuyoutPrice()).thenReturn(buyoutPrice);
        when(auction.getFinalPrice()).thenReturn(null);
        when(auction.getAuctionStatus()).thenReturn(AuctionStatus.ACTIVE);
        when(listing.getListingTitle()).thenReturn("Listing Title");
        when(listing.getShortDescription()).thenReturn("Listing Description");
        when(initialPrice.getPrice()).thenReturn(BigDecimal.valueOf(100));
        when(buyoutPrice.getPrice()).thenReturn(BigDecimal.valueOf(500));

        // Act
        AuctionDTO dto = AuctionMapper.toDTO(auction, listing);

        // Assert
        assertEquals(BigDecimal.valueOf(500), dto.getBuyoutPrice());
        assertNull(dto.getFinalPrice());
    }

    @Test
    void shouldThrow_whenAuctionIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> AuctionMapper.toDTO(null, listing));
    }

    @Test
    void shouldThrow_whenListingIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> AuctionMapper.toDTO(auction, null));
    }

    // =================================================================================================================
    // =========== toDTOs ==============================================================================================
    // =================================================================================================================

    @Test
    void shouldMapAllAuctions_whenAllListingsArePresent() {
        // Arrange
        when(auction.id()).thenReturn(auctionId);
        when(auction.getListingId()).thenReturn(listingId);
        when(auction.getAuctionStartDateTime()).thenReturn(start);
        when(auction.getAuctionEndDateTime()).thenReturn(end);
        when(auction.getClosedAt()).thenReturn(null);
        when(auction.getInitialPrice()).thenReturn(initialPrice);
        when(auction.getBuyoutPrice()).thenReturn(null);
        when(auction.getFinalPrice()).thenReturn(null);
        when(auction.getAuctionStatus()).thenReturn(AuctionStatus.ACTIVE);
        when(listing.getListingTitle()).thenReturn("Listing Title");
        when(listing.getShortDescription()).thenReturn("Listing Description");
        when(initialPrice.getPrice()).thenReturn(BigDecimal.valueOf(100));

        Map<ListingId, Listing> listingMap = Map.of(listingId, listing);

        // Act
        List<AuctionDTO> dtos = AuctionMapper.toDTOs(List.of(auction), listingMap);

        // Assert
        assertEquals(1, dtos.size());
        assertEquals(auctionId.toString(), dtos.get(0).getAuctionId());
    }

    @Test
    void shouldThrow_whenListingNotFoundInMap() {
        // Arrange
        when(auction.getListingId()).thenReturn(listingId);
        Map<ListingId, Listing> emptyMap = Map.of();

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> AuctionMapper.toDTOs(List.of(auction), emptyMap));
    }

    @Test
    void shouldThrow_whenAuctionsListIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> AuctionMapper.toDTOs(null, Map.of()));
    }

    @Test
    void shouldThrow_whenListingMapIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> AuctionMapper.toDTOs(List.of(auction), null));
    }

    @Test
    void shouldReturnEmptyList_whenAuctionsListIsEmpty() {
        // Act
        List<AuctionDTO> dtos = AuctionMapper.toDTOs(List.of(), Map.of());

        // Assert
        assertTrue(dtos.isEmpty());
    }

    // =================================================================================================================
    // =========== toVOs ===============================================================================================
    // =================================================================================================================

    @Test
    void shouldConvertStringToAuctionId() {
        // Act
        AuctionId result = AuctionMapper.toAuctionId("a123");

        // Assert
        assertEquals(new AuctionId("a123"), result);
    }

    @Test
    void shouldConvertStringToListingId() {
        // Act
        ListingId result = AuctionMapper.toListingId("l123");

        // Assert
        assertEquals(new ListingId("l123"), result);
    }
}