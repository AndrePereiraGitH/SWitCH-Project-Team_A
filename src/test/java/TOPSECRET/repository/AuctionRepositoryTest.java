package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class AuctionRepositoryTest {

    @Test
    void empty_constructor_should_create_empty_auctionList() {
        // arrange + act
        AuctionRepository auctionRepo = new AuctionRepository();

        List<Auction> auctionsList = auctionRepo.getAllAuctions();

        assertNotNull(auctionRepo);
        assertNotNull(auctionsList);
        assertEquals(0, auctionsList.size());
    }

    @Test
    void create_and_add_should_return_true_if_new_auction_is_added_to_list() throws InstanceAlreadyExistsException {

        // arrange
        AuctionRepository auctionRepository = new AuctionRepository();

        Listing listing = instantiateListing();
        LocalDateTime now = fixedNow();
        LocalDateTime initialDate = now;
        LocalDateTime finalDate = now.plusDays(2);

        // act
        boolean result = auctionRepository.createAndAdd(listing, initialDate, finalDate, new Price(new BigDecimal(150.50)));

        // assert
        assertTrue(result);
        assertEquals(1, auctionRepository.getAllAuctions().size());
    }

    @Test
    void create_and_add_should_return_FALSE_if_new_auction_already_exists_in_auctionsList() throws InstanceAlreadyExistsException {
        // arrange
        AuctionRepository auctionRepository = new AuctionRepository();

        Listing listing = instantiateListing();
        LocalDateTime now = fixedNow();
        LocalDateTime initialDate = now;
        LocalDateTime finalDate = now.plusDays(2);

        assertTrue(auctionRepository.createAndAdd(listing, initialDate, finalDate, price("150.50")));
        assertEquals(1, auctionRepository.getAllAuctions().size());

        assertFalse(auctionRepository.createAndAdd(listing, initialDate, finalDate, price("150.50")));
        assertEquals(1, auctionRepository.getAllAuctions().size());
    }

    @Test

    void getItemWhenAuctionHasThePublication(){

        //Arrange
        AuctionRepository auctionRepository = new AuctionRepository();

        Listing listing = instantiateListing();
        LocalDateTime now = fixedNow();
        LocalDateTime initialDate = now;
        LocalDateTime finalDate = now.plusDays(2);
        auctionRepository.createAndAdd(listing,initialDate,finalDate,price("150.50"));
        Publication publication= listing.getItems().get(0).getPublication();
        String expectedPublicationTitle= publication.getBibliographicalData().getTitle();


        // Act
        List<Item> listOfItemsOnAuction = auctionRepository.getListOfItemsOnAuctionByGivenPublication(publication);
        String realPublicationTitle = listOfItemsOnAuction.get(0).getPublication().getBibliographicalData().getTitle();

        // Assert

        assertEquals(1,listOfItemsOnAuction.size());
        assertEquals(expectedPublicationTitle,realPublicationTitle);


    }

    @Test
    void shouldReturnAnEmptyListWhenAuctionDoesNotHaveThePublication(){

        //Arrange
        AuctionRepository auctionRepository = new AuctionRepository();
        Listing listing = instantiateListing();
        LocalDateTime now = fixedNow();
        LocalDateTime initialDate = now;
        LocalDateTime finalDate = now.plusDays(2);
        auctionRepository.createAndAdd(listing,initialDate,finalDate,price("150.50"));



        Publication publication2 = createValidPublication2(instantiateBibliographicalData());

        //Act
        List<Item> listOfItemsOnAuction = auctionRepository.getListOfItemsOnAuctionByGivenPublication(publication2);


        // Assert

        assertEquals(0,listOfItemsOnAuction.size());
        assertTrue(listOfItemsOnAuction.isEmpty());


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


    // ============================================================
    // ============================ Helpers =======================
    // ============================================================

    private static Price price(String value) {
        return new Price(new BigDecimal(value));
    }

    private static Auction createValidActiveAuction() {
        Listing listing = instantiateListing();
        LocalDateTime now = fixedNow();

        LocalDateTime initialDate = now;
        LocalDateTime finalDate = now.plusDays(2);

        return new Auction(listing, initialDate, finalDate, price("150.00"), FIXED_CLOCK);
    }

    private static Listing instantiateListing() {
        Item bookItem = instantiateItem(instantiateBibliographicalData(), new Dimensions(12, 10, 2));
        Price price = new Price(new BigDecimal("35.00"));
        final List<Item> items = new ArrayList<>(List.of(bookItem));
        return new Listing(
                items,
                price,
                "short description",
                "This is our first listing",
                instantiateUser("Joseph"),
                instantiatePictures()
        );
    }

    private static BibliographicalData instantiateBibliographicalData() {
        Profile authorProfile = new Profile("J. R. R. Tolkien", new Country("UNITED_KINGDOM"), LocalDate.of(1892, 1, 3));
        Author author = new Author(authorProfile, "valid bio");

        List<Author> authors = new ArrayList<>();
        authors.add(author);


        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre ("Fantasy"));
        genres.add(new Genre ("Mister"));

        String chunkOfText =
                "Continuing the story begun in The Hobbit, all three parts of the epic masterpiece, " +
                        "The Lord of the Rings, in one paperback. Features the definitive edition of the text, " +
                        "fold-out flaps with the original two-colour maps, and a revised and expanded index.";

        Contact contact = new Contact();
        Publisher publisher = new Publisher("HarperCollins Publishers", "1111111111", contact);

        return new BibliographicalData(
                "The Lord Of The Rings",
                authors,
                Locale.ENGLISH,
                publisher,
                LocalDate.of(1991, 11, 1),
                List.of(new Genre ("Fantasy")),
                700,
                chunkOfText
        );
    }

    private static List<Picture> instantiatePictures() {
        Picture pic = new Picture("picture_url");
        List<Picture> pictures = new ArrayList<>();
        pictures.add(pic);
        return pictures;
    }

    private static Item instantiateItem(BibliographicalData bd, Dimensions dimensions) {
        Publication publication = createValidPublication(bd);

        return new Item(
                Condition.LIKE_NEW,
                1.2,
                dimensions,
                publication
        );
    }

    private static Publication createValidPublication(BibliographicalData bd) {
        // PublicationType
        PublicationType publicationType = new PublicationType(
                MediaFormat.BOOK,
                TypeOfBinding.PAPERBACK,
                "Paperback"
        );

        // User (seller/owner of the publication)
        User user = instantiateUser("maria1");

        // Code
        Code code = Code.fromIsbn13("9781234567890");

        return new Publication(
                bd,
                publicationType,
                user,
                PublicationStatus.FOR_SALE,
                "1st edition",
                code
        );
    }

    private static Publication createValidPublication2(BibliographicalData bd) {
        // PublicationType
        PublicationType publicationType = new PublicationType(
                MediaFormat.BOOK,
                TypeOfBinding.PAPERBACK,
                "Paperback"
        );

        // User (seller/owner of the publication)
        User user = instantiateUser("maria1");

        // Code
        Code code = Code.fromIsbn13("9781234567820");

        return new Publication(
                bd,
                publicationType,
                user,
                PublicationStatus.FOR_SALE,
                "1st edition",
                code
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