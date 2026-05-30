package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.AuctionAuthorizationPolicy;
import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Auction.AuctionFactory;
import TOPSECRET.domain.Auction.Bid;
import TOPSECRET.domain.Auction.IAuctionRepository;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.AuctionId;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.ValueObjects.UserId;
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
 *         throwing {@link java.nio.file.AccessDeniedException} when the user lacks permission,
 *         {@link InstanceNotFoundException} when the user or listing does not exist,
 *         {@link IllegalArgumentException} when the listing is not of type AUCTION,
 *         and {@link InstanceAlreadyExistsException} when the auction already exists;</li>
 *     <li>correctly delegates persistence to the {@link IAuctionRepository};</li>
 *     <li>returns the persisted {@link Auction} instance after creation;</li>
 *     <li>places a bid on an existing auction via {@code placeBid}, delegating
 *         bidding rules to the {@link Auction} aggregate and throwing
 *         {@link java.nio.file.AccessDeniedException} when the user lacks permission,
 *         {@link InstanceNotFoundException} when the user, auction, or listing does not exist,
 *         and {@link IllegalArgumentException} when the bidder is the seller;</li>
 *     <li>retrieves all auctions via {@code findAll};</li>
 *     <li>retrieves a single auction by identifier via {@code findById},
 *         throwing {@link InstanceNotFoundException} when not found;</li>
 *     <li>retrieves auctions by listing identifiers via {@code findByListingIdIn},
 *         throwing {@link NullPointerException} when the list is {@code null};</li>
 *     <li>retrieves auctions by listing identifiers sorted by end date via
 *         {@code findByListingIdInOrderByAuctionDeadlineAsc},
 *         throwing {@link NullPointerException} when the list is {@code null}.</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
class AuctionServiceTest {

    // ======================================
    // ========== Set Up ====================
    // ======================================

    @Mock
    private AuctionFactory auctionFactoryDouble;

    @Mock
    private IAuctionRepository auctionRepoDouble;

    @Mock
    private IListingRepository listingRepoDouble;

    @Mock
    private IUserRepository userRepoDouble;

    @Mock
    private AuctionAuthorizationPolicy policyDouble;

    @Mock
    private AuctionId auctionIdDouble;

    @Mock
    private Listing listingDouble;

    @Mock
    private ListingId listingIdDouble1;
    @Mock
    private ListingId listingIdDouble2;

    @Mock
    private Auction auctionDouble;

    @Mock
    private UserId bidderIdDouble;
    @Mock
    private UserId sellerIdDouble;

    @Mock
    private User userDouble;

    @Mock
    private Bid bidDouble;

    @InjectMocks
    private AuctionService auctionService;

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
                userRepoDouble, policyDouble));
    }

    @Test
    void constructor_shouldThrowErrorWhenAuctionRepositoryIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new AuctionService(null, auctionRepoDouble,
                listingRepoDouble, userRepoDouble, policyDouble));
    }

    @Test
    void constructor_shouldThrowErrorWhenAuctionFactoryIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new AuctionService(auctionFactoryDouble, null,
                listingRepoDouble, userRepoDouble, policyDouble));
    }

    @Test
    void constructor_shouldThrowErrorWhenListingRepositoryIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new AuctionService(auctionFactoryDouble, auctionRepoDouble, null, userRepoDouble, policyDouble));
    }

    @Test
    void constructor_shouldThrowErrorWhenUserRepositoryIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new AuctionService(auctionFactoryDouble, auctionRepoDouble, listingRepoDouble, null, policyDouble));
    }

    @Test
    void constructor_shouldThrowErrorWhenPolicyIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new AuctionService(auctionFactoryDouble, auctionRepoDouble, listingRepoDouble, userRepoDouble, null));
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
    void createAndSave_shouldThrowWhenUserDoesNotHavePermission() throws Exception {
        // Arrange
        when(userRepoDouble.findByID(sellerIdDouble)).thenReturn(Optional.of(userDouble));
        when(policyDouble.canCreateAuction(userDouble)).thenReturn(false);

        // Act + Assert
        assertThrows(AccessDeniedException.class,
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
    void placeBid_shouldThrowWhenUserDoesNotHavePermission() throws Exception {
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
}