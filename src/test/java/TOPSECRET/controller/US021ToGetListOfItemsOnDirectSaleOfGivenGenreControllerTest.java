package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.repository.DirectSaleRepository;
import TOPSECRET.repository.GenreRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class US021ToGetListOfItemsOnDirectSaleOfGivenGenreControllerTest {

    // Metodo auxiliar para criar um controller
    private US021ToGetListOfItemsOnDirectSaleOfGivenGenreController createController() {
        List<Genre> genres = new ArrayList<>();
        List<DirectSale> directSales = new ArrayList<>();
        GenreRepository genreRepository = new GenreRepository(genres);
        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);
        return new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(genreRepository, directSaleRepository);
    }

    // Constructor tests
    @Test
    public void testConstructor_Success() {
        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller = createController();
        assertNotNull(controller);
    }

    @Test
    public void testConstructor_NullGenreRepository() {
        List<DirectSale> directSales = new ArrayList<>();
        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(null, directSaleRepository);
        });

        assertEquals("GenreRepository and directSaleRepository cannot be null", exception.getMessage());
    }

    @Test
    public void testConstructor_NullDirectSaleRepository() {
        List<Genre> genres = new ArrayList<>();
        GenreRepository genreRepository = new GenreRepository(genres);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(genreRepository, null);
        });

        assertEquals("GenreRepository and directSaleRepository cannot be null", exception.getMessage());
    }

    @Test
    public void testConstructor_BothNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(null, null);
        });

        assertEquals("GenreRepository and directSaleRepository cannot be null", exception.getMessage());
    }

    // getAllGenres tests
    @Test
    public void testGetAllGenres_EmptyList() {
        //Arrange
        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller = createController();

        //Act
        List<Genre> result = controller.getAllGenres();

        //Assert
        assertNotNull(result); // Confirma que a lista existe
        assertTrue(result.isEmpty()); // Verifica que a lista está vazia
    }

    @Test
    public void testGetAllGenres_WithGenres() {
        //Arrange
        List<Genre> genres = new ArrayList<>();
        List<DirectSale> directSales = new ArrayList<>();
        GenreRepository genreRepository = new GenreRepository(genres);
        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);
        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(genreRepository, directSaleRepository);

        genreRepository.createAndAddGenre("Fiction");
        genreRepository.createAndAddGenre("Science");

        //Act
        List<Genre> result = controller.getAllGenres();

        //Assert
        assertNotNull(result);
        assertEquals(2, result.size()); // Valida que existe e tem os 2 géneros acrescentados
    }

//    // getListOfItemsOnDirectSale tests
//    @Test
//    public void testGetListOfItemsOnDirectSale_NullGenre() {
//        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller = createController();
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            controller.getListOfItemsOnDirectSale(null);
//        });
//
//        assertEquals("Genre cannot be null", exception.getMessage());
//    }
//
//    @Test
//    public void testGetEmptyListOfItemsOnDirectSale_ValidGenre() {
//        // Cria o controller
//        List<Genre> genres = new ArrayList<>();
//        GenreRepository genreRepository = new GenreRepository(genres);
//        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller = createController();
//
//        Genre genre = genreRepository.createAndAddGenre("Fiction");
//
//        List<DirectSale> result = controller.getListOfItemsOnDirectSale(genre);
//
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    public void testGetListOfItemsOnDirectSale_ValidGenre() {
//        // Cria repositórios
//        List<Genre> genres = new ArrayList<>();
//        List<DirectSale> directSales = new ArrayList<>();
//        GenreRepository genreRepository = new GenreRepository(genres);
//        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);
//
//        // Cria o controller com os mesmos repositórios
//        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
//                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(genreRepository, directSaleRepository);
//
//        // Cria o género
//        Genre genre = genreRepository.createAndAddGenre("Fiction");
//
//        // Cria um listing com item do género "Fiction"
//        Listing listing = createListing();
//        LocalDateTime date = LocalDateTime.of(2024, 12, 1, 9, 0);
//
//        // Adiciona o DirectSale ao repositório
//        directSaleRepository.createAndAddDirectSale(listing, date);
//
//        // Testa
//        List<DirectSale> result = controller.getListOfItemsOnDirectSale(genre);
//
//        assertNotNull(result);
//        assertEquals(1, result.size());
//    }


//    private static Listing createListing(){
//        return new Listing(
//                List.of(createItem()),
//                new Price(new BigDecimal("19.99")),
//                "Short Description",
//                "Listing Title",
//                createUser(),
//                List.of(new Picture("pic"))
//        );
//    }

//    private static Item createItem(){
//        return new Item(
//                Condition.LIKE_NEW,
//                230.30,
//                new Dimensions(123, 242, 124),
//                createPublication()
//        );
//    }

    private static User createUser(){
        return new User(
                "Seller",
                new Contact(
                        "918374628",
                        "+351",
                        new Email("user", "example.com"),
                        new Address("Avenida da Liberdade", "25", "3470-302", createCountry())
                ),
                new Profile(
                        "Seller",
                        createCountry(),
                        LocalDate.of(1993, 9, 19)
                ),
                UserRole.NORMAL_USER
        );
    }


    private static Country createCountry(){
        return new Country(
                "Portugal"
        );
    }

    private static Profile createProfile(){
        return new Profile(
                "Freida",
                createCountry(),
                LocalDate.of(1968, 10, 12)
        );
    }

    private static Author createAuthor(){
        return new Author(
                createProfile(),
                "valid bio"
        );
    }

//    private static BibliographicalData createBibliographicalData(){
//        return new BibliographicalData(
//                "Harry Potter: Pedra Filosofal",
//                List.of(createAuthor()),
//                new Locale("pt", "PT"),
//                new Publisher(
//                        "PortoEditora",
//                        "192394938",
//                        new Contact(
//                                "917485647",
//                                "+351",
//                                new Email("asa", "example.com"),
//                                new Address("Rua Dr. Angelo Miranda", "182", "4540-340", createCountry())
//                        )
//                ),
//                LocalDate.of(2020, 1, 30),
//                List.of(new Genre("Fiction")),
//                504,
//                "Text Description"
//        );
//    }
//
//    private static PublicationType createPublicationType(){
//        return new PublicationType(
//                MediaFormat.BOOK,
//                TypeOfBinding.HARDBACK,
//                "Details"
//        );
//    }

    private static Code createCode(){
        return Code.fromIsbn10(
                "1928375849"
        );
    }

//    private static Publication createPublication(){
//        return new Publication(
//                createBibliographicalData(),
//                createPublicationType(),
//                createUser(),
//                PublicationStatus.FOR_SALE,
//                "1st Edition",
//                createCode()
//        );
//    }

}