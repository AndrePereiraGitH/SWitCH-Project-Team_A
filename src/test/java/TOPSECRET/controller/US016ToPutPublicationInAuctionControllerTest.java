package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.dto.PublicationSampleDTO_US016;
import TOPSECRET.repository.*;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link US016ToPutPublicationInAuctionController}.
 * <p>
 * This test validates:
 * <ul>
 *   <li>Retrieval of publications from a user's library</li>
 *   <li>Conversion of publications to DTOs</li>
 *   <li>Validation rules when creating auction listings</li>
 *   <li>Successful auction creation and duplicate listing behavior</li>
 * </ul>
 */
class US016ToPutPublicationInAuctionControllerTest {

    private final LibraryRepository libRepo = new LibraryRepository(new HashMap<>());


    // --------------------------------------
    // -----getAllPublicationsInUserLibrary ------
    // --------------------------------------

    @Test
    void getAllPublicationsInUserLibrary_shouldThrow_ifUsernameNull() {
        US016ToPutPublicationInAuctionController controller = newControllerWithLibraries(Map.of());

        assertThrows(IllegalArgumentException.class,
                () -> controller.getUserLibraryPublications(null));
    }

    @Test
    void getAllPublicationsInUserLibrary_shouldThrow_ifLibraryNotFound() {
        US016ToPutPublicationInAuctionController controller = newControllerWithLibraries(Map.of());

        assertThrows(InstanceNotFoundException.class,
                () -> controller.getUserLibraryPublications("user1x"));
    }

    @Test
    void getAllPublicationsInUserLibrary_shouldThrowEmptyStack_ifNoPublications() throws Exception {
        // arrange
        User user = user("Rodolph");
        Library lib = new Library("Lib1" + user.getUserName(), user);

        Map<String, Library> libs = new HashMap<>();
        libs.put(user.getUserName(), lib);

        US016ToPutPublicationInAuctionController controller = newControllerWithLibraries(libs);

        // act + assert
        assertThrows(EmptyStackException.class,
                () -> controller.getUserLibraryPublications(user.getUserName()));
    }

    @Test
    void getAllPublicationsInUserLibrary_shouldReturnAListOfSameSize() throws Exception {
        // arrange

        User user = user("Rodolph");
        PublicationSample p1 = publication(user);
        PublicationSample p2 = uniquePublication(user);

        Library lib1 = new Library("Lib1" + user.getUserName(), user);
        addPublicationToLibrary(lib1, p1);
        addPublicationToLibrary(lib1, p2);

        Map<String, Library> libs = new HashMap<>();
        libs.put(user.getUserName(), lib1);

        US016ToPutPublicationInAuctionController controller = newControllerWithLibraries(libs);

        // act
        List<PublicationSampleDTO_US016> result = controller.getUserLibraryPublications(user.getUserName());

        // assert
        assertEquals(2, result.size());
    }

    @Test
    void getAllPublicationsInUserLibrary_shouldReturn_listOfPublicationSampleDTO() throws InstanceNotFoundException {
        User user = user("Rodolph");
        PublicationSample p1 = publication(user);
        PublicationSample p2 = uniquePublication(user);

        Library lib1 = new Library("Lib1" + user.getUserName(), user);
        addPublicationToLibrary(lib1, p1);
        addPublicationToLibrary(lib1, p2);

        Map<String, Library> libs = new HashMap<>();
        libs.put(user.getUserName(), lib1);

        List<PublicationSampleDTO_US016> expectedList = lib1.getAllPublications().stream().map(PublicationSampleDTO_US016::toDTO).toList();

        US016ToPutPublicationInAuctionController controller = newControllerWithLibraries(libs);

        // act
        List<PublicationSampleDTO_US016> result = controller.getUserLibraryPublications(user.getUserName());

        // assert
        assertEquals(expectedList, result);
    }


    // -------------------------
    // putPublicationForAuction
    // -------------------------

    @Test
    void putPublicationForAuction_shouldThrow_ifPublicationNull() {
        // arrange
        US016ToPutPublicationInAuctionController controller = newControllerWithLibraries(Map.of());
        List<Picture> pics = new ArrayList<>();
        Picture pic1 = new Picture("picURL");
        pics.add(pic1);

        // act + assert
        assertThrows(IllegalArgumentException.class, () ->
                // listing
                controller.createAuctionListing(
                        "title",
                        null,
                        price("35.50"),
                        "short",
                        pics,
                        // auction
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(1),
                        price("100.00")
                )
        );
    }

    @Test
    void putPublicationForAuction_shouldThrow_ifInitialFinalOrBuyoutNull() {

        // arrange
        US016ToPutPublicationInAuctionController controller = newControllerWithLibraries(Map.of());

        PublicationSample pub = publication(user("owner1"));
        List<PublicationSampleDTO_US016> pubsDTO = new ArrayList<>();
        pubsDTO.add(PublicationSampleDTO_US016.toDTO(pub));
        List<Picture> pics = new ArrayList<>();
        Picture pic1 = new Picture("picURL");
        pics.add(pic1);

        // act + assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.createAuctionListing("title", pubsDTO, price("10.00"), "d", pics, null, LocalDateTime.now().plusDays(1), price("50.00")));

        assertThrows(IllegalArgumentException.class, () ->
                controller.createAuctionListing("title", pubsDTO, price("10.00"), "d", pics,
                        LocalDateTime.now(), null, price("50.00")));

        assertThrows(IllegalArgumentException.class, () ->
                controller.createAuctionListing("title", pubsDTO, price("10.00"), "d", pics,
                        LocalDateTime.now(), LocalDateTime.now().plusDays(1), null));
    }

    @Test
    void putPublicationForAuction_shouldCreateAuction_andReturnTrue() throws Exception {

        // arrange
        PublicationSample pub = publication(user("owner1"));
        List<PublicationSample> list = new ArrayList<>();
        list.add(pub);

        PublicationSampleRepository pr1 = new PublicationSampleRepository(list);
        ListingRepository listingRepo = new ListingRepository();
        AuctionRepository auctionRepo = new AuctionRepository();
        LibraryRepository libraryRepo = new LibraryRepository(Map.of());

        US016ToPutPublicationInAuctionController controller = new US016ToPutPublicationInAuctionController(libraryRepo, listingRepo, auctionRepo, pr1);

        List<PublicationSampleDTO_US016> pubsDto = new ArrayList<>();
        pubsDto.add(PublicationSampleDTO_US016.toDTO(pub));

        List<Picture> pics = new ArrayList<>();
        Picture pic1 = new Picture("picURL");
        pics.add(pic1);

        Price listingPrice = price("35.50");
        Price buyOutPrice = price("100.00");

        LocalDateTime initial = LocalDateTime.of(2027, 1, 2, 10, 0);
        LocalDateTime fin = LocalDateTime.of(2027, 1, 30, 10, 0);

        int before = auctionRepo.getAllAuctions().size();

        // act
        boolean result = controller.createAuctionListing("title", pubsDto, listingPrice, "short description", pics, initial, fin, buyOutPrice
        );

        // assert
        assertTrue(result);
        assertEquals(before + 1, auctionRepo.getAllAuctions().size());
    }

    @Test
    void putPublicationForAuction_shouldThrowInstanceAlreadyExists_ifDuplicatedListing() throws Exception {
        // arrange
        ListingRepository listingRepo = new ListingRepository();
        AuctionRepository auctionRepo = new AuctionRepository();
        LibraryRepository libraryRepo = new LibraryRepository(Map.of());

        PublicationSample pub = publication(user("owner1"));
        List<PublicationSample> list = new ArrayList<>();
        list.add(pub);

        PublicationSampleRepository pr1 = new PublicationSampleRepository(list);

        US016ToPutPublicationInAuctionController controller = new US016ToPutPublicationInAuctionController(libraryRepo, listingRepo, auctionRepo, pr1);

        Price listingPrice = price("35.50");
        Price buyOutPrice = price("100.00");

        List<PublicationSampleDTO_US016> pubsDto = new ArrayList<>();
        pubsDto.add(PublicationSampleDTO_US016.toDTO(pub));

        List<Picture> pics = new ArrayList<>();
        Picture pic1 = new Picture("picURL");
        pics.add(pic1);

        LocalDateTime initial = LocalDateTime.of(2027, 2, 23, 10, 0);
        LocalDateTime fin = LocalDateTime.of(2027, 2, 28, 10, 0);

        assertTrue(controller.createAuctionListing("title", pubsDto, listingPrice, "short description", pics, initial, fin, buyOutPrice));

        // act + assert
        // Just to ensure this exception is thrown in Listing class when the publications are already in a listing

        assertThrows(IllegalArgumentException.class, () ->
                controller.createAuctionListing("title", pubsDto, listingPrice, "short description", pics, initial, fin, buyOutPrice)
        );
    }


    // ============================================================
    // ========================== Helpers =========================
    // ============================================================


    private US016ToPutPublicationInAuctionController newControllerWithLibraries(Map<String, Library> libs) {
        LibraryRepository libraryRepo = new LibraryRepository(libs);
        ItemRepository itemRepo = new ItemRepository();
        ListingRepository listingRepo = new ListingRepository();
        AuctionRepository auctionRepo = new AuctionRepository();
        PublicationSampleRepository publicationSampleRepository = new PublicationSampleRepository();
        return new US016ToPutPublicationInAuctionController(libraryRepo, listingRepo, auctionRepo, publicationSampleRepository);
    }

    private Price price(String v) {
        return new Price(new BigDecimal(v));
    }

    private User user(String username) {
        Contact contact = contact();
        Profile profile = profile();
        return new User(username, contact, profile, UserRole.NORMAL_USER);
    }

    private PublicationSample publication(User owner) {
        BibliographicalData bd = bibliographicalData();
        PublicationType type = publicationType();
        PublicationStatus status = PublicationStatus.READING;
        Code code = Code.fromIsbn10("123456789X");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
        Edition edition = new Edition(
                "1st",
                code,
                physicalAttributes,
                pubType,
                publisher,
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1)
        );

        return new PublicationSample(bd, owner, status, edition, Condition.LIKE_NEW);
    }

    private PublicationSample uniquePublication(User owner) {
        BibliographicalData bd = bibliographicalData();
        PublicationType type = publicationType();
        PublicationStatus status = PublicationStatus.FOR_SALE;
        Code code = Code.fromIsbn10("12345678" + (System.nanoTime() % 10) + "X");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
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

        return new PublicationSample(bd, owner, status, edition, Condition.LIKE_NEW);
    }

    private PublicationType publicationType() {
        return new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "some details");
    }

    private void addPublicationToLibrary(Library lib, PublicationSample p) {
        lib.addPublication(p);
    }

    private Contact contact() {
        return new Contact("12343244",
                "321",
                new Email("example", "domain.pt"),
                new Address("Street", "12G", "1234-123",
                        new Country("United_States")
                ));
    }

    private Profile profile() {
        return new Profile("Rodolph", new Country("United_kingdom"), LocalDate.of(1967, 12, 3));
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
                LocalDate.of(1991, 11, 1),
                List.of(new Genre("Fantasy")),
                chunkOfText
        );
    }


}