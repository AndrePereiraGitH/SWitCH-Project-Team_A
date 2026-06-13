package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.Listing.ListingFactory;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test suite for the in‑memory implementation of the Listing repository
 * ({@link ListingRepositoryMem}), validating its behaviour in storing,
 * retrieving, and filtering {@link Listing} aggregates.
 *
 * <p>This suite ensures that the repository enforces all required invariants and
 * behaves consistently with domain expectations, including:
 * <ul>
 *     <li>correct handling of listing creation through the {@link ListingFactory};</li>
 *     <li>prevention of duplicate listings via {@code InstanceAlreadyExistsException};</li>
 *     <li>successful creation and storage of new listings with distinct identities;</li>
 *     <li>accurate filtering of listings by sale type (DIRECT_SALE vs AUCTION);</li>
 *     <li>return of defensive copies for all query methods, ensuring repository
 *         encapsulation and preventing external mutation;</li>
 *     <li>robust handling of edge cases such as empty repositories or absence of
 *         listings matching a given filter.</li>
 * </ul>
 *
 * <p>Overall, this test class provides comprehensive coverage of the in‑memory
 * repository implementation, ensuring predictable behaviour and strong
 * encapsulation in support of the Listing aggregate lifecycle.
 */
class ListingRepositoryMemTest {

    // ======================================================
    // ================== Set Up ============================
    // ======================================================

    Listing listing1Double;
    Listing listing2Double;
    Listing listing3Double;

    PublicationSampleId pub1IdDouble;
    PublicationSampleId pub2IdDouble;
    PublicationSampleId pub3IdDouble;

    Price price1;
    Price price2;
    Price price3;

    Picture pic1;
    Picture pic2;
    Picture pic3;

    UserId sellerIdDouble;

    @BeforeEach
    void setUp() {

        listing1Double = mock(Listing.class);
        listing2Double = mock(Listing.class);
        listing3Double = mock(Listing.class);

        pub1IdDouble = mock(PublicationSampleId.class);
        pub2IdDouble = mock(PublicationSampleId.class);
        pub3IdDouble = mock(PublicationSampleId.class);

        price1 = mock(Price.class);
        price2 = mock(Price.class);
        price3 = mock(Price.class);

        pic1 = mock(Picture.class);
        pic2 = mock(Picture.class);
        pic3 = mock(Picture.class);

        sellerIdDouble = mock(UserId.class);

        when(listing1Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(listing2Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(listing3Double.getSaleType()).thenReturn(SaleType.AUCTION);
    }

    // ====================================================
    // ====== Save ========================================
    // ====================================================

    @Test
    void save_shouldThrowExceptionWhenListingAlreadyExists() {
        // arrange

        IListingRepository repo = new ListingRepositoryMem();

        repo.save(listing1Double);

        // act + assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.save(listing1Double)
        );
    }

    @Test
    void save_shouldAddAndReturnNewListing() {
        // arrange

        IListingRepository repo = new ListingRepositoryMem();

        // act
        Listing result = repo.save(listing1Double);

        // assert
        assertEquals(listing1Double, result);
        assertTrue(repo.findAll().contains(result));
    }


    @Test
    void save_ShouldAllowDifferentListingsWithDifferentIds() {
        // arrange

        IListingRepository repo = new ListingRepositoryMem();

        // act
        repo.save(listing1Double);
        repo.save(listing2Double);

        // assert
        assertEquals(2, repo.findAll().size());
    }

    // ======================================================
    // ==== Tests to findAllDirectSaleListings===============
    // ======================================================

    @Test
    void findAllDirectSaleListings_ShouldReturnOnlyDirectSaleListings() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        // listing1Double and listing2Double: DIRECT_SALE (stubbed in setUp)
        // listing3Double: AUCTION (stubbed in setUp)
        repo.save(listing1Double);
        repo.save(listing2Double);
        repo.save(listing3Double);

        // Act
        List<Listing> result = repo.findAllByDirectSale();

        // Assert
        assertEquals(2, result.size());
        assertEquals(listing1Double, result.get(0));
        assertEquals(listing2Double, result.get(1));
    }

    @Test
    void findAllDirectSaleListings_ShouldReturnEmptyListWhenNoDirectSaleListingsExist() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        // listing3Double: AUCTION (stubbed in setUp)
        repo.save(listing3Double);

        // Act
        List<Listing> result = repo.findAllByDirectSale();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllDirectSaleListings_ShouldReturnEmptyListWhenRepositoryIsEmpty() {
        // arrange

        IListingRepository repo = new ListingRepositoryMem();

        // Act
        List<Listing> result = repo.findAllByDirectSale();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllDirectSaleListings_ShouldReturnNewIndependentList() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        // listing1Double: DIRECT_SALE (stubbed in setUp)
        repo.save(listing1Double);

        // Act
        List<Listing> firstCall = repo.findAllByDirectSale();
        firstCall.clear(); // tries to modify the list

        List<Listing> secondCall = repo.findAllByDirectSale();

        assertEquals(1, secondCall.size());
    }

    // ======================================================
    // ==== Tests to findAllAuctionListings==================
    // ======================================================

    @Test
    void findAllAuctionListings_ShouldReturnOnlyAuctionListings() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        // listing1Double: DIRECT_SALE, listing2Double: DIRECT_SALE, listing3Double: AUCTION (all stubbed in setUp)
        repo.save(listing1Double);
        repo.save(listing2Double);
        repo.save(listing3Double);

        // Act
        List<Listing> result = repo.findAllByAuction();

        // Assert
        assertEquals(1, result.size());
        assertEquals(listing3Double, result.get(0));
    }

    @Test
    void findAllAuctionListings_ShouldReturnEmptyListWhenNoAuctionListingsExist() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        // listing1Double: DIRECT_SALE (stubbed in setUp)
        repo.save(listing1Double);

        // Act
        List<Listing> result = repo.findAllByAuction();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllAuctionListings_ShouldReturnEmptyListWhenRepositoryIsEmpty() {
        // arrange

        IListingRepository repo = new ListingRepositoryMem();

        // Act
        List<Listing> result = repo.findAllByAuction();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllAuctionListings_ShouldReturnNewIndependentList() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        // listing3Double: AUCTION (stubbed in setUp)
        repo.save(listing3Double);

        // Act
        List<Listing> firstCall = repo.findAllByAuction();
        firstCall.clear(); // tries to modify the list

        List<Listing> secondCall = repo.findAllByAuction();

        assertEquals(1, secondCall.size());
    }

    // ======================================================
    // ========== Tests to getAllListings ===================
    // ======================================================

    @Test
    void getAllListings_ShouldReturnAllListingsInRepository() {
        // arrange

        IListingRepository repo = new ListingRepositoryMem();

        repo.save(listing1Double);
        repo.save(listing2Double);

        // act
        List<Listing> allListings = repo.findAll();

        // assert
        assertEquals(2, allListings.size(), "getAllListings should return all listings added to the repository");
        assertTrue(allListings.contains(listing1Double), "The list should contain listing1Double");
        assertTrue(allListings.contains(listing2Double), "The list should contain listing2Double");
    }

    @Test
    void getAllListings_ShouldReturnUnmodifiableList() {
        // arrange

        IListingRepository repo = new ListingRepositoryMem();

        repo.save(listing1Double);

        // act
        List<Listing> result = repo.findAll();

        // assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(listing2Double));
    }

    // ====================================================
    // ====== FIND LISTING BY ID  =========================
    // ====================================================

    @Test
    void findListingById_shouldThrowExceptionWhenIdIsNull() {
        // arrange

        IListingRepository repo = new ListingRepositoryMem();

        // act + Assert
        assertThrows(IllegalArgumentException.class, () -> {
            repo.findById(null);
        });
    }

    @Test
    void findListingById_shouldReturnListingWhenIdExists() {
        // arrange
        ListingId id1 = new ListingId("id1");
        when(listing1Double.id()).thenReturn(id1);


        IListingRepository repo = new ListingRepositoryMem();

        repo.save(listing1Double);

        // act
        Optional<Listing> result = repo.findById(id1);

        // assert
        assertTrue(result.isPresent());
        assertEquals(listing1Double, result.get());
    }

    @Test
    void findListingById_shouldReturnEmptyWhenIdDoNotExists() {
        // arrange
        ListingId id1 = new ListingId("id1");
        ListingId newId = new ListingId("newId");

        when(listing1Double.id()).thenReturn(id1);


        IListingRepository repo = new ListingRepositoryMem();

        // act (nothing saved — repo is empty)
        Optional<Listing> result = repo.findById(newId);

        // assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findListingById_shouldReturnEmpty_whenIdDoesNotExistInNonEmptyRepository() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();
        Listing listing = mock(Listing.class);
        ListingId presentId = mock(ListingId.class);
        ListingId absentId = mock(ListingId.class);

        when(listing.id()).thenReturn(presentId);
        repo.save(listing);

        // Act
        Optional<Listing> result = repo.findById(absentId);

        // Assert
        assertTrue(result.isEmpty());
    }

    // ======================================================
    // ==== Tests to getAuctionListingsContaining ===========
    // ======================================================

    @Test
    void getAuctionListingsContaining_returnsListings_whenMatchingIdExists() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        when(listing1Double.isListingLinkedToAnAuction()).thenReturn(true);
        when(listing2Double.isListingLinkedToAnAuction()).thenReturn(true);
        when(listing3Double.isListingLinkedToAnAuction()).thenReturn(true);

        when(listing1Double.containsPubSampleId(pub1IdDouble)).thenReturn(true);
        when(listing2Double.containsPubSampleId(pub2IdDouble)).thenReturn(true);
        when(listing3Double.containsPubSampleId(pub3IdDouble)).thenReturn(true);

        repo.save(listing1Double);
        repo.save(listing2Double);
        repo.save(listing3Double);

        // Act
        List<Listing> result = repo.findAllByAuctionAndPublicationSampleIdIn(List.of(pub1IdDouble));

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(listing1Double));
        assertFalse(result.contains(listing2Double));
        assertFalse(result.contains(listing3Double));
    }

    @Test
    void getAuctionListingsContaining_returnsMultipleListings_whenMultipleListingsMatch() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        when(listing1Double.isListingLinkedToAnAuction()).thenReturn(true);
        when(listing2Double.isListingLinkedToAnAuction()).thenReturn(true);
        when(listing3Double.isListingLinkedToAnAuction()).thenReturn(true);

        when(listing1Double.containsPubSampleId(pub1IdDouble)).thenReturn(true);
        when(listing2Double.containsPubSampleId(pub2IdDouble)).thenReturn(true);
        when(listing3Double.containsPubSampleId(pub3IdDouble)).thenReturn(true);

        repo.save(listing1Double);
        repo.save(listing2Double);
        repo.save(listing3Double);

        // Act
        List<Listing> result = repo.findAllByAuctionAndPublicationSampleIdIn(List.of(pub1IdDouble, pub2IdDouble));

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(listing1Double));
        assertTrue(result.contains(listing2Double));
        assertFalse(result.contains(listing3Double));
    }

    @Test
    void getAuctionListingsContaining_excludesListing_whenNotLinkedToAuction() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        when(listing1Double.isListingLinkedToAnAuction()).thenReturn(false);
        when(listing2Double.isListingLinkedToAnAuction()).thenReturn(false);
        when(listing3Double.isListingLinkedToAnAuction()).thenReturn(false);

        when(listing1Double.containsPubSampleId(pub1IdDouble)).thenReturn(true);
        when(listing2Double.containsPubSampleId(pub2IdDouble)).thenReturn(true);
        when(listing3Double.containsPubSampleId(pub3IdDouble)).thenReturn(true);

        repo.save(listing1Double);
        repo.save(listing2Double);
        repo.save(listing3Double);

        // Act
        List<Listing> result = repo.findAllByAuctionAndPublicationSampleIdIn(List.of(pub1IdDouble, pub2IdDouble));

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void getAuctionListingsContaining_excludesListing_whenNoItemIdMatches() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        PublicationSampleId pub4Id = mock(PublicationSampleId.class);

        when(listing1Double.isListingLinkedToAnAuction()).thenReturn(true);
        when(listing2Double.isListingLinkedToAnAuction()).thenReturn(true);
        when(listing3Double.isListingLinkedToAnAuction()).thenReturn(true);

        when(listing1Double.getItemsId()).thenReturn(List.of(pub1IdDouble));
        when(listing2Double.getItemsId()).thenReturn(List.of(pub2IdDouble));
        when(listing3Double.getItemsId()).thenReturn(List.of(pub3IdDouble));

        repo.save(listing1Double);
        repo.save(listing2Double);
        repo.save(listing3Double);

        // Act
        List<Listing> result = repo.findAllByAuctionAndPublicationSampleIdIn(List.of(pub4Id));

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void getAuctionListingsContaining_returnsEmptyList_whenListingsIsEmpty() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        // Act
        List<Listing> result = repo.findAllByAuctionAndPublicationSampleIdIn(List.of(pub1IdDouble));

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void getAuctionListingsContaining_throwsException_whenListIsNull() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                repo.findAllByAuctionAndPublicationSampleIdIn(null)
        );
    }

    @Test
    void getAuctionListingsContaining_throwsException_whenListIsEmpty() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.findAllByAuctionAndPublicationSampleIdIn(List.of())
        );
    }

    @Test
    void findAllListingId_shouldThrowException_whenItemsIdByPublicationSampleIdIsNull() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> repo.findIdsByPublicationSampleIdIn(null));
    }

    @Test
    void findAllListingId_shouldReturnListingIdByPublicationSampleId_whenItemMatches() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        PublicationSampleId itemIdDouble = mock(PublicationSampleId.class);
        Listing listingDouble = mock(Listing.class);
        ListingId listingIdDouble = mock(ListingId.class);

        when(listingDouble.getItemsId()).thenReturn(List.of(itemIdDouble));
        when(listingDouble.id()).thenReturn(listingIdDouble);

        repo.save(listingDouble);

        // Act
        List<ListingId> result = repo.findIdsByPublicationSampleIdIn(List.of(itemIdDouble));

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(listingIdDouble));
    }

    @Test
    void findAllListingIdByPublicationSampleId_shouldReturnEmpty_whenNoItemMatches() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        PublicationSampleId itemDouble1 = mock(PublicationSampleId.class);
        PublicationSampleId itemDouble2 = mock(PublicationSampleId.class);

        when(listing1Double.getItemsId()).thenReturn(List.of(itemDouble1));

        repo.save(listing1Double);

        // Act
        List<ListingId> result = repo.findIdsByPublicationSampleIdIn(List.of(itemDouble2));

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllListingId_shouldReturnMultipleListingIdByPublicationSampleIds_whenMultipleMatches() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        PublicationSampleId itemIdDouble = mock(PublicationSampleId.class);

        Listing listingDouble1 = mock(Listing.class);
        Listing listingDouble2 = mock(Listing.class);

        ListingId idDouble1 = mock(ListingId.class);
        ListingId idDouble2 = mock(ListingId.class);

        when(listingDouble1.getItemsId()).thenReturn(List.of(itemIdDouble));
        when(listingDouble2.getItemsId()).thenReturn(List.of(itemIdDouble));

        when(listingDouble1.id()).thenReturn(idDouble1);
        when(listingDouble2.id()).thenReturn(idDouble2);

        repo.save(listingDouble1);
        repo.save(listingDouble2);

        // Act
        List<ListingId> result = repo.findIdsByPublicationSampleIdIn(List.of(itemIdDouble));

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(idDouble1));
        assertTrue(result.contains(idDouble2));
    }

    // ======================================================
    // ==== Tests for findListingIdsByPublicationSampleIds ==
    // ======================================================

    @Test
    void findListingIdsByPublicationSampleIds_shouldReturnListingIds_whenListingsContainGivenPublicationSampleIds() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        ListingId listingId1 = new ListingId("listing1");
        ListingId listingId2 = new ListingId("listing2");
        ListingId listingId3 = new ListingId("listing3");

        when(listing1Double.getItemsId()).thenReturn(List.of(pub1IdDouble, pub2IdDouble));
        when(listing2Double.getItemsId()).thenReturn(List.of(pub3IdDouble));
        when(listing3Double.getItemsId()).thenReturn(List.of(pub2IdDouble));

        when(listing1Double.id()).thenReturn(listingId1);
        when(listing2Double.id()).thenReturn(listingId2);
        when(listing3Double.id()).thenReturn(listingId3);

        repo.save(listing1Double);
        repo.save(listing2Double);
        repo.save(listing3Double);

        // Act
        List<ListingId> result = repo.findIdsByPublicationSampleIdIn(List.of(pub2IdDouble));

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(listingId1));
        assertTrue(result.contains(listingId3));
        assertFalse(result.contains(listingId2));
    }

    @Test
    void findListingIdsByPublicationSampleIds_shouldReturnSingleListingId_whenOnlyOneListingContainsGivenPublicationSampleIds() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        ListingId listingId1 = new ListingId("listing1");
        ListingId listingId2 = new ListingId("listing2");
        ListingId listingId3 = new ListingId("listing3");

        when(listing1Double.getItemsId()).thenReturn(List.of(pub1IdDouble));
        when(listing2Double.getItemsId()).thenReturn(List.of(pub2IdDouble));
        when(listing3Double.getItemsId()).thenReturn(List.of(pub3IdDouble));

        when(listing1Double.id()).thenReturn(listingId1);
        when(listing2Double.id()).thenReturn(listingId2);
        when(listing3Double.id()).thenReturn(listingId3);

        repo.save(listing1Double);
        repo.save(listing2Double);
        repo.save(listing3Double);

        // Act
        List<ListingId> result = repo.findIdsByPublicationSampleIdIn(List.of(pub1IdDouble));

        // Assert
        assertEquals(1, result.size());
        assertEquals(listingId1, result.get(0));
    }

    @Test
    void findListingIdsByPublicationSampleIds_shouldReturnMultipleListingIds_whenSearchingMultiplePublicationSampleIds() {
        // Arrange
        IListingRepository repo = new ListingRepositoryMem();

        ListingId listingId1 = new ListingId("listing1");
        ListingId listingId2 = new ListingId("listing2");
        ListingId listingId3 = new ListingId("listing3");

        when(listing1Double.getItemsId()).thenReturn(List.of(pub1IdDouble));
        when(listing2Double.getItemsId()).thenReturn(List.of(pub2IdDouble));
        when(listing3Double.getItemsId()).thenReturn(List.of(pub3IdDouble));

        when(listing1Double.id()).thenReturn(listingId1);
        when(listing2Double.id()).thenReturn(listingId2);
        when(listing3Double.id()).thenReturn(listingId3);

        repo.save(listing1Double);
        repo.save(listing2Double);
        repo.save(listing3Double);

        // Act
        List<ListingId> result = repo.findIdsByPublicationSampleIdIn(List.of(pub1IdDouble, pub3IdDouble));

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(listingId1));
        assertTrue(result.contains(listingId3));
        assertFalse(result.contains(listingId2));
    }

    @Test
    void findListingIdsByPublicationSampleIds_shouldReturnEmptyList_whenNoListingContainsGivenPublicationSampleIds() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        PublicationSampleId pub4Id = new PublicationSampleId("pub4Id");

        ListingId listingId1 = new ListingId("listing1");
        ListingId listingId2 = new ListingId("listing2");

        when(listing1Double.getItemsId()).thenReturn(List.of(pub1IdDouble));
        when(listing2Double.getItemsId()).thenReturn(List.of(pub2IdDouble));

        when(listing1Double.id()).thenReturn(listingId1);
        when(listing2Double.id()).thenReturn(listingId2);

        repo.save(listing1Double);
        repo.save(listing2Double);

        // Act
        List<ListingId> result = repo.findIdsByPublicationSampleIdIn(List.of(pub4Id));

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findListingIdsByPublicationSampleIds_shouldReturnEmptyList_whenRepositoryIsEmpty() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        // Act
        List<ListingId> result = repo.findIdsByPublicationSampleIdIn(List.of(pub1IdDouble));

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findListingIdsByPublicationSampleIds_shouldThrowException_whenPublicationSamplesIdsIsNull() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> repo.findIdsByPublicationSampleIdIn(null));
    }


    // ======================================================
// ==== Tests for findByPublicationSampleIdIn ===========
// ======================================================

    @Test
    void findByPublicationSampleIdIn_shouldThrowException_whenPublicationSampleIdsIsNull() {
        // Arrange
        IListingRepository repo = new ListingRepositoryMem();

        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                repo.findByPublicationSampleIdIn(null)
        );
    }

    @Test
    void findByPublicationSampleIdIn_shouldReturnEmptyList_whenRepositoryIsEmpty() {
        // Arrange
        IListingRepository repo = new ListingRepositoryMem();

        // Act
        List<Listing> result = repo.findByPublicationSampleIdIn(List.of(pub1IdDouble));

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByPublicationSampleIdIn_shouldReturnEmptyList_whenNoListingContainsGivenIds() {
        // Arrange
        IListingRepository repo = new ListingRepositoryMem();

        PublicationSampleId pub4IdDouble = mock(PublicationSampleId.class);

        when(listing1Double.getItemsId()).thenReturn(List.of(pub1IdDouble));
        when(listing2Double.getItemsId()).thenReturn(List.of(pub2IdDouble));

        repo.save(listing1Double);
        repo.save(listing2Double);

        // Act
        List<Listing> result = repo.findByPublicationSampleIdIn(List.of(pub4IdDouble));

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByPublicationSampleIdIn_shouldReturnListing_whenSingleIdMatches() {
        // Arrange
        IListingRepository repo = new ListingRepositoryMem();

        when(listing1Double.getItemsId()).thenReturn(List.of(pub1IdDouble));
        when(listing2Double.getItemsId()).thenReturn(List.of(pub2IdDouble));

        repo.save(listing1Double);
        repo.save(listing2Double);

        // Act
        List<Listing> result = repo.findByPublicationSampleIdIn(List.of(pub1IdDouble));

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(listing1Double));
        assertFalse(result.contains(listing2Double));
    }

    @Test
    void findByPublicationSampleIdIn_shouldReturnMultipleListings_whenMultipleIdsMatch() {
        // Arrange
        IListingRepository repo = new ListingRepositoryMem();

        when(listing1Double.getItemsId()).thenReturn(List.of(pub1IdDouble));
        when(listing2Double.getItemsId()).thenReturn(List.of(pub2IdDouble));
        when(listing3Double.getItemsId()).thenReturn(List.of(pub3IdDouble));

        repo.save(listing1Double);
        repo.save(listing2Double);
        repo.save(listing3Double);

        // Act
        List<Listing> result = repo.findByPublicationSampleIdIn(List.of(pub1IdDouble, pub3IdDouble));

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(listing1Double));
        assertTrue(result.contains(listing3Double));
        assertFalse(result.contains(listing2Double));
    }

    @Test
    void findByPublicationSampleIdIn_shouldReturnListingOnce_whenListingContainsMultipleMatchingIds() {
        // Arrange
        IListingRepository repo = new ListingRepositoryMem();

        when(listing1Double.getItemsId()).thenReturn(List.of(pub1IdDouble, pub2IdDouble));

        repo.save(listing1Double);

        // Act
        List<Listing> result = repo.findByPublicationSampleIdIn(List.of(pub1IdDouble, pub2IdDouble));

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(listing1Double));
    }

    @Test
    void findByPublicationSampleIdIn_shouldReturnEmptyList_whenInputListIsEmpty() {
        // Arrange
        IListingRepository repo = new ListingRepositoryMem();

        when(listing1Double.getItemsId()).thenReturn(List.of(pub1IdDouble));

        repo.save(listing1Double);

        // Act
        List<Listing> result = repo.findByPublicationSampleIdIn(List.of());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ======================================================
    // ==== Tests to getDirectSaleListingsContaining ===========
    // ======================================================

    @Test
    void findDirectSaleListingsContaining_returnsListings_whenMatchingIdExists() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        when(listing1Double.isListingLinkedToADirectSale()).thenReturn(true);
        when(listing2Double.isListingLinkedToADirectSale()).thenReturn(true);
        when(listing3Double.isListingLinkedToADirectSale()).thenReturn(true);

        when(listing1Double.containsPubSampleId(pub1IdDouble)).thenReturn(true);
        when(listing2Double.containsPubSampleId(pub2IdDouble)).thenReturn(true);
        when(listing3Double.containsPubSampleId(pub3IdDouble)).thenReturn(true);

        repo.save(listing1Double);
        repo.save(listing2Double);
        repo.save(listing3Double);

        // Act
        List<Listing> result = repo.findAllByDirectSaleAndPublicationSampleIdIn(List.of(pub1IdDouble));

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(listing1Double));
        assertFalse(result.contains(listing2Double));
        assertFalse(result.contains(listing3Double));
    }

    @Test
    void findDirectSaleListingsContaining_returnsMultipleListings_whenMultipleListingsMatch() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        when(listing1Double.isListingLinkedToADirectSale()).thenReturn(true);
        when(listing2Double.isListingLinkedToADirectSale()).thenReturn(true);
        when(listing3Double.isListingLinkedToADirectSale()).thenReturn(true);

        when(listing1Double.containsPubSampleId(pub1IdDouble)).thenReturn(true);
        when(listing2Double.containsPubSampleId(pub2IdDouble)).thenReturn(true);
        when(listing3Double.containsPubSampleId(pub3IdDouble)).thenReturn(true);

        repo.save(listing1Double);
        repo.save(listing2Double);
        repo.save(listing3Double);

        // Act
        List<Listing> result = repo.findAllByDirectSaleAndPublicationSampleIdIn(List.of(pub1IdDouble, pub2IdDouble));

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(listing1Double));
        assertTrue(result.contains(listing2Double));
        assertFalse(result.contains(listing3Double));
    }

    @Test
    void findDirectSaleListingsContaining_excludesListing_whenNotLinkedToADirectSale() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        when(listing1Double.isListingLinkedToADirectSale()).thenReturn(false);
        when(listing2Double.isListingLinkedToADirectSale()).thenReturn(false);
        when(listing3Double.isListingLinkedToADirectSale()).thenReturn(false);

        when(listing1Double.containsPubSampleId(pub1IdDouble)).thenReturn(true);
        when(listing2Double.containsPubSampleId(pub2IdDouble)).thenReturn(true);
        when(listing3Double.containsPubSampleId(pub3IdDouble)).thenReturn(true);

        repo.save(listing1Double);
        repo.save(listing2Double);
        repo.save(listing3Double);

        // Act
        List<Listing> result = repo.findAllByAuctionAndPublicationSampleIdIn(List.of(pub1IdDouble, pub2IdDouble));

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void findDirectSaleListingsContaining_excludesListing_whenNoItemIdMatches() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        PublicationSampleId pub4Id = mock(PublicationSampleId.class);

        when(listing1Double.isListingLinkedToADirectSale()).thenReturn(true);
        when(listing2Double.isListingLinkedToADirectSale()).thenReturn(true);
        when(listing3Double.isListingLinkedToADirectSale()).thenReturn(true);

        when(listing1Double.getItemsId()).thenReturn(List.of(pub1IdDouble));
        when(listing2Double.getItemsId()).thenReturn(List.of(pub2IdDouble));
        when(listing3Double.getItemsId()).thenReturn(List.of(pub3IdDouble));

        repo.save(listing1Double);
        repo.save(listing2Double);
        repo.save(listing3Double);

        // Act
        List<Listing> result = repo.findAllByDirectSaleAndPublicationSampleIdIn(List.of(pub4Id));

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void findDirectSaleListingsContaining_returnsEmptyList_whenListingsIsEmpty() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        // Act
        List<Listing> result = repo.findAllByDirectSaleAndPublicationSampleIdIn(List.of(pub1IdDouble));

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void findDirectSaleListingsContaining_throwsException_whenListIsNull() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.findAllByDirectSaleAndPublicationSampleIdIn(null)
        );
    }

    @Test
    void findDirectSaleListingsContaining_returnsEmptyList_whenInputListIsEmpty() {
        // Arrange

        IListingRepository repo = new ListingRepositoryMem();

        // Act
        List<Listing> result = repo.findAllByDirectSaleAndPublicationSampleIdIn(List.of());

        // Assert
        assertTrue(result.isEmpty());
    }


    // US021 -----------------------------------------------------------------------------------------
    //Method getListOfItemsOnDirectSaleOfAGivenGenre--------------------------------------------------
//    @Test
//    public void getListOfItemsOnDirectSaleOfAGivenGenre_shouldThrownErrorWhenGenreIsNull() {
//      ListingFactory listingFactoryDouble = mock(ListingFactory.class);
//      Genre genreDouble = null;
//
//        // Arrange
//        ListingRepositoryMem repo = new ListingRepositoryMem(listingFactoryDouble);
//
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class,
//                () -> repo.getListOfItemsOnDirectSaleOfAGivenGenre(null));
//
//    }


//    @Test
//    public void getListOfItemsOnDirectSaleOfAGivenGenre_shouldReturnEmptyList() {
//
//        ListingFactory listingFactoryDouble = mock(ListingFactory.class);
//        Genre genreDouble = mock(Genre.class);
//
//        // Arrange
//        ListingRepositoryMem repo = new ListingRepositoryMem(listingFactoryDouble);
//
//        // Act
//        List<PublicationSample> result = repo.getListOfItemsOnDirectSaleOfAGivenGenre(genreDouble);
//
//        // Assert
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//    }

//    @Test
//    public void getListOfItemsOnDirectSaleOfAGivenGenre_shouldReturnEmptyListIfNoMatchingGenre() throws InstanceAlreadyExistsException {
//        ListingFactory listingFactoryDouble = mock(ListingFactory.class);
//        Genre genreDouble = mock(Genre.class);
//
//        // Arrange
//        IListingRepository repo = new ListingRepositoryMem(listingFactoryDouble);
//
//        Listing listingDouble = mock(Listing.class);
//        PublicationSample sampleDouble = mock(PublicationSample.class);
//
//        when(listingDouble.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
//        when(listingDouble.getItems()).thenReturn(List.of(sampleDouble));
//        when(sampleDouble.hasGenre(genreDouble)).thenReturn(false);
//        when(listingFactoryDouble.create(any(), any(), any(), any(), any(), any()))
//                .thenReturn(listingDouble);
//
//        repo.createAndAdd("Title", List.of(sampleDouble), SaleType.DIRECT_SALE,
//                mock(Price.class), "desc", List.of(mock(Picture.class)));
//
//        // Act
//        List<PublicationSample> result = repo.getListOfItemsOnDirectSaleOfAGivenGenre(genreDouble);
//
//        // Assert
//        assertTrue(result.isEmpty());
//
//    }

//    @Test
//    public void getListOfItemsOnDirectSaleOfAGivenGenre_shouldReturnListWithOneMatch() throws InstanceAlreadyExistsException {
//        ListingFactory listingFactoryDouble = mock(ListingFactory.class);
//        Genre genreDouble = mock(Genre.class);
//
//        // Arrange
//        IListingRepository repo = new ListingRepositoryMem(listingFactoryDouble);
//
//        Listing listingDouble = mock(Listing.class);
//        PublicationSample sampleDouble = mock(PublicationSample.class);
//
//        when(listingDouble.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
//        when(listingDouble.getItems()).thenReturn(List.of(sampleDouble));
//        when(sampleDouble.hasGenre(genreDouble)).thenReturn(true);
//        when(listingFactoryDouble.create(any(), any(), any(), any(), any(), any()))
//                .thenReturn(listingDouble);
//
//        repo.createAndAdd("Title", List.of(sampleDouble), SaleType.DIRECT_SALE,
//                mock(Price.class), "desc", List.of(mock(Picture.class)));
//
//        // Act
//        List<PublicationSample> result = repo.getListOfItemsOnDirectSaleOfAGivenGenre(genreDouble);
//
//        // Assert
//        assertEquals(1, result.size());
//        assertTrue(result.contains(sampleDouble));
//    }

//    @Test
//    public void getListOfItemsOnDirectSaleOfAGivenGenre_shouldReturnListWithMultipleMatches() throws InstanceAlreadyExistsException {
//        ListingFactory listingFactoryDouble = mock(ListingFactory.class);
//        Genre genreDouble = mock(Genre.class);
//
//
//        // Arrange
//        IListingRepository repo = new ListingRepositoryMem(listingFactoryDouble);
//
//        Listing listing1Double = mock(Listing.class);
//        Listing listing2Double = mock(Listing.class);
//        PublicationSample sample1Double = mock(PublicationSample.class);
//        PublicationSample sample2Double = mock(PublicationSample.class);
//
//        when(listing1Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
//        when(listing1Double.getItems()).thenReturn(List.of(sample1Double));
//        when(sample1Double.hasGenre(genreDouble)).thenReturn(true);
//
//        when(listing2Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
//        when(listing2Double.getItems()).thenReturn(List.of(sample2Double));
//        when(sample2Double.hasGenre(genreDouble)).thenReturn(true);
//
//        when(listingFactoryDouble.create(any(), any(), any(), any(), any(), any()))
//                .thenReturn(listing1Double)
//                .thenReturn(listing2Double);
//
//        repo.createAndAdd("Title 1", List.of(sample1Double), SaleType.DIRECT_SALE,
//                mock(Price.class), "desc", List.of(mock(Picture.class)));
//        repo.createAndAdd("Title 2", List.of(sample2Double), SaleType.DIRECT_SALE,
//                mock(Price.class), "desc", List.of(mock(Picture.class)));
//
//        // Act
//        List<PublicationSample> result = repo.getListOfItemsOnDirectSaleOfAGivenGenre(genreDouble);
//
//        // Assert
//        assertEquals(2, result.size());
//    }

//    @Test
//    void getListOfItemsOnDirectSaleOfAGivenGenre_shouldIgnoreAuctionListings() throws Exception {
//        ListingFactory listingFactoryDouble = mock(ListingFactory.class);
//        Genre genreDouble = mock(Genre.class);
//
//        // Arrange
//        IListingRepository repo = new ListingRepositoryMem(listingFactoryDouble);
//
//        Listing listingDouble = mock(Listing.class);
//        PublicationSample sampleDouble = mock(PublicationSample.class);
//
//        when(listingDouble.getSaleType()).thenReturn(SaleType.AUCTION); // não é direct sale
//        when(listingDouble.getItems()).thenReturn(List.of(sampleDouble));
//        when(sampleDouble.hasGenre(genreDouble)).thenReturn(true);
//        when(listingFactoryDouble.create(any(), any(), any(), any(), any(), any()))
//                .thenReturn(listingDouble);
//
//        repo.createAndAdd("Title", List.of(sampleDouble), SaleType.AUCTION,
//                mock(Price.class), "desc", List.of(mock(Picture.class)));
//
//        // Act
//        List<PublicationSample> result = repo.getListOfItemsOnDirectSaleOfAGivenGenre(genreDouble);
//
//        // Assert
//        assertTrue(result.isEmpty());
//    }

// US021 - END ------------------------------------------------------------------------------------------------

//    @Test
//    void getListOfItemsOnDirectSaleOfAGivenAuthor_ShouldFilterCorrectly() throws Exception {
//
//        ListingFactory listingFactoryDouble = mock(ListingFactory.class);
//        IListingRepository repo = new ListingRepositoryMem(listingFactoryDouble);
//
//        Author targetAuthor = mock(Author.class);
//        PublicationSample sampleWithAuthor = mock(PublicationSample.class);
//        PublicationSample sampleWithoutAuthor = mock(PublicationSample.class);
//
//        BibliographicalData bdWithAuthor = mock(BibliographicalData.class);
//        when(bdWithAuthor.getAuthors()).thenReturn(List.of(targetAuthor));
//        when(sampleWithAuthor.getBibliographicalData()).thenReturn(bdWithAuthor);
//
//        BibliographicalData bdWithoutAuthor = mock(BibliographicalData.class);
//        when(bdWithoutAuthor.getAuthors()).thenReturn(List.of(mock(Author.class)));
//        when(sampleWithoutAuthor.getBibliographicalData()).thenReturn(bdWithoutAuthor);
//
//        Listing listingDirectSale = mock(Listing.class);
//        Listing listingAuction = mock(Listing.class);
//
//        when(listingDirectSale.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
//        when(listingDirectSale.getItems()).thenReturn(List.of(sampleWithAuthor, sampleWithoutAuthor));
//
//        when(listingAuction.getSaleType()).thenReturn(SaleType.AUCTION);
//        when(listingAuction.getItems()).thenReturn(List.of(sampleWithAuthor));
//
//        when(listingFactoryDouble.create("listing1", List.of(sampleWithAuthor), SaleType.DIRECT_SALE, price1,
//                "desc", List.of(pic1))).thenReturn(listingDirectSale);
//
//        when(listingFactoryDouble.create("listing2", List.of(sampleWithAuthor), SaleType.AUCTION, price2,
//                "desc", List.of(pic2))).thenReturn(listingAuction);
//
//        repo.createAndAdd("listing1", List.of(sampleWithAuthor), SaleType.DIRECT_SALE, price1, "desc", List.of(pic1));
//        repo.createAndAdd("listing2", List.of(sampleWithAuthor), SaleType.AUCTION, price2, "desc", List.of(pic2));
//
//        List<PublicationSample> result = repo.getListOfItemsOnDirectSaleOfAGivenAuthor(targetAuthor);
//
//        assertEquals(1, result.size());
//        assertTrue(result.contains(sampleWithAuthor));
//        assertFalse(result.contains(sampleWithoutAuthor));
//    }

//    @Test
//    void getListOfItemsOnDirectSaleofAGivenAuthor_ShouldThrowOnNullAuthor() {
//        ListingFactory listingFactoryDouble = mock(ListingFactory.class);
//        IListingRepository repo = new ListingRepositoryMem(listingFactoryDouble);
//
//        assertThrows(IllegalArgumentException.class, () ->
//                repo.getListOfItemsOnDirectSaleOfAGivenAuthor(null)
//        );
//    }

//    @Test
//    void getListOfItemsOnDirectSaleofAGivenAuthor_ShouldIgnoreNonDirectSaleListings() throws Exception {
//
//        // Arrange
//        ListingFactory listingFactoryDouble = mock(ListingFactory.class);
//        IListingRepository repo = new ListingRepositoryMem(listingFactoryDouble);
//
//        Author targetAuthor = mock(Author.class);
//        PublicationSample sampleWithAuthor = mock(PublicationSample.class);
//
//        BibliographicalData bdWithAuthor = mock(BibliographicalData.class);
//        when(bdWithAuthor.getAuthors()).thenReturn(List.of(targetAuthor));
//        when(sampleWithAuthor.getBibliographicalData()).thenReturn(bdWithAuthor);
//
//        Listing listingAuction = mock(Listing.class);
//        when(listingAuction.getSaleType()).thenReturn(SaleType.AUCTION);
//        when(listingAuction.getItems()).thenReturn(List.of(sampleWithAuthor));
//
//        // Add non-DIRECT_SALE listing directly to repository
//        repo.getAllListings().add(listingAuction);
//
//        // Act
//        List<PublicationSample> result = repo.getListOfItemsOnDirectSaleOfAGivenAuthor(targetAuthor);
//
//        // Assert
//        assertTrue(result.isEmpty(), "Items in non-DIRECT_SALE listings should be ignored");
//    }

}
