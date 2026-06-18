package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.ListingLinkProvider;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.listing.ListingMapper;
import TOPSECRET.dto.listing.request.ListingRequestDTO;
import TOPSECRET.dto.listing.response.ListingDetailsDTO;
import TOPSECRET.services.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link ListingRESTController} class.
 * <p>
 * This test suite verifies the REST endpoints for creating and retrieving listings,
 * ensuring proper HTTP status codes, correct response bodies, and appropriate
 * exception handling for scenarios like missing entities or access denial.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class ListingRESTControllerTest {

    // =========================================================================
    // MOCKS & DEPENDENCIES
    // =========================================================================

    @Mock
    private BibliographicalDataService biblioDataService;

    @Mock
    private PublicationSampleService publicationService;

    @Mock
    private ListingService listingService;

    @Mock
    private EditionService editionService;

    @Mock
    private AuthorService authorService;

    @Mock
    private GenreService genreService;

    @Mock
    private PublisherService publisherService;

    @Mock
    private ListingLinkProvider listingLinkProvider;

    @Mock
    private UserService userService;

    @InjectMocks
    private ListingRESTController controller;

    // =========================================================================
    // TESTS: CREATE LISTING (POST)
    // =========================================================================

    @Test
    void createListing_shouldReturnCreatedStatus_whenListingIsCreated() throws Exception {
        // Arrange
        String userId = "user-1";
        ListingRequestDTO request = ValidCreateRequest();
        UserId userIdVO = mock(UserId.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        Price price = mock(Price.class);
        Picture picture = mock(Picture.class);
        Listing listing = fullListingGraphDouble();
        when(listing.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);

        when(listingService.createAndSave(
                eq("Listing title"),
                eq(List.of(publicationSampleId)),
                eq(SaleType.DIRECT_SALE),
                eq(price),
                eq("Short description"),
                eq(userIdVO),
                eq(List.of(picture))
        )).thenReturn(listing);

        links(dto, userIdVO);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            createListingMapper(mockedMapper, userId, userIdVO, publicationSampleId, price, picture);
            toDetailsDTODouble(mockedMapper, listing, dto);

            // Act
            ResponseEntity<ListingDetailsDTO> response = controller.createListing(request, userId);

            // Assert
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }
    }

    @Test
    void createListing_shouldReturnCreatedBody_whenListingIsCreated() throws Exception {
        // Arrange
        String userId = "user-1";
        ListingRequestDTO request = ValidCreateRequest();
        UserId userIdVO = mock(UserId.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        Price price = mock(Price.class);
        Picture picture = mock(Picture.class);
        Listing listing = fullListingGraphDouble();
        when(listing.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);

        when(listingService.createAndSave(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(listing);

        links(dto, userIdVO);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            createListingMapper(mockedMapper, userId, userIdVO, publicationSampleId, price, picture);
            toDetailsDTODouble(mockedMapper, listing, dto);

            // Act
            ResponseEntity<ListingDetailsDTO> response = controller.createListing(request, userId);

            // Assert
            assertSame(dto, response.getBody());
        }
    }

    @Test
    void createListing_shouldAddAuctionLinks_whenSaleTypeIsAuction() throws Exception {
        // Arrange
        String userId = "user-1";
        ListingRequestDTO request = ValidCreateRequest();
        UserId userIdVO = mock(UserId.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        Price price = mock(Price.class);
        Picture picture = mock(Picture.class);
        Listing listing = fullListingGraphDouble();

        when(listing.getSaleType()).thenReturn(SaleType.AUCTION);

        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);

        when(listingService.createAndSave(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(listing);

        when(listingLinkProvider.getPublicLinks()).thenReturn(List.of());
        when(listingLinkProvider.getAuthenticatedUserLinks(userIdVO)).thenReturn(List.of());
        when(listingLinkProvider.getAuctionLinks(userIdVO)).thenReturn(List.of(Link.of("/auction")));
        when(dto.add(any(Link.class))).thenReturn(dto);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            createListingMapper(mockedMapper, userId, userIdVO, publicationSampleId, price, picture);
            toDetailsDTODouble(mockedMapper, listing, dto);

            // Act
            ResponseEntity<ListingDetailsDTO> response = controller.createListing(request, userId);

            // Assert
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            verify(listingLinkProvider).getAuctionLinks(userIdVO); // Kills the surviving AUCTION conditional
        }
    }

    @Test
    void createListing_shouldReturnBadRequest_whenPublicationIdsAreNull() {
        // Arrange
        ListingRequestDTO request = mock(ListingRequestDTO.class);
        when(request.getPublicationIds()).thenReturn(null);

        // Act
        ResponseEntity<ListingDetailsDTO> response = controller.createListing(request, "user-1");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createListing_shouldReturnBadRequest_whenPublicationIdsAreEmpty() {
        // Arrange
        ListingRequestDTO request = mock(ListingRequestDTO.class);
        when(request.getPublicationIds()).thenReturn(List.of());

        // Act
        ResponseEntity<ListingDetailsDTO> response = controller.createListing(request, "user-1");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createListing_shouldReturnForbidden_whenServiceThrowsAccessDeniedException() {
        // Arrange
        String userId = "user-1";
        ListingRequestDTO request = validCreateRequestWithoutPictures();
        UserId userIdVO = mock(UserId.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        Price price = mock(Price.class);

        when(listingService.createAndSave(any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new AccessDeniedException("denied"));

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            createListingMapperWithoutPicture(mockedMapper, userId, userIdVO, publicationSampleId, price);

            // Act
            ResponseEntity<ListingDetailsDTO> response = controller.createListing(request, userId);

            // Assert
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }
    }

    @Test
    void createListing_shouldReturnBadRequest_whenMapperThrowsIllegalArgumentException() {
        // Arrange
        ListingRequestDTO request = mock(ListingRequestDTO.class);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toUserId("invalid-user"))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<ListingDetailsDTO> response = controller.createListing(request, "invalid-user");

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void createListing_shouldReturnNotFound_whenRelatedDataDoesNotExist() {
        // Arrange
        String userId = "user-1";
        ListingRequestDTO request = validCreateRequestWithoutPictures();
        UserId userIdVO = mock(UserId.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        Price price = mock(Price.class);
        Listing listing = mock(Listing.class);

        when(listingService.createAndSave(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(listing);
        when(listing.getItemsId()).thenReturn(List.of(publicationSampleId));
        when(publicationService.findById(publicationSampleId))
                .thenThrow(new EntityNotFoundException());

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            createListingMapperWithoutPicture(mockedMapper, userId, userIdVO, publicationSampleId, price);

            // Act
            ResponseEntity<ListingDetailsDTO> response = controller.createListing(request, userId);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Test
    void createListing_shouldReturnInternalServerError_whenUnexpectedExceptionOccurs() {
        // Arrange
        ListingRequestDTO request = mock(ListingRequestDTO.class);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toUserId("user-1"))
                    .thenThrow(new RuntimeException());

            // Act
            ResponseEntity<ListingDetailsDTO> response = controller.createListing(request, "user-1");

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    // =========================================================================
    // TESTS: FIND BY ID (GET)
    // =========================================================================

    @Test
    void findById_shouldReturnOkStatus_whenListingExists() throws Exception {
        // Arrange
        ListingId listingId = mock(ListingId.class);
        Listing listing = fullListingGraphDouble();
        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);

        when(listingService.findById(listingId)).thenReturn(listing);
        publicLinks(dto);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toListingId("listing-1")).thenReturn(listingId);
            toDetailsDTODouble(mockedMapper, listing, dto);

            // Act
            ResponseEntity<ListingDetailsDTO> response = controller.findById("listing-1");

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Test
    void findById_shouldReturnDtoBody_whenListingExists() throws Exception {
        // Arrange
        ListingId listingId = mock(ListingId.class);
        Listing listing = fullListingGraphDouble();
        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);

        when(listingService.findById(listingId)).thenReturn(listing);
        publicLinks(dto);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toListingId("listing-1")).thenReturn(listingId);
            toDetailsDTODouble(mockedMapper, listing, dto);

            // Act
            ResponseEntity<ListingDetailsDTO> response = controller.findById("listing-1");

            // Assert
            assertSame(dto, response.getBody());
        }
    }

    @Test
    void findById_shouldReturnBadRequest_whenIdIsInvalid() {
        // Arrange
        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toListingId(""))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<ListingDetailsDTO> response = controller.findById("");

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void findById_shouldReturnNotFound_whenListingDoesNotExist() {
        // Arrange
        ListingId listingId = mock(ListingId.class);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toListingId("listing-1")).thenReturn(listingId);
            when(listingService.findById(listingId)).thenThrow(new EntityNotFoundException());

            // Act
            ResponseEntity<ListingDetailsDTO> response = controller.findById("listing-1");

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Test
    void findById_shouldReturnInternalServerError_whenUnexpectedExceptionOccurs() {
        // Arrange
        ListingId listingId = mock(ListingId.class);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toListingId("listing-1")).thenReturn(listingId);
            when(listingService.findById(listingId)).thenThrow(new RuntimeException());

            // Act
            ResponseEntity<ListingDetailsDTO> response = controller.findById("listing-1");

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    @Test
    void findById_shouldMapPublisher() throws Exception {
        // Arrange
        ListingId listingId = mock(ListingId.class);
        Listing listing = fullListingGraphDouble();
        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);

        when(listingService.findById(listingId)).thenReturn(listing);

        try (MockedStatic<ListingMapper> mapper = mockStatic(ListingMapper.class)) {
            mapper.when(() -> ListingMapper.toListingId("listing-1")).thenReturn(listingId);
            mapper.when(() -> ListingMapper.toListingDetailsDTO(
                    eq(listing),
                    anyList(),
                    anyList(),
                    anyList(),
                    anyList(),
                    anyList(),
                    argThat(list -> !list.isEmpty()), // 7th arg is publishers
                    any()
            )).thenReturn(dto);

            // Act
            ResponseEntity<ListingDetailsDTO> response = controller.findById("listing-1");

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    // =========================================================================
    // TESTS: FIND ALL (GET)
    // =========================================================================

    @Test
    void findAll_shouldReturnOkStatus_whenListingsExist() throws Exception {
        // Arrange
        Listing listing = fullListingGraphDouble();
        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);

        when(listingService.findAllFiltered(null, null, null, null, null)).thenReturn(List.of(listing));
        publicLinks(dto);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            toDetailsDTODouble(mockedMapper, listing, dto);

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response = controller.findAll(null, null, null, null, null);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Test
    void findAll_shouldReturnOneDto_whenListingsExist() throws Exception {
        // Arrange
        Listing listing = fullListingGraphDouble();
        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);

        when(listingService.findAllFiltered(null, null, null, null, null)).thenReturn(List.of(listing));
        publicLinks(dto);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            toDetailsDTODouble(mockedMapper, listing, dto);

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response = controller.findAll(null, null, null, null, null);

            // Assert
            assertEquals(1, response.getBody().size());
        }
    }

    @Test
    void findAll_shouldReturnExpectedDto_whenListingsExist() throws Exception {
        // Arrange
        Listing listing = fullListingGraphDouble();
        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);

        when(listingService.findAllFiltered(null, null, null, null, null)).thenReturn(List.of(listing));
        publicLinks(dto);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            toDetailsDTODouble(mockedMapper, listing, dto);

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response = controller.findAll(null, null, null, null, null);

            // Assert
            assertSame(dto, response.getBody().get(0));
        }
    }

    @Test
    void findAll_shouldReturnOkStatus_whenNoListingsExist() {
        // Arrange
        when(listingService.findAllFiltered(null, null, null, null, null)).thenReturn(List.of());

        // Act
        ResponseEntity<List<ListingDetailsDTO>> response = controller.findAll(null, null, null, null, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void findAll_shouldReturnEmptyBody_whenNoListingsExist() {
        // Arrange
        when(listingService.findAllFiltered(null, null, null, null, null)).thenReturn(List.of());

        // Act
        ResponseEntity<List<ListingDetailsDTO>> response = controller.findAll(null, null, null, null, null);

        // Assert
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void findAll_shouldReturnNotFound_whenRelatedDataDoesNotExist() {
        // Arrange
        Listing listing = mock(Listing.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);

        when(listingService.findAllFiltered(null, null, null, null, null)).thenReturn(List.of(listing));
        when(listing.getItemsId()).thenReturn(List.of(publicationSampleId));
        when(publicationService.findById(publicationSampleId))
                .thenThrow(new EntityNotFoundException());

        // Act
        ResponseEntity<List<ListingDetailsDTO>> response = controller.findAll(null, null, null, null, null);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void findAll_shouldReturnInternalServerError_whenUnexpectedExceptionOccurs() {
        // Arrange
        when(listingService.findAllFiltered(null, null, null, null, null)).thenThrow(new RuntimeException());

        // Act
        ResponseEntity<List<ListingDetailsDTO>> response = controller.findAll(null, null, null, null, null);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void findAll_shouldReturnBadRequest_whenIllegalArgumentExceptionOccurs() {
        // Arrange
        when(listingService.findAllFiltered(null, null, null, null, null))
                .thenThrow(new IllegalArgumentException());

        // Act
        ResponseEntity<List<ListingDetailsDTO>> response =
                controller.findAll(null, null, null, null, null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // =========================================================================
    // TESTS: FIND BY PUBLICATION (GET)
    // =========================================================================

    @Test
    void findListingsByPublication_shouldReturnOkStatus_whenListingsExist() throws Exception {
        // Arrange
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);
        Listing listing = fullListingGraphDouble();
        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);
        publicationSearchFlow(bdId, listing);
        publicLinks(dto);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toBibliographicalDataId("bd-1")).thenReturn(bdId);
            toDetailsDTODouble(mockedMapper, listing, dto);

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response =
                    controller.findListingsByPublication("bd-1");

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Test
    void findListingsByPublication_shouldReturnOneDto_whenListingsExist() throws Exception {
        // Arrange
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);
        Listing listing = fullListingGraphDouble();
        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);
        publicationSearchFlow(bdId, listing);
        publicLinks(dto);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toBibliographicalDataId("bd-1")).thenReturn(bdId);
            toDetailsDTODouble(mockedMapper, listing, dto);

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response =
                    controller.findListingsByPublication("bd-1");

            // Assert
            assertEquals(1, response.getBody().size());
        }
    }

    @Test
    void findListingsByPublication_shouldReturnExpectedDto_whenListingsExist() throws Exception {
        // Arrange
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);
        Listing listing = fullListingGraphDouble();
        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);
        publicationSearchFlow(bdId, listing);
        publicLinks(dto);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toBibliographicalDataId("bd-1")).thenReturn(bdId);
            toDetailsDTODouble(mockedMapper, listing, dto);

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response =
                    controller.findListingsByPublication("bd-1");

            // Assert
            assertSame(dto, response.getBody().get(0));
        }
    }

    @Test
    void findListingsByPublication_shouldReturnOkWithEmptyBody_whenNoPublicationSamplesExist() {
        // Arrange
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toBibliographicalDataId("bd-1")).thenReturn(bdId);
            when(publicationService.findIdsByBibliographicalDataIdIn(List.of(bdId)))
                    .thenReturn(List.of());

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response =
                    controller.findListingsByPublication("bd-1");

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().isEmpty());
        }
    }

    @Test
    void findListingsByPublication_shouldReturnOkStatus_whenNoListingsExist() {
        // Arrange
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);
        publicationSearchFlowWithoutListings(bdId);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toBibliographicalDataId("bd-1")).thenReturn(bdId);

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response =
                    controller.findListingsByPublication("bd-1");

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Test
    void findListingsByPublication_shouldReturnEmptyBody_whenNoListingsExist() {
        // Arrange
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);
        publicationSearchFlowWithoutListings(bdId);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toBibliographicalDataId("bd-1")).thenReturn(bdId);

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response =
                    controller.findListingsByPublication("bd-1");

            // Assert
            assertTrue(response.getBody().isEmpty());
        }
    }

    @Test
    void findListingsByPublication_shouldReturnBadRequest_whenBdIdIsInvalid() {
        // Arrange
        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toBibliographicalDataId(""))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response =
                    controller.findListingsByPublication("");

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void findListingsByPublication_shouldReturnNotFound_whenRelatedDataDoesNotExist() {
        // Arrange
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toBibliographicalDataId("bd-1")).thenReturn(bdId);
            when(publicationService.findIdsByBibliographicalDataIdIn(List.of(bdId)))
                    .thenThrow(new EntityNotFoundException());

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response =
                    controller.findListingsByPublication("bd-1");

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Test
    void findListingsByPublication_shouldReturnInternalServerError_whenUnexpectedExceptionOccurs() {
        // Arrange
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toBibliographicalDataId("bd-1")).thenReturn(bdId);
            when(publicationService.findIdsByBibliographicalDataIdIn(List.of(bdId)))
                    .thenThrow(new RuntimeException());

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response =
                    controller.findListingsByPublication("bd-1");

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    @Test
    void findListingsByPublication_shouldPassGenresToMapper() throws Exception {
        // Arrange
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);
        Listing listing = fullListingGraphDouble();
        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);
        publicationSearchFlow(bdId, listing);
        publicLinks(dto);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toBibliographicalDataId("bd-1")).thenReturn(bdId);
            toDetailsDTODouble(mockedMapper, listing, dto);

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response = controller.findListingsByPublication("bd-1");

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            mockedMapper.verify(() -> ListingMapper.toListingDetailsDTO(
                    eq(listing), anyList(), anyList(), anyList(), anyList(), argThat(genres -> !genres.isEmpty()), anyList(), any()
            ));
        }
    }

    @Test
    void findListingsByPublication_shouldPassAuthorsToMapper() throws Exception {
        // Arrange
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);
        Listing listing = fullListingGraphDouble();
        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);
        publicationSearchFlow(bdId, listing);
        publicLinks(dto);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toBibliographicalDataId("bd-1")).thenReturn(bdId);
            toDetailsDTODouble(mockedMapper, listing, dto);

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response = controller.findListingsByPublication("bd-1");

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            // Fixing arg positions: 5th arg is authors
            mockedMapper.verify(() -> ListingMapper.toListingDetailsDTO(
                    eq(listing), anyList(), anyList(), anyList(), argThat(authors -> !authors.isEmpty()), anyList(), anyList(), any()
            ));
        }
    }

    // =========================================================================
    // TESTS: FIND DIRECT SALE BY GENRE (GET)
    // =========================================================================

    @Test
    void getAllDirectSaleByGenre_shouldReturnOkStatus_whenListingsExist() throws Exception {
        // Arrange
        GenreId genreId = mock(GenreId.class);
        Listing listing = fullListingGraphDouble();
        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);
        genreSearchFlow(genreId, listing);
        publicLinks(dto);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toGenreId("genre-1")).thenReturn(genreId);
            toDetailsDTODouble(mockedMapper, listing, dto);

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response = controller.getAllDirectSaleByGenre("genre-1");

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Test
    void getAllDirectSaleByGenre_shouldReturnOneDto_whenListingsExist() throws Exception {
        // Arrange
        GenreId genreId = mock(GenreId.class);
        Listing listing = fullListingGraphDouble();
        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);
        genreSearchFlow(genreId, listing);
        publicLinks(dto);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toGenreId("genre-1")).thenReturn(genreId);
            toDetailsDTODouble(mockedMapper, listing, dto);

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response = controller.getAllDirectSaleByGenre("genre-1");

            // Assert
            assertEquals(1, response.getBody().size());
        }
    }

    @Test
    void getAllDirectSaleByGenre_shouldReturnExpectedDto_whenListingsExist() throws Exception {
        // Arrange
        GenreId genreId = mock(GenreId.class);
        Listing listing = fullListingGraphDouble();
        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);
        genreSearchFlow(genreId, listing);
        publicLinks(dto);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toGenreId("genre-1")).thenReturn(genreId);
            toDetailsDTODouble(mockedMapper, listing, dto);

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response = controller.getAllDirectSaleByGenre("genre-1");

            // Assert
            assertSame(dto, response.getBody().get(0));
        }
    }

    @Test
    void getAllDirectSaleByGenre_shouldReturnOkStatus_whenNoListingsExist() {
        // Arrange
        GenreId genreId = mock(GenreId.class);
        genreSearchFlowWithoutListings(genreId);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toGenreId("genre-1")).thenReturn(genreId);

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response = controller.getAllDirectSaleByGenre("genre-1");

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Test
    void getAllDirectSaleByGenre_shouldReturnEmptyBody_whenNoListingsExist() {
        // Arrange
        GenreId genreId = mock(GenreId.class);
        genreSearchFlowWithoutListings(genreId);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toGenreId("genre-1")).thenReturn(genreId);

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response = controller.getAllDirectSaleByGenre("genre-1");

            // Assert
            assertTrue(response.getBody().isEmpty());
        }
    }

    @Test
    void getAllDirectSaleByGenre_shouldReturnBadRequest_whenGenreIdIsInvalid() {
        // Arrange
        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toGenreId(""))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response = controller.getAllDirectSaleByGenre("");

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void getAllDirectSaleByGenre_shouldReturnNotFound_whenRelatedDataDoesNotExist() {
        // Arrange
        GenreId genreId = mock(GenreId.class);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toGenreId("genre-1")).thenReturn(genreId);
            when(biblioDataService.findAllByGenreId(genreId)).thenThrow(new EntityNotFoundException());

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response = controller.getAllDirectSaleByGenre("genre-1");

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Test
    void getAllDirectSaleByGenre_shouldReturnInternalServerError_whenUnexpectedExceptionOccurs() {
        // Arrange
        GenreId genreId = mock(GenreId.class);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toGenreId("genre-1")).thenReturn(genreId);
            when(biblioDataService.findAllByGenreId(genreId)).thenThrow(new RuntimeException());

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response = controller.getAllDirectSaleByGenre("genre-1");

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    // =========================================================================
    // TESTS: FIND ALL BY GENRE (GET)
    // =========================================================================

    @Test
    void findListingsByGenre_shouldReturnOkStatus_whenListingsExist() throws Exception {
        // Arrange
        GenreId genreId = mock(GenreId.class);
        Listing listing = fullListingGraphDouble();
        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);
        genreSearchFlowAllSaleTypes(genreId, listing);
        publicLinks(dto);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toGenreId("genre-1")).thenReturn(genreId);
            toDetailsDTODouble(mockedMapper, listing, dto);

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response =
                    controller.findListingsByGenre("genre-1");

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Test
    void findListingsByGenre_shouldReturnOneDto_whenListingsExist() throws Exception {
        // Arrange
        GenreId genreId = mock(GenreId.class);
        Listing listing = fullListingGraphDouble();
        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);
        genreSearchFlowAllSaleTypes(genreId, listing);
        publicLinks(dto);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toGenreId("genre-1")).thenReturn(genreId);
            toDetailsDTODouble(mockedMapper, listing, dto);

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response =
                    controller.findListingsByGenre("genre-1");

            // Assert
            assertEquals(1, response.getBody().size());
        }
    }

    @Test
    void findListingsByGenre_shouldReturnExpectedDto_whenListingsExist() throws Exception {
        // Arrange
        GenreId genreId = mock(GenreId.class);
        Listing listing = fullListingGraphDouble();
        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);
        genreSearchFlowAllSaleTypes(genreId, listing);
        publicLinks(dto);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toGenreId("genre-1")).thenReturn(genreId);
            toDetailsDTODouble(mockedMapper, listing, dto);

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response =
                    controller.findListingsByGenre("genre-1");

            // Assert
            assertSame(dto, response.getBody().get(0));
        }
    }

    @Test
    void findListingsByGenre_shouldReturnOkWithEmptyBody_whenNoPublicationSamplesExist() {
        // Arrange
        GenreId genreId = mock(GenreId.class);
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toGenreId("genre-1")).thenReturn(genreId);
            when(biblioDataService.findAllByGenreId(genreId)).thenReturn(List.of(bdId));
            when(publicationService.findIdsByBibliographicalDataIdIn(List.of(bdId)))
                    .thenReturn(List.of());

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response =
                    controller.findListingsByGenre("genre-1");

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().isEmpty());
        }
    }

    @Test
    void findListingsByGenre_shouldReturnOkStatus_whenNoListingsExist() {
        // Arrange
        GenreId genreId = mock(GenreId.class);
        genreSearchFlowAllSaleTypesWithoutListings(genreId);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toGenreId("genre-1")).thenReturn(genreId);

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response =
                    controller.findListingsByGenre("genre-1");

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Test
    void findListingsByGenre_shouldReturnEmptyBody_whenNoListingsExist() {
        // Arrange
        GenreId genreId = mock(GenreId.class);
        genreSearchFlowAllSaleTypesWithoutListings(genreId);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toGenreId("genre-1")).thenReturn(genreId);

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response =
                    controller.findListingsByGenre("genre-1");

            // Assert
            assertTrue(response.getBody().isEmpty());
        }
    }

    @Test
    void findListingsByGenre_shouldReturnBadRequest_whenGenreIdIsInvalid() {
        // Arrange
        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toGenreId(""))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response =
                    controller.findListingsByGenre("");

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void findListingsByGenre_shouldReturnNotFound_whenRelatedDataDoesNotExist() {
        // Arrange
        GenreId genreId = mock(GenreId.class);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toGenreId("genre-1")).thenReturn(genreId);
            when(biblioDataService.findAllByGenreId(genreId)).thenThrow(new EntityNotFoundException());

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response =
                    controller.findListingsByGenre("genre-1");

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Test
    void findListingsByGenre_shouldReturnInternalServerError_whenUnexpectedExceptionOccurs() {
        // Arrange
        GenreId genreId = mock(GenreId.class);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toGenreId("genre-1")).thenReturn(genreId);
            when(biblioDataService.findAllByGenreId(genreId)).thenThrow(new RuntimeException());

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response =
                    controller.findListingsByGenre("genre-1");

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    @Test
    void findListingsByGenre_shouldPassGenresToMapper() throws Exception {
        // Arrange
        GenreId genreId = mock(GenreId.class);
        Listing listing = fullListingGraphDouble();
        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);
        genreSearchFlowAllSaleTypes(genreId, listing);
        publicLinks(dto);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toGenreId("genre-1")).thenReturn(genreId);
            toDetailsDTODouble(mockedMapper, listing, dto);

            controller.findListingsByGenre("genre-1");

            // Assert
            mockedMapper.verify(() -> ListingMapper.toListingDetailsDTO(
                    eq(listing),
                    anyList(),
                    anyList(),
                    anyList(),
                    anyList(),
                    argThat(genres -> !genres.isEmpty()), // 6th arg is genres
                    anyList(),
                    any()
            ));
        }
    }

    @Test
    void findListingsByGenre_shouldPassAuthorsToMapper() throws Exception {
        // Arrange
        GenreId genreId = mock(GenreId.class);
        Listing listing = fullListingGraphDouble();
        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);
        genreSearchFlowAllSaleTypes(genreId, listing);
        publicLinks(dto);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toGenreId("genre-1")).thenReturn(genreId);
            toDetailsDTODouble(mockedMapper, listing, dto);

            controller.findListingsByGenre("genre-1");

            // Assert
            mockedMapper.verify(() -> ListingMapper.toListingDetailsDTO(
                    eq(listing),
                    anyList(),
                    anyList(),
                    anyList(),
                    argThat(authors -> !authors.isEmpty()), // 5th arg is authors
                    anyList(),
                    anyList(),
                    any()
            ));
        }
    }

    // =========================================================================
    // TESTS: FIND DIRECT SALE BY PUBLISHER (GET)
    // =========================================================================

    @Test
    void getAllDirectSaleByPublisher_shouldReturnOkStatus_whenListingsExist() throws Exception {
        // Arrange
        PublisherID publisherId = mock(PublisherID.class);
        Listing listing = fullListingGraphDouble();
        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);
        publisherSearchFlow(publisherId, listing);
        publicLinks(dto);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toPublisherId("publisher-1")).thenReturn(publisherId);
            toDetailsDTODouble(mockedMapper, listing, dto);

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response = controller.getAllDirectSaleByPublisher("publisher-1");

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Test
    void getAllDirectSaleByPublisher_shouldReturnOneDto_whenListingsExist() throws Exception {
        // Arrange
        PublisherID publisherId = mock(PublisherID.class);
        Listing listing = fullListingGraphDouble();
        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);
        publisherSearchFlow(publisherId, listing);
        publicLinks(dto);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toPublisherId("publisher-1")).thenReturn(publisherId);
            toDetailsDTODouble(mockedMapper, listing, dto);

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response = controller.getAllDirectSaleByPublisher("publisher-1");

            // Assert
            assertEquals(1, response.getBody().size());
        }
    }

    @Test
    void getAllDirectSaleByPublisher_shouldReturnExpectedDto_whenListingsExist() throws Exception {
        // Arrange
        PublisherID publisherId = mock(PublisherID.class);
        Listing listing = fullListingGraphDouble();
        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);
        publisherSearchFlow(publisherId, listing);
        publicLinks(dto);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toPublisherId("publisher-1")).thenReturn(publisherId);
            toDetailsDTODouble(mockedMapper, listing, dto);

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response = controller.getAllDirectSaleByPublisher("publisher-1");

            // Assert
            assertSame(dto, response.getBody().get(0));
        }
    }

    @Test
    void getAllDirectSaleByPublisher_shouldReturnOkStatus_whenNoListingsExist() {
        // Arrange
        PublisherID publisherId = mock(PublisherID.class);
        publisherSearchFlowWithoutListings(publisherId);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toPublisherId("publisher-1")).thenReturn(publisherId);

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response = controller.getAllDirectSaleByPublisher("publisher-1");

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Test
    void getAllDirectSaleByPublisher_shouldReturnEmptyBody_whenNoListingsExist() {
        // Arrange
        PublisherID publisherId = mock(PublisherID.class);
        publisherSearchFlowWithoutListings(publisherId);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toPublisherId("publisher-1")).thenReturn(publisherId);

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response = controller.getAllDirectSaleByPublisher("publisher-1");

            // Assert
            assertTrue(response.getBody().isEmpty());
        }
    }

    @Test
    void getAllDirectSaleByPublisher_shouldReturnBadRequest_whenPublisherIdIsInvalid() {
        // Arrange
        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toPublisherId(""))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response = controller.getAllDirectSaleByPublisher("");

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void getAllDirectSaleByPublisher_shouldReturnNotFound_whenRelatedDataDoesNotExist() {
        // Arrange
        PublisherID publisherId = mock(PublisherID.class);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toPublisherId("publisher-1")).thenReturn(publisherId);
            when(editionService.findIdsByPublisherId(publisherId)).thenThrow(new EntityNotFoundException());

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response = controller.getAllDirectSaleByPublisher("publisher-1");

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Test
    void getAllDirectSaleByPublisher_shouldReturnInternalServerError_whenUnexpectedExceptionOccurs() {
        // Arrange
        PublisherID publisherId = mock(PublisherID.class);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toPublisherId("publisher-1")).thenReturn(publisherId);
            when(editionService.findIdsByPublisherId(publisherId)).thenThrow(new RuntimeException());

            // Act
            ResponseEntity<List<ListingDetailsDTO>> response = controller.getAllDirectSaleByPublisher("publisher-1");

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    // =========================================================================
    // TESTS: MARK LISTING AS SOLD (PATCH)
    // =========================================================================

    @Test
    void markListingAsSold_shouldReturnOk_whenSuccessful() throws Exception {
        // Arrange
        String listingIdStr = "listing-1";
        String buyerIdStr = "user-2";
        ListingId listingIdVO = mock(ListingId.class);
        UserId buyerIdVO = mock(UserId.class);
        Listing listing = fullListingGraphDouble();

        when(listing.getSaleType()).thenReturn(SaleType.DIRECT_SALE);

        // Mock to cover the `getBuyer()` conditional logic
        when(listing.getBuyerId()).thenReturn(buyerIdVO);
        when(userService.findByID(buyerIdVO)).thenReturn(mock(User.class));

        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);

        when(listingService.markListingAsSold(listingIdVO, buyerIdVO)).thenReturn(listing);

        links(dto, buyerIdVO);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toListingId(listingIdStr)).thenReturn(listingIdVO);
            mockedMapper.when(() -> ListingMapper.toUserId(buyerIdStr)).thenReturn(buyerIdVO);
            toDetailsDTODouble(mockedMapper, listing, dto);

            // Act
            ResponseEntity<ListingDetailsDTO> response = controller.markListingAsSold(listingIdStr, buyerIdStr);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Test
    void markListingAsSold_shouldReturnOkBody_whenSuccessful() throws Exception {
        // Arrange
        String listingIdStr = "listing-1";
        String buyerIdStr = "user-2";
        ListingId listingIdVO = mock(ListingId.class);
        UserId buyerIdVO = mock(UserId.class);
        Listing listing = fullListingGraphDouble();

        when(listing.getSaleType()).thenReturn(SaleType.DIRECT_SALE);

        // Mock to cover the `getBuyer()` conditional logic
        when(listing.getBuyerId()).thenReturn(buyerIdVO);
        when(userService.findByID(buyerIdVO)).thenReturn(mock(User.class));

        ListingDetailsDTO dto = mock(ListingDetailsDTO.class);

        when(listingService.markListingAsSold(listingIdVO, buyerIdVO)).thenReturn(listing);

        links(dto, buyerIdVO);

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toListingId(listingIdStr)).thenReturn(listingIdVO);
            mockedMapper.when(() -> ListingMapper.toUserId(buyerIdStr)).thenReturn(buyerIdVO);
            toDetailsDTODouble(mockedMapper, listing, dto);

            // Act
            ResponseEntity<ListingDetailsDTO> response = controller.markListingAsSold(listingIdStr, buyerIdStr);

            // Assert
            assertSame(dto, response.getBody());
        }
    }

    @Test
    void markListingAsSold_shouldReturnBadRequest_whenIdIsInvalid() {
        // Arrange
        String listingIdStr = "invalid";
        String buyerIdStr = "user-2";

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toListingId(listingIdStr))
                    .thenThrow(new IllegalArgumentException("invalid id"));

            // Act
            ResponseEntity<ListingDetailsDTO> response = controller.markListingAsSold(listingIdStr, buyerIdStr);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void markListingAsSold_shouldReturnForbidden_whenBuyerIsSeller() {
        // Arrange
        String listingIdStr = "listing-1";
        String buyerIdStr = "user-2";
        ListingId listingIdVO = mock(ListingId.class);
        UserId buyerIdVO = mock(UserId.class);

        when(listingService.markListingAsSold(listingIdVO, buyerIdVO))
                .thenThrow(new AccessDeniedException("You cannot buy your own listing"));

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toListingId(listingIdStr)).thenReturn(listingIdVO);
            mockedMapper.when(() -> ListingMapper.toUserId(buyerIdStr)).thenReturn(buyerIdVO);

            // Act
            ResponseEntity<ListingDetailsDTO> response = controller.markListingAsSold(listingIdStr, buyerIdStr);

            // Assert
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }
    }

    @Test
    void markListingAsSold_shouldReturnForbidden_whenPolicyDenied() {
        // Arrange
        String listingIdStr = "listing-1";
        String buyerIdStr = "user-2";
        ListingId listingIdVO = mock(ListingId.class);
        UserId buyerIdVO = mock(UserId.class);

        when(listingService.markListingAsSold(listingIdVO, buyerIdVO))
                .thenThrow(new AccessDeniedException("You do not have permissions to buy this listing"));

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toListingId(listingIdStr)).thenReturn(listingIdVO);
            mockedMapper.when(() -> ListingMapper.toUserId(buyerIdStr)).thenReturn(buyerIdVO);

            // Act
            ResponseEntity<ListingDetailsDTO> response = controller.markListingAsSold(listingIdStr, buyerIdStr);

            // Assert
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }
    }

    @Test
    void markListingAsSold_shouldReturnNotFound_whenListingDoesNotExist() {
        // Arrange
        String listingIdStr = "listing-1";
        String buyerIdStr = "user-2";
        ListingId listingIdVO = mock(ListingId.class);
        UserId buyerIdVO = mock(UserId.class);

        when(listingService.markListingAsSold(listingIdVO, buyerIdVO))
                .thenThrow(new EntityNotFoundException("Listing not found"));

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toListingId(listingIdStr)).thenReturn(listingIdVO);
            mockedMapper.when(() -> ListingMapper.toUserId(buyerIdStr)).thenReturn(buyerIdVO);

            // Act
            ResponseEntity<ListingDetailsDTO> response = controller.markListingAsSold(listingIdStr, buyerIdStr);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Test
    void markListingAsSold_shouldReturnConflict_whenListingAlreadySold() {
        // Arrange
        String listingIdStr = "listing-1";
        String buyerIdStr = "user-2";
        ListingId listingIdVO = mock(ListingId.class);
        UserId buyerIdVO = mock(UserId.class);

        when(listingService.markListingAsSold(listingIdVO, buyerIdVO))
                .thenThrow(new IllegalStateException("Listing is already sold"));

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toListingId(listingIdStr)).thenReturn(listingIdVO);
            mockedMapper.when(() -> ListingMapper.toUserId(buyerIdStr)).thenReturn(buyerIdVO);

            // Act
            ResponseEntity<ListingDetailsDTO> response = controller.markListingAsSold(listingIdStr, buyerIdStr);

            // Assert
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        }
    }

    @Test
    void markListingAsSold_shouldReturnInternalServerError_onUnexpectedException() {
        // Arrange
        String listingIdStr = "listing-1";
        String buyerIdStr = "user-2";
        ListingId listingIdVO = mock(ListingId.class);
        UserId buyerIdVO = mock(UserId.class);

        when(listingService.markListingAsSold(listingIdVO, buyerIdVO))
                .thenThrow(new RuntimeException("unexpected"));

        try (MockedStatic<ListingMapper> mockedMapper = mockStatic(ListingMapper.class)) {
            mockedMapper.when(() -> ListingMapper.toListingId(listingIdStr)).thenReturn(listingIdVO);
            mockedMapper.when(() -> ListingMapper.toUserId(buyerIdStr)).thenReturn(buyerIdVO);

            // Act
            ResponseEntity<ListingDetailsDTO> response = controller.markListingAsSold(listingIdStr, buyerIdStr);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }


    // =========================================================================
    // TESTS: OPTIONS
    // =========================================================================

    @Test
    void optionsRoot_shouldReturnOkStatus() {
        // Act
        ResponseEntity<Void> response = controller.optionsRoot();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void optionsRoot_shouldReturnAllowHeaderWithSupportedMethods() {
        // Act
        ResponseEntity<Void> response = controller.optionsRoot();

        // Assert
        assertEquals(Set.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS), response.getHeaders().getAllow());
    }

    @Test
    void optionsById_shouldReturnOkStatus() {
        // Act
        ResponseEntity<Void> response = controller.optionsById();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void optionsById_shouldReturnAllowHeaderWithSupportedMethods() {
        // Act
        ResponseEntity<Void> response = controller.optionsById();

        // Assert
        assertEquals(Set.of(HttpMethod.GET, HttpMethod.OPTIONS), response.getHeaders().getAllow());
    }

    @Test
    void optionsByPublication_shouldReturnOkStatus() {
        // Act
        ResponseEntity<Void> response = controller.optionsByPublication();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void optionsByPublication_shouldReturnAllowHeaderWithSupportedMethods() {
        // Act
        ResponseEntity<Void> response = controller.optionsByPublication();

        // Assert
        assertEquals(Set.of(HttpMethod.GET, HttpMethod.OPTIONS), response.getHeaders().getAllow());
    }

    @Test
    void optionsDirectSaleByGenre_shouldReturnOkStatus() {
        // Act
        ResponseEntity<Void> response = controller.optionsDirectSaleByGenre();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void optionsDirectSaleByGenre_shouldReturnAllowHeaderWithSupportedMethods() {
        // Act
        ResponseEntity<Void> response = controller.optionsDirectSaleByGenre();

        // Assert
        assertEquals(Set.of(HttpMethod.GET, HttpMethod.OPTIONS), response.getHeaders().getAllow());
    }

    @Test
    void optionsByGenre_shouldReturnOkStatus() {
        // Act
        ResponseEntity<Void> response = controller.optionsByGenre();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void optionsByGenre_shouldReturnAllowHeaderWithSupportedMethods() {
        // Act
        ResponseEntity<Void> response = controller.optionsByGenre();

        // Assert
        assertEquals(Set.of(HttpMethod.GET, HttpMethod.OPTIONS), response.getHeaders().getAllow());
    }

    @Test
    void optionsDirectSaleByPublisher_shouldReturnOkStatus() {
        // Act
        ResponseEntity<Void> response = controller.optionsDirectSaleByPublisher();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void optionsDirectSaleByPublisher_shouldReturnAllowHeaderWithSupportedMethods() {
        // Act
        ResponseEntity<Void> response = controller.optionsDirectSaleByPublisher();

        // Assert
        assertEquals(Set.of(HttpMethod.GET, HttpMethod.OPTIONS), response.getHeaders().getAllow());
    }

    @Test
    void optionsListingSold_shouldReturnOkStatus() {
        // Act
        ResponseEntity<Void> response = controller.optionsListingSold();

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void optionsListingSold_shouldReturnAllowHeaderWithSupportedMethods() {
        //Act
        ResponseEntity<Void> response = controller.optionsListingSold();

        // Assert
        assertEquals(Set.of(HttpMethod.GET, HttpMethod.PATCH, HttpMethod.OPTIONS), response.getHeaders().getAllow());
    }


    // =========================================================================
    // PRIVATE HELPER METHODS - SETUP & MOCKING
    // =========================================================================

    private ListingRequestDTO ValidCreateRequest() {
        ListingRequestDTO request = mock(ListingRequestDTO.class);

        when(request.getPublicationIds()).thenReturn(List.of("pub-1"));
        when(request.getSaleType()).thenReturn("DIRECT_SALE");
        when(request.getPrice()).thenReturn("10.00");
        when(request.getPictures()).thenReturn(List.of("pic.jpg"));
        when(request.getListingTitle()).thenReturn("Listing title");
        when(request.getShortDescription()).thenReturn("Short description");

        return request;
    }

    private ListingRequestDTO validCreateRequestWithoutPictures() {
        ListingRequestDTO request = mock(ListingRequestDTO.class);

        when(request.getPublicationIds()).thenReturn(List.of("pub-1"));
        when(request.getSaleType()).thenReturn("DIRECT_SALE");
        when(request.getPrice()).thenReturn("10.00");
        when(request.getPictures()).thenReturn(null);
        when(request.getListingTitle()).thenReturn("Listing title");
        when(request.getShortDescription()).thenReturn("Short description");

        return request;
    }

    private void createListingMapper(
            MockedStatic<ListingMapper> mockedMapper,
            String userId,
            UserId userIdVO,
            PublicationSampleId publicationSampleId,
            Price price,
            Picture picture
    ) {
        mockedMapper.when(() -> ListingMapper.toUserId(userId)).thenReturn(userIdVO);
        mockedMapper.when(() -> ListingMapper.toPublicationId("pub-1")).thenReturn(publicationSampleId);
        mockedMapper.when(() -> ListingMapper.toSaleType("DIRECT_SALE")).thenReturn(SaleType.DIRECT_SALE);
        mockedMapper.when(() -> ListingMapper.toPrice("10.00")).thenReturn(price);
        mockedMapper.when(() -> ListingMapper.toPicture("pic.jpg")).thenReturn(picture);
    }

    private void createListingMapperWithoutPicture(
            MockedStatic<ListingMapper> mockedMapper,
            String userId,
            UserId userIdVO,
            PublicationSampleId publicationSampleId,
            Price price
    ) {
        mockedMapper.when(() -> ListingMapper.toUserId(userId)).thenReturn(userIdVO);
        mockedMapper.when(() -> ListingMapper.toPublicationId("pub-1")).thenReturn(publicationSampleId);
        mockedMapper.when(() -> ListingMapper.toSaleType("DIRECT_SALE")).thenReturn(SaleType.DIRECT_SALE);
        mockedMapper.when(() -> ListingMapper.toPrice("10.00")).thenReturn(price);
    }

    private void links(ListingDetailsDTO dto, UserId userIdVO) throws InstanceNotFoundException {
        when(listingLinkProvider.getPublicLinks()).thenReturn(List.of(Link.of("/listings", "all")));
        when(listingLinkProvider.getAuthenticatedUserLinks(userIdVO))
                .thenReturn(List.of(Link.of("/users/user-1/listings", "owner-listings")));
        when(dto.add(any(Link.class))).thenReturn(dto);
    }

    private void publicLinks(ListingDetailsDTO dto) {
        when(listingLinkProvider.getPublicLinks()).thenReturn(List.of());
        when(dto.add(any(Link.class))).thenReturn(dto);
    }

    private void genreSearchFlow(GenreId genreId, Listing listing) {
        BibliographicalDataId bibliographicalDataId = mock(BibliographicalDataId.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);

        when(biblioDataService.findAllByGenreId(genreId)).thenReturn(List.of(bibliographicalDataId));
        when(publicationService.findIdsByBibliographicalDataIdIn(List.of(bibliographicalDataId)))
                .thenReturn(List.of(publicationSampleId));
        when(listingService.findAllDirectSaleAndPublicationSampleIdIn(List.of(publicationSampleId)))
                .thenReturn(List.of(listing));
    }

    private void genreSearchFlowWithoutListings(GenreId genreId) {
        BibliographicalDataId bibliographicalDataId = mock(BibliographicalDataId.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);

        when(biblioDataService.findAllByGenreId(genreId)).thenReturn(List.of(bibliographicalDataId));
        when(publicationService.findIdsByBibliographicalDataIdIn(List.of(bibliographicalDataId)))
                .thenReturn(List.of(publicationSampleId));
        when(listingService.findAllDirectSaleAndPublicationSampleIdIn(List.of(publicationSampleId)))
                .thenReturn(List.of());
    }

    private void publisherSearchFlow(PublisherID publisherId, Listing listing) {
        EditionId editionId = mock(EditionId.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);

        when(editionService.findIdsByPublisherId(publisherId)).thenReturn(List.of(editionId));
        when(publicationService.findIdsByEditionIdIn(List.of(editionId)))
                .thenReturn(List.of(publicationSampleId));
        when(listingService.findAllDirectSaleAndPublicationSampleIdIn(List.of(publicationSampleId)))
                .thenReturn(List.of(listing));
    }

    private void publisherSearchFlowWithoutListings(PublisherID publisherId) {
        EditionId editionId = mock(EditionId.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);

        when(editionService.findIdsByPublisherId(publisherId)).thenReturn(List.of(editionId));
        when(publicationService.findIdsByEditionIdIn(List.of(editionId)))
                .thenReturn(List.of(publicationSampleId));
        when(listingService.findAllDirectSaleAndPublicationSampleIdIn(List.of(publicationSampleId)))
                .thenReturn(List.of());
    }

    private void publicationSearchFlow(BibliographicalDataId bdId, Listing listing) {
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);

        when(publicationService.findIdsByBibliographicalDataIdIn(List.of(bdId)))
                .thenReturn(List.of(publicationSampleId));
        when(listingService.findByPublicationSampleIdIn(List.of(publicationSampleId)))
                .thenReturn(List.of(listing));
    }

    private void publicationSearchFlowWithoutListings(BibliographicalDataId bdId) {
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);

        when(publicationService.findIdsByBibliographicalDataIdIn(List.of(bdId)))
                .thenReturn(List.of(publicationSampleId));
        when(listingService.findByPublicationSampleIdIn(List.of(publicationSampleId)))
                .thenReturn(List.of());
    }

    private Listing fullListingGraphDouble() throws InstanceNotFoundException {
        Listing listing = mock(Listing.class);
        ListingId listingId = mock(ListingId.class);

        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        PublicationSample publicationSample = mock(PublicationSample.class);

        BibliographicalDataId bibliographicalDataId = mock(BibliographicalDataId.class);
        BibliographicalData bibliographicalData = mock(BibliographicalData.class);

        AuthorId authorId = mock(AuthorId.class);
        Author author = mock(Author.class);

        GenreId genreId = mock(GenreId.class);
        Genre genre = mock(Genre.class);

        EditionId editionId = mock(EditionId.class);
        Edition edition = mock(Edition.class);

        PublisherID publisherId = mock(PublisherID.class);
        Publisher publisher = mock(Publisher.class);

        when(listing.id()).thenReturn(listingId);
        when(listingId.getId()).thenReturn("listing-1");

        when(listing.getItemsId()).thenReturn(List.of(publicationSampleId));
        when(publicationService.findById(publicationSampleId)).thenReturn(publicationSample);

        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(biblioDataService.findById(bibliographicalDataId)).thenReturn(bibliographicalData);

        when(bibliographicalData.getAuthors()).thenReturn(List.of(authorId));
        when(authorService.findById(authorId)).thenReturn(author);

        when(bibliographicalData.getGenres()).thenReturn(List.of(genreId));
        when(genreService.findById(genreId)).thenReturn(genre);

        when(publicationSample.getEditionID()).thenReturn(editionId);
        when(editionService.findById(editionId)).thenReturn(edition);

        when(edition.getPublisherID()).thenReturn(publisherId);
        when(publisherService.findById(publisherId)).thenReturn(publisher);

        return listing;
    }

    private void toDetailsDTODouble(
            MockedStatic<ListingMapper> mockedMapper,
            Listing listing,
            ListingDetailsDTO dto
    ) {
        mockedMapper.when(() -> ListingMapper.toListingDetailsDTO(
                eq(listing),
                anyList(),
                anyList(),
                anyList(),
                anyList(),
                anyList(),
                anyList(),
                any()
        )).thenReturn(dto);
    }

    private void genreSearchFlowAllSaleTypes(GenreId genreId, Listing listing) {
        BibliographicalDataId bibliographicalDataId = mock(BibliographicalDataId.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);

        when(biblioDataService.findAllByGenreId(genreId)).thenReturn(List.of(bibliographicalDataId));
        when(publicationService.findIdsByBibliographicalDataIdIn(List.of(bibliographicalDataId)))
                .thenReturn(List.of(publicationSampleId));
        when(listingService.findByPublicationSampleIdIn(List.of(publicationSampleId)))
                .thenReturn(List.of(listing));
    }

    private void genreSearchFlowAllSaleTypesWithoutListings(GenreId genreId) {
        BibliographicalDataId bibliographicalDataId = mock(BibliographicalDataId.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);

        when(biblioDataService.findAllByGenreId(genreId)).thenReturn(List.of(bibliographicalDataId));
        when(publicationService.findIdsByBibliographicalDataIdIn(List.of(bibliographicalDataId)))
                .thenReturn(List.of(publicationSampleId));
        when(listingService.findByPublicationSampleIdIn(List.of(publicationSampleId)))
                .thenReturn(List.of());
    }
}