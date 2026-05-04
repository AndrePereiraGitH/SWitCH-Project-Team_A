package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Auction.AuctionFactory;
import TOPSECRET.domain.Auction.IAuctionRepository;
import TOPSECRET.domain.ValueObjects.AuctionId;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test suite for the in‑memory implementation of the auction repository
 * ({@link AuctionRepositoryMem}), validating its behaviour in creating,
 * storing, and retrieving {@link Auction} aggregates.
 *
 * <p>This suite ensures that the repository enforces all required invariants and
 * behaves consistently with domain expectations, including:
 * <ul>
 *     <li>correct delegation of auction creation to the {@link AuctionFactory};</li>
 *     <li>prevention of duplicate auctions through {@link InstanceAlreadyExistsException};</li>
 *     <li>successful creation and storage of new auctions with distinct identities;</li>
 *     <li>accurate retrieval of all auctions stored in the repository;</li>
 *     <li>robust handling of edge cases such as empty repositories or attempts
 *         to add an already‑existing auction.</li>
 * </ul>
 *
 * <p>Overall, this test class provides comprehensive coverage of the in‑memory
 * auction repository, ensuring predictable behaviour, correct enforcement of
 * domain constraints, and strong encapsulation of stored auction data.
 */
class AuctionRepositoryMemTest {

    private Auction auction1;
    private Auction auction2;
    private Auction auction3;
    private AuctionFactory auctionFactory;
    private LocalDateTime initialDate;
    private LocalDateTime finalDate;
    private Price initialPrice;
    private Price buyOutPrice;
    private ListingId listingId;


    @BeforeEach
    void setup(){

        auction1 = createAuctionDouble();
        auction2 = createAuctionDouble();
        auction3 = createAuctionDouble();

        auctionFactory = mock(AuctionFactory.class);

        initialDate = LocalDateTime.now();
        finalDate = initialDate.plusDays(1);

        initialPrice = new Price (new BigDecimal("10.00"));
        buyOutPrice = new Price (new BigDecimal("100.00"));

        listingId = new ListingId ("listingId");
    }

    // =================================================================================================================
    // ============== FIND ALL =========================================================================================
    // =================================================================================================================

    @Test
    void findAll_shouldReturnListOfAllAuctionsInRepo() throws InstanceAlreadyExistsException {
        //Arrange
        IAuctionRepository auctionRepository = instantiateAuctionRepository();

        //Act
        List<Auction> result = auctionRepository.findAll();

        //Assert
        assertEquals(3, result.size());
    }

    // =================================================================================================================
    // ============== SAVE =============================================================================================
    // =================================================================================================================

    @Test
    void save_shouldThrowsWhenAuctionAlreadyExists() throws InstanceAlreadyExistsException {
        //arrange
        IAuctionRepository auctionRepository = instantiateAuctionRepository();

        when(auctionFactory.create(listingId, initialDate, finalDate, initialPrice, buyOutPrice)).thenReturn(auction1);

        // act + assert
        InstanceAlreadyExistsException exception = assertThrows(InstanceAlreadyExistsException.class,
                () -> auctionRepository.save(auction1));

        assertEquals("This auction already exists", exception.getMessage());

    }

    @Test
    void save_addsAndReturnsAuction_whenAuctionDoesNotExist() throws Exception {
        // arrange
        IAuctionRepository auctionRepository = instantiateAuctionRepository();

        Auction newAuction = mock(Auction.class);

        when(auctionFactory.create(listingId, initialDate, finalDate, initialPrice, buyOutPrice)).thenReturn(newAuction);

        int beforeSize = auctionRepository.findAll().size();

        // act
        Auction result = auctionRepository.save(newAuction);

        // assert
        assertSame(newAuction, result);

        List<Auction> all = auctionRepository.findAll();
        assertEquals(beforeSize + 1, all.size());
        assertTrue(all.contains(newAuction));
    }

    // =================================================================================================================
    // =============== FIND BY ID ======================================================================================
    // =================================================================================================================

    @Test
    void findById_shouldThrowExceptionWhenIdIsNull() throws Exception {
        // arrange
        IAuctionRepository repo = instantiateAuctionRepository();

        // act + assert
        assertThrows(IllegalArgumentException.class, () -> {
            repo.findById(null);
        });
    }

    @Test
    void findById_shouldReturnAuctionWhenIdExists() throws Exception {
        // arrange
        AuctionId auctionId1 = new AuctionId("auction1");
        AuctionId auctionId2 = new AuctionId("auction2");
        AuctionId auctionId3 = new AuctionId("auction3");

        when(auction1.id()).thenReturn(auctionId1);
        when(auction2.id()).thenReturn(auctionId2);
        when(auction3.id()).thenReturn(auctionId3);

        IAuctionRepository repo = instantiateAuctionRepository();

        // act
        Optional<Auction> result = repo.findById(auctionId1);

        // assert
        assertTrue(result.isPresent());
        assertEquals(auction1, result.get());
    }

    @Test
    void findById_shouldReturnEmptyWhenIdDoesNotExist() throws Exception {
        // arrange
        AuctionId newId = new AuctionId("notExisting");
        AuctionId auctionId1 = new AuctionId("auction1");
        AuctionId auctionId2 = new AuctionId("auction2");
        AuctionId auctionId3 = new AuctionId("auction3");

        when(auction1.id()).thenReturn(auctionId1);
        when(auction2.id()).thenReturn(auctionId2);
        when(auction3.id()).thenReturn(auctionId3);

        IAuctionRepository repo = instantiateAuctionRepository();

        // act
        Optional<Auction> result = repo.findById(newId);

        // assert
        assertTrue(result.isEmpty());
    }

    // =================================================================================================================
    // =============== FIND BY LISTING ID ==============================================================================
    // =================================================================================================================

    @Test
    void findByListingId_shouldThrowException_whenListingIdsIsNull() throws Exception {
        // arrange
        IAuctionRepository repo = instantiateAuctionRepository();

        // act + assert
        assertThrows(IllegalArgumentException.class, () -> repo.findByListingIdIn(null));
    }

    @Test
    void findByListingId_shouldReturnAuctions_whenMultipleAuctionsMatchListingIds() throws Exception {
        // arrange
        ListingId listingId1 = new ListingId("listingId1");
        ListingId listingId2 = new ListingId("listingId2");
        ListingId listingId3 = new ListingId("listingId3");

        when(auction1.getListingId()).thenReturn(listingId1);
        when(auction2.getListingId()).thenReturn(listingId2);
        when(auction3.getListingId()).thenReturn(listingId3);

        IAuctionRepository repo = instantiateAuctionRepository();

        // act
        List<Auction> result = repo.findByListingIdIn(List.of(listingId1, listingId2));

        // assert
        assertEquals(2, result.size());
        assertTrue(result.contains(auction1));
        assertTrue(result.contains(auction2));
        assertFalse(result.contains(auction3));
    }

    @Test
    void findByListingId_shouldReturnAuction_whenOneAuctionMatchesListingIds() throws Exception {
        // arrange
        ListingId listingId1 = new ListingId("listingId1");
        ListingId listingId2 = new ListingId("listingId2");
        ListingId listingId3 = new ListingId("listingId3");

        when(auction1.getListingId()).thenReturn(listingId1);
        when(auction2.getListingId()).thenReturn(listingId2);
        when(auction3.getListingId()).thenReturn(listingId3);

        IAuctionRepository repo = instantiateAuctionRepository();

        // act
        List<Auction> result = repo.findByListingIdIn(List.of(listingId2));

        // assert
        assertEquals(1, result.size());
        assertEquals(auction2, result.get(0));
    }

    @Test
    void findByListingId_shouldReturnEmptyList_whenNoAuctionMatchesListingIds() throws Exception {
        // arrange
        ListingId listingId1 = new ListingId("listingId1");
        ListingId listingId2 = new ListingId("listingId2");
        ListingId listingId3 = new ListingId("listingId3");
        ListingId nonExistingListingId = new ListingId("nonExistingListingId");

        when(auction1.getListingId()).thenReturn(listingId1);
        when(auction2.getListingId()).thenReturn(listingId2);
        when(auction3.getListingId()).thenReturn(listingId3);

        IAuctionRepository repo = instantiateAuctionRepository();

        // act
        List<Auction> result = repo.findByListingIdIn(List.of(nonExistingListingId));

        // assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByListingId_shouldReturnEmptyList_whenRepositoryIsEmpty() {
        // arrange
        IAuctionRepository repo = new AuctionRepositoryMem();

        // act
        List<Auction> result = repo.findByListingIdIn(List.of(listingId));

        // assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

//    @Test
//    void getListOfItemsOnAuctionGivenGenre_shouldThrowException_whenListIsNull() {
//        // Arrange
//
//        IAuctionRepository repo = new AuctionRepositoryMem(auctionFactory);
//
//        //Act & Assert
//        assertThrows(IllegalArgumentException.class, () ->
//                repo.getListOfItemsOnAuctionGivenGenre(null));
//    }
//
//    @Test
//    void getListOfItemsOnAuctionGivenGenre_shouldReturnEmptyList() {
//        // Arrange
//        IAuctionRepository repo = new AuctionRepositoryMem(auctionFactory);
//        List<ListingId> listingIds = List.of(mock(ListingId.class));
//
//
//        // Act
//        List<Auction> result = repo.getListOfItemsOnAuctionGivenGenre(listingIds);
//
//        // Assert
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void getListOfItemsOnAuctionGivenGenre_shouldReturnMatchingAuctions() throws Exception {
//
//        // Arrange
//        AuctionFactory factory = mock(AuctionFactory.class);
//        IAuctionRepository repo = new AuctionRepositoryMem(factory);
//
//        ListingId listingId = mock(ListingId.class);
//
//        Auction auction = mock(Auction.class);
//        when(auction.getListingId()).thenReturn(listingId);
//
//        when(factory.create(any(), any(), any(), any(), any()))
//                .thenReturn(auction);
//
//        repo.createAndAdd(
//                listingId,
//                LocalDateTime.now(),
//                LocalDateTime.now().plusDays(1),
//                mock(Price.class),
//                mock(Price.class)
//        );
//
//        // Act
//        List<Auction> result =
//                repo.getListOfItemsOnAuctionGivenGenre(List.of(listingId));
//
//        // Assert
//        assertEquals(1, result.size());
//        assertTrue(result.contains(auction));
//    }


    // -------------------------------------------------------------------------
    // Helper / factory methods
    // -------------------------------------------------------------------------

    private IAuctionRepository instantiateAuctionRepository() throws InstanceAlreadyExistsException {
        IAuctionRepository repo = new AuctionRepositoryMem();

        when(auctionFactory.create(any(), any(), any(), any(), any()))
                .thenReturn(auction1)
                .thenReturn(auction2)
                .thenReturn(auction3);

        ListingId listingId1 = new ListingId("listingId1");
        ListingId listingId2 = new ListingId("listingId2");
        ListingId listingId3 = new ListingId("listingId3");
        Price initialPrice = new Price(new BigDecimal("10.00"));
        Price buyoutPrice = new Price(new BigDecimal("100.00"));

        repo.save(auction1);
        repo.save(auction2);
        repo.save(auction3);

        return repo;
    }


    private Auction createAuctionDouble() {
        ListingId listingId = new ListingId("listingId");

        Auction auction = mock(Auction.class);
        when(auction.getListingId()).thenReturn(listingId);

        return auction;
    }

}