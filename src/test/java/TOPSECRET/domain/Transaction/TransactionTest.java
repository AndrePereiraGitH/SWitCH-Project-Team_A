package TOPSECRET.domain.Transaction;

import TOPSECRET.domain.Enum.TransactionStatus;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionTest {

    TransactionID id;
    UserId buyerId;
    Price price;
    LocalDateTime transactionDate;
    Address shippingAddress;
    PaymentDetails paymentDetails;
    TransactionStatus status;
    ListingId listingId;
    List<ListingId> listings;

    @BeforeEach
    void setUp() {
        id = mock(TransactionID.class);
        buyerId = mock(UserId.class);
        price = mock(Price.class);
        transactionDate = LocalDateTime.now();
        shippingAddress = mock(Address.class);
        paymentDetails = mock(PaymentDetails.class);
        status = TransactionStatus.PENDING;
        listingId = mock(ListingId.class);
        listings = List.of(listingId);
    }

    // =========================================================
    // ======================= CREATE ==========================
    // =========================================================

    @Test
    void shouldCreateTransactionSuccessfully() {
        // Act
        Transaction transaction = new Transaction(buyerId, price, transactionDate, shippingAddress,
                paymentDetails, status, listings
        );

        // Assert
        assertEquals(buyerId, transaction.getBuyerId());
        assertEquals(price, transaction.getPrice());
        assertEquals(transactionDate, transaction.getTransactionDate());
        assertEquals(shippingAddress, transaction.getShippingAddress());
        assertEquals(paymentDetails, transaction.getPaymentDetails());
        assertEquals(status, transaction.getStatus());
        assertEquals(listings, transaction.getListingIds());
    }

    @Test
    void shouldThrowWhenBuyerIdIsNull() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(null, price, transactionDate, shippingAddress,
                        paymentDetails, status, listings)
        );
    }

    @Test
    void shouldThrowWhenPriceIsNull() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(buyerId, null, transactionDate, shippingAddress,
                        paymentDetails, status, listings)
        );
    }

    @Test
    void shouldThrowWhenTransactionDateIsNull() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(buyerId, price, null, shippingAddress,
                        paymentDetails, status, listings)
        );
    }

    @Test
    void shouldThrowWhenTransactionDateIsFuture() {

        // Arrange
        LocalDateTime future = LocalDateTime.now().plusDays(1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(buyerId, price, future, shippingAddress,
                        paymentDetails, status, listings)
        );
    }

    @Test
    void shouldThrowWhenShippingAddressIsNull() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(buyerId, price, transactionDate, null,
                        paymentDetails, status, listings)
        );
    }


    @Test
    void shouldThrowWhenPaymentDetailsIsNull() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(buyerId, price, transactionDate, shippingAddress,
                        null, status, listings)
        );
    }


    @Test
    void shouldThrowWhenListingsIsNull() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(buyerId, price, transactionDate, shippingAddress,
                        paymentDetails, status, null)
        );
    }


    // =========================================================
    // ================== CREATE WITH ID =======================
    // =========================================================


    @Test
    void shouldRecreateTransactionSuccessfully() {
        // Act
        Transaction transaction = new Transaction(id, buyerId, price, transactionDate, shippingAddress,
                paymentDetails, status, listings
        );

        // Assert
        assertEquals(id, transaction.id());
        assertEquals(buyerId, transaction.getBuyerId());
        assertEquals(price, transaction.getPrice());
        assertEquals(transactionDate, transaction.getTransactionDate());
        assertEquals(shippingAddress, transaction.getShippingAddress());
        assertEquals(paymentDetails, transaction.getPaymentDetails());
        assertEquals(status, transaction.getStatus());
        assertEquals(listings, transaction.getListingIds());
    }

    @Test
    void shouldThrowWhenIdIsNull_withId() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(null, buyerId, price, transactionDate, shippingAddress,
                        paymentDetails, status, listings)
        );
    }

    @Test
    void shouldThrowWhenBuyerIdIsNull_withId() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(id, null, price, transactionDate, shippingAddress,
                        paymentDetails, status, listings)
        );
    }

    @Test
    void shouldThrowWhenPriceIsNull_withId() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(id, buyerId, null, transactionDate, shippingAddress,
                        paymentDetails, status, listings)
        );
    }

    @Test
    void shouldThrowWhenTransactionDateIsNull_withId() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(id, buyerId, price, null, shippingAddress,
                        paymentDetails, status, listings)
        );
    }

    @Test
    void shouldThrowWhenTransactionDateIsFuture_withId() {

        // Arrange
        LocalDateTime future = LocalDateTime.now().plusDays(1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(id, buyerId, price, future, shippingAddress,
                        paymentDetails, status, listings)
        );
    }

    @Test
    void shouldThrowWhenShippingAddressIsNull_withId() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(id, buyerId, price, transactionDate, null,
                        paymentDetails, status, listings)
        );
    }


    @Test
    void shouldThrowWhenPaymentDetailsIsNull_withId() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(id, buyerId, price, transactionDate, shippingAddress,
                        null, status, listings)
        );
    }


    @Test
    void shouldThrowWhenListingsIsNull_withId() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(id, buyerId, price, transactionDate, shippingAddress,
                        paymentDetails, status, null)
        );
    }


    // =========================================================
    // ====================== SAME AS ==========================
    // =========================================================

    @Test
    void sameAsShouldReturnTrueForSameData() {

        // Arrange
        Transaction transaction1 = new Transaction(id, buyerId, price, transactionDate, shippingAddress,
                paymentDetails, status, listings
        );

        Transaction transaction2 = new Transaction(id, buyerId, price, transactionDate, shippingAddress,
                paymentDetails, status, listings
        );

        // Act + assert
        assertTrue(transaction1.sameAs(transaction2));
    }

    @Test
    void sameAsShouldReturnFalseWhenBuyerDiffers() {

        //Arrange
        UserId otherBuyer = mock(UserId.class);

        Transaction transaction1 = new Transaction(id, buyerId, price, transactionDate, shippingAddress,
                paymentDetails, status, listings
        );

        Transaction transaction2 = new Transaction(id, otherBuyer, price, transactionDate, shippingAddress,
                paymentDetails, status, listings
        );

        //Act + assert
        assertFalse(transaction1.sameAs(transaction2));
    }

    @Test
    void sameAsShouldReturnFalseWhenStatusDiffers() {

        //Arrange
        Transaction transaction1 = new Transaction(id, buyerId, price, transactionDate, shippingAddress,
                paymentDetails, status, listings
        );

        Transaction transaction2 = new Transaction(id, buyerId, price, transactionDate, shippingAddress,
                paymentDetails, TransactionStatus.CANCELED, listings
        );

        //Act + assert
        assertFalse(transaction1.sameAs(transaction2));
    }

    @Test
    void sameAs_shouldReturnFalseWhenComparedWithNull() {
        //Arrange
        Transaction transaction = new Transaction(id, buyerId, price, transactionDate, shippingAddress,
                paymentDetails, status, listings
        );

        assertFalse(transaction.sameAs(null));
    }

    // =========================================================
    // ====================== HASH CODE ========================
    // =========================================================

    @Test
    void hashCode_shouldBeBasedOnId() {
        // Arrange
        Transaction transaction = new Transaction(id, buyerId, price, transactionDate, shippingAddress,
                paymentDetails, status, listings
        );

        // Assert
        assertEquals(id.hashCode(), transaction.hashCode());
    }


    // =========================================================
    // ======================= EQUALS ==========================
    // =========================================================

    @Test
    void equalsShouldReturnFalseForDifferentTransactions() {

        //Arrange
        Transaction transaction1 = new Transaction(buyerId, price, transactionDate, shippingAddress,
                paymentDetails, status, listings
        );

        Transaction transaction2 = new Transaction(buyerId, price, transactionDate, shippingAddress,
                paymentDetails, status, listings
        );

        //Act + assert
        assertFalse(transaction1.equals(transaction2));
    }

    @Test
    void equalsShouldReturnFalseForDifferentObjectType() {

        //Arrange
        Transaction transaction = new Transaction(id, buyerId, price, transactionDate, shippingAddress,
                paymentDetails, status, listings
        );

        //Act + assert
        assertFalse(transaction.equals("this is not a transaction"));
    }

}