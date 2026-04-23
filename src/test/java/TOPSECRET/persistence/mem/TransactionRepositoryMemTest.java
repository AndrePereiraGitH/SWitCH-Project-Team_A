package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Enum.TransactionStatus;
import TOPSECRET.domain.Transaction.Transaction;
import TOPSECRET.domain.Transaction.TransactionFactory;
import TOPSECRET.domain.ValueObjects.AuctionId;
import TOPSECRET.domain.ValueObjects.DirectSaleID;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionRepositoryMemTest {

    private TransactionFactory transactionFactory;
    private TransactionRepositoryMem repository;

    private UserId buyer;
    private AuctionId auction;
    private DirectSaleID directSale;
    private Price price;
    private List<UserId> sellers;
    private Transaction transaction;

    private LocalDateTime transactionDate;
    private LocalDateTime paymentDate;
    private TransactionStatus status;

    @BeforeEach
    void setUp() {
        transactionFactory = mock(TransactionFactory.class);
        repository = new TransactionRepositoryMem(transactionFactory);

        buyer = mock(UserId.class);
        auction = mock(AuctionId.class);
        directSale = mock(DirectSaleID.class);
        price = mock(Price.class);
        sellers = List.of(mock(UserId.class));

        transaction = mock(Transaction.class);

        transactionDate = LocalDateTime.now();
        paymentDate = LocalDateTime.now().plusDays(1);
        status = TransactionStatus.PENDING;
    }

    @Test
    void createAndAddFromAuction_shouldAddTransaction_whenNotExisting() throws Exception {

        // Arrange
        when(transactionFactory.createFromAuction(
                buyer, auction, price, transactionDate, paymentDate, sellers, status))
                .thenReturn(transaction);

        // Act
        Transaction result = repository.createAndAddFromAuction(
                buyer, auction, price, transactionDate, paymentDate, sellers, status
        );

        // Assert
        assertEquals(transaction, result);
        assertTrue(repository.getAllTransactions().contains(transaction));
    }

    @Test
    void createAndAddFromAuction_shouldThrowException_whenAlreadyExisting() throws Exception {

        //Arrange
        when(transactionFactory.createFromAuction(
                buyer, auction, price, transactionDate, paymentDate, sellers, status))
                .thenReturn(transaction);

        repository.createAndAddFromAuction(buyer, auction, price, transactionDate,
                paymentDate, sellers, status);

        //Act + assert
        assertThrows(InstanceAlreadyExistsException.class, () -> repository.createAndAddFromAuction(
                        buyer, auction, price, transactionDate, paymentDate, sellers, status
                )
        );
    }

    @Test
    void createAndAddFromDirectSale_shouldAddTransaction_whenNotExisting() throws Exception {
        // Arrange
        when(transactionFactory.createFromDirectSale(
                buyer, price, directSale, transactionDate, paymentDate, sellers, status))
                .thenReturn(transaction);

        // Act
        Transaction result = repository.createAndAddFromDirectSale(
                buyer, price, directSale, transactionDate, paymentDate, sellers, status
        );

        // Assert
        assertEquals(transaction, result);
        assertTrue(repository.getAllTransactions().contains(transaction));
    }

    @Test
    void createAndAddFromDirectSale_shouldThrowException_whenTransactionAlreadyExists() throws Exception {
        // Arrange
        when(transactionFactory.createFromDirectSale(
                buyer, price, directSale, transactionDate, paymentDate, sellers, status))
                .thenReturn(transaction);

        repository.createAndAddFromDirectSale(buyer, price, directSale, transactionDate,
                paymentDate, sellers, status);

        // Act & Assert
        assertThrows(InstanceAlreadyExistsException.class, () ->
                repository.createAndAddFromDirectSale(
                        buyer, price, directSale, transactionDate, paymentDate, sellers, status
                )
        );
    }

    @Test
    void getAllTransactions_shouldReturnCopy_notOriginalList() throws Exception {
        // Arrange
        when(transactionFactory.createFromAuction(buyer, auction, price, transactionDate, paymentDate,
                sellers, status))
                .thenReturn(transaction);

        repository.createAndAddFromAuction(
                buyer, auction, price, transactionDate, paymentDate,
                sellers, status
        );

        // Act
        List<Transaction> result = repository.getAllTransactions();

        // Assert
        assertEquals(1, result.size());
        assertThrows(UnsupportedOperationException.class, () -> result.add(mock(Transaction.class)));
    }
}
