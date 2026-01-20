package TOPSECRET.domain;

import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void TransactionEmptyConstructorTest() {
        // Arrange
        TOPSECRET.domain.Transaction transaction = new TOPSECRET.domain.Transaction();

        // Assert
        assertNotNull(transaction);
    }

    @Test
    void AuctionTransactionConstructorIsValid() {
        // Act
        Transaction transaction = new Transaction (
                createValidUser("Buyer"),
                createValidAuction(),
                new Price(new BigDecimal("19.99")),
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(createValidUser("Seller")),
                TransactionStatus.PENDING
        );

        // Assert
        assertNotNull(transaction);
    }

    @Test
    void auctionConstructorNullAuction() {
        //Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                new Transaction(
                        createValidUser("Buyer"),
                        null,
                        new Price(new BigDecimal("10.00")),
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of(createValidUser("Seller")),
                        TransactionStatus.PENDING
                )
        );
        assertEquals("Auction cannot be null", e.getMessage());
    }

    @Test
    void auctionConstructorNullBuyer() {
        //Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                new Transaction(
                        null,
                        createValidAuction(),
                        new Price(new BigDecimal("10.00")),
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of(createValidUser("Seller")),
                        TransactionStatus.PENDING
                )
        );

        assertEquals("Buyer cannot be null", e.getMessage());
    }

    @Test
    void auctionConstructorNullPrice() {
        //Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                new Transaction(
                        createValidUser("Buyer"),
                        createValidAuction(),
                        null,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of(createValidUser("Seller")),
                        TransactionStatus.PENDING
                )
        );

        assertEquals("Price cannot be null", e.getMessage());
    }

    @Test
    void auctionConstructorNullTransactionDate() {
        //Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                new Transaction(
                        createValidUser("Buyer"),
                        createValidAuction(),
                        new Price(new BigDecimal("10.00")),
                        null,
                        LocalDateTime.now(),
                        List.of(createValidUser("Seller")),
                        TransactionStatus.PENDING
                )
        );
        assertEquals("Transaction date cannot be null", e.getMessage());
    }

    @Test
    void auctionConstructorFutureTransactionDate() {
        //Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                new Transaction(
                        createValidUser("Buyer"),
                        createValidAuction(),
                        new Price(new BigDecimal("10.00")),
                        LocalDateTime.now().plusDays(1),
                        LocalDateTime.now(),
                        List.of(createValidUser("Seller")),
                        TransactionStatus.PENDING
                )
        );
        assertEquals("Transaction date cannot be null", e.getMessage());
    }

    @Test
    void auctionConstructorNullPaymentDate() {
        //Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                new Transaction(
                        createValidUser("Buyer"),
                        createValidAuction(),
                        new Price(new BigDecimal("10.00")),
                        LocalDateTime.now(),
                        null,
                        List.of(createValidUser("Seller")),
                        TransactionStatus.PENDING
                )
        );
        assertEquals("Future Payment date cannot be null", e.getMessage());

    }

    @Test
    void auctionConstructorFuturePaymentDate() {
        //Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                new Transaction(
                        createValidUser("Buyer"),
                        createValidAuction(),
                        new Price(new BigDecimal("10.00")),
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(1),
                        List.of(createValidUser("Seller")),
                        TransactionStatus.PENDING
                )
        );
        assertEquals("Future Payment date cannot be null", e.getMessage());
    }

    @Test
    void auctionConstructorNullSellers() {
        //Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                new Transaction(
                        createValidUser("Buyer"),
                        createValidAuction(),
                        new Price(new BigDecimal("10.00")),
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        null,
                        TransactionStatus.PENDING
                )
        );
        assertEquals("Seller list cannot be null", e.getMessage());
    }

    @Test
    void cartTransactionConstructorIsValid() {
        // Arrange
        User buyer = createValidUser("Buyer");

        // Act
        Transaction transaction = new Transaction(
                buyer,
                new Price(new BigDecimal("19.99")),
                createValidCart(buyer),
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(createValidUser("Seller"))
        );

        // Assert
        assertNotNull(transaction);
    }

    @Test
    void cartConstructorNullCart() {
        // Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                new Transaction(
                        createValidUser("Buyer"),
                        new Price(new BigDecimal("10.00")),
                        null,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of(createValidUser("Seller"))
                )
        );
        assertEquals("Cart cannot be null", e.getMessage());
    }

    @Test
    void cartConstructorNullBuyer() {
        // Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                new Transaction(
                        null,
                        new Price(new BigDecimal("10.00")),
                        createValidCart(createValidUser("Buyer")),
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of(createValidUser("Seller"))
                )
        );
        assertEquals("Buyer cannot be null", e.getMessage());
    }

    @Test
    void cartConstructorNullPrice() {
        // Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                new Transaction(
                        createValidUser("Buyer"),
                        null,
                        createValidCart(createValidUser("Buyer")),
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of(createValidUser("Seller"))
                )
        );
        assertEquals("Price cannot be null", e.getMessage());
    }

    @Test
    void cartConstructorNullTransactionDate() {
        // Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                new Transaction(
                        createValidUser("Buyer"),
                        new Price(new BigDecimal("10.00")),
                        createValidCart(createValidUser("Buyer")),
                        null,
                        LocalDateTime.now(),
                        List.of(createValidUser("Seller"))
                )
        );
        assertEquals("Transaction date cannot be null", e.getMessage());
    }

    @Test
    void cartConstructorNullPaymentDate() {
        // Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                new Transaction(
                        createValidUser("Buyer"),
                        new Price(new BigDecimal("10.00")),
                        createValidCart(createValidUser("Buyer")),
                        LocalDateTime.now(),
                        null,
                        List.of(createValidUser("Seller"))
                )
        );
        assertEquals("Transaction payment date cannot be null", e.getMessage());
    }

    @Test
    void cartConstructorNullSellers() {
        // Act & Asser
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                new Transaction(
                        createValidUser("Buyer"),
                        new Price(new BigDecimal("10.00")),
                        createValidCart(createValidUser("Buyer")),
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        null
                )
        );
        assertEquals("Seller list cannot be null", e.getMessage());
    }


    //=============================================
    // Private Methods to Help Instantiate

    private static User createValidUser(String name){
        Email email = new Email("asa", "example.com");
        Address address = new Address("Rua Dra. Salomé Martingo", "nº 455", "4540-440", new Country("France"));
        Contact contact = new Contact("916274847", "+351", email, address);
        Profile profile = new Profile(name, new Country("Portugal"), LocalDate.of(1995, 10, 14));
        UserRole userRole = UserRole.NORMAL_USER;

        return new User(name, contact, profile, userRole);
    }

    private static BibliographicalData createValidBibliographicalData(){
        List<Profile> authors = List.of(
                new Profile("J. K. Rowling", new Country("Portugal"), LocalDate.of(1995, 10, 14)),
                new Profile("Freida", new Country("Spain"), LocalDate.of(1956, 10, 10))
        );

        return new BibliographicalData(
                "O Senhor do Anéis: A irmandade do Anel",
                authors,
                new Locale("pt","PT"),
                new Publisher(
                        "PortoEditora",
                        "123456748",
                        new Contact(
                                "918475648",
                                "+351",
                                new Email("asa", "example.com"),
                                new Address("Rua Antónia Pinto", "123", "5503-231", new Country("Portugal"))
                        )
                ),
                LocalDate.of(2020,1,10),
                List.of(),
                200,
                "Text Description"
                );
    }


    private static Item createValidItem(){
        return new Item(
                Condition.LIKE_NEW,
                250.30,
                new Dimensions (102, 120, 102),
                TypeOfBinding.PAPERBACK,
                "1st Edition",
                createValidBibliographicalData()
        );
    }

    private static Listing createValidListing(){
        return new Listing(
                List.of(createValidItem()),
                new Price (new BigDecimal("19.00")),
                "ShortDescription",
                "Invalid Title",
                createValidUser("Seller"),
                List.of(new Picture("Pic1"))
        );
    }

    private static Auction createValidAuction(){
        LocalDateTime now = LocalDateTime.now().plusMinutes(1);
        return new Auction(
                createValidListing(),
                now,
                now.plusDays(1),
                new Price(new BigDecimal("10.00")),
                new Price(new BigDecimal("19.99"))
        );
    }

    private static Cart createValidCart(User buyer){
        Cart cart = new Cart(buyer);
        cart.addListing(createValidListing());
        return cart;
    }
}