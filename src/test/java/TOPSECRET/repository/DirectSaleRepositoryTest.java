package TOPSECRET.repository;

import TOPSECRET.domain.*;

import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;


class DirectSaleRepositoryTest {

    // Valid primitive attributes
    private final String validDescription = "Valid Description";
    private final String validTitle = "PS5 Pro";


    //Price
    Price validPrice = new Price(BigDecimal.valueOf(50.99));

    // Pictures
    List<Picture> validPictures = List.of(new Picture("path/to/pic.jpg"));

    // Dimensions
    Dimensions dimensions = new Dimensions(10.0, 0.2, 5.2);

    // User
    LocalDate validBirthday = LocalDate.of(2000, 1, 1);
    Profile validProfile = new Profile("John",new Country("PORTUGAL"),validBirthday);
    Email validEmail = new Email("john.doe", "example.com");
    Address validAddress = new Address("dummyStreet", "dummyNumber", "dummyZipCode", new Country("PORTUGAL"));
    Contact validContact = new Contact("123456789","+351",validEmail,validAddress);
    User validSeller = new User("Seller1",validContact,validProfile, UserRole.NORMAL_USER);

    // BibliographicalData
    Profile profile = new Profile("Tove Ditlevsen", new Country("DENMARK"), LocalDate.of(1917, 12, 14));
    Author author = new Author(profile, "valio bio");
    Address address = new Address("R Maria", "12", "156-5", new Country("PARAGUAY"));
    Email email = new Email("Maria1", "gmail.com");
    Contact contact = new Contact("915945695", "+351", email, address);
    Publisher publisher = new Publisher("Some Publisher", "PT123456789", contact);
    BibliographicalData bibliographicalData = new BibliographicalData(
            "Childhood, Youth, Dependency: The Copenhagen Trilogy",
            List.of(author),
            Locale.ENGLISH,
//            publisher,
            LocalDate.of(2020, 1, 1),
            List.of(new Genre ("Fiction")),
//            200,
            "Some text"
    );


    PublicationType publicationType = new PublicationType(TypeOfBinding.PAPERBACK, "Paperback"
    );

    User user = new User("maria1", contact, profile, UserRole.NORMAL_USER);
    PublicationStatus publicationStatus = PublicationStatus.FOR_SALE;

    Code code1 = Code.fromIsbn13("9781234567890");
    Code code2 = Code.fromIsbn13("9781234567891");
    Code code3 = Code.fromIsbn13("9781234567892");

    // Edition
    Code code = Code.fromIsbn13 ("978316148412");
    PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
    PublicationType pubType = new PublicationType(TypeOfBinding.CASE_BINDING, "Some details");

    Edition edition1= new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
    Edition edition2= new Edition("2st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
    Edition edition3= new Edition("3st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

    Item validItem1 = new Item(Condition.LIKE_NEW, 1.2, dimensions,
//            new Publication(bibliographicalData, publicationType, user, publicationStatus, "1st edition", code1));
            new Publication(bibliographicalData, publicationType, user, publicationStatus, edition1, code1));

    Item validItem2 = new Item(Condition.POOR, 1.2, dimensions,
//            new Publication(bibliographicalData, publicationType, user, publicationStatus, "2nd edition", code2));
            new Publication(bibliographicalData, publicationType, user, publicationStatus, edition2, code2));

    Item validItem3 = new Item(Condition.POOR, 1.2, dimensions,
//            new Publication(bibliographicalData, publicationType, user, publicationStatus, "3rd edition", code3));
            new Publication(bibliographicalData, publicationType, user, publicationStatus, edition3, code3));



    private final List<Item> severalItemList =
            new ArrayList<>(List.of(validItem1, validItem2, validItem3));

    Listing validListing = new Listing(severalItemList,validPrice,validDescription,validTitle,validSeller, validPictures);


    DirectSale validDirectSale = new DirectSale(validListing, LocalDateTime.now());



    @Test
    void ensureANewDirectSaleIsCreatedAndAdded() {
        // Arrange
        List<DirectSale> list = new ArrayList<>();
        DirectSaleRepository repo = new DirectSaleRepository(list);

        //Act
        boolean result = repo.createAndAddDirectSale(validListing,LocalDateTime.now());

        //Assert
        assertTrue(result);

    }

    /*@Test
    void ensureADuplicateDirectSaleIsNotCreatedAndAdded(){
        // Arrange
        List<DirectSale> list = new ArrayList<>();
        list.add(validDirectSale);
        DirectSaleRepository repo = new DirectSaleRepository(list);


        //Act
        boolean result = repo.createAndAddDirectSale(validListing, LocalDateTime.now());

        //Assert
        assertTrue(result);
    }

     */

    @Test
    void ensureIfDirectSaleExistsReturnsTrue(){
        // Arrange
        List<DirectSale> list = new ArrayList<>();
        list.add(validDirectSale);
        DirectSaleRepository repo = new DirectSaleRepository(list);


        //Act
        boolean result = repo.ifDirectSaleExists(validDirectSale);

        //Assert
        assertTrue(result);

    }

    @Test
    void ensureIfDirectSaleDoesNotExistReturnsFalse(){
        // Arrange
        List<DirectSale> list = new ArrayList<>();
        DirectSaleRepository repo = new DirectSaleRepository(list);


        //Act
        boolean result = repo.ifDirectSaleExists(validDirectSale);

        //Assert
        assertFalse(result);

    }

    @Test
    void ensureConstructorCopiesListNotOriginalReferences(){
        // Arrange
        List<DirectSale> list = new ArrayList<>();
        DirectSaleRepository repo = new DirectSaleRepository(list);
        list.add(validDirectSale);

        // Act
        boolean expectedResult = repo.ifDirectSaleExists(validDirectSale);

        //Assert
        assertFalse(expectedResult);

    }

    //US021-------------------------------------------------------------------------------------------
    @Test
    public void testGetListOfItemsOnDirectSaleOfAGivenGenre_NullGenre() {
        // Arrange
        List<DirectSale> directSales = new ArrayList<>();
        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            directSaleRepository.getListOfItemsOnDirectSaleOfAGivenGenre(null);
        });
        assertEquals("Genre cannot be null", exception.getMessage());
    }

    @Test
    public void testGetListOfItemsOnDirectSaleOfAGivenGenre_EmptyRepository() {
        // Arrange
        List<DirectSale> directSales = new ArrayList<>();
        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);
        Genre genre = new Genre("Fiction");

        // Act
        List<Item> result = directSaleRepository.getListOfItemsOnDirectSaleOfAGivenGenre(genre);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetListOfItemsOnDirectSaleOfAGivenGenre_NoMatchingGenre() {
        // Arrange
        List<DirectSale> directSales = new ArrayList<>();
        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);

        // Add a DirectSale with "Science" genre
        Listing listing = createListingWithGenre("Science");
        directSaleRepository.createAndAddDirectSale(listing, LocalDateTime.now());

        Genre genre = new Genre("Fiction"); // looking for Fiction, but only Science exists

        // Act
        List<Item> result = directSaleRepository.getListOfItemsOnDirectSaleOfAGivenGenre(genre);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty()); // should return nothing
    }

    @Test
    public void testGetListOfItemsOnDirectSaleOfAGivenGenre_OneMatch() {
        // Arrange
        List<DirectSale> directSales = new ArrayList<>();
        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);

        Listing listing = createListingWithGenre("Fiction");
        directSaleRepository.createAndAddDirectSale(listing, LocalDateTime.now());

        Genre genre = new Genre("Fiction");

        // Act
        List<Item> result = directSaleRepository.getListOfItemsOnDirectSaleOfAGivenGenre(genre);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetListOfItemsOnDirectSaleOfAGivenGenre_MultipleMatches() {
        // Arrange
        List<DirectSale> directSales = new ArrayList<>();
        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);

        // Two different listings both with Fiction items
        directSaleRepository.createAndAddDirectSale(createListingWithGenre("Fiction"), LocalDateTime.now());
        directSaleRepository.createAndAddDirectSale(createListingWithGenre("Fiction"), LocalDateTime.now().plusDays(1));

        Genre genre = new Genre("Fiction");

        // Act
        List<Item> result = directSaleRepository.getListOfItemsOnDirectSaleOfAGivenGenre(genre);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetListOfItemsOnDirectSaleOfAGivenGenre_MixedGenres() {
        // Arrange
        List<DirectSale> directSales = new ArrayList<>();
        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);

        // One Fiction, one Science
        directSaleRepository.createAndAddDirectSale(createListingWithGenre("Fiction"), LocalDateTime.now());
        directSaleRepository.createAndAddDirectSale(createListingWithGenre("Science"), LocalDateTime.now().plusDays(1));

        Genre genre = new Genre("Fiction");

        // Act
        List<Item> result = directSaleRepository.getListOfItemsOnDirectSaleOfAGivenGenre(genre);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size()); // only Fiction item returned
    }

    private static Listing createListingWithGenre(String genreName) {
        return new Listing(
                List.of(createItemWithGenre(genreName)),
                new Price(new BigDecimal("19.99")),
                "Short Description",
                "Listing Title",
                createUser(),
                List.of(new Picture("pic"))
        );
    }

    private static User createUser(){
        return new User(
                "Seller",
                new Contact(
                        "918374628",
                        "+351",
                        new Email("user", "example.com"),
                        new Address("Avenida da Liberdade", "25", "3470-302", createCountry())
                ),
                new Profile(
                        "Seller",
                        createCountry(),
                        LocalDate.of(1993, 9, 19)
                ),
                UserRole.NORMAL_USER
        );
    }

    private static Country createCountry() {
        return new Country("PORTUGAL");       // match your Countries enum value
    }

    private static Item createItemWithGenre(String genreName) {
        return new Item(
                Condition.LIKE_NEW,
                230.30,
                new Dimensions(123, 242, 124),
                createPublicationWithGenre(genreName)
        );
    }

    private static Publication createPublicationWithGenre(String genreName) {
        return new Publication(
                createBibliographicalDataWithGenre(genreName),
                createPublicationType(),
                createUser(),
                PublicationStatus.FOR_SALE,
                createEdition(),
                createCode()
        );
    }

    private static Code createCode(){
        return Code.fromIsbn10(
                "1928375849"
        );
    }


    private static Edition createEdition() {
        return new Edition(
                "1st Edition",
                createCode(),
                createPhysicalAttributes(),
                createPublicationType(),
                createPublisher(),
                Locale.ENGLISH,
                LocalDate.now()
        );
    }

    private static Publisher createPublisher() {
        return new Publisher(
                "Publisher Name",  // name: String
                "123456789",       // vatNumber: String
                createContact()    // contact: Contact
        );
    }

    private static Contact createContact() {
        return new Contact("912345678", "123", createEmail(), createAddress());
    }

    private static Email createEmail() {
        return new Email("test", "test.com");
    }

    private static Address createAddress() {
        return new Address("Rua Test", "1", "4000-000", createCountry());
    }


    private static PhysicalAttributes createPhysicalAttributes() {
        return new PhysicalAttributes(
                new Dimensions(10.0, 20.0, 5.0),  // dimensions
                1.5,                               // weight: double
                300                                // numberOfPages: int
        );
    }


    private static PublicationType createPublicationType(){
        return new PublicationType(
                TypeOfBinding.HARDBACK,
                "Details"
        );
    }

    private static BibliographicalData createBibliographicalDataWithGenre(String genreName) {
        return new BibliographicalData(
                "Harry Potter",
                List.of(createAuthor()),
                new Locale("pt", "PT"),
                LocalDate.of(2020, 1, 30),
                List.of(new Genre(genreName)),  // dynamic genre
                "Text Description"
        );
    }


    private static Author createAuthor(){
        return new Author(
                createProfile(),
                "valid bio"
        );
    }

    private static Profile createProfile(){
        return new Profile(
                "Freida",
                createCountry(),
                LocalDate.of(1968, 10, 12)
        );
    }
    // US021 End ----------------------------------------------------------


    // US023 Start-------------------------------------- -------------------------------------------------------------

    // Test1 - Return all DS that contains publication
    @Test
    void getListOfItemsOnDirectSaleReturnsAllMatchingSales() {
        // Arrange
        // Publicação alvo: a do validItem1
        Publication targetPublication = validItem1.getPublication();

        // Criar duas DirectSale diferentes com items que contêm a mesma publicação
        Listing listing1 = new Listing(
                List.of(validItem1),
                validPrice,
                validDescription,
                validTitle,
                validSeller,
                validPictures
        );

        Listing listing2 = new Listing(
                List.of(new Item(Condition.LIKE_NEW, 1.2, dimensions, targetPublication)),
                validPrice,
                validDescription,
                validTitle,
                validSeller,
                validPictures
        );

        DirectSale ds1 = new DirectSale(listing1, LocalDateTime.now());
        DirectSale ds2 = new DirectSale(listing2, LocalDateTime.now());

        DirectSaleRepository repo = new DirectSaleRepository(List.of(ds1, ds2));

        // Act
        List<DirectSale> result = repo.getListOfItemsOnDirectSale(targetPublication);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(ds1));
        assertTrue(result.contains(ds2));
    }

    // Test2 - Return empty list when publication does not have any DS

    // Edition
    Edition edition= new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

    @Test
    void getListOfItemsOnDirectSaleReturnsEmptyWhenNoMatch() {
        // Arrange
        // Criar publicação que não aparece em nenhum item
        Publication unrelatedPublication = new Publication(
                bibliographicalData,
                publicationType,
                user,
                publicationStatus,
//                "Unrelated edition",
                edition,
                Code.fromIsbn13("9999999999999")
        );

        DirectSaleRepository repo = new DirectSaleRepository(List.of(validDirectSale));

        // Act
        List<DirectSale> result = repo.getListOfItemsOnDirectSale(unrelatedPublication);

        // Assert
        assertTrue(result.isEmpty());
    }

    // Test3 - One DS with more than 1 publication (the same one), only returns 1
    @Test
    void getListOfItemsOnDirectSaleDoesNotDuplicateDirectSale() {
        // Arrange
        Publication targetPublication = validItem1.getPublication();

        // Criar dois items com a mesma publicação
        Item itemA = new Item(Condition.LIKE_NEW, 1.2, dimensions, targetPublication);
        Item itemB = new Item(Condition.LIKE_NEW, 1.2, dimensions, targetPublication);

        Listing listing = new Listing(
                List.of(itemA, itemB),
                validPrice,
                validDescription,
                validTitle,
                validSeller,
                validPictures
        );

        DirectSale ds = new DirectSale(listing, LocalDateTime.now());

        DirectSaleRepository repo = new DirectSaleRepository(List.of(ds));

        // Act
        List<DirectSale> result = repo.getListOfItemsOnDirectSale(targetPublication);

        // Assert
        assertEquals(1, result.size());
        assertSame(ds, result.get(0));
    }

    // Test4 - Verifies if publication is not null
    @Test
    void getListOfItemsOnDirectSaleShouldThrowWhenPublicationIsNull() {
        // Arrange
        DirectSaleRepository dsRepo = new DirectSaleRepository(List.of(validDirectSale));

        // Act
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dsRepo.getListOfItemsOnDirectSale((Publication) null)     // Publication cast because there's also this method signature for Author
        );

        // Assert
        assertEquals("Publication cannot be null", e.getMessage());
    }

    // US023 End -------------------------------------------------------------------------------------------------------

//    @Test
//    void getListOfItemsOnDirectSale_shouldReturnCopyOfListOfDirectSales() {
//        // Arrange
//        List<DirectSale> directSales = List.of(new DirectSale());
//        DirectSaleRepository repository = new DirectSaleRepository(directSales);
//
//        // Act
//        List<DirectSale> result = repository.getListOfItemsOnDirectSale(publisher);
//
//        // Assert
//        assertNotSame(result, directSales);
//        assertEquals(1, result.size());
//        assertSame(result.get(0), directSales.get(0));
//    }
}