package TOPSECRET.domain;

import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private User buyer;
    private Cart cart;
    private Listing listing1;
    private Listing listing2;

    @BeforeEach
    void setUp() {
        buyer = new User();
        cart = new Cart(buyer);

        Profile profile = new Profile(
                "Tove Ditlevsen",
                new Country("DENMARK"),
                LocalDate.of(1917, 12, 14)
        );

        Author author = new Author(profile, "valid bio");

        Address address = new Address(
                "R Maria",
                "12",
                "156-5",
                new Country("PARAGUAY")
        );

        Email email = new Email(
                "Maria1",
                "gmail.com"
        );

        Contact contact = new Contact(
                "915945695",
                "+351",
                email,
                address
        );

        Publisher publisher = new Publisher(
                "Some Publisher",
                "PT123456789",
                contact
        );

        BibliographicalData bibliographicalData = new BibliographicalData(
                "Childhood, Youth, Dependency: The Copenhagen Trilogy",
                List.of(author),
                Locale.ENGLISH,
//                publisher,
                LocalDate.of(2020, 1, 1),
                List.of(new Genre ("Fiction")),
//                200,
                "Some text"
        );

        Dimensions dimensions = new Dimensions(10.0, 0.2, 5.2);
        List<Picture> pictures = List.of(new Picture("path/to/pic.jpg"));
        PublicationType publicationType = new PublicationType(
                TypeOfBinding.PAPERBACK,
                "Paperback"
        );
        User seller = new User("seller1", contact, profile, UserRole.NORMAL_USER);
        Code code = Code.fromIsbn13("9781234567890");
        // Edition
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(TypeOfBinding.CASE_BINDING, "Some details");
        Edition edition= new Edition(
                "1st",
                code,
                physicalAttributes,
                pubType,
                publisher,
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1)
        );

        Publication publication = new Publication(
                bibliographicalData,
                publicationType,
                seller,
                PublicationStatus.READING,
//                "1st edition",
                edition,
                code
        );


        // Edition
        Edition edition1= new Edition("1st",
                code,
                physicalAttributes,
                pubType, publisher,
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1));

        //PublicationSample
        PublicationSample item1 = new PublicationSample(
                bibliographicalData,
                seller,
                PublicationStatus.READ,
                edition1,
                Condition.LIKE_NEW);

        PublicationSample item2 = new PublicationSample(
                bibliographicalData,
                seller,
                PublicationStatus.READ,
                edition1,
                Condition.POOR);


        final List<PublicationSample> items1 = new ArrayList<>(List.of(item1));
        final List<PublicationSample> items2 = new ArrayList<>(List.of(item2));


        listing1 = new Listing(
                "Listing Title 1",
                items1,
                SaleType.DIRECT_SALE,
                new Price(new BigDecimal("50.00")),
                "Short description 1",
                pictures
        );

        listing2 = new Listing(
                "Listing Title 2",
                items2,
                SaleType.DIRECT_SALE,
                new Price(new BigDecimal("50.00")),
                "Short description 2",
                pictures
        );
    }

    // Cart must have a buyer
    @Test
    void cartShouldBeCreatedWithBuyer() {
        assertEquals(buyer, cart.getBuyer());
    }

    // Cart should start empty
    @Test
    void cartShouldStartEmpty() {
        assertTrue(cart.isEmpty());
    }

    // Cart should add listings
    @Test
    void cartShouldAddListing() {
        cart.addListing(listing1);

        assertEquals(1, cart.getListingsList().size());
        assertTrue(cart.getListingsList().contains(listing1));
    }

    // Cart shouldn't add null listings
    @Test
    void addingNullListingShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> cart.addListing(null));
    }

    // Cart should remove listings
    @Test
    void cartShouldRemoveListing() {
        cart.addListing(listing1);
        cart.addListing(listing2);
        cart.removeListing(listing2);

        assertEquals(1, cart.getListingsList().size());
        assertFalse(cart.getListingsList().contains(listing2));
    }

    // Cart should be empty after adding and removing listings
    @Test
    void cartIsEmptyAfterAddAndRemove() {
        assertTrue(cart.isEmpty());
        cart.addListing(listing1);
        assertFalse(cart.isEmpty());
        cart.removeListing(listing1);
        assertTrue(cart.isEmpty());
    }

    // Cart should add multiple listings
    @Test
    void cartShouldAddMultipleListings() {
        cart.addListing(listing1);
        cart.addListing(listing2);

        assertEquals(2, cart.getListingsList().size());
        assertTrue(cart.getListingsList().containsAll(List.of(listing1, listing2)));
    }

    // Cart should not allow add or removal after finished
    @Test
    void cartShouldNotAllowAddOrRemoveAfterFinish() {
        cart.addListing(listing1);
        cart.finish();

        assertTrue(cart.isFinished());

        assertThrows(IllegalStateException.class, () -> cart.addListing(listing2));
        assertThrows(IllegalStateException.class, () -> cart.removeListing(listing1));
    }

    // Finishing empty cart should throw exception
    @Test
    void finishingEmptyCartShouldThrowException() {
        assertTrue(cart.isEmpty());
        assertThrows(IllegalStateException.class, cart::finish);
    }

    // Finished cart should reflect cart state
    @Test
    void isFinishedShouldReflectCartState() {
        assertFalse(cart.isFinished());
        cart.addListing(listing1);
        cart.finish();
        assertTrue(cart.isFinished());
    }

    // Removing from empty cart should return false
    @Test
    void removingFromEmptyCartShouldReturnFalse() {
        assertTrue(cart.isEmpty());
        assertFalse(cart.removeListing(listing1));
    }
}