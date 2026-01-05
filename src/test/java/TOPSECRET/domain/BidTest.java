package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Country;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class BidTest {
    //Not an author profile
    @Test
    void constructorShouldCreateBidWhenValid() {
        // Arrange
        Address address = new Address("Main Street", "12345", "3678-769", Country.FRANCE);



        Email email = new Email("joanabeauty", "gmail.com");
        Contact contact = new Contact("912345678", "+351", email, address);


        Profile userProfile = new Profile(
                "Maria Silva",           // name
                Country.PORTUGAL,        // country
                LocalDate.of(1990, Month.MAY, 15), // birthday
                false                    // isAuthor
        );


        User bidder = new User("Maria123", contact, userProfile);
        double value = 100.50;
        LocalDateTime bidDateTime = LocalDateTime.now();

        // Act
        Bid result = new Bid(bidder, value, bidDateTime);

        // Assert
        assertEquals(bidder, result.getBidder());
        assertEquals(value, result.getValue(), 0.001);
        assertEquals(bidDateTime, result.getBidDateTime());
        assertFalse(bidder.getProfile().isAuthor());
    }


    // Author profile
    @Test
    void constructorShouldWorkWithAuthorProfile() {
        // Arrange
        Address address = new Address("Writer's Avenue", "456", "1000-001", Country.AUSTRALIA);
        Email email = new Email("author.john", "email.com");
        Contact contact = new Contact("912345679", "+1", email, address);

        // Criar Profile de autor
        Profile authorProfile = new Profile(
                "John Writer",
                Country.AUSTRALIA,
                LocalDate.of(1985, Month.JANUARY, 20),
                true  // isAuthor = true
        );

        User bidder = new User("AuthorJohn", contact, authorProfile);
        double value = 250.75;
        LocalDateTime bidDateTime = LocalDateTime.now();

        // Act
        Bid result = new Bid(bidder, value, bidDateTime);

        // Assert
        assertEquals(bidder, result.getBidder());
        assertEquals(value, result.getValue(), 0.001);
        assertEquals(bidDateTime, result.getBidDateTime());
        assertTrue(bidder.getProfile().isAuthor());
    }


    @Test
    void constructorShouldThrowExceptionWhenBidderIsNull() {
        // Arrange
        double value = 100.50;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Bid(null, value, LocalDateTime.now())
        );

        assertEquals("bidder can't be null.", exception.getMessage());
    }
}