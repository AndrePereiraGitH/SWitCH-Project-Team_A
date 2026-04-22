package TOPSECRET.domain.Transaction;

import TOPSECRET.domain.Enum.TransactionStatus;
import TOPSECRET.domain.ValueObjects.AuctionId;
import TOPSECRET.domain.ValueObjects.DirectSaleID;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionTest {

    TransactionFactory transactionFactory;

    @BeforeEach
    void setUp() {
        transactionFactory = new TransactionFactory();
    }

    @Test
    void shouldCreateAuctionTransactionSuccessfully() {

        // Arrange
        UserId buyer = mock(UserId.class);
        AuctionId auction = mock(AuctionId.class);
        Price price = mock(Price.class);
        UserId seller = mock(UserId.class);
        List<UserId> sellers = List.of(seller);
        LocalDateTime now = LocalDateTime.now();

        // Act
        Transaction transaction = transactionFactory.createFromAuction(
                buyer, auction, price, now, now, sellers, TransactionStatus.PENDING
        );

        // Assert
        assertNotNull(transaction);
    }

    @Test
    void shouldThrowWhenAuctionIsNull() {

        // Arrange
        UserId buyer = mock(UserId.class);
        Price price = mock(Price.class);
        UserId seller = mock(UserId.class);
        List<UserId> sellers = List.of(seller);
        LocalDateTime now = LocalDateTime.now();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                transactionFactory.createFromAuction(buyer, null, price, now, now, sellers,
                        TransactionStatus.PENDING)
        );
    }

    @Test
    void shouldThrowWhenBuyerIsNull_AuctionConstructor() {

        // Arrange
        AuctionId auction = mock(AuctionId.class);
        Price price = mock(Price.class);
        UserId seller = mock(UserId.class);
        List<UserId> sellers = List.of(seller);
        LocalDateTime now = LocalDateTime.now();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                transactionFactory.createFromAuction(null, auction, price, now, now, sellers,
                        TransactionStatus.PENDING)
        );
    }

    @Test
    void shouldThrowWhenPriceIsNull_AuctionConstructor() {

        // Arrange
        UserId buyer = mock(UserId.class);
        AuctionId auction = mock(AuctionId.class);
        UserId seller = mock(UserId.class);
        List<UserId> sellers = List.of(seller);
        LocalDateTime now = LocalDateTime.now();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                transactionFactory.createFromAuction(buyer, auction, null, now, now, sellers,
                        TransactionStatus.PENDING)
        );
    }

    @Test
    void shouldThrowWhenTransactionDateIsNull_AuctionConstructor() {

        // Arrange
        UserId buyer = mock(UserId.class);
        AuctionId auction = mock(AuctionId.class);
        Price price = mock(Price.class);
        UserId seller = mock(UserId.class);
        List<UserId> sellers = List.of(seller);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                transactionFactory.createFromAuction(buyer, auction, price, null, LocalDateTime.now(),
                        sellers, TransactionStatus.PENDING)
        );
    }

    @Test
    void shouldThrowWhenTransactionDateIsFuture_AuctionConstructor() {

        // Arrange
        UserId buyer = mock(UserId.class);
        AuctionId auction = mock(AuctionId.class);
        Price price = mock(Price.class);
        UserId seller = mock(UserId.class);
        List<UserId> sellers = List.of(seller);
        LocalDateTime future = LocalDateTime.now().plusDays(1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                transactionFactory.createFromAuction(buyer, auction, price, future, LocalDateTime.now(),
                        sellers, TransactionStatus.PENDING)
        );
    }

    @Test
    void shouldThrowWhenPaymentDateIsNull_AuctionConstructor() {

        // Arrange
        UserId buyer = mock(UserId.class);
        AuctionId auction = mock(AuctionId.class);
        Price price = mock(Price.class);
        UserId seller = mock(UserId.class);
        List<UserId> sellers = List.of(seller);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                transactionFactory.createFromAuction(buyer, auction, price, LocalDateTime.now(), null,
                        sellers, TransactionStatus.PENDING)
        );
    }

    @Test
    void shouldThrowWhenPaymentDateIsFuture_AuctionConstructor() {

        // Arrange
        UserId buyer = mock(UserId.class);
        AuctionId auction = mock(AuctionId.class);
        Price price = mock(Price.class);
        UserId seller = mock(UserId.class);
        List<UserId> sellers = List.of(seller);
        LocalDateTime future = LocalDateTime.now().plusDays(1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                transactionFactory.createFromAuction(buyer, auction, price, LocalDateTime.now(), future,
                        sellers, TransactionStatus.PENDING)
        );
    }

    @Test
    void shouldThrowWhenSellersIsNull_AuctionConstructor() {

        // Arrange
        UserId buyer = mock(UserId.class);
        AuctionId auction = mock(AuctionId.class);
        Price price = mock(Price.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                transactionFactory.createFromAuction(buyer, auction, price, LocalDateTime.now(), LocalDateTime.now(), null,
                        TransactionStatus.PENDING)
        );
    }

    // =========================================================
    // DIRECT SALE CONSTRUCTOR

    @Test
    void shouldCreateDirectSaleTransactionSuccessfully() {

        // Arrange
        UserId buyer = mock(UserId.class);
        DirectSaleID directSale = mock(DirectSaleID.class);
        Price price = mock(Price.class);
        UserId seller = mock(UserId.class);
        List<UserId> sellers = List.of(seller);
        LocalDateTime now = LocalDateTime.now();
        TransactionStatus status = TransactionStatus.PENDING;

        // Act
        Transaction transaction = transactionFactory.createFromDirectSale(
                buyer, price, directSale, now, now, sellers, status
        );

        // Assert
        assertNotNull(transaction);
    }

    @Test
    void shouldThrowWhenDirectSaleIsNull() {

        // Arrange
        UserId buyer = mock(UserId.class);
        Price price = mock(Price.class);
        UserId seller = mock(UserId.class);
        List<UserId> sellers = List.of(seller);
        TransactionStatus status = TransactionStatus.PENDING;


        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                transactionFactory.createFromDirectSale(buyer, price, null, LocalDateTime.now(),
                        LocalDateTime.now(), sellers, status)
        );
    }

    @Test
    void shouldThrowWhenBuyerIsNull_DirectSaleConstructor() {

        // Arrange
        DirectSaleID directSale = mock(DirectSaleID.class);
        Price price = mock(Price.class);
        UserId seller = mock(UserId.class);
        List<UserId> sellers = List.of(seller);
        TransactionStatus status = TransactionStatus.PENDING;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                transactionFactory.createFromDirectSale(null, price, directSale, LocalDateTime.now(),
                        LocalDateTime.now(), sellers, status)
        );
    }

    @Test
    void shouldThrowWhenPriceIsNull_DirectSaleConstructor() {

        // Arrange
        UserId buyer = mock(UserId.class);
        DirectSaleID directSale = mock(DirectSaleID.class);
        UserId seller = mock(UserId.class);
        List<UserId> sellers = List.of(seller);
        TransactionStatus status = TransactionStatus.PENDING;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                transactionFactory.createFromDirectSale(buyer, null, directSale, LocalDateTime.now(),
                        LocalDateTime.now(), sellers, status)
        );
    }

    @Test
    void shouldThrowWhenTransactionDateIsNull_DirectSaleConstructor() {

        // Arrange
        UserId buyer = mock(UserId.class);
        DirectSaleID directSale = mock(DirectSaleID.class);
        Price price = mock(Price.class);
        UserId seller = mock(UserId.class);
        List<UserId> sellers = List.of(seller);
        TransactionStatus status = TransactionStatus.PENDING;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                transactionFactory.createFromDirectSale(buyer, price, directSale, null,
                        LocalDateTime.now(), sellers, status)
        );
    }

    @Test
    void shouldThrowWhenTransactionDateIsFuture_DirectSaleConstructor() {

        // Arrange
        UserId buyer = mock(UserId.class);
        DirectSaleID directSale = mock(DirectSaleID.class);
        Price price = mock(Price.class);
        UserId seller = mock(UserId.class);
        List<UserId> sellers = List.of(seller);
        LocalDateTime future = LocalDateTime.now().plusDays(1);
        TransactionStatus status = TransactionStatus.PENDING;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                transactionFactory.createFromDirectSale(buyer, price, directSale, future,
                        LocalDateTime.now(), sellers, status)
        );
    }

    @Test
    void shouldThrowWhenPaymentDateIsNull_DirectSaleConstructor() {

        // Arrange
        UserId buyer = mock(UserId.class);
        DirectSaleID directSale = mock(DirectSaleID.class);
        Price price = mock(Price.class);
        UserId seller = mock(UserId.class);
        List<UserId> sellers = List.of(seller);
        TransactionStatus status = TransactionStatus.PENDING;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                transactionFactory.createFromDirectSale(buyer, price, directSale,
                        LocalDateTime.now(), null, sellers, status)
        );
    }

    @Test
    void shouldThrowWhenPaymentDateIsFuture_DirectSaleConstructor() {

        // Arrange
        UserId buyer = mock(UserId.class);
        DirectSaleID directSale = mock(DirectSaleID.class);
        Price price = mock(Price.class);
        UserId seller = mock(UserId.class);
        List<UserId> sellers = List.of(seller);
        LocalDateTime future = LocalDateTime.now().plusDays(1);
        TransactionStatus status = TransactionStatus.PENDING;


        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                transactionFactory.createFromDirectSale(buyer, price, directSale, LocalDateTime.now(),
                        future, sellers, status)
        );
    }

    @Test
    void shouldThrowWhenSellersIsNull_DirectSaleConstructor() {

        // Arrange
        UserId buyer = mock(UserId.class);
        DirectSaleID directSale = mock(DirectSaleID.class);
        Price price = mock(Price.class);
        TransactionStatus status = TransactionStatus.PENDING;


        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                transactionFactory.createFromDirectSale(buyer, price, directSale, LocalDateTime.now(),
                        LocalDateTime.now(), null, status)
        );
    }

    @Test
    void shouldThrowWhenStatusIsNull_DirectSaleConstructor() {

        // Arrange
        UserId buyer = mock(UserId.class);
        DirectSaleID directSale = mock(DirectSaleID.class);
        Price price = mock(Price.class);
        UserId seller = mock(UserId.class);

        List<UserId> sellers = List.of(seller);
        LocalDateTime now = LocalDateTime.now();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                transactionFactory.createFromDirectSale(
                        buyer,
                        price,
                        directSale,
                        now,
                        now,
                        sellers,
                        null
                )
        );
    }

    @Test
    void sameAsShouldReturnTrueForSameData() {

        //Arrange
        UserId buyer = mock(UserId.class);
        Price price = mock(Price.class);
        UserId seller = mock(UserId.class);
        List<UserId> sellers = List.of(seller);
        LocalDateTime now = LocalDateTime.now();

        Transaction transaction1 = transactionFactory.createFromAuction(
                buyer, mock(AuctionId.class), price, now, now, sellers, TransactionStatus.PENDING
        );

        Transaction transaction2 = transactionFactory.createFromAuction(
                buyer, mock(AuctionId.class), price, now, now, sellers, TransactionStatus.PENDING
        );

        //Act + assert
        assertTrue(transaction1.sameAs(transaction2));
    }

    @Test
    void equalsShouldReturnFalseForDifferentTransactions() {

        //Arrange
        UserId buyer = mock(UserId.class);
        Price price = mock(Price.class);
        UserId seller = mock(UserId.class);
        List<UserId> sellers = List.of(seller);
        LocalDateTime now = LocalDateTime.now();

        Transaction transaction1 = transactionFactory.createFromAuction(
                buyer, mock(AuctionId.class), price, now, now, sellers, TransactionStatus.PENDING
        );

        Transaction transaction2 = transactionFactory.createFromAuction(
                buyer, mock(AuctionId.class), price, now, now, sellers, TransactionStatus.PENDING
        );

        //Act + assert
        assertFalse(transaction1.equals(transaction2));
    }

    @Test
    void equalsShouldReturnFalseForDifferentObjectType() {

        //Arrange
        UserId buyer = mock(UserId.class);
        Price price = mock(Price.class);
        UserId seller = mock(UserId.class);
        List<UserId> sellers = List.of(seller);
        LocalDateTime now = LocalDateTime.now();

        Transaction transaction = transactionFactory.createFromAuction(
                buyer, mock(AuctionId.class), price, now, now, sellers, TransactionStatus.PENDING
        );

        //Act + assert
        assertFalse(transaction.equals("this is not a transaction"));
    }

    @Test
    void sameAsShouldReturnFalseWhenBuyerDiffers() {

        //Arrange
        Price price = mock(Price.class);
        UserId seller = mock(UserId.class);
        List<UserId> sellers = List.of(seller);
        LocalDateTime now = LocalDateTime.now();

        Transaction transaction1 = transactionFactory.createFromAuction(
                mock(UserId.class), mock(AuctionId.class), price, now, now, sellers, TransactionStatus.PENDING
        );

        Transaction transaction2 = transactionFactory.createFromAuction(
                mock(UserId.class), mock(AuctionId.class), price, now, now, sellers, TransactionStatus.PENDING
        );

        //Act + assert
        assertFalse(transaction1.sameAs(transaction2));
    }

    @Test
    void sameAsShouldReturnFalseWhenStatusDiffers() {

        //Arrange
        UserId buyer = mock(UserId.class);
        Price price = mock(Price.class);
        UserId seller = mock(UserId.class);
        List<UserId> sellers = List.of(seller);
        LocalDateTime now = LocalDateTime.now();

        Transaction transaction1 = transactionFactory.createFromAuction(
                buyer, mock(AuctionId.class), price, now, now, sellers, TransactionStatus.PENDING
        );

        Transaction transaction2 = transactionFactory.createFromAuction(
                buyer, mock(AuctionId.class), price, now, now, sellers, TransactionStatus.COMPLETED
        );

        //Act + assert
        assertFalse(transaction1.sameAs(transaction2));
    }
}