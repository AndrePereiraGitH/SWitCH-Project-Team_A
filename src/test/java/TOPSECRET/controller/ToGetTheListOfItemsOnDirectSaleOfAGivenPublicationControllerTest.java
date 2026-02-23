package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.dto.DirectSaleMapper_US023;
import TOPSECRET.dto.PublicationMapper_US023;
import TOPSECRET.repository.PublicationSampleRepository;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    User validBuyer;

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

        // Initialize buyer
        validProfile = new Profile("Ross", new Country("PORTUGAL"), LocalDate.of(2000, 1, 1));
        validEmail = new Email("ross.doe", "example.com");
        validAddress = new Address("dummyStreet", "dummyNumber", "dummyZipCode", new Country("PORTUGAL"));
        validContact = new Contact("123456789", "+351", validEmail, validAddress);
        validBuyer = new User("Buyer1", validContact, validProfile, UserRole.NORMAL_USER);

        // Bibliographical data
        profile = new Profile("Tove Ditlevsen", new Country("DENMARK"), LocalDate.of(1917, 12, 14));
        author = new Author(profile, "valio bio");
//        address = new Address("R Maria", "12", "156-5", new Country("PARAGUAY"));
//        email = new Email("Maria1", "gmail.com");
//        contact = new Contact("915945695", "+351", email, address);
//        publisher = new Publisher("Some Publisher", "PT123456789", contact);
        bibliographicalData = new BibliographicalData(
                "Childhood, Youth, Dependency: The Copenhagen Trilogy",
                List.of(author),
                Locale.ENGLISH,
//                publisher,
                LocalDate.of(2020, 1, 1),
                List.of(new Genre("Fiction")),
//                200,
                "Some text"
        );

        publicationType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Paperback");
        user = new User("maria1", contact, profile, UserRole.NORMAL_USER);
        publicationStatus = PublicationStatus.FOR_SALE;

        code1 = Code.fromIsbn13("9781234567890");
        code2 = Code.fromIsbn13("9781234567891");
        code3 = Code.fromIsbn13("9781234567892");

        // Edition
        Code code = Code.fromIsbn13 ("978316148412");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
        Edition edition1= new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
        Edition edition2= new Edition("2st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
        Edition edition3= new Edition("3st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

        // Items referencing different publication editions
        validItem1 = new Item(Condition.LIKE_NEW, 1.2, dimensions,
//                new Publication(bibliographicalData, publicationType, user, publicationStatus, "1st edition", code1));
                new Publication(bibliographicalData, publicationType, user, publicationStatus, edition1, code1));

        validItem2 = new Item(Condition.POOR, 1.2, dimensions,
//                new Publication(bibliographicalData, publicationType, user, publicationStatus, "2nd edition", code2));
                new Publication(bibliographicalData, publicationType, user, publicationStatus, edition2, code2));

        validItem3 = new Item(Condition.POOR, 1.2, dimensions,
//                new Publication(bibliographicalData, publicationType, user, publicationStatus, "3rd edition", code3));
                new Publication(bibliographicalData, publicationType, user, publicationStatus, edition3, code3));

        severalItemList = new ArrayList<>(List.of(validItem1, validItem2, validItem3));
//        validListing = new Listing(validTitle, severalItemList,SaleType.DIRECT_SALE, validPrice, validDescription, validSeller, validPictures);
//        validDirectSale = new DirectSale(validListing, LocalDateTime.now());
        validDirectSale = new DirectSale(List.of(validListing), validBuyer, false);
    }

//    @Test
//    void getAllPublicationsReturnsMappedDTOs() {
//        // arrange
////        Publication pub1 = new Publication(bibliographicalData, publicationType, user, publicationStatus, "1st edition", code1);
////        Publication pub2 = new Publication(bibliographicalData, publicationType, user, publicationStatus, "2nd edition", code2);
//
//        // Edition
//        Code code = Code.fromIsbn13 ("978316148412");
//        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
//        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
//        PublicationType pubType = new PublicationType(TypeOfBinding.CASE_BINDING, "Some details");
//        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
//        Edition edition1= new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
//        Edition edition2= new Edition("2st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
//
//        Publication pub1 = new Publication(bibliographicalData, publicationType, user, publicationStatus, edition1, code1);
//        Publication pub2 = new Publication(bibliographicalData, publicationType, user, publicationStatus, edition2, code2);
//
//        PublicationRepository pubRepo = new PublicationRepository(List.of(pub1, pub2));
//        PublicationMapper_US023 pubMapper = new PublicationMapper_US023();
//        DirectSaleRepository dsRepo = new DirectSaleRepository(List.of());
//        DirectSaleMapper_US023 dsMapper = new DirectSaleMapper_US023();
//
//        ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller = new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(pubRepo, pubMapper, dsRepo, dsMapper);
//
//        // act
//        List<PublicationDTO_US023> result = controller.getAllPublications();
//
//        // assert
//        assertEquals(2, result.size());
//        assertEquals(pub1.getBibliographicalData().getTitle(), result.get(0).pubTitle);
//        assertEquals(pub2.getBibliographicalData().getTitle(), result.get(1).pubTitle);
//    }

//    @Test
//    void getAllPublicationsReturnsMappedListThatIsIndependentAndMutable() {
//        // arrange
//
//        // Edition
//        Code code = Code.fromIsbn13 ("978316148412");
//        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
//        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
//        PublicationType pubType = new PublicationType(TypeOfBinding.CASE_BINDING, "Some details");
//        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
//        Edition edition1= new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
//
    ////        Publication pub = new Publication(bibliographicalData, publicationType, user, publicationStatus, "1st edition", code1);
//        Publication pub = new Publication(bibliographicalData, publicationType, user, publicationStatus, edition1, code1);
//
//        PublicationRepository pubRepo = new PublicationRepository(List.of(pub));
//        PublicationMapper_US023 pubMapper = new PublicationMapper_US023();
//        DirectSaleRepository dsRepo = new DirectSaleRepository(List.of());
//        DirectSaleMapper_US023 dsMapper = new DirectSaleMapper_US023();
//
//
//        ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller = new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(pubRepo, pubMapper, dsRepo, dsMapper);
//
//        // act
//        List<PublicationDTO_US023> result = controller.getAllPublications();
//
//        // assert
//        assertEquals(1, result.size());
//        assertInstanceOf(PublicationDTO_US023.class, result.get(0));
//    }


//    @Test
//    void getListOfItemsOnDirectSaleReturnsMappedDTOs() {
    // arrange
    Publication targetPublication = validItem1.getPublication();

//        Listing listing1 = new Listing(validTitle, List.of(validItem1), SaleType.DIRECT_SALE, validPrice, validDescription, validSeller, validPictures);
//        Listing listing2 = new Listing(validTitle, List.of(validItem1), SaleType.DIRECT_SALE, validPrice, validDescription, validSeller, validPictures);

//        DirectSale ds1 = new DirectSale(listing1, LocalDateTime.now());
//        DirectSale ds2 = new DirectSale(listing2, LocalDateTime.now());
//        DirectSale ds1 = new DirectSale(List.of(listing1), validBuyer, false);
//        DirectSale ds2 = new DirectSale(List.of(listing2), validBuyer, false);

    PublicationSampleRepository pubRepo = new PublicationSampleRepository(List.of());
    PublicationMapper_US023 pubMapper = new PublicationMapper_US023();
    //        DirectSaleRepository dsRepo = new DirectSaleRepository(List.of(ds1, ds2));
    DirectSaleMapper_US023 dsMapper = new DirectSaleMapper_US023();

//        ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
//                new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(pubRepo, pubMapper,dsRepo, dsMapper);

    // act
//        List<DirectSaleDTO_US023> result = controller.getListOfItemsOnDirectSale(targetPublication);

    // assert
//        assertEquals(2, result.size());
//        DirectSaleDTO_US023 dto = result.get(0);
//        assertAll(
//                () -> assertEquals(validItem1.getPublication().getBibliographicalData().getTitle(), dto.publicationTitle),
//                () -> assertEquals(validPrice.getPrice(), dto.price),
//                () -> assertEquals(validTitle, dto.listingTitle),
//                () -> assertEquals(validSeller.getProfile().getName(), dto.seller),
//                () -> assertEquals(List.of(author.getProfile().getName()), dto.authors)
//        );
//    }

//    @Test
//    void getAllPublicationsReturnsEmptyListWhenRepositoryIsEmpty() {
//        PublicationRepository pubRepo = new PublicationRepository(List.of());
//        PublicationMapper_US023 pubMapper = new PublicationMapper_US023();
//        DirectSaleRepository dsRepo = new DirectSaleRepository(List.of());
//        DirectSaleMapper_US023 dsMapper = new DirectSaleMapper_US023();
//
//
//        ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller = new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(pubRepo, pubMapper, dsRepo, dsMapper);
//
//        List<PublicationDTO_US023> result = controller.getAllPublications();
//
//        assertTrue(result.isEmpty());
//    }

//    @Test
//    void getListOfItemsOnDirectSaleMapsAllFieldsCorrectly() {
//        // arrange
//        Publication targetPublication = validItem1.getPublication();
//
//        Listing listing = new Listing(
//                validTitle,
//                List.of(validItem1),
//                SaleType.DIRECT_SALE,
//                validPrice,
//                validDescription,
//                validSeller,
//                validPictures
//        );
//
////        DirectSale ds = new DirectSale(listing, LocalDateTime.now());
//        DirectSale ds = new DirectSale(List.of(listing), validBuyer, false);
//
//        PublicationRepository pubRepo = new PublicationRepository(List.of());
//        PublicationMapper_US023 pubMapper = new PublicationMapper_US023();
//        DirectSaleRepository dsRepo = new DirectSaleRepository(List.of(ds));
//        DirectSaleMapper_US023 dsMapper = new DirectSaleMapper_US023();
//
//        ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
//                new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(pubRepo, pubMapper, dsRepo, dsMapper);
//
//        // act
//        List<DirectSaleDTO_US023> result = controller.getListOfItemsOnDirectSale(targetPublication);
//
//        // assert
//        assertEquals(1, result.size());
//        DirectSaleDTO_US023 dto = result.get(0);
//
//        assertAll(
//                () -> assertEquals(validItem1.getPublication().getBibliographicalData().getTitle(), dto.publicationTitle),
//                () -> assertEquals(validPrice.getPrice(), dto.price),
//                () -> assertEquals(validTitle, dto.listingTitle),
//                () -> assertEquals(validSeller.getProfile().getName(), dto.seller),
//                () -> assertEquals(List.of(author.getProfile().getName()), dto.authors)
//        );
//    }

//    @Test
//    void getListOfItemsOnDirectSaleThrowsWhenPublicationIsNull() {
//        // arrange
//        PublicationRepository pubRepo = new PublicationRepository(List.of());
//        PublicationMapper_US023 pubMapper = new PublicationMapper_US023();
//        DirectSaleRepository dsRepo = new DirectSaleRepository(List.of());
//        DirectSaleMapper_US023 dsMapper = new DirectSaleMapper_US023();
//
//        ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller =
//                new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(pubRepo, pubMapper, dsRepo, dsMapper);
//
//        // act
//        IllegalArgumentException e = assertThrows(
//                IllegalArgumentException.class,
//                () -> controller.getListOfItemsOnDirectSale(null)
//        );

//        // assert
//        assertEquals("Publication cannot be null", e.getMessage());
//    }
}