package TOPSECRET.dto;

import TOPSECRET.dto.listing.US028ListingDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link US028ListingDTO}.
 *
 * <p>Covers the following scenarios:</p>
 * <ul>
 *   <li>Successful instantiation with valid arguments</li>
 *   <li>Null validation for each individual field</li>
 *   <li>Record contract: {@code equals}, {@code hashCode}, and {@code toString}</li>
 * </ul>
 */
class US028ListingDTOTest {

    // -------------------------------------------------------------------------
    // Happy path
    // -------------------------------------------------------------------------

    @Test
    void shouldCreateDTOSuccessfully_whenAllFieldsAreValid() {
        // Arrange
        String listingId   = "id-1";
        String price       = "99.99";
        String description = "A nice item";
        String listing     = "Listing Title";

        // Act
        US028ListingDTO dto = new US028ListingDTO(listingId, price, description, listing);

        // Assert
        assertEquals("id-1",          dto.listingId());
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
                () -> new US028ListingDTO(listingId, price, description, listing));

        assertEquals("ListingId can not be null", ex.getMessage());
    }

    @Test
    void shouldThrowNullPointerException_whenPriceIsNull() {
        // Arrange
        String listingId   = "id-1";
        String price       = null;
        String description = "A nice item";
        String listing     = "Listing Title";

        // Act & Assert
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> new US028ListingDTO(listingId, price, description, listing));

        assertEquals("Price can not be null", ex.getMessage());
    }

    @Test
    void shouldThrowNullPointerException_whenDescriptionIsNull() {
        // Arrange
        String listingId   = "id-1";
        String price       = "99.99";
        String description = null;
        String listing     = "Listing Title";

        // Act & Assert
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> new US028ListingDTO(listingId, price, description, listing));

        assertEquals("Description can not be null", ex.getMessage());
    }

    @Test
    void shouldThrowNullPointerException_whenListingIsNull() {
        // Arrange
        String listingId   = "id-1";
        String price       = "99.99";
        String description = "A nice item";
        String listing     = null;

        // Act & Assert
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> new US028ListingDTO(listingId, price, description, listing));

        assertEquals("Publication titles can not be null", ex.getMessage());
    }

    // -------------------------------------------------------------------------
    // Record behaviour
    // -------------------------------------------------------------------------

    @Test
    void shouldBeEqual_whenTwoDTOsHaveTheSameValues() {
        // Arrange
        US028ListingDTO dto1 = new US028ListingDTO("id-1", "99.99", "A nice item", "Listing Title");
        US028ListingDTO dto2 = new US028ListingDTO("id-1", "99.99", "A nice item", "Listing Title");

        // Act & Assert
        assertEquals(dto1, dto2);
    }

    @Test
    void shouldNotBeEqual_whenDTOsHaveDifferentValues() {
        // Arrange
        US028ListingDTO dto1 = new US028ListingDTO("id-1", "99.99", "A nice item",      "Listing Title");
        US028ListingDTO dto2 = new US028ListingDTO("id-2", "49.99", "A different item", "Other Title");

        // Act & Assert
        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldHaveSameHashCode_whenTwoDTOsAreEqual() {
        // Arrange
        US028ListingDTO dto1 = new US028ListingDTO("id-1", "99.99", "A nice item", "Listing Title");
        US028ListingDTO dto2 = new US028ListingDTO("id-1", "99.99", "A nice item", "Listing Title");

        // Act & Assert
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void shouldReturnMeaningfulToString() {
        // Arrange
        US028ListingDTO dto = new US028ListingDTO("id-1", "99.99", "A nice item", "Listing Title");

        // Act
        String result = dto.toString();

        // Assert
        assertTrue(result.contains("id-1"));
        assertTrue(result.contains("99.99"));
        assertTrue(result.contains("A nice item"));
        assertTrue(result.contains("Listing Title"));
    }
}