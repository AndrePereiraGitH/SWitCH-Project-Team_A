package TOPSECRET.controller;

import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.US015PublicationSampleDTO;
import TOPSECRET.dto.US015PublicationSampleMapper;
import TOPSECRET.services.ListingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test suite for the {@link US015ToPutPublicationInDirectSaleController}.
 *
 * <p>This class validates the behaviour of the controller responsible for
 * handling the US015 — placing a publication sample in direct sale.
 *
 * <p>The suite covers:</p>
 * <ul>
 *     <li>Constructor validation and defensive programming checks</li>
 *     <li>Retrieval and transformation of publication samples into DTOs</li>
 *     <li>Error handling when required domain objects or repositories are missing</li>
 *     <li>Filtering logic for available publication samples</li>
 *     <li>Delegation to the {@link ListingService} when creating a new listing</li>
 *     <li>Validation of input arguments for the direct sale operation</li>
 * </ul>
 */
class US015ToPutPublicationInDirectSaleControllerTest {

    // ==============================================================
    // ====================== Set Up ================================
    // ==============================================================

    private ILibraryRepository libraryRepoDouble;
    private IPublicationSampleRepository pubSampleRepoDouble;
    private IListingRepository listingRepoDouble;
    private US015PublicationSampleMapper mapperDouble;
    private ListingService listingServiceDouble;

    private UserId userIdDouble;
    private Library libraryDouble;
    private PublicationSample pubSample1Double;
    private PublicationSample pubSample2Double;
    private PublicationSampleId pubSampleId1Double;
    private PublicationSampleId pubSampleId2Double;
    private Price priceDouble;
    private List<Picture> picturesDouble;

    @BeforeEach
    void setup() throws InstanceNotFoundException {

        // Repos
        libraryRepoDouble = mock(ILibraryRepository.class);
        pubSampleRepoDouble = mock(IPublicationSampleRepository.class);
        listingRepoDouble = mock(IListingRepository.class);

        // Mapper
        mapperDouble = mock(US015PublicationSampleMapper.class);

        // Service
        listingServiceDouble = mock(ListingService.class);

        // User
        userIdDouble = mock(UserId.class);

        // Library
        libraryDouble = mock(Library.class);
        LibraryId libraryId = new LibraryId("libId1");
        when(libraryDouble.id()).thenReturn(libraryId);
        when(libraryRepoDouble.getLibraryByUserId(userIdDouble)).thenReturn(libraryDouble);

        // Publication Samples
        pubSample1Double = mock(PublicationSample.class);
        pubSampleId1Double = mock(PublicationSampleId.class);
        pubSample2Double = mock(PublicationSample.class);
        pubSampleId2Double = mock(PublicationSampleId.class);

        when(pubSample1Double.isAvailableForSale()).thenReturn(true);
        when(pubSample2Double.isAvailableForSale()).thenReturn(true);

        when(pubSampleRepoDouble.getPublicationsByLibraryID(libraryId))
                .thenReturn(List.of(pubSample1Double, pubSample2Double));

        // Price
        priceDouble = mock(Price.class);

        // Pictures
        Picture pictureDouble = mock(Picture.class);
        picturesDouble = List.of(pictureDouble);
    }

    // ── Helper ────────────────────────────────────────────────────
    private US015ToPutPublicationInDirectSaleController buildController() {
        return new US015ToPutPublicationInDirectSaleController(
                libraryRepoDouble, pubSampleRepoDouble,
                listingRepoDouble, mapperDouble, listingServiceDouble);
    }

    // ==============================================================
    // ================ CONSTRUCTOR =================================
    // ==============================================================

    @Test
    void constructor_ShouldThrowExceptionWhenLibraryRepoIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new US015ToPutPublicationInDirectSaleController(null, pubSampleRepoDouble,
                        listingRepoDouble, mapperDouble, listingServiceDouble));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenPubSampleRepoIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, null,
                        listingRepoDouble, mapperDouble, listingServiceDouble));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenListingRepoIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble,
                        null, mapperDouble, listingServiceDouble));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenMapperIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble,
                        listingRepoDouble, null, listingServiceDouble));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenListingServiceIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble,
                        listingRepoDouble, mapperDouble, null));
    }

    // ==============================================================
    // ======= findLibraryPublicationsAvailableForSale ==============
    // ==============================================================

    @Test
    void findLibraryPublicationsAvailableForSale_ShouldReturnDTOsFromMapper() throws InstanceNotFoundException {
        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryDouble.id()).thenReturn(libraryIdDouble);
        when(libraryRepoDouble.getLibraryByUserId(userIdDouble)).thenReturn(libraryDouble);

        List<PublicationSample> pubSamples = List.of(pubSample1Double, pubSample2Double);
        when(pubSampleRepoDouble.findPublicationSamplesAvailableForSaleByLibraryId(libraryIdDouble))
                .thenReturn(pubSamples);

        List<US015PublicationSampleDTO> dtoFromMapper = List.of(
                new US015PublicationSampleDTO("title1", List.of("author1"), "1234567890", "BOOK"),
                new US015PublicationSampleDTO("title2", List.of("author2"), "9876543210", "BOOK")
        );
        when(mapperDouble.toDTOs(anyList())).thenReturn(dtoFromMapper);

        // Act
        List<US015PublicationSampleDTO> listDTOs = buildController()
                .findLibraryPublicationsAvailableForSale(userIdDouble);

        // Assert
        assertNotNull(listDTOs);
        assertEquals(dtoFromMapper, listDTOs);
    }

    @Test
    void findLibraryPublicationsAvailableForSale_ShouldReturnEmptyListWhenNoSamplesAvailable() throws Exception {
        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryDouble.id()).thenReturn(libraryIdDouble);
        when(libraryRepoDouble.getLibraryByUserId(userIdDouble)).thenReturn(libraryDouble);

        when(pubSampleRepoDouble.findPublicationSamplesAvailableForSaleByLibraryId(libraryIdDouble))
                .thenReturn(List.of());
        when(mapperDouble.toDTOs(List.of())).thenReturn(List.of());

        // Act
        List<US015PublicationSampleDTO> result = buildController()
                .findLibraryPublicationsAvailableForSale(userIdDouble);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findLibraryPublicationsAvailableForSale_ShouldReturnOnlySamplesProvidedByRepository() throws Exception {
        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryDouble.id()).thenReturn(libraryIdDouble);
        when(libraryRepoDouble.getLibraryByUserId(userIdDouble)).thenReturn(libraryDouble);

        List<PublicationSample> pubSamples = List.of(pubSample1Double);
        when(pubSampleRepoDouble.findPublicationSamplesAvailableForSaleByLibraryId(libraryIdDouble))
                .thenReturn(pubSamples);

        List<US015PublicationSampleDTO> dtoList =
                List.of(new US015PublicationSampleDTO("title", List.of("author"), "code", "BOOK"));
        when(mapperDouble.toDTOs(pubSamples)).thenReturn(dtoList);

        // Act
        List<US015PublicationSampleDTO> result = buildController()
                .findLibraryPublicationsAvailableForSale(userIdDouble);

        // Assert
        assertEquals(dtoList, result);
    }

    // ==============================================================
    // ================= putPublicationForSale ======================
    // ==============================================================

    @Test
    void putPublicationForSale_ShouldThrowExceptionWhenTitleIsInvalid() {
        assertThrows(IllegalArgumentException.class, () ->
                buildController().putPublicationForSale("   ", List.of(pubSampleId1Double),
                        priceDouble, "desc", userIdDouble, picturesDouble));
    }

    @Test
    void putPublicationForSale_ShouldThrowExceptionWhenPublicationSampleIdsAreNull() {
        assertThrows(IllegalArgumentException.class, () ->
                buildController().putPublicationForSale("Valid Title", null,
                        priceDouble, "desc", userIdDouble, picturesDouble));
    }

    @Test
    void putPublicationForSale_ShouldThrowExceptionWhenPriceIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                buildController().putPublicationForSale("Valid Title", List.of(pubSampleId1Double),
                        null, "desc", userIdDouble, picturesDouble));
    }

    @Test
    void putPublicationForSale_ShouldThrowExceptionWhenShortDescriptionIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                buildController().putPublicationForSale("Valid Title", List.of(pubSampleId1Double),
                        priceDouble, null, userIdDouble, picturesDouble));
    }

    @Test
    void putPublicationForSale_ShouldThrowExceptionWhenSellerIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                buildController().putPublicationForSale("Valid Title", List.of(pubSampleId1Double),
                        priceDouble, "short description", null, picturesDouble));
    }

    @Test
    void putPublicationForSale_ShouldThrowExceptionWhenPicturesIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                buildController().putPublicationForSale("Valid Title", List.of(pubSampleId1Double),
                        priceDouble, "short description", userIdDouble, null));
    }

    @Test
    void putPublicationForSale_ShouldReturnListingCreatedByListingService() throws InstanceAlreadyExistsException {
        // Arrange
        Listing expectedListing = mock(Listing.class);
        when(listingServiceDouble.createAndSave("title", List.of(pubSampleId1Double), SaleType.DIRECT_SALE,
                priceDouble, "short description", userIdDouble, picturesDouble))
                .thenReturn(expectedListing);

        // Act
        Listing result = buildController().putPublicationForSale("title", List.of(pubSampleId1Double),
                priceDouble, "short description", userIdDouble, picturesDouble);

        // Assert
        assertNotNull(result);
        assertSame(expectedListing, result);
    }
}