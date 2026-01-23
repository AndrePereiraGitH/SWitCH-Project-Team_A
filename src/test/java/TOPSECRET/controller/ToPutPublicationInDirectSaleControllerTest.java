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
    Publisher publisher1 = new Publisher("Some Publisher", "PT123456789", contact1);

    BibliographicalData bibliographicalData1 = new BibliographicalData(
            "Childhood, Youth, Dependency: The Copenhagen Trilogy",
            List.of(author1),
            Locale.ENGLISH,
            publisher1,
            LocalDate.of(2020, 1, 1),
            List.of(new Genre("Fiction")),
            200,
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
    String edition1 = "2nd Edition";

    Code code1 = Code.fromIsbn10("1234567890");

    PublicationType publicationType =
            new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Paperback");

    Publication publication1 = new Publication(
            bibliographicalData1, publicationType, user1, pubStatus1, edition1, code1
    );

    // ------------------------------------------------------

    @BeforeEach
    void setup() {
        // Arrange
        library = new Library("MyLibrary", user1);
        library.addPublication(publication1);

        directSaleRepo = new DirectSaleRepository(new ArrayList<>());
        itemRepo = new ItemRepository();

        controller = new ToPutPublicationInDirectSaleController(
                library,
                directSaleRepo,
                itemRepo
        );
    }

    // TEST 1 - getAllPublications -------------------------------------------------------------------------------------
    @Test
    void ensureGetAllPublicationsReturnsMappedDTOs() {
        List<LibraryPublicationDTO_US015> result = controller.getAllPublications();

        assertEquals(1, result.size());
        assertEquals("Childhood, Youth, Dependency: The Copenhagen Trilogy", result.get(0).pubTitle);
        assertEquals("1234567890", result.get(0).code);
        assertEquals(List.of("Tove Ditlevsen"), result.get(0).authorName);
        assertEquals("BOOK", result.get(0).publicationType);
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

    @Test
    void ensureCreateAndAddItemThrowsIfConditionIsNull() {
        Dimensions dimensions = new Dimensions(10, 20, 30);

        assertThrows(IllegalArgumentException.class, () ->
                controller.createAndAddItem(null, 1.5, dimensions, publication1)
        );
    }

    @Test
    void ensureCreateAndAddItemThrowsIfDimensionsIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                controller.createAndAddItem(Condition.GOOD, 1.5, null, publication1)
        );
    }

    @Test
    void ensureCreateAndAddItemThrowsIfPublicationIsNull() {
        Dimensions dimensions = new Dimensions(10, 20, 30);

        assertThrows(IllegalArgumentException.class, () ->
                controller.createAndAddItem(Condition.GOOD, 1.5, dimensions, null)
        );
    }

    @Test
    void ensureCreateAndAddItemThrowsIfWeightIsNotPositive() {
        Dimensions dimensions = new Dimensions(10, 20, 30);

        assertThrows(IllegalArgumentException.class, () ->
                controller.createAndAddItem(Condition.GOOD, 0, dimensions, publication1)
        );
    }

    // Test 3 - createAndAddDirectSale ---------------------------------------------------------------------------------
    @Test
    void ensureCreateAndAddDirectSaleCreatesAndStoresDirectSale() throws InstanceAlreadyExistsException {
        Dimensions dimensions = new Dimensions(10, 20, 30);
        Item item = controller.createAndAddItem(Condition.GOOD, 1.5, dimensions, publication1);

        Price price = new Price(new BigDecimal("10.5"));
        Listing listing = new Listing(List.of(item), price, "Short description", "Mag for sale like new", user1);

        boolean result = controller.createAndAddDirectSale(listing);

        assertTrue(result);
        assertEquals(1, directSaleRepo.getListOfItemsOnDirectSale(publication1).size());
    }

    @Test
    void ensureCreateAndAddDirectSaleThrowsIfListingIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                controller.createAndAddDirectSale(null)
        );
    }
}