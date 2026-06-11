package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.AuctionAuthorizationPolicy;
import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Auction.AuctionFactory;
import TOPSECRET.domain.Auction.Bid;
import TOPSECRET.domain.Auction.IAuctionRepository;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.Enum.AuctionStatus;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Test suite for {@link AuctionService}, validating its behaviour as an
 * application-layer component responsible for managing {@link Auction} lifecycle operations.
 *
 * <p>These tests ensure that the service:
 * <ul>
 *     <li>rejects invalid dependencies in the constructor;</li>
 *     <li>correctly delegates auction creation to the {@link AuctionFactory},
 *         throwing {@link AccessDeniedException} when the user lacks permission,
 *         {@link InstanceNotFoundException} when the user or listing does not exist,
 *         {@link IllegalArgumentException} when the listing is not of type AUCTION,
 *         when the listing already has an active, scheduled or draft auction,
 *         and {@link InstanceAlreadyExistsException} when the auction already exists
 *         in the repository;</li>
 *     <li>correctly delegates persistence to the {@link IAuctionRepository};</li>
 *     <li>returns the persisted {@link Auction} instance after creation;</li>
 *     <li>places a bid on an existing auction via {@code placeBid}, delegating
 *         bidding rules to the {@link Auction} aggregate and throwing
 *         {@link AccessDeniedException} when the user lacks permission,
 *         {@link InstanceNotFoundException} when the user, auction, or listing does not exist,
 *         and {@link IllegalArgumentException} when the bidder is the seller;</li>
 *     <li>retrieves all auctions via {@code findAll};</li>
 *     <li>retrieves a single auction by identifier via {@code findById},
 *         throwing {@link InstanceNotFoundException} when not found;</li>
 *     <li>retrieves auctions by listing identifiers via {@code findByListingIdIn},
 *         throwing {@link NullPointerException} when the list is {@code null};</li>
 *     <li>retrieves auctions by listing identifiers sorted by end date via
 *         {@code findByListingIdInOrderByAuctionDeadlineAsc},
 *         throwing {@link NullPointerException} when the list is {@code null};</li>
 *     <li>retrieves auctions filtered by genre, author, publisher, or bibliographical data
 *         via {@code findAuctions}, including sorting by deadline when filtering by genre.</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
class AuctionServiceTest {

    // ======================================
    // ========== Set Up ====================
    // ======================================

    @Mock private AuctionFactory auctionFactoryDouble;
    @Mock private IAuctionRepository auctionRepoDouble;
    @Mock private IListingRepository listingRepoDouble;
    @Mock private IPublicationSampleRepository pubSampleRepoDouble;
    @Mock private IBibliographicalDataRepository bdRepoDouble;
    @Mock private IEditionRepository editionRepoDouble;
    @Mock private IUserRepository userRepoDouble;
    @Mock private AuctionAuthorizationPolicy policyDouble;

    @InjectMocks private AuctionService auctionService;

    @Mock private AuctionId auctionIdDouble;
    @Mock private ListingId listingIdDouble1;
    @Mock private ListingId listingIdDouble2;
    @Mock private GenreId genreIdDouble;
    @Mock private AuthorId authorIdDouble;
    @Mock private PublisherID publisherIdDouble;
    @Mock private BibliographicalDataId bdIdDouble;
    @Mock private PublicationSampleId pubSampleIdDouble;
    @Mock private EditionId editionIdDouble;
    @Mock private UserId bidderIdDouble;
    @Mock private UserId sellerIdDouble;

    @Mock private Auction auctionDouble;
    @Mock private Listing listingDouble;
    @Mock private User userDouble;
    @Mock private Bid bidDouble;

    private final LocalDateTime startDate = LocalDateTime.now().plusDays(1);
    private final LocalDateTime endDate = startDate.plusDays(5);
    private final Price initialPrice = new Price(new BigDecimal("10.00"));
    private final Price buyOutPrice = new Price(new BigDecimal("100.00"));

    // =============================================
    // ========== constructor ======================
    // =============================================

    @Test
    void constructor_shouldReturnServiceWhenDependenciesAreValid() {
        // Act + Assert
        assertDoesNotThrow(() -> new AuctionService(auctionFactoryDouble, auctionRepoDouble, listingRepoDouble,
                pubSampleRepoDouble, bdRepoDouble, editionRepoDouble, userRepoDouble, policyDouble));
    }

    @Test
    void constructor_shouldThrowErrorWhenAuctionFactoryIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new AuctionService(null, auctionRepoDouble,
                listingRepoDouble, pubSampleRepoDouble, bdRepoDouble, editionRepoDouble, userRepoDouble, policyDouble));
    }

    @Test
    void constructor_shouldThrowErrorWhenAuctionRepositoryIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new AuctionService(auctionFactoryDouble, null,
                listingRepoDouble, pubSampleRepoDouble, bdRepoDouble, editionRepoDouble, userRepoDouble, policyDouble));
    }

    @Test
    void constructor_shouldThrowErrorWhenListingRepositoryIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new AuctionService(auctionFactoryDouble, auctionRepoDouble, null, pubSampleRepoDouble,
                        bdRepoDouble, editionRepoDouble, userRepoDouble, policyDouble));
    }

    @Test
    void constructor_shouldThrowErrorWhenPubSampleRepositoryIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new AuctionService(auctionFactoryDouble, auctionRepoDouble, listingRepoDouble, null,
                        bdRepoDouble, editionRepoDouble, userRepoDouble, policyDouble));
    }

    @Test
    void constructor_shouldThrowErrorWhenBibliographicalDataRepositoryIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new AuctionService(auctionFactoryDouble, auctionRepoDouble, listingRepoDouble, pubSampleRepoDouble,
                        null, editionRepoDouble, userRepoDouble, policyDouble));
    }

    @Test
    void constructor_shouldThrowErrorWhenEditionRepositoryIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new AuctionService(auctionFactoryDouble, auctionRepoDouble, listingRepoDouble, pubSampleRepoDouble,
                        bdRepoDouble, null, userRepoDouble, policyDouble));
    }

    @Test
    void constructor_shouldThrowErrorWhenUserRepositoryIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new AuctionService(auctionFactoryDouble, auctionRepoDouble, listingRepoDouble, pubSampleRepoDouble,
                        bdRepoDouble, editionRepoDouble, null, policyDouble));
    }

    @Test
    void constructor_shouldThrowErrorWhenPolicyIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new AuctionService(auctionFactoryDouble, auctionRepoDouble, listingRepoDouble, pubSampleRepoDouble,
                        bdRepoDouble, editionRepoDouble, userRepoDouble, null));
    }

    // =============================================
    // ========== createAndSave ====================
    // =============================================

    @Test
    void creatAndSave_shouldCreateAndSaveAuctionSuccessfully() throws Exception {
        // Arrange
        when(listingRepoDouble.findById(listingIdDouble1)).thenReturn(Optional.of(listingDouble));
        when(listingDouble.getSaleType()).thenReturn(SaleType.AUCTION);
        when(auctionFactoryDouble.create(listingIdDouble1, startDate, endDate, initialPrice, buyOutPrice)).thenReturn(auctionDouble);
        when(auctionRepoDouble.save(auctionDouble)).thenReturn(auctionDouble);
        when(userRepoDouble.findByID(sellerIdDouble)).thenReturn(Optional.of(userDouble));
        when(policyDouble.canCreateAuction(userDouble)).thenReturn(true);

        // Act
        Auction result = auctionService.createAndSave(listingIdDouble1, startDate, endDate, initialPrice, buyOutPrice, sellerIdDouble);

        // Assert
        assertEquals(auctionDouble, result);
    }

    @Test
    void createAndSave_shouldThrowExceptionWhenAuctionAlreadyExists() throws Exception {
        // Arrange
        when(listingRepoDouble.findById(listingIdDouble1)).thenReturn(Optional.of(listingDouble));
        when(listingDouble.getSaleType()).thenReturn(SaleType.AUCTION);
        when(auctionFactoryDouble.create(listingIdDouble1, startDate, endDate, initialPrice, buyOutPrice)).thenReturn(auctionDouble);
        when(auctionRepoDouble.save(auctionDouble)).thenThrow(new InstanceAlreadyExistsException());
        when(userRepoDouble.findByID(sellerIdDouble)).thenReturn(Optional.of(userDouble));
        when(policyDouble.canCreateAuction(userDouble)).thenReturn(true);

        // Act + Assert
        assertThrows(InstanceAlreadyExistsException.class, () ->
                auctionService.createAndSave(listingIdDouble1, startDate, endDate, initialPrice, buyOutPrice, sellerIdDouble)
        );
    }

    @Test
    void createAndSave_shouldThrowWhenListingNotFound() {
        // Arrange
        when(listingRepoDouble.findById(listingIdDouble1)).thenReturn(Optional.empty());
        when(userRepoDouble.findByID(sellerIdDouble)).thenReturn(Optional.of(userDouble));
        when(policyDouble.canCreateAuction(userDouble)).thenReturn(true);

        // Act + Assert
        assertThrows(InstanceNotFoundException.class,
                () -> auctionService.createAndSave(listingIdDouble1, startDate, endDate, initialPrice, buyOutPrice, sellerIdDouble));
    }

    @Test
    void createAndSave_shouldThrowWhenListingIsNotOfTypeAuction() {
        // Arrange
        when(listingRepoDouble.findById(listingIdDouble1)).thenReturn(Optional.of(listingDouble));
        when(listingDouble.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(userRepoDouble.findByID(sellerIdDouble)).thenReturn(Optional.of(userDouble));
        when(policyDouble.canCreateAuction(userDouble)).thenReturn(true);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> auctionService.createAndSave(listingIdDouble1, startDate, endDate, initialPrice, buyOutPrice, sellerIdDouble));
    }

    @Test
    void createAndSave_shouldThrowWhenUserNotFound() {
        // Arrange
        when(userRepoDouble.findByID(sellerIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(InstanceNotFoundException.class,
                () -> auctionService.createAndSave(listingIdDouble1, startDate, endDate, initialPrice, buyOutPrice, sellerIdDouble));
    }

    @Test
    void createAndSave_shouldThrowWhenUserDoesNotHavePermission() {
        // Arrange
        when(userRepoDouble.findByID(sellerIdDouble)).thenReturn(Optional.of(userDouble));
        when(policyDouble.canCreateAuction(userDouble)).thenReturn(false);

        // Act + Assert
        assertThrows(AccessDeniedException.class,
                () -> auctionService.createAndSave(listingIdDouble1, startDate, endDate, initialPrice, buyOutPrice, sellerIdDouble));
    }

    @Test
    void createAndSave_shouldThrow_whenListingAlreadyHasActiveAuction() {
        // Arrange
        when(userRepoDouble.findByID(sellerIdDouble)).thenReturn(Optional.of(userDouble));
        when(policyDouble.canCreateAuction(userDouble)).thenReturn(true);
        when(listingRepoDouble.findById(listingIdDouble1)).thenReturn(Optional.of(listingDouble));
        when(listingDouble.getSaleType()).thenReturn(SaleType.AUCTION);
        when(auctionRepoDouble.findByListingIdIn(List.of(listingIdDouble1))).thenReturn(List.of(auctionDouble));
        when(auctionDouble.getAuctionStatus()).thenReturn(AuctionStatus.ACTIVE);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> auctionService.createAndSave(listingIdDouble1, startDate, endDate, initialPrice, buyOutPrice, sellerIdDouble));
    }

    @Test
    void createAndSave_shouldThrow_whenListingAlreadyHasScheduledAuction() {
        // Arrange
        when(userRepoDouble.findByID(sellerIdDouble)).thenReturn(Optional.of(userDouble));
        when(policyDouble.canCreateAuction(userDouble)).thenReturn(true);
        when(listingRepoDouble.findById(listingIdDouble1)).thenReturn(Optional.of(listingDouble));
        when(listingDouble.getSaleType()).thenReturn(SaleType.AUCTION);
        when(auctionRepoDouble.findByListingIdIn(List.of(listingIdDouble1))).thenReturn(List.of(auctionDouble));
        when(auctionDouble.getAuctionStatus()).thenReturn(AuctionStatus.SCHEDULED);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> auctionService.createAndSave(listingIdDouble1, startDate, endDate, initialPrice, buyOutPrice, sellerIdDouble));
    }

    @Test
    void createAndSave_shouldThrow_whenListingAlreadyHasDraftAuction() {
        // Arrange
        when(userRepoDouble.findByID(sellerIdDouble)).thenReturn(Optional.of(userDouble));
        when(policyDouble.canCreateAuction(userDouble)).thenReturn(true);
        when(listingRepoDouble.findById(listingIdDouble1)).thenReturn(Optional.of(listingDouble));
        when(listingDouble.getSaleType()).thenReturn(SaleType.AUCTION);
        when(auctionRepoDouble.findByListingIdIn(List.of(listingIdDouble1))).thenReturn(List.of(auctionDouble));
        when(auctionDouble.getAuctionStatus()).thenReturn(AuctionStatus.DRAFT);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> auctionService.createAndSave(listingIdDouble1, startDate, endDate, initialPrice, buyOutPrice, sellerIdDouble));
    }

    // =============================================
    // ========== placeBid =========================
    // =============================================

    @Test
    void placeBid_shouldReturnBidWhenBidIsValid() throws Exception {
        // Arrange
        when(auctionRepoDouble.findById(auctionIdDouble)).thenReturn(Optional.of(auctionDouble));
        when(auctionDouble.getListingId()).thenReturn(listingIdDouble1);
        when(listingRepoDouble.findById(listingIdDouble1)).thenReturn(Optional.of(listingDouble));
        when(listingDouble.getSellerId()).thenReturn(sellerIdDouble);
        when(auctionDouble.placeBid(bidderIdDouble, initialPrice)).thenReturn(bidDouble);
        when(auctionRepoDouble.update(auctionDouble)).thenReturn(auctionDouble);
        when(userRepoDouble.findByID(bidderIdDouble)).thenReturn(Optional.of(userDouble));
        when(policyDouble.canPlaceBid(userDouble)).thenReturn(true);

        // Act
        Bid result = auctionService.placeBid(auctionIdDouble, bidderIdDouble, initialPrice);

        // Assert
        assertEquals(bidDouble, result);
    }

    @Test
    void placeBid_shouldThrowWhenUserNotFound() {
        // Arrange
        when(userRepoDouble.findByID(bidderIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(InstanceNotFoundException.class,
                () -> auctionService.placeBid(auctionIdDouble, bidderIdDouble, initialPrice));
    }

    @Test
    void placeBid_shouldThrowWhenUserDoesNotHavePermission() {
        // Arrange
        when(userRepoDouble.findByID(bidderIdDouble)).thenReturn(Optional.of(userDouble));
        when(policyDouble.canPlaceBid(userDouble)).thenReturn(false);

        // Act + Assert
        assertThrows(AccessDeniedException.class,
                () -> auctionService.placeBid(auctionIdDouble, bidderIdDouble, initialPrice));
    }

    @Test
    void placeBid_shouldThrowWhenAuctionNotFound() {
        // Arrange
        when(auctionRepoDouble.findById(auctionIdDouble)).thenReturn(Optional.empty());
        when(userRepoDouble.findByID(bidderIdDouble)).thenReturn(Optional.of(userDouble));
        when(policyDouble.canPlaceBid(userDouble)).thenReturn(true);

        // Act + Assert
        assertThrows(InstanceNotFoundException.class,
                () -> auctionService.placeBid(auctionIdDouble, bidderIdDouble, initialPrice));
    }

    @Test
    void placeBid_shouldThrowWhenListingNotFound() {
        // Arrange
        when(auctionRepoDouble.findById(auctionIdDouble)).thenReturn(Optional.of(auctionDouble));
        when(auctionDouble.getListingId()).thenReturn(listingIdDouble1);
        when(listingRepoDouble.findById(listingIdDouble1)).thenReturn(Optional.empty());
        when(userRepoDouble.findByID(bidderIdDouble)).thenReturn(Optional.of(userDouble));
        when(policyDouble.canPlaceBid(userDouble)).thenReturn(true);

        // Act + Assert
        assertThrows(InstanceNotFoundException.class,
                () -> auctionService.placeBid(auctionIdDouble, bidderIdDouble, initialPrice));
    }

    @Test
    void placeBid_shouldThrowWhenBidderIsSeller() {
        // Arrange
        when(auctionRepoDouble.findById(auctionIdDouble)).thenReturn(Optional.of(auctionDouble));
        when(auctionDouble.getListingId()).thenReturn(listingIdDouble1);
        when(listingRepoDouble.findById(listingIdDouble1)).thenReturn(Optional.of(listingDouble));
        when(listingDouble.getSellerId()).thenReturn(bidderIdDouble);
        when(userRepoDouble.findByID(bidderIdDouble)).thenReturn(Optional.of(userDouble));
        when(policyDouble.canPlaceBid(userDouble)).thenReturn(true);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> auctionService.placeBid(auctionIdDouble, bidderIdDouble, initialPrice));
    }

    // =============================================
    // ========== findAll ==========================
    // =============================================

    @Test
    void findAll_shouldReturnAllAuctions() {
        // Arrange
        when(auctionRepoDouble.findAll()).thenReturn(List.of(auctionDouble));

        // Act
        List<Auction> result = auctionService.findAll();

        // Assert
        assertEquals(List.of(auctionDouble), result);
    }

    @Test
    void findAll_shouldReturnEmptyListWhenNoAuctionsExist() {
        // Arrange
        when(auctionRepoDouble.findAll()).thenReturn(List.of());

        // Act
        List<Auction> result = auctionService.findAll();

        // Assert
        assertTrue(result.isEmpty());
    }

    // =============================================
    // ========== findById =========================
    // =============================================

    @Test
    void findById_shouldReturnAuctionWhenAuctionExists() throws InstanceNotFoundException {
        // Arrange
        when(auctionRepoDouble.findById(auctionIdDouble)).thenReturn(Optional.of(auctionDouble));

        // Act
        Auction result = auctionService.findById(auctionIdDouble);

        // Assert
        assertEquals(auctionDouble, result);
    }

    @Test
    void findById_shouldThrowErrorWhenAuctionDoesNotExist() {
        // Arrange
        when(auctionRepoDouble.findById(auctionIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(InstanceNotFoundException.class, () -> auctionService.findById(auctionIdDouble));
    }

    // =============================================
    // ========== findByListingIdIn ================
    // =============================================

    @Test
    void findByListingIdIn_shouldReturnAuctionsWhenListingsExist() {
        // Arrange
        List<ListingId> listingIds = List.of(listingIdDouble1, listingIdDouble2);
        when(auctionRepoDouble.findByListingIdIn(listingIds)).thenReturn(List.of(auctionDouble));

        // Act
        List<Auction> result = auctionService.findByListingIdIn(listingIds);

        // Assert
        assertEquals(List.of(auctionDouble), result);
    }

    @Test
    void findByListingIdIn_shouldReturnEmptyListWhenNoAuctionsFound() {
        // Arrange
        List<ListingId> listingIds = List.of(listingIdDouble1);
        when(auctionRepoDouble.findByListingIdIn(listingIds)).thenReturn(List.of());

        // Act
        List<Auction> result = auctionService.findByListingIdIn(listingIds);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByListingIdIn_shouldThrowWhenListingIdsIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> auctionService.findByListingIdIn(null));
    }

    // =====================================================================
    // ========== findByListingIdInOrderByAuctionDeadlineAsc ===============
    // =====================================================================

    @Test
    void findByListingIdInOrderByAuctionDeadlineAscShouldReturnSortedAuctions() {
        // Arrange
        List<ListingId> listingIds = List.of(listingIdDouble1);
        when(auctionRepoDouble.findByListingIdInOrderByAuctionDeadlineAsc(listingIds)).thenReturn(List.of(auctionDouble));

        // Act
        List<Auction> result = auctionService.findByListingIdInOrderByAuctionDeadlineAsc(listingIds);

        // Assert
        assertEquals(List.of(auctionDouble), result);
    }

    @Test
    void findByListingIdInOrderByAuctionDeadlineAscShouldReturnEmptyList_whenNoAuctionsFound() {
        // Arrange
        List<ListingId> listingIds = List.of(listingIdDouble1);
        when(auctionRepoDouble.findByListingIdInOrderByAuctionDeadlineAsc(listingIds)).thenReturn(List.of());

        // Act
        List<Auction> result = auctionService.findByListingIdInOrderByAuctionDeadlineAsc(listingIds);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByListingIdInOrderByAuctionDeadlineAscShouldThrow_whenListingIdsIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> auctionService.findByListingIdInOrderByAuctionDeadlineAsc(null));
    }

    // =============================================
    // ========== findAuctions =====================
    // =============================================

    @Test
    void findAuctions_shouldReturnAllAuctions_whenNoFilterProvided() {
        // Arrange
        when(auctionRepoDouble.findAll()).thenReturn(List.of(auctionDouble));

        // Act
        List<Auction> result = auctionService.findAuctions(null, null, null,
                null, null, false);

        // Assert
        assertEquals(List.of(auctionDouble), result);
    }

    @Test
    void findAuctions_shouldReturnAuctions_whenFilteredByGenre() {
        // Arrange
        when(bdRepoDouble.findAllByGenreId(genreIdDouble)).thenReturn(List.of(bdIdDouble));
        when(pubSampleRepoDouble.findIdsByBibliographicalDataIdIn(List.of(bdIdDouble))).thenReturn(List.of(pubSampleIdDouble));
        when(listingRepoDouble.findAllByAuctionAndPublicationSampleIdIn(List.of(pubSampleIdDouble))).thenReturn(List.of(listingDouble));
        when(listingDouble.id()).thenReturn(listingIdDouble1);
        when(auctionRepoDouble.findByListingIdIn(List.of(listingIdDouble1))).thenReturn(List.of(auctionDouble));

        // Act
        List<Auction> result = auctionService.findAuctions(genreIdDouble, null, null,
                null, null, false);

        // Assert
        assertEquals(List.of(auctionDouble), result);
    }

    @Test
    void findAuctions_shouldReturnAuctionsSortedByDeadline_whenFilteredByGenreWithSortByDeadline() {
        // Arrange
        when(bdRepoDouble.findAllByGenreId(genreIdDouble)).thenReturn(List.of(bdIdDouble));
        when(pubSampleRepoDouble.findIdsByBibliographicalDataIdIn(List.of(bdIdDouble))).thenReturn(List.of(pubSampleIdDouble));
        when(listingRepoDouble.findAllByAuctionAndPublicationSampleIdIn(List.of(pubSampleIdDouble))).thenReturn(List.of(listingDouble));
        when(listingDouble.id()).thenReturn(listingIdDouble1);
        when(auctionRepoDouble.findByListingIdInOrderByAuctionDeadlineAsc(List.of(listingIdDouble1))).thenReturn(List.of(auctionDouble));

        // Act
        List<Auction> result = auctionService.findAuctions(genreIdDouble, null, null,
                null, null, true);

        // Assert
        assertEquals(List.of(auctionDouble), result);
    }

    @Test
    void findAuctions_shouldReturnAuctions_whenFilteredByAuthor() {
        // Arrange
        when(bdRepoDouble.findIdByAuthorId(authorIdDouble)).thenReturn(List.of(bdIdDouble));
        when(pubSampleRepoDouble.findIdsByBibliographicalDataIdIn(List.of(bdIdDouble))).thenReturn(List.of(pubSampleIdDouble));
        when(listingRepoDouble.findAllByAuctionAndPublicationSampleIdIn(List.of(pubSampleIdDouble))).thenReturn(List.of(listingDouble));
        when(listingDouble.id()).thenReturn(listingIdDouble1);
        when(auctionRepoDouble.findByListingIdIn(List.of(listingIdDouble1))).thenReturn(List.of(auctionDouble));

        // Act
        List<Auction> result = auctionService.findAuctions(null, authorIdDouble, null,
                null, null, false);

        // Assert
        assertEquals(List.of(auctionDouble), result);
    }

    @Test
    void findAuctions_shouldReturnAuctions_whenFilteredByPublisher() {
        // Arrange
        when(editionRepoDouble.findIdsByPublisherId(publisherIdDouble)).thenReturn(List.of(editionIdDouble));
        when(pubSampleRepoDouble.findIdsByEditionIdIn(List.of(editionIdDouble))).thenReturn(List.of(pubSampleIdDouble));
        when(listingRepoDouble.findAllByAuctionAndPublicationSampleIdIn(List.of(pubSampleIdDouble))).thenReturn(List.of(listingDouble));
        when(listingDouble.id()).thenReturn(listingIdDouble1);
        when(auctionRepoDouble.findByListingIdIn(List.of(listingIdDouble1))).thenReturn(List.of(auctionDouble));

        // Act
        List<Auction> result = auctionService.findAuctions(null, null, publisherIdDouble,
                null, null, false);

        // Assert
        assertEquals(List.of(auctionDouble), result);
    }

    @Test
    void findAuctions_shouldReturnAuctions_whenFilteredByBibliographicalData() {
        // Arrange
        when(pubSampleRepoDouble.findIdsByBibliographicalDataId(bdIdDouble)).thenReturn(List.of(pubSampleIdDouble));
        when(listingRepoDouble.findAllByAuctionAndPublicationSampleIdIn(List.of(pubSampleIdDouble))).thenReturn(List.of(listingDouble));
        when(listingDouble.id()).thenReturn(listingIdDouble1);
        when(auctionRepoDouble.findByListingIdIn(List.of(listingIdDouble1))).thenReturn(List.of(auctionDouble));

        // Act
        List<Auction> result = auctionService.findAuctions(null, null, null,
                bdIdDouble, null, false);

        // Assert
        assertEquals(List.of(auctionDouble), result);
    }

    @Test
    void findAuctions_shouldReturnEmptyList_whenNoAuctionsFound() {
        // Arrange
        when(auctionRepoDouble.findAll()).thenReturn(List.of());

        // Act
        List<Auction> result = auctionService.findAuctions(null, null, null,
                null,null, false);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAuctions_shouldReturnEmptyList_whenGenreHasNoBibliographicalData() {
        // Arrange
        when(bdRepoDouble.findAllByGenreId(genreIdDouble)).thenReturn(List.of());

        // Act
        List<Auction> result = auctionService.findAuctions(genreIdDouble, null, null,
                null, null, false);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAuctions_shouldReturnEmptyList_whenGenreHasNoPublicationSamples() {
        // Arrange
        when(bdRepoDouble.findAllByGenreId(genreIdDouble)).thenReturn(List.of(bdIdDouble));
        when(pubSampleRepoDouble.findIdsByBibliographicalDataIdIn(List.of(bdIdDouble))).thenReturn(List.of());

        // Act
        List<Auction> result = auctionService.findAuctions(genreIdDouble, null, null,
                null, null, false);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAuctions_shouldReturnEmptyList_whenGenreHasNoListings() {
        // Arrange
        when(bdRepoDouble.findAllByGenreId(genreIdDouble)).thenReturn(List.of(bdIdDouble));
        when(pubSampleRepoDouble.findIdsByBibliographicalDataIdIn(List.of(bdIdDouble))).thenReturn(List.of(pubSampleIdDouble));
        when(listingRepoDouble.findAllByAuctionAndPublicationSampleIdIn(List.of(pubSampleIdDouble))).thenReturn(List.of());

        // Act
        List<Auction> result = auctionService.findAuctions(genreIdDouble, null, null,
                null, null, false);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAuctions_shouldReturnEmptyList_whenAuthorHasNoBibliographicalData() {
        // Arrange
        when(bdRepoDouble.findIdByAuthorId(authorIdDouble)).thenReturn(List.of());

        // Act
        List<Auction> result = auctionService.findAuctions(null, authorIdDouble, null,
                null,null, false);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAuctions_shouldReturnEmptyList_whenAuthorHasNoPublicationSamples() {
        // Arrange
        when(bdRepoDouble.findIdByAuthorId(authorIdDouble)).thenReturn(List.of(bdIdDouble));
        when(pubSampleRepoDouble.findIdsByBibliographicalDataIdIn(List.of(bdIdDouble))).thenReturn(List.of());

        // Act
        List<Auction> result = auctionService.findAuctions(null, authorIdDouble, null,
                null,null, false);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAuctions_shouldReturnEmptyList_whenAuthorHasNoListings() {
        // Arrange
        when(bdRepoDouble.findIdByAuthorId(authorIdDouble)).thenReturn(List.of(bdIdDouble));
        when(pubSampleRepoDouble.findIdsByBibliographicalDataIdIn(List.of(bdIdDouble))).thenReturn(List.of(pubSampleIdDouble));
        when(listingRepoDouble.findAllByAuctionAndPublicationSampleIdIn(List.of(pubSampleIdDouble))).thenReturn(List.of());

        // Act
        List<Auction> result = auctionService.findAuctions(null, authorIdDouble, null,
                null,null, false);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAuctions_shouldReturnEmptyList_whenPublisherHasNoEditions() {
        // Arrange
        when(editionRepoDouble.findIdsByPublisherId(publisherIdDouble)).thenReturn(List.of());

        // Act
        List<Auction> result = auctionService.findAuctions(null, null, publisherIdDouble,
                null,null, false);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAuctions_shouldReturnEmptyList_whenPublisherHasNoPublicationSamples() {
        // Arrange
        when(editionRepoDouble.findIdsByPublisherId(publisherIdDouble)).thenReturn(List.of(editionIdDouble));
        when(pubSampleRepoDouble.findIdsByEditionIdIn(List.of(editionIdDouble))).thenReturn(List.of());

        // Act
        List<Auction> result = auctionService.findAuctions(null, null, publisherIdDouble,
                null,null, false);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAuctions_shouldReturnEmptyList_whenPublisherHasNoListings() {
        // Arrange
        when(editionRepoDouble.findIdsByPublisherId(publisherIdDouble)).thenReturn(List.of(editionIdDouble));
        when(pubSampleRepoDouble.findIdsByEditionIdIn(List.of(editionIdDouble))).thenReturn(List.of(pubSampleIdDouble));
        when(listingRepoDouble.findAllByAuctionAndPublicationSampleIdIn(List.of(pubSampleIdDouble))).thenReturn(List.of());

        // Act
        List<Auction> result = auctionService.findAuctions(null, null, publisherIdDouble,
                null, null, false);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAuctions_shouldReturnEmptyList_whenBibliographicalDataHasNoPublicationSamples() {
        // Arrange
        when(pubSampleRepoDouble.findIdsByBibliographicalDataId(bdIdDouble)).thenReturn(List.of());

        // Act
        List<Auction> result = auctionService.findAuctions(null, null, null, bdIdDouble,
                null,false);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAuctions_shouldReturnEmptyList_whenBibliographicalDataHasNoListings() {
        // Arrange
        when(pubSampleRepoDouble.findIdsByBibliographicalDataId(bdIdDouble)).thenReturn(List.of(pubSampleIdDouble));
        when(listingRepoDouble.findAllByAuctionAndPublicationSampleIdIn(List.of(pubSampleIdDouble))).thenReturn(List.of());

        // Act
        List<Auction> result = auctionService.findAuctions(null, null, null, bdIdDouble,
                null,false);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAuctions_shouldReturnAuctions_whenFilteredByListingId() {
        // Arrange
        when(auctionRepoDouble.findByListingIdIn(List.of(listingIdDouble1))).thenReturn(List.of(auctionDouble));

        // Act
        List<Auction> result = auctionService.findAuctions(null, null, null,
                null, listingIdDouble1, false);

        // Assert
        assertEquals(List.of(auctionDouble), result);
    }

    @Test
    void findAuctions_shouldReturnEmptyList_whenListingHasNoAuctions() {
        // Arrange
        when(auctionRepoDouble.findByListingIdIn(List.of(listingIdDouble1))).thenReturn(List.of());

        // Act
        List<Auction> result = auctionService.findAuctions(null, null, null,
                null, listingIdDouble1, false);

        // Assert
        assertTrue(result.isEmpty());
    }
}