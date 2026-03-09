package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.Enum.SaleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListingRepositoryTest {

    // ======================================================
    // ================== Set Up ============================
    // ======================================================

    ListingRepository repo;
    ListingFactory listingFactoryDouble;

    Listing listing1Double;
    Listing listing2Double;
    Listing listing3Double;

    PublicationSample pub1;
    PublicationSample pub2;
    PublicationSample pub3;

    Price price1;
    Price price2;
    Price price3;

    Picture pic1;
    Picture pic2;
    Picture pic3;

    @BeforeEach
    void setUp() {

        listingFactoryDouble = mock(ListingFactory.class);
        repo = new ListingRepository(listingFactoryDouble);

        listing1Double = mock(Listing.class);
        listing2Double = mock(Listing.class);
        listing3Double = mock(Listing.class);

        pub1 = mock(PublicationSample.class);
        pub2 = mock(PublicationSample.class);
        pub3 = mock(PublicationSample.class);

        price1 = mock(Price.class);
        price2 = mock(Price.class);
        price3 = mock(Price.class);

        pic1 = mock(Picture.class);
        pic2 = mock(Picture.class);
        pic3 = mock(Picture.class);

        when(listing1Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(listing2Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(listing3Double.getSaleType()).thenReturn(SaleType.AUCTION);
    }

    // ======================================================
    // ==== Tests to findAllDirectSaleListings===============
    // ======================================================


    @Test
    void findAllDirectSaleListings_ShouldReturnOnlyDirectSaleListings() throws Exception {

        // Arrange
        when(listingFactoryDouble.create("title1", List.of(pub1), SaleType.DIRECT_SALE, price1, "description1", List.of(pic1)
        )).thenReturn(listing1Double);

        when(listingFactoryDouble.create("title2", List.of(pub2), SaleType.DIRECT_SALE, price2, "description2", List.of(pic2)
        )).thenReturn(listing2Double);

        when(listingFactoryDouble.create("title3", List.of(pub3), SaleType.AUCTION, price3, "description3", List.of(pic3)
        )).thenReturn(listing3Double);

        repo.createAndAdd("title1", List.of(pub1), SaleType.DIRECT_SALE, price1, "description1", List.of(pic1));
        repo.createAndAdd("title2", List.of(pub2), SaleType.DIRECT_SALE, price2, "description2", List.of(pic2));
        repo.createAndAdd("title3", List.of(pub3), SaleType.AUCTION,      price3, "description3", List.of(pic3));

        // Act
        List<Listing> result = repo.findAllDirectSaleListings();

        // Assert
        assertEquals(2, result.size());
        assertEquals(listing1Double, result.get(0));
        assertEquals(listing2Double, result.get(1));
    }

    @Test
    void findAllDirectSaleListings_ShouldReturnEmptyListWhenNoDirectSaleListingsExist() throws Exception {

        // Arrange
        when(listingFactoryDouble.create("title3", List.of(pub3), SaleType.AUCTION, price3, "description3",
                List.of(pic3))).thenReturn(listing3Double);

        when(listing3Double.getSaleType()).thenReturn(SaleType.AUCTION);

        repo.createAndAdd("title3", List.of(pub3), SaleType.AUCTION, price3, "description3", List.of(pic3));

        // Act
        List<Listing> result = repo.findAllDirectSaleListings();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllDirectSaleListings_ShouldReturnEmptyListWhenRepositoryIsEmpty() {
        // Act
        List<Listing> result = repo.findAllDirectSaleListings();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllDirectSaleListings_ShouldReturnNewIndependentList() throws Exception {

        // Arrange
        when(listingFactoryDouble.create("title1", List.of(pub1), SaleType.DIRECT_SALE, price1, "description1",
                List.of(pic1))).thenReturn(listing1Double);

        when(listing1Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);

        repo.createAndAdd("title1", List.of(pub1), SaleType.DIRECT_SALE, price1, "description1", List.of(pic1));

        // Act
        List<Listing> firstCall = repo.findAllDirectSaleListings();
        firstCall.clear(); // tries to modify the list

        List<Listing> secondCall = repo.findAllDirectSaleListings();

        assertEquals(1, secondCall.size());
    }

    @Test
    void getListOfItemsOnDirectSaleOfAGivenAuthor_ShouldFilterCorrectly() throws Exception {

        Author targetAuthor = mock(Author.class);
        PublicationSample sampleWithAuthor = mock(PublicationSample.class);
        PublicationSample sampleWithoutAuthor = mock(PublicationSample.class);

        BibliographicalData bdWithAuthor = mock(BibliographicalData.class);
        when(bdWithAuthor.getAuthors()).thenReturn(List.of(targetAuthor));
        when(sampleWithAuthor.getBibliographicalData()).thenReturn(bdWithAuthor);

        BibliographicalData bdWithoutAuthor = mock(BibliographicalData.class);
        when(bdWithoutAuthor.getAuthors()).thenReturn(List.of(mock(Author.class)));
        when(sampleWithoutAuthor.getBibliographicalData()).thenReturn(bdWithoutAuthor);

        Listing listingDirectSale = mock(Listing.class);
        Listing listingAuction = mock(Listing.class);

        when(listingDirectSale.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(listingDirectSale.getItems()).thenReturn(List.of(sampleWithAuthor, sampleWithoutAuthor));

        when(listingAuction.getSaleType()).thenReturn(SaleType.AUCTION);
        when(listingAuction.getItems()).thenReturn(List.of(sampleWithAuthor));

        when(listingFactoryDouble.create("listing1", List.of(sampleWithAuthor), SaleType.DIRECT_SALE, price1,
                "desc", List.of(pic1))).thenReturn(listingDirectSale);

        when(listingFactoryDouble.create("listing2", List.of(sampleWithAuthor), SaleType.AUCTION, price2,
                "desc", List.of(pic2))).thenReturn(listingAuction);

        repo.createAndAdd("listing1", List.of(sampleWithAuthor), SaleType.DIRECT_SALE, price1, "desc", List.of(pic1));
        repo.createAndAdd("listing2", List.of(sampleWithAuthor), SaleType.AUCTION, price2, "desc", List.of(pic2));

        List<PublicationSample> result = repo.getListOfItemsOnDirectSaleofAGivenAuthor(targetAuthor);

        assertEquals(1, result.size());
        assertTrue(result.contains(sampleWithAuthor));
        assertFalse(result.contains(sampleWithoutAuthor));
    }

    @Test
    void getListOfItemsOnDirectSaleofAGivenAuthor_ShouldThrowOnNullAuthor() {
        assertThrows(IllegalArgumentException.class, () ->
                repo.getListOfItemsOnDirectSaleofAGivenAuthor(null)
        );
    }

    /**
     * Tests that items in non-DIRECT_SALE listings are ignored even if they contain the target author.
     * <p>
     * This ensures that getListOfItemsOnDirectSaleofAGivenAuthor only returns items from DIRECT_SALE listings.
     */
    @Test
    void getListOfItemsOnDirectSaleofAGivenAuthor_ShouldIgnoreNonDirectSaleListings() throws Exception {

        // Arrange
        Author targetAuthor = mock(Author.class);
        PublicationSample sampleWithAuthor = mock(PublicationSample.class);

        BibliographicalData bdWithAuthor = mock(BibliographicalData.class);
        when(bdWithAuthor.getAuthors()).thenReturn(List.of(targetAuthor));
        when(sampleWithAuthor.getBibliographicalData()).thenReturn(bdWithAuthor);

        Listing listingAuction = mock(Listing.class);
        when(listingAuction.getSaleType()).thenReturn(SaleType.AUCTION);
        when(listingAuction.getItems()).thenReturn(List.of(sampleWithAuthor));

        // Add non-DIRECT_SALE listing directly to repository
        repo.getAllListings().add(listingAuction);

        // Act
        List<PublicationSample> result = repo.getListOfItemsOnDirectSaleofAGivenAuthor(targetAuthor);

        // Assert
        assertTrue(result.isEmpty(), "Items in non-DIRECT_SALE listings should be ignored");
    }

    /**
     * Tests that getAllListings returns the complete list of listings stored in the repository.
     *
     * <p>This ensures that the method reflects the repository's internal state
     * and returns all listings that were added via createAndAdd.</p>
     */
    @Test
    void getAllListings_ShouldReturnAllListingsInRepository() throws Exception {

        // Arrange
        when(listingFactoryDouble.create("title1", List.of(pub1), SaleType.DIRECT_SALE, price1,
                "description1", List.of(pic1))).thenReturn(listing1Double);
        when(listingFactoryDouble.create("title2", List.of(pub2), SaleType.AUCTION, price2,
                "description2", List.of(pic2))).thenReturn(listing2Double);

        repo.createAndAdd("title1", List.of(pub1), SaleType.DIRECT_SALE, price1, "description1", List.of(pic1));
        repo.createAndAdd("title2", List.of(pub2), SaleType.AUCTION, price2, "description2", List.of(pic2));

        // Act
        List<Listing> allListings = repo.getAllListings();

        // Assert
        assertEquals(2, allListings.size(), "getAllListings should return all listings added to the repository");
        assertTrue(allListings.contains(listing1Double), "The list should contain listing1Double");
        assertTrue(allListings.contains(listing2Double), "The list should contain listing2Double");
    }
}