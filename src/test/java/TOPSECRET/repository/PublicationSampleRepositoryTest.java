package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class PublicationSampleRepositoryTest {

    // ===========================================================
    // ======================== Set Up ===========================
    // ===========================================================

    Listing listing1Double;
    Listing listing2Double;
    Listing listing3Double;
    PublicationSample pubSample1Double;
    PublicationSample pubSample2Double;
    PublicationSample pubSample3Double;
    PublicationSample pubSample4Double;
    BibliographicalData bd1Double;
    BibliographicalData bd2Double;
    BibliographicalData bd3Double;
    PublicationSampleRepository repo;

    @BeforeEach
    void setUp() {

        listing1Double = mock(Listing.class);                                           // contains pubSample1
        listing2Double = mock(Listing.class);                                           // contains pubSample2
        listing3Double = mock(Listing.class);                                           // contains pubSample3 e 4

        pubSample1Double = mock(PublicationSample.class);                               // contains bd1
        pubSample2Double = mock(PublicationSample.class);                               // contains bd2
        pubSample3Double = mock(PublicationSample.class);                               // contains bd3
        pubSample4Double = mock(PublicationSample.class);                               // contains bd1

        bd1Double = mock(BibliographicalData.class);
        bd2Double = mock(BibliographicalData.class);
        bd3Double = mock(BibliographicalData.class);

        Edition edition1Double = mock(Edition.class);
        Edition edition2Double = mock(Edition.class);

        // listings
        when(listing1Double.getItems()).thenReturn(List.of(pubSample1Double));
        when(listing2Double.getItems()).thenReturn(List.of(pubSample2Double));
        when(listing3Double.getItems()).thenReturn(List.of(pubSample3Double, pubSample4Double));

        // pubSample1
        when(pubSample1Double.getBibliographicalData()).thenReturn(bd1Double);

        // pubSample2
        when(pubSample2Double.getBibliographicalData()).thenReturn(bd2Double);

        // pubSample3
        when(pubSample3Double.getBibliographicalData()).thenReturn(bd3Double);

        // pubSample4
        when(pubSample4Double.getBibliographicalData()).thenReturn(bd1Double);

        // Repository
        repo = new PublicationSampleRepository();
    }


    // ===========================================================
    // ===== Tests for  findItemsByListingAndPublication =========
    // ===========================================================

    @Test
    void findItemsByListingAndPublication_ShouldReturnSamplesMatchingPublication() {
        // Arrange
        List<Listing> listOfListings = List.of(listing1Double, listing2Double, listing3Double);

        // Act
        List<PublicationSample> itemsList = repo.findItemsByListingAndPublication(listOfListings, bd1Double);

        // Assert
        assertEquals(2, itemsList.size());
        assertEquals(pubSample1Double, itemsList.get(0));
        assertEquals(pubSample4Double, itemsList.get(1));
    }

    @Test
    void findItemsByListingAndPublication_ShouldReturnEmptyListWhenNoMatches() {
        // Arrange
        List<Listing> listOfListings = List.of(listing1Double, listing3Double);

        // Act
        List<PublicationSample> itemsList = repo.findItemsByListingAndPublication(listOfListings, bd2Double);

        assertTrue(itemsList.isEmpty());
    }

    @Test
    void findItemsByListingAndPublication_ShouldReturnEmptyListWhenListingsEmpty() {
        // Arrange
        List<Listing> listOfListings = List.of();

        // Act
        List<PublicationSample> itemsList = repo.findItemsByListingAndPublication(listOfListings, bd1Double);

        // Assert
        assertTrue(itemsList.isEmpty());
    }

    @Test
    void findItemsByListingAndPublication_ShouldReturnImmutableList() {
        // Arrange
        List<Listing> listOfListings = List.of(listing1Double);

        // Act
        List<PublicationSample> itemsList = repo.findItemsByListingAndPublication(listOfListings, bd1Double);

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> itemsList.add(pubSample1Double));
    }

    // Test for listings with more than 1 item (listing3)
    @Test
    void findItemsByListingAndPublication_ShouldHandleMultipleSamplesInListing() {
        // Arrange
        List<Listing> listOfListings = List.of(listing3Double);

        // Act
        List<PublicationSample> itemsList = repo.findItemsByListingAndPublication(listOfListings, bd1Double);

        assertEquals(1, itemsList.size());
        assertTrue(itemsList.contains(pubSample4Double));
    }
}