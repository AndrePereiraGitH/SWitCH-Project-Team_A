package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.US015PublicationSampleDTO;
import TOPSECRET.dto.US015PublicationSampleMapper;
import TOPSECRET.services.ListingService;
import TOPSECRET.services.PublicationSampleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test suite for the {@link ToPutForSaleAPublicationInDirectSaleController}.
 *
 * <p>This class validates the behaviour of the controller responsible for
 * handling the US015 — placing a publication sample in direct sale.
 *
 * <p>The suite covers:</p>
 * <ul>
 * <li>Constructor validation and defensive programming checks</li>
 * <li>Retrieval and transformation of publication samples into DTOs</li>
 * <li>Error handling when required domain objects or services are missing</li>
 * <li>Filtering logic for available publication samples</li>
 * <li>Delegation to the {@link ListingService} when creating a new listing</li>
 * <li>Validation of input arguments for the direct sale operation</li>
 * </ul>
 */
class ToPutForSaleAPublicationInDirectSaleControllerTest {

    // ==============================================================
    // ====================== Set Up ================================
    // ==============================================================

    private ILibraryRepository libraryRepoDouble;
    private PublicationSampleService pubSampleServiceDouble;
    private US015PublicationSampleMapper mapperDouble;
    private ListingService listingServiceDouble;

    private UserId userIdDouble;
    private Library libraryDouble;
    private PublicationSample pubSample1Double;
    private PublicationSample pubSample2Double;
    private PublicationSampleId pubSampleId1Double;
    private Price priceDouble;
    private List<Picture> picturesDouble;

    @BeforeEach
    void setup() {

        // Repos & Services
        libraryRepoDouble = mock(ILibraryRepository.class);
        pubSampleServiceDouble = mock(PublicationSampleService.class);
        listingServiceDouble = mock(ListingService.class);

        // Mapper
        mapperDouble = mock(US015PublicationSampleMapper.class);

        // User
        userIdDouble = mock(UserId.class);

        // Library
        libraryDouble = mock(Library.class);

        // Publication Samples
        pubSample1Double = mock(PublicationSample.class);
        pubSampleId1Double = mock(PublicationSampleId.class);
        pubSample2Double = mock(PublicationSample.class);

        when(pubSample1Double.isAvailableForSale()).thenReturn(true);
        when(pubSample2Double.isAvailableForSale()).thenReturn(true);

        // Price
        priceDouble = mock(Price.class);

        // Pictures
        Picture pictureDouble = mock(Picture.class);
        picturesDouble = List.of(pictureDouble);
    }

    // ── Helper ────────────────────────────────────────────────────
    private ToPutForSaleAPublicationInDirectSaleController buildController() {
        return new ToPutForSaleAPublicationInDirectSaleController(
                libraryRepoDouble, pubSampleServiceDouble,
                listingServiceDouble, mapperDouble);
    }

    // ==============================================================
    // ================ CONSTRUCTOR =================================
    // ==============================================================

    @Test
    void constructor_ShouldThrowExceptionWhenLibraryRepoIsNull() {
        // Arrange (Handled by @BeforeEach)

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new ToPutForSaleAPublicationInDirectSaleController(null, pubSampleServiceDouble,
                        listingServiceDouble, mapperDouble));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenPubSampleServiceIsNull() {
        // Arrange (Handled by @BeforeEach)

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new ToPutForSaleAPublicationInDirectSaleController(libraryRepoDouble, null,
                        listingServiceDouble, mapperDouble));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenListingServiceIsNull() {
        // Arrange (Handled by @BeforeEach)

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new ToPutForSaleAPublicationInDirectSaleController(libraryRepoDouble, pubSampleServiceDouble,
                        null, mapperDouble));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenMapperIsNull() {
        // Arrange (Handled by @BeforeEach)

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new ToPutForSaleAPublicationInDirectSaleController(libraryRepoDouble, pubSampleServiceDouble,
                        listingServiceDouble, null));
    }


    // ==============================================================
    // ======= findLibraryPublicationsAvailableForSale ==============
    // ==============================================================

    @Test
    void findLibraryPublicationsAvailableForSale_ShouldThrowExceptionWhenUserIdIsNull() {
        // Arrange
        ToPutForSaleAPublicationInDirectSaleController controller = buildController();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.findLibraryPublicationsAvailableForSale(null));
    }

    @Test
    void findLibraryPublicationsAvailableForSale_ShouldReturnDTOsFromMapper() throws InstanceNotFoundException {
        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryDouble.id()).thenReturn(libraryIdDouble);
        when(libraryRepoDouble.findByUserId(userIdDouble)).thenReturn(Optional.of(libraryDouble));

        List<PublicationSample> pubSamples = List.of(pubSample1Double, pubSample2Double);
        when(pubSampleServiceDouble.findByLibraryIdAndPublicationStatusNotIn( userIdDouble))
                .thenReturn(pubSamples);

        List<US015PublicationSampleDTO> dtoFromMapper = List.of(
                new US015PublicationSampleDTO("title1", List.of("author1"), "1234567890", "BOOK"),
                new US015PublicationSampleDTO("title2", List.of("author2"), "9876543210", "BOOK")
        );
        when(mapperDouble.toDTOs(pubSamples)).thenReturn(dtoFromMapper);

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
        when(libraryRepoDouble.findByUserId(userIdDouble)).thenReturn(Optional.of(libraryDouble));

        when(pubSampleServiceDouble.findByLibraryIdAndPublicationStatusNotIn(userIdDouble))
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
    void findLibraryPublicationsAvailableForSale_ShouldReturnOnlySamplesProvidedByService() throws Exception {
        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryDouble.id()).thenReturn(libraryIdDouble);
        when(libraryRepoDouble.findByUserId(userIdDouble)).thenReturn(Optional.of(libraryDouble));

        List<PublicationSample> pubSamples = List.of(pubSample1Double);
        when(pubSampleServiceDouble.findByLibraryIdAndPublicationStatusNotIn(userIdDouble))
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
    // ================= createDirectSaleListing ====================
    // ==============================================================

    @Test
    void createDirectSaleListing_ShouldThrowExceptionWhenTitleIsInvalid() {
        // Arrange
        ToPutForSaleAPublicationInDirectSaleController controller = buildController();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.createDirectSaleListing("   ", List.of(pubSampleId1Double),
                        priceDouble, "desc", userIdDouble, picturesDouble));
    }

    @Test
    void createDirectSaleListing_ShouldThrowExceptionWhenPublicationSampleIdsAreNull() {
        // Arrange
        ToPutForSaleAPublicationInDirectSaleController controller = buildController();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.createDirectSaleListing("Valid Title", null,
                        priceDouble, "desc", userIdDouble, picturesDouble));
    }

    @Test
    void createDirectSaleListing_ShouldThrowExceptionWhenPriceIsNull() {
        // Arrange
        ToPutForSaleAPublicationInDirectSaleController controller = buildController();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.createDirectSaleListing("Valid Title", List.of(pubSampleId1Double),
                        null, "desc", userIdDouble, picturesDouble));
    }

    @Test
    void createDirectSaleListing_ShouldThrowExceptionWhenShortDescriptionIsNull() {
        // Arrange
        ToPutForSaleAPublicationInDirectSaleController controller = buildController();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.createDirectSaleListing("Valid Title", List.of(pubSampleId1Double),
                        priceDouble, null, userIdDouble, picturesDouble));
    }

    @Test
    void createDirectSaleListing_ShouldThrowExceptionWhenSellerIsNull() {
        // Arrange
        ToPutForSaleAPublicationInDirectSaleController controller = buildController();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.createDirectSaleListing("Valid Title", List.of(pubSampleId1Double),
                        priceDouble, "short description", null, picturesDouble));
    }

    @Test
    void createDirectSaleListing_ShouldThrowExceptionWhenPicturesIsNull() {
        // Arrange
        ToPutForSaleAPublicationInDirectSaleController controller = buildController();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.createDirectSaleListing("Valid Title", List.of(pubSampleId1Double),
                        priceDouble, "short description", userIdDouble, null));
    }

    @Test
    void createDirectSaleListing_ShouldReturnListingCreatedByListingService() throws InstanceAlreadyExistsException, AccessDeniedException, InstanceNotFoundException {
        // Arrange
        Listing expectedListing = mock(Listing.class);
        when(listingServiceDouble.createAndSave("title", List.of(pubSampleId1Double), SaleType.DIRECT_SALE,
                priceDouble, "short description", userIdDouble, picturesDouble))
                .thenReturn(expectedListing);

        ToPutForSaleAPublicationInDirectSaleController controller = buildController();

        // Act
        Listing result = controller.createDirectSaleListing("title", List.of(pubSampleId1Double),
                priceDouble, "short description", userIdDouble, picturesDouble);

        // Assert
        assertNotNull(result);
        assertSame(expectedListing, result);
    }
}