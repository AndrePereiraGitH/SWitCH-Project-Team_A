package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.repository.GenreRepository;
import TOPSECRET.repository.ListingRepository;
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
class US021ToGetListOfItemsOnDirectSaleOfGivenGenreControllerDependenciesTest {

    private GenreRepository genreRepository;
    private ListingRepository listingRepository;
    private US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller;



    @Test
    void ensureControllerIsCreatedSuccessfully() {
        //Arrange
        genreRepository = new GenreRepository( new GenreFactory());
        listingRepository = new ListingRepository();

        controller = new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                genreRepository, listingRepository);

        //Assert
        assertNotNull(controller);
    }

    @Test
    void ensureThrowsExceptionWhenGenreRepositoryIsNull() {
        //Arrange
        listingRepository = new ListingRepository();

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(null, listingRepository));
    }

    @Test
    void ensureThrowsExceptionWhenListingRepositoryIsNull() {
        //Arrange
        genreRepository = new GenreRepository( new GenreFactory());

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(genreRepository, null));
    }

    @Test
    void ensureThrowsExceptionWhenBothRepositoriesAreNull() {
        //Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(null, null));
    }

    @Test
    void ensureGetAllGenresReturnsEmptyList() {
        //Arrange
        genreRepository = new GenreRepository( new GenreFactory());
        listingRepository = new ListingRepository();

        controller = new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                genreRepository, listingRepository);

        //Act
        List<Genre> result = controller.getAllGenres();

        //Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void ensureGetAllGenresReturnsCorrectList() {
        //Arrange
        genreRepository = new GenreRepository( new GenreFactory());
        listingRepository = new ListingRepository();

        controller = new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                genreRepository, listingRepository);

        genreRepository.createAndAdd("Fiction");
        genreRepository.createAndAdd("Science");

        //Act
        List<Genre> result = controller.getAllGenres();

        //Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void ensureThrowsExceptionWhenGenreIsNull() {
        //Arrange
        genreRepository = new GenreRepository( new GenreFactory());
        listingRepository = new ListingRepository();

        controller = new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                genreRepository, listingRepository);

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.getListOfItemsOnDirectSaleOfAGivenGenre(null));
    }

    @Test
    void ensureReturnsEmptyListWhenNoItemsMatchGenre() {
        //Arrange
        genreRepository = new GenreRepository( new GenreFactory());
        listingRepository = new ListingRepository();
        controller = new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                genreRepository, listingRepository);

        Genre genre = genreRepository.createAndAdd("Fiction");

        //Act
        List<PublicationSample> result =
                controller.getListOfItemsOnDirectSaleOfAGivenGenre(genre);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    /**
     * Verifies that the controller returns a list containing one item
     * when there is exactly one listing of the given genre on direct sale.
     *
     * A listing with a "Fiction" sample is created and added to the repository.
     * The controller is then queried for items of the "Fiction" genre,
     * and the result should contain exactly one item.
     */
    @Test
    void ensureReturnsListWithOneItemWhenOneMatchesGenre() throws Exception {
        // Arrange
        genreRepository = new GenreRepository( new GenreFactory());
        listingRepository = new ListingRepository();
        controller = new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                genreRepository, listingRepository);

        Genre genre = genreRepository.createAndAdd("Fiction");


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
     * Verifies that the controller returns an empty list when no listings
     * match the queried genre.
     *
     * A listing with a "Fiction" sample is created, but the controller
     * is queried for "Science" genre items. Since no listings belong
     * to "Science", the result should be empty.
     */
    @Test
    void ensureReturnsEmptyListWhenNoListingsMatchGenre() throws Exception {
        // Arrange
        genreRepository = new GenreRepository( new GenreFactory());
        listingRepository = new ListingRepository();
        controller = new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                genreRepository, listingRepository);

        genreRepository.createAndAdd("Fiction");
        Genre scienceGenre = genreRepository.createAndAdd("Science");


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

        // Act
        List<PublicationSample> result =
                controller.getListOfItemsOnDirectSaleOfAGivenGenre(scienceGenre);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    /**
     * Verifies that the controller returns an empty list when the only
     * existing listing is not of type DIRECT_SALE.
     *
     * A listing with a "Fiction" sample is created as an AUCTION.
     * When querying for direct sale items of the "Fiction" genre,
     * the result should be empty since the listing type does not match.
     */
    @Test
    void ensureReturnsEmptyListWhenListingIsNotDirectSale() throws Exception {
        // Arrange
        genreRepository = new GenreRepository(new GenreFactory());
        listingRepository = new ListingRepository();
        controller = new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                genreRepository, listingRepository);


        Genre genre = genreRepository.createAndAdd("Fiction");


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
        PublisherFactory publisherFactory = new PublisherFactory();
        return publisherFactory.create(
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
        return new Country();
    }

}