package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.Enum.ListingStatus;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
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
    private UserId sellerId;
    private UserId buyerId;
    private LocalDate date;
    private ListingStatus listingStatus;

    @BeforeEach
    void setup() {
        listingId = mock(ListingId.class);
        pubId_1 = mock(PublicationSampleId.class);
        pubId_2 = mock(PublicationSampleId.class);
        oneItemList = List.of(pubId_1);
        severalItemList = List.of(pubId_1, pubId_2);
        sellerId = mock(UserId.class);
        buyerId = mock(UserId.class);
        date = LocalDate.now();
        listingStatus = ListingStatus.ACTIVE;
    }

    // =========================================================================
    // Constructor
    // =========================================================================

    @Test
    void shouldCreateListingWithOneItem() {

        // Act
        ListingDM dm = new ListingDM(
                listingId,
                description,
                oneItemList,
                sellerId,
                null,
                SaleType.DIRECT_SALE,
                title,
                pictures,
                price,
                date,
                listingStatus);

        // Assert
        assertNotNull(dm);
    }

    @Test
    void shouldCreateSoldListingWithBuyerId() {

        // Act
        ListingDM dm = new ListingDM(
                listingId,
                description,
                oneItemList,
                sellerId,
                buyerId,
                SaleType.DIRECT_SALE,
                title,
                pictures,
                price,
                date,
                ListingStatus.SOLD);

        // Assert
        assertNotNull(dm);
        assertEquals(buyerId, dm.getBuyerId());
        assertEquals(ListingStatus.SOLD, dm.getListingStatus());
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
                new ListingDM(null,
                        description,
                        oneItemList,
                        sellerId,
                        buyerId,
                        SaleType.DIRECT_SALE,
                        title,
                        pictures,
                        price,
                        date,
                        listingStatus));
    }

    @Test
    void shouldThrowWhenDescriptionIsNull() {

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new ListingDM(
                        listingId,
                        null,
                        oneItemList,
                        sellerId,
                        buyerId,
                        SaleType.DIRECT_SALE,
                        title,
                        pictures,
                        price,
                        date,
                        listingStatus));
    }

    @Test
    void shouldThrowWhenItemsIdIsNull() {

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new ListingDM(
                        listingId,
                        description,
                        null,
                        sellerId,
                        buyerId,
                        SaleType.DIRECT_SALE,
                        title,
                        pictures,
                        price,
                        date,
                        listingStatus));
    }

    @Test
    void shouldThrowWhenSellerIdIsNull() {

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new ListingDM(
                        listingId,
                        description,
                        oneItemList,
                        null,
                        buyerId,
                        SaleType.DIRECT_SALE,
                        title,
                        pictures,
                        price,
                        date,
                        listingStatus));
    }

    @Test
    void shouldThrowWhenSaleTypeIsNull() {

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new ListingDM(
                        listingId,
                        description,
                        oneItemList,
                        sellerId,
                        buyerId,
                        null,
                        title,
                        pictures,
                        price,
                        date,
                        listingStatus));
    }

    @Test
    void shouldThrowWhenTitleIsNull() {

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new ListingDM(
                        listingId,
                        description,
                        oneItemList,
                        sellerId,
                        buyerId,
                        SaleType.DIRECT_SALE,
                        null,
                        pictures,
                        price,
                        date,
                        listingStatus));
    }

    @Test
    void shouldThrowWhenPicturesIsNull() {

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new ListingDM(
                        listingId,
                        description,
                        oneItemList,
                        sellerId,
                        buyerId,
                        SaleType.DIRECT_SALE,
                        title,
                        null,
                        price,
                        date,
                        listingStatus));
    }

    @Test
    void shouldThrowWhenPriceIsNull() {

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new ListingDM(
                        listingId,
                        description,
                        oneItemList,
                        sellerId,
                        buyerId,
                        SaleType.DIRECT_SALE,
                        title,
                        pictures,
                        null,
                        date,
                        listingStatus));
    }

    @Test
    void shouldThrowWhenPublicationDateIsNull() {

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new ListingDM(
                        listingId,
                        description,
                        oneItemList,
                        sellerId,
                        buyerId,
                        SaleType.DIRECT_SALE,
                        title,
                        pictures,
                        price,
                        null,
                        listingStatus));
    }

    @Test
    void shouldThrowWhenListingStatusIsNull() {

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new ListingDM(
                        listingId,
                        description,
                        oneItemList,
                        sellerId,
                        buyerId,
                        SaleType.DIRECT_SALE,
                        title,
                        pictures,
                        price,
                        date,
                        null));
    }

    @Test
    void shouldNotThrowWhenBuyerIdIsNull() {

        // Act
        ListingDM dm = new ListingDM(
                listingId,
                description,
                oneItemList,
                sellerId,
                null,
                SaleType.DIRECT_SALE,
                title,
                pictures,
                price,
                date,
                listingStatus);

        // Assert
        assertNull(dm.getBuyerId());
    }

    // =========================================================================
    // Getters
    // =========================================================================

    @Test
    void getListingId_shouldReturnListingId() {
        // Arrange
        ListingDM dm = new ListingDM(
                listingId,
                description,
                severalItemList,
                sellerId,
                buyerId,
                SaleType.DIRECT_SALE,
                title,
                pictures,
                price,
                date,
                listingStatus);

        // Act
        ListingId result = dm.getListingId();

        // Assert
        assertEquals(listingId, result);
    }

    @Test
    void getDescription_shouldReturnDescription() {
        // Arrange
        ListingDM dm = new ListingDM(
                listingId,
                description,
                severalItemList,
                sellerId,
                buyerId,
                SaleType.DIRECT_SALE,
                title,
                pictures,
                price,
                date,
                listingStatus);

        // Act
        String result = dm.getShortDescription();

        // Assert
        assertEquals(description, result);
    }

    @Test
    void getSellerId_shouldReturnSellerId() {
        // Arrange
        ListingDM dm = new ListingDM(
                listingId,
                description,
                severalItemList,
                sellerId,
                buyerId,
                SaleType.DIRECT_SALE,
                title,
                pictures,
                price,
                date,
                listingStatus);

        // Act
        UserId result = dm.getSellerId();

        // Assert
        assertEquals(sellerId, result);
    }

    @Test
    void getBuyerId_shouldReturnBuyerId() {
        // Arrange
        ListingDM dm = new ListingDM(
                listingId,
                description,
                severalItemList,
                sellerId,
                buyerId,
                SaleType.DIRECT_SALE,
                title,
                pictures,
                price,
                date,
                listingStatus);

        // Act
        UserId result = dm.getBuyerId();

        // Assert
        assertEquals(buyerId, result);
    }

    @Test
    void getSaleType_shouldReturnSaleType() {
        // Arrange
        ListingDM dm = new ListingDM(
                listingId,
                description,
                severalItemList,
                sellerId,
                buyerId,
                SaleType.DIRECT_SALE,
                title,
                pictures,
                price,
                date,
                listingStatus);

        // Act
        SaleType result = dm.getSaleType();

        // Assert
        assertEquals(SaleType.DIRECT_SALE, result);
    }

    @Test
    void getTitle_shouldReturnTitle() {
        // Arrange
        ListingDM dm = new ListingDM(
                listingId,
                description,
                severalItemList,
                sellerId,
                buyerId,
                SaleType.DIRECT_SALE,
                title,
                pictures,
                price,
                date,
                listingStatus);

        // Act
        String result = dm.getListingTitle();

        // Assert
        assertEquals(title, result);
    }

    @Test
    void getPictures_shouldReturnPictures() {
        // Arrange
        ListingDM dm = new ListingDM(
                listingId,
                description,
                severalItemList,
                sellerId,
                buyerId,
                SaleType.DIRECT_SALE,
                title,
                pictures,
                price,
                date,
                listingStatus);

        // Act
        List<String> result = dm.getPictures();

        // Assert
        assertEquals(pictures, result);
    }

    @Test
    void getPrice_shouldReturnPrice() {
        // Arrange
        ListingDM dm = new ListingDM(
                listingId,
                description,
                severalItemList,
                sellerId,
                buyerId,
                SaleType.DIRECT_SALE,
                title,
                pictures,
                price,
                date,
                listingStatus);

        // Act
        String result = dm.getPrice();

        // Assert
        assertEquals(price, result);
    }


    @Test
    void getItemsId_shouldReturnItemsId() {
        // Arrange
        ListingDM dm = new ListingDM(
                listingId,
                description,
                severalItemList,
                sellerId,
                buyerId,
                SaleType.DIRECT_SALE,
                title,
                pictures,
                price,
                date,
                listingStatus);

        // Act
        List<PublicationSampleId> result = dm.getItemsId();

        // Assert
        assertEquals(severalItemList, result);
    }

    @Test
    void getPublicationDate_shouldReturnDate() {
        // Arrange
        ListingDM dm = new ListingDM(
                listingId,
                description,
                severalItemList,
                sellerId,
                buyerId,
                SaleType.DIRECT_SALE,
                title,
                pictures,
                price,
                date,
                listingStatus);

        // Act
        LocalDate result = dm.getPublicationDate();

        // Assert
        assertEquals(date, result);
    }

    @Test
    void getListingStatus_shouldReturnStatus() {
        // Arrange
        ListingDM dm = new ListingDM(
                listingId,
                description,
                severalItemList,
                sellerId,
                buyerId,
                SaleType.DIRECT_SALE,
                title,
                pictures,
                price,
                date,
                listingStatus);

        // Act
        ListingStatus result = dm.getListingStatus();

        // Assert
        assertEquals(listingStatus, result);
    }
}