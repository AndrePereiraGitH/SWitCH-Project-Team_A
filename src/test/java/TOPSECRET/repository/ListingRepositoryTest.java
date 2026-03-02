package TOPSECRET.repository;

import TOPSECRET.domain.*;
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
    Listing listing1Double;
    Listing listing2Double;
    Listing listing3Double;

    @BeforeEach
    void setUp() {

        listing1Double = mock(Listing.class);               // directSale
        listing2Double = mock(Listing.class);               // directSale
        listing3Double = mock(Listing.class);               // auction

        PublicationSample pubSample1Double = mock(PublicationSample.class);
        PublicationSample pubSample2Double = mock(PublicationSample.class);
        PublicationSample pubSample3Double = mock(PublicationSample.class);

        Price price1Double = mock(Price.class);
        Price price2Double = mock(Price.class);
        Price price3Double = mock(Price.class);

        User seller1Double = mock(User.class);                              // seller of listing 1 and 3
        User seller2Double = mock(User.class);                              // seller of listing 2

        Picture picture1Double = mock(Picture.class);
        Picture picture2Double = mock(Picture.class);
        Picture picture3Double = mock(Picture.class);

        // listing1
        when(listing1Double.getListingTitle()).thenReturn("title1");
        when(listing1Double.getItems()).thenReturn(List.of(pubSample1Double));
        when(listing1Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(listing1Double.getPrice()).thenReturn(price1Double);
        when(price1Double.getPrice()).thenReturn(BigDecimal.valueOf(10));
        when(listing1Double.getShortDescription()).thenReturn("description1");
        when(listing1Double.getSeller()).thenReturn(seller1Double);
        when(seller1Double.getUserName()).thenReturn("seller username1");
        when(listing1Double.getPictures()).thenReturn(List.of(picture1Double));
        when(picture1Double.get_picture()).thenReturn("picture1");

        // listing2
        when(listing2Double.getListingTitle()).thenReturn("title2");
        when(listing2Double.getItems()).thenReturn(List.of(pubSample2Double));
        when(listing2Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(listing2Double.getPrice()).thenReturn(price2Double);
        when(price2Double.getPrice()).thenReturn(BigDecimal.valueOf(5));
        when(listing2Double.getShortDescription()).thenReturn("description2");
        when(listing2Double.getSeller()).thenReturn(seller2Double);
        when(seller2Double.getUserName()).thenReturn("seller username2");
        when(listing2Double.getPictures()).thenReturn(List.of(picture2Double));
        when(picture2Double.get_picture()).thenReturn("picture2");

        // listing3
        when(listing3Double.getListingTitle()).thenReturn("title3");
        when(listing3Double.getItems()).thenReturn(List.of(pubSample3Double));
        when(listing3Double.getSaleType()).thenReturn(SaleType.AUCTION);
        when(listing3Double.getPrice()).thenReturn(price3Double);
        when(price3Double.getPrice()).thenReturn(BigDecimal.valueOf(10.50));
        when(listing3Double.getShortDescription()).thenReturn("description3");
        when(listing3Double.getSeller()).thenReturn(seller1Double);
        when(seller1Double.getUserName()).thenReturn("seller username3");
        when(listing3Double.getPictures()).thenReturn(List.of(picture3Double));
        when(picture3Double.get_picture()).thenReturn("picture3");


        // Repository
        repo = new ListingRepository();
    }

    // ======================================================
    // ================= Tests to add =======================
    // ======================================================

    @Test
    void add_ShouldReturnTrueWhenListingIsNew() {
        // Act
        boolean result = repo.add(listing1Double);

        // Assert
        assertTrue(result);
    }

    @Test
    void add_ShouldStoreListingWhenNew() {
        // Arrange
        repo.add(listing1Double);

        // Act
        List<Listing> listings = repo.getAllListings();

        // Assert
        assertTrue(listings.contains(listing1Double));
    }

    @Test
    void add_ShouldReturnFalseWhenListingAlreadyExists() {
        // Arrange
        repo.add(listing1Double);

        // Act
        boolean result = repo.add(listing1Double);

        // Assert
        assertFalse(result);
    }

    @Test
    void add_ShouldNotAddDuplicateListing() {
        // Arrange
        repo.add(listing1Double);
        repo.add(listing1Double);

        // Act
        List<Listing> allListings = repo.getAllListings();

        assertEquals(1, allListings.size());
    }

    @Test
    void add_ShouldAllowAddingMultipleDifferentListings() {
        repo.add(listing1Double);
        repo.add(listing2Double);

        List<Listing> all = repo.getAllListings();

        assertEquals(2, all.size());
        assertTrue(all.contains(listing1Double));
        assertTrue(all.contains(listing2Double));
    }

    // ======================================================
    // ==== Tests to findAllDirectSaleListings===============
    // ======================================================

    @Test
    void findAllDirectSaleListings_ShouldReturnOnlyDirectSaleListings() {
        // Arrange
        repo.add(listing1Double);
        repo.add(listing2Double);
        repo.add(listing3Double);

        // Act
        List<Listing> dsListings = repo.findAllDirectSaleListings();

        // Assert
        assertEquals(2, repo.findAllDirectSaleListings().size());
        assertEquals(listing1Double, repo.findAllDirectSaleListings().get(0));
        assertEquals(listing2Double, repo.findAllDirectSaleListings().get(1));
    }

    @Test
    void findAllDirectSaleListings_ShouldReturnEmptyListWhenNoDirectSaleListingsExist() {
        // Arrange
        repo.add(listing3Double);

        // Act
        List<Listing> dsListings = repo.findAllDirectSaleListings();

        // Assert
        assertTrue(dsListings.isEmpty());
    }

    @Test
    void findAllDirectSaleListings_ShouldReturnEmptyListWhenRepositoryIsEmpty() {
        // Act
        List<Listing> dsListings = repo.findAllDirectSaleListings();

        assertTrue(dsListings.isEmpty());
    }

    @Test
    void findAllDirectSaleListings_ShouldReturnNewIndependentList() {
        // Arrange
        repo.add(listing1Double);

        // Act
        List<Listing> dsListings1 = repo.findAllDirectSaleListings();
        dsListings1.clear();                                                             // tries to modify

        List<Listing> dsListings2 = repo.findAllDirectSaleListings();

        assertEquals(1, dsListings2.size());
    }

    @Test
    void getListOfItemsOnDirectSaleofAGivenAuthor_ShouldFilterCorrectly() {

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

        repo.add(listingDirectSale);
        repo.add(listingAuction);

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
}