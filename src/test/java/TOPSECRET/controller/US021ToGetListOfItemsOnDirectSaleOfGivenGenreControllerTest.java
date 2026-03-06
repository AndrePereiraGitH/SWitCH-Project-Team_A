package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.repository.GenreRepository;
import TOPSECRET.repository.ListingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for {@link US021ToGetListOfItemsOnDirectSaleOfGivenGenreController}.
 * Tests are performed without mocking, using real instances of the repositories.
 */
class US021ToGetListOfItemsOnDirectSaleOfGivenGenreControllerTest {

    private GenreRepository genreRepository;
    private ListingRepository listingRepository;
    private US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller;

    /**
     * Executed before each test to guarantee isolation between tests.
     */
    @BeforeEach
    void setUp() {
        genreRepository = new GenreRepository(new ArrayList<>(), new GenreFactory());
        listingRepository = new ListingRepository();
        controller = new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                genreRepository, listingRepository);
    }

    @Test
    void ensureControllerIsCreatedSuccessfully() {
        assertNotNull(controller);
    }

    @Test
    void ensureThrowsExceptionWhenGenreRepositoryIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(null, listingRepository));
    }

    @Test
    void ensureThrowsExceptionWhenListingRepositoryIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(genreRepository, null));
    }

    @Test
    void ensureThrowsExceptionWhenBothRepositoriesAreNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(null, null));
    }

    @Test
    void ensureGetAllGenresReturnsEmptyList() {
        List<Genre> result = controller.getAllGenres();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void ensureGetAllGenresReturnsCorrectList() {
        genreRepository.createAndAddGenre("Fiction");
        genreRepository.createAndAddGenre("Science");

        List<Genre> result = controller.getAllGenres();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void ensureThrowsExceptionWhenGenreIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                controller.getListOfItemsOnDirectSaleOfAGivenGenre(null));
    }

    @Test
    void ensureReturnsEmptyListWhenNoItemsMatchGenre() {
        Genre genre = genreRepository.createAndAddGenre("Fiction");

        List<PublicationSample> result =
                controller.getListOfItemsOnDirectSaleOfAGivenGenre(genre);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

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
                SaleType.AUCTION,
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

    // --- Helper methods ---

    private static Edition createEdition() {

        EditionFactory editionFactory = new EditionFactory();
        return editionFactory.create(
                "1st Edition",
                createCode(),
                createPhysicalAttributes(),
                createPublicationType(),
                createPublisher(),
                Locale.ENGLISH,
                LocalDate.of(2011, 1, 1)
        );
    }

    private static Code createCode() {
        return Code.fromIsbn13("9780747532743");
    }

    private static PhysicalAttributes createPhysicalAttributes() {
        return new PhysicalAttributes(
                new Dimensions(10.0, 20.0, 5.0),
                1.5,
                300
        );
    }

    private static Publisher createPublisher() {
        return new Publisher(
                "Publisher Name",
                "123456789",
                createContact()
        );
    }

    private static PublicationType createPublicationType() {
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

    private static BibliographicalData createBibliographicalData() {
        return new BibliographicalData(
                "Harry Potter: Pedra Filosofal",
                List.of(createAuthor()),
                new Locale("pt", "PT"),
                LocalDate.of(2020, 1, 30),
                List.of(new Genre("Fiction")),
                "Text Description"
        );
    }

    private static Author createAuthor() {
        AuthorFactory authorFactory = new AuthorFactory();
        return authorFactory.createWithExistingProfile(
                createProfile(),
                "valid bio");
    }

    private static Profile createProfile() {
        return new Profile(
                "Freida",
                createCountry(),
                LocalDate.of(1968, 10, 12)
        );
    }

    private static User createSeller() {
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
        return new Country("PORTUGAL");
    }

}