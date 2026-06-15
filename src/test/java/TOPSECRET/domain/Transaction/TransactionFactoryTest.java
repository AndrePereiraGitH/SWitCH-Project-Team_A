package TOPSECRET.domain.Transaction;

import TOPSECRET.domain.Enum.TransactionStatus;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TransactionFactoryTest {

    UserId buyer = mock(UserId.class);
    Price price = mock(Price.class);
    LocalDateTime transactionDate = LocalDateTime.now();
    Address shippingAddress = mock(Address.class);
    PaymentDetails paymentDetails = mock(PaymentDetails.class);
    TransactionStatus status = TransactionStatus.PENDING;
    ListingId listingId = mock(ListingId.class);
    List<ListingId> listings = List.of(listingId);

    @Test
    void createShouldCreateValidTransaction() {

        // Arrange
        TransactionFactory factory = new TransactionFactory();

        // Act
        Transaction transactionResult = factory.create(
                buyer,
                price,
                transactionDate,
                shippingAddress,
                paymentDetails,
                status,
                listings
        );

        // Assert
        assertNotNull(transactionResult);
        assertNotNull(transactionResult.id());
    }

    @Test
    void createShouldRecreateCorrectTransactionWithId() {

        // Arrange
        TransactionFactory factory = new TransactionFactory();

        TransactionID transactionId = mock(TransactionID.class);

        // Act
        Transaction transactionResult = factory.create(
                transactionId,
                buyer,
                price,
                transactionDate,
                shippingAddress,
                paymentDetails,
                status,
                listings
        );

        // Assert
        assertNotNull(transactionResult);
        assertSame(transactionId, transactionResult.id());
    }
}