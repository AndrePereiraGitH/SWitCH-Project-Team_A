package TOPSECRET.dto;

import TOPSECRET.controller.GetListOfItemsOnAuctionOfGivenGenreController;
import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;

import TOPSECRET.repository.AuctionRepository;
import TOPSECRET.repository.GenreRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;


class AuctionMapper_US022Test {

    @Test
    void getListOfItemsOnAuctionNullGenreThrowsException() {
        // Arrange
        GetListOfItemsOnAuctionOfGivenGenreController controller =
                new GetListOfItemsOnAuctionOfGivenGenreController(
                        new AuctionRepository(),
                        GenreRepository.getINSTANCE(),
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
        Genre fantasy = new Genre("Fantasy");

        GetListOfItemsOnAuctionOfGivenGenreController controller =
                new GetListOfItemsOnAuctionOfGivenGenreController(
                        new AuctionRepository(),
                        GenreRepository.getINSTANCE(),
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
        Genre fantasy = new Genre("Fantasy");

        AuctionRepository auctionRepository = new AuctionRepository();
        auctionRepository.getAllAuctions().add(createAuctionWithGenre(fantasy));

        GetListOfItemsOnAuctionOfGivenGenreController controller =
                new GetListOfItemsOnAuctionOfGivenGenreController(
                        auctionRepository,
                        GenreRepository.getINSTANCE(),
                        new AuctionMapper_US022(),
                        new GenreMapper_US022()
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

        AuctionRepository auctionRepository = new AuctionRepository();
        auctionRepository.getAllAuctions().add(createAuctionWithGenre(fantasy));
        auctionRepository.getAllAuctions().add(createAuctionWithGenre(romance));

        GetListOfItemsOnAuctionOfGivenGenreController controller =
                new GetListOfItemsOnAuctionOfGivenGenreController(
                        auctionRepository,
                        GenreRepository.getINSTANCE(),
                        new AuctionMapper_US022(),
                        new GenreMapper_US022()
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

        AuctionRepository auctionRepository = new AuctionRepository();
        auctionRepository.getAllAuctions().add(createAuctionWithGenre(fantasy));
        auctionRepository.getAllAuctions().add(createAuctionWithGenre(fantasy));

        GetListOfItemsOnAuctionOfGivenGenreController controller =
                new GetListOfItemsOnAuctionOfGivenGenreController(
                        auctionRepository,
                        GenreRepository.getINSTANCE(),
                        new AuctionMapper_US022(),
                        new GenreMapper_US022()
                );
        // Act
        List<AuctionDTO_US022> result =
                controller.getListOfItemsOnAuctionGivenGenre(fantasy);
        // Assert
        assertEquals(2, result.size());
    }

    // =========================================================
    // Private Methods to Help Instantiate

    private Auction createAuctionWithGenre(Genre genre) {

        BibliographicalData bd = new BibliographicalData(
                "Book Title",
                List.of(new Author(
                        new Profile("Author", new Country("Portugal"), LocalDate.of(1980, 1, 1)),
                        "bio")),
                new Locale("pt","PT"),
                new Publisher("Pub","123", null),
                LocalDate.of(2020,1,1),
                List.of(genre),
                243,
                "Description"
        );

        User seller = createUser("Seller");

        Publication publication = new Publication(
                bd,
                new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Paperback"),
                seller,
                PublicationStatus.FOR_SALE,
                "1st",
                Code.fromIsbn13("9781234567890")
        );

        Item item = new Item(
                Condition.LIKE_NEW,
                100,
                new Dimensions(10,10,10),
                publication
        );

        Listing listing = new Listing(
                List.of(item),
                new Price(new BigDecimal("10.00")),
                "Short desc",
                "Valid Title",
                seller,
                List.of(new Picture("pic1"))
        );

        LocalDateTime start = LocalDateTime.now().plusMinutes(1);
        //LocalDateTime end = LocalDateTime.now().plusMinutes(1);
        LocalDateTime end = start.plusMinutes(1);

        return new Auction(
                listing,
                start,
                end,
                new Price(new BigDecimal("20.00"))
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