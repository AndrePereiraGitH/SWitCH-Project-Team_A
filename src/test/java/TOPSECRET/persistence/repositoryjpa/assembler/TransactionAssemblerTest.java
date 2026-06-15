package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Enum.TransactionStatus;
import TOPSECRET.domain.Transaction.Transaction;
import TOPSECRET.domain.Transaction.TransactionFactory;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.persistence.repositoryjpa.datamodel.TransactionDM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link TransactionAssembler}.
 *
 * <p>This test class validates the behaviour of the assembler responsible for
 * converting between {@link Transaction} domain objects and
 * {@link TransactionDM} persistence entities.</p>
 *
 * <p>The following behaviours are covered:</p>
 * <ul>
 *     <li><b>Constructor validation</b> — verifies that the assembler rejects
 *     a {@code null} {@link TransactionFactory} dependency.</li>
 *
 *     <li><b>toDM conversion</b> — verifies that a domain object is correctly
 *     converted into a persistence data model using all values exposed through
 *     the domain getters.</li>
 *
 *     <li><b>toDomain conversion</b> — verifies that a persistence entity is
 *     correctly reconstructed into a domain object through the reconstruction
 *     overload of {@link TransactionFactory#create}.</li>
 *
 *     <li><b>Null guards</b> — verifies that all public methods reject
 *     {@code null} arguments with {@link NullPointerException}.</li>
 * </ul>
 *
 * <p>Mockito is used to isolate the assembler from both the persistence layer
 * and the domain construction logic. All collaborators are represented using
 * test doubles so the tests remain focused exclusively on mapping behaviour.</p>
 */
@ExtendWith(MockitoExtension.class)
class TransactionAssemblerTest {

    private TransactionID transactionIdDouble;
    private UserId buyerIdDouble;
    private Price priceDouble;
    private LocalDateTime transactionDate;
    private Address shippingAddressDouble;
    private PaymentDetails paymentDetailsDouble;
    private TransactionStatus status;
    private List<ListingId> listingIds;

    @Mock
    private TransactionFactory factoryDouble;

    @InjectMocks
    private TransactionAssembler assembler;

    @BeforeEach
    void setup() {
        transactionIdDouble    = mock(TransactionID.class);
        buyerIdDouble          = mock(UserId.class);
        priceDouble            = mock(Price.class);
        transactionDate        = LocalDateTime.now().minusMinutes(1);
        shippingAddressDouble  = mock(Address.class);
        paymentDetailsDouble   = mock(PaymentDetails.class);
        status                 = TransactionStatus.COMPLETED;
        listingIds             = List.of(mock(ListingId.class));
    }

    // =========================================================================
    // Constructor
    // =========================================================================

    @Test
    void constructor_shouldThrowNullPointerException_whenFactoryIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> new TransactionAssembler(null));
    }

    // =========================================================================
    // toDM
    // =========================================================================

    @Test
    void toDM_shouldReturnAValidDataModel() {

        // Arrange
        Transaction transactionDouble = mock(Transaction.class);

        when(transactionDouble.id()).thenReturn(transactionIdDouble);
        when(transactionDouble.getBuyerId()).thenReturn(buyerIdDouble);
        when(transactionDouble.getPrice()).thenReturn(priceDouble);
        when(transactionDouble.getTransactionDate()).thenReturn(transactionDate);
        when(transactionDouble.getShippingAddress()).thenReturn(shippingAddressDouble);
        when(transactionDouble.getPaymentDetails()).thenReturn(paymentDetailsDouble);
        when(transactionDouble.getStatus()).thenReturn(status);
        when(transactionDouble.getListingIds()).thenReturn(listingIds);

        // Act
        TransactionDM result = assembler.toDM(transactionDouble);

        // Assert
        assertNotNull(result);
        assertEquals(transactionIdDouble,   result.getTransactionId());
        assertEquals(buyerIdDouble,         result.getBuyerId());
        assertEquals(priceDouble,           result.getPrice());
        assertEquals(transactionDate,       result.getTransactionDate());
        assertEquals(shippingAddressDouble, result.getShippingAddress());
        assertEquals(paymentDetailsDouble,  result.getPaymentDetails());
        assertEquals(status,                result.getStatus());
        assertEquals(listingIds,            result.getListingIds());
    }

    @Test
    void toDM_shouldThrowNullPointerException_whenTransactionIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> assembler.toDM(null));
    }

    // =========================================================================
    // toDomain
    // =========================================================================

    @Test
    void toDomain_shouldReturnAValidDomainObject() {

        // Arrange
        TransactionDM dmDouble = mock(TransactionDM.class);

        when(dmDouble.getTransactionId()).thenReturn(transactionIdDouble);
        when(dmDouble.getBuyerId()).thenReturn(buyerIdDouble);
        when(dmDouble.getPrice()).thenReturn(priceDouble);
        when(dmDouble.getTransactionDate()).thenReturn(transactionDate);
        when(dmDouble.getShippingAddress()).thenReturn(shippingAddressDouble);
        when(dmDouble.getPaymentDetails()).thenReturn(paymentDetailsDouble);
        when(dmDouble.getStatus()).thenReturn(status);
        when(dmDouble.getListingIds()).thenReturn(listingIds);

        Transaction transactionDouble = mock(Transaction.class);

        when(factoryDouble.create(
                transactionIdDouble,
                buyerIdDouble,
                priceDouble,
                transactionDate,
                shippingAddressDouble,
                paymentDetailsDouble,
                status,
                listingIds
        )).thenReturn(transactionDouble);

        // Act
        Transaction result = assembler.toDomain(dmDouble);

        // Assert
        assertNotNull(result);
        assertEquals(transactionDouble, result);
    }

    @Test
    void toDomain_shouldThrowNullPointerException_whenTransactionDMIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> assembler.toDomain(null));
    }
}