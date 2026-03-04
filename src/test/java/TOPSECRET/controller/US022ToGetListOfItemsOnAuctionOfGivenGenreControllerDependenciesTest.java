package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.dto.AuctionDTO_US022;
import TOPSECRET.dto.GenreDTO_US022;
import TOPSECRET.repository.AuctionRepository;
import TOPSECRET.repository.GenreRepository;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class US022ToGetListOfItemsOnAuctionOfGivenGenreControllerTest {

    private AuctionFactory auctionFactory;

    @Test
    void getAllGenresReturnsAllGenresFromRepository() {
        // Arrange
        GenreRepository genreRepository = new GenreRepository(new ArrayList<>(), new GenreFactory());
        genreRepository.createAndAddGenre("Fantasy");
        genreRepository.createAndAddGenre("Romance");

        US022ToGetListOfItemsOnAuctionOfGivenGenreController controller =
                new US022ToGetListOfItemsOnAuctionOfGivenGenreController(
                        new AuctionRepository(auctionFactory),
                        genreRepository
                );

        // Act
        List<GenreDTO_US022> result = controller.getAllGenres();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(g -> g.name.equals("Fantasy")));
        assertTrue(result.stream().anyMatch(g -> g.name.equals("Romance")));
    }

    @Test
    void getListOfItemsOnAuctionNullGenreThrowsException() {
        // Arrange
        US022ToGetListOfItemsOnAuctionOfGivenGenreController controller =
                new US022ToGetListOfItemsOnAuctionOfGivenGenreController(
                        new AuctionRepository(auctionFactory),
                        new GenreRepository(new ArrayList<>(), new GenreFactory())
                );

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.getListOfItemsOnAuctionGivenGenre(null)
        );
    }

    @Test
    void getListOfItemsOnAuctionNoAuctionsReturnsEmptyList() {
        // Arrange
        Genre fantasy = new Genre("Fantasy");

        US022ToGetListOfItemsOnAuctionOfGivenGenreController controller =
                new US022ToGetListOfItemsOnAuctionOfGivenGenreController(
                        new AuctionRepository(auctionFactory),
                        new GenreRepository(new ArrayList<>(), new GenreFactory())
                );

        // Act
        List<AuctionDTO_US022> result =
                controller.getListOfItemsOnAuctionGivenGenre(fantasy);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getListOfItemsOnAuctionGivenGenre_returnsDtosWhenAuctionsExist() {

        // Arrange
        AuctionRepository auctionRepoMock = mock(AuctionRepository.class);
        GenreRepository genreRepoMock = mock(GenreRepository.class);

        US022ToGetListOfItemsOnAuctionOfGivenGenreController controller =
                new US022ToGetListOfItemsOnAuctionOfGivenGenreController(
                        auctionRepoMock,
                        genreRepoMock
                );

        Genre genre = new Genre("Fiction");

        BibliographicalData bd = mock(BibliographicalData.class);
        when(bd.getTitle()).thenReturn("Test Title");
        when(bd.getGenres()).thenReturn(List.of(genre));

        PublicationSample publicationSample = mock(PublicationSample.class);
        when(publicationSample.getBibliographicalData()).thenReturn(bd);

        Listing listing = mock(Listing.class);
        when(listing.getItems()).thenReturn(List.of(publicationSample));

        Auction auction = mock(Auction.class);
        when(auction.getListing()).thenReturn(listing);

        when(auctionRepoMock.getListOfItemsOnAuctionGivenGenre(genre))
                .thenReturn(List.of(auction));

        // Act
        List<AuctionDTO_US022> result =
                controller.getListOfItemsOnAuctionGivenGenre(genre);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test Title", result.get(0).title);
    }

//   @Test
//    void getListOfItemsOnAuctionOneMatchingAuctionReturnsOneDTO() {
//        // Arrange
//        Genre fantasy = new Genre("Fantasy");
//
//        AuctionRepository auctionRepository = new AuctionRepository(auctionFactory);
//        auctionRepository.getAllAuctions().add(createAuctionWithGenre(fantasy));
//
//        US022ToGetListOfItemsOnAuctionOfGivenGenreController controller =
//                new US022ToGetListOfItemsOnAuctionOfGivenGenreController(
//                        auctionRepository,
//                        new GenreRepository(new ArrayList<>())
//                );
//
//        // Act
//        List<AuctionDTO_US022> result =
//                controller.getListOfItemsOnAuctionGivenGenre(fantasy);
//
//        // Assert
//        assertEquals(1, result.size());
//        assertTrue(result.get(0).genres.contains("Fantasy"));
//    }

//    @Test
//    void getListOfItemsOnAuctionFiltersByGenre() {
//        // Arrange
//        Genre fantasy = new Genre("Fantasy");
//        Genre romance = new Genre("Romance");
//
//        AuctionRepository auctionRepository = new AuctionRepository(auctionFactory);
//        auctionRepository.getAllAuctions().add(createAuctionWithGenre(fantasy));
//        auctionRepository.getAllAuctions().add(createAuctionWithGenre(romance));
//
//        US022ToGetListOfItemsOnAuctionOfGivenGenreController controller =
//                new US022ToGetListOfItemsOnAuctionOfGivenGenreController(
//                        auctionRepository,
//                        new GenreRepository(new ArrayList<>())
//                );
//
//        // Act
//        List<AuctionDTO_US022> result =
//                controller.getListOfItemsOnAuctionGivenGenre(fantasy);
//
//        // Assert
//        assertEquals(1, result.size());
//        assertTrue(result.get(0).genres.contains("Fantasy"));
//        assertFalse(result.get(0).genres.contains("Romance"));
//    }

//    @Test
//    void getListOfItemsOnAuctionMultipleMatchingAuctionsReturnsAll() {
//        // Arrange
//        Genre fantasy = new Genre("Fantasy");
//
//        AuctionRepository auctionRepository = new AuctionRepository(auctionFactory);
//        auctionRepository.getAllAuctions().add(createAuctionWithGenre(fantasy));
//        auctionRepository.getAllAuctions().add(createAuctionWithGenre(fantasy));
//
//        US022ToGetListOfItemsOnAuctionOfGivenGenreController controller =
//                new US022ToGetListOfItemsOnAuctionOfGivenGenreController(
//                        auctionRepository,
//                        new GenreRepository(new ArrayList<>())
//                );
//
//        // Act
//        List<AuctionDTO_US022> result =
//                controller.getListOfItemsOnAuctionGivenGenre(fantasy);
//
//        // Assert
//        assertEquals(2, result.size());
//    }

    // ==========================================================
    // Private Methods to Help Instantiate

//    private Auction createAuctionWithGenre(Genre genre) {
//
//        BibliographicalData bd = createBibliographicalDataWithGenre(genre);
//        User seller = createUser("Seller");
//        Publication publication = createPublication(bd, seller);
//        PublicationSample item = new PublicationSample(
//                bd,
//                new User(
//                        "Marrike",
//                        new Contact("91233213","+351",
//                            new Email("a","b.com"),
//                                new Address("Rua","1","1111", new Country("Portugal"))),
//                new Profile("marrike", new Country("Portugal"), LocalDate.of(1990,1,1)),
//                UserRole.NORMAL_USER),
//                PublicationStatus.READ,
//                edition,
//                Condition.NEW
//        );
//
//        Listing listing = new Listing(
//                "Valid Title",
//                List.of(item),
//                SaleType.AUCTION,
//                new Price(new BigDecimal("10.00")),
//                "Short desc",
//                List.of(new Picture("pic1"))
//        );
//
//        LocalDateTime start = LocalDateTime.of(2026, 6, 30, 14, 30);
//        LocalDateTime end = LocalDateTime.of(2026, 7, 1, 14, 30);
//
//        return new Auction(listing, start, end, new Price(new BigDecimal("20.00")));
//    }
    private BibliographicalData createBibliographicalDataWithGenre(Genre genre) {
        AuthorFactory authorFactory = new AuthorFactory();

        return new BibliographicalData(
                "Book",
                List.of(authorFactory.createAuthorWithExistingProfile( new Profile("Freida", new Country("Spain"), LocalDate.of(1975, 4, 25)), "Valid Bio")),
                new Locale("pt","PT"),
//                new Publisher("Pub","123",null),
                LocalDate.now(),
                List.of(genre),
//                100,
                "Desc"
        );
    }
    // Edition
    Code code = Code.fromIsbn13 ("9783161484123");
    Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
    PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
    PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
    Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
    EditionFactory editionFactory = new EditionFactory();

    Edition edition= editionFactory.create("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

    private Publication createPublication(BibliographicalData bd, User seller) {
        return new Publication(
                bd,
                new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Paperback"),
                seller,
                PublicationStatus.READ,
//                "1st",
                edition,
                Code.fromIsbn13("9781234567890")
        );
    }

    private User createUser(String name) {
        return new User(
                name,
                new Contact("9123","+351",
                        new Email("a","b.com"),
                        new Address("Rua","1","1111", new Country("Portugal"))),
                new Profile(name, new Country("Portugal"), LocalDate.of(1990,1,1)),
                UserRole.NORMAL_USER
        );
    }
}