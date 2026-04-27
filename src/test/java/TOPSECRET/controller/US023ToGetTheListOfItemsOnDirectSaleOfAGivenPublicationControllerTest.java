package TOPSECRET.controller;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.dto.US023BibliographicalDataDTO;
import TOPSECRET.dto.US023BibliographicalDataMapper;
import TOPSECRET.dto.US023ListingDTO;
import TOPSECRET.dto.US023ListingMapper;
import TOPSECRET.persistence.mem.BibliographicalDataRepositoryMem;
import TOPSECRET.persistence.mem.ListingRepositoryMem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController}.
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
class US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationControllerTest {
    private static final Log log = LogFactory.getLog(US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationControllerTest.class);

    // ===========================================
    // ================= Set Up ==================
    // ===========================================

    private IPublicationSampleRepository pubSampleRepoDouble;
    private PublicationSampleId psId1Double;
    private PublicationSampleId psId2Double;

    private IListingRepository listingRepoDouble;
    private Listing listing1Double;
    private Listing listing2Double;
    private ListingId listingId1Double;
    private ListingId listingId2Double;

    private IBibliographicalDataRepository bdRepoDouble;
    private BibliographicalData bd1Double;
    private BibliographicalData bd2Double;
    private BibliographicalDataID bdId1Double;
    private BibliographicalDataID bdId2Double;

    private US023BibliographicalDataMapper bdMapperDouble;
    private US023ListingMapper listingMapperDouble;

    @BeforeEach
    void setUp() {

        // Bibliographical Data
        bd1Double = mock(BibliographicalData.class);
        bdId1Double = mock(BibliographicalDataID.class);
        when(bd1Double.id()).thenReturn(bdId1Double);
        bd2Double = mock(BibliographicalData.class);
        bdId2Double = mock(BibliographicalDataID.class);
        when(bd2Double.id()).thenReturn(bdId2Double);

        // Publication Sample
        PublicationSample pubSample1Double = mock(PublicationSample.class);
        psId1Double = mock(PublicationSampleId.class);
        when(pubSample1Double.id()).thenReturn(psId1Double);
        when(pubSample1Double.getBibliographicalDataID()).thenReturn(bdId1Double);

        PublicationSample pubSample2Double = mock(PublicationSample.class);
        psId2Double = mock(PublicationSampleId.class);
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

        // Repositories
        bdRepoDouble = mock(BibliographicalDataRepositoryMem.class);
        listingRepoDouble = mock(ListingRepositoryMem.class);
        pubSampleRepoDouble = mock(IPublicationSampleRepository.class);

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
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdRepoDouble, listingRepoDouble, pubSampleRepoDouble, bdMapperDouble, listingMapperDouble));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenBdRepoIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        null, listingRepoDouble, pubSampleRepoDouble, bdMapperDouble, listingMapperDouble));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenListingRepoIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdRepoDouble, null, pubSampleRepoDouble, bdMapperDouble, listingMapperDouble));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenPubSampleRepoIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdRepoDouble, listingRepoDouble, null, bdMapperDouble, listingMapperDouble));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenBdMapperIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdRepoDouble, listingRepoDouble, pubSampleRepoDouble, null, listingMapperDouble));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenPubSampleMapperIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdRepoDouble, listingRepoDouble, pubSampleRepoDouble, bdMapperDouble, null));
    }

    // =================================================================================================================
    // ================= GET ALL PUBLICATIONSBD ========================================================================
    // =================================================================================================================

    @Test
    void getAllPublicationBD_ShouldReturnAllPublications () throws InstanceNotFoundException {
        // Arrange
        when(bdRepoDouble.getAllPublicationsBD()).thenReturn(List.of(bd1Double, bd2Double));

        List<US023BibliographicalDataDTO> listDTODouble = List.of(mock(US023BibliographicalDataDTO.class),mock(US023BibliographicalDataDTO.class));
        when(bdMapperDouble.toDTOs(List.of(bd1Double, bd2Double))).thenReturn(listDTODouble);

        US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(bdRepoDouble,
                        listingRepoDouble, pubSampleRepoDouble, bdMapperDouble, listingMapperDouble);

        // Act
        List<US023BibliographicalDataDTO> listOfPublicationsDTO = controller.getAllPublicationBD();

        // Assert
        assertNotNull(listOfPublicationsDTO);
        assertEquals(2, listOfPublicationsDTO.size());
    }

    @Test
    void getAllPublicationBD_ShouldReturnEmptyListWhenRepoIsEmpty() throws InstanceNotFoundException {
        // Arrange
        when(bdRepoDouble.getAllPublicationsBD()).thenReturn(List.of());
        when(bdMapperDouble.toDTOs(List.of())).thenReturn(List.of());

        US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdRepoDouble, listingRepoDouble, pubSampleRepoDouble, bdMapperDouble, listingMapperDouble);

        // Act
        List<US023BibliographicalDataDTO> listOfPublicationsDTO = controller.getAllPublicationBD();

        // Assert
        assertTrue(listOfPublicationsDTO.isEmpty());
    }

    // =================================================================================================================
    // ================= FIND LIST OF ITEMS ON DIRECT SALE =============================================================
    // =================================================================================================================

    @Test
    void findListOfItemsOnDirectSale_ShouldReturnEmptyListWhenNoListingIds() throws Exception {
        // Arrange
        when(pubSampleRepoDouble.findPubSampleIdsByBibliographicalDataId(bdId1Double)).thenReturn(List.of(psId1Double));

        when(listingRepoDouble.findIdsByPublicationSampleIdIn(List.of(psId1Double))).thenReturn(List.of());

        when(listingMapperDouble.toDTOs(List.of())).thenReturn(List.of());

        US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdRepoDouble, listingRepoDouble, pubSampleRepoDouble, bdMapperDouble, listingMapperDouble);

        // Act
        List<US023ListingDTO> result = controller.findListOfItemsOnDirectSale(bdId1Double);

        // Assert
        assertTrue(result.isEmpty());
    }


    @Test
    void findListOfItemsOnDirectSale_ShouldReturnDTOs() throws Exception {
        // Arrange
        when(pubSampleRepoDouble.findPubSampleIdsByBibliographicalDataId(bdId1Double)).thenReturn(List.of(psId1Double));

        when(listingRepoDouble.findIdsByPublicationSampleIdIn(List.of(psId1Double))).thenReturn(List.of(listingId1Double));

        US023ListingDTO dto = mock(US023ListingDTO.class);
        when(listingMapperDouble.toDTOs(List.of(listingId1Double))).thenReturn(List.of(dto));

        US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdRepoDouble, listingRepoDouble, pubSampleRepoDouble, bdMapperDouble, listingMapperDouble);

        // Act
        List<US023ListingDTO> result = controller.findListOfItemsOnDirectSale(bdId1Double);

        // Assert
        assertEquals(1, result.size());
        assertSame(dto, result.get(0));
    }

    @Test
    void findListOfItemsOnDirectSale_ShouldReturnEmptyListWhenMapperReturnsEmpty() throws Exception {
        // Arrange
        when(pubSampleRepoDouble.findPubSampleIdsByBibliographicalDataId(bdId1Double)).thenReturn(List.of(psId1Double));

        when(listingRepoDouble.findIdsByPublicationSampleIdIn(List.of(psId1Double))).thenReturn(List.of(listingId1Double));

        when(listingMapperDouble.toDTOs(List.of(listingId1Double))).thenReturn(List.of());

        US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdRepoDouble, listingRepoDouble, pubSampleRepoDouble, bdMapperDouble, listingMapperDouble);

        // Act
        List<US023ListingDTO> result = controller.findListOfItemsOnDirectSale(bdId1Double);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findListOfItemsOnDirectSale_ShouldThrowWhenBdIdIsNull() {
        // Arrange
        US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdRepoDouble, listingRepoDouble, pubSampleRepoDouble, bdMapperDouble, listingMapperDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.findListOfItemsOnDirectSale(null));
    }
}