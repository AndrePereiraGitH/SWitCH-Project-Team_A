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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class US026AuctionMapperTest {

    private IListingRepository listingRepo;
    private US026AuctionMapper mapper;

    private Auction auctionDouble;
    private Listing listingDouble;
    private ListingId listingIdDouble;
    private AuctionId auctionIdDouble;

    private Auction auction1Double;
    private Auction auction2Double;

    private Listing listing1Double;
    private Listing listing2Double;

    private ListingId listingId1Double;
    private ListingId listingId2Double;

    private AuctionId auctionId1Double;
    private AuctionId auctionId2Double;

    @BeforeEach
    void setUp() {
        listingRepo = mock(IListingRepository.class);
        mapper = new US026AuctionMapper(listingRepo);

        auctionDouble = mock(Auction.class);
        listingDouble = mock(Listing.class);
        listingIdDouble = mock(ListingId.class);
        auctionIdDouble = mock(AuctionId.class);

        auction1Double = mock(Auction.class);
        auction2Double = mock(Auction.class);

        listing1Double = mock(Listing.class);
        listing2Double = mock(Listing.class);

        listingId1Double = mock(ListingId.class);
        listingId2Double = mock(ListingId.class);

        auctionId1Double = mock(AuctionId.class);
        auctionId2Double = mock(AuctionId.class);
    }

    @Test
    void fromDomain_shouldConvertAuctionToDto() throws InstanceNotFoundException {

        // arrange
        when(auctionDouble.getListingId()).thenReturn(listingIdDouble);
        when(listingRepo.findListingById(listingIdDouble)).thenReturn(Optional.of(listingDouble));
        when(auctionDouble.id()).thenReturn(auctionIdDouble);
        when(listingDouble.getListingTitle()).thenReturn("Lord Of the Rings");
        when(auctionDouble.getAuctionStatus()).thenReturn(AuctionStatus.ACTIVE);

        // act
        US026AuctionDTO result = mapper.fromDomain(auctionDouble);

        // assert
        assertEquals(auctionIdDouble, result.id());
        assertEquals("Lord Of the Rings", result.listingTitle());
        assertEquals(AuctionStatus.ACTIVE, result.auctionStatus());
    }

    @Test
    void fromDomain_shouldThrow_whenListingNotFound() {

        // arrange
        when(auctionDouble.getListingId()).thenReturn(listingIdDouble);
        when(listingRepo.findListingById(listingIdDouble)).thenReturn(Optional.empty());

        // act + assert
        InstanceNotFoundException exception = assertThrows(
                InstanceNotFoundException.class,
                () -> mapper.fromDomain(auctionDouble)
        );

        assertEquals("Listing not found", exception.getMessage());
    }

    @Test
    void fromDomainList_shouldConvertAuctionListToDtoList() throws InstanceNotFoundException {

        // arrange
        when(auction1Double.getListingId()).thenReturn(listingId1Double);
        when(listingRepo.findListingById(listingId1Double)).thenReturn(Optional.of(listing1Double));
        when(auction1Double.id()).thenReturn(auctionId1Double);
        when(listing1Double.getListingTitle()).thenReturn("Lord Of the Rings");
        when(auction1Double.getAuctionStatus()).thenReturn(AuctionStatus.ACTIVE);

        when(auction2Double.getListingId()).thenReturn(listingId2Double);
        when(listingRepo.findListingById(listingId2Double)).thenReturn(Optional.of(listing2Double));
        when(auction2Double.id()).thenReturn(auctionId2Double);
        when(listing2Double.getListingTitle()).thenReturn("Sculpture");
        when(auction2Double.getAuctionStatus()).thenReturn(AuctionStatus.SCHEDULED);

        // act
        List<US026AuctionDTO> result = mapper.fromDomainList(List.of(auction1Double, auction2Double));

        // assert
        assertEquals(2, result.size());

        assertEquals(auctionId1Double, result.get(0).id());
        assertEquals("Lord Of the Rings", result.get(0).listingTitle());
        assertEquals(AuctionStatus.ACTIVE, result.get(0).auctionStatus());

        assertEquals(auctionId2Double, result.get(1).id());
        assertEquals("Sculpture", result.get(1).listingTitle());
        assertEquals(AuctionStatus.SCHEDULED, result.get(1).auctionStatus());
    }

    @Test
    void fromDomainList_shouldReturnEmptyList_WhenAuctionListIsEmpty() throws InstanceNotFoundException {

        // act
        List<US026AuctionDTO> result = mapper.fromDomainList(List.of());

        // assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenOneAuctionInListHasNoListing() {

        // arrange
        when(auction1Double.getListingId()).thenReturn(listingId1Double);
        when(listingRepo.findListingById(listingId1Double)).thenReturn(Optional.of(listing1Double));
        when(auction1Double.id()).thenReturn(auctionId1Double);
        when(listing1Double.getListingTitle()).thenReturn("Painting");
        when(auction1Double.getAuctionStatus()).thenReturn(AuctionStatus.ACTIVE);

        when(auction2Double.getListingId()).thenReturn(listingId2Double);
        when(listingRepo.findListingById(listingId2Double)).thenReturn(Optional.empty());

        // act + assert
        assertThrows(
                InstanceNotFoundException.class,
                () -> mapper.fromDomainList(List.of(auction1Double, auction2Double))
        );
    }
}