package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.dto.AuctionDTO_US022;
import TOPSECRET.dto.GenreDTO_US022;
import TOPSECRET.repository.AuctionRepository;
import TOPSECRET.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class US022ToGetListOfItemsOnAuctionOfGivenGenreControllerDependenciesTest {

    private AuctionFactory auctionFactory;
    private GenreFactory genreFactory;

    @BeforeEach
    void setUp() {
        AuctionFactory auctionFactory = new AuctionFactory();
        GenreFactory genreFactory = new GenreFactory();
    }

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
    void getListOfItemsOnAuctionOneMatchingAuctionReturnsOneDTO() {
        // Arrange
        Genre fantasy = new Genre("Fantasy");

        AuctionRepository auctionRepository = new AuctionRepository(auctionFactory);
        auctionRepository.getAllAuctions().add(createAuctionWithGenre(fantasy));

        US022ToGetListOfItemsOnAuctionOfGivenGenreController controller =
                new US022ToGetListOfItemsOnAuctionOfGivenGenreController(
                        auctionRepository,
                        new GenreRepository(new ArrayList<>(), new GenreFactory())
                );

        // Act
        List<AuctionDTO_US022> result =
                controller.getListOfItemsOnAuctionGivenGenre(fantasy);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.get(0).genres.contains("Fantasy"));
    }
    @Test
    void getListOfItemsOnAuctionFiltersByGenre() {
        // Arrange
        Genre fantasy = new Genre("Fantasy");
        Genre romance = new Genre("Romance");

        AuctionRepository auctionRepository = new AuctionRepository(auctionFactory);
        auctionRepository.getAllAuctions().add(createAuctionWithGenre(fantasy));
        auctionRepository.getAllAuctions().add(createAuctionWithGenre(romance));

        US022ToGetListOfItemsOnAuctionOfGivenGenreController controller =
                new US022ToGetListOfItemsOnAuctionOfGivenGenreController(
                        auctionRepository,
                        new GenreRepository(new ArrayList<>(), new GenreFactory())
                );

        // Act
        List<AuctionDTO_US022> result =
                controller.getListOfItemsOnAuctionGivenGenre(fantasy);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.get(0).genres.contains("Fantasy"));
        assertFalse(result.get(0).genres.contains("Romance"));
    }
    @Test
    void getListOfItemsOnAuctionMultipleMatchingAuctionsReturnsAll() {
        // Arrange
        Genre fantasy = new Genre("Fantasy");

        AuctionRepository auctionRepository = new AuctionRepository(auctionFactory);
        auctionRepository.getAllAuctions().add(createAuctionWithGenre(fantasy));
        auctionRepository.getAllAuctions().add(createAuctionWithGenre(fantasy));

        US022ToGetListOfItemsOnAuctionOfGivenGenreController controller =
                new US022ToGetListOfItemsOnAuctionOfGivenGenreController(
                        auctionRepository,
                        new GenreRepository(new ArrayList<>(), new GenreFactory())
                );

        // Act
        List<AuctionDTO_US022> result =
                controller.getListOfItemsOnAuctionGivenGenre(fantasy);

        // Assert
        assertEquals(2, result.size());
    }

 // ==========================================================
 // Private Methods to Help Instantiate



    private Auction createAuctionWithGenre(Genre genre) {

        BibliographicalData bd = createBibliographicalDataWithGenre(genre);
        User seller = createUser("Seller");

        PublicationSampleFactory pSampleFactory = new PublicationSampleFactory();
        PublicationSample item = pSampleFactory.create(
                bd,
                new User(
                        "Marrike",
                        new Contact("91233213","+351",
                            new Email("a","b.com"),
                                new Address("Rua","1","1111", new Country("Portugal"))),
                new Profile("marrike", new Country("Portugal"), LocalDate.of(1990,1,1)),
                UserRole.NORMAL_USER),
                PublicationStatus.READ,
                edition,
                Condition.NEW
        );

        ListingFactory listingFactory = new ListingFactory();
        Listing listing = listingFactory.create(
                "Valid Title",
                List.of(item),
                SaleType.AUCTION,
                new Price(new BigDecimal("10.00")),
                "Short desc",
                List.of(new Picture("pic1"))
        );

        LocalDateTime start = LocalDateTime.of(2026, 6, 30, 14, 30);
        LocalDateTime end = LocalDateTime.of(2026, 7, 1, 14, 30);

        AuctionFactory auctionFactory = new AuctionFactory();
        return auctionFactory.create(listing, start, end, new Price(new BigDecimal("20.00")));
    }

private BibliographicalData createBibliographicalDataWithGenre(Genre genre) {
        BibliographicalDataFactory bdFactory = new BibliographicalDataFactory();
        AuthorFactory authorFactory = new AuthorFactory();
        return bdFactory.create(
                "Book",
                List.of(authorFactory.createWithExistingProfile( new Profile("Freida", new Country("Spain"), LocalDate.of(1975, 4, 25)), "Valid Bio")),
                new Locale("pt","PT"),
                LocalDate.now(),
                List.of(genre),
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
                edition,
                Code.fromIsbn13("9781234567890")
        );
    }

    private User createUser(String name) {
        UserFactory userFactory = new UserFactory();
        return userFactory.create(
                name,
                new Contact("9123","+351",
                        new Email("a","b.com"),
                        new Address("Rua","1","1111", new Country("Portugal"))),
                new Profile(name, new Country("Portugal"), LocalDate.of(1990,1,1)),
                UserRole.NORMAL_USER
        );
    }
}