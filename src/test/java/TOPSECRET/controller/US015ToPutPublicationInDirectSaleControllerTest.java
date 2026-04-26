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
import TOPSECRET.persistence.mem.ListingRepositoryMem;
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
 *     <li>Delegation to the {@link ListingRepositoryMem} when creating a new listing</li>
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

        when(pubSampleRepoDouble.findByLibraryId(libraryId)).thenReturn(List.of(pubSample1Double, pubSample2Double));

        // Price
        priceDouble = mock(Price.class);

        // Pictures
        Picture pictureDouble = mock(Picture.class);
        picturesDouble = List.of(pictureDouble);
    }

    // ==============================================================
    // ================ CONSTRUCTOR =================================
    // ==============================================================

    @Test
    void constructor_ShouldThrowExceptionWhenLibraryRepoIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US015ToPutPublicationInDirectSaleController(null, pubSampleRepoDouble,
                        listingRepoDouble, mapperDouble));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenPubSampleRepoIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, null,
                        listingRepoDouble, mapperDouble));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenBDRepoIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble,
                        null, mapperDouble));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenMapperIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble,
                        listingRepoDouble, null));
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
        when(pubSampleRepoDouble.findAvailableForSaleByLibraryId(libraryIdDouble)).thenReturn(pubSamples);

        List<US015PublicationSampleDTO> dtoFromMapper = List.of(
                new US015PublicationSampleDTO("title1", List.of("author1"), "1234567890", "BOOK"),
                new US015PublicationSampleDTO("title2", List.of("author2"), "9876543210", "BOOK")
        );

        when(mapperDouble.toDTOs(anyList())).thenReturn(dtoFromMapper);

        US015ToPutPublicationInDirectSaleController controller =
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble,
                        listingRepoDouble, mapperDouble);

        // Act
        List<US015PublicationSampleDTO> listDTOs = controller.findLibraryPublicationsAvailableForSale(userIdDouble);

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

        when(pubSampleRepoDouble.findAvailableForSaleByLibraryId(libraryIdDouble)).thenReturn(List.of());

        when(mapperDouble.toDTOs(List.of())).thenReturn(List.of());

        US015ToPutPublicationInDirectSaleController controller =
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble,
                        listingRepoDouble, mapperDouble);

        // Act
        List<US015PublicationSampleDTO> result =
                controller.findLibraryPublicationsAvailableForSale(userIdDouble);

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
        when(pubSampleRepoDouble.findAvailableForSaleByLibraryId(libraryIdDouble))
                .thenReturn(pubSamples);

        List<US015PublicationSampleDTO> dtoList =
                List.of(new US015PublicationSampleDTO("title", List.of("author"), "code", "BOOK"));
        when(mapperDouble.toDTOs(pubSamples)).thenReturn(dtoList);

        US015ToPutPublicationInDirectSaleController controller =
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble,
                        listingRepoDouble, mapperDouble);

        // Act
        List<US015PublicationSampleDTO> result =
                controller.findLibraryPublicationsAvailableForSale(userIdDouble);

        // Assert
        assertEquals(dtoList, result);
    }


    // ==============================================================
    // ================= putPublicationForSale ======================
    // ==============================================================

    @Test
    void putPublicationForSale_ShouldThrowExceptionWhenTitleIsInvalid() {
        // Arrange
        US015ToPutPublicationInDirectSaleController controller =
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble,
                        listingRepoDouble, mapperDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.putPublicationForSale("   ", List.of(pubSampleId1Double), priceDouble, "desc",
                        userIdDouble, picturesDouble));
    }

    @Test
    void putPublicationForSale_ShouldThrowExceptionWhenPublicationSampleIdsAreNull() {
        // Arrange
        US015ToPutPublicationInDirectSaleController controller =
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble,
                        listingRepoDouble, mapperDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.putPublicationForSale("Valid Title", null, priceDouble, "desc",
                        userIdDouble, picturesDouble));
    }

    @Test
    void putPublicationForSale_ShouldThrowExceptionWhenPriceIsNull() {
        // Arrange
        PublicationSample pubSampleDouble = mock(PublicationSample.class);
        Picture pictureDouble = mock(Picture.class);

        US015ToPutPublicationInDirectSaleController controller =
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble,
                        listingRepoDouble, mapperDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.putPublicationForSale("Valid Title", List.of(pubSampleId1Double), null, "desc",
                        userIdDouble, picturesDouble));
    }

    @Test
    void putPublicationForSale_ShouldThrowExceptionWhenShortDescriptionIsNull() {
        // Arrange
        US015ToPutPublicationInDirectSaleController controller =
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble,
                        listingRepoDouble, mapperDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.putPublicationForSale("Valid Title", List.of(pubSampleId1Double), priceDouble, null,
                        userIdDouble, picturesDouble));
    }

    @Test
    void putPublicationForSale_ShouldThrowExceptionWhenSellerIsNull() {
        // Arrange
        US015ToPutPublicationInDirectSaleController controller =
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble,
                        listingRepoDouble, mapperDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.putPublicationForSale("Valid Title", List.of(pubSampleId1Double), priceDouble,
                        "short description", null, picturesDouble));
    }

    @Test
    void putPublicationForSale_ShouldThrowExceptionWhenPicturesIsNull() {
        // Arrange
        US015ToPutPublicationInDirectSaleController controller =
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble,
                        listingRepoDouble, mapperDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.putPublicationForSale("Valid Title", List.of(pubSampleId1Double), priceDouble,
                        "short description", userIdDouble, null));
    }

    @Test
    void putPublicationForSale_ShouldReturnListingCreatedByListingRepository() throws InstanceAlreadyExistsException {
        // Arrange
        US015ToPutPublicationInDirectSaleController controller =
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble,
                        listingRepoDouble, mapperDouble);

        Listing expectedListing = mock(Listing.class);

        when(listingRepoDouble.createAndAdd("title", List.of(pubSampleId1Double), SaleType.DIRECT_SALE, priceDouble,
                "short description", userIdDouble, picturesDouble)).thenReturn(expectedListing);

        // Act
        Listing result = controller.putPublicationForSale("title", List.of(pubSampleId1Double), priceDouble,
                "short description", userIdDouble, picturesDouble);

        // Assert
        assertNotNull(result);
        assertSame(expectedListing, result);
    }
}