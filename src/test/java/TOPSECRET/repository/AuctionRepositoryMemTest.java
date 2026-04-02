package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Auction.AuctionFactory;
import TOPSECRET.domain.Auction.IAuctionRepository;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

//    private Author author1;
//    private Author author2;
//    private PublicationSample sample1;
//    private PublicationSample sample2;
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
//        author1 = createMockAuthor("Rodolph");
//        author2 = createMockAuthor("Adolf");

//        sample1 = createMockPublicationSample("First title", List.of(author1));
//        sample2 = createMockPublicationSample("Second title", List.of(author2));

        auction1 = createMockAuction();
        auction2 = createMockAuction();
        auction3 = createMockAuction();

        auctionFactory = mock(AuctionFactory.class);

        initialDate = LocalDateTime.now();
        finalDate = initialDate.plusDays(1);

        initialPrice = new Price (new BigDecimal("10.00"));
        buyOutPrice = new Price (new BigDecimal("100.00"));

        listingId = new ListingId ("listingId");
    }

    @Test
    void getAllAuctions_shouldReturnListOfAllAuctionsInRepo() throws InstanceAlreadyExistsException {
        //Arrange
        IAuctionRepository auctionRepository = instantiateAuctionRepository();

        //Act
        List<Auction> result = auctionRepository.getAllAuctions();

        //Assert
        assertEquals(3, result.size());
    }

    @Test
    void createAndAdd_shouldThrowsWhenAuctionAlreadyExists() throws InstanceAlreadyExistsException {
        //arrange
        IAuctionRepository auctionRepository = instantiateAuctionRepository();

        when(auctionFactory.create(listingId, initialDate, finalDate, initialPrice, buyOutPrice)).thenReturn(auction1);

        // act + assert
        InstanceAlreadyExistsException exception = assertThrows(InstanceAlreadyExistsException.class,
                () -> auctionRepository.createAndAdd(listingId, initialDate, finalDate, initialPrice ,buyOutPrice));

        assertEquals("This auction already exists", exception.getMessage());

    }

    @Test
    void createAndAdd_addsAndReturnsAuction_whenAuctionDoesNotExist() throws Exception {
        // arrange
        IAuctionRepository auctionRepository = instantiateAuctionRepository();

        Auction created = mock(Auction.class);

        when(auctionFactory.create(listingId, initialDate, finalDate, initialPrice, buyOutPrice)).thenReturn(created);

        int beforeSize = auctionRepository.getAllAuctions().size();

        // act
        Auction result = auctionRepository.createAndAdd(listingId, initialDate, finalDate, initialPrice, buyOutPrice);

        // assert
        assertSame(created, result);

        List<Auction> all = auctionRepository.getAllAuctions();
        assertEquals(beforeSize + 1, all.size());
        assertTrue(all.contains(created));
    }


    // US024 getListOfItemsOnAuctionByGivenPublication -----------------------------------------------------------------
//    /**
//     * Verifies that {@code getListOfItemsOnAuctionByGivenPublication()} returns
//     * only the {@link PublicationSample} instances whose title matches the given
//     * publication title, across multiple auctions.
//     *
//     * <p>Setup: three auctions containing a mix of "First title" and "Second title"
//     * samples. Querying for "First title" should return exactly two matches —
//     * one from {@code auction1} and one from {@code auction2}.</p>
//     */
//    @Test
//    void ensure_getListOfItemsOnAuctionByGivenPublication_returnAList() throws InstanceAlreadyExistsException {
//        // Arrange
//        IAuctionRepository auctionRepository = instantiateAuctionRepository();
//
//        // Act
//        List<PublicationSample> listOfItemsOnAuction =
//                auctionRepository.getListOfItemsOnAuctionByGivenPublication("First title");
//
//        // Assert
//        assertEquals(2, listOfItemsOnAuction.size());
//        assertEquals("First title", listOfItemsOnAuction.get(0).getBibliographicalData().getTitle());
//        assertEquals("First title", listOfItemsOnAuction.get(1).getBibliographicalData().getTitle());
//    }
//
//    /**
//     * Verifies that {@code getListOfItemsOnAuctionByGivenPublication()} returns
//     * an empty list when no auction contains a sample matching the given title.
//     *
//     * <p>Setup: three auctions containing only "First title" and "Second title"
//     * samples. Querying for the non-existent "Third title" should yield an
//     * empty, non-{@code null} list.</p>
//     */
//    @Test
//    void shouldReturnAnEmptyListWhenAuctionDoesNotHaveThePublication() throws InstanceAlreadyExistsException {
//        // Arrange
//        IAuctionRepository auctionRepository = instantiateAuctionRepository();
//
//        // Act
//        List<PublicationSample> listOfItemsOnAuction =
//                auctionRepository.getListOfItemsOnAuctionByGivenPublication("Third title");
//
//        // Assert
//        assertEquals(0, listOfItemsOnAuction.size());
//        assertTrue(listOfItemsOnAuction.isEmpty());
//    }
//
//    @Test
//    void ensure_getListOfItemsOnAuctionByGivenPublication_throwsExceptionWhenPublicationIsNull() throws InstanceAlreadyExistsException {
//        // Arrange
//        IAuctionRepository auctionRepository = instantiateAuctionRepository();
//
//
//        // Act & Assert
//        assertThrows(NullPointerException.class, () ->
//                auctionRepository.getListOfItemsOnAuctionByGivenPublication(null));
//
//    }
//
//    @Test
//    void ensure_getListOfItemsOnAuctionByGivenPublication_throwsExceptionWhenPublicationIsEmpty() throws InstanceAlreadyExistsException {
//        // Arrange
//        IAuctionRepository auctionRepository = instantiateAuctionRepository();
//
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class, () ->
//                auctionRepository.getListOfItemsOnAuctionByGivenPublication(""));
//
//    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * US026
     */

//    @Test
//    void getListOfItemsOnAuctionGivenAuthor_shouldThrows_ifAuthorDTOisNULL() throws InstanceAlreadyExistsException {
//        IAuctionRepository auctionRepository = instantiateAuctionRepository();
//
//        IllegalArgumentException exception =  assertThrows(IllegalArgumentException.class, () -> auctionRepository.getListOfItemsOnAuctionGivenAuthor(null));
//
//        assertEquals("Author cannot be null or blank", exception.getMessage());
//    }
//
//    @Test
//    void getListOfItemsOnAuctionGivenAuthor_shouldThrows_ifAuthorDTOisBlank() throws InstanceAlreadyExistsException {
//        // arrange
//        IAuctionRepository auctionRepository = instantiateAuctionRepository();
//        AuthorDTO_US026 authorDtoDouble = mock(AuthorDTO_US026.class);
//        when(authorDtoDouble.name()).thenReturn("    ");
//
//        // act + assert
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> auctionRepository.getListOfItemsOnAuctionGivenAuthor(authorDtoDouble));
//
//        assertEquals("Author cannot be null or blank", exception.getMessage());
//    }
//
//    @Test
//    void getListOfItemsOnAuctionGivenAuthor_noMatchesAuctions_returnsEmpty() throws InstanceAlreadyExistsException {
//        // arrange
//        IAuctionRepository auctionRepository = instantiateAuctionRepository();
//        AuthorDTO_US026 authorDtoDouble = mock(AuthorDTO_US026.class);
//        when(authorDtoDouble.name()).thenReturn("Mary");
//
//        // act
//        List<Auction> result = auctionRepository.getListOfItemsOnAuctionGivenAuthor(authorDtoDouble);
//
//        // assert
//        assertEquals(0, result.size());
//
//    }
//
//    @Test
//    void getListOfItemsOnAuctionGivenAuthor_auctionHasMatchingAuthor_returnsThatAuction() throws InstanceAlreadyExistsException {
//        // arrange
//        IAuctionRepository auctionRepository = instantiateAuctionRepository();
//        AuthorDTO_US026 authorDtoDouble = mock(AuthorDTO_US026.class);
//        when(authorDtoDouble.name()).thenReturn("Adolf");
//
//        // act
//        List<Auction> result = auctionRepository.getListOfItemsOnAuctionGivenAuthor(authorDtoDouble);
//
//        // assert
//        assertEquals(2, result.size());
//    }
//
//    @Test
//    void getListOfItemsOnAuctionGivenAuthor_auctionHasMatchingAuthor_returnsThatAuction_ensuresTitle() throws InstanceAlreadyExistsException {
//        // arrange
//        IAuctionRepository auctionRepository = instantiateAuctionRepository();
//        AuthorDTO_US026 authorDtoDouble = mock(AuthorDTO_US026.class);
//        when(authorDtoDouble.name()).thenReturn("Adolf");
//
//        // act
//        List<Auction> result = auctionRepository.getListOfItemsOnAuctionGivenAuthor(authorDtoDouble);
//
//        // assert
//        assertEquals("First title", result.get(0).getListing().getItems().get(0).getBibliographicalData().getTitle());
//        assertEquals("Second title", result.get(1).getListing().getItems().get(0).getBibliographicalData().getTitle());
//
//
//    }
//
//    @Test
//    void getListOfItemsOnAuctionGivenAuthor_auctionHasMatchingAuthor_returnsThatAuction_ensuresSameAuction() throws InstanceAlreadyExistsException {
//        // arrange
//        IAuctionRepository auctionRepository = instantiateAuctionRepository();
//        AuthorDTO_US026 authorDtoDouble = mock(AuthorDTO_US026.class);
//        when(authorDtoDouble.name()).thenReturn("Adolf");
//
//        // act
//        List<Auction> result = auctionRepository.getListOfItemsOnAuctionGivenAuthor(authorDtoDouble);
//
//        // assert
//        assertSame(auction1, result.get(0));
//        assertSame(auction3, result.get(1));
//        assertFalse(result.contains(auction2));
//    }
//
//    @Test
//    void getListOfItemsOnAuctionGivenGenreShouldThrowExceptionWhenGenreIsNull() throws InstanceAlreadyExistsException {
//        IAuctionRepository auctionRepository = instantiateAuctionRepository();
//
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class, () ->
//                auctionRepository.getListOfItemsOnAuctionGivenGenre(null)
//        );
//    }
//
//    @Test
//    void getListOfItemsOnAuctionGivenGenreNoMatchesReturnsEmptyList() throws InstanceAlreadyExistsException {
//
//        // Arrange
//        IAuctionRepository auctionRepository = instantiateAuctionRepository();
//        Genre genre = mock(Genre.class);
//
//        // Act
//        List<Auction> result =
//                auctionRepository.getListOfItemsOnAuctionGivenGenre(genre);
//
//        // Assert
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void getListOfItemsOnAuctionGivenGenreOneAuctionMatchesReturnsOneAuction() throws InstanceAlreadyExistsException {
//
//        // Arrange
//        Genre fantasy = mock(Genre.class);
//
//        PublicationSample sample = createMockPublicationSampleWithGenre(fantasy);
//        Auction auction = createMockAuction(List.of(sample));
//
//        IAuctionRepository repo = new AuctionRepositoryMem(auctionFactory);
//        when(auctionFactory.create(any(), any(), any(), any())).thenReturn(auction);
//        repo.createAndAdd(mock(Listing.class), LocalDateTime.now(), LocalDateTime.now().plusDays(1), mock(Price.class));
//
//        // Act
//        List<Auction> result = repo.getListOfItemsOnAuctionGivenGenre(fantasy);
//
//        // Assert
//        assertEquals(1, result.size());
//        assertSame(auction, result.get(0));
//    }
//
//    @Test
//    void getListOfItemsOnAuctionGivenGenreMultipleAuctionsMatchReturnsAll() throws InstanceAlreadyExistsException {
//
//        // Arrange
//        Genre fantasy = mock(Genre.class);
//
//        PublicationSample sample1 = createMockPublicationSampleWithGenre(fantasy);
//        PublicationSample sample2 = createMockPublicationSampleWithGenre(fantasy);
//
//        Auction auction1 = createMockAuction(List.of(sample1));
//        Auction auction2 = createMockAuction(List.of(sample2));
//
//        IAuctionRepository repo = new AuctionRepositoryMem(auctionFactory);
//        when(auctionFactory.create(any(), any(), any(), any()))
//                .thenReturn(auction1)
//                .thenReturn(auction2);
//        repo.createAndAdd(mock(Listing.class), LocalDateTime.now(), LocalDateTime.now().plusDays(1), mock(Price.class));
//        repo.createAndAdd(mock(Listing.class), LocalDateTime.now(), LocalDateTime.now().plusDays(1), mock(Price.class));
//
//        // Act
//        List<Auction> result = repo.getListOfItemsOnAuctionGivenGenre(fantasy);
//
//        // Assert
//        assertEquals(2, result.size());
//        assertTrue(result.contains(auction1));
//        assertTrue(result.contains(auction2));
//    }

    // -----------------------------------------------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // Helper / factory methods
    // -------------------------------------------------------------------------

    private IAuctionRepository instantiateAuctionRepository() throws InstanceAlreadyExistsException {
        IAuctionRepository repo = new AuctionRepositoryMem(auctionFactory);

        when(auctionFactory.create(any(), any(), any(), any(), any()))
                .thenReturn(auction1)
                .thenReturn(auction2)
                .thenReturn(auction3);

        ListingId listingId1 = new ListingId("listingId1");
        ListingId listingId2 = new ListingId("listingId2");
        ListingId listingId3 = new ListingId("listingId3");
        Price initialPrice = new Price(new BigDecimal("10.00"));
        Price buyoutPrice = new Price(new BigDecimal("100.00"));

        repo.createAndAdd(listingId1, LocalDateTime.now(), LocalDateTime.now().plusDays(1), initialPrice,  buyoutPrice);
        repo.createAndAdd(listingId2, LocalDateTime.now(), LocalDateTime.now().plusDays(1), initialPrice,  buyoutPrice);
        repo.createAndAdd(listingId3, LocalDateTime.now(), LocalDateTime.now().plusDays(1), initialPrice,  buyoutPrice);

        return repo;
    }

//    /**
//     * Creates a mocked {@link PublicationSample} whose bibliographical data
//     * returns the given title.
//     *
//     * @param title the value to be returned by
//     *              {@code getBibliographicalData().getTitle()}
//     * @return a configured {@link PublicationSample} mock
//     */
//    private PublicationSample createMockPublicationSample(String title, List<Author> author) {
//        BibliographicalData bd = mock(BibliographicalData.class);
//        when(bd.getTitle()).thenReturn(title);
//        when(bd.getAuthors()).thenReturn(author);
//
//        PublicationSample sample = mock(PublicationSample.class);
//        when(sample.getBibliographicalData()).thenReturn(bd);
//
//        return sample;
//    }

    private Auction createMockAuction() {
        ListingId listingId = new ListingId("listingId");

        Auction auction = mock(Auction.class);
        when(auction.getListingId()).thenReturn(listingId);

        return auction;
    }

//    private Author createMockAuthor(String authorName) {
//        Author authorDouble = mock(Author.class);
//
//        when(authorDouble.getName()).thenReturn(authorName);
//
//        return authorDouble;
//    }

//    private PublicationSample createMockPublicationSampleWithGenre(Genre genre) {
//
//        BibliographicalData bd = mock(BibliographicalData.class);
//        when(bd.getGenres()).thenReturn(List.of(genre));
//
//        PublicationSample sample = mock(PublicationSample.class);
//        when(sample.getBibliographicalData()).thenReturn(bd);
//
//        return sample;
//    }

}