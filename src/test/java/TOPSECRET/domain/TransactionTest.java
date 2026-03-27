package TOPSECRET.domain;

import TOPSECRET.domain.DirectSale.DirectSale;
import TOPSECRET.domain.Enum.TransactionStatus;
import TOPSECRET.domain.ValueObjects.Price;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionTest {

    // =========================================================
    // AUCTION CONSTRUCTOR

    @Test
    void shouldCreateAuctionTransactionSuccessfully() {

        // Arrange
        User buyer = mock(User.class);
        Auction auction = mock(Auction.class);
        Price price = mock(Price.class);
        User seller = mock(User.class);
        List<User> sellers = List.of(seller);
        LocalDateTime now = LocalDateTime.now();

        // Act
        Transaction transaction = new Transaction(
                buyer, auction, price, now, now, sellers, TransactionStatus.PENDING
        );

        // Assert
        assertNotNull(transaction);
    }

    @Test
    void shouldThrowWhenAuctionIsNull() {

        // Arrange
        User buyer = mock(User.class);
        Price price = mock(Price.class);
        User seller = mock(User.class);
        List<User> sellers = List.of(seller);
        LocalDateTime now = LocalDateTime.now();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(buyer, null, price, now, now, sellers, TransactionStatus.PENDING)
        );
    }

    @Test
    void shouldThrowWhenBuyerIsNull_AuctionConstructor() {

        // Arrange
        Auction auction = mock(Auction.class);
        Price price = mock(Price.class);
        User seller = mock(User.class);
        List<User> sellers = List.of(seller);
        LocalDateTime now = LocalDateTime.now();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(null, auction, price, now, now, sellers, TransactionStatus.PENDING)
        );
    }

    @Test
    void shouldThrowWhenPriceIsNull_AuctionConstructor() {

        // Arrange
        User buyer = mock(User.class);
        Auction auction = mock(Auction.class);
        User seller = mock(User.class);
        List<User> sellers = List.of(seller);
        LocalDateTime now = LocalDateTime.now();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(buyer, auction, null, now, now, sellers, TransactionStatus.PENDING)
        );
    }

    @Test
    void shouldThrowWhenTransactionDateIsNull_AuctionConstructor() {

        // Arrange
        User buyer = mock(User.class);
        Auction auction = mock(Auction.class);
        Price price = mock(Price.class);
        User seller = mock(User.class);
        List<User> sellers = List.of(seller);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(buyer, auction, price, null, LocalDateTime.now(), sellers, TransactionStatus.PENDING)
        );
    }

    @Test
    void shouldThrowWhenTransactionDateIsFuture_AuctionConstructor() {

        // Arrange
        User buyer = mock(User.class);
        Auction auction = mock(Auction.class);
        Price price = mock(Price.class);
        User seller = mock(User.class);
        List<User> sellers = List.of(seller);
        LocalDateTime future = LocalDateTime.now().plusDays(1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(buyer, auction, price, future, LocalDateTime.now(), sellers, TransactionStatus.PENDING)
        );
    }

    @Test
    void shouldThrowWhenPaymentDateIsNull_AuctionConstructor() {

        // Arrange
        User buyer = mock(User.class);
        Auction auction = mock(Auction.class);
        Price price = mock(Price.class);
        User seller = mock(User.class);
        List<User> sellers = List.of(seller);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(buyer, auction, price, LocalDateTime.now(), null, sellers, TransactionStatus.PENDING)
        );
    }

    @Test
    void shouldThrowWhenPaymentDateIsFuture_AuctionConstructor() {

        // Arrange
        User buyer = mock(User.class);
        Auction auction = mock(Auction.class);
        Price price = mock(Price.class);
        User seller = mock(User.class);
        List<User> sellers = List.of(seller);
        LocalDateTime future = LocalDateTime.now().plusDays(1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(buyer, auction, price, LocalDateTime.now(), future, sellers, TransactionStatus.PENDING)
        );
    }

    @Test
    void shouldThrowWhenSellersIsNull_AuctionConstructor() {

        // Arrange
        User buyer = mock(User.class);
        Auction auction = mock(Auction.class);
        Price price = mock(Price.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(buyer, auction, price, LocalDateTime.now(), LocalDateTime.now(), null, TransactionStatus.PENDING)
        );
    }

    // =========================================================
    // DIRECT SALE CONSTRUCTOR

    @Test
    void shouldCreateDirectSaleTransactionSuccessfully() {

        // Arrange
        User buyer = mock(User.class);
        DirectSale directSale = mock(DirectSale.class);
        Price price = mock(Price.class);
        User seller = mock(User.class);
        List<User> sellers = List.of(seller);
        LocalDateTime now = LocalDateTime.now();
        TransactionStatus status = TransactionStatus.PENDING;

        // Act
        Transaction transaction = new Transaction(
                buyer, price, directSale, now, now, sellers, status
        );

        // Assert
        assertNotNull(transaction);
    }

    @Test
    void shouldThrowWhenDirectSaleIsNull() {

        // Arrange
        User buyer = mock(User.class);
        Price price = mock(Price.class);
        User seller = mock(User.class);
        List<User> sellers = List.of(seller);
        TransactionStatus status = TransactionStatus.PENDING;


        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(buyer, price, null, LocalDateTime.now(), LocalDateTime.now(), sellers, status)
        );
    }

    @Test
    void shouldThrowWhenBuyerIsNull_DirectSaleConstructor() {

        // Arrange
        DirectSale directSale = mock(DirectSale.class);
        Price price = mock(Price.class);
        User seller = mock(User.class);
        List<User> sellers = List.of(seller);
        TransactionStatus status = TransactionStatus.PENDING;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(null, price, directSale, LocalDateTime.now(), LocalDateTime.now(), sellers, status)
        );
    }

    @Test
    void shouldThrowWhenPriceIsNull_DirectSaleConstructor() {

        // Arrange
        User buyer = mock(User.class);
        DirectSale directSale = mock(DirectSale.class);
        User seller = mock(User.class);
        List<User> sellers = List.of(seller);
        TransactionStatus status = TransactionStatus.PENDING;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(buyer, null, directSale, LocalDateTime.now(), LocalDateTime.now(), sellers, status)
        );
    }

    @Test
    void shouldThrowWhenTransactionDateIsNull_DirectSaleConstructor() {

        // Arrange
        User buyer = mock(User.class);
        DirectSale directSale = mock(DirectSale.class);
        Price price = mock(Price.class);
        User seller = mock(User.class);
        List<User> sellers = List.of(seller);
        TransactionStatus status = TransactionStatus.PENDING;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(buyer, price, directSale, null, LocalDateTime.now(), sellers, status)
        );
    }

    @Test
    void shouldThrowWhenTransactionDateIsFuture_DirectSaleConstructor() {

        // Arrange
        User buyer = mock(User.class);
        DirectSale directSale = mock(DirectSale.class);
        Price price = mock(Price.class);
        User seller = mock(User.class);
        List<User> sellers = List.of(seller);
        LocalDateTime future = LocalDateTime.now().plusDays(1);
        TransactionStatus status = TransactionStatus.PENDING;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(buyer, price, directSale, future, LocalDateTime.now(), sellers, status)
        );
    }

    @Test
    void shouldThrowWhenPaymentDateIsNull_DirectSaleConstructor() {

        // Arrange
        User buyer = mock(User.class);
        DirectSale directSale = mock(DirectSale.class);
        Price price = mock(Price.class);
        User seller = mock(User.class);
        List<User> sellers = List.of(seller);
        TransactionStatus status = TransactionStatus.PENDING;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(buyer, price, directSale, LocalDateTime.now(), null, sellers, status)
        );
    }

    @Test
    void shouldThrowWhenPaymentDateIsFuture_DirectSaleConstructor() {

        // Arrange
        User buyer = mock(User.class);
        DirectSale directSale = mock(DirectSale.class);
        Price price = mock(Price.class);
        User seller = mock(User.class);
        List<User> sellers = List.of(seller);
        LocalDateTime future = LocalDateTime.now().plusDays(1);
        TransactionStatus status = TransactionStatus.PENDING;


        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(buyer, price, directSale, LocalDateTime.now(), future, sellers, status)
        );
    }

    @Test
    void shouldThrowWhenSellersIsNull_DirectSaleConstructor() {

        // Arrange
        User buyer = mock(User.class);
        DirectSale directSale = mock(DirectSale.class);
        Price price = mock(Price.class);
        TransactionStatus status = TransactionStatus.PENDING;


        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(buyer, price, directSale, LocalDateTime.now(), LocalDateTime.now(), null, status)
        );
    }

    /*@Test
    void shouldThrowWhenStatusIsNull_DirectSaleConstructor() {

        // Arrange
        User buyer = mock(User.class);
        DirectSale directSale = mock(DirectSale.class);
        Price price = mock(Price.class);
        User seller = mock(User.class);

        List<User> sellers = List.of(seller);
        LocalDateTime now = LocalDateTime.now();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(
                        buyer,
                        price,
                        directSale,
                        now,
                        now,
                        sellers,
                        null
                )
        );
    }*/
}