package TOPSECRET.services;

import TOPSECRET.domain.Enum.TransactionStatus;
import TOPSECRET.domain.Transaction.ITransactionRepository;
import TOPSECRET.domain.Transaction.Transaction;
import TOPSECRET.domain.Transaction.TransactionFactory;
import TOPSECRET.domain.ValueObjects.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link TransactionService}.
 *
 * <p>Verifies the service's behaviour in isolation by mocking both the
 * repository ({@link ITransactionRepository}) and the factory ({@link TransactionFactory}).
 * Tests are grouped by method under test.</p>
 *
 * <p>Each test follows the Arrange / Act / Assert structure and exercises
 * a single logical scenario.</p>
 */
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private ITransactionRepository transactionRepository;

    @Mock
    private TransactionFactory transactionFactory;

    @InjectMocks
    private TransactionService sut;

    private UserId buyerIdDouble;
    private Price priceDouble;
    private LocalDateTime transactionDate;
    private Address shippingAddressDouble;
    private PaymentDetails paymentDetailsDouble;
    private List<ListingId> listingIds;
    private TransactionID transactionIdDouble;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(ITransactionRepository.class);
        transactionFactory    = mock(TransactionFactory.class);

        sut = new TransactionService(transactionRepository, transactionFactory);

        buyerIdDouble        = mock(UserId.class);
        priceDouble          = mock(Price.class);
        transactionDate      = LocalDateTime.now().minusMinutes(1);
        shippingAddressDouble = mock(Address.class);
        paymentDetailsDouble = mock(PaymentDetails.class);
        listingIds           = List.of(mock(ListingId.class));
        transactionIdDouble  = mock(TransactionID.class);
    }

    // =========================================================================
    // Constructor
    // =========================================================================

    @Test
    void constructor_shouldThrowNullPointerException_whenRepositoryIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> new TransactionService(null, transactionFactory));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenFactoryIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> new TransactionService(transactionRepository, null));
    }

    // =========================================================================
    // createAndSave
    // =========================================================================

    @Test
    void createAndSave_shouldReturnSavedTransaction_whenAllArgumentsAreValid() {

        // Arrange
        Transaction createdTransaction = mock(Transaction.class);
        Transaction savedTransaction   = mock(Transaction.class);

        when(transactionFactory.create(
                buyerIdDouble,
                priceDouble,
                transactionDate,
                shippingAddressDouble,
                paymentDetailsDouble,
                TransactionStatus.COMPLETED,
                listingIds
        )).thenReturn(createdTransaction);

        when(transactionRepository.save(createdTransaction)).thenReturn(savedTransaction);

        // Act
        Transaction result = sut.createAndSave(
                buyerIdDouble, priceDouble, transactionDate,
                shippingAddressDouble, paymentDetailsDouble, listingIds);

        // Assert
        assertSame(savedTransaction, result);
    }

    @Test
    void createAndSave_shouldPersistTransactionWithCompletedStatus() {

        // Arrange
        Transaction createdTransaction = mock(Transaction.class);
        Transaction savedTransaction   = mock(Transaction.class);

        when(transactionFactory.create(
                eq(buyerIdDouble),
                eq(priceDouble),
                eq(transactionDate),
                eq(shippingAddressDouble),
                eq(paymentDetailsDouble),
                eq(TransactionStatus.COMPLETED),
                eq(listingIds)
        )).thenReturn(createdTransaction);

        when(transactionRepository.save(createdTransaction)).thenReturn(savedTransaction);

        // Act
        Transaction result = sut.createAndSave(
                buyerIdDouble, priceDouble, transactionDate,
                shippingAddressDouble, paymentDetailsDouble, listingIds);

        // Assert
        assertNotNull(result);
    }

    // =========================================================================
    // findById
    // =========================================================================

    @Test
    void findById_shouldReturnTransaction_whenTransactionExists() {

        // Arrange
        Transaction transaction = mock(Transaction.class);

        when(transactionRepository.findById(transactionIdDouble))
                .thenReturn(Optional.of(transaction));

        // Act
        Transaction result = sut.findById(transactionIdDouble);

        // Assert
        assertSame(transaction, result);
    }

    @Test
    void findById_shouldThrowEntityNotFoundException_whenTransactionDoesNotExist() {

        // Arrange
        when(transactionRepository.findById(transactionIdDouble))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EntityNotFoundException.class,
                () -> sut.findById(transactionIdDouble));
    }

    // =========================================================================
    // findAll
    // =========================================================================

    @Test
    void findAll_shouldReturnAllTransactions() {

        // Arrange
        Transaction tx1 = mock(Transaction.class);
        Transaction tx2 = mock(Transaction.class);
        List<Transaction> transactions = List.of(tx1, tx2);

        when(transactionRepository.findAll()).thenReturn(transactions);

        // Act
        List<Transaction> result = sut.findAll();

        // Assert
        assertSame(transactions, result);
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoTransactionsExist() {

        // Arrange
        when(transactionRepository.findAll()).thenReturn(List.of());

        // Act
        List<Transaction> result = sut.findAll();

        // Assert
        assertTrue(result.isEmpty());
    }

    // =========================================================================
    // findAllByBuyerId
    // =========================================================================

    @Test
    void findAllByBuyerId_shouldReturnAllTransactions_whenBuyerHasMultiple() {

        // Arrange
        Transaction tx1 = mock(Transaction.class);
        Transaction tx2 = mock(Transaction.class);
        List<Transaction> transactions = List.of(tx1, tx2);

        when(transactionRepository.findAllByBuyerId(buyerIdDouble))
                .thenReturn(transactions);

        // Act
        List<Transaction> result = sut.findAllByBuyerId(buyerIdDouble);

        // Assert
        assertSame(transactions, result);
    }

    @Test
    void findAllByBuyerId_shouldReturnOneTransaction_whenBuyerHasOne() {

        // Arrange
        Transaction tx = mock(Transaction.class);

        when(transactionRepository.findAllByBuyerId(buyerIdDouble))
                .thenReturn(List.of(tx));

        // Act
        List<Transaction> result = sut.findAllByBuyerId(buyerIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertSame(tx, result.get(0));
    }

    @Test
    void findAllByBuyerId_shouldReturnEmptyList_whenBuyerHasNoTransactions() {

        // Arrange
        when(transactionRepository.findAllByBuyerId(buyerIdDouble))
                .thenReturn(List.of());

        // Act
        List<Transaction> result = sut.findAllByBuyerId(buyerIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllByBuyerId_shouldThrowNullPointerException_whenBuyerIdIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> sut.findAllByBuyerId(null));
    }
}