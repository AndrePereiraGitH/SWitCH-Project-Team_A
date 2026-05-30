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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
 *     <li>{@code getAuctions} — returns {@code 200 OK} with filtered results
 *         by genre, author, publisher, bibliographical data, or unfiltered,
 *         {@code 404 Not Found} when a listing does not exist,
 *         {@code 500 Internal Server Error} for unexpected errors.</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
class AuctionRESTControllerTest {

    @Mock
    private AuctionLinkProvider auctionLinkProviderDouble;

    @Mock
    private AuctionService auctionServiceDouble;
    @Mock
    private ListingService listingServiceDouble;
    @Mock
    private PublicationSampleService pubSampleServiceDouble;
    @Mock
    private BibliographicalDataService bdServiceDouble;
    @Mock
    private EditionService editionServiceDouble;

    @Mock
    private Auction auctionDouble;
    @Mock
    private Listing listingDouble;

    @Mock
    private ListingId listingIdDouble;
    @Mock
    private AuctionId auctionIdDouble;

    @Mock
    private Price initialPriceDouble;

    @InjectMocks
    private AuctionRESTController controller;

    private final String userId = "user-123";
    private final String auctionId = "auction-123";
    private final String listingId = "listing-456";
    private final String genre = "genre-123";
    private final String author = "author-123";
    private final String publisher = "publisher-123";
    private final String bd = "bd-123";

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
    }

    @Test
    void createAuction_shouldReturn403_whenUserDoesNotHavePermission() throws Exception {
        // Arrange
        CreateAuctionDTO requestDTO = new CreateAuctionDTO(listingId, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(7), BigDecimal.valueOf(100), BigDecimal.valueOf(500));

        when(listingServiceDouble.findById(any())).thenReturn(listingDouble);
        when(auctionServiceDouble.createAndSave(any(), any(), any(), any(), any(), any()))
                .thenThrow(new AccessDeniedException("Forbidden"));

        // Act
        ResponseEntity<AuctionDTO> response = controller.createAuction(requestDTO, userId);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void createAuction_shouldReturn404_whenListingNotFound() throws Exception {
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

        when(listingServiceDouble.findById(any())).thenReturn(listingDouble);
        when(auctionServiceDouble.createAndSave(any(), any(), any(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Invalid argument"));

        // Act
        ResponseEntity<AuctionDTO> response = controller.createAuction(requestDTO, userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // =================================================================================================================
    // =========== placeBid ============================================================================================
    // =================================================================================================================

    @Test
    void placeBid_shouldReturn204_whenBidIsPlacedSuccessfully() throws Exception {
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


    // =================================================================================================================
    // =========== getAuctions =========================================================================================
    // =================================================================================================================

    @Test
    void getAuctions_shouldReturn200_whenNoFilterProvided() throws Exception {
        // Arrange
        setupAuctionDouble();
        when(auctionServiceDouble.findAll()).thenReturn(List.of(auctionDouble));
        when(auctionDouble.getListingId()).thenReturn(listingIdDouble);
        when(listingServiceDouble.findById(listingIdDouble)).thenReturn(listingDouble);
        when(auctionLinkProviderDouble.getLinksForAuction(any())).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<List<AuctionDTO>> response = controller.getAuctions(null, null, null,
                null, false, userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAuctions_shouldReturn200_whenFilteredByGenre() throws Exception {
        // Arrange
        setupAuctionDouble();
        when(bdServiceDouble.findAllByGenreId(any())).thenReturn(List.of());
        when(pubSampleServiceDouble.findIdsByBibliographicalDataIdIn(any())).thenReturn(List.of());
        when(listingServiceDouble.findByAuctionAndPublicationSampleIdIn(any())).thenReturn(List.of(listingDouble));
        when(listingDouble.id()).thenReturn(listingIdDouble);
        when(auctionServiceDouble.findByListingIdIn(any())).thenReturn(List.of(auctionDouble));

        // Act
        ResponseEntity<List<AuctionDTO>> response = controller.getAuctions(genre, null, null,
                null, false, userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAuctions_shouldReturn200_whenFilteredByGenreSortedByDeadline() throws Exception {
        // Arrange
        setupAuctionDouble();
        when(bdServiceDouble.findAllByGenreId(any())).thenReturn(List.of());
        when(pubSampleServiceDouble.findIdsByBibliographicalDataIdIn(any())).thenReturn(List.of());
        when(listingServiceDouble.findByAuctionAndPublicationSampleIdIn(any())).thenReturn(List.of(listingDouble));
        when(listingDouble.id()).thenReturn(listingIdDouble);
        when(auctionServiceDouble.findByListingIdInOrderByAuctionDeadlineAsc(any())).thenReturn(List.of(auctionDouble));

        // Act
        ResponseEntity<List<AuctionDTO>> response = controller.getAuctions(genre, null, null,
                null, true, userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAuctions_shouldReturn200_whenFilteredByAuthor() throws Exception {
        // Arrange
        setupAuctionDouble();
        when(bdServiceDouble.findIdByAuthorId(any())).thenReturn(List.of());
        when(pubSampleServiceDouble.findIdsByBibliographicalDataIdIn(any())).thenReturn(List.of());
        when(listingServiceDouble.findByAuctionAndPublicationSampleIdIn(any())).thenReturn(List.of(listingDouble));
        when(listingDouble.id()).thenReturn(listingIdDouble);
        when(auctionServiceDouble.findByListingIdIn(any())).thenReturn(List.of(auctionDouble));

        // Act
        ResponseEntity<List<AuctionDTO>> response = controller.getAuctions(null, author, null,
                null, false, userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAuctions_shouldReturn200_whenFilteredByPublisher() throws Exception {
        // Arrange
        setupAuctionDouble();
        when(editionServiceDouble.findIdsByPublisherId(any())).thenReturn(List.of());
        when(pubSampleServiceDouble.findIdsByEditionIdIn(any())).thenReturn(List.of());
        when(listingServiceDouble.findByAuctionAndPublicationSampleIdIn(any())).thenReturn(List.of(listingDouble));
        when(listingDouble.id()).thenReturn(listingIdDouble);
        when(auctionServiceDouble.findByListingIdIn(any())).thenReturn(List.of(auctionDouble));

        // Act
        ResponseEntity<List<AuctionDTO>> response = controller.getAuctions(null, null, publisher,
                null, false, userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAuctions_shouldReturn200_whenFilteredByBibliographicalData() throws Exception {
        // Arrange
        setupAuctionDouble();
        when(pubSampleServiceDouble.findIdsByBibliographicalDataId(any())).thenReturn(List.of());
        when(listingServiceDouble.findByAuctionAndPublicationSampleIdIn(any())).thenReturn(List.of(listingDouble));
        when(listingDouble.id()).thenReturn(listingIdDouble);
        when(auctionServiceDouble.findByListingIdIn(any())).thenReturn(List.of(auctionDouble));

        // Act
        ResponseEntity<List<AuctionDTO>> response = controller.getAuctions(null, null, null,
                bd, false, userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAuctions_shouldReturn404_whenListingNotFound() throws Exception {
        // Arrange
        when(auctionServiceDouble.findAll()).thenReturn(List.of(auctionDouble));
        when(auctionDouble.getListingId()).thenReturn(listingIdDouble);
        when(listingServiceDouble.findById(listingIdDouble)).thenThrow(new EntityNotFoundException("Listing not found"));

        // Act
        ResponseEntity<List<AuctionDTO>> response = controller.getAuctions(null, null, null,
                null, false, userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAuctions_shouldReturn500_whenUnexpectedErrorOccurs() throws Exception {
        // Arrange
        when(auctionServiceDouble.findAll()).thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<List<AuctionDTO>> response = controller.getAuctions(null, null, null,
                null, false, userId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    // =================================================================================================================
    // ============= Private Helper ====================================================================================
    // =================================================================================================================

    private void setupAuctionDouble() throws Exception {
        when(auctionDouble.id()).thenReturn(auctionIdDouble);
        when(auctionDouble.getListingId()).thenReturn(listingIdDouble);
        when(auctionDouble.getAuctionStatus()).thenReturn(AuctionStatus.ACTIVE);
        when(auctionDouble.getInitialPrice()).thenReturn(initialPriceDouble);
        when(auctionDouble.getBuyoutPrice()).thenReturn(null);
        when(auctionDouble.getFinalPrice()).thenReturn(null);
        when(auctionDouble.getClosedAt()).thenReturn(null);
        when(initialPriceDouble.getPrice()).thenReturn(BigDecimal.valueOf(100));
        when(listingDouble.getListingTitle()).thenReturn("Listing Title");
        when(listingDouble.getShortDescription()).thenReturn("Listing Description");
        when(listingServiceDouble.findById(any())).thenReturn(listingDouble);
        when(auctionLinkProviderDouble.getLinksForAuction(any())).thenReturn(new ArrayList<>());
    }
}