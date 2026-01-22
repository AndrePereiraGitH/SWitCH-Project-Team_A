package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.dto.DirectSaleDTO_US023;
import TOPSECRET.dto.DirectSaleMapper_US023;
import TOPSECRET.repository.DirectSaleRepository;
import TOPSECRET.repository.PublicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationControllerTest {

    private final String validTitle = "PS5 Pro";
    private final String validDescription = "Valid Description";

    Price validPrice;
    List<Picture> validPictures;
    Dimensions dimensions;

    Profile validProfile;
    Email validEmail;
    Address validAddress;
    Contact validContact;
    User validSeller;

    Profile profile;
    Author author;
    Address address;
    Email email;
    Contact contact;
    Publisher publisher;
    BibliographicalData bibliographicalData;

    PublicationType publicationType;
    User user;
    PublicationStatus publicationStatus;

    Code code1;
    Code code2;
    Code code3;

    Item validItem1;
    Item validItem2;
    Item validItem3;

    private List<Item> severalItemList;
    Listing validListing;
    DirectSale validDirectSale;

    @BeforeEach
    void setUp() {
        // Initialize price, pictures and dimensions
        validPrice = new Price(BigDecimal.valueOf(50.99));
        validPictures = List.of(new Picture("path/to/pic.jpg"));
        dimensions = new Dimensions(10.0, 0.2, 5.2);

        // Initialize seller
        validProfile = new Profile("John", new Country("PORTUGAL"), LocalDate.of(2000, 1, 1));
        validEmail = new Email("john.doe", "example.com");
        validAddress = new Address("dummyStreet", "dummyNumber", "dummyZipCode", new Country("PORTUGAL"));
        validContact = new Contact("123456789", "+351", validEmail, validAddress);
        validSeller = new User("Seller1", validContact, validProfile, UserRole.NORMAL_USER);

        // Bibliographical data
        profile = new Profile("Tove Ditlevsen", new Country("DENMARK"), LocalDate.of(1917, 12, 14));
        author = new Author(profile, "valio bio");
        address = new Address("R Maria", "12", "156-5", new Country("PARAGUAY"));
        email = new Email("Maria1", "gmail.com");
        contact = new Contact("915945695", "+351", email, address);
        publisher = new Publisher("Some Publisher", "PT123456789", contact);
        bibliographicalData = new BibliographicalData(
                "Childhood, Youth, Dependency: The Copenhagen Trilogy",
                List.of(author),
                Locale.ENGLISH,
                publisher,
                LocalDate.of(2020, 1, 1),
                List.of(new Genre("Fiction")),
                200,
                "Some text"
        );

        publicationType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Paperback");
        user = new User("maria1", contact, profile, UserRole.NORMAL_USER);
        publicationStatus = PublicationStatus.FOR_SALE;

        code1 = Code.fromIsbn13("9781234567890");
        code2 = Code.fromIsbn13("9781234567891");
        code3 = Code.fromIsbn13("9781234567892");

        // Items referencing different publication editions
        validItem1 = new Item(Condition.LIKE_NEW, 1.2, dimensions,
                new Publication(bibliographicalData, publicationType, user, publicationStatus, "1st edition", code1));

        validItem2 = new Item(Condition.POOR, 1.2, dimensions,
                new Publication(bibliographicalData, publicationType, user, publicationStatus, "2nd edition", code2));

        validItem3 = new Item(Condition.POOR, 1.2, dimensions,
                new Publication(bibliographicalData, publicationType, user, publicationStatus, "3rd edition", code3));

        severalItemList = new ArrayList<>(List.of(validItem1, validItem2, validItem3));
        validListing = new Listing(severalItemList, validPrice, validDescription, validTitle, validSeller, validPictures);
        validDirectSale = new DirectSale(validListing, LocalDateTime.now());
    }

    @Test
    void getAllPublicationsReturnsAllPublicationsFromRepository() {
        // arrange
        Publication pub1 = new Publication(bibliographicalData, publicationType, user, publicationStatus, "1st edition", code1);
        Publication pub2 = new Publication(bibliographicalData, publicationType, user, publicationStatus, "2nd edition", code2);

        PublicationRepository publicationRepo = new PublicationRepository(List.of(pub1, pub2));
        DirectSaleRepository directSaleRepo = new DirectSaleRepository(List.of());
        DirectSaleMapper_US023 directSaleMapper = new DirectSaleMapper_US023();

        ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(publicationRepo, directSaleRepo, directSaleMapper);

        // act
        List<Publication> result = controller.getAllPublications();

        // assert
        assertEquals(2, result.size());
        assertTrue(result.contains(pub1));
        assertTrue(result.contains(pub2));
    }

    @Test
    void getAllPublicationsReturnsCopyNotInternalList() {
        // arrange
        Publication pub = new Publication(bibliographicalData, publicationType, user, publicationStatus, "1st edition", code1);

        PublicationRepository publicationRepo = new PublicationRepository(List.of(pub));
        DirectSaleRepository directSaleRepo = new DirectSaleRepository(List.of());
        DirectSaleMapper_US023 mapper = new DirectSaleMapper_US023();

        ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(publicationRepo, directSaleRepo, mapper);

        // act
        List<Publication> result = controller.getAllPublications();

        // The repository returns an immutable copy (attempting to modify should throw exception)
        assertThrows(UnsupportedOperationException.class, result::clear);

        // Verify repository remains intact
        assertEquals(1, controller.getAllPublications().size());
    }

    @Test
    void getListOfItemsOnDirectSaleReturnsMappedDTOs() {
        // arrange
        Publication targetPublication = validItem1.getPublication();

        Listing listing1 = new Listing(List.of(validItem1), validPrice, validDescription, validTitle, validSeller, validPictures);
        Listing listing2 = new Listing(List.of(validItem1), validPrice, validDescription, validTitle, validSeller, validPictures);

        DirectSale ds1 = new DirectSale(listing1, LocalDateTime.now());
        DirectSale ds2 = new DirectSale(listing2, LocalDateTime.now());

        DirectSaleRepository dsRepo = new DirectSaleRepository(List.of(ds1, ds2));
        PublicationRepository pubRepo = new PublicationRepository(List.of());
        DirectSaleMapper_US023 mapper = new DirectSaleMapper_US023();

        ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(pubRepo, dsRepo, mapper);

        // act
        List<DirectSaleDTO_US023> result = controller.getListOfItemsOnDirectSale(targetPublication);

        // assert
        assertEquals(2, result.size());
        DirectSaleDTO_US023 dto = result.get(0);
        assertAll(
                () -> assertEquals(validItem1.getPublication().getBibliographicalData().getTitle(), dto.publicationTitle),
                () -> assertEquals(validPrice.getPrice(), dto.price),
                () -> assertEquals(validTitle, dto.listingTitle),
                () -> assertEquals(validSeller.getProfile().getName(), dto.seller),
                () -> assertEquals(List.of(author.getProfile().getName()), dto.authors)
        );
    }

    @Test
    void getListOfItemsOnDirectSaleReturnsEmptyListWhenNoMatches() {
        // arrange
        Publication unrelatedPublication = new Publication(
                bibliographicalData, publicationType, user, publicationStatus,
                "Unrelated", Code.fromIsbn13("9999999999999")
        );

        DirectSaleRepository dsRepo = new DirectSaleRepository(List.of());
        PublicationRepository pubRepo = new PublicationRepository(List.of());
        DirectSaleMapper_US023 mapper = new DirectSaleMapper_US023();

        ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(pubRepo, dsRepo, mapper);

        // act
        List<DirectSaleDTO_US023> result = controller.getListOfItemsOnDirectSale(unrelatedPublication);

        // assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getListOfItemsOnDirectSaleMapsAllFieldsCorrectly() {
        // arrange
        Publication targetPublication = validItem1.getPublication();

        Listing listing = new Listing(
                List.of(validItem1),
                validPrice,
                validDescription,
                validTitle,
                validSeller,
                validPictures
        );

        DirectSale ds = new DirectSale(listing, LocalDateTime.now());

        DirectSaleRepository dsRepo = new DirectSaleRepository(List.of(ds));
        PublicationRepository pubRepo = new PublicationRepository(List.of());
        DirectSaleMapper_US023 mapper = new DirectSaleMapper_US023();

        ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
                new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(pubRepo, dsRepo, mapper);

        // act
        List<DirectSaleDTO_US023> result = controller.getListOfItemsOnDirectSale(targetPublication);

        // assert
        assertEquals(1, result.size());
        DirectSaleDTO_US023 dto = result.get(0);

        assertAll(
                () -> assertEquals(validItem1.getPublication().getBibliographicalData().getTitle(), dto.publicationTitle),
                () -> assertEquals(validPrice.getPrice(), dto.price),
                () -> assertEquals(validTitle, dto.listingTitle),
                () -> assertEquals(validSeller.getProfile().getName(), dto.seller),
                () -> assertEquals(List.of(author.getProfile().getName()), dto.authors)
        );
    }
}