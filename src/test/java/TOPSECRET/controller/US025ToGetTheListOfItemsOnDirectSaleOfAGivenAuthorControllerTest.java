//package TOPSECRET.controller;
//
//import TOPSECRET.domain.*;
//import TOPSECRET.domain.Enum.*;
//import TOPSECRET.dto.AuthorDTO_US025;
//import TOPSECRET.repository.AuthorRepository;
//import TOPSECRET.repository.ListingRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import javax.management.InstanceAlreadyExistsException;
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Unit tests for {@link US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController}.
// *
// * This test class verifies:
// * - Retrieval of all authors as DTOs.
// * - Retrieval of direct sale PublicationSamples for an author with sales.
// * - Correct behavior when the author has no direct sales.
// * - Proper exception handling when null is provided.
// */
//class US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorControllerTest {
//
//    private US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController controller;
//    private AuthorRepository authorRepository;
//    private ListingRepository listingRepository;
//
//    private Author author1;
//    private Author author2;
//
//    @BeforeEach
//    void setUp() throws InstanceAlreadyExistsException {
//
//        // ---------- Author Repository ----------
//        authorRepository = new AuthorRepository(new ArrayList<>(), new AuthorFactory());
//
//        author1 = authorRepository.createAndAddAuthor(
//                "Author One",
//                new Country("PORTUGAL"),
//                LocalDate.of(1970, 1, 1),
//                "Biography One"
//        );
//
//        author2 = authorRepository.createAndAddAuthor(
//                "Author Two",
//                new Country("PORTUGAL"),
//                LocalDate.of(1980, 1, 1),
//                "Biography Two"
//        );
//
//        // ---------- Listing Repository ----------
//        listingRepository = new ListingRepository();
//
//        // ---------- PublicationSample 1 ----------
//        BibliographicalData bibliographicalData1 = new BibliographicalData(
//                "Book 1",
//                List.of(author1),
//                Locale.ENGLISH,
//                LocalDate.of(1983, 7, 12),
//                List.of(new Genre("genre1")),
//                "text1"
//        );
//
//        PublicationSample sample1 = new PublicationSample(
//                bibliographicalData1,
//                new User("user1",
//                        new Contact("123", "456",
//                                new Email("user1", "gmail.com"),
//                                new Address("street1","city1","zip1", new Country("Portugal"))
//                        ),
//                        new Profile("profile1", new Country("Portugal"), LocalDate.of(1983,3,5)),
//                        UserRole.NORMAL_USER
//                ),
//                PublicationStatus.READ,
//                new Edition("ed1",
//                        new Code("1","1","1"),
//                        new PhysicalAttributes(new Dimensions(1,1,1), 1, 100),
//                        new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "details1"),
//                        new Publisher("pub1","123",
//                                new Contact("123","456",
//                                        new Email("pub1","gmail.com"),
//                                        new Address("street2","city2","zip2", new Country("Portugal"))
//                                )
//                        ),
//                        Locale.ENGLISH,
//                        LocalDate.of(1965,4,3)
//                ),
//                Condition.NEW
//        );
//
//        // ---------- PublicationSample 2 ----------
//        BibliographicalData bibliographicalData2 = new BibliographicalData(
//                "Book 2",
//                List.of(author1),
//                Locale.ENGLISH,
//                LocalDate.of(1990, 5, 10),
//                List.of(new Genre("genre2")),
//                "text2"
//        );
//
//        PublicationSample sample2 = new PublicationSample(
//                bibliographicalData2,
//                new User("user2",
//                        new Contact("789", "101",
//                                new Email("user2", "gmail.com"),
//                                new Address("street3","city3","zip3", new Country("Portugal"))
//                        ),
//                        new Profile("profile2", new Country("Portugal"), LocalDate.of(1985,6,5)),
//                        UserRole.NORMAL_USER
//                ),
//                PublicationStatus.READ,
//                new Edition("ed2",
//                        new Code("2","2","2"),
//                        new PhysicalAttributes(new Dimensions(2,2,2), 2, 200),
//                        new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "details2"),
//                        new Publisher("pub2","456",
//                                new Contact("789","101",
//                                        new Email("pub2","gmail.com"),
//                                        new Address("street4","city4","zip4", new Country("Portugal"))
//                                )
//                        ),
//                        Locale.ENGLISH,
//                        LocalDate.of(2000,7,3)
//                ),
//                Condition.NEW
//        );
//
//        // ---------- Create DIRECT_SALE Listing ----------
//        listingRepository.createAndAdd(
//                "Listing 1",
//                List.of(sample1, sample2),
//                SaleType.DIRECT_SALE,
//                new Price(BigDecimal.valueOf(10)),
//                "Short description",
//                new ArrayList<>()
//        );
//
//        // ---------- Initialize Controller ----------
//        controller = new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
//                authorRepository,
//                listingRepository
//        );
//    }
//
//    @Test
//    void shouldReturnAuthorsDTOList() {
//        List<AuthorDTO_US025> authors = controller.getAllAuthors();
//
//        assertNotNull(authors);
//        assertEquals(2, authors.size());
//    }
//
//    @Test
//    void shouldReturnPublicationSamplesForAuthorWithDirectSales() {
//        List<PublicationSample> result =
//                controller.getListOfItemsOnDirectSaleOfAGivenAuthor(author1);
//
//        assertNotNull(result);
//        assertEquals(2, result.size()); // author1 tem 2 samples no listing
//    }
//
//    @Test
//    void shouldReturnEmptyListForAuthorWithoutDirectSales() {
//        List<PublicationSample> result =
//                controller.getListOfItemsOnDirectSaleOfAGivenAuthor(author2);
//
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenAuthorIsNull() {
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> controller.getListOfItemsOnDirectSaleOfAGivenAuthor(null)
//        );
//    }
//
//    @Test
//    void constructorShouldThrowExceptionWhenAuthorRepositoryIsNull() {
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
//                        null,
//                        listingRepository
//                )
//        );
//    }
//
//    @Test
//    void constructorShouldThrowExceptionWhenListingRepositoryIsNull() {
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
//                        authorRepository,
//                        null
//                )
//        );
//    }
//}