package TOPSECRET.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link US024AucListingDTO}.
 *
 * <p>Covers the following scenarios:</p>
 * <ul>
 *   <li>Successful instantiation with valid arguments</li>
 *   <li>Null validation for each individual field</li>
 *   <li>Record contract: {@code equals}, {@code hashCode}, and {@code toString}</li>
 * </ul>
 */
class US024AucListingDTOTest {

    // -------------------------------------------------------------------------
    // Happy path
    // -------------------------------------------------------------------------

    @Test
    void shouldCreateDTOSuccessfully_whenAllFieldsAreValid() {
        // Arrange
        String listingId   = "auctionId-1";
        String price       = "99.99";
        String description = "A nice item";
        String listing     = "Listing Title";

        // Act
        US024AucListingDTO dto = new US024AucListingDTO(listingId, price, description, listing);

        // Assert
        assertEquals("auctionId-1",          dto.listingId());
        assertEquals("99.99",         dto.price());
        assertEquals("A nice item",   dto.description());
        assertEquals("Listing Title", dto.listing());
    }

    // -------------------------------------------------------------------------
    // Null guards
    // -------------------------------------------------------------------------

    @Test
    void shouldThrowNullPointerException_whenListingIdIsNull() {
        // Arrange
        String listingId   = null;
        String price       = "99.99";
        String description = "A nice item";
        String listing     = "Listing Title";

        // Act & Assert
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> new US024AucListingDTO(listingId, price, description, listing));

        assertEquals("ListingId can not be null", ex.getMessage());
    }

    @Test
    void shouldThrowNullPointerException_whenPriceIsNull() {
        // Arrange
        String listingId   = "auctionId-1";
        String price       = null;
        String description = "A nice item";
        String listing     = "Listing Title";

        // Act & Assert
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> new US024AucListingDTO(listingId, price, description, listing));

        assertEquals("Price can not be null", ex.getMessage());
    }

    @Test
    void shouldThrowNullPointerException_whenDescriptionIsNull() {
        // Arrange
        String listingId   = "auctionId-1";
        String price       = "99.99";
        String description = null;
        String listing     = "Listing Title";

        // Act & Assert
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> new US024AucListingDTO(listingId, price, description, listing));

        assertEquals("Description can not be null", ex.getMessage());
    }

    @Test
    void shouldThrowNullPointerException_whenListingIsNull() {
        // Arrange
        String listingId   = "auctionId-1";
        String price       = "99.99";
        String description = "A nice item";
        String listing     = null;

        // Act & Assert
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> new US024AucListingDTO(listingId, price, description, listing));

        assertEquals("Publication titles can not be null", ex.getMessage());
    }

    // -------------------------------------------------------------------------
    // Record behaviour
    // -------------------------------------------------------------------------

    @Test
    void shouldBeEqual_whenTwoDTOsHaveTheSameValues() {
        // Arrange
        US024AucListingDTO dto1 = new US024AucListingDTO("auctionId-1", "99.99", "A nice item", "Listing Title");
        US024AucListingDTO dto2 = new US024AucListingDTO("auctionId-1", "99.99", "A nice item", "Listing Title");

        // Act & Assert
        assertEquals(dto1, dto2);
    }

    @Test
    void shouldNotBeEqual_whenDTOsHaveDifferentValues() {
        // Arrange
        US024AucListingDTO dto1 = new US024AucListingDTO("auctionId-1", "99.99", "A nice item",      "Listing Title");
        US024AucListingDTO dto2 = new US024AucListingDTO("auctionId-2", "49.99", "A different item", "Other Title");

        // Act & Assert
        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldHaveSameHashCode_whenTwoDTOsAreEqual() {
        // Arrange
        US024AucListingDTO dto1 = new US024AucListingDTO("auctionId-1", "99.99", "A nice item", "Listing Title");
        US024AucListingDTO dto2 = new US024AucListingDTO("auctionId-1", "99.99", "A nice item", "Listing Title");

        // Act & Assert
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void shouldReturnMeaningfulToString() {
        // Arrange
        US024AucListingDTO dto = new US024AucListingDTO("auctionId-1", "99.99", "A nice item", "Listing Title");

        // Act
        String result = dto.toString();

        // Assert
        assertTrue(result.contains("auctionId-1"));
        assertTrue(result.contains("99.99"));
        assertTrue(result.contains("A nice item"));
        assertTrue(result.contains("Listing Title"));
    }
}