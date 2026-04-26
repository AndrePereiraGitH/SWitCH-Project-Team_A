package TOPSECRET.controller;

import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.dto.US024AucListingDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link US024GetTheListOfItemsOnAuctionOfAGivenPublicationController}.
 *
 * <p>All repository dependencies are replaced with Mockito doubles to ensure
 * tests are isolated from infrastructure concerns and focus solely on
 * controller behaviour.</p>
 */
class US024GetTheListOfItemsOnAuctionOfAGivenPublicationControllerTest {

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private IBibliographicalDataRepository bdRepository;
    private IPublicationSampleRepository pubSampleRepository;
    private IListingRepository listingRepository;
    private US024GetTheListOfItemsOnAuctionOfAGivenPublicationController controller;

    // -------------------------------------------------------------------------
    // Setup
    // -------------------------------------------------------------------------

    /**
     * Initialises fresh mock repositories and a new controller instance
     * before each test to guarantee test isolation.
     */
    @BeforeEach
    void setUp() {
        bdRepository = mock(IBibliographicalDataRepository.class);
        pubSampleRepository = mock(IPublicationSampleRepository.class);
        listingRepository = mock(IListingRepository.class);
        controller = new US024GetTheListOfItemsOnAuctionOfAGivenPublicationController(
                bdRepository, pubSampleRepository, listingRepository);
    }


    // -------------------------------------------------------------------------
    // Tests — Constructor
    // -------------------------------------------------------------------------

    @Test
    void constructor_ShouldThrowNullPointerException_WhenBdRepositoryIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                new US024GetTheListOfItemsOnAuctionOfAGivenPublicationController(null, pubSampleRepository, listingRepository)
        );
    }

    @Test
    void constructor_ShouldThrowNullPointerException_WhenPubSampleRepositoryIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                new US024GetTheListOfItemsOnAuctionOfAGivenPublicationController(bdRepository, null, listingRepository)
        );
    }

    @Test
    void constructor_ShouldThrowNullPointerException_WhenListingRepositoryIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                new US024GetTheListOfItemsOnAuctionOfAGivenPublicationController(bdRepository, pubSampleRepository, null)
        );
    }

    @Test
    void constructor_ShouldCreateController_WhenAllRepositoriesAreValid() {
        // Act
        US024GetTheListOfItemsOnAuctionOfAGivenPublicationController controller =
                new US024GetTheListOfItemsOnAuctionOfAGivenPublicationController(bdRepository, pubSampleRepository, listingRepository);

        // Assert
        assertNotNull(controller);
    }

    // -------------------------------------------------------------------------
    // Tests — getAllPublicationTitles
    // -------------------------------------------------------------------------

    @Test
    void getAllPublicationTitles_ShouldReturnListOfTitles_WhenTitlesExist() {
        // Arrange
        when(bdRepository.getAllTitles()).thenReturn(List.of("Title1", "Title2"));

        // Act
        List<String> result = controller.getAllPublicationTitles();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Title1", result.get(0));
        assertEquals("Title2", result.get(1));
    }

    @Test
    void getAllPublicationTitles_ShouldReturnEmptyList_WhenNoTitlesExist() {
        // Arrange
        when(bdRepository.getAllTitles()).thenReturn(List.of());

        // Act
        List<String> result = controller.getAllPublicationTitles();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    // -------------------------------------------------------------------------
    // Tests — getAuctionListingsByTitle
    // -------------------------------------------------------------------------

    @Test
    void getAuctionListingsByTitle_ShouldThrowNullPointerException_WhenTitleIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                controller.getAuctionListingsByTitle(null)
        );
    }

    @Test
    void getAuctionListingsByTitle_ShouldThrowIllegalArgumentException_WhenTitleIsBlank() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.getAuctionListingsByTitle("")
        );
    }

    @Test
    void getAuctionListingsByTitle_ShouldThrowIllegalArgumentException_WhenTitleIsNotFound() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.getAuctionListingsByTitle("Title Not Found")
        );
    }

    @Test
    void getAuctionListingsByTitle_ShouldReturnEmptyList_WhenNoAuctionListingsExist() {
        // Arrange
        BibliographicalDataID bdId = mock(BibliographicalDataID.class);

        PublicationSampleId pubId_1 = mock(PublicationSampleId.class);
        PublicationSampleId pubId_2 = mock(PublicationSampleId.class);
        PublicationSampleId pubId_3 = mock(PublicationSampleId.class);

        when(bdRepository.findBibliographicalDataIdByTitle("Title1"))
                .thenReturn(Optional.of(bdId));
        when(pubSampleRepository.findIdsByBibliographicalDataId(bdId))
                .thenReturn(List.of(pubId_1, pubId_2, pubId_3));
        when(listingRepository.getAuctionListingsContaining(List.of(pubId_1, pubId_2, pubId_3)))
                .thenReturn(List.of());

        // Act
        List<US024AucListingDTO> result = controller.getAuctionListingsByTitle("Title1");

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void getAuctionListingsByTitle_ShouldReturnListOfDTOs_WhenAuctionListingsExist() {
        // Arrange
        BibliographicalDataID bdId = mock(BibliographicalDataID.class);

        PublicationSampleId pubId_1 = mock(PublicationSampleId.class);
        PublicationSampleId pubId_2 = mock(PublicationSampleId.class);
        PublicationSampleId pubId_3 = mock(PublicationSampleId.class);

        Listing listing_1 = createListingDouble("50", "shortDesc", "ListingTitle");
        Listing listing_2 = createListingDouble("50", "shortDesc", "ListingTitle");
        Listing listing_3 = createListingDouble("50", "shortDesc", "ListingTitle");

        when(bdRepository.findBibliographicalDataIdByTitle("Title1"))
                .thenReturn(Optional.of(bdId));
        when(pubSampleRepository.findIdsByBibliographicalDataId(bdId))
                .thenReturn(List.of(pubId_1, pubId_2, pubId_3));
        when(listingRepository.getAuctionListingsContaining(List.of(pubId_1, pubId_2, pubId_3)))
                .thenReturn(List.of(listing_1, listing_2, listing_3));

        // Act
        List<US024AucListingDTO> result = controller.getAuctionListingsByTitle("Title1");

        // Assert
        assertEquals(3, result.size());
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private Listing createListingDouble(String price, String shortDescription, String listingTitle) {
        ListingId listingId = mock(ListingId.class);
        when(listingId.getId()).thenReturn(UUID.randomUUID().toString());

        Price priceValue = mock(Price.class);
        when(priceValue.getPrice()).thenReturn(new BigDecimal(price));

        Listing listingDouble = mock(Listing.class);
        when(listingDouble.id()).thenReturn(listingId);
        when(listingDouble.getPrice()).thenReturn(priceValue);
        when(listingDouble.getShortDescription()).thenReturn(shortDescription);
        when(listingDouble.getListingTitle()).thenReturn(listingTitle);

        return listingDouble;
    }
}