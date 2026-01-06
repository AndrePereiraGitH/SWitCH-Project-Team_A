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


        // Act
        Bid result = new Bid(bidder, value);

        // Assert
        assertEquals(bidder, result.getBidder());
        assertEquals(value, result.getValue(), 0.001);

                // Verify timestamp was automatically generated
        assertTrue(result.getTimeOfBid().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(result.getTimeOfBid().isAfter(LocalDateTime.now().minusSeconds(5)));

        assertNotNull(result.getTimeOfBid());

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


        // Act
        Bid result = new Bid(bidder, value);

        // Assert
        assertEquals(bidder, result.getBidder());
        assertEquals(value, result.getValue(), 0.001);

        assertNotNull(result.getTimeOfBid());

        assertTrue(bidder.getProfile().isAuthor());
    }


    @Test
    void constructorShouldThrowExceptionWhenBidderIsNull() {
        // Arrange
        double value = 100.50;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Bid(null, value)
        );

        assertEquals("Bidder can't be null.", exception.getMessage());
    }

    @Test
    void constructorShouldThrowExceptionWhenValueIsZero() {
        // Arrange
        Address address = new Address("Rua das Flores", "123", "1100-123", Country.PORTUGAL);

        Email email = new Email("joao.silva", "gmail.com");
        Contact contact = new Contact("912345678", "+351", email, address);

        Profile userProfile = new Profile(
                "João Silva Mendes",
                Country.PORTUGAL,
                LocalDate.of(2009, Month.MAY, 15),
                false
        );
        User bidder = new User("Joaogamer", contact, userProfile);
        double invalidValue = 0.0;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Bid(bidder, invalidValue)
        );

        assertEquals("Bid value must be positive.", exception.getMessage());
    }


    @Test
    void getBidInfoShouldReturnCorrectFormat() {
        // Arrange
        Address address = new Address("Calle Gran Víat", "45", "28013", Country.SPAIN);

        Email email = new Email("pedro.garcia", "yahoo.es");
        Contact contact = new Contact("654321098", "+34", email, address);

        Profile userProfile = new Profile(
                "Pedro Garcia",
                Country.PORTUGAL,
                LocalDate.of(1995, Month.NOVEMBER, 03),
                false
        );


        User bidder = new User("pedrinhoFortnite", contact, userProfile);
        double value = 123.45;

        // Act
        Bid bid = new Bid(bidder, value);
        String result = bid.getBidInfo();


        // Assert
        assertTrue(result.contains("Bidder: pedrinhoFortnite"));
        assertTrue(result.contains("Value: 123.45"));
        assertTrue(result.contains("Time: "));
        assertTrue(result.startsWith("Bidder: "));
    }



    @Test
    void shouldHandleVerySmallPositiveValue() {
        // Arrange
        Address address = new Address("Rue de Rivoli", "72", "75001", Country.FRANCE);


        Email email = new Email("sophie.martin", "outlook.fr");
        Contact contact = new Contact("0612345678", "+33", email, address);

        Profile userProfile = new Profile(
                "Sophie Martin",
                Country.PORTUGAL,
                LocalDate.of(2004, Month.JULY, 18),
                false
        );


        User bidder = new User("MarShophie", contact, userProfile);
        double smallValue = 0.01; // Small positive value


        // Act
        Bid result = new Bid(bidder, smallValue);

        // Assert
        assertEquals(smallValue, result.getValue(), 0.001);
        assertNotNull(result.getTimeOfBid());
    }


    @Test
    void shouldHandleVeryLargeValue() {
        // Arrange
        Address address = new Address("5th Avenue", "350", "10018", Country.UNITED_STATES);

        Email email = new Email("john.doe", "icloud.com");
        Contact contact = new Contact("5551234567", "+1", email, address);

        Profile userProfile = new Profile(
                "John Doe",
                Country.PORTUGAL,
                LocalDate.of(2018, Month.JANUARY, 30),
                false
        );


        User bidder = new User("Jonhnie66", contact, userProfile);
        double largeValue = 9999999.99; // Big value


        // Act
        Bid result = new Bid(bidder, largeValue);


        // Assert
        assertEquals(largeValue, result.getValue(), 0.001);
        assertNotNull(result.getTimeOfBid());
    }


    // Verifying that the bid does not have a wrong or fixed timestamp
    @Test
    void timestampShouldBeSetToCurrentTime() {
        // Arrange
        Address address = new Address("Via del Corso", "200", "00186", Country.ITALY);

        Email email = new Email("luca.rossi", "libero.it");
        Contact contact = new Contact("3334455667", "+39", email, address);

        Profile userProfile = new Profile(
                "Luca Rossi",
                Country.ITALY,
                LocalDate.of(1980, Month.APRIL, 12),
                false
        );
        User bidder = new User("LucaR", contact, userProfile);
        double value = 150.0;

        // Capture time before creation
        LocalDateTime beforeCreation = LocalDateTime.now().minusSeconds(1);

        // Act
        Bid result = new Bid(bidder, value);

        // Capture time after creation
        LocalDateTime afterCreation = LocalDateTime.now().plusSeconds(1);

        // Assert
        assertTrue(result.getTimeOfBid().isAfter(beforeCreation) || result.getTimeOfBid().isEqual(beforeCreation));
        assertTrue(result.getTimeOfBid().isBefore(afterCreation) || result.getTimeOfBid().isEqual(afterCreation));
    }


    // Verifying that each bid has a unique timestamp
    @Test
    void shouldCreateMultipleBidsWithDifferentTimestamps() throws InterruptedException {
        // Arrange
        Address address = new Address("Friedrichstraße", "110", "10117", Country.GERMANY);

        Email email = new Email("anna.muller", "web.de");
        Contact contact = new Contact("01761234567", "+49", email, address);

        Profile userProfile = new Profile(
                "Anna Müller",
                Country.GERMANY,
                LocalDate.of(1998, Month.AUGUST, 27),
                false
        );
        User bidder = new User("AnnaM", contact, userProfile);
        double value = 200.0;

        // Act
        Bid firstBid = new Bid(bidder, value);
        Thread.sleep(10); // Capture time before creation that there is a timestamp difference
        Bid secondBid = new Bid(bidder, value);

        // Assert
        assertTrue(secondBid.getTimeOfBid().isAfter(firstBid.getTimeOfBid()));
    }

}