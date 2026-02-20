package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;

import TOPSECRET.repository.PublicationSampleRepository;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


class GetTheListOfItemsOnAuctionOfAGivenPublicationControllerTest {

    //Publication
    Profile profile = new Profile("Tove Ditlevsen", new Country("DENMARK"), LocalDate.of(1917, 12, 14));
    Author author = new Author(profile, "valid bio" );
    Address address = new Address("R Maria", "12", "156-5", new Country("PORTUGAL"));
    Email email = new Email("maria1", "gmail.com");
    Contact contact = new Contact("915945695", "+351", email, address);
//    Publisher publisher = new Publisher("Some Publisher", "PT123456789", contact);
    PublicationType publicationType = new PublicationType(TypeOfBinding.PAPERBACK, "Paperback");

    BibliographicalData bd = new BibliographicalData(
            "Childhood, Youth, Dependency: The Copenhagen Trilogy",
            List.of(author),
            Locale.ENGLISH,
//            publisher,
            LocalDate.of(2020, 1, 1),
            List.of(new Genre ("Fiction")),
//            200,
            "Some text"
    );

    User user = new User("maria1", contact, profile, UserRole.NORMAL_USER
    );

    Code code = Code.fromIsbn13("9781234567890");

    // Edition
    Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
    PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
    PublicationType pubType = new PublicationType(TypeOfBinding.CASE_BINDING, "Some details");
    Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
    Edition edition1= new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
    Edition edition2= new Edition("2st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

//    Publication pub1 = new Publication(bd, publicationType, user, PublicationStatus.FOR_SALE, "1st edition", code);
    Publication pub1 = new Publication(bd, publicationType, user, PublicationStatus.FOR_SALE, edition1, code);

//    Publication pub2 = new Publication(bd, publicationType, user, PublicationStatus.FOR_SALE, "2st edition", code);
    Publication pub2 = new Publication(bd, publicationType, user, PublicationStatus.FOR_SALE, edition2, code);



    // Item
    Dimensions dim = new Dimensions(10,10,10);

    Item item1 = new Item(Condition.LIKE_NEW,50,dim,pub1);

    Item item2 = new Item(Condition.LIKE_NEW,50,dim,pub2);

    private final List<Item> itemList1 = new ArrayList<>(List.of(item1));

    private final List<Item> itemList2 = new ArrayList<>(List.of(item2));

    // User
    LocalDate validBirthday = LocalDate.of(2000, 1, 1);
    Profile validProfile = new Profile("John",new Country("PORTUGAL"),validBirthday);
    Email validEmail = new Email("john.doe", "example.com");
    Address validAddress = new Address("dummyStreet", "dummyNumber", "dummyZipCode", new Country("PORTUGAL"));
    Contact validContact = new Contact("123456789","+351",validEmail,validAddress);
    User validSeller = new User("Seller1",validContact,validProfile,UserRole.NORMAL_USER);

    // Pictures
    List<Picture> validPictures = List.of(new Picture("path/to/pic.jpg"));


//    // Listing
//    Price validPrice = new Price(BigDecimal.valueOf(50.99));
//    private final String validDescription = "Valid Description";
//    private final String validTitle = "PS5 Pro";
//    Listing validListing1 = new Listing(validTitle,itemList1,SaleType.AUCTION,validPrice,validDescription,validSeller, validPictures);



//    // Auction
//    Price price = new Price(BigDecimal.valueOf(90));
//    Auction auc1 = new Auction(validListing1,LocalDateTime.of(2027, 7, 31, 14, 30),LocalDateTime.of(2028, 7, 31, 14, 30),price);


//    // Auc repo
//    List<Auction> aucList = new ArrayList<>(List.of(auc1));
//    AuctionRepository aucRepo = new AuctionRepository(aucList);

    //Pub repo
    List<PublicationSample> pubList = new ArrayList<>();
    private final PublicationSampleRepository pubRepo = new PublicationSampleRepository(pubList);

//    //Controller
//    private GetTheListOfItemsOnAuctionOfAGivenPublicationController ctrl = new GetTheListOfItemsOnAuctionOfAGivenPublicationController(pubRepo, aucRepo);

//    @Test
//    void getAllPublicationsShouldReturnListOfDTOsWhenPublicationsExist() {
//        // Arrange
////        pubRepo.createAndAdd(bd, publicationType, user, PublicationStatus.FOR_SALE, "1st edition", code);
////        pubRepo.createAndAdd(bd, publicationType, user, PublicationStatus.FOR_SALE, "2st edition", code);
//        pubRepo.createAndAdd(bd, publicationType, user, PublicationStatus.FOR_SALE, edition1, code);
//        pubRepo.createAndAdd(bd, publicationType, user, PublicationStatus.FOR_SALE, edition2, code);
//
//
//        // Act
//        List<PublicationDetailsDTO> result = ctrl.getAllPublications();
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(2, result.size());
//        String expectedTitle1 = pub1.getBibliographicalData().getTitle();
//        assertEquals(expectedTitle1, result.get(0).pubTitle);
//
//        String expectedTitle2 = pub2.getBibliographicalData().getTitle();
//        assertEquals(expectedTitle2,result.get(1).pubTitle);
//
//    }

//    @Test
//    void getAllPublicationsShouldReturnEmptyListWhenRepositoryIsEmpty() {
//        // Arrange
//        pubList.clear();
//
//        // Act
//        List<PublicationDetailsDTO> result = ctrl.getAllPublications();
//
//        // Assert
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//    }

//    @Test
//    void getListOfItemsOnAuctionShouldReturnItemsWhenMatchesPublicationDTO() {
//        // Arrange
//
//        // Act
//        List<ItemDetailsDTO> result = ctrl.getListOfItemsOnAuctionByGivenPublicationDTO(pub1);
//        String expectedTitle = pub1.getBibliographicalData().getTitle();
//
//        // Assert
//        assertNotNull(result);
//        assertFalse(result.isEmpty());
//        assertEquals(1, result.size());
//        assertEquals(expectedTitle,result.get(0).publication);
//    }


//    @Test
//    void getListOfItemsOnAuctionShouldReturnEmptyWhenNoItemsMatchPublicationDTO() {
//        // Arrange
//
//        // Act
//        List<ItemDetailsDTO> result = ctrl.getListOfItemsOnAuctionByGivenPublicationDTO(pub2);
//
//        // Assert
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//    }



}