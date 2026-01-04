package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BidTest {
    //Test: Constructor&getFullAddress
    @Test
    void constructorShouldCreateBidWhenValid() {
        // Arrange
//        Address address = new Address("Rua do Mar", "30", "3678-769", Country.FRANCE);



//        Email email = new Email("JoanaBeauty", "@gmail.com");
//        Contact contact = new Contact("912345678", "+351", email, address);

//        User bidder = new User("Maria123", contact, "Admin");
        String bidder = "Maria123";
        double value = 100.50;
        LocalDateTime bidDateTime = LocalDateTime.now();

        // Act
        Bid result = new Bid(bidder, value, bidDateTime);

        // Assert
        assertEquals(bidder, result.getBidder());
        assertEquals(value, result.getValue(), 0.001);
        assertEquals(bidDateTime, result.getBidDateTime());
    }



//    @Test
//    void constructorShouldThrowExceptionWhenBidderIsNull() {
//        // Arrange
//        double value = 100.50;                                              // Not for now
//
//        // Act & Assert
//        IllegalArgumentException exception = assertThrows(
//                IllegalArgumentException.class,
//                () -> new Bid(null, value)
//        );
//
//        assertEquals("bidder can't be null.", exception.getMessage());
//    }
}