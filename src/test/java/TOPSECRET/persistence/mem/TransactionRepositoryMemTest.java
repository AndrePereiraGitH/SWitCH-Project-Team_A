package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Transaction.ITransactionRepository;
import TOPSECRET.domain.Transaction.Transaction;
import TOPSECRET.domain.ValueObjects.TransactionID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionRepositoryMemTest {

    private Transaction transaction;
    private TransactionID transactionId;

    @BeforeEach
    void setUp() {
        transaction = mock(Transaction.class);
        transactionId = mock(TransactionID.class);

        when(transaction.id()).thenReturn(transactionId);
    }

    @Test
    void save_shouldStoreTransaction_whenNotExisting() {
        // Arrange
        ITransactionRepository repository = new TransactionRepositoryMem();

        // Act
        Transaction result = repository.save(transaction);

        // Assert
        assertEquals(transaction, result);
        assertTrue(repository.findAll().contains(transaction));
    }

    @Test
    void save_shouldThrowException_whenTransactionIsNull() {
        // Arrange
        ITransactionRepository repository = new TransactionRepositoryMem();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                repository.save(null)
        );
    }

    @Test
    void save_shouldThrowException_whenTransactionAlreadyExists() {
        // Arrange
        ITransactionRepository repository = new TransactionRepositoryMem();

        // Act
        repository.save(transaction);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                repository.save(transaction)
        );
    }

    @Test
    void findById_shouldReturnTransaction_whenExisting() {
        // Arrange
        ITransactionRepository repository = new TransactionRepositoryMem();

        // Act
        repository.save(transaction);

        Optional<Transaction> result = repository.findById(transactionId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(transaction, result.get());
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExisting() {
        // Arrange
        ITransactionRepository repository = new TransactionRepositoryMem();

        TransactionID otherId = mock(TransactionID.class);

        // Act
        Optional<Transaction> result = repository.findById(otherId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findById_shouldThrowException_whenIdIsNull() {
        // Arrange
        ITransactionRepository repository = new TransactionRepositoryMem();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                repository.findById(null)
        );
    }

    @Test
    void existsById_shouldReturnTrue_whenExisting() {
        // Arrange
        ITransactionRepository repository = new TransactionRepositoryMem();

        // Act
        repository.save(transaction);

        // Arrange
        assertTrue(repository.existsById(transactionId));
    }

    @Test
    void existsById_shouldReturnFalse_whenNotExisting() {
        // Arrange
        ITransactionRepository repository = new TransactionRepositoryMem();

        TransactionID otherId = mock(TransactionID.class);

        // Act+Assert
        assertFalse(repository.existsById(otherId));
    }

    @Test
    void existsById_shouldThrowException_whenIdIsNull() {
        // Arrange
        ITransactionRepository repository = new TransactionRepositoryMem();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                repository.existsById(null)
        );
    }

    @Test
    void findAll_shouldReturnImmutableCopy() {
        // Arrange
        ITransactionRepository repository = new TransactionRepositoryMem();

        // Act
        repository.save(transaction);

        List<Transaction> result = repository.findAll();

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(transaction));
        assertThrows(UnsupportedOperationException.class, () ->
                result.add(mock(Transaction.class))
        );
    }
}