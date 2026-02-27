package TOPSECRET.dto;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class AuctionDTO_US026Test {

    private AuthorRepository authorRepo;
    AuthorFactory authorFactory = new AuthorFactory();
    EditionFactory editionFactory = new EditionFactory();


   @BeforeEach
    void setup() {
        authorRepo = new AuthorRepository(new ArrayList<>(), new AuthorFactory());
    }

    // ============================================================
    // ========================= Test Clock =======================
    // ============================================================

    private static final ZoneId ZONE = ZoneId.of("UTC");
    private static final Instant FIXED_INSTANT = Instant.parse("2030-01-01T10:00:00Z");
    private static final Clock FIXED_CLOCK = Clock.fixed(FIXED_INSTANT, ZONE);

    // Pictures
    List<Picture> pictures = List.of(new Picture("path/to/pic.jpg"));

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
        AuctionDTO_US026 dto = AuctionDTO_US026.fromAuction(auction);

        // assert
        assertNotNull(dto);

        assertSame(listing, dto.listing(), "Mappper should provide to DTO the same Listing instance");
    }

    @Test
    void fromAuction_whenAuctionIsNull_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> AuctionDTO_US026.fromAuction(null));
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

        PublicationSample publication = instantiatePublication(List.of(author), user);


        return new Listing(
                "Book Pillars of the earth",
                List.of(publication),
                SaleType.AUCTION,
                price("35.50"),
                "short description",
                pictures
        );
    }

    private Author instantiateAuthor(String name) {
        return authorRepo.createAndAddAuthor(name, new Country("United_kingdom"), LocalDate.of(1949, 6, 5),"this is ken follett's biography", authorFactory);
    }

    private PublicationSample instantiatePublication(List<Author> authors, User user) {
        PublicationType publicationType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "some details");
        BibliographicalData bd = createBibliographicalData("Pillars Of the Earth", authors);

        // Edition
        Code code = Code.fromIsbn13 ("9999999999999");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
        Edition edition= editionFactory.create("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

        return new PublicationSample(
                bd,
                user,
                PublicationStatus.READING,
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