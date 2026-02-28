package TOPSECRET.repository;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuctionRepositoryTest {


    @Test
    void ensure_getListOfItemsOnAuctionByGivenPublication_returnAList() {

        // Arrange
        PublicationSample sample1 = createMockPublicationSample("First title");
        PublicationSample sample2 = createMockPublicationSample("Second title");

        Auction auction1 = createMockAuction(List.of(sample1, sample2));
        Auction auction2 = createMockAuction(List.of(sample1));
        Auction auction3 = createMockAuction(List.of(sample2));

        AuctionRepository auctionRepository = createAuctionRepository(List.of(auction1, auction2, auction3));

        // Act
        List<PublicationSample> listOfItemsOnAuction = auctionRepository.getListOfItemsOnAuctionByGivenPublication("First title");

        // Assert
        assertEquals(2, listOfItemsOnAuction.size());
        assertEquals("First title", listOfItemsOnAuction.get(0).getBibliographicalData().getTitle());
        assertEquals("First title", listOfItemsOnAuction.get(1).getBibliographicalData().getTitle());

    }

    @Test
    void shouldReturnAnEmptyListWhenAuctionDoesNotHaveThePublication(){

        // Arrange
        PublicationSample sample1 = createMockPublicationSample("First title");
        PublicationSample sample2 = createMockPublicationSample("Second title");

        Auction auction1 = createMockAuction(List.of(sample1, sample2));
        Auction auction2 = createMockAuction(List.of(sample1));
        Auction auction3 = createMockAuction(List.of(sample2));

        AuctionRepository auctionRepository = createAuctionRepository(List.of(auction1, auction2, auction3));

        // Act
        List<PublicationSample> listOfItemsOnAuction = auctionRepository.getListOfItemsOnAuctionByGivenPublication("Third title");

        // Assert
        assertEquals(0, listOfItemsOnAuction.size());
        assertTrue(listOfItemsOnAuction.isEmpty());
    }



    // HELPERS

    /**
     * Helper method to create a mock PublicationSample with the given title
     */
    private PublicationSample createMockPublicationSample(String title) {
        BibliographicalData bd = mock(BibliographicalData.class);
        when(bd.getTitle()).thenReturn(title);

        PublicationSample sample = mock(PublicationSample.class);
        when(sample.getBibliographicalData()).thenReturn(bd);

        return sample;
    }

    /**
     * Helper method to create a mock Auction with items from the given listing
     */
    private Auction createMockAuction(List<PublicationSample> items) {
        Listing listing = mock(Listing.class);
        when(listing.getItems()).thenReturn(items);

        Auction auction = mock(Auction.class);
        when(auction.getListing()).thenReturn(listing);

        return auction;
    }

    /**
     * Helper method to create an AuctionRepository with the given auctions
     */
    private AuctionRepository createAuctionRepository(List<Auction> auctions) {
        AuctionFactory auctionFactory = mock(AuctionFactory.class);
        return new AuctionRepository(auctions, auctionFactory);
    }


}