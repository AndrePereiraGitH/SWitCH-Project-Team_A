package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.ListingLinkProvider;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.listing.ListingMapper;
import TOPSECRET.dto.listing.request.ListingRequestDTO;
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
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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
    // --------------  createListing  -----------------------------
    // ============================================================

    @Test
    void createListing_shouldReturnCreatedWithDtoAndLinks_whenListingIsCreated()
            throws InstanceNotFoundException {

        // Arrange
        String userId = "userId";
        UserId userId1 = mock(UserId.class);
        ListingRequestDTO request = mock(ListingRequestDTO.class);

        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        PublicationSample publicationSample = mock(PublicationSample.class);
        BibliographicalDataId bibliographicalDataId = mock(BibliographicalDataId.class);
        BibliographicalData bibliographicalData = mock(BibliographicalData.class);

        Listing listing = mock(Listing.class);
        ListingId listingId = mock(ListingId.class);
        ListingResponseDTO dto = mock(ListingResponseDTO.class);

        Picture picture = mock(Picture.class);
        Price price = mock(Price.class);

        Link providerLink = Link.of("/listings", "all");
        List<Link> addedLinks = new ArrayList<>();

        when(request.getPublicationIds()).thenReturn(List.of("pub-1"));
        when(request.getSaleType()).thenReturn("DIRECT_SALE");
        when(request.getPrice()).thenReturn("10.00");
        when(request.getPictures()).thenReturn(List.of("pic.jpg"));
        when(request.getListingTitle()).thenReturn("Listing title");
        when(request.getShortDescription()).thenReturn("Short description");

        when(listingService.createAndSave(
                eq("Listing title"),
                eq(List.of(publicationSampleId)),
                eq(SaleType.DIRECT_SALE),
                eq(price),
                eq("Short description"),
                eq(userId1),
                eq(List.of(picture))
        )).thenReturn(listing);

        when(publicationService.findById(publicationSampleId)).thenReturn(publicationSample);
        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(biblioDataService.findById(bibliographicalDataId)).thenReturn(bibliographicalData);

        when(listing.id()).thenReturn(listingId);
        when(listingId.getId()).thenReturn("listing-1");

        when(listingLinkProvider.getLinksForListings(userId1)).thenReturn(List.of(providerLink));
        when(dto.add(any(Link.class))).thenAnswer(invocation -> {
            addedLinks.add(invocation.getArgument(0));
            return dto;
        });

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toUserId(userId))
                            .thenReturn(userId1);
            mockedMapper.when(() -> ListingMapper.toPublicationId("pub-1"))
                    .thenReturn(publicationSampleId);
            mockedMapper.when(() -> ListingMapper.toSaleType("DIRECT_SALE"))
                    .thenReturn(SaleType.DIRECT_SALE);
            mockedMapper.when(() -> ListingMapper.toPrice("10.00"))
                    .thenReturn(price);
            mockedMapper.when(() -> ListingMapper.toPicture("pic.jpg"))
                    .thenReturn(picture);
            mockedMapper.when(() -> ListingMapper.toResponseDTO(
                            eq(listing),
                            eq(List.of(bibliographicalData))
                    ))
                    .thenReturn(dto);

            // Act
            ResponseEntity<ListingResponseDTO> response =
                    controller.createListing(request, userId);

            // Assert
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertSame(dto, response.getBody());
            assertEquals(2, addedLinks.size());
            assertTrue(addedLinks.stream().anyMatch(link -> link.getRel().value().equals("all")));
            assertTrue(addedLinks.stream().anyMatch(link -> link.getRel().value().equals("self")));
        }
    }

    @Test
    void createListing_shouldReturnForbidden_whenServiceThrowsAccessDeniedException() {

        // Arrange
        String userId = "userId";
        UserId userId1 = mock(UserId.class);

        ListingRequestDTO request = mock(ListingRequestDTO.class);

        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        Price price = mock(Price.class);

        when(request.getPublicationIds()).thenReturn(List.of("pub-1"));
        when(request.getSaleType()).thenReturn("DIRECT_SALE");
        when(request.getPrice()).thenReturn("10.00");
        when(request.getPictures()).thenReturn(List.of());
        when(request.getListingTitle()).thenReturn("Listing title");
        when(request.getShortDescription()).thenReturn("Short description");

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toUserId(userId))
                    .thenReturn(userId1);
            mockedMapper.when(() -> ListingMapper.toPublicationId("pub-1"))
                    .thenReturn(publicationSampleId);
            mockedMapper.when(() -> ListingMapper.toSaleType("DIRECT_SALE"))
                    .thenReturn(SaleType.DIRECT_SALE);
            mockedMapper.when(() -> ListingMapper.toPrice("10.00"))
                    .thenReturn(price);

            when(listingService.createAndSave(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
            )).thenThrow(new AccessDeniedException("denied"));

            // Act
            ResponseEntity<ListingResponseDTO> response =
                    controller.createListing(request, userId);

            // Assert
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    @Test
    void createListing_shouldReturnNotFound_whenPublicationRelatedDataDoesNotExist() {

        // Arrange
        UserId userId1 = mock(UserId.class);
        String userId = "userId";
        ListingRequestDTO request = mock(ListingRequestDTO.class);

        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        Price price = mock(Price.class);
        Listing listing = mock(Listing.class);

        when(request.getPublicationIds()).thenReturn(List.of("pub-1"));
        when(request.getSaleType()).thenReturn("DIRECT_SALE");
        when(request.getPrice()).thenReturn("10.00");
        when(request.getPictures()).thenReturn(List.of());
        when(request.getListingTitle()).thenReturn("Listing title");
        when(request.getShortDescription()).thenReturn("Short description");

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toUserId(userId))
                    .thenReturn(userId1);
            mockedMapper.when(() -> ListingMapper.toPublicationId("pub-1"))
                    .thenReturn(publicationSampleId);
            mockedMapper.when(() -> ListingMapper.toSaleType("DIRECT_SALE"))
                    .thenReturn(SaleType.DIRECT_SALE);
            mockedMapper.when(() -> ListingMapper.toPrice("10.00"))
                    .thenReturn(price);

            when(listingService.createAndSave(any(), any(), any(), any(), any(), any(), any()))
                    .thenReturn(listing);
            when(publicationService.findById(publicationSampleId))
                    .thenThrow(new EntityNotFoundException());

            // Act
            ResponseEntity<ListingResponseDTO> response =
                    controller.createListing(request, userId);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    @Test
    void createListing_shouldReturnBadRequest_whenServiceThrowsIllegalArgumentException() {

        // Arrange
        UserId userId1 = mock(UserId.class);
        String userId = "userId";

        ListingRequestDTO request = mock(ListingRequestDTO.class);

        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        Price price = mock(Price.class);

        when(request.getPublicationIds()).thenReturn(List.of("11111111"));
        when(request.getSaleType()).thenReturn("DIRECT_SALE");
        when(request.getPrice()).thenReturn("10.00");
        when(request.getPictures()).thenReturn(List.of());
        when(request.getListingTitle()).thenReturn("Listing title");
        when(request.getShortDescription()).thenReturn("Short description");

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toUserId(userId)).thenReturn(userId1);
            mockedMapper.when(() -> ListingMapper.toPublicationId("11111111")).thenReturn(publicationSampleId);
            mockedMapper.when(() -> ListingMapper.toSaleType("DIRECT_SALE")).thenReturn(SaleType.DIRECT_SALE);
            mockedMapper.when(() -> ListingMapper.toPrice("10.00")).thenReturn(price);

            when(listingService.createAndSave(any(), any(), any(), any(), any(), any(), any()))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<ListingResponseDTO> response = controller.createListing(request, userId);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    @Test
    void createListing_shouldReturnInternalServerError_whenServiceThrowsUnexpectedException() {

        // Arrange
        UserId userId1 = mock(UserId.class);
        String userId = "userId";

        ListingRequestDTO request = mock(ListingRequestDTO.class);

        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        Price price = mock(Price.class);

        when(request.getPublicationIds()).thenReturn(List.of("11111111"));
        when(request.getSaleType()).thenReturn("DIRECT_SALE");
        when(request.getPrice()).thenReturn("10.00");
        when(request.getPictures()).thenReturn(List.of());
        when(request.getListingTitle()).thenReturn("Listing title");
        when(request.getShortDescription()).thenReturn("Short description");

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toUserId(userId)).thenReturn(userId1);
            mockedMapper.when(() -> ListingMapper.toPublicationId("11111111")).thenReturn(publicationSampleId);
            mockedMapper.when(() -> ListingMapper.toSaleType("DIRECT_SALE")).thenReturn(SaleType.DIRECT_SALE);
            mockedMapper.when(() -> ListingMapper.toPrice("10.00")).thenReturn(price);

            when(listingService.createAndSave(any(), any(), any(), any(), any(), any(), any()))
                    .thenThrow(new RuntimeException());

            // Act
            ResponseEntity<ListingResponseDTO> response = controller.createListing(request, userId);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

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
    // --------------  findById  -----------------------------------
    // ============================================================

    @Test
    void findById_shouldReturnOkWithDto_whenListingExists()
            throws InstanceNotFoundException {

        // Arrange
        String id = "listing-1";
        UserId userId = mock(UserId.class);
        String userId1 = "userid";

        ListingId listingId = mock(ListingId.class);
        Listing listing = mock(Listing.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        PublicationSample publicationSample = mock(PublicationSample.class);
        BibliographicalDataId bibliographicalDataId = mock(BibliographicalDataId.class);
        BibliographicalData bibliographicalData = mock(BibliographicalData.class);
        ListingResponseDTO dto = mock(ListingResponseDTO.class);

        when(listingService.findById(listingId)).thenReturn(listing);
        when(listing.getItemsId()).thenReturn(List.of(publicationSampleId));
        when(publicationService.findById(publicationSampleId)).thenReturn(publicationSample);
        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(biblioDataService.findById(bibliographicalDataId)).thenReturn(bibliographicalData);
        when(listingLinkProvider.getLinksForListings(userId)).thenReturn(List.of());

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toUserId(userId1))
                    .thenReturn(userId);
            mockedMapper.when(() -> ListingMapper.toListingId(id))
                    .thenReturn(listingId);
            mockedMapper.when(() -> ListingMapper.toResponseDTO(
                            eq(listing),
                            eq(List.of(bibliographicalData))
                    ))
                    .thenReturn(dto);

            // Act
            ResponseEntity<ListingResponseDTO> response =
                    controller.findById(id, userId1);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertSame(dto, response.getBody());
        }
    }

    @Test
    void findById_shouldReturnNotFound_whenListingDoesNotExist() {

        // Arrange
        String id = "listing-1";
        UserId userId = mock(UserId.class);
        String userId1 = "userId";
        ListingId listingId = mock(ListingId.class);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toUserId(userId1))
                    .thenReturn(userId);
            mockedMapper.when(() -> ListingMapper.toListingId(id))
                    .thenReturn(listingId);

            when(listingService.findById(listingId))
                    .thenThrow(new EntityNotFoundException());

            // Act
            ResponseEntity<ListingResponseDTO> response =
                    controller.findById(id, userId1);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    @Test
    void findById_shouldThrowIllegalArgumentException_whenListingIdIsInvalid() {

        // Arrange
        String id = "";

        String userId = "userId";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.findById(id, userId)
        );
    }

    @Test
    void findById_shouldReturnInternalServerError_whenUnexpectedExceptionOccurs() {

        // Arrange
        String id = "listing-1";
        UserId userId = mock(UserId.class);
        String userId1 = "userId";
        ListingId listingId = mock(ListingId.class);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toUserId(userId1))
                    .thenReturn(userId);
            mockedMapper.when(() -> ListingMapper.toListingId(id))
                    .thenReturn(listingId);

            when(listingService.findById(listingId))
                    .thenThrow(new RuntimeException());

            // Act
            ResponseEntity<ListingResponseDTO> response =
                    controller.findById(id, userId1);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertNull(response.getBody());
        }
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

        UserId userIdObject = mock(UserId.class);
        PublisherID publisherIdObject = mock(PublisherID.class);

        EditionId editionId = mock(EditionId.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);

        Listing listing = mock(Listing.class);
        PublicationSample publicationSample = mock(PublicationSample.class);
        BibliographicalDataId bibliographicalDataId = mock(BibliographicalDataId.class);
        BibliographicalData bibliographicalData = mock(BibliographicalData.class);
        ListingResponseDTO dto = mock(ListingResponseDTO.class);

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

        when(listingLinkProvider.getLinksForListings(any(UserId.class)))
                .thenReturn(List.of());

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toUserId(userId))
                    .thenReturn(userIdObject);
            mockedMapper.when(() -> ListingMapper.toPublisherId(publisherId))
                    .thenReturn(publisherIdObject);
            mockedMapper.when(() -> ListingMapper.toResponseDTO(
                            eq(listing),
                            eq(List.of(bibliographicalData))
                    ))
                    .thenReturn(dto);

            // Act
            ResponseEntity<List<ListingResponseDTO>> response =
                    controller.getAllDirectSaleByPublisher(publisherId, userId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertSame(dto, response.getBody().get(0));
        }
    }

    @Test
    void getAllDirectSaleByPublisher_shouldReturnBadRequest_whenPublisherIdIsInvalid() {

        // Arrange
        String publisherId = "";
        String userId = "11111111";

        // Act
        ResponseEntity<List<ListingResponseDTO>> response =
                controller.getAllDirectSaleByPublisher(publisherId, userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getAllDirectSaleByPublisher_shouldReturnBadRequest_whenUserIdIsInvalid() {

        // Arrange
        String publisherId = "publisher-1";
        String userId = "";

        // Act
        ResponseEntity<List<ListingResponseDTO>> response =
                controller.getAllDirectSaleByPublisher(publisherId, userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getAllDirectSaleByPublisher_shouldReturnNotFound_whenBibliographicalDataDoesNotExist()
            throws InstanceNotFoundException {

        // Arrange
        String publisherId = "publisher-1";
        String userId = "11111111";

        UserId userIdObject = mock(UserId.class);
        PublisherID publisherIdObject = mock(PublisherID.class);

        EditionId editionId = mock(EditionId.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        Listing listing = mock(Listing.class);
        PublicationSample publicationSample = mock(PublicationSample.class);
        BibliographicalDataId bibliographicalDataId = mock(BibliographicalDataId.class);

        when(editionService.findIdsByPublisherId(any(PublisherID.class)))
                .thenReturn(List.of(editionId));
        when(publicationService.findIdsByEditionIdIn(List.of(editionId)))
                .thenReturn(List.of(publicationSampleId));
        when(listingService.findAllDirectSaleAndPublicationSampleIdIn(List.of(publicationSampleId)))
                .thenReturn(List.of(listing));

        when(listing.getItemsId()).thenReturn(List.of(publicationSampleId));
        when(publicationService.findById(publicationSampleId)).thenReturn(publicationSample);
        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(biblioDataService.findById(bibliographicalDataId))
                .thenThrow(new InstanceNotFoundException());

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toUserId(userId))
                    .thenReturn(userIdObject);
            mockedMapper.when(() -> ListingMapper.toPublisherId(publisherId))
                    .thenReturn(publisherIdObject);

            // Act
            ResponseEntity<List<ListingResponseDTO>> response =
                    controller.getAllDirectSaleByPublisher(publisherId, userId);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNull(response.getBody());
        }
    }
}