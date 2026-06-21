package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.AuctionLinkProvider;
import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Enum.AuctionStatus;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.ValueObjects.AuctionId;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.dto.auction.request.CreateAuctionDTO;
import TOPSECRET.dto.auction.request.PlaceBidDTO;
import TOPSECRET.dto.auction.response.AuctionDTO;
import TOPSECRET.services.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceNotFoundException;
import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link AuctionRESTController}.
 *
 * <p>Verifies the behaviour of each endpoint, including:</p>
 * <ul>
 *     <li>{@code createAuction} — returns {@code 201 Created} on success,
 *         {@code 400 Bad Request} for invalid input,
 *         {@code 403 Forbidden} when the user lacks permission,
 *         {@code 404 Not Found} when the listing does not exist;</li>
 *     <li>{@code placeBid} — returns {@code 204 No Content} on success,
 *         {@code 400 Bad Request} for invalid bid,
 *         {@code 403 Forbidden} when the user lacks permission,
 *         {@code 404 Not Found} when the auction does not exist;</li>
 *     <li>{@code getAuctions} — returns {@code 200 OK} with results filtered by
 *         genre, author, publisher, bibliographical data, or unfiltered,
 *         delegating filtering logic to {@link AuctionService#findAuctions},
 *         {@code 404 Not Found} when a listing does not exist,
 *         {@code 500 Internal Server Error} for unexpected errors;</li>
 *     <li>{@code getAuctionById} — returns {@code 200 OK} when the auction exists,
 *         {@code 400 Bad Request} if the identifier is invalid,
 *         {@code 404 Not Found} when the auction or listing does not exist,
 *         {@code 500 Internal Server Error} for unexpected errors.</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
class AuctionRESTControllerTest {

    @Mock private AuctionService auctionServiceDouble;
    @Mock private ListingService listingServiceDouble;

    @Mock private AuctionLinkProvider auctionLinkProviderDouble;

    @InjectMocks private AuctionRESTController controller;

    @Mock private Auction auctionDouble;
    @Mock private Listing listingDouble;

    @Mock private ListingId listingIdDouble;
    @Mock private AuctionId auctionIdDouble;

    @Mock private Price initialPriceDouble;

    private final String userId = "user-123";
    private final String auctionId = "auction-123";
    private final String listingId = "listing-456";
    private final String genre = "genre-123";

    // =================================================================================================================
    // =========== createAuction =======================================================================================
    // =================================================================================================================

    @Test
    void createAuction_shouldReturn201_whenAuctionIsCreatedSuccessfully() throws Exception {
        // Arrange
        setupAuctionDouble();
        CreateAuctionDTO requestDTO = new CreateAuctionDTO(listingId, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(7), BigDecimal.valueOf(100), BigDecimal.valueOf(500));

        when(auctionServiceDouble.createAndSave(any(), any(), any(), any(), any(), any())).thenReturn(auctionDouble);

        // Act
        ResponseEntity<AuctionDTO> response = controller.createAuction(requestDTO, userId);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().getLinks().isEmpty());         // kills the mutant from forEach
    }

    @Test
    void createAuction_shouldReturn403_whenUserDoesNotHavePermission() throws Exception {
        // Arrange
        CreateAuctionDTO requestDTO = new CreateAuctionDTO(listingId, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(7), BigDecimal.valueOf(100), BigDecimal.valueOf(500));

        when(auctionServiceDouble.createAndSave(any(), any(), any(), any(), any(), any()))
                .thenThrow(new AccessDeniedException("Forbidden"));

        // Act
        ResponseEntity<AuctionDTO> response = controller.createAuction(requestDTO, userId);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void createAuction_shouldReturn404_whenListingNotFound() {
        // Arrange
        CreateAuctionDTO requestDTO = new CreateAuctionDTO(listingId, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(7), BigDecimal.valueOf(100), BigDecimal.valueOf(500));

        when(listingServiceDouble.findById(any())).thenThrow(new EntityNotFoundException("Listing not found"));

        // Act
        ResponseEntity<AuctionDTO> response = controller.createAuction(requestDTO, userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createAuction_shouldReturn400_whenArgumentIsInvalid() throws Exception {
        // Arrange
        CreateAuctionDTO requestDTO = new CreateAuctionDTO(listingId, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(7), BigDecimal.valueOf(100), BigDecimal.valueOf(500));

        when(auctionServiceDouble.createAndSave(any(), any(), any(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Invalid argument"));

        // Act
        ResponseEntity<AuctionDTO> response = controller.createAuction(requestDTO, userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createAuction_shouldReturn500_whenUnexpectedErrorOccurs() throws Exception {
        // Arrange
        CreateAuctionDTO requestDTO = new CreateAuctionDTO(listingId, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(7), BigDecimal.valueOf(100), BigDecimal.valueOf(500));

        when(auctionServiceDouble.createAndSave(any(), any(), any(), any(), any(), any()))
                .thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<AuctionDTO> response = controller.createAuction(requestDTO, userId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // =================================================================================================================
    // =========== placeBid ============================================================================================
    // =================================================================================================================

    @Test
    void placeBid_shouldReturn204_whenBidIsPlacedSuccessfully() {
        // Arrange
        PlaceBidDTO requestDTO = new PlaceBidDTO(BigDecimal.valueOf(150));

        // Act
        ResponseEntity<Void> response = controller.placeBid(auctionId, requestDTO, userId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void placeBid_shouldReturn403_whenUserDoesNotHavePermission() throws Exception {
        // Arrange
        PlaceBidDTO requestDTO = new PlaceBidDTO(BigDecimal.valueOf(150));

        when(auctionServiceDouble.placeBid(any(), any(), any())).thenThrow(new AccessDeniedException("Forbidden"));

        // Act
        ResponseEntity<Void> response = controller.placeBid(auctionId, requestDTO, userId);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void placeBid_shouldReturn404_whenAuctionNotFound() throws Exception {
        // Arrange
        PlaceBidDTO requestDTO = new PlaceBidDTO(BigDecimal.valueOf(150));

        when(auctionServiceDouble.placeBid(any(), any(), any()))
                .thenThrow(new InstanceNotFoundException("Auction not found"));

        // Act
        ResponseEntity<Void> response = controller.placeBid(auctionId, requestDTO, userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void placeBid_shouldReturn400_whenBidIsInvalid() throws Exception {
        // Arrange
        PlaceBidDTO requestDTO = new PlaceBidDTO(BigDecimal.valueOf(150));

        when(auctionServiceDouble.placeBid(any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Invalid bid"));

        // Act
        ResponseEntity<Void> response = controller.placeBid(auctionId, requestDTO, userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void placeBid_shouldReturn500_whenUnexpectedErrorOccurs() throws Exception {
        // Arrange
        PlaceBidDTO requestDTO = new PlaceBidDTO(BigDecimal.valueOf(150));

        when(auctionServiceDouble.placeBid(any(), any(), any())).thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<Void> response = controller.placeBid(auctionId, requestDTO, userId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    // =================================================================================================================
    // =========== getAuctions =========================================================================================
    // =================================================================================================================

    @Test
    void getAuctions_shouldReturn200_whenNoFilterProvided() {
        // Arrange
        setupAuctionDouble();
        when(auctionServiceDouble.findAuctions(any(), any(), any(), any(), any(), any(Boolean.class)))
                .thenReturn(List.of(auctionDouble));

        // Act
        ResponseEntity<List<AuctionDTO>> response = controller.getAuctions(null, null, null,
                null, null, false);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().get(0).getLinks().isEmpty());        // kills the mutant from forEach
    }

    @Test
    void getAuctions_shouldReturn200_whenFilteredByGenre() {
        // Arrange
        setupAuctionDouble();
        when(auctionServiceDouble.findAuctions(any(), any(), any(), any(), any(), any(Boolean.class)))
                .thenReturn(List.of(auctionDouble));

        // Act
        ResponseEntity<List<AuctionDTO>> response = controller.getAuctions(genre, null, null,
                null, null, false);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAuctions_shouldReturn200_whenFilteredByGenreSortedByDeadline() {
        // Arrange
        setupAuctionDouble();
        when(auctionServiceDouble.findAuctions(any(), any(), any(), any(), any(), any(Boolean.class)))
                .thenReturn(List.of(auctionDouble));

        // Act
        ResponseEntity<List<AuctionDTO>> response = controller.getAuctions(genre, null, null,
                null, null, true);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAuctions_shouldReturn200_whenFilteredByAuthor() {
        // Arrange
        setupAuctionDouble();
        when(auctionServiceDouble.findAuctions(any(), any(), any(), any(), any(), any(Boolean.class)))
                .thenReturn(List.of(auctionDouble));
        String author = "author-123";

        // Act
        ResponseEntity<List<AuctionDTO>> response = controller.getAuctions(null, author, null,
                null, null, false);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAuctions_shouldReturn200_whenFilteredByPublisher() {
        // Arrange
        setupAuctionDouble();
        when(auctionServiceDouble.findAuctions(any(), any(), any(), any(), any(), any(Boolean.class)))
                .thenReturn(List.of(auctionDouble));
        String publisher = "publisher-123";

        // Act
        ResponseEntity<List<AuctionDTO>> response = controller.getAuctions(null, null, publisher,
                null, null, false);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAuctions_shouldReturn200_whenFilteredByBibliographicalData() {
        // Arrange
        setupAuctionDouble();
        when(auctionServiceDouble.findAuctions(any(), any(), any(), any(), any(), any(Boolean.class)))
                .thenReturn(List.of(auctionDouble));
        String bd = "bd-123";

        // Act
        ResponseEntity<List<AuctionDTO>> response = controller.getAuctions(null, null, null,
                bd, null, false);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAuctions_shouldReturn404_whenListingNotFound() {
        // Arrange
        when(auctionServiceDouble.findAuctions(any(), any(), any(), any(), any(), any(Boolean.class)))
                .thenReturn(List.of(auctionDouble));
        when(auctionDouble.getListingId()).thenReturn(listingIdDouble);
        when(listingServiceDouble.findById(listingIdDouble))
                .thenThrow(new EntityNotFoundException("Listing not found"));

        // Act
        ResponseEntity<List<AuctionDTO>> response = controller.getAuctions(null, null, null,
                null, null, false);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAuctions_shouldHandleDuplicateListingIds() {
        // Arrange
        Auction auctionDouble2 = mock(Auction.class);
        AuctionId auctionIdDouble2 = mock(AuctionId.class);

        // Setup auctionDouble
        when(auctionDouble.id()).thenReturn(auctionIdDouble);
        when(auctionDouble.getListingId()).thenReturn(listingIdDouble);
        when(auctionDouble.getAuctionStatus()).thenReturn(AuctionStatus.ACTIVE);
        when(auctionDouble.getInitialPrice()).thenReturn(initialPriceDouble);
        when(auctionDouble.getBuyoutPrice()).thenReturn(null);
        when(auctionDouble.getFinalPrice()).thenReturn(null);
        when(auctionDouble.getClosedAt()).thenReturn(null);
        when(initialPriceDouble.getPrice()).thenReturn(BigDecimal.valueOf(100));

        // Setup auctionDouble2 (same listingId as auctionDouble)
        when(auctionDouble2.id()).thenReturn(auctionIdDouble2);
        when(auctionDouble2.getListingId()).thenReturn(listingIdDouble);
        when(auctionDouble2.getAuctionStatus()).thenReturn(AuctionStatus.ACTIVE);
        when(auctionDouble2.getInitialPrice()).thenReturn(initialPriceDouble);
        when(auctionDouble2.getBuyoutPrice()).thenReturn(null);
        when(auctionDouble2.getFinalPrice()).thenReturn(null);
        when(auctionDouble2.getClosedAt()).thenReturn(null);

        when(listingDouble.getListingTitle()).thenReturn("Listing Title");
        when(listingServiceDouble.findById(any())).thenReturn(listingDouble);
        when(auctionLinkProviderDouble.getPublicLinks()).thenReturn(new ArrayList<>());

        when(auctionServiceDouble.findAuctions(any(), any(), any(), any(), any(), any(Boolean.class)))
                .thenReturn(List.of(auctionDouble, auctionDouble2));

        // Act
        ResponseEntity<List<AuctionDTO>> response = controller.getAuctions(null, null, null,
                null, null, false);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());     // kills the mutant from forEach
    }

    @Test
    void getAuctions_shouldReturn400_whenArgumentIsInvalid() {
        // Arrange
        when(auctionServiceDouble.findAuctions(any(), any(), any(), any(), any(), any(Boolean.class)))
                .thenThrow(new IllegalArgumentException("Invalid argument"));

        // Act
        ResponseEntity<List<AuctionDTO>> response = controller.getAuctions(null, null, null,
                null, null, false);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getAuctions_shouldReturn500_whenUnexpectedErrorOccurs() {
        // Arrange
        when(auctionServiceDouble.findAuctions(any(), any(), any(), any(), any(), any(Boolean.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<List<AuctionDTO>> response = controller.getAuctions(null, null, null,
                null, null, false);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void getAuctions_shouldReturn200_whenFilteredByListingId() {
        // Arrange
        setupAuctionDouble();
        when(auctionServiceDouble.findAuctions(any(), any(), any(), any(), any(), any(Boolean.class)))
                .thenReturn(List.of(auctionDouble));

        // Act
        ResponseEntity<List<AuctionDTO>> response = controller.getAuctions(null, null, null,
                null, listingId, false);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    // =================================================================================================================
    // =========== getAuctionById ======================================================================================
    // =================================================================================================================

    @Test
    void getAuctionById_shouldReturn200_whenAuctionExists() throws Exception {
        // Arrange
        setupAuctionDouble();
        when(auctionServiceDouble.findById(any())).thenReturn(auctionDouble);

        // Act
        ResponseEntity<AuctionDTO> response = controller.getAuctionById(auctionId, userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().getLinks().isEmpty());       // kills the mutant from forEach
    }

    @Test
    void getAuctionById_shouldReturn404_whenAuctionNotFound() throws Exception {
        // Arrange
        when(auctionServiceDouble.findById(any())).thenThrow(new InstanceNotFoundException("Auction not found"));

        // Act
        ResponseEntity<AuctionDTO> response = controller.getAuctionById(auctionId, userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAuctionById_shouldReturn404_whenListingNotFound() throws Exception {
        // Arrange
        when(auctionServiceDouble.findById(any())).thenReturn(auctionDouble);
        when(auctionDouble.getListingId()).thenReturn(listingIdDouble);
        when(listingServiceDouble.findById(any())).thenThrow(new EntityNotFoundException("Listing not found"));

        // Act
        ResponseEntity<AuctionDTO> response = controller.getAuctionById(auctionId, userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAuctionById_shouldReturn400_whenAuctionIdIsInvalid() {
        // Act
        ResponseEntity<AuctionDTO> response = controller.getAuctionById("", userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getAuctionById_shouldReturn500_whenUnexpectedErrorOccurs() throws Exception {
        // Arrange
        when(auctionServiceDouble.findById(any())).thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<AuctionDTO> response = controller.getAuctionById(auctionId, userId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void getAuctionById_shouldReturn200_whenAuctionExistsAndUserIsNotAuthenticated() throws Exception {
        // Arrange
        setupAuctionDouble();
        when(auctionServiceDouble.findById(any())).thenReturn(auctionDouble);

        // Act
        ResponseEntity<AuctionDTO> response = controller.getAuctionById(auctionId, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().getLinks().isEmpty());
    }

    // =================================================================================================================
    // =========== Options =============================================================================================
    // =================================================================================================================

    @Test
    void optionsRoot_shouldReturn200_withAllowedMethods() {
        // Act
        ResponseEntity<Void> response = controller.optionsRoot();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getHeaders().getAllow().contains(org.springframework.http.HttpMethod.GET));
        assertTrue(response.getHeaders().getAllow().contains(org.springframework.http.HttpMethod.POST));
        assertTrue(response.getHeaders().getAllow().contains(org.springframework.http.HttpMethod.OPTIONS));
    }

    @Test
    void optionsBids_shouldReturn200_withAllowedMethods() {
        // Act
        ResponseEntity<Void> response = controller.optionsBids(auctionId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getHeaders().getAllow().contains(org.springframework.http.HttpMethod.POST));
        assertTrue(response.getHeaders().getAllow().contains(org.springframework.http.HttpMethod.OPTIONS));
        assertFalse(response.getHeaders().getAllow().contains(org.springframework.http.HttpMethod.GET));
    }

    @Test
    void optionsById_shouldReturn200_withAllowedMethods() {
        // Act
        ResponseEntity<Void> response = controller.optionsById(auctionId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getHeaders().getAllow().contains(org.springframework.http.HttpMethod.GET));
        assertTrue(response.getHeaders().getAllow().contains(org.springframework.http.HttpMethod.OPTIONS));
        assertFalse(response.getHeaders().getAllow().contains(org.springframework.http.HttpMethod.POST));
    }


    // =================================================================================================================
    // ============= Private Helper ====================================================================================
    // =================================================================================================================

    private void setupAuctionDouble() {
        when(auctionDouble.id()).thenReturn(auctionIdDouble);
        when(auctionDouble.getListingId()).thenReturn(listingIdDouble);
        when(auctionDouble.getAuctionStatus()).thenReturn(AuctionStatus.ACTIVE);
        when(auctionDouble.getInitialPrice()).thenReturn(initialPriceDouble);
        when(auctionDouble.getBuyoutPrice()).thenReturn(null);
        when(auctionDouble.getFinalPrice()).thenReturn(null);
        when(auctionDouble.getClosedAt()).thenReturn(null);
        when(initialPriceDouble.getPrice()).thenReturn(BigDecimal.valueOf(100));
        when(listingDouble.getListingTitle()).thenReturn("Listing Title");
        when(listingServiceDouble.findById(any())).thenReturn(listingDouble);
        when(auctionDouble.getCurrentHighestBid()).thenReturn(null);
        when(auctionDouble.getBidHistory()).thenReturn(List.of());
        when(auctionLinkProviderDouble.getPublicLinks()).thenReturn(new ArrayList<>());
    }
}