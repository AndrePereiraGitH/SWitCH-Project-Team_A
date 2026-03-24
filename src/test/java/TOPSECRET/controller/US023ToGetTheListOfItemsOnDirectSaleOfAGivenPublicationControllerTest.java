package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.ValueObjects.Code;
import TOPSECRET.domain.PublicationType;
import TOPSECRET.dto.BibliographicalDataDTO_US023;
import TOPSECRET.dto.PublicationSampleDTO_US023;
import TOPSECRET.repository.BibliographicalDataRepository;
import TOPSECRET.repository.IListingRepository;
import TOPSECRET.repository.IPublicationSampleRepository;
import TOPSECRET.repository.ListingRepositoryMem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationControllerTest {

    // ===========================================
    // ================= Set Up ==================
    // ===========================================

    private IPublicationSampleRepository pubSampleRepoDouble;
    private PublicationSample pubSample1Double;
    private PublicationSample pubSample2Double;
    private IListingRepository listingRepoDouble;
    private List<Listing> listListingsDouble;
    private Listing listing1Double;
    private Listing listing2Double;
    private BibliographicalDataRepository bdRepoDouble;
    private List<BibliographicalData> listBDDouble;
    private BibliographicalData bd1Double;
    private BibliographicalData bd2Double;

    @BeforeEach
    void setUp() {

        // Mocks
        listingRepoDouble = mock(ListingRepositoryMem.class);
        listing1Double = mock(Listing.class);
        listing2Double = mock(Listing.class);
        listListingsDouble= List.of(listing1Double, listing2Double);

        pubSampleRepoDouble = mock(IPublicationSampleRepository.class);
        pubSample1Double = mock(PublicationSample.class);
        pubSample2Double = mock(PublicationSample.class);

        bdRepoDouble = mock(BibliographicalDataRepository.class);
        bd1Double = mock(BibliographicalData.class);
        bd2Double = mock(BibliographicalData.class);
        listBDDouble= List.of(bd1Double,bd2Double);

        // Publication Sample attributes needed
        Edition edition1Double = mock(Edition.class);
        Edition edition2Double = mock(Edition.class);
        PublicationType pubTypeDouble = mock(PublicationType.class);
        Publisher publisherDouble = mock(Publisher.class);
        User owner1Double = mock(User.class);

        // PublicationSample1
        when(pubSample1Double.getBibliographicalData()).thenReturn(bd1Double);

        when(pubSample1Double.getPublicationStatus()).thenReturn(PublicationStatus.READ);
        when(pubSample1Double.getEdition()).thenReturn(edition1Double);
        when(pubSample1Double.getOwner()).thenReturn(owner1Double);
        when(owner1Double.getUserName()).thenReturn("owner1");
        when(edition1Double.getEditionNumber()).thenReturn("1st");
        when(edition1Double.getCode()).thenReturn(Code.fromIsbn10("1234567890"));
        when(edition1Double.getPublicationType()).thenReturn(pubTypeDouble);
        when(pubTypeDouble.getMediaFormat()).thenReturn(MediaFormat.BOOK);
        when(edition1Double.getEditionLanguage()).thenReturn(Locale.ENGLISH);
        when(edition1Double.getPublisher()).thenReturn(publisherDouble);
        when(publisherDouble.getName()).thenReturn("publisher name");
        when(pubSample1Double.getCondition()).thenReturn(Condition.LIKE_NEW);

        // PublicationSample2 (different bibliographicalData)
        when(pubSample2Double.getBibliographicalData()).thenReturn(bd2Double);

        when(pubSample2Double.getPublicationStatus()).thenReturn(PublicationStatus.READ);
        when(pubSample2Double.getEdition()).thenReturn(edition1Double);
        when(pubSample2Double.getOwner()).thenReturn(owner1Double);
        when(owner1Double.getUserName()).thenReturn("owner1");
        when(edition2Double.getEditionNumber()).thenReturn("1st");
        when(edition2Double.getCode()).thenReturn(Code.fromIsbn10("1234567890"));
        when(edition2Double.getPublicationType()).thenReturn(pubTypeDouble);
        when(pubTypeDouble.getMediaFormat()).thenReturn(MediaFormat.BOOK);
        when(edition2Double.getEditionLanguage()).thenReturn(Locale.ENGLISH);
        when(edition2Double.getPublisher()).thenReturn(publisherDouble);
        when(publisherDouble.getName()).thenReturn("publisher name");
        when(pubSample2Double.getCondition()).thenReturn(Condition.NEW);

        // PublicationSample3 (same bibliographicalData as 1, but different editions)
        when(pubSample2Double.getBibliographicalData()).thenReturn(bd1Double);

        when(pubSample2Double.getPublicationStatus()).thenReturn(PublicationStatus.READ);
        when(pubSample2Double.getEdition()).thenReturn(edition1Double);
        when(pubSample2Double.getOwner()).thenReturn(owner1Double);
        when(owner1Double.getUserName()).thenReturn("owner1");
        when(edition2Double.getEditionNumber()).thenReturn("1st");
        when(edition2Double.getCode()).thenReturn(Code.fromIsbn10("1234567890"));
        when(edition2Double.getPublicationType()).thenReturn(pubTypeDouble);
        when(pubTypeDouble.getMediaFormat()).thenReturn(MediaFormat.BOOK);
        when(edition2Double.getEditionLanguage()).thenReturn(Locale.ENGLISH);
        when(edition2Double.getPublisher()).thenReturn(publisherDouble);
        when(publisherDouble.getName()).thenReturn("publisher name");
        when(pubSample2Double.getCondition()).thenReturn(Condition.NEW);

        // Repositories
        when(listingRepoDouble.findAllDirectSaleListings()).thenReturn(listListingsDouble);
        when(pubSampleRepoDouble.findItemsByListingAndPublication(listListingsDouble,bd1Double)).thenReturn(List.of(pubSample1Double));
        when(bdRepoDouble.getAllPublicationsBD()).thenReturn(listBDDouble);
    }

    // ===========================================
    // ================= Tests ===================
    // ===========================================

    // Tests for getAllPublicationBD -----------------------------------------------------------------------------------
    @Test
    void getAllPublicationBD_ShouldReturnAllPublications () {

        // Arrange
        US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(bdRepoDouble,
                        listingRepoDouble, pubSampleRepoDouble);

        // Act
        List<BibliographicalDataDTO_US023> listOfPublicationsDTO = controller.getAllPublicationBD();

        // Assert
        assertNotNull(listOfPublicationsDTO);
        assertEquals(2, listOfPublicationsDTO.size());
    }

    // Tests for findListOfItemsOnDirectSale ---------------------------------------------------------------------------

    @Test
    void findListOfItemsOnDirectSale_ShouldReturnEmptyListWhenNoItemsFound() {

        // Arrange
        when(pubSampleRepoDouble.findItemsByListingAndPublication(listListingsDouble, bd1Double)).thenReturn(List.of());

        US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(bdRepoDouble,
                        listingRepoDouble, pubSampleRepoDouble);

        // Act
        List<PublicationSampleDTO_US023> itemsListDTO = controller.findListOfItemsOnDirectSale(bd1Double);

        // Assert
        assertNotNull(itemsListDTO);
        assertTrue(itemsListDTO.isEmpty());
    }

    @Test
    void findListOfItemsOnDirectSale_ShouldReturnAllItem_WhenMultipleSamplesExist() {

        // Arrange
        when(pubSampleRepoDouble.findItemsByListingAndPublication(listListingsDouble, bd1Double))
                .thenReturn(List.of(pubSample1Double, pubSample2Double));

        US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(bdRepoDouble,
                        listingRepoDouble, pubSampleRepoDouble);

        // Act
        List<PublicationSampleDTO_US023> itemsListDTO = controller.findListOfItemsOnDirectSale(bd1Double);

        // Assert
        assertNotNull(itemsListDTO);
        assertEquals(2, itemsListDTO.size());
    }

    @Test
    void findListOfItemsOnDirectSale_ShouldMapAttributesCorrectly() {

        // Arrange
        US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(bdRepoDouble,
                        listingRepoDouble, pubSampleRepoDouble);

        // Act
        List<PublicationSampleDTO_US023> itemsListDTO = controller.findListOfItemsOnDirectSale(bd1Double);

        // Assert
        PublicationSampleDTO_US023 dto = itemsListDTO.get(0);
        assertEquals("owner1", dto.getOwnerUsername());
        assertEquals("1st", dto.getEditionNumber());
        assertEquals("publisher name", dto.getPublisherName());
        assertEquals("LIKE_NEW", dto.getCondition());
    }
}