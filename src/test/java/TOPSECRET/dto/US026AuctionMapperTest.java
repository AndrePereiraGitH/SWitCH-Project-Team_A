package TOPSECRET.dto;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Enum.AuctionStatus;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.ValueObjects.AuctionId;
import TOPSECRET.domain.ValueObjects.ListingId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link US026AuctionMapper}.
 */
class US026AuctionMapperTest {

    private IListingRepository listingRepo;
    private US026AuctionMapper mapper;

    private Auction auction1;
    private Auction auction2;

    private Listing listing1;
    private Listing listing2;

    private AuctionId auctionId1;
    private AuctionId auctionId2;

    private ListingId listingId1;
    private ListingId listingId2;

    @BeforeEach
    void setUp() {
        listingRepo = mock(IListingRepository.class);
        mapper = new US026AuctionMapper(listingRepo);

        auction1 = mock(Auction.class);
        auction2 = mock(Auction.class);

        listing1 = mock(Listing.class);
        listing2 = mock(Listing.class);

        auctionId1 = mock(AuctionId.class);
        auctionId2 = mock(AuctionId.class);

        listingId1 = mock(ListingId.class);
        listingId2 = mock(ListingId.class);
    }

    @Test
    void fromDomain_shouldReturnDto_whenListingExists() throws InstanceNotFoundException {
        // Arrange
        when(auction1.getListingId()).thenReturn(listingId1);
        when(auction1.id()).thenReturn(auctionId1);
        when(auction1.getAuctionStatus()).thenReturn(AuctionStatus.ACTIVE);

        when(listingRepo.findListingById(listingId1)).thenReturn(Optional.of(listing1));
        when(listing1.getListingTitle()).thenReturn("Listing Title 1");

        // Act
        US026AuctionDTO result = mapper.fromDomain(auction1);

        // Assert
        assertNotNull(result);
        assertEquals(auctionId1, result.id());
        assertEquals("Listing Title 1", result.listingTitle());
        assertEquals(AuctionStatus.ACTIVE, result.auctionStatus());
    }

    @Test
    void fromDomain_shouldThrowInstanceNotFoundException_whenListingDoesNotExist() {
        // Arrange
        when(auction1.getListingId()).thenReturn(listingId1);
        when(listingRepo.findListingById(listingId1)).thenReturn(Optional.empty());

        // Act + Assert
        InstanceNotFoundException exception = assertThrows(
                InstanceNotFoundException.class,
                () -> mapper.fromDomain(auction1)
        );

        assertEquals("Listing not found", exception.getMessage());
    }

    @Test
    void fromDomainList_shouldReturnDtoList_whenAllListingsExist() throws InstanceNotFoundException {
        // Arrange
        when(auction1.getListingId()).thenReturn(listingId1);
        when(auction1.id()).thenReturn(auctionId1);
        when(auction1.getAuctionStatus()).thenReturn(AuctionStatus.ACTIVE);

        when(auction2.getListingId()).thenReturn(listingId2);
        when(auction2.id()).thenReturn(auctionId2);
        when(auction2.getAuctionStatus()).thenReturn(AuctionStatus.ENDED_WITH_WINNER);

        when(listingRepo.findListingById(listingId1)).thenReturn(Optional.of(listing1));
        when(listingRepo.findListingById(listingId2)).thenReturn(Optional.of(listing2));

        when(listing1.getListingTitle()).thenReturn("Listing Title 1");
        when(listing2.getListingTitle()).thenReturn("Listing Title 2");

        // Act
        List<US026AuctionDTO> result = mapper.fromDomainList(List.of(auction1, auction2));

        // Assert
        assertEquals(2, result.size());

        assertEquals(auctionId1, result.get(0).id());
        assertEquals("Listing Title 1", result.get(0).listingTitle());
        assertEquals(AuctionStatus.ACTIVE, result.get(0).auctionStatus());

        assertEquals(auctionId2, result.get(1).id());
        assertEquals("Listing Title 2", result.get(1).listingTitle());
        assertEquals(AuctionStatus.ENDED_WITH_WINNER, result.get(1).auctionStatus());
    }

    @Test
    void fromDomainList_shouldReturnEmptyList_whenInputListIsEmpty() throws InstanceNotFoundException {
        // Act
        List<US026AuctionDTO> result = mapper.fromDomainList(List.of());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void fromDomainList_shouldThrowInstanceNotFoundException_whenAtLeastOneListingDoesNotExist() {
        // Arrange
        when(auction1.getListingId()).thenReturn(listingId1);
        when(auction1.id()).thenReturn(auctionId1);
        when(auction1.getAuctionStatus()).thenReturn(AuctionStatus.ACTIVE);

        when(auction2.getListingId()).thenReturn(listingId2);

        when(listingRepo.findListingById(listingId1)).thenReturn(Optional.of(listing1));
        when(listingRepo.findListingById(listingId2)).thenReturn(Optional.empty());

        when(listing1.getListingTitle()).thenReturn("Listing Title 1");

        // Act + Assert
        InstanceNotFoundException exception = assertThrows(
                InstanceNotFoundException.class,
                () -> mapper.fromDomainList(List.of(auction1, auction2))
        );

        assertEquals("Listing not found", exception.getMessage());
    }
}