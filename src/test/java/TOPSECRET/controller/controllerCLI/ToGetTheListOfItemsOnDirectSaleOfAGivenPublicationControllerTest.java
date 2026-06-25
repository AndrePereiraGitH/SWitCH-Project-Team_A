package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.dto.US023BibliographicalDataDTO;
import TOPSECRET.dto.US023BibliographicalDataMapper;
import TOPSECRET.dto.US023ListingDTO;
import TOPSECRET.dto.US023ListingMapper;
import TOPSECRET.services.BibliographicalDataService;
import TOPSECRET.services.ListingService;
import TOPSECRET.services.PublicationSampleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController}.
 *
 * <p>
 * This class verifies the behavior of the controller responsible for retrieving
 * bibliographical data and determining which publication samples are available
 * for direct sale for a given publication (US023).
 * </p>
 *
 * <p>
 * The tests focus on:
 * <ul>
 *     <li>Validating constructor dependency checks.</li>
 *     <li>Ensuring correct retrieval and aggregation of publication sample IDs
 *         from direct‑sale listings.</li>
 *     <li>Confirming that the controller interacts with its repositories and
 *         mappers as expected.</li>
 *     <li>Verifying that empty repositories or listings result in an empty DTO list.</li>
 *     <li>Ensuring that domain publication samples are correctly mapped into DTOs.</li>
 * </ul>
 * </p>
 */
class ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationControllerTest {

    // ===========================================
    // ================= Set Up ==================
    // ===========================================

    private PublicationSampleService pubSampleServiceDouble;
    private PublicationSampleId psId1Double;

    private ListingService listingService;
    private Listing listing1Double;
    private Listing listing2Double;
    private ListingId listingId1Double;
    private ListingId listingId2Double;

    private BibliographicalDataService bdServiceDouble;
    private BibliographicalData bd1Double;
    private BibliographicalData bd2Double;
    private BibliographicalDataId bdId1Double;
    private BibliographicalDataId bdId2Double;

    private US023BibliographicalDataMapper bdMapperDouble;
    private US023ListingMapper listingMapperDouble;

    @BeforeEach
    void setUp() {

        // Bibliographical Data
        bd1Double = mock(BibliographicalData.class);
        bdId1Double = mock(BibliographicalDataId.class);
        when(bd1Double.id()).thenReturn(bdId1Double);
        bd2Double = mock(BibliographicalData.class);
        bdId2Double = mock(BibliographicalDataId.class);
        when(bd2Double.id()).thenReturn(bdId2Double);

        // Publication Sample
        PublicationSample pubSample1Double = mock(PublicationSample.class);
        psId1Double = mock(PublicationSampleId.class);
        when(pubSample1Double.id()).thenReturn(psId1Double);
        when(pubSample1Double.getBibliographicalDataID()).thenReturn(bdId1Double);

        PublicationSample pubSample2Double = mock(PublicationSample.class);
        PublicationSampleId psId2Double = mock(PublicationSampleId.class);
        when(pubSample2Double.id()).thenReturn(psId2Double);
        when(pubSample2Double.getBibliographicalDataID()).thenReturn(bdId2Double);

        // Listing
        listing1Double = mock(Listing.class);
        listingId1Double = mock(ListingId.class);
        when(listing1Double.getItemsId()).thenReturn(List.of(psId1Double));
        when(listing1Double.id()).thenReturn(listingId1Double);
        listing2Double = mock(Listing.class);
        listingId2Double = mock(ListingId.class);
        when(listing2Double.getItemsId()).thenReturn(List.of(psId2Double));
        when(listing2Double.id()).thenReturn(listingId2Double);

        // Services
        bdServiceDouble = mock(BibliographicalDataService.class);
        listingService = mock(ListingService.class);
        pubSampleServiceDouble = mock(PublicationSampleService.class);

        // Mappers
        bdMapperDouble = mock(US023BibliographicalDataMapper.class);
        listingMapperDouble = mock(US023ListingMapper.class);
    }

    // =================================================================================================================
    // ================= CONSTRUCTOR ===================================================================================
    // =================================================================================================================

    @Test
    void constructor_shouldCreateInstance_whenAllDependenciesAreValid() {
        // Act + Assert
        assertDoesNotThrow(() ->
                new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdServiceDouble, listingService, pubSampleServiceDouble, bdMapperDouble, listingMapperDouble));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenBdRepoIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        null, listingService, pubSampleServiceDouble, bdMapperDouble, listingMapperDouble));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenListingRepoIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdServiceDouble, null, pubSampleServiceDouble, bdMapperDouble, listingMapperDouble));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenPubSampleRepoIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdServiceDouble, listingService, null, bdMapperDouble, listingMapperDouble));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenBdMapperIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdServiceDouble, listingService, pubSampleServiceDouble, null, listingMapperDouble));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenPubSampleMapperIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdServiceDouble, listingService, pubSampleServiceDouble, bdMapperDouble, null));
    }

    // =================================================================================================================
    // ================= findAllBibliographicalData ====================================================================
    // =================================================================================================================

    @Test
    void findAllBibliographicalData_ShouldReturnAllPublications () throws InstanceNotFoundException {
        // Arrange
        when(bdServiceDouble.findAll()).thenReturn(List.of(bd1Double, bd2Double));

        List<US023BibliographicalDataDTO> listDTODouble = List.of(mock(US023BibliographicalDataDTO.class),mock(US023BibliographicalDataDTO.class));
        when(bdMapperDouble.toDTOs(List.of(bd1Double, bd2Double))).thenReturn(listDTODouble);

        ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(bdServiceDouble,
                        listingService, pubSampleServiceDouble, bdMapperDouble, listingMapperDouble);

        // Act
        List<US023BibliographicalDataDTO> listOfPublicationsDTO = controller.findAllBibliographicalData();

        // Assert
        assertNotNull(listOfPublicationsDTO);
        assertEquals(2, listOfPublicationsDTO.size());
    }

    @Test
    void findAllBibliographicalData_ShouldReturnEmptyListWhenRepoIsEmpty() throws InstanceNotFoundException {
        // Arrange
        when(bdServiceDouble.findAll()).thenReturn(List.of());
        when(bdMapperDouble.toDTOs(List.of())).thenReturn(List.of());

        ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdServiceDouble, listingService, pubSampleServiceDouble, bdMapperDouble, listingMapperDouble);

        // Act
        List<US023BibliographicalDataDTO> listOfPublicationsDTO = controller.findAllBibliographicalData();

        // Assert
        assertTrue(listOfPublicationsDTO.isEmpty());
    }

    // =================================================================================================================
    // ================= FIND LIST OF ITEMS ON DIRECT SALE =============================================================
    // =================================================================================================================

    @Test
    void findListOfItemsOnDirectSale_ShouldReturnEmptyListWhenNoListingIds() throws Exception {
        // Arrange
        when(pubSampleServiceDouble.findIdsByBibliographicalDataIdIn(List.of(bdId1Double))).thenReturn(List.of(psId1Double));

        when(listingService.findAllDirectSaleAndPublicationSampleIdIn(List.of(psId1Double))).thenReturn(List.of());

        when(listingMapperDouble.toDTOs(List.of())).thenReturn(List.of());

        ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdServiceDouble, listingService, pubSampleServiceDouble, bdMapperDouble, listingMapperDouble);

        // Act
        List<US023ListingDTO> result = controller.findListOfItemsOnDirectSale(bdId1Double);

        // Assert
        assertTrue(result.isEmpty());
    }


    @Test
    void findListOfItemsOnDirectSale_ShouldReturnDTOs() throws Exception {
        // Arrange
        when(pubSampleServiceDouble.findIdsByBibliographicalDataIdIn(List.of(bdId1Double))).thenReturn(List.of(psId1Double));

        when(listingService.findAllDirectSaleAndPublicationSampleIdIn(List.of(psId1Double))).thenReturn(List.of(listing1Double));

        US023ListingDTO dto = mock(US023ListingDTO.class);
        when(listingMapperDouble.toDTOs(List.of(listing1Double))).thenReturn(List.of(dto));

        ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdServiceDouble, listingService, pubSampleServiceDouble, bdMapperDouble, listingMapperDouble);

        // Act
        List<US023ListingDTO> result = controller.findListOfItemsOnDirectSale(bdId1Double);

        // Assert
        assertEquals(1, result.size());
        assertSame(dto, result.get(0));
    }

    @Test
    void findListOfItemsOnDirectSale_ShouldReturnEmptyListWhenMapperReturnsEmpty() throws Exception {
        // Arrange
        when(pubSampleServiceDouble.findIdsByBibliographicalDataIdIn(List.of(bdId1Double))).thenReturn(List.of(psId1Double));

        when(listingService.findAllDirectSaleAndPublicationSampleIdIn(List.of(psId1Double))).thenReturn(List.of(listing1Double));

        when(listingMapperDouble.toDTOs(List.of(listing1Double))).thenReturn(List.of());

        ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdServiceDouble, listingService, pubSampleServiceDouble, bdMapperDouble, listingMapperDouble);

        // Act
        List<US023ListingDTO> result = controller.findListOfItemsOnDirectSale(bdId1Double);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findListOfItemsOnDirectSale_ShouldThrowWhenBdIdIsNull() {
        // Arrange
        ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdServiceDouble, listingService, pubSampleServiceDouble, bdMapperDouble, listingMapperDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.findListOfItemsOnDirectSale(null));
    }
}