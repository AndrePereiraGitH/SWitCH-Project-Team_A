package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.dto.AuctionDTO_US026;
import TOPSECRET.dto.AuthorDTO_US026;
import TOPSECRET.repository.AuctionRepository;
import TOPSECRET.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class US026ToGetTheListOfItemsOnAuctionOfAGivenAuthorControllerTest {

    // Instantiate repos
    AuthorRepository authorRepo;
    AuctionRepository auctionRepo;
    US026ToGetTheListOfItemsOnAuctionOfAGivenAuthorController ctrl;

    @BeforeEach
    void setup() {
        authorRepo = new AuthorRepository(new ArrayList<>());
        auctionRepo = new AuctionRepository();
        ctrl = new US026ToGetTheListOfItemsOnAuctionOfAGivenAuthorController(authorRepo, auctionRepo);

    }

    @Test
    void testConstructor() {
        assertNotNull(ctrl);
    }

    @Test
    void getAllAuthors_whenNoAuthors_shouldReturnEmptyList() {
        assertNotNull(ctrl.getAllAuthors());
        assertEquals(0, ctrl.getAllAuthors().size());

    }

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

    @Test
    void getListOfItemsOnAuctionByAuthor_whenAuctionsExist_shouldReturnDtoListWithSameSize() {
        // arrange

        Author author1 = authorRepo.createAndAddAuthor("Ken Follett", new Country("United_kingdom"), LocalDate.of(1949, 6, 5),"this is ken follett's biography");
        List<Author> authors = new ArrayList<>();
        authors.add(author1);

        List<AuthorDTO_US026> allAuthorsDto = ctrl.getAllAuthors();
        assertNotNull(allAuthorsDto);
        assertFalse(allAuthorsDto.isEmpty());


        // AuthorDTO_US026 authorDto = ctrl.getAllAuthors().get(0);

        AuthorDTO_US026 authorDto =
                allAuthorsDto.stream()
                        .filter(a -> "Ken Follett".equals(a.name()))
                        .findFirst()
                        .orElseThrow(() -> new AssertionError("Não foi encontrado o DTO do autor criado no arrange"));


        User user = instantiateUser("Rodolph");
        Item item1 = instanciateItem(authors, user);
        List<Item> items = new ArrayList<>();
        items.add(item1);

        Item item2 = instanciateItem(authors, user);
        List<Item> items2 = new ArrayList<>();
        items2.add(item2);

        Listing listing = new Listing("Book Pillars of the earth",items, SaleType.AUCTION, price("35.50"), "short description", user, List.of(new Picture("pic1")));
        Listing listing2 = new Listing("Book Pillars of the earth", items2, SaleType.AUCTION, price("35.50"), "short description", user, List.of(new Picture("pic1")));


        LocalDateTime now = fixedNow();

        LocalDateTime initialDate = now.plusMinutes(5); // future => SCHEDULED
        LocalDateTime finalDate = now.plusDays(2);

        boolean auction = auctionRepo.createAndAdd(listing, initialDate, finalDate, price("150.00"));
        boolean auction2 = auctionRepo.createAndAdd(listing2, initialDate, finalDate, price("200.00"));

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

    private Item instanciateItem(List<Author> authors, User user) {
        return new Item(
                Condition.LIKE_NEW,
                2.3,
                new Dimensions(10,10,10),
                instanciatePublication(authors, user)
                );
    }

    private static Auction createValidScheduledAuction(Listing listing) {

        LocalDateTime now = fixedNow();

        LocalDateTime initialDate = now.plusMinutes(5); // future => SCHEDULED
        LocalDateTime finalDate = now.plusDays(2);

        return new Auction(listing, initialDate, finalDate, price("150.00"), FIXED_CLOCK);
    }

    private Publication instanciatePublication( List<Author> authors, User user) {
        PublicationType publicationType = new PublicationType(TypeOfBinding.HARDBACK, "some details" );
        BibliographicalData bd = createBibliographicalData("Pillars Of the Earth", authors);
        // Edition
        Code code = Code.fromIsbn13 ("978316148412");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
        Edition edition= new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

        return new Publication(
                bd,
                publicationType,
                user,
                PublicationStatus.FOR_SALE,
//                "2nd",
                edition,
                Code.fromIsbn10("fghdsjhklfgjhksgfjhksad")
        );
    }

    private BibliographicalData createBibliographicalData(String title, List<Author> authors) {

        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre("novel"));

        return new BibliographicalData(
                title,
                authors,
                Locale.ENGLISH,
//                new Publisher("Bertrand",
//                        "1111111111",
//                        new Contact(
//                                "9111111111",
//                                "321",
//                                new Email("example", "example.com"),
//                                new Address("street xxxx", "1234", "1223-212", new Country("united_kingdom"))
//                        )
//                ),
                LocalDate.of(1989, 3, 1),
                genres,
//                432,
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