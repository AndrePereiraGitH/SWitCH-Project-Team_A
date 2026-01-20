package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Countries;
import TOPSECRET.domain.Enum.UserRole;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class BidTest {

    //Not an author profile
    @Test
    void constructorShouldCreateBidWhenValid() {
        // Arrange
        Address address = new Address("Main Street", "12345", "3678-769", Countries.FRANCE);


        Email email = new Email("joanabeauty", "gmail.com");
        Contact contact = new Contact("912345678", "+351", email, address);


        Profile userProfile = new Profile(
                "Maria Silva",           // name
                Countries.PORTUGAL,        // country
                LocalDate.of(1990, Month.MAY, 15) // birthday
        );


        User bidder = new User("Maria123", contact, userProfile, UserRole.NORMAL_USER);
        Price value = new Price(new BigDecimal ("100.50"));


        // Act
        Bid result = new Bid(bidder, value);

        // Assert
        assertEquals(bidder, result.getBidder());
        assertEquals(value, result.getValue());

                // Verify timestamp was automatically generated
        assertTrue(result.getTimeOfBid().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(result.getTimeOfBid().isAfter(LocalDateTime.now().minusSeconds(5)));

        assertNotNull(result.getTimeOfBid());
    }


    // Author profile
    @Test
    void constructorShouldWorkWithAuthorProfile() {
        // Arrange
        Address address = new Address("Writer's Avenue", "456", "1000-001", Countries.AUSTRALIA);

        Email email = new Email("author.john", "email.com");
        Contact contact = new Contact("912345679", "+1", email, address);

        // Criar Profile de autor
        Profile authorProfile = new Profile(
                "John Writer",
                Countries.AUSTRALIA,
                LocalDate.of(1985, Month.JANUARY, 20)
        );

        User bidder = new User("AuthorJohn", contact, authorProfile, UserRole.NORMAL_USER);
        Price value = new Price(new BigDecimal ("250.75"));


        // Act
        Bid result = new Bid(bidder, value);

        // Assert
        assertEquals(bidder, result.getBidder());
        assertEquals(value, result.getValue());

        assertNotNull(result.getTimeOfBid());
    }


    @Test
    void constructorShouldThrowExceptionWhenBidderIsNull() {
        // Arrange
        Price value = new Price(new BigDecimal ("100.50"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Bid(null, value)
        );

        assertEquals("Bidder can't be null.", exception.getMessage());
    }

    @Test
    void constructorShouldThrowExceptionWhenValueIsNull() {
        // Arrange
        Address address = new Address("Rua das Flores", "123", "1100-123", Countries.PORTUGAL);

        Email email = new Email("joao.silva", "gmail.com");
        Contact contact = new Contact("912345678", "+351", email, address);

        Profile userProfile = new Profile(
                "João Silva Mendes",
                Countries.PORTUGAL,
                LocalDate.of(2009, Month.MAY, 15)
        );
        User bidder = new User("Joaogamer", contact, userProfile, UserRole.NORMAL_USER);


        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Bid(bidder, null)
        );
        assertEquals("Bid value can't be null.", exception.getMessage());
    }


    @Test
    void getBidInfoShouldReturnCorrectFormat() {
        // Arrange
        Address address = new Address("Calle Gran Víat", "45", "28013", Countries.SPAIN);

        Email email = new Email("pedro.garcia", "yahoo.es");
        Contact contact = new Contact("654321098", "+34", email, address);

        Profile userProfile = new Profile(
                "Pedro Garcia",
                Countries.PORTUGAL,
                LocalDate.of(1995, Month.NOVEMBER, 03)
        );


        User bidder = new User("pedrinhoFortnite", contact, userProfile, UserRole.NORMAL_USER);
        Price value = new Price(new BigDecimal("123.45"));

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
        Address address = new Address("Rue de Rivoli", "72", "75001", Countries.FRANCE);


        Email email = new Email("sophie.martin", "outlook.fr");
        Contact contact = new Contact("0612345678", "+33", email, address);

        Profile userProfile = new Profile(
                "Sophie Martin",
                Countries.PORTUGAL,
                LocalDate.of(2004, Month.JULY, 18)
        );


        User bidder = new User("MarShophie", contact, userProfile, UserRole.NORMAL_USER);
        Price smallValue = new Price(new BigDecimal("0.01"));


        // Act
        Bid result = new Bid(bidder, smallValue);

        // Assert
        assertEquals(smallValue, result.getValue());
        assertNotNull(result.getTimeOfBid());
    }


    @Test
    void shouldHandleVeryLargeValue() {
        // Arrange
        Address address = new Address("5th Avenue", "350", "10018", Countries.UNITED_STATES);

        Email email = new Email("john.doe", "icloud.com");
        Contact contact = new Contact("5551234567", "+1", email, address);

        Profile userProfile = new Profile(
                "John Doe",
                Countries.PORTUGAL,
                LocalDate.of(2018, Month.JANUARY, 30)
        );


        User bidder = new User("Jonhnie66", contact, userProfile, UserRole.NORMAL_USER);
        Price largeValue = new Price(new BigDecimal("9999999.99")); // Big value


        // Act
        Bid result = new Bid(bidder, largeValue);


        // Assert
        assertEquals(largeValue, result.getValue());
        assertNotNull(result.getTimeOfBid());
    }


    // Verifying that the bid does not have a wrong or fixed timestamp
    @Test
    void timestampShouldBeSetToCurrentTime() {
        // Arrange
        Address address = new Address("Via del Corso", "200", "00186", Countries.ITALY);

        Email email = new Email("luca.rossi", "libero.it");
        Contact contact = new Contact("3334455667", "+39", email, address);

        Profile userProfile = new Profile(
                "Luca Rossi",
                Countries.ITALY,
                LocalDate.of(1980, Month.APRIL, 12)
        );
        User bidder = new User("LucaR", contact, userProfile, UserRole.NORMAL_USER);
        Price value = new Price(new BigDecimal("150.0"));

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
        Address address = new Address("Friedrichstraße", "110", "10117", Countries.GERMANY);

        Email email = new Email("anna.muller", "web.de");
        Contact contact = new Contact("01761234567", "+49", email, address);

        Profile userProfile = new Profile(
                "Anna Müller",
                Countries.GERMANY,
                LocalDate.of(1998, Month.AUGUST, 27)
        );
        User bidder = new User("AnnaM", contact, userProfile, UserRole.NORMAL_USER);
        Price value = new Price(new BigDecimal("200.0"));

        // Act
        Bid firstBid = new Bid(bidder, value);
        Thread.sleep(10); // Capture time before creation that there is a timestamp difference
        Bid secondBid = new Bid(bidder, value);

        // Assert
        assertTrue(secondBid.getTimeOfBid().isAfter(firstBid.getTimeOfBid()));
    }

    @Test
    void shouldThrowExceptionWhenPriceIsInvalid() {
        // Arrange
        Address address = new Address("Test Street", "123", "1234-567", Countries.PORTUGAL);
        Email email = new Email("test", "test.com");
        Contact contact = new Contact("912345678", "+351", email, address);

        Profile userProfile = new Profile(
                "André Simões",
                Countries.PORTUGAL,
                LocalDate.of(1990, Month.JANUARY, 1)
        );
        User bidder = new User("noteme", contact, userProfile, UserRole.NORMAL_USER);

        // Act & Assert - Tests if Price rejects negative value
        assertThrows(
                IllegalArgumentException.class,
                () -> new Price(new BigDecimal("-10.00"))
        );

        // Act & Assert - Tests if Price rejects values with more than 2 decimals
        assertThrows(
                IllegalArgumentException.class,
                () -> new Price(new BigDecimal("10.123"))
        );
    }
}