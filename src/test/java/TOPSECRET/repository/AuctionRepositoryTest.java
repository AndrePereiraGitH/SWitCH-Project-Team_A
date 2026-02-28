package TOPSECRET.repository;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link AuctionRepository#getListOfItemsOnAuctionByGivenPublication(String)}.
 *
 * <p>All dependencies are replaced with Mockito doubles to isolate the
 * repository logic from domain construction and persistence concerns.</p>
 */
class AuctionRepositoryTest {

    /**
     * Verifies that {@code getListOfItemsOnAuctionByGivenPublication()} returns
     * only the {@link PublicationSample} instances whose title matches the given
     * publication title, across multiple auctions.
     *
     * <p>Setup: three auctions containing a mix of "First title" and "Second title"
     * samples. Querying for "First title" should return exactly two matches —
     * one from {@code auction1} and one from {@code auction2}.</p>
     */
    @Test
    void ensure_getListOfItemsOnAuctionByGivenPublication_returnAList() {

        // Arrange
        PublicationSample sample1 = createMockPublicationSample("First title");
        PublicationSample sample2 = createMockPublicationSample("Second title");

        Auction auction1 = createMockAuction(List.of(sample1, sample2));
        Auction auction2 = createMockAuction(List.of(sample1));
        Auction auction3 = createMockAuction(List.of(sample2));

        AuctionRepository auctionRepository = createMockAuctionRepository(List.of(auction1, auction2, auction3));

        // Act
        List<PublicationSample> listOfItemsOnAuction =
                auctionRepository.getListOfItemsOnAuctionByGivenPublication("First title");

        // Assert
        assertEquals(2, listOfItemsOnAuction.size());
        assertEquals("First title", listOfItemsOnAuction.get(0).getBibliographicalData().getTitle());
        assertEquals("First title", listOfItemsOnAuction.get(1).getBibliographicalData().getTitle());
    }

    /**
     * Verifies that {@code getListOfItemsOnAuctionByGivenPublication()} returns
     * an empty list when no auction contains a sample matching the given title.
     *
     * <p>Setup: three auctions containing only "First title" and "Second title"
     * samples. Querying for the non-existent "Third title" should yield an
     * empty, non-{@code null} list.</p>
     */
    @Test
    void shouldReturnAnEmptyListWhenAuctionDoesNotHaveThePublication() {

        // Arrange
        PublicationSample sample1 = createMockPublicationSample("First title");
        PublicationSample sample2 = createMockPublicationSample("Second title");

        Auction auction1 = createMockAuction(List.of(sample1, sample2));
        Auction auction2 = createMockAuction(List.of(sample1));
        Auction auction3 = createMockAuction(List.of(sample2));

        AuctionRepository auctionRepository = createMockAuctionRepository(List.of(auction1, auction2, auction3));

        // Act
        List<PublicationSample> listOfItemsOnAuction =
                auctionRepository.getListOfItemsOnAuctionByGivenPublication("Third title");

        // Assert
        assertEquals(0, listOfItemsOnAuction.size());
        assertTrue(listOfItemsOnAuction.isEmpty());
    }

    // -------------------------------------------------------------------------
    // Helper / factory methods
    // -------------------------------------------------------------------------

    /**
     * Creates a mocked {@link PublicationSample} whose bibliographical data
     * returns the given title.
     *
     * @param title the value to be returned by
     *              {@code getBibliographicalData().getTitle()}
     * @return a configured {@link PublicationSample} mock
     */
    private PublicationSample createMockPublicationSample(String title) {
        BibliographicalData bd = mock(BibliographicalData.class);
        when(bd.getTitle()).thenReturn(title);

        PublicationSample sample = mock(PublicationSample.class);
        when(sample.getBibliographicalData()).thenReturn(bd);

        return sample;
    }

    /**
     * Creates a mocked {@link Auction} whose {@link Listing} returns
     * the given list of {@link PublicationSample} items.
     *
     * @param items the items to be returned by {@code getListing().getItems()}
     * @return a configured {@link Auction} mock
     */
    private Auction createMockAuction(List<PublicationSample> items) {
        Listing listing = mock(Listing.class);
        when(listing.getItems()).thenReturn(items);

        Auction auction = mock(Auction.class);
        when(auction.getListing()).thenReturn(listing);

        return auction;
    }

    /**
     * Creates an {@link AuctionRepository} pre-populated with the given
     * list of auctions, using a mocked {@link AuctionFactory}.
     *
     * @param auctions the initial list of {@link Auction} objects to populate
     *                 the repository with
     * @return a fully initialised {@link AuctionRepository}
     */
    private AuctionRepository createMockAuctionRepository(List<Auction> auctions) {
        AuctionFactory auctionFactory = mock(AuctionFactory.class);
        return new AuctionRepository(auctions, auctionFactory);
    }
}