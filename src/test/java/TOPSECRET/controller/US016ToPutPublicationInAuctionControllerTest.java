package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.repository.AuctionRepository;
import TOPSECRET.repository.ItemRepository;
import TOPSECRET.repository.LibraryRepository;
import TOPSECRET.repository.ListingRepository;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class US016ToPutPublicationInAuctionControllerTest {

    // --------------------------------------
    // -----getAllPublicationsInUserLibrary ------
    // --------------------------------------

//    @Test
//    void getAllPublicationsInUserLibrary_shouldThrow_ifUsernameNull() {
//        var controller = newControllerWithLibraries(Map.of());
//
//        assertThrows(IllegalArgumentException.class,
//                () -> controller.getAllPublicationsInUserLibrary(null));
//    }
//
//    @Test
//    void getAllPublicationsInUserLibrary_shouldThrow_ifLibraryNotFound() {
//        var controller = newControllerWithLibraries(Map.of());
//
//        assertThrows(InstanceNotFoundException.class,
//                () -> controller.getAllPublicationsInUserLibrary("user1x"));
//    }
//
//    @Test
//    void getAllPublicationsInUserLibrary_shouldThrowEmptyStack_ifNoPublications() throws Exception {
//        // arrange
//        User user = user("Rodolph");
//        Library lib = new Library("Lib1" + user.getUserName(), user);
//
//        Map<String, Library> libs = new HashMap<>();
//        libs.put(user.getUserName(), lib);
//
//        var controller = newControllerWithLibraries(libs);
//
//        // act + assert
//        assertThrows(EmptyStackException.class,
//                () -> controller.getAllPublicationsInUserLibrary(user.getUserName()));
//    }
//
//    @Test
//    void getAllPublicationsInUserLibrary_shouldReturnPublications() throws Exception {
//        // arrange
//        User user = user("Rodolph");
//        Publication p1 = publication(user);
//        Publication p2 = uniquePublication(user);
//
//        Library lib1 = new Library("Lib1" + user.getUserName(), user);
//        addPublicationToLibrary(lib1, p1);
//        addPublicationToLibrary(lib1, p2);
//
//        Map<String, Library> libs = new HashMap<>();
//        libs.put(user.getUserName(), lib1);
//
//        var controller = newControllerWithLibraries(libs);
//
//        // act
//        List<Publication> pubs = controller.getAllPublicationsInUserLibrary(user.getUserName());
//
//        // assert
//        assertEquals(2, pubs.size());
//        assertTrue(pubs.contains(p1));
//        assertTrue(pubs.contains(p2));
//    }


    // -------------------------
    // putPublicationForAuction
    // -------------------------

//    @Test
//    void putPublicationForAuction_shouldThrow_ifPublicationNull() {
//        // arrange
//        var controller = newControllerWithLibraries(Map.of());
//
//        // act + assert
//        assertThrows(IllegalArgumentException.class, () ->
//                controller.putPublicationForAuction(
//                        null,
//                        price("35.50"),
//                        "short",
//                        "title",
//                        user("seller"),
//                        LocalDateTime.now(),
//                        LocalDateTime.now().plusDays(1),
//                        price("100.00")
//                )
//        );
//    }

//    @Test
//    void putPublicationForAuction_shouldThrow_ifInitialFinalOrBuyoutNull() {
//
//        // arrange
//        var controller = newControllerWithLibraries(Map.of());
//
//        Publication pub = publication(user("owner1"));
//        User seller = user("seller");
//
//        // act + assert
//        assertThrows(IllegalArgumentException.class, () ->
//                controller.putPublicationForAuction(pub, price("10.00"), "d", "title", seller,
//                        null, LocalDateTime.now().plusDays(1), price("50.00")));
//
//        assertThrows(IllegalArgumentException.class, () ->
//                controller.putPublicationForAuction(pub, price("10.00"), "d", "title", seller,
//                        LocalDateTime.now(), null, price("50.00")));
//
//        assertThrows(IllegalArgumentException.class, () ->
//                controller.putPublicationForAuction(pub, price("10.00"), "d", "title", seller,
//                        LocalDateTime.now(), LocalDateTime.now().plusDays(1), null));
//    }

//    @Test
//    void putPublicationForAuction_shouldCreateAuction_andReturnTrue() throws Exception {
//        // arrange
//        ItemRepository itemRepo = new ItemRepository();
//        ListingRepository listingRepo = new ListingRepository();
//        AuctionRepository auctionRepo = new AuctionRepository();
//        LibraryRepository libraryRepo = new LibraryRepository(Map.of());
//
//        var controller = new US016ToPutPublicationInAuctionController(libraryRepo, itemRepo, listingRepo, auctionRepo);
//
//        Publication pub = publication(user("owner1"));
//        User seller = user("seller");
//        Price listingPrice = price("35.50");
//        Price buyOutPrice = price("100.00");
//
//        LocalDateTime initial = LocalDateTime.of(2027, 1, 2, 10, 0);
//        LocalDateTime fin = LocalDateTime.of(2027, 1, 30, 10, 0);
//
//        int before = auctionRepo.getAllAuctions().size();
//
//        // act
//        boolean result = controller.putPublicationForAuction(
//                pub, listingPrice, "short description", "title", seller, initial, fin, buyOutPrice
//        );
//
//        // assert
//        assertTrue(result);
//        assertEquals(before + 1, auctionRepo.getAllAuctions().size());
//    }

//    @Test
//    void putPublicationForAuction_shouldThrowInstanceAlreadyExists_ifDuplicatedListing() throws Exception {
//        // arrange
//        ItemRepository itemRepo = new ItemRepository();
//        ListingRepository listingRepo = new ListingRepository();
//        AuctionRepository auctionRepo = new AuctionRepository();
//        LibraryRepository libraryRepo = new LibraryRepository(Map.of());
//
//        var controller = new US016ToPutPublicationInAuctionController(libraryRepo, itemRepo, listingRepo, auctionRepo);
//
//        Publication pub = publication(user("owner1"));
//        User seller = user("seller");
//        Price listingPrice = price("35.50");
//        Price buyOutPrice = price("100.00");
//
//        LocalDateTime initial = LocalDateTime.of(2026, 2, 23, 10, 0);
//        LocalDateTime fin = LocalDateTime.of(2026, 2, 28, 10, 0);
//
//        controller.putPublicationForAuction(pub, listingPrice, "short description", "title", seller, initial, fin, buyOutPrice);
//
//        // act + assert
//        assertThrows(InstanceAlreadyExistsException.class, () ->
//                controller.putPublicationForAuction(pub, listingPrice, "short description", "title", seller, initial, fin, buyOutPrice)
//        );
//    }





    // ============================================================
    // ========================== Helpers =========================
    // ============================================================

    private US016ToPutPublicationInAuctionController newControllerWithLibraries(Map<String, Library> libs) {
        LibraryRepository libraryRepo = new LibraryRepository(libs);
        ItemRepository itemRepo = new ItemRepository();
        ListingRepository listingRepo = new ListingRepository();
        AuctionRepository auctionRepo = new AuctionRepository();
        return new US016ToPutPublicationInAuctionController(libraryRepo, itemRepo, listingRepo, auctionRepo);
    }

    private Price price(String v) {
        return new Price(new BigDecimal(v));
    }

    private User user(String username) {
        Contact contact = contact();
        Profile profile = profile();
        return new User(username, contact, profile, UserRole.NORMAL_USER);
    }

    private Publication publication(User owner) {
        BibliographicalData bd = bibliographicalData();
        PublicationType type = publicationType();
        PublicationStatus status = PublicationStatus.READING;
        Code code = Code.fromIsbn10("123456789X");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
        Edition edition= new Edition(
                "1st",
                code,
                physicalAttributes,
                pubType,
                publisher,
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1)
        );

//        return new Publication(bd, type, owner, status, "1st", code);
        return new Publication(bd, type, owner, status, edition, code);
    }

    private Publication uniquePublication(User owner) {
        BibliographicalData bd = bibliographicalData();
        PublicationType type = publicationType();
        PublicationStatus status = PublicationStatus.FOR_SALE;
        Code code = Code.fromIsbn10("12345678" + (System.nanoTime() % 10) + "X");
    //    Code code = Code.fromIsbn10("978" + System.nanoTime());                 // generates always a new code --> new publication
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);

        Edition edition = new Edition(
                "2st",
                code,
                physicalAttributes,
                pubType,
                publisher,
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1)
        );

        return new Publication(bd, type, owner, status, edition, code);
    }

    private PublicationType publicationType() {
        return new PublicationType(TypeOfBinding.HARDBACK, "some details");
    }

//    private void addPublicationToLibrary(Library lib, Publication p) {
//        lib.addPublication(p);
//    }

    private Contact contact() {
        return new Contact("12343244",
                "321",
                new Email("example", "domain.pt"),
                new Address("Street", "12G", "1234-123",
                        new Country("United_States")
                ));
    }

    private Profile profile() {
        return new Profile("Rodolph", new Country("United_kingdom"), LocalDate.of(1967, 12,3));
    }

    private BibliographicalData bibliographicalData() {

        Author author = new Author(profile(), "valid bio");

        List<Author> authors = new ArrayList<>();
        authors.add(author);
        String chunkOfText =
                "Continuing the story begun in The Hobbit, all three parts of the epic masterpiece, " +
                        "The Lord of the Rings, in one paperback. Features the definitive edition of the text, " +
                        "fold-out flaps with the original two-colour maps, and a revised and expanded index.";

        return new BibliographicalData(
                "The Lord Of The Rings",
                authors,
                Locale.ENGLISH,
//                new Publisher("HarperCollins Publishers", "1111111111", contact()),
                LocalDate.of(1991, 11, 1),
                List.of(new Genre ("Fantasy")),
//                700,
                chunkOfText
        ); }


}