package TOPSECRET.controller;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.dto.US023BibliographicalDataDTO;
import TOPSECRET.dto.US023BibliographicalDataMapper;
import TOPSECRET.dto.US023PublicationSampleDTO;
import TOPSECRET.dto.US023PublicationSampleMapper;
import TOPSECRET.repository.BibliographicalDataRepositoryMem;
import TOPSECRET.repository.ListingRepositoryMem;
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

    // ===========================================
    // ================= Set Up ==================
    // ===========================================

    private IPublicationSampleRepository pubSampleRepoDouble;
    private PublicationSampleId psId1Double;
    private PublicationSampleId psId2Double;

    private IListingRepository listingRepoDouble;
    private Listing listing1Double;
    private Listing listing2Double;

    private IBibliographicalDataRepository bdRepoDouble;
    private BibliographicalData bd1Double;
    private BibliographicalData bd2Double;
    private BibliographicalDataID bdId1Double;
    private BibliographicalDataID bdId2Double;

    private US023BibliographicalDataMapper bdMapperDouble;
    private US023PublicationSampleMapper pubSampleMapperDouble;

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
        when(listing1Double.getItemsId()).thenReturn(List.of(psId1Double));
        listing2Double = mock(Listing.class);
        when(listing2Double.getItemsId()).thenReturn(List.of(psId2Double));

        // Repositories
        bdRepoDouble = mock(BibliographicalDataRepositoryMem.class);
        listingRepoDouble = mock(ListingRepositoryMem.class);
        pubSampleRepoDouble = mock(IPublicationSampleRepository.class);

        // Mappers
        bdMapperDouble = mock(US023BibliographicalDataMapper.class);
        pubSampleMapperDouble = mock(US023PublicationSampleMapper.class);
    }

    // =================================================================================================================
    // ================= CONSTRUCTOR ===================================================================================
    // =================================================================================================================

    @Test
    void constructor_shouldCreateInstance_whenAllDependenciesAreValid() {
        // Act + Assert
        assertDoesNotThrow(() ->
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdRepoDouble, listingRepoDouble, pubSampleRepoDouble, bdMapperDouble, pubSampleMapperDouble));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenBdRepoIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        null, listingRepoDouble, pubSampleRepoDouble, bdMapperDouble, pubSampleMapperDouble));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenListingRepoIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdRepoDouble, null, pubSampleRepoDouble, bdMapperDouble, pubSampleMapperDouble));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenPubSampleRepoIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdRepoDouble, listingRepoDouble, null, bdMapperDouble, pubSampleMapperDouble));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenBdMapperIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdRepoDouble, listingRepoDouble, pubSampleRepoDouble, null, pubSampleMapperDouble));
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
                        listingRepoDouble, pubSampleRepoDouble, bdMapperDouble, pubSampleMapperDouble);

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
                        bdRepoDouble, listingRepoDouble, pubSampleRepoDouble, bdMapperDouble, pubSampleMapperDouble);

        // Act
        List<US023BibliographicalDataDTO> listOfPublicationsDTO = controller.getAllPublicationBD();

        // Assert
        assertTrue(listOfPublicationsDTO.isEmpty());
    }

    // =================================================================================================================
    // ================= FIND LIST OF ITEMS ON DIRECT SALE =============================================================
    // =================================================================================================================

    @Test
    void findListOfItemsOnDirectSale_ShouldReturnEmptyListWhenListingsIsEmpty() throws InstanceNotFoundException {
        // Arrange
        when(listingRepoDouble.findAllDirectSaleListings()).thenReturn(List.of());

        US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(bdRepoDouble,
                        listingRepoDouble, pubSampleRepoDouble, bdMapperDouble, pubSampleMapperDouble);

        // Act
        List<US023PublicationSampleDTO> listOfPubSampleDTO = controller.findListOfItemsOnDirectSale(bdId1Double);

        // Assert
        assertNotNull(listOfPubSampleDTO);
        assertTrue(listOfPubSampleDTO.isEmpty());
    }

    @Test
    void findListOfItemsOnDirectSale_ShouldReturnEmptyListWhenNoItemsFound() throws InstanceNotFoundException {
        // Arrange
        when(listingRepoDouble.findAllDirectSaleListings()).thenReturn(List.of());
        when(pubSampleRepoDouble.findItemsByIdsAndPublication(List.of(), bdId1Double)).thenReturn(List.of());

        US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(bdRepoDouble,
                        listingRepoDouble, pubSampleRepoDouble, bdMapperDouble, pubSampleMapperDouble);

        // Act
        List<US023PublicationSampleDTO> itemsListDTO = controller.findListOfItemsOnDirectSale(bdId1Double);

        // Assert
        assertNotNull(itemsListDTO);
        assertTrue(itemsListDTO.isEmpty());
    }

    @Test
    void findListOfItemsOnDirectSale_ShouldReturnAllItemWhenMultipleSamplesExist() throws InstanceNotFoundException {
        // Arrange
        PublicationSample pubSample1Double = mock(PublicationSample.class);
        PublicationSample pubSample2Double = mock(PublicationSample.class);
        when(pubSample1Double.id()).thenReturn(psId1Double);
        when(pubSample2Double.id()).thenReturn(psId2Double);
        when(pubSample1Double.getBibliographicalDataID()).thenReturn(bdId1Double);
        when(pubSample2Double.getBibliographicalDataID()).thenReturn(bdId1Double);

        when(listingRepoDouble.findAllDirectSaleListings())
                .thenReturn(List.of(listing1Double, listing2Double));

        when(pubSampleRepoDouble.findItemsByIdsAndPublication(List.of(psId1Double, psId2Double), bdId1Double))
                .thenReturn(List.of(pubSample1Double, pubSample2Double));

        US023PublicationSampleDTO dto1 = mock(US023PublicationSampleDTO.class);
        US023PublicationSampleDTO dto2 = mock(US023PublicationSampleDTO.class);
        when(pubSampleMapperDouble.toDTOs(List.of(pubSample1Double, pubSample2Double)))
                .thenReturn(List.of(dto1, dto2));

        US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(bdRepoDouble,
                        listingRepoDouble, pubSampleRepoDouble, bdMapperDouble, pubSampleMapperDouble);

        // Act
        List<US023PublicationSampleDTO> itemsListDTO = controller.findListOfItemsOnDirectSale(bdId1Double);

        // Assert
        assertNotNull(itemsListDTO);
        assertEquals(2, itemsListDTO.size());
    }

    @Test
    void findListOfItemsOnDirectSale_ShouldIgnoreDuplicateIds() throws InstanceNotFoundException {
        // Arrange
        when(listing1Double.getItemsId()).thenReturn(List.of(psId1Double, psId1Double));
        when(listing2Double.getItemsId()).thenReturn(List.of(psId1Double));

        when(listingRepoDouble.findAllDirectSaleListings())
                .thenReturn(List.of(listing1Double, listing2Double));

        PublicationSample pubSample = mock(PublicationSample.class);
        when(pubSampleRepoDouble.findItemsByIdsAndPublication(List.of(psId1Double), bdId1Double))
                .thenReturn(List.of(pubSample));

        US023PublicationSampleDTO dto = mock(US023PublicationSampleDTO.class);
        when(pubSampleMapperDouble.toDTOs(List.of(pubSample)))
                .thenReturn(List.of(dto));

        US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdRepoDouble, listingRepoDouble, pubSampleRepoDouble, bdMapperDouble, pubSampleMapperDouble);

        // Act
        List<US023PublicationSampleDTO> result = controller.findListOfItemsOnDirectSale(bdId1Double);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void findListOfItemsOnDirectSale_ShouldReturnEmptyListWhenMapperReturnsEmpty() throws InstanceNotFoundException {
        // Arrange
        when(listingRepoDouble.findAllDirectSaleListings())
                .thenReturn(List.of(listing1Double));

        PublicationSample pubSample = mock(PublicationSample.class);
        when(pubSampleRepoDouble.findItemsByIdsAndPublication(List.of(psId1Double), bdId1Double))
                .thenReturn(List.of(pubSample));

        when(pubSampleMapperDouble.toDTOs(List.of(pubSample)))
                .thenReturn(List.of());

        US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdRepoDouble, listingRepoDouble, pubSampleRepoDouble, bdMapperDouble, pubSampleMapperDouble);

        // Act
        List<US023PublicationSampleDTO> result = controller.findListOfItemsOnDirectSale(bdId1Double);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findListOfItemsOnDirectSale_ShouldThrowWhenRepoReturnsNull() {
        // Arrange
        when(listingRepoDouble.findAllDirectSaleListings()).thenReturn(null);

        US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdRepoDouble, listingRepoDouble, pubSampleRepoDouble, bdMapperDouble, pubSampleMapperDouble);

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> controller.findListOfItemsOnDirectSale(bdId1Double));
    }

    @Test
    void findListOfItemsOnDirectSale_ShouldThrowWhenListingItemsIdIsNull() {
        // Arrange
        when(listing1Double.getItemsId()).thenReturn(null);
        when(listingRepoDouble.findAllDirectSaleListings())
                .thenReturn(List.of(listing1Double));

        US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdRepoDouble, listingRepoDouble, pubSampleRepoDouble, bdMapperDouble, pubSampleMapperDouble);

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> controller.findListOfItemsOnDirectSale(bdId1Double));
    }

    @Test
    void findListOfItemsOnDirectSale_ShouldReturnEmptyListWhenNoSamplesMatchPublication() throws InstanceNotFoundException {
        // Arrange
        when(listingRepoDouble.findAllDirectSaleListings())
                .thenReturn(List.of(listing1Double, listing2Double));

        when(pubSampleRepoDouble.findItemsByIdsAndPublication(List.of(psId1Double, psId2Double), bdId1Double))
                .thenReturn(List.of());

        when(pubSampleMapperDouble.toDTOs(List.of()))
                .thenReturn(List.of());

        US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new US023ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(
                        bdRepoDouble, listingRepoDouble, pubSampleRepoDouble, bdMapperDouble, pubSampleMapperDouble);

        // Act
        List<US023PublicationSampleDTO> result = controller.findListOfItemsOnDirectSale(bdId1Double);

        // Assert
        assertTrue(result.isEmpty());
    }
}