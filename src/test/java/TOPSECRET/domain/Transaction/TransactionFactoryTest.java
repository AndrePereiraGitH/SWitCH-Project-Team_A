package TOPSECRET.domain.Transaction;

import TOPSECRET.domain.Enum.TransactionStatus;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link TransactionFactory}.
 *
 * <p>Verifies that the factory correctly creates new {@link Transaction} instances
 * with generated identifiers and the expected initial status, and that it correctly
 * reconstructs existing transactions from persisted data.</p>
 */
@ExtendWith(MockitoExtension.class)
class TransactionFactoryTest {

    @Test
    void createShouldCreateValidTransaction() {

        // ARRANGE
        TransactionFactory factory    = new TransactionFactory();
        UserId buyer                  = mock(UserId.class);
        Price price                   = mock(Price.class);
        Address shippingAddress       = mock(Address.class);
        PaymentDetails paymentDetails = mock(PaymentDetails.class);
        List<ListingId> listings      = List.of(mock(ListingId.class));
        LocalDateTime transactionDate = LocalDateTime.now();

        // ACT
        Transaction result = factory.create(
                buyer, price, transactionDate,
                shippingAddress, paymentDetails, listings
        );

        // ASSERT
        assertNotNull(result);
        assertNotNull(result.id());
    }

    @Test
    void createShouldCreateTransactionWithPendingStatus() {

        // ARRANGE
        TransactionFactory factory    = new TransactionFactory();
        UserId buyer                  = mock(UserId.class);
        Price price                   = mock(Price.class);
        Address shippingAddress       = mock(Address.class);
        PaymentDetails paymentDetails = mock(PaymentDetails.class);
        List<ListingId> listings      = List.of(mock(ListingId.class));
        LocalDateTime transactionDate = LocalDateTime.now();

        // ACT
        Transaction result = factory.create(
                buyer, price, transactionDate,
                shippingAddress, paymentDetails, listings
        );

        // ASSERT
        assertEquals(TransactionStatus.PENDING, result.getStatus());
    }

    @Test
    void createShouldRecreateCorrectTransactionWithId() {

        // ARRANGE
        TransactionFactory factory    = new TransactionFactory();
        TransactionID transactionId   = mock(TransactionID.class);
        UserId buyer                  = mock(UserId.class);
        Price price                   = mock(Price.class);
        Address shippingAddress       = mock(Address.class);
        PaymentDetails paymentDetails = mock(PaymentDetails.class);
        List<ListingId> listings      = List.of(mock(ListingId.class));
        LocalDateTime transactionDate = LocalDateTime.now();

        // ACT
        Transaction result = factory.create(
                transactionId, buyer, price, transactionDate,
                shippingAddress, paymentDetails,
                TransactionStatus.PENDING, listings
        );

        // ASSERT
        assertSame(transactionId, result.id());
    }
}