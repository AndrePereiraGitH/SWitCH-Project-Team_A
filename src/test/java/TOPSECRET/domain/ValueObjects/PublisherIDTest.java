package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PublisherIDTest {

    // -------------------------------------------------------------------------
    // Construction — happy path
    // -------------------------------------------------------------------------

    @Test
    void givenValidId_whenConstructing_thenCreatesSuccessfully() {
        new PublisherID("123456789");
    }

    @Test
    void givenValidId_whenGetId_thenReturnsCorrectId() {
        // Arrange
        PublisherID publisherID = new PublisherID("123456789");
        // Act
        String result = publisherID.getId();
        // Assert
        assertEquals("123456789", result);
    }

    // -------------------------------------------------------------------------
    // Construction — invalid input
    // -------------------------------------------------------------------------

    @Test
    void givenNullId_whenConstructing_thenThrowsIllegalArgumentException() {
        // Act
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> new PublisherID(null)
        );
        // Assert
        assertEquals("PublisherId must be a non empty String", ex.getMessage());
    }

    @Test
    void givenBlankId_whenConstructing_thenThrowsIllegalArgumentException() {
        // Act
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> new PublisherID("   ")
        );
        // Assert
        assertEquals("PublisherId must be a non empty String", ex.getMessage());
    }

    // -------------------------------------------------------------------------
    // Equality contract
    // -------------------------------------------------------------------------

    @Test
    void givenSameInstance_whenEquals_thenReturnsTrue() {
        // Arrange
        PublisherID publisherID = new PublisherID("123456789");
        // Act + Assert
        assertEquals(publisherID, publisherID);
    }

    @Test
    void givenTwoIdsWithSameValue_whenEquals_thenReturnsTrue() {
        // Arrange
        PublisherID first = new PublisherID("123456789");
        PublisherID second = new PublisherID("123456789");
        // Assert
        assertEquals(first, second);
    }

    @Test
    void givenTwoIdsWithDifferentValues_whenEquals_thenReturnsFalse() {
        // Arrange
        PublisherID first = new PublisherID("123456789");
        PublisherID second = new PublisherID("987654321");
        // Assert
        assertNotEquals(first, second);
    }

    @Test
    void givenNull_whenEquals_thenReturnsFalse() {
        // Arrange
        PublisherID publisherID = new PublisherID("123456789");
        // Assert
        assertNotEquals(null, publisherID);
    }

    @Test
    void givenDifferentType_whenEquals_thenReturnsFalse() {
        // Arrange
        PublisherID publisherID = new PublisherID("123456789");
        // Assert
        assertNotEquals("123456789", publisherID);
    }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Test
    void givenValidId_whenToString_thenReturnsRawId() {
        // Arrange
        PublisherID publisherID = new PublisherID("123456789");
        // Assert
        assertEquals("123456789", publisherID.toString());
    }

    @Test
    void givenValidId_whenToStringAndGetId_thenBothMatch() {
        // Arrange
        PublisherID publisherID = new PublisherID("123456789");
        // Assert
        assertEquals(publisherID.getId(), publisherID.toString());
    }

    // -------------------------------------------------------------------------
    // Immutability
    // -------------------------------------------------------------------------

    @Test
    void givenValidId_whenGetIdCalledMultipleTimes_thenAlwaysReturnsSameValue() {
        // Arrange
        PublisherID publisherID = new PublisherID("123456789");
        // Assert
        assertEquals(publisherID.getId(), publisherID.getId());
    }
}
