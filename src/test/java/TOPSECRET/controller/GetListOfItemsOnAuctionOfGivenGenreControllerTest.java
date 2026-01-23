package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.dto.AuctionDTO_US022;
import TOPSECRET.dto.AuctionMapper_US022;
import TOPSECRET.dto.GenreDTO_US022;
import TOPSECRET.dto.GenreMapper_US022;
import TOPSECRET.repository.AuctionRepository;
import TOPSECRET.repository.GenreRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class GetListOfItemsOnAuctionOfGivenGenreControllerTest {

    @Test
    void getAllGenresReturnsAllGenresFromRepository() {
        // Arrange
        GenreRepository genreRepository = GenreRepository.getINSTANCE();
        genreRepository.clear();

        GenreMapper_US022 genreMapper = new GenreMapper_US022();

        genreRepository.createAndAddGenre("Fantasy");
        genreRepository.createAndAddGenre("Romance");

        GetListOfItemsOnAuctionOfGivenGenreController controller =
                new GetListOfItemsOnAuctionOfGivenGenreController(
                        new AuctionRepository(),
                        genreRepository,
                        new AuctionMapper_US022(),
                        genreMapper
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
        GenreRepository genreRepository = GenreRepository.getINSTANCE();
        genreRepository.clear();

        GetListOfItemsOnAuctionOfGivenGenreController controller =
                new GetListOfItemsOnAuctionOfGivenGenreController(
                        new AuctionRepository(),
                        genreRepository,
                        new AuctionMapper_US022(),
                        new GenreMapper_US022()
                );

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.getListOfItemsOnAuctionGivenGenre(null)
        );
    }

    @Test
    void getListOfItemsOnAuctionNoAuctionsReturnsEmptyList() {
        // Arrange
        GenreRepository genreRepository = GenreRepository.getINSTANCE();
        genreRepository.clear();

        Genre fantasy = new Genre("Fantasy");

        GetListOfItemsOnAuctionOfGivenGenreController controller =
                new GetListOfItemsOnAuctionOfGivenGenreController(
                        new AuctionRepository(),
                        genreRepository,
                        new AuctionMapper_US022(),
                        new GenreMapper_US022()
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
        GenreRepository genreRepository = GenreRepository.getINSTANCE();
        genreRepository.clear();

        Genre fantasy = new Genre("Fantasy");

        AuctionRepository auctionRepository = new AuctionRepository();
        auctionRepository.getAllAuctions().add(createAuctionWithGenre(fantasy));

        GetListOfItemsOnAuctionOfGivenGenreController controller =
                new GetListOfItemsOnAuctionOfGivenGenreController(
                        auctionRepository,
                        genreRepository,
                        new AuctionMapper_US022(),
                        new GenreMapper_US022()
                );
        // Act
        List<AuctionDTO_US022> result =
                controller.getListOfItemsOnAuctionGivenGenre(fantasy);
        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void getListOfItemsOnAuctionFiltersByGenre() {
        // Arrange
        GenreRepository genreRepository = GenreRepository.getINSTANCE();
        genreRepository.clear();

        Genre fantasy = new Genre("Fantasy");
        Genre romance = new Genre("Romance");

        AuctionRepository auctionRepository = new AuctionRepository();
        auctionRepository.getAllAuctions().add(createAuctionWithGenre(fantasy));
        auctionRepository.getAllAuctions().add(createAuctionWithGenre(romance));

        GetListOfItemsOnAuctionOfGivenGenreController controller =
                new GetListOfItemsOnAuctionOfGivenGenreController(
                        auctionRepository,
                        genreRepository,
                        new AuctionMapper_US022(),
                        new GenreMapper_US022()
                );
        // Act
        List<AuctionDTO_US022> result =
                controller.getListOfItemsOnAuctionGivenGenre(fantasy);
        // Assert
        assertEquals(1, result.size());
    }

    // ==========================================================
    // Private Methods to Help Instantiate

    private Auction createAuctionWithGenre(Genre genre) {

        BibliographicalData bd = createBibliographicalDataWithGenre(genre);
        User seller = createUser("Seller");
        Publication publication = createPublication(bd, seller);
        Item item = new Item(Condition.LIKE_NEW, 100, new Dimensions(10,10,10), publication);

        Listing listing = new Listing(
                List.of(item),
                new Price(new BigDecimal("10.00")),
                "Short desc",
                "Valid Title",
                seller,
                List.of(new Picture("pic1"))
        );

        LocalDateTime start = LocalDateTime.now().plusMinutes(1);
        LocalDateTime end = start.plusDays(1);

        return new Auction(listing, start, end, new Price(new BigDecimal("20.00")));
    }
    private BibliographicalData createBibliographicalDataWithGenre(Genre genre) {
        return new BibliographicalData(
                "Book",
                List.of(new Author(new Profile("Freida", new Country("Spain"), LocalDate.of(1975, 4, 25)), "Valid Bio")),
                new Locale("pt","PT"),
                new Publisher("Pub","123",null),
                LocalDate.now(),
                List.of(genre),
                100,
                "Desc"
        );
    }

    private Publication createPublication(BibliographicalData bd, User seller) {
        return new Publication(
                bd,
                new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Paperback"),
                seller,
                PublicationStatus.FOR_SALE,
                "1st",
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