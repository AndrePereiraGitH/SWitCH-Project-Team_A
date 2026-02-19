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

    // Constructor tests
    @Test
    public void testConstructor_Success() {
        //Arrange
        List<Genre> genres = new ArrayList<>();
        List<DirectSale> directSales = new ArrayList<>();
        GenreRepository genreRepository = new GenreRepository(genres);
        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);
        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(genreRepository, directSaleRepository);

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
        List<Genre> genres = new ArrayList<>();
        List<DirectSale> directSales = new ArrayList<>();
        GenreRepository genreRepository = new GenreRepository(genres);
        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);
        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(genreRepository, directSaleRepository);


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

    // getListOfItemsOnDirectSale tests
    @Test
    public void testGetListOfItemsOnDirectSale_NullGenre() {
        //Arrange
        List<Genre> genres = new ArrayList<>();
        List<DirectSale> directSales = new ArrayList<>();
        GenreRepository genreRepository = new GenreRepository(genres);
        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);
        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(genreRepository, directSaleRepository);


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.getListOfItemsOnDirectSaleOfAGivenGenre(null);
        });

        assertEquals("Genre cannot be null", exception.getMessage());
    }

    @Test
    public void testGetEmptyListOfItemsOnDirectSale_ValidGenre() {
        // Cria o controller
        List<Genre> genres = new ArrayList<>();
        List<DirectSale> directSales = new ArrayList<>();
        GenreRepository genreRepository = new GenreRepository(genres);
        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);
        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(genreRepository, directSaleRepository);

        Genre genre = genreRepository.createAndAddGenre("Fiction");

        List<Item> result = controller.getListOfItemsOnDirectSaleOfAGivenGenre(genre);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetListOfItemsOnDirectSale_ValidGenre() {
        // Cria repositórios
        List<Genre> genres = new ArrayList<>();
        List<DirectSale> directSales = new ArrayList<>();
        GenreRepository genreRepository = new GenreRepository(genres);
        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);

        // Cria o controller com os mesmos repositórios
        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(genreRepository, directSaleRepository);

        // Cria o género
        Genre genre = genreRepository.createAndAddGenre("Fiction");

        // Cria um listing com item do género "Fiction"
        Listing listing = createListing();
//        LocalDateTime date = LocalDateTime.of(2024, 12, 1, 9, 0);

        // Adiciona o DirectSale ao repositório
//        directSaleRepository.createAndAddDirectSale(listing, date);
        directSaleRepository.createAndAddDirectSale(List.of(listing), createBuyer(), false);

        // Testa
        List<Item> result = controller.getListOfItemsOnDirectSaleOfAGivenGenre(genre);

        assertNotNull(result);
        assertEquals(1, result.size());
    }


    private static Listing createListing(){
        return new Listing(
                "Listing Title",
                List.of(createItem()),
                SaleType.DIRECT_SALE,
                new Price(new BigDecimal("19.99")),
                "Short Description",
                createSeller(),
                List.of(new Picture("pic"))
        );
    }

    private static Item createItem(){
        return new Item(
                Condition.LIKE_NEW,
                230.30,
                new Dimensions(123, 242, 124),
                createPublication()
        );
    }

    private static User createSeller(){
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

    private static User createBuyer(){
        return new User(
                "Buyer",
                new Contact(
                        "919999999",
                        "+351",
                        new Email("buyer", "example.com"),
                        new Address("Avenida da Republica", "25", "3470-302", createCountry())
                ),
                new Profile(
                        "Buyer_username",
                        createCountry(),
                        LocalDate.of(1993, 9, 19)
                ),
                UserRole.NORMAL_USER
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

    private static BibliographicalData createBibliographicalData(){
        return new BibliographicalData(
                "Harry Potter: Pedra Filosofal",
                List.of(createAuthor()),
                new Locale("pt", "PT"),
                LocalDate.of(2020, 1, 30),
                List.of(new Genre("Fiction")),
                "Text Description"
        );
    }

    private static PublicationType createPublicationType(){
        return new PublicationType(
                TypeOfBinding.HARDBACK,
                "Details"
        );
    }

    private static Code createCode(){
        return Code.fromIsbn10(
                "1928375849"
        );
    }

    private static Country createCountry() {
        return new Country("PORTUGAL");       // match your Countries enum value
    }

    private static Email createEmail() {
        return new Email("test", "test.com");
    }

    private static Address createAddress() {
        return new Address("Rua Test", "1", "4000-000", createCountry());
    }

    private static Contact createContact() {
        return new Contact("912345678", "123", createEmail(), createAddress());
    }

    private static Publisher createPublisher() {
        return new Publisher(
                "Publisher Name",  // name: String
                "123456789",       // vatNumber: String
                createContact()    // contact: Contact
        );
    }

    private static Edition createEdition() {
        return new Edition(
                "1st Edition",
                createCode(),
                createPhysicalAttributes(),
                createPublicationType(),
                createPublisher(),
                Locale.ENGLISH,
                LocalDate.now()
        );
    }

    private static PhysicalAttributes createPhysicalAttributes() {
        return new PhysicalAttributes(
                new Dimensions(10.0, 20.0, 5.0),  // dimensions
                1.5,                               // weight: double
                300                                // numberOfPages: int
        );
    }

    private static Publication createPublication() {
        return new Publication(
                createBibliographicalData(),
                createPublicationType(),
                createSeller(),
                PublicationStatus.FOR_SALE,
                createEdition(),
                createCode()
        );
    }

}