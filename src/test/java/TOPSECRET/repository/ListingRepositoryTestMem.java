package TOPSECRET.repository;

import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.Listing.ListingFactory;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
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
class ListingRepositoryTestMem {

    // ======================================================
    // ================== Set Up ============================
    // ======================================================

    Listing listing1Double;
    Listing listing2Double;
    Listing listing3Double;

    PublicationSampleId pub1Id;
    PublicationSampleId pub2Id;
    PublicationSampleId pub3Id;

    Price price1;
    Price price2;
    Price price3;

    Picture pic1;
    Picture pic2;
    Picture pic3;

    UserId sellerId;

    @BeforeEach
    void setUp() {

        listing1Double = mock(Listing.class);
        listing2Double = mock(Listing.class);
        listing3Double = mock(Listing.class);
        
        pub1Id = new PublicationSampleId("pub1Id");
        pub2Id = new PublicationSampleId("pub2Id");
        pub3Id = new PublicationSampleId("pub3Id");

        price1 = mock(Price.class);
        price2 = mock(Price.class);
        price3 = mock(Price.class);

        pic1 = mock(Picture.class);
        pic2 = mock(Picture.class);
        pic3 = mock(Picture.class);

        sellerId = new UserId("sellerId");
        
        when(listing1Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(listing2Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(listing3Double.getSaleType()).thenReturn(SaleType.AUCTION);
    }

    // ====================================================
    // ====== Create and Add ==============================
    // ====================================================

    @Test
    void shouldThrowExceptionWhenListingAlreadyExists() throws Exception {
        // arrange
        ListingFactory listingFactoryDouble = mock(ListingFactory.class);
        ListingRepositoryMem repo = new ListingRepositoryMem(listingFactoryDouble);
        
        String listingTitle = "title1";
        List<PublicationSampleId> itemsId = List.of(pub1Id);
        SaleType saleType = SaleType.DIRECT_SALE;
        String shortDescription = "Description";
        List<Picture> pictures = List.of(pic1, pic2);

        Listing existingListing = mock(Listing.class);

        repo.addListing(existingListing);

        when(listingFactoryDouble.create(listingTitle, itemsId, saleType, price1, shortDescription, sellerId, pictures))
                .thenReturn(existingListing);

        // act + assert
        assertThrows(InstanceAlreadyExistsException.class, () ->
                repo.createAndAdd(listingTitle, itemsId, saleType, price1, shortDescription, sellerId, pictures)
        );
    }

    @Test
    void createAndAdd_shouldAddAndReturnNewListing() throws InstanceAlreadyExistsException {

        // arrange
        ListingFactory listingFactoryDouble = mock(ListingFactory.class);
        ListingRepositoryMem repo = new ListingRepositoryMem(listingFactoryDouble);

        String listingTitle = "Title";
        List<PublicationSampleId> itemsId = List.of(pub1Id);
        SaleType saleType = SaleType.DIRECT_SALE;
        String shortDescription = "Description";
        List<Picture> pictures = List.of(pic1, pic2);

        Listing newListing = mock(Listing.class);

        when(listingFactoryDouble.create(
                listingTitle,
                itemsId,
                saleType,
                price1,
                shortDescription,
                sellerId,
                pictures
        )).thenReturn(newListing);

        // act
        Listing result = repo.createAndAdd(listingTitle, itemsId, saleType, price1, shortDescription, sellerId, pictures);

        // assert
        assertEquals(newListing, result);
        assertTrue(repo.getAllListings().contains(result));
    }

    @Test
    void constructor_ShouldThrowWhenFactoryIsNull() {

        // assert
        assertThrows(IllegalArgumentException.class, () -> new ListingRepositoryMem(null));
    }

    @Test
    void createAndAdd_ShouldAllowDifferentListingsWithDifferentIds() throws Exception {

        // arrange
        ListingFactory factory = mock(ListingFactory.class);
        ListingRepositoryMem repo = new ListingRepositoryMem(factory);

        Listing listingA = mock(Listing.class);
        Listing listingB = mock(Listing.class);

        when(factory.create("title", List.of(pub1Id), SaleType.DIRECT_SALE, price1, "desc", sellerId, List.of(pic1)))
                .thenReturn(listingA)
                .thenReturn(listingB);

        // act
        repo.createAndAdd("title", List.of(pub1Id), SaleType.DIRECT_SALE, price1, "desc", sellerId, List.of(pic1));
        repo.createAndAdd("title", List.of(pub1Id), SaleType.DIRECT_SALE, price1, "desc", sellerId, List.of(pic1));

        // assert
        assertEquals(2, repo.getAllListings().size());
    }

    // ======================================================
    // ==== Tests to findAllDirectSaleListings===============
    // ======================================================


    @Test
    void findAllDirectSaleListings_ShouldReturnOnlyDirectSaleListings() throws Exception {

        // Arrange
        ListingFactory listingFactoryDouble = mock(ListingFactory.class);
        ListingRepositoryMem repo = new ListingRepositoryMem(listingFactoryDouble);

        when(listingFactoryDouble.create("title1", List.of(pub1Id), SaleType.DIRECT_SALE, price1, "description1", sellerId, List.of(pic1)
        )).thenReturn(listing1Double);

        when(listingFactoryDouble.create("title2", List.of(pub2Id), SaleType.DIRECT_SALE, price2, "description2", sellerId, List.of(pic2)
        )).thenReturn(listing2Double);

        when(listingFactoryDouble.create("title3", List.of(pub3Id), SaleType.AUCTION, price3, "description3", sellerId, List.of(pic3)
        )).thenReturn(listing3Double);

        repo.createAndAdd("title1", List.of(pub1Id), SaleType.DIRECT_SALE, price1, "description1", sellerId, List.of(pic1));
        repo.createAndAdd("title2", List.of(pub2Id), SaleType.DIRECT_SALE, price2, "description2", sellerId, List.of(pic2));
        repo.createAndAdd("title3", List.of(pub3Id), SaleType.AUCTION,      price3, "description3", sellerId, List.of(pic3));

        // Act
        List<Listing> result = repo.findAllDirectSaleListings();

        // Assert
        assertEquals(2, result.size());
        assertEquals(listing1Double, result.get(0));
        assertEquals(listing2Double, result.get(1));
    }

    @Test
    void findAllDirectSaleListings_ShouldReturnEmptyListWhenNoDirectSaleListingsExist() throws Exception {

        // Arrange
        ListingFactory listingFactoryDouble = mock(ListingFactory.class);
        ListingRepositoryMem repo = new ListingRepositoryMem(listingFactoryDouble);

        when(listingFactoryDouble.create("title3", List.of(pub3Id), SaleType.AUCTION, price3, "description3", sellerId,
                List.of(pic3))).thenReturn(listing3Double);

        when(listing3Double.getSaleType()).thenReturn(SaleType.AUCTION);

        repo.createAndAdd("title3", List.of(pub3Id), SaleType.AUCTION, price3, "description3", sellerId, List.of(pic3));

        // Act
        List<Listing> result = repo.findAllDirectSaleListings();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllDirectSaleListings_ShouldReturnEmptyListWhenRepositoryIsEmpty() {
        // arrange
        ListingFactory listingFactoryDouble = mock(ListingFactory.class);
        ListingRepositoryMem repo = new ListingRepositoryMem(listingFactoryDouble);

        // Act
        List<Listing> result = repo.findAllDirectSaleListings();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllDirectSaleListings_ShouldReturnNewIndependentList() throws Exception {

        // Arrange
        ListingFactory listingFactoryDouble = mock(ListingFactory.class);
        ListingRepositoryMem repo = new ListingRepositoryMem(listingFactoryDouble);

        when(listingFactoryDouble.create("title1", List.of(pub1Id), SaleType.DIRECT_SALE, price1, "description1", sellerId,
                List.of(pic1))).thenReturn(listing1Double);

        when(listing1Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);

        repo.createAndAdd("title1", List.of(pub1Id), SaleType.DIRECT_SALE, price1, "description1", sellerId, List.of(pic1));

        // Act
        List<Listing> firstCall = repo.findAllDirectSaleListings();
        firstCall.clear(); // tries to modify the list

        List<Listing> secondCall = repo.findAllDirectSaleListings();

        assertEquals(1, secondCall.size());
    }

    // ======================================================
    // ==== Tests to findAllAuctionListings==================
    // ======================================================


    @Test
    void findAllAuctionListings_ShouldReturnOnlyAuctionListings() throws Exception {

        // Arrange
        ListingFactory listingFactoryDouble = mock(ListingFactory.class);
        ListingRepositoryMem repo = new ListingRepositoryMem(listingFactoryDouble);

        when(listingFactoryDouble.create("title1", List.of(pub1Id), SaleType.DIRECT_SALE, price1, "description1", sellerId, List.of(pic1)
        )).thenReturn(listing1Double);

        when(listingFactoryDouble.create("title2", List.of(pub2Id), SaleType.DIRECT_SALE, price2, "description2", sellerId, List.of(pic2)
        )).thenReturn(listing2Double);

        when(listingFactoryDouble.create("title3", List.of(pub3Id), SaleType.AUCTION, price3, "description3", sellerId, List.of(pic3)
        )).thenReturn(listing3Double);

        repo.createAndAdd("title1", List.of(pub1Id), SaleType.DIRECT_SALE, price1, "description1", sellerId, List.of(pic1));
        repo.createAndAdd("title2", List.of(pub2Id), SaleType.DIRECT_SALE, price2, "description2", sellerId, List.of(pic2));
        repo.createAndAdd("title3", List.of(pub3Id), SaleType.AUCTION, price3, "description3", sellerId, List.of(pic3));

        // Act
        List<Listing> result = repo.findAllAuctionListings();

        // Assert
        assertEquals(1, result.size());
        assertEquals(listing3Double, result.get(0));
    }

    @Test
    void findAllAuctionListings_ShouldReturnEmptyListWhenNoAuctionListingsExist() throws Exception {

        // Arrange
        ListingFactory listingFactoryDouble = mock(ListingFactory.class);
        ListingRepositoryMem repo = new ListingRepositoryMem(listingFactoryDouble);

        when(listingFactoryDouble.create("title1", List.of(pub1Id), SaleType.DIRECT_SALE, price1, "description1", sellerId,
                List.of(pic1))).thenReturn(listing1Double);

        when(listing1Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);

        repo.createAndAdd("title1", List.of(pub1Id), SaleType.DIRECT_SALE, price1, "description1", sellerId, List.of(pic1));

        // Act
        List<Listing> result = repo.findAllAuctionListings();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllAuctionListings_ShouldReturnEmptyListWhenRepositoryIsEmpty() {

        // arrange
        ListingFactory listingFactoryDouble = mock(ListingFactory.class);
        ListingRepositoryMem repo = new ListingRepositoryMem(listingFactoryDouble);

        // Act
        List<Listing> result = repo.findAllAuctionListings();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllAuctionListings_ShouldReturnNewIndependentList() throws Exception {

        // Arrange
        ListingFactory listingFactoryDouble = mock(ListingFactory.class);
        ListingRepositoryMem repo = new ListingRepositoryMem(listingFactoryDouble);

        when(listingFactoryDouble.create("title3", List.of(pub3Id), SaleType.AUCTION, price3, "description3", sellerId,
                List.of(pic3))).thenReturn(listing3Double);

        when(listing3Double.getSaleType()).thenReturn(SaleType.AUCTION);

        repo.createAndAdd("title3", List.of(pub3Id), SaleType.AUCTION, price3, "description3", sellerId, List.of(pic3));

        // Act
        List<Listing> firstCall = repo.findAllAuctionListings();
        firstCall.clear(); // tries to modify the list

        List<Listing> secondCall = repo.findAllAuctionListings();

        assertEquals(1, secondCall.size());
    }

    // ======================================================
    // ========== Tests to getAllListings ===================
    // ======================================================

    @Test
    void getAllListings_ShouldReturnAllListingsInRepository() throws Exception {

        // arrange
        ListingFactory listingFactoryDouble = mock(ListingFactory.class);
        ListingRepositoryMem repo = new ListingRepositoryMem(listingFactoryDouble);

        when(listingFactoryDouble.create("title1", List.of(pub1Id), SaleType.DIRECT_SALE, price1,
                "description1", sellerId, List.of(pic1))).thenReturn(listing1Double);
        when(listingFactoryDouble.create("title2", List.of(pub2Id), SaleType.AUCTION, price2,
                "description2", sellerId, List.of(pic2))).thenReturn(listing2Double);

        repo.createAndAdd("title1", List.of(pub1Id), SaleType.DIRECT_SALE, price1, "description1", sellerId, List.of(pic1));
        repo.createAndAdd("title2", List.of(pub2Id), SaleType.AUCTION, price2, "description2", sellerId, List.of(pic2));

        // act
        List<Listing> allListings = repo.getAllListings();

        // assert
        assertEquals(2, allListings.size(), "getAllListings should return all listings added to the repository");
        assertTrue(allListings.contains(listing1Double), "The list should contain listing1Double");
        assertTrue(allListings.contains(listing2Double), "The list should contain listing2Double");
    }

    @Test
    void getAllListings_ShouldReturnUnmodifiableList() throws Exception {

        // arrange
        ListingFactory factory = mock(ListingFactory.class);
        ListingRepositoryMem repo = new ListingRepositoryMem(factory);

        when(factory.create("title", List.of(pub1Id), SaleType.DIRECT_SALE, price1, "description", sellerId, List.of(pic1)))
                .thenReturn(listing1Double);

        // act
        repo.createAndAdd("title", List.of(pub1Id), SaleType.DIRECT_SALE, price1, "description", sellerId, List.of(pic1));

        List<Listing> result = repo.getAllListings();

        // assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(listing2Double));
    }

    // ====================================================
    // ====== FIND LISTING BY ID  =========================
    // ====================================================

    @Test
    void findListingById_shouldThrowExceptionWhenIdIsNull() {

        // arrange
        ListingFactory factory = mock(ListingFactory.class);
        ListingRepositoryMem repo = new ListingRepositoryMem(factory);

        // act + Assert
        assertThrows(IllegalArgumentException.class, () -> {
            repo.findListingById(null);
        });
    }

    @Test
    void findListingById_shouldReturnListingWhenIdExists() {

        // arrange
        ListingId id1 = new ListingId("id1");

        when(listing1Double.id()).thenReturn(id1);

        ListingFactory factory = mock(ListingFactory.class);
        ListingRepositoryMem repo = new ListingRepositoryMem(factory);
        repo.addListing(listing1Double);

        // act
        Optional<Listing> result = repo.findListingById(id1);

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

        ListingFactory factory = mock(ListingFactory.class);
        ListingRepositoryMem repo = new ListingRepositoryMem(factory);
        repo.addListing(listing1Double);

        // act
        Optional<Listing> result = repo.findListingById(newId);

        // assert
        assertTrue(result.isEmpty());
    }

    // ======================================================
    // ==== Tests to getAuctionListingsContaining ===========
    // ======================================================

    @Test
    void getAuctionListingsContaining_returnsListings_whenMatchingIdExists() throws InstanceAlreadyExistsException {

        // Arrange
        ListingFactory factory = mock(ListingFactory.class);

        when(factory.create("title", List.of(pub1Id), SaleType.AUCTION, price1, "description", sellerId, List.of(pic1)))
                .thenReturn(listing1Double);
        when(factory.create("title", List.of(pub2Id), SaleType.AUCTION, price1, "description", sellerId, List.of(pic1)))
                .thenReturn(listing2Double);
        when(factory.create("title", List.of(pub3Id), SaleType.AUCTION, price1, "description", sellerId, List.of(pic1)))
                .thenReturn(listing3Double);

        when(listing1Double.isListingLinkedToAnAuction()).thenReturn(true);
        when(listing2Double.isListingLinkedToAnAuction()).thenReturn(true);
        when(listing3Double.isListingLinkedToAnAuction()).thenReturn(true);

        when(listing1Double.containsPubSampleId(pub1Id)).thenReturn(true);
        when(listing2Double.containsPubSampleId(pub2Id)).thenReturn(true);
        when(listing3Double.containsPubSampleId(pub3Id)).thenReturn(true);

        ListingRepositoryMem repo = new ListingRepositoryMem(factory);

        repo.createAndAdd("title", List.of(pub1Id), SaleType.AUCTION, price1, "description", sellerId, List.of(pic1));
        repo.createAndAdd("title", List.of(pub2Id), SaleType.AUCTION, price1, "description", sellerId, List.of(pic1));
        repo.createAndAdd("title", List.of(pub3Id), SaleType.AUCTION, price1, "description", sellerId, List.of(pic1));

        // Act
        List<Listing> result = repo.getAuctionListingsContaining(List.of(pub1Id));

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(listing1Double));
        assertFalse(result.contains(listing2Double));
        assertFalse(result.contains(listing3Double));
    }

    @Test
    void getAuctionListingsContaining_returnsMultipleListings_whenMultipleListingsMatch() throws InstanceAlreadyExistsException {

        // Arrange
        ListingFactory factory = mock(ListingFactory.class);

        when(factory.create("title", List.of(pub1Id), SaleType.AUCTION, price1, "description", sellerId, List.of(pic1)))
                .thenReturn(listing1Double);
        when(factory.create("title", List.of(pub2Id), SaleType.AUCTION, price1, "description", sellerId, List.of(pic1)))
                .thenReturn(listing2Double);
        when(factory.create("title", List.of(pub3Id), SaleType.AUCTION, price1, "description", sellerId, List.of(pic1)))
                .thenReturn(listing3Double);

        when(listing1Double.isListingLinkedToAnAuction()).thenReturn(true);
        when(listing2Double.isListingLinkedToAnAuction()).thenReturn(true);
        when(listing3Double.isListingLinkedToAnAuction()).thenReturn(true);

        when(listing1Double.containsPubSampleId(pub1Id)).thenReturn(true);
        when(listing2Double.containsPubSampleId(pub2Id)).thenReturn(true);
        when(listing3Double.containsPubSampleId(pub3Id)).thenReturn(true);

        ListingRepositoryMem repo = new ListingRepositoryMem(factory);

        repo.createAndAdd("title", List.of(pub1Id), SaleType.AUCTION, price1, "description", sellerId, List.of(pic1));
        repo.createAndAdd("title", List.of(pub2Id), SaleType.AUCTION, price1, "description", sellerId, List.of(pic1));
        repo.createAndAdd("title", List.of(pub3Id), SaleType.AUCTION, price1, "description", sellerId, List.of(pic1));

        // Act
        List<Listing> result = repo.getAuctionListingsContaining(List.of(pub1Id, pub2Id));

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(listing1Double));
        assertTrue(result.contains(listing2Double));
        assertFalse(result.contains(listing3Double));
    }

    @Test
    void getAuctionListingsContaining_excludesListing_whenNotLinkedToAuction() throws InstanceAlreadyExistsException {

        // Arrange
        ListingFactory factory = mock(ListingFactory.class);

        when(factory.create("title", List.of(pub1Id), SaleType.DIRECT_SALE, price1, "description", sellerId, List.of(pic1)))
                .thenReturn(listing1Double);
        when(factory.create("title", List.of(pub2Id), SaleType.DIRECT_SALE, price1, "description", sellerId, List.of(pic1)))
                .thenReturn(listing2Double);
        when(factory.create("title", List.of(pub3Id), SaleType.DIRECT_SALE, price1, "description", sellerId, List.of(pic1)))
                .thenReturn(listing3Double);

        when(listing1Double.isListingLinkedToAnAuction()).thenReturn(false);
        when(listing2Double.isListingLinkedToAnAuction()).thenReturn(false);
        when(listing3Double.isListingLinkedToAnAuction()).thenReturn(false);

        when(listing1Double.containsPubSampleId(pub1Id)).thenReturn(true);
        when(listing2Double.containsPubSampleId(pub2Id)).thenReturn(true);
        when(listing3Double.containsPubSampleId(pub3Id)).thenReturn(true);

        ListingRepositoryMem repo = new ListingRepositoryMem(factory);

        repo.createAndAdd("title", List.of(pub1Id), SaleType.DIRECT_SALE, price1, "description", sellerId, List.of(pic1));
        repo.createAndAdd("title", List.of(pub2Id), SaleType.DIRECT_SALE, price1, "description", sellerId, List.of(pic1));
        repo.createAndAdd("title", List.of(pub3Id), SaleType.DIRECT_SALE, price1, "description", sellerId, List.of(pic1));

        // Act
        List<Listing> result = repo.getAuctionListingsContaining(List.of(pub1Id, pub2Id));

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void getAuctionListingsContaining_excludesListing_whenNoItemIdMatches() throws InstanceAlreadyExistsException {

        // Arrange
        ListingFactory factory = mock(ListingFactory.class);

        PublicationSampleId pub4Id = mock(PublicationSampleId.class);

        when(factory.create("title", List.of(pub1Id), SaleType.AUCTION, price1, "description", sellerId, List.of(pic1)))
                .thenReturn(listing1Double);
        when(factory.create("title", List.of(pub2Id), SaleType.AUCTION, price1, "description", sellerId, List.of(pic1)))
                .thenReturn(listing2Double);
        when(factory.create("title", List.of(pub3Id), SaleType.AUCTION, price1, "description", sellerId, List.of(pic1)))
                .thenReturn(listing3Double);

        when(listing1Double.isListingLinkedToAnAuction()).thenReturn(true);
        when(listing2Double.isListingLinkedToAnAuction()).thenReturn(true);
        when(listing3Double.isListingLinkedToAnAuction()).thenReturn(true);

        when(listing1Double.getItemsId()).thenReturn(List.of(pub1Id));
        when(listing2Double.getItemsId()).thenReturn(List.of(pub2Id));
        when(listing3Double.getItemsId()).thenReturn(List.of(pub3Id));

        ListingRepositoryMem repo = new ListingRepositoryMem(factory);

        repo.createAndAdd("title", List.of(pub1Id), SaleType.AUCTION, price1, "description", sellerId, List.of(pic1));
        repo.createAndAdd("title", List.of(pub2Id), SaleType.AUCTION, price1, "description", sellerId, List.of(pic1));
        repo.createAndAdd("title", List.of(pub3Id), SaleType.AUCTION, price1, "description", sellerId, List.of(pic1));

        // Act
        List<Listing> result = repo.getAuctionListingsContaining(List.of(pub4Id));

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void getAuctionListingsContaining_returnsEmptyList_whenListingsIsEmpty() {

        // Arrange
        ListingFactory factory = mock(ListingFactory.class);
        ListingRepositoryMem repo = new ListingRepositoryMem(factory);

        // Act
        List<Listing> result = repo.getAuctionListingsContaining(List.of(pub1Id));

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void getAuctionListingsContaining_throwsException_whenListIsNull() throws InstanceAlreadyExistsException {

        // Arrange
        ListingFactory factory = mock(ListingFactory.class);
        ListingRepositoryMem repo = new ListingRepositoryMem(factory);

        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                repo.getAuctionListingsContaining(null)
        );
    }

    @Test
    void getAuctionListingsContaining_throwsException_whenListIsEmpty() throws InstanceAlreadyExistsException {

        // Arrange
        ListingFactory factory = mock(ListingFactory.class);
        ListingRepositoryMem repo = new ListingRepositoryMem(factory);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.getAuctionListingsContaining(List.of())
        );
    }

    // ======================================================
    // ==== Tests to getDirectSaleListingsContaining ===========
    // ======================================================

    @Test
    void findDirectSaleListingsContaining_returnsListings_whenMatchingIdExists() throws InstanceAlreadyExistsException {

        // Arrange
        ListingFactory factory = mock(ListingFactory.class);

        when(factory.create("title", List.of(pub1Id), SaleType.DIRECT_SALE, price1, "description", sellerId, List.of(pic1)))
                .thenReturn(listing1Double);
        when(factory.create("title", List.of(pub2Id), SaleType.DIRECT_SALE, price1, "description", sellerId, List.of(pic1)))
                .thenReturn(listing2Double);
        when(factory.create("title", List.of(pub3Id), SaleType.DIRECT_SALE, price1, "description", sellerId, List.of(pic1)))
                .thenReturn(listing3Double);

        when(listing1Double.isListingLinkedToADirectSale()).thenReturn(true);
        when(listing2Double.isListingLinkedToADirectSale()).thenReturn(true);
        when(listing3Double.isListingLinkedToADirectSale()).thenReturn(true);

        when(listing1Double.containsPubSampleId(pub1Id)).thenReturn(true);
        when(listing2Double.containsPubSampleId(pub2Id)).thenReturn(true);
        when(listing3Double.containsPubSampleId(pub3Id)).thenReturn(true);

        ListingRepositoryMem repo = new ListingRepositoryMem(factory);

        repo.createAndAdd("title", List.of(pub1Id), SaleType.DIRECT_SALE, price1, "description", sellerId, List.of(pic1));
        repo.createAndAdd("title", List.of(pub2Id), SaleType.DIRECT_SALE, price1, "description", sellerId, List.of(pic1));
        repo.createAndAdd("title", List.of(pub3Id), SaleType.DIRECT_SALE, price1, "description", sellerId, List.of(pic1));

        // Act
        List<Listing> result = repo.findDirectSaleListingsContaining(List.of(pub1Id));

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(listing1Double));
        assertFalse(result.contains(listing2Double));
        assertFalse(result.contains(listing3Double));
    }

    @Test
    void findDirectSaleListingsContaining_returnsMultipleListings_whenMultipleListingsMatch() throws InstanceAlreadyExistsException {

        // Arrange
        ListingFactory factory = mock(ListingFactory.class);

        when(factory.create("title", List.of(pub1Id), SaleType.DIRECT_SALE, price1, "description", sellerId, List.of(pic1)))
                .thenReturn(listing1Double);
        when(factory.create("title", List.of(pub2Id), SaleType.DIRECT_SALE, price1, "description", sellerId, List.of(pic1)))
                .thenReturn(listing2Double);
        when(factory.create("title", List.of(pub3Id), SaleType.DIRECT_SALE, price1, "description", sellerId, List.of(pic1)))
                .thenReturn(listing3Double);

        when(listing1Double.isListingLinkedToADirectSale()).thenReturn(true);
        when(listing2Double.isListingLinkedToADirectSale()).thenReturn(true);
        when(listing3Double.isListingLinkedToADirectSale()).thenReturn(true);

        when(listing1Double.containsPubSampleId(pub1Id)).thenReturn(true);
        when(listing2Double.containsPubSampleId(pub2Id)).thenReturn(true);
        when(listing3Double.containsPubSampleId(pub3Id)).thenReturn(true);

        ListingRepositoryMem repo = new ListingRepositoryMem(factory);

        repo.createAndAdd("title", List.of(pub1Id), SaleType.DIRECT_SALE, price1, "description", sellerId, List.of(pic1));
        repo.createAndAdd("title", List.of(pub2Id), SaleType.DIRECT_SALE, price1, "description", sellerId, List.of(pic1));
        repo.createAndAdd("title", List.of(pub3Id), SaleType.DIRECT_SALE, price1, "description", sellerId, List.of(pic1));

        // Act
        List<Listing> result = repo.findDirectSaleListingsContaining(List.of(pub1Id, pub2Id));

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(listing1Double));
        assertTrue(result.contains(listing2Double));
        assertFalse(result.contains(listing3Double));
    }

    @Test
    void findDirectSaleListingsContaining_excludesListing_whenNotLinkedToADirectSale() throws InstanceAlreadyExistsException {

        // Arrange
        ListingFactory factory = mock(ListingFactory.class);

        when(factory.create("title", List.of(pub1Id), SaleType.AUCTION, price1, "description", sellerId, List.of(pic1)))
                .thenReturn(listing1Double);
        when(factory.create("title", List.of(pub2Id), SaleType.AUCTION, price1, "description", sellerId, List.of(pic1)))
                .thenReturn(listing2Double);
        when(factory.create("title", List.of(pub3Id), SaleType.AUCTION, price1, "description", sellerId, List.of(pic1)))
                .thenReturn(listing3Double);

        when(listing1Double.isListingLinkedToADirectSale()).thenReturn(false);
        when(listing2Double.isListingLinkedToADirectSale()).thenReturn(false);
        when(listing3Double.isListingLinkedToADirectSale()).thenReturn(false);

        when(listing1Double.containsPubSampleId(pub1Id)).thenReturn(true);
        when(listing2Double.containsPubSampleId(pub2Id)).thenReturn(true);
        when(listing3Double.containsPubSampleId(pub3Id)).thenReturn(true);

        ListingRepositoryMem repo = new ListingRepositoryMem(factory);

        repo.createAndAdd("title", List.of(pub1Id), SaleType.AUCTION, price1, "description", sellerId, List.of(pic1));
        repo.createAndAdd("title", List.of(pub2Id), SaleType.AUCTION, price1, "description", sellerId, List.of(pic1));
        repo.createAndAdd("title", List.of(pub3Id), SaleType.AUCTION, price1, "description", sellerId, List.of(pic1));

        // Act
        List<Listing> result = repo.getAuctionListingsContaining(List.of(pub1Id, pub2Id));

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void findDirectSaleListingsContaining_excludesListing_whenNoItemIdMatches() throws InstanceAlreadyExistsException {

        // Arrange
        ListingFactory factory = mock(ListingFactory.class);

        PublicationSampleId pub4Id = mock(PublicationSampleId.class);

        when(factory.create("title", List.of(pub1Id), SaleType.DIRECT_SALE, price1, "description", sellerId, List.of(pic1)))
                .thenReturn(listing1Double);
        when(factory.create("title", List.of(pub2Id), SaleType.DIRECT_SALE, price1, "description", sellerId, List.of(pic1)))
                .thenReturn(listing2Double);
        when(factory.create("title", List.of(pub3Id), SaleType.DIRECT_SALE, price1, "description", sellerId, List.of(pic1)))
                .thenReturn(listing3Double);

        when(listing1Double.isListingLinkedToADirectSale()).thenReturn(true);
        when(listing2Double.isListingLinkedToADirectSale()).thenReturn(true);
        when(listing3Double.isListingLinkedToADirectSale()).thenReturn(true);

        when(listing1Double.getItemsId()).thenReturn(List.of(pub1Id));
        when(listing2Double.getItemsId()).thenReturn(List.of(pub2Id));
        when(listing3Double.getItemsId()).thenReturn(List.of(pub3Id));

        ListingRepositoryMem repo = new ListingRepositoryMem(factory);

        repo.createAndAdd("title", List.of(pub1Id), SaleType.DIRECT_SALE, price1, "description", sellerId, List.of(pic1));
        repo.createAndAdd("title", List.of(pub2Id), SaleType.DIRECT_SALE, price1, "description", sellerId, List.of(pic1));
        repo.createAndAdd("title", List.of(pub3Id), SaleType.DIRECT_SALE, price1, "description", sellerId, List.of(pic1));

        // Act
        List<Listing> result = repo.findDirectSaleListingsContaining(List.of(pub4Id));

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void findDirectSaleListingsContaining_returnsEmptyList_whenListingsIsEmpty() {

        // Arrange
        ListingFactory factory = mock(ListingFactory.class);
        ListingRepositoryMem repo = new ListingRepositoryMem(factory);

        // Act
        List<Listing> result = repo.findDirectSaleListingsContaining(List.of(pub1Id));

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void findDirectSaleListingsContaining_throwsException_whenListIsNull() throws InstanceAlreadyExistsException {

        // Arrange
        ListingFactory factory = mock(ListingFactory.class);
        ListingRepositoryMem repo = new ListingRepositoryMem(factory);

        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                repo.findDirectSaleListingsContaining(null)
        );
    }

    @Test
    void findDirectSaleListingsContaining_throwsException_whenListIsEmpty() throws InstanceAlreadyExistsException {

        // Arrange
        ListingFactory factory = mock(ListingFactory.class);
        ListingRepositoryMem repo = new ListingRepositoryMem(factory);

        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                repo.findDirectSaleListingsContaining(List.of())
        );
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
//        ListingRepositoryMem repo = new ListingRepositoryMem(listingFactoryDouble);
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
//        ListingRepositoryMem repo = new ListingRepositoryMem(listingFactoryDouble);
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
//        ListingRepositoryMem repo = new ListingRepositoryMem(listingFactoryDouble);
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
//        ListingRepositoryMem repo = new ListingRepositoryMem(listingFactoryDouble);
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
//        ListingRepositoryMem repo = new ListingRepositoryMem(listingFactoryDouble);
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
//        ListingRepositoryMem repo = new ListingRepositoryMem(listingFactoryDouble);
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
//        ListingRepositoryMem repo = new ListingRepositoryMem(listingFactoryDouble);
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