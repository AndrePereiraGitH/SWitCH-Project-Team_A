package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.dto.LibraryPublicationDTO_US015;
import TOPSECRET.repository.DirectSaleRepository;
import TOPSECRET.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ToPutPublicationInDirectSaleControllerTest {

    private Library library;
    private DirectSaleRepository directSaleRepo;
    private ItemRepository itemRepo;

    private ToPutPublicationInDirectSaleController controller;

    Country country1 = new Country("Denmark");
    Country country2 = new Country("Germany");

    Profile profileAuthor1 = new Profile("Tove Ditlevsen", country1, LocalDate.of(1917, 12, 14));
    Author author1 = new Author(profileAuthor1, "valid bio1");
    Address address1 = new Address("R Maria", "12", "156-5", country2);
    Email email1 = new Email("Maria1", "gmail.com");
    Contact contact1 = new Contact("915945695", "+351", email1, address1);
//    Publisher publisher1 = new Publisher("Some Publisher", "PT123456789", contact1);

    BibliographicalData bibliographicalData1 = new BibliographicalData(
            "Childhood, Youth, Dependency: The Copenhagen Trilogy",
            List.of(author1),
            Locale.ENGLISH,
//            publisher1,
            LocalDate.of(2020, 1, 1),
            List.of(new Genre("Fiction")),
//            200,
            "Some text"
    );
    Country country4 = new Country("Uganda");
    Profile profile2 = new Profile(
            "Test Profile",
            country4,
            LocalDate.of(1997, 8, 15)
    );

    UserRole userRole1 = UserRole.ADMIN;
    User user1 = new User("coolGuy", contact1, profile2, userRole1);

    PublicationStatus pubStatus1 = PublicationStatus.READING;
//    String edition1 = "2nd Edition";
    Code code = Code.fromIsbn13 ("978316148412");
    Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
    PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
    PublicationType pubType = new PublicationType(TypeOfBinding.CASE_BINDING, "Some details");
    Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
    Edition edition1= new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

    Code code1 = Code.fromIsbn10("1234567890");

    PublicationType publicationType =
            new PublicationType(TypeOfBinding.PAPERBACK, "Paperback");

    Publication publication1 = new Publication(
            bibliographicalData1, publicationType, user1, pubStatus1, edition1, code1
    );



    // ------------------------------------------------------

    @BeforeEach
    void setup() {
        // Arrange
        library = new Library(List.of(publication1), "My Library", user1);

        directSaleRepo = new DirectSaleRepository(new ArrayList<>());
        itemRepo = new ItemRepository();

        controller = new ToPutPublicationInDirectSaleController(library, directSaleRepo, itemRepo) {
//            @Override
//            LocalDateTime date() {
//                return LocalDateTime.of(2026, 1, 25, 12, 0);
//            }
        };
    }


    // TEST 1 - getAllPublications -------------------------------------------------------------------------------------
    @Test
    void ensureGetAllPublicationsReturnsMappedDTOs() {
        List<LibraryPublicationDTO_US015> result = controller.getAllPublications();

        assertEquals(1, result.size());
        assertEquals("Childhood, Youth, Dependency: The Copenhagen Trilogy", result.get(0).pubTitle);
        assertEquals("1234567890", result.get(0).code);
        assertEquals(List.of("Tove Ditlevsen"), result.get(0).authorName);
        assertEquals("Paperback", result.get(0).publicationType);
    }

    // Test 2 - createAndAddItem ---------------------------------------------------------------------------------------
    @Test
    void ensureCreateAndAddItemCreatesAndStoresItem() throws InstanceAlreadyExistsException {
        Dimensions dimensions = new Dimensions(10, 20, 30);

        Item item = controller.createAndAddItem(Condition.GOOD, 1.5, dimensions, publication1);

        assertNotNull(item);
        assertEquals(publication1, item.getPublication());
        assertEquals(1, itemRepo.get_Items().size());
    }

    // Test 3 - Verifies that a valid DirectSale is created and stored--------------------------------------------------
    @Test
    void ensureCreateAndAddDirectSaleCreatesAndStoresDirectSale() throws InstanceAlreadyExistsException {
        Dimensions dimensions = new Dimensions(10, 20, 30);
        Item item = controller.createAndAddItem(Condition.GOOD, 1.5, dimensions, publication1);

        Price price = new Price(new BigDecimal("10.5"));
        Listing listing = new Listing("Mag for sale like new", List.of(item), SaleType.DIRECT_SALE, price, "Short description", user1, List.of(new Picture("pic")));

//        boolean result = controller.createAndAddDirectSale(listing);
        boolean result = controller.createAndAddDirectSale(List.of(listing), user1, false);

        assertTrue(result);
        assertEquals(1, directSaleRepo.getListOfItemsOnDirectSale(publication1).size());
    }

    // Test 4 - Ensures that throws an error when the listing is null --------------------------------------------------
    @Test
    void ensureCreateAndAddDirectSaleThrowsIfListingIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
//                controller.createAndAddDirectSale(null)
                controller.createAndAddDirectSale(null, user1, false)
        );
    }

    // Test 5 - Ensures that throws an error when the buyer is null --------------------------------------------------
    @Test
    void ensureCreateAndAddDirectSaleThrowsIfBuyerIsNull() throws InstanceAlreadyExistsException {

        Dimensions dimensions = new Dimensions(10, 20, 30);
        Item item = controller.createAndAddItem(Condition.GOOD, 1.5, dimensions, publication1);

        Price price = new Price(new BigDecimal("10.5"));
        Listing listing = new Listing("Mag for sale like new",List.of(item), SaleType.DIRECT_SALE,price,"Short description", user1, List.of(new Picture("pic1")));

        assertThrows(IllegalArgumentException.class, () ->
                controller.createAndAddDirectSale(List.of(listing), null, false)
        );
    }

    // Test 6 - Ensures that throws an error when the listing has a null item-------------------------------------------
//    @Test
//    void ensureCreateAndAddDirectSaleThrowsIfListingContainsNullItem() throws InstanceAlreadyExistsException {
//        Dimensions dimensions = new Dimensions(10, 20, 30);
//        Item item = controller.createAndAddItem(Condition.GOOD, 1.5, dimensions, publication1);
//
//        Price price = new Price(new BigDecimal("10.5"));
//
//        // Creates list with a valid item and other null
//        List<Item> items = new ArrayList<>();
//        items.add(null);
//
//        Listing listing = new Listing(items, price, "description", "title", user1);
//
//        assertThrows(IllegalArgumentException.class, () ->
////                controller.createAndAddDirectSale(listing)
//                controller.createAndAddDirectSale(List.of(listing), user1, false)
//        );
//    }

    // Test 7 - Ensures that returns false when a DirectSale is duplicated ---------------------------------------------
    @Test
    void ensureCreateAndAddDirectSaleReturnsFalseWhenDirectSaleAlreadyExists() throws Exception {
        Dimensions dimensions = new Dimensions(10, 20, 30);
        Item item = controller.createAndAddItem(Condition.GOOD, 1.5, dimensions, publication1);

        Price price = new Price(new BigDecimal("10.5"));
        Listing listing = new Listing("title", List.of(item),SaleType.DIRECT_SALE, price, "description", user1, List.of(new Picture("pic1")));

        // First time should return true
//        assertTrue(controller.createAndAddDirectSale(listing));
        assertTrue(controller.createAndAddDirectSale(List.of(listing), user1, false));

        // Second time should return false (duplicated)
//        boolean result = controller.createAndAddDirectSale(listing);
        boolean result = controller.createAndAddDirectSale(List.of(listing), user1, false);

        assertFalse(result);
    }
}