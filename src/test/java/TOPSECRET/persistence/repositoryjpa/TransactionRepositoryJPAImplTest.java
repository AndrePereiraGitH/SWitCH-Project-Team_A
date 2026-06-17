package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.Transaction.ITransactionRepository;
import TOPSECRET.domain.Transaction.Transaction;
import TOPSECRET.domain.ValueObjects.TransactionID;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.persistence.repositoryjpa.assembler.TransactionAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.TransactionDM;
import TOPSECRET.persistence.repositoryjpa.jpa.ITransactionRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link TransactionRepositoryJPAImpl}.
 *
 * <p>Tests the JPA-based implementation of {@link ITransactionRepository}, which acts as
 * an anti-corruption layer between the domain model and the JPA persistence layer. All
 * conversions between domain objects ({@link Transaction}) and data model objects
 * ({@link TransactionDM}) are delegated to {@link TransactionAssembler}.</p>
 *
 * <p>Tests are organized into the following sections:</p>
 * <ul>
 *   <li><b>save</b> — persisting a transaction and null guards.</li>
 *   <li><b>findById</b> — retrieval by identity, empty results, and null guards.</li>
 *   <li><b>existsById</b> — presence checks with true/false outcomes and null guards.</li>
 *   <li><b>findAll</b> — retrieval of zero, one, or many transactions.</li>
 *   <li><b>findAllByBuyerId</b> — retrieval by buyer with multiple/single/no matches and null guards.</li>
 * </ul>
 *
 * @see TransactionRepositoryJPAImpl
 * @see ITransactionRepository
 * @see TransactionAssembler
 */
@ExtendWith(MockitoExtension.class)
class TransactionRepositoryJPAImplTest {

    @Mock
    private TransactionAssembler assembler;

    @Mock
    private ITransactionRepositoryJpa jpa;

    @InjectMocks
    private TransactionRepositoryJPAImpl repository;

    private TransactionID transactionIdDouble;
    private Transaction transactionDouble;
    private TransactionDM transactionDMDouble;
    private UserId buyerIdDouble;

    @BeforeEach
    void setup() {
        transactionIdDouble = mock(TransactionID.class);
        transactionDouble   = mock(Transaction.class);
        transactionDMDouble = mock(TransactionDM.class);
        buyerIdDouble       = mock(UserId.class);
    }

    // =========================================================================
    // save
    // =========================================================================

    @Test
    void save_shouldReturnSavedTransaction() {

        // Arrange
        when(assembler.toDM(transactionDouble))
                .thenReturn(transactionDMDouble);

        when(jpa.save(transactionDMDouble))
                .thenReturn(transactionDMDouble);

        when(assembler.toDomain(transactionDMDouble))
                .thenReturn(transactionDouble);

        // Act
        Transaction result = repository.save(transactionDouble);

        // Assert
        assertNotNull(result);
        assertEquals(transactionDouble, result);
    }

    @Test
    void save_shouldThrowNullPointerException_whenTransactionIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repository.save(null));
    }

    // =========================================================================
    // findById
    // =========================================================================

    @Test
    void findById_shouldReturnTransaction_whenIdExists() {

        // Arrange
        when(jpa.findById(transactionIdDouble))
                .thenReturn(Optional.of(transactionDMDouble));

        when(assembler.toDomain(transactionDMDouble))
                .thenReturn(transactionDouble);

        // Act
        Optional<Transaction> result = repository.findById(transactionIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(transactionDouble, result.get());
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenIdDoesNotExist() {

        // Arrange
        when(jpa.findById(transactionIdDouble))
                .thenReturn(Optional.empty());

        // Act
        Optional<Transaction> result = repository.findById(transactionIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findById_shouldThrowNullPointerException_whenIdIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repository.findById(null));
    }

    // =========================================================================
    // existsById
    // =========================================================================

    @Test
    void existsById_shouldReturnTrue_whenTransactionExists() {

        // Arrange
        when(jpa.existsById(transactionIdDouble))
                .thenReturn(true);

        // Act
        boolean result = repository.existsById(transactionIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsById_shouldReturnFalse_whenTransactionDoesNotExist() {

        // Arrange
        when(jpa.existsById(transactionIdDouble))
                .thenReturn(false);

        // Act
        boolean result = repository.existsById(transactionIdDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void existsById_shouldThrowNullPointerException_whenIdIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repository.existsById(null));
    }

    // =========================================================================
    // findAll
    // =========================================================================

    @Test
    void findAll_shouldReturnEmptyList_whenRepositoryContainsNoTransactions() {

        // Arrange
        when(jpa.findAll()).thenReturn(List.of());

        // Act
        List<Transaction> result = repository.findAll();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldReturnListWithOneElement_whenOneTransactionExists() {

        // Arrange
        TransactionDM dm1 = mock(TransactionDM.class);
        Transaction tx1   = mock(Transaction.class);

        when(jpa.findAll()).thenReturn(List.of(dm1));
        when(assembler.toDomain(dm1)).thenReturn(tx1);

        // Act
        List<Transaction> result = repository.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(tx1, result.get(0));
    }

    @Test
    void findAll_shouldReturnAllMappedTransactions_whenMultipleExist() {

        // Arrange
        TransactionDM dm1 = mock(TransactionDM.class);
        TransactionDM dm2 = mock(TransactionDM.class);

        Transaction tx1 = mock(Transaction.class);
        Transaction tx2 = mock(Transaction.class);

        when(jpa.findAll()).thenReturn(List.of(dm1, dm2));
        when(assembler.toDomain(dm1)).thenReturn(tx1);
        when(assembler.toDomain(dm2)).thenReturn(tx2);

        // Act
        List<Transaction> result = repository.findAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(tx1));
        assertTrue(result.contains(tx2));
    }

    // =========================================================================
    // findAllByBuyerId
    // =========================================================================

    @Test
    void findAllByBuyerId_shouldReturnAllTransactions_whenBuyerHasMultiple() {

        // Arrange
        TransactionDM dm1 = mock(TransactionDM.class);
        TransactionDM dm2 = mock(TransactionDM.class);

        Transaction tx1 = mock(Transaction.class);
        Transaction tx2 = mock(Transaction.class);

        when(jpa.findAllByBuyerId(buyerIdDouble)).thenReturn(List.of(dm1, dm2));
        when(assembler.toDomain(dm1)).thenReturn(tx1);
        when(assembler.toDomain(dm2)).thenReturn(tx2);

        // Act
        List<Transaction> result = repository.findAllByBuyerId(buyerIdDouble);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(tx1));
        assertTrue(result.contains(tx2));
    }

    @Test
    void findAllByBuyerId_shouldReturnOneTransaction_whenBuyerHasOne() {

        // Arrange
        TransactionDM dm = mock(TransactionDM.class);
        Transaction tx   = mock(Transaction.class);

        when(jpa.findAllByBuyerId(buyerIdDouble)).thenReturn(List.of(dm));
        when(assembler.toDomain(dm)).thenReturn(tx);

        // Act
        List<Transaction> result = repository.findAllByBuyerId(buyerIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertEquals(tx, result.get(0));
    }

    @Test
    void findAllByBuyerId_shouldReturnEmptyList_whenBuyerHasNoTransactions() {

        // Arrange
        when(jpa.findAllByBuyerId(buyerIdDouble)).thenReturn(List.of());

        // Act
        List<Transaction> result = repository.findAllByBuyerId(buyerIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllByBuyerId_shouldThrowNullPointerException_whenBuyerIdIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repository.findAllByBuyerId(null));
    }
}