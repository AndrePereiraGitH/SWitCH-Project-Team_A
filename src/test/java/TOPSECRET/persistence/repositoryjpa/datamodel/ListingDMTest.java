package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link ListingDM}.
 *
 * <p>Covers:
 * <ul>
 *   <li>Constructor - valid creation and null guard validation</li>
 *   <li>No-arg constructor - JPA protected access enforcement</li>
 *   <li>Getters - correct field retrieval after construction</li>
 * </ul>
 *
 * <p>Uses doubles for {@link ListingId}, {@link UserId},
 * and {@link PublicationSampleId} to isolate the data model
 * from domain value object implementations.
 */
class ListingDMTest {

    private ListingId listingId;
    private final String description = "Valid Description";
    private final String title = "PS5 Pro";
    private String price = "50";
    private List<String> pictures = List.of("pic1", "pic2");
    private List<PublicationSampleId> oneItemList;
    private List<PublicationSampleId> severalItemList;
    private PublicationSampleId pubId_1;
    private PublicationSampleId pubId_2;
    private UserId userId;

    @BeforeEach
    void setup() {
        listingId = mock(ListingId.class);
        pubId_1 = mock(PublicationSampleId.class);
        pubId_2 = mock(PublicationSampleId.class);
        oneItemList = List.of(pubId_1);
        severalItemList = List.of(pubId_1, pubId_2);
        userId = mock(UserId.class);
    }

    // =========================================================================
    // Constructor
    // =========================================================================

    @Test
    void shouldCreateListingWithOneItem() {

        // Act
        ListingDM dm = new ListingDM(listingId, description,
                oneItemList, userId, SaleType.DIRECT_SALE, title,
                pictures, price);

        // Assert
        assertNotNull(dm);
    }

    @Test
    void noArgConstructor_shouldBeProtected() throws NoSuchMethodException {
        // Arrange
        Constructor<ListingDM> constructor =
                ListingDM.class.getDeclaredConstructor();

        // Act
        int modifiers = constructor.getModifiers();

        // Assert
        assertTrue(Modifier.isProtected(modifiers));
    }

    @Test
    void shouldThrowWhenListingIdIsNull() {

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new ListingDM(null, description, oneItemList,
                        userId, SaleType.DIRECT_SALE, title,
                        pictures, price));
    }

    @Test
    void shouldThrowWhenDescriptionIsNull() {

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new ListingDM(listingId, null, oneItemList,
                        userId, SaleType.DIRECT_SALE, title,
                        pictures, price));
    }

    @Test
    void shouldThrowWhenItemsIdIsNull() {

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new ListingDM(listingId, description, null,
                        userId, SaleType.DIRECT_SALE, title,
                        pictures, price));
    }

    @Test
    void shouldThrowWhenSellerIdIsNull() {

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new ListingDM(listingId, description, oneItemList,
                        null, SaleType.DIRECT_SALE, title,
                        pictures, price));
    }

    @Test
    void shouldThrowWhenSaleTypeIsNull() {

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new ListingDM(listingId, description, oneItemList,
                        userId, null, title,
                        pictures, price));
    }

    @Test
    void shouldThrowWhenTitleIsNull() {

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new ListingDM(listingId, description, oneItemList,
                        userId, SaleType.DIRECT_SALE, null,
                        pictures, price));
    }

    @Test
    void shouldThrowWhenPicturesIsNull() {

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new ListingDM(listingId, description, oneItemList,
                        userId, SaleType.DIRECT_SALE, title,
                        null, price));
    }

    @Test
    void shouldThrowWhenPriceIsNull() {

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new ListingDM(listingId, description, oneItemList,
                        userId, SaleType.DIRECT_SALE, title,
                        pictures, null));
    }

    // =========================================================================
    // Getters
    // =========================================================================

    @Test
    void getListingId_shouldReturnListingId() {
        // Arrange
        ListingDM dm = new ListingDM(listingId, description,
                severalItemList, userId, SaleType.DIRECT_SALE, title,
                pictures, price);

        // Act
        ListingId result = dm.get_listingId();

        // Assert
        assertEquals(listingId, result);
    }

    @Test
    void getDescription_shouldReturnDescription() {
        // Arrange
        ListingDM dm = new ListingDM(listingId, description,
                severalItemList, userId, SaleType.DIRECT_SALE, title,
                pictures, price);

        // Act
        String result = dm.get_shortDescription();

        // Assert
        assertEquals(description, result);
    }

    @Test
    void getUserId_shouldReturnUserId() {
        // Arrange
        ListingDM dm = new ListingDM(listingId, description,
                severalItemList, userId, SaleType.DIRECT_SALE, title,
                pictures, price);

        // Act
        UserId result = dm.get_sellerId();

        // Assert
        assertEquals(userId, result);
    }

    @Test
    void getSaleType_shouldReturnSaleType() {
        // Arrange
        ListingDM dm = new ListingDM(listingId, description,
                severalItemList, userId, SaleType.DIRECT_SALE, title,
                pictures, price);

        // Act
        SaleType result = dm.get_saleType();

        // Assert
        assertEquals(SaleType.DIRECT_SALE, result);
    }

    @Test
    void getTitle_shouldReturnTitle() {
        // Arrange
        ListingDM dm = new ListingDM(listingId, description,
                severalItemList, userId, SaleType.DIRECT_SALE, title,
                pictures, price);

        // Act
        String result = dm.get_listingTitle();

        // Assert
        assertEquals(title, result);
    }

    @Test
    void getPictures_shouldReturnPictures() {
        // Arrange
        ListingDM dm = new ListingDM(listingId, description,
                severalItemList, userId, SaleType.DIRECT_SALE, title,
                pictures, price);

        // Act
        List<String> result = dm.get_pictures();

        // Assert
        assertEquals(pictures, result);
    }

    @Test
    void getPrice_shouldReturnPrice() {
        // Arrange
        ListingDM dm = new ListingDM(listingId, description,
                severalItemList, userId, SaleType.DIRECT_SALE, title,
                pictures, price);

        // Act
        String result = dm.get_price();

        // Assert
        assertEquals(price, result);
    }

    @Test
    void getItemsId_shouldReturnItemsId() {
        // Arrange
        ListingDM dm = new ListingDM(listingId, description,
                severalItemList, userId, SaleType.DIRECT_SALE, title,
                pictures, price);

        // Act
        List<PublicationSampleId> result = dm.get_itemsId();

        // Assert
        assertEquals(severalItemList, result);
    }
}