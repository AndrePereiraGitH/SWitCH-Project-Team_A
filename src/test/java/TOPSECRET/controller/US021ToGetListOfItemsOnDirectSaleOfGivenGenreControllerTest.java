package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.repository.DirectSaleRepository;
import TOPSECRET.repository.GenreRepository;
import TOPSECRET.repository.ListingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.*;

class US021ToGetListOfItemsOnDirectSaleOfGivenGenreControllerTest {

    private GenreRepository genreRepository;
    private ListingRepository listingRepository;
    private US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller;


    /**
     * Executed before each test to guarantee isolation between tests.
     * Initialises fresh instances of the repositories and controller.
     */
    //It is executed before each test, guarantees isolation between tests
    @BeforeEach
    void setUp() {
        genreRepository = new GenreRepository(new ArrayList<>());
        listingRepository = new ListingRepository();
        controller = new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                genreRepository, listingRepository);
    }

    // Constructor tests

    /**
     * Ensures the controller is successfully created with valid repositories.
     */
    @Test
    void ensureControllerIsCreatedSuccessfully() {
        assertNotNull(controller);
    }


    /**
     * Ensures an {@link IllegalArgumentException} is thrown when the GenreRepository is null.
     */
    @Test
    void ensureThrowsExceptionWhenGenreRepositoryIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(null, listingRepository));

        assertEquals("GenreRepository and listingRepository cannot be null", exception.getMessage());
    }


    /**
     * Ensures an {@link IllegalArgumentException} is thrown when the ListingRepository is null.
     */
    @Test
    void ensureThrowsExceptionWhenListingRepositoryIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(genreRepository, null));

        assertEquals("GenreRepository and listingRepository cannot be null", exception.getMessage());
    }


    /**
     * Ensures an {@link IllegalArgumentException} is thrown when both repositories are null.
     */
    @Test
    void ensureThrowsExceptionWhenBothRepositoriesAreNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(null, null));

        assertEquals("GenreRepository and listingRepository cannot be null", exception.getMessage());
    }

    //getAllGenres tests


    /**
     * Ensures {@code getAllGenres()} returns an empty list when no genres exist in the repository.
     */
    @Test
    void ensureGetAllGenresReturnsEmptyList() {
        // Act
        List<Genre> result = controller.getAllGenres();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    /**
     * Ensures {@code getAllGenres()} returns the correct list of genres when genres exist.
     */
    @Test
    void ensureGetAllGenresReturnsCorrectList() {
        // Arrange
        genreRepository.createAndAddGenre("Fiction");
        genreRepository.createAndAddGenre("Science");

        // Act
        List<Genre> result = controller.getAllGenres();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }


    // getListOfItemsOnDirectSaleOfAGivenGenre tests

    /**
     * Ensures an {@link IllegalArgumentException} is thrown when the selected genre is null.
     */
    @Test
    void ensureThrowsExceptionWhenGenreIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                controller.getListOfItemsOnDirectSaleOfAGivenGenre(null));

        assertEquals("Genre cannot be null", exception.getMessage());
    }


    /**
     * Ensures {@code getListOfItemsOnDirectSaleOfAGivenGenre()} returns an empty list
     * when no items on direct sale match the given genre.
     */
    @Test
    void ensureReturnsEmptyListWhenNoItemsMatchGenre() {
        // Arrange
        Genre genre = genreRepository.createAndAddGenre("Fiction");

        // Act
        List<PublicationSample> result =
                controller.getListOfItemsOnDirectSaleOfAGivenGenre(genre);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    /**
     * Ensures {@code getListOfItemsOnDirectSaleOfAGivenGenre()} returns the correct list
     * when there is one item on direct sale matching the given genre.
     */
    @Test
    void ensureReturnsListWithOneItemWhenOneMatchesGenre() throws Exception {
        // Arrange
        Genre genre = genreRepository.createAndAddGenre("Fiction");

        PublicationSample sample = new PublicationSample(
                createBibliographicalData(),
                createSeller(),
                PublicationStatus.READ,
                createEdition(),
                Condition.LIKE_NEW
        );

        listingRepository.createAndAdd(
                "Valid Title",
                List.of(sample),
                SaleType.DIRECT_SALE,
                new Price(new BigDecimal("19.99")),
                "Short Description Here",
                List.of(new Picture("pic"))
        );

        // Act
        List<PublicationSample> result =
                controller.getListOfItemsOnDirectSaleOfAGivenGenre(genre);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

    }

    /**
     * Ensures {@code getListOfItemsOnDirectSaleOfAGivenGenre()} returns an empty list
     * when listings exist but none match the given genre.
     */
    @Test
    void ensureReturnsEmptyListWhenNoListingsMatchGenre() throws Exception {
        // Arrange
        Genre fictionGenre = genreRepository.createAndAddGenre("Fiction");
        genreRepository.createAndAddGenre("Science");

        PublicationSample sample = new PublicationSample(
                createBibliographicalData(), // uses "Fiction"
                createSeller(),
                PublicationStatus.READ,
                createEdition(),
                Condition.LIKE_NEW
        );

        listingRepository.createAndAdd(
                "Valid Title",
                List.of(sample),
                SaleType.DIRECT_SALE,
                new Price(new BigDecimal("19.99")),
                "Short Description Here",
                List.of(new Picture("pic"))
        );

        Genre scienceGenre = new Genre("Science");

        // Act
        List<PublicationSample> result =
                controller.getListOfItemsOnDirectSaleOfAGivenGenre(scienceGenre);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Ensures {@code getListOfItemsOnDirectSaleOfAGivenGenre()} returns an empty list
     * when listings exist but are not of type DIRECT_SALE.
     */
    @Test
    void ensureReturnsEmptyListWhenListingIsNotDirectSale() throws Exception {
        // Arrange
        Genre genre = genreRepository.createAndAddGenre("Fiction");

        PublicationSample sample = new PublicationSample(
                createBibliographicalData(),
                createSeller(),
                PublicationStatus.READ,
                createEdition(),
                Condition.LIKE_NEW
        );

        listingRepository.createAndAdd(
                "Valid Title",
                List.of(sample),
                SaleType.AUCTION,       // Not a direct sale!
                new Price(new BigDecimal("19.99")),
                "Short Description Here",
                List.of(new Picture("pic"))
        );

        // Act
        List<PublicationSample> result =
                controller.getListOfItemsOnDirectSaleOfAGivenGenre(genre);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    private static Edition createEdition() {
        return new Edition(
                "1st Edition",
                createCode(),
                createPhysicalAttributes(),
                createPublicationType(),
                createPublisher(),
                Locale.ENGLISH,
                LocalDate.of(2011,1,1)
        );
    }

    private static Code createCode() {
        return Code.fromIsbn13("9780747532743");
    }


    private static PhysicalAttributes createPhysicalAttributes() {
        return new PhysicalAttributes(
                new Dimensions(10.0, 20.0, 5.0),  // dimensions
                1.5,                               // weight: double
                300                                // numberOfPages: int
        );
    }

    private static Publisher createPublisher() {
        return new Publisher(
                "Publisher Name",  // name: String
                "123456789",       // vatNumber: String
                createContact()    // contact: Contact
        );
    }

    private static PublicationType createPublicationType(){
        return new PublicationType(
                MediaFormat.BOOK,
                TypeOfBinding.HARDBACK,
                "Details"
        );
    }

    private static Contact createContact() {
        return new Contact("912345678", "123", createEmail(), createAddress());
    }

    private static Email createEmail() {
        return new Email("test", "test.com");
    }

    private static Address createAddress() {
        return new Address("Rua Test", "1", "4000-000", createCountry());
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

    private static Author createAuthor(){
        return new Author(
                createProfile(),
                "valid bio"
        );
    }

    private static Profile createProfile(){
        return new Profile(
                "Freida",
                createCountry(),
                LocalDate.of(1968, 10, 12)
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

    private static Country createCountry() {
        return new Country("PORTUGAL");       // match your Countries enum value
    }

}