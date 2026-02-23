package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.repository.DirectSaleRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static TOPSECRET.domain.Enum.Countries.PORTUGAL;
import static org.junit.jupiter.api.Assertions.*;

class ToGetListOfItemsOnDirectSaleOfGivenPublisherControllerTest {

    private ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller;
    private DirectSaleRepository repository;

    private Publisher publisherA;
    private Publisher publisherB;

    @BeforeEach
    void setUp() {

        publisherA = new Publisher("Publisher A", "VAT-A", null);
        publisherB = new Publisher("Publisher B", "VAT-B", null);

        Publication publicationA = publicationWithPublisher(publisherA);
        Publication publicationB = publicationWithPublisher(publisherB);

        Item itemA1 = item(publicationA);
        Item itemA2 = item(publicationA);
        Item itemB = item(publicationB);

//        Listing listing1 = listingWithItems(itemA1, itemB);
//        Listing listing2 = listingWithItems(itemA2);

        User buyer = new User (
                "Buyer name", 
                new Contact("919999999","+351",new Email ("buyer", "gmail.com"), null),
                new Profile("Buyer name", new Country("Portugal"), LocalDate.of(1999,12,01)),
                UserRole.NORMAL_USER
        );

//        DirectSale ds1 = new DirectSale(listing1, LocalDateTime.now());
//        DirectSale ds2 = new DirectSale(listing2, LocalDateTime.now());
//        DirectSale ds1 = new DirectSale(List.of(listing1), buyer, false);
//        DirectSale ds2 = new DirectSale(List.of(listing2), buyer, false);

//        repository = new DirectSaleRepository(List.of(ds1, ds2));
        controller = new ToGetListOfItemsOnDirectSaleOfGivenPublisherController(repository);
    }

//    @Test
//    void constructor_shouldThrowExceptionWhenNullDirectSaleRepository() {
//        // Arrange
//        DirectSaleRepository directSaleRepository = null;
//
//        // Act & Assert
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ToGetListOfItemsOnDirectSaleOfGivenPublisherController(directSaleRepository));
//        assertEquals("directSaleRepository cannot be null", exception.getMessage());
//    }

//    @Test
//    void constructor_doesNotThrownWhenValidDirectSaleRepository() {
//        // Arrange
//        DirectSaleRepository directSaleRepository = new DirectSaleRepository(List.of());
//
//        // Act & Assert
//        assertDoesNotThrow(() -> new ToGetListOfItemsOnDirectSaleOfGivenPublisherController(directSaleRepository));
//    }

//    @Test
//    void getItemsOnDirectSaleByPublisher_shouldReturnEmptyListWhenNullPublisher() {
//        List<Item> result = controller.getItemsOnDirectSaleByPublisher(null);
//
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//    }

//    @Test
//    void getItemsOnDirectSaleByPublisher_shouldReturnEmptyListWhenNoMatchingPublisher() {
//        Publisher unknownPublisher =
//                new Publisher("Unknown", "VAT-X", null);
//
//        List<Item> result =
//                controller.getItemsOnDirectSaleByPublisher(unknownPublisher);
//
//        assertTrue(result.isEmpty());
//    }

//    @Test
//    void getItemsOnDirectSaleByPublisher_shouldReturnsItemsWhenMatchingPublisher() {
//        List<Item> result =
//                controller.getItemsOnDirectSaleByPublisher(publisherA);
//
//        assertEquals(2, result.size());
//        result.forEach(item ->
////                assertEquals(publisherA,
////                        item.getPublication()
////                                .getBibliographicalData()
////                                .getPublisher()));
//                assertEquals(publisherA,
//                        item.getPublication()
//                                .getEdition()
//                                .getPublisher()));
//    }

    private static Publication publicationWithPublisher(Publisher publisher) {
        Country country = new Country("Portugal");
        Profile profile = new Profile("Profile", country, LocalDate.now());
        Author author = new Author(profile, "bio");
        Genre genre = new Genre("Genre");

        BibliographicalData data =
                new BibliographicalData(
                        "Title",
                        List.of(author),
                        Locale.getDefault(),
//                        publisher,
                        LocalDate.now(),
                        List.of(genre),
//                        100,
                        "ChunksOfText"
                );

        PublicationType publicationType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "details");
        Address address = new Address("street", "number", "zipCode", country);
        Email email = new Email("userName", "domain.com");
        Contact contact = new Contact("915945695", "+351", email, address);
        User user = new User("userName", contact, profile, UserRole.NORMAL_USER);
        Code code = Code.fromIsbn13("isbn13");

        // Edition
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
        Edition edition= new Edition(
                "1st",
                code,
                physicalAttributes,
                pubType,
                publisher,
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1)
        );
//        return new Publication(data, publicationType, user, PublicationStatus.FOR_SALE, "edition", code);
        return new Publication(data, publicationType, user, PublicationStatus.FOR_SALE, edition, code);
    }

    private static Item item(Publication publication) {
        return new Item(
                Condition.NEW,
                1.0,
                new Dimensions(10, 10, 10),
                publication
        );
    }

//    private static Listing listingWithItems(Item... items) {
//        Price price = new Price(new BigDecimal(10));
//        Country country = new Country("Portugal");
//        Address address = new Address("street", "number", "zipCode", country);
//        Email email = new Email("userName", "domain.com");
//        Contact contact = new Contact("915945695", "+351", email, address);
//        Profile profile = new Profile("Profile", country, LocalDate.now());
//        User user = new User("userName", contact, profile, UserRole.NORMAL_USER);
//        Picture picture = new Picture("picture");
//
//        return new Listing("listingTitle", Arrays.stream(items).toList(), SaleType.DIRECT_SALE, price, "shortDescription", user, List.of(picture));
//    }
}
