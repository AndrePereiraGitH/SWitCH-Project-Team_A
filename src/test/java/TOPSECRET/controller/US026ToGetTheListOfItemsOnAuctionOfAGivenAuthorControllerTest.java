package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.dto.AuctionDTO_US026;
import TOPSECRET.dto.AuthorDTO_US026;
import TOPSECRET.repository.AuctionRepository;
import TOPSECRET.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for {@link US026ToGetTheListOfItemsOnAuctionOfAGivenAuthorController}.
 *
 * <p>This test class verifies:
 * <ul>
 *   <li>controller instantiation</li>
 *   <li>retrieval of all authors</li>
 *   <li>retrieval of auction items for a given author</li>
 * </ul>
 * </p>
 */
class US026ToGetTheListOfItemsOnAuctionOfAGivenAuthorControllerTest {

    // Instantiate repos
    AuthorRepository authorRepo;
    AuctionRepository auctionRepo;
    US026ToGetTheListOfItemsOnAuctionOfAGivenAuthorController ctrl;
    AuthorFactory authorFactory = new AuthorFactory();

    @BeforeEach
    void setup() {
        AuctionFactory auctionFactory = new AuctionFactory();
        authorRepo = new AuthorRepository(new ArrayList<>(), new AuthorFactory());
        auctionRepo = new AuctionRepository(auctionFactory);
        ctrl = new US026ToGetTheListOfItemsOnAuctionOfAGivenAuthorController(authorRepo, auctionRepo);

    }

    /**
     * Verifies that the controller is correctly instantiated.
     */
    @Test
    void testConstructor() {
        assertNotNull(ctrl);
    }

    /**
     * Verifies that when no authors exist, the controller returns an empty list.
     */
    @Test
    void getAllAuthors_whenNoAuthors_shouldReturnEmptyList() {
        assertNotNull(ctrl.getAllAuthors());
        assertEquals(0, ctrl.getAllAuthors().size());

    }

    /**
     * Verifies that when authors exist, the controller returns a DTO list
     * with the same number of elements.
     */
    @Test
    void getAllAuthors_whenAuthorsExist_shouldReturnMapperDtoListWithSameSize() {

        // arrange
        Author author1 = authorRepo.createAndAddAuthor("Ken Follett", new Country("United_kingdom"), LocalDate.of(1949, 6, 5),"this is ken follett's biography");
        Author author2 = authorRepo.createAndAddAuthor("Dan Brown", new Country("United_kingdom"), LocalDate.of(1964, 6, 5),"this is Dan Brown's biography");

        // act
        var result = ctrl.getAllAuthors();

        // assert
        assertNotNull(result);
        assertEquals(2, result.size());

    }

    /**
     * Verifies that when the selected author has no auction items,
     * the controller returns an empty list.
     */
    @Test
    void  getListOfItemsOnAuctionByAuthor_whenNoAuctionsByAuthor_shouldReturn_EmptyList() {
        // arrange
        Author author1 = authorRepo.createAndAddAuthor("Ken Follett", new Country("United_kingdom"), LocalDate.of(1949, 6, 5),"this is ken follett's biography");

        AuthorDTO_US026 authorDto = ctrl.getAllAuthors().get(0);

        // act
        List<AuctionDTO_US026> result = ctrl.getListOfItemsOnAuctionByAuthor(authorDto);

        // assert
        assertNotNull(result);
        assertEquals(0, result.size());

    }

    /**
     * Verifies that when auction items exist for the selected author,
     * the controller returns a DTO list with the same number of auctions.
     */
    @Test
    void getListOfItemsOnAuctionByAuthor_whenAuctionsExist_shouldReturnDtoListWithSameSize() throws InstanceAlreadyExistsException {
        // arrange

        Author author1 = authorRepo.createAndAddAuthor("Ken Follett", new Country("United_kingdom"), LocalDate.of(1949, 6, 5),"this is ken follett's biography");
        List<Author> authors = new ArrayList<>();
        authors.add(author1);

        List<AuthorDTO_US026> allAuthorsDto = ctrl.getAllAuthors();
        assertNotNull(allAuthorsDto);
        assertFalse(allAuthorsDto.isEmpty());


        AuthorDTO_US026 authorDto = ctrl.getAllAuthors().get(0);

        User user = instantiateUser("Rodolph");
        PublicationSample item1 = instanciatePublication(authors, user);
        List<PublicationSample> items = new ArrayList<>();
        items.add(item1);

        PublicationSample item2 = instanciatePublication(authors, user);
        List<PublicationSample> items2 = new ArrayList<>();
        items2.add(item2);

        Listing listing = new Listing("Book Pillars of the earth",items, SaleType.AUCTION, price("35.50"), "short description", List.of(new Picture("pic1")));
        Listing listing2 = new Listing("Book Pillars of the earth", items2, SaleType.AUCTION, price("35.50"), "short description", List.of(new Picture("pic1")));


        LocalDateTime now = fixedNow();

        LocalDateTime initialDate = now.plusMinutes(5); // future => SCHEDULED
        LocalDateTime finalDate = now.plusDays(2);

        auctionRepo.createAndAdd(listing, initialDate, finalDate, price("150.00"));
        auctionRepo.createAndAdd(listing2, initialDate, finalDate, price("200.00"));

        // act
        List<AuctionDTO_US026> result = ctrl.getListOfItemsOnAuctionByAuthor(authorDto);

        assertNotNull(result);
        assertEquals(2, result.size());

    }





    // ============================================================
    // ========================= Test Clock =======================
    // ============================================================

    private static final ZoneId ZONE = ZoneId.of("UTC");
    private static final Instant FIXED_INSTANT = Instant.parse("2030-01-01T10:00:00Z");
    private static final Clock FIXED_CLOCK = Clock.fixed(FIXED_INSTANT, ZONE);

    private static LocalDateTime fixedNow() {
        return LocalDateTime.ofInstant(FIXED_INSTANT, ZONE);
    }


    private static Price price(String value) {
        return new Price(new BigDecimal(value));
    }


    private static Auction createValidScheduledAuction(Listing listing) {

        LocalDateTime now = fixedNow();

        LocalDateTime initialDate = now.plusMinutes(5); // future => SCHEDULED
        LocalDateTime finalDate = now.plusDays(2);

        return new Auction(listing, initialDate, finalDate, price("150.00"), FIXED_CLOCK);
    }

    private PublicationSample instanciatePublication( List<Author> authors, User user) {
        BibliographicalData bd = createBibliographicalData("Pillars Of the Earth", authors);
        // Edition
        Code code = Code.fromIsbn13 ("9793161484123");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);


        EditionFactory editionFactory = new EditionFactory();
        Edition edition= editionFactory.create("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

        return new PublicationSample(
                bd,
                user,
                PublicationStatus.READING,
//                "2nd",
                edition,
                Condition.LIKE_NEW
        );
    }


    private BibliographicalData createBibliographicalData(String title, List<Author> authors) {

        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre("novel"));

        return new BibliographicalData(
                title,
                authors,
                Locale.ENGLISH,
                LocalDate.of(1989, 3, 1),
                genres,
                "chunks chunks chunks"
        );

    }

    private static User instantiateUser(String name) {
        Email email = new Email("switcher", "example.com");
        Address add = new Address("Rua x", "12B", "4444-111", new Country("PORTUGAL"));
        Contact contact = new Contact("911111111", "351", email, add);
        LocalDate birthday = LocalDate.of(1982, 12, 3);
        Profile profile = new Profile("Albert Trump", new Country("EL_SALVADOR"), birthday);
        UserRole userRole = UserRole.NORMAL_USER;
        return new User(name, contact, profile,userRole);
    }

}