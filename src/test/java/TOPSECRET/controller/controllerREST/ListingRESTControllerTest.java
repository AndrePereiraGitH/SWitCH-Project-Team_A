package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.ListingLinkProvider;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.listing.ListingMapper;
import TOPSECRET.dto.listing.response.ListingResponseDTO;
import TOPSECRET.services.BibliographicalDataService;
import TOPSECRET.services.EditionService;
import TOPSECRET.services.ListingService;
import TOPSECRET.services.PublicationSampleService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.management.InstanceNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListingRESTControllerTest {

    @Mock
    private BibliographicalDataService biblioDataService;

    @Mock
    private PublicationSampleService publicationService;

    @Mock
    private ListingService listingService;

    @Mock
    private EditionService editionService;

    @Mock
    private ListingLinkProvider listingLinkProvider;

    @InjectMocks
    private ListingRESTController controller;

    // ============================================================
    // --------------  findAll  -----------------------------------
    // ============================================================

    @Test
    void findAll_shouldReturnOkWithDtos_whenListingsExist()
            throws InstanceNotFoundException {

        // Arrange
        String userId = "11111111";

        UserId userIdObject = mock(UserId.class);
        Listing listing = mock(Listing.class);
        ListingId listingId = mock(ListingId.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        PublicationSample publicationSample = mock(PublicationSample.class);
        BibliographicalDataId bibliographicalDataId = mock(BibliographicalDataId.class);
        BibliographicalData bibliographicalData = mock(BibliographicalData.class);
        ListingResponseDTO dto = mock(ListingResponseDTO.class);

        when(listingService.findAll()).thenReturn(List.of(listing));
        when(listing.getItemsId()).thenReturn(List.of(publicationSampleId));
        when(publicationService.findById(publicationSampleId)).thenReturn(publicationSample);
        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(biblioDataService.findById(bibliographicalDataId)).thenReturn(bibliographicalData);
        when(listing.id()).thenReturn(listingId);
        when(listingId.getId()).thenReturn("22222222-2222-2222-2222-222222222222");
        when(listingLinkProvider.getLinksForListings(any(UserId.class))).thenReturn(List.of());

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toUserId(userId))
                    .thenReturn(userIdObject);

            mockedMapper.when(() -> ListingMapper.toResponseDTO(
                            eq(listing),
                            eq(List.of(bibliographicalData))
                    ))
                    .thenReturn(dto);

            // Act
            ResponseEntity<List<ListingResponseDTO>> response =
                    controller.findAll(userId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
            assertSame(dto, response.getBody().get(0));
        }
    }

    @Test
    void findAll_shouldReturnOkWithEmptyList_whenNoListingsExist() {

        // Arrange
        String userId = "11111111";

        when(listingService.findAll()).thenReturn(List.of());

        // Act
        ResponseEntity<List<ListingResponseDTO>> response =
                controller.findAll(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void findAll_shouldReturnBadRequest_whenUserIdIsInvalid() {

        // Arrange
        String userId = "";

        // Act
        ResponseEntity<List<ListingResponseDTO>> response =
                controller.findAll(userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void findAll_shouldReturnNotFound_whenListingServiceThrowsEntityNotFoundException() {

        // Arrange
        String userId = "11111111";

        when(listingService.findAll()).thenThrow(new EntityNotFoundException());

        // Act
        ResponseEntity<List<ListingResponseDTO>> response =
                controller.findAll(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void findAll_shouldReturnNotFound_whenBibliographicalDataDoesNotExist()
            throws InstanceNotFoundException {

        // Arrange
        String userId = "11111111";

        Listing listing = mock(Listing.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        PublicationSample publicationSample = mock(PublicationSample.class);
        BibliographicalDataId bibliographicalDataId = mock(BibliographicalDataId.class);

        when(listingService.findAll()).thenReturn(List.of(listing));
        when(listing.getItemsId()).thenReturn(List.of(publicationSampleId));
        when(publicationService.findById(publicationSampleId)).thenReturn(publicationSample);
        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(biblioDataService.findById(bibliographicalDataId))
                .thenThrow(new InstanceNotFoundException());

        // Act
        ResponseEntity<List<ListingResponseDTO>> response =
                controller.findAll(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void findAll_shouldReturnInternalServerError_whenUnexpectedExceptionOccurs() {

        // Arrange
        String userId = "11111111";

        when(listingService.findAll()).thenThrow(new RuntimeException());

        // Act
        ResponseEntity<List<ListingResponseDTO>> response =
                controller.findAll(userId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    // ============================================================
    // --------------  getAllDirectSaleByGenre  -------------------
    // ============================================================

    @Test
    void getAllDirectSaleByGenre_shouldReturnOkWithDtos_whenListingsExist()
            throws InstanceNotFoundException {

        // Arrange
        String genreId = "22222222";
        String userId = "11111111";

        GenreId genreIdObject = mock(GenreId.class);
        UserId userIdObject = mock(UserId.class);

        BibliographicalDataId bibliographicalDataId = mock(BibliographicalDataId.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        Listing listing = mock(Listing.class);
        PublicationSample publicationSample = mock(PublicationSample.class);
        BibliographicalData bibliographicalData = mock(BibliographicalData.class);
        ListingResponseDTO dto = mock(ListingResponseDTO.class);

        when(biblioDataService.findAllByGenreId(any(GenreId.class)))
                .thenReturn(List.of(bibliographicalDataId));

        when(publicationService.findIdsByBibliographicalDataIdIn(List.of(bibliographicalDataId)))
                .thenReturn(List.of(publicationSampleId));

        when(listingService.findAllDirectSaleAndPublicationSampleIdIn(List.of(publicationSampleId)))
                .thenReturn(List.of(listing));

        when(listing.getItemsId()).thenReturn(List.of(publicationSampleId));
        when(publicationService.findById(publicationSampleId)).thenReturn(publicationSample);
        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(biblioDataService.findById(bibliographicalDataId)).thenReturn(bibliographicalData);
        when(listingLinkProvider.getLinksForListings(any(UserId.class))).thenReturn(List.of());

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toGenreId(genreId))
                    .thenReturn(genreIdObject);

            mockedMapper.when(() -> ListingMapper.toUserId(userId))
                    .thenReturn(userIdObject);

            mockedMapper.when(() -> ListingMapper.toResponseDTO(
                            eq(listing),
                            eq(List.of(bibliographicalData))
                    ))
                    .thenReturn(dto);

            // Act
            ResponseEntity<List<ListingResponseDTO>> response =
                    controller.getAllDirectSaleByGenre(genreId, userId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
            assertSame(dto, response.getBody().get(0));
        }
    }

    @Test
    void getAllDirectSaleByGenre_shouldReturnOkWithEmptyList_whenNoListingsExist() {

        // Arrange
        String genreId = "fantasy";
        String userId = "11111111";

        BibliographicalDataId bibliographicalDataId = mock(BibliographicalDataId.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);

        when(biblioDataService.findAllByGenreId(any(GenreId.class)))
                .thenReturn(List.of(bibliographicalDataId));

        when(publicationService.findIdsByBibliographicalDataIdIn(List.of(bibliographicalDataId)))
                .thenReturn(List.of(publicationSampleId));

        when(listingService.findAllDirectSaleAndPublicationSampleIdIn(List.of(publicationSampleId)))
                .thenReturn(List.of());

        // Act
        ResponseEntity<List<ListingResponseDTO>> response =
                controller.getAllDirectSaleByGenre(genreId, userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void getAllDirectSaleByGenre_shouldReturnBadRequest_whenGenreIdIsInvalid() {

        // Arrange
        String genreId = "";
        String userId = "11111111";

        // Act
        ResponseEntity<List<ListingResponseDTO>> response =
                controller.getAllDirectSaleByGenre(genreId, userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getAllDirectSaleByGenre_shouldReturnBadRequest_whenUserIdIsInvalid() {

        // Arrange
        String genreId = "fantasy";
        String userId = "";

        // Act
        ResponseEntity<List<ListingResponseDTO>> response =
                controller.getAllDirectSaleByGenre(genreId, userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getAllDirectSaleByGenre_shouldReturnNotFound_whenBibliographicalDataDoesNotExist()
            throws InstanceNotFoundException {

        // Arrange
        String genreId = "fantasy";
        String userId = "11111111-1111-1111-1111-111111111111";

        BibliographicalDataId bibliographicalDataId = mock(BibliographicalDataId.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        Listing listing = mock(Listing.class);
        PublicationSample publicationSample = mock(PublicationSample.class);

        when(biblioDataService.findAllByGenreId(any(GenreId.class)))
                .thenReturn(List.of(bibliographicalDataId));

        when(publicationService.findIdsByBibliographicalDataIdIn(List.of(bibliographicalDataId)))
                .thenReturn(List.of(publicationSampleId));

        when(listingService.findAllDirectSaleAndPublicationSampleIdIn(List.of(publicationSampleId)))
                .thenReturn(List.of(listing));

        when(listing.getItemsId()).thenReturn(List.of(publicationSampleId));
        when(publicationService.findById(publicationSampleId)).thenReturn(publicationSample);
        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(biblioDataService.findById(bibliographicalDataId))
                .thenThrow(new InstanceNotFoundException());

        // Act
        ResponseEntity<List<ListingResponseDTO>> response =
                controller.getAllDirectSaleByGenre(genreId, userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getAllDirectSaleByGenre_shouldReturnNotFound_whenBiblioDataServiceThrowsEntityNotFoundException() {

        // Arrange
        String genreId = "fantasy";
        String userId = "11111111-1111-1111-1111-111111111111";

        when(biblioDataService.findAllByGenreId(any(GenreId.class)))
                .thenThrow(new EntityNotFoundException());

        // Act
        ResponseEntity<List<ListingResponseDTO>> response =
                controller.getAllDirectSaleByGenre(genreId, userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // ============================================================
    // --------------  getAllDirectSaleByPublisher  ---------------
    // ============================================================

    @Test
    void getAllDirectSaleByPublisher_shouldReturnOkWithDtos_whenListingsExist()
            throws InstanceNotFoundException {

        // Arrange
        String publisherId = "publisher-1";
        String userId = "11111111";

        EditionId editionId = mock(EditionId.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);

        Listing listing = mock(Listing.class);
        ListingId listingId = mock(ListingId.class);
        PublicationSample publicationSample = mock(PublicationSample.class);
        BibliographicalDataId bibliographicalDataId = mock(BibliographicalDataId.class);
        BibliographicalData bibliographicalData = mock(BibliographicalData.class);
        Price price = mock(Price.class);
        Picture picture = mock(Picture.class);

        when(editionService.findIdsByPublisherId(any(PublisherID.class)))
                .thenReturn(List.of(editionId));

        when(publicationService.findIdsByEditionIdIn(List.of(editionId)))
                .thenReturn(List.of(publicationSampleId));

        when(listingService.findAllDirectSaleAndPublicationSampleIdIn(List.of(publicationSampleId)))
                .thenReturn(List.of(listing));

        when(listing.getItemsId()).thenReturn(List.of(publicationSampleId));
        when(publicationService.findById(publicationSampleId)).thenReturn(publicationSample);
        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(biblioDataService.findById(bibliographicalDataId)).thenReturn(bibliographicalData);

        when(listing.id()).thenReturn(listingId);
        when(listingId.getId()).thenReturn("listing-1");

        when(listing.getListingTitle()).thenReturn("Listing title");
        when(listing.getPrice()).thenReturn(price);
        when(price.toString()).thenReturn("10.00");
        when(listing.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(listing.getShortDescription()).thenReturn("Short description");

        UserId sellerId = mock(UserId.class);
        when(listing.getSellerId()).thenReturn(sellerId);
        when(sellerId.getUserId()).thenReturn("seller-1");

        when(picture.getPicture()).thenReturn("pic.jpg");
        when(listing.getPictures()).thenReturn(List.of(picture));
        when(listing.getPublicationDate()).thenReturn(LocalDate.now());

        when(bibliographicalData.getTitle()).thenReturn("Book title");

        when(listingLinkProvider.getLinksForListings(any(UserId.class)))
                .thenReturn(List.of());

        // Act
        ResponseEntity<List<ListingResponseDTO>> response =
                controller.getAllDirectSaleByPublisher(publisherId, userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }
}