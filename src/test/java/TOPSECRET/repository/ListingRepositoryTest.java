package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ListingRepositoryTest {

    ListingRepository listingRepo = new ListingRepository();
    ItemRepository itemRepo = new ItemRepository();

    @Test
    void testConstructor_shouldCreateEmptyList() {
        assertNotNull(listingRepo);
        assertEquals(0, listingRepo.listings.size());
    }

//    @Test
//    void createAndAdd_shouldReturn_validListing() throws InstanceAlreadyExistsException {
//
//        // arrange
//        List<Item> items = new ArrayList<>();
//        Item item = itemRepo.createAndAddItem(Condition.LIKE_NEW, 2, new Dimensions(10, 10, 10), instaciatePublication());
//        items.add(item);
//
//        // act
//        Listing list = listingRepo.createAndAdd("Title Auction",items, SaleType.DIRECT_SALE, price("35.50"), "This is the listing description", instantiateUser("ImSeller"),List.of(new Picture("pic1")));
//
//        // assert
//        assertNotNull(list);
//        assertEquals(1, listingRepo.listings.size());
//
//    }

//    @Test
//    void shouldThrow_ifDuplicatedListing() throws InstanceAlreadyExistsException {
//
//        // arrange
//        List<Item> items = new ArrayList<>();
//        Item item = itemRepo.createAndAddItem(Condition.LIKE_NEW, 2, new Dimensions(10, 10, 10), instaciatePublication());
//        items.add(item);
//        User owner = instantiateUser("ImSeller");
//        listingRepo.createAndAdd("Title Auction", items, SaleType.DIRECT_SALE, price("35.50"), "This is the listing description", owner, List.of(new Picture("pic1")));
//
//
//        // act + assert
//        InstanceAlreadyExistsException e = assertThrows(InstanceAlreadyExistsException.class, () -> listingRepo.createAndAdd("Title Auction", items, SaleType.DIRECT_SALE, price("35.50"), "This is the listing description", owner, List.of(new Picture("pic1"))));
//
//        assertEquals("Listing already exists.", e.getMessage());
//
//    }

    // Edition
    Code code = Code.fromIsbn13 ("9783161484123");
    Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
    PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
    PublicationType pubType = new PublicationType(TypeOfBinding.CASE_BINDING, "Some details");
    Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
    Edition edition= new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

    private Publication instaciatePublication() {
        PublicationType pubType = new PublicationType(TypeOfBinding.PAPERBACK, "some details");
//        Publication pub = new Publication(instantiateBibliographicalData(), pubType, instantiateUser("Owner"), PublicationStatus.FOR_SALE, "2Edition", Code.fromIsbn13("ghjgajhsggkdagdk"));
        Publication pub = new Publication(instantiateBibliographicalData(), pubType, instantiateUser("Owner"), PublicationStatus.FOR_SALE, edition, Code.fromIsbn13("9781234567897"));

        return pub;
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

//        Contact contact = new Contact();
//        Publisher publisher = new Publisher("HarperCollins Publishers", "1111111111", contact);

        return new BibliographicalData(
                "The Lord Of The Rings",
                authors,
                Locale.ENGLISH,
//                publisher,
                LocalDate.of(1991, 11, 1),
                List.of(new Genre ("Fantasy")),
//                700,
                chunkOfText
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

    private static Price price(String value) {
        return new Price(new BigDecimal(value));
    }

}