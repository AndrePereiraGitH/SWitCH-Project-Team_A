package TOPSECRET.domain;

import TOPSECRET.domain.ValueObjects.Price;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BidTest {

    // --- Testes de construção válida ------------------------
    @Test
    void constructorShouldCreateBidWhenValid() {

        // Arrange
        User biderDouble = mock(User.class);
        when(biderDouble.getUserName()).thenReturn("Bider");

        Profile userProfile = mock(Profile.class);
        when(biderDouble.getProfile()).thenReturn(userProfile);
        when(userProfile.getName()).thenReturn("Maria123");

        Price valueDouble = mock(Price.class);

        LocalDateTime beforeCreation = LocalDateTime.now(); // captura antes da criação da Bid

        //Act
        Bid result = new Bid(biderDouble, valueDouble);

        LocalDateTime afterCreation = LocalDateTime.now(); // captura depois da criação da Bid

        //Assert
        assertEquals(biderDouble, result.getBidder());
        assertEquals(valueDouble, result.getValue());

        // Verifica que o tempo da bid foi criado entre "antes" e "depois"
        assertNotNull(result.getTimeOfBid());
        assertFalse(result.getTimeOfBid().isBefore(beforeCreation));
        assertFalse(result.getTimeOfBid().isAfter(afterCreation));
    }

    @Test
    void constructorShouldWorkWithAuthorProfile() {
        // Arrange
        User biderDouble = mock(User.class);
        when(biderDouble.getUserName()).thenReturn("Bider");

        Profile authorProfile = mock(Profile.class);
        when(biderDouble.getProfile()).thenReturn(authorProfile);
        when(authorProfile.getName()).thenReturn("João Marques");

        Price valueDouble = mock(Price.class);

        LocalDateTime beforeCreation = LocalDateTime.now(); // captures time before the creation of bid

        //Act
        Bid result = new Bid(biderDouble, valueDouble);

        LocalDateTime afterCreation = LocalDateTime.now(); // captures time after the creation of bid

        //Assert
        assertEquals(biderDouble, result.getBidder());
        assertEquals(valueDouble, result.getValue());

        // Check that the bid time was created between "before" and "after"
        assertNotNull(result.getTimeOfBid());
        assertFalse(result.getTimeOfBid().isBefore(beforeCreation));
        assertFalse(result.getTimeOfBid().isAfter(afterCreation));
    }

    // ---- Testes de validação do construtor ------------------
    @Test
    void constructorShouldThrowExceptionWhenBidderIsNull() {
        // Arrange
        Price valueDouble = mock(Price.class);

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Bid(null, valueDouble)
        );


        // Assert
        assertEquals("Bidder can't be null.", exception.getMessage());
    }

    @Test
    void constructorShouldThrowExceptionWhenValueIsNull() {
        // Arrange
        User bidder = mock(User.class);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Bid(bidder, null)
        );
        assertEquals("Bid value can't be null.", exception.getMessage());
    }

    @Test
    void timestampShouldBeSetToCurrentTime() {
        // Arrange
        User bidder = mock(User.class);
        Price value = mock(Price.class);

        LocalDateTime before = LocalDateTime.now();

        // Act
        Bid result = new Bid(bidder, value);

        LocalDateTime after = LocalDateTime.now();

        // Assert
        assertFalse(result.getTimeOfBid().isBefore(before));
        assertFalse(result.getTimeOfBid().isAfter(after));
    }

    @Test
    void shouldCreateMultipleBidsWithDifferentTimestamps() throws InterruptedException {
        // Arrange
        User bidder = mock(User.class);
        Price value = mock(Price.class);

        // Act
        Bid firstBid = new Bid(bidder, value);
        Thread.sleep(10);
        Bid secondBid = new Bid(bidder, value);


        // Assert
        assertTrue(secondBid.getTimeOfBid().isAfter(firstBid.getTimeOfBid()));
    }

    // ---- Teste de getBidInfo --------------------------

    @Test
    void getBidInfoShouldReturnCorrectFormat() {
        // Arrange
        User biderDouble = mock(User.class);
        when(biderDouble.getUserName()).thenReturn("Bider");

        Price valueDouble = mock(Price.class);
        when(valueDouble.getPrice()).thenReturn(new BigDecimal("180.00"));

        //Act
        Bid bid = new Bid(biderDouble, valueDouble);
        String result = bid.getBidInfo();

        //Assert
        assertTrue(result.contains("Bidder: Bider"));
        assertTrue(result.contains("Value: 180.00"));
        assertTrue(result.contains("Time: "));
        assertTrue(result.startsWith("Bidder: "));
    }


    // ---  Testes de valores extremos ------------------
    @Test
    void shouldHandleVerySmallPositiveValue() {
        // Arrange
        User bidder = mock(User.class);
        Price smallValue = new Price(new BigDecimal("0.01"));

        // Act
        Bid result = new Bid(bidder, smallValue);

        // Assert
        assertEquals(smallValue, result.getValue());
    }

    @Test
    void shouldHandleVeryLargeValue() {
        // Arrange
        User bidder = mock(User.class);
        Price largeValue = new Price(new BigDecimal("9999999.99"));

        // Act
        Bid result = new Bid(bidder, largeValue);

        // Assert
        assertEquals(largeValue, result.getValue());
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNegative() {
        // Arrange
        BigDecimal negativeValue = new BigDecimal("-10.00");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Price(negativeValue));
    }

    @Test
    void shouldThrowExceptionWhenPriceHasMoreThanTwoDecimals() {
        // Arrange
        BigDecimal tooManyDecimals = new BigDecimal("10.123");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Price(tooManyDecimals));
    }

}