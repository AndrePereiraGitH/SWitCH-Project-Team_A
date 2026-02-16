package TOPSECRET.dto;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
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

class AuctionMapper_US026Test {

    private AuthorRepository authorRepo;


   @BeforeEach
    void setup() {
        authorRepo = new AuthorRepository(new ArrayList<>());
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

    @Test
    void fromAuction_whenValidAuction_shouldMapListingReference() {
        // arrange
        Listing listing = createListingWithOneItemForAuthorName("Ken Follett");
        Auction auction = createValidScheduledAuction(listing);

        // act
        AuctionDTO_US026 dto = AuctionMapper_US026.fromAuction(auction);

        // assert
        assertNotNull(dto);

        assertSame(listing, dto.listing(), "Mappper should provide to DTO the same Listing instance");
    }

    @Test
    void fromAuction_whenAuctionIsNull_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> AuctionMapper_US026.fromAuction(null));
    }

    // ============================================================
    // ===================== Helpers (p/ criar domínio) ============
    // ============================================================

    private Auction createValidScheduledAuction(Listing listing) {
        LocalDateTime now = fixedNow();
        LocalDateTime initialDate = now.plusMinutes(5);
        LocalDateTime finalDate = now.plusDays(2);
        return new Auction(listing, initialDate, finalDate, price("150.00"), FIXED_CLOCK);
    }

    private Listing createListingWithOneItemForAuthorName(String authorName) {
        User user = instantiateUser("Rodolph");

        Author author = instantiateAuthor(authorName);

        Publication publication = instantiatePublication(List.of(author), user);
        Item item = new Item(
                Condition.LIKE_NEW,
                2.3,
                new Dimensions(10, 10, 10),
                publication
        );

        return new Listing(
                List.of(item),
                price("35.50"),
                "short description",
                "Book Pillars of the earth",
                user
        );
    }

    private Author instantiateAuthor(String name) {
        return authorRepo.createAndAddAuthor(name, new Country("United_kingdom"), LocalDate.of(1949, 6, 5),"this is ken follett's biography");
    }

    private Publication instantiatePublication(List<Author> authors, User user) {
        PublicationType publicationType = new PublicationType(TypeOfBinding.HARDBACK, "some details");
        BibliographicalData bd = createBibliographicalData("Pillars Of the Earth", authors);

        return new Publication(
                bd,
                publicationType,
                user,
                PublicationStatus.FOR_SALE,
                "2nd",
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
                new Publisher("Bertrand",
                        "1111111111",
                        new Contact(
                                "9111111111",
                                "321",
                                new Email("example", "example.com"),
                                new Address("street xxxx", "1234", "1223-212", new Country("united_kingdom"))
                        )
                ),
                LocalDate.of(1989, 3, 1),
                genres,
                432,
                "chunks chunks chunks"
        );
    }

    private User instantiateUser(String name) {
        Email email = new Email("switcher", "example.com");
        Address add = new Address("Rua x", "12B", "4444-111", new Country("PORTUGAL"));
        Contact contact = new Contact("911111111", "351", email, add);
        LocalDate birthday = LocalDate.of(1982, 12, 3);
        Profile profile = new Profile("Albert Trump", new Country("EL_SALVADOR"), birthday);
        UserRole userRole = UserRole.NORMAL_USER;
        return new User(name, contact, profile, userRole);
    }



}