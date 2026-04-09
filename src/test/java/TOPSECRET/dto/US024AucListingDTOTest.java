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
        US024AucListingDTO dto = new US024AucListingDTO("id-1", "99.99", "A nice item", "Listing Title");

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
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> new US024AucListingDTO(null, "99.99", "A nice item", "Listing Title"));

        assertEquals("ListingId can not be null", ex.getMessage());
    }

    @Test
    void shouldThrowNullPointerException_whenPriceIsNull() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> new US024AucListingDTO("id-1", null, "A nice item", "Listing Title"));

        assertEquals("Price can not be null", ex.getMessage());
    }

    @Test
    void shouldThrowNullPointerException_whenDescriptionIsNull() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> new US024AucListingDTO("id-1", "99.99", null, "Listing Title"));

        assertEquals("Description can not be null", ex.getMessage());
    }

    @Test
    void shouldThrowNullPointerException_whenListingIsNull() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> new US024AucListingDTO("id-1", "99.99", "A nice item", null));

        assertEquals("Publication titles can not be null", ex.getMessage());
    }

    // -------------------------------------------------------------------------
    // Record behaviour
    // -------------------------------------------------------------------------

    @Test
    void shouldBeEqual_whenTwoDTOsHaveTheSameValues() {
        US024AucListingDTO dto1 = new US024AucListingDTO("id-1", "99.99", "A nice item", "Listing Title");
        US024AucListingDTO dto2 = new US024AucListingDTO("id-1", "99.99", "A nice item", "Listing Title");

        assertEquals(dto1, dto2);
    }

    @Test
    void shouldNotBeEqual_whenDTOsHaveDifferentValues() {
        US024AucListingDTO dto1 = new US024AucListingDTO("id-1", "99.99", "A nice item",      "Listing Title");
        US024AucListingDTO dto2 = new US024AucListingDTO("id-2", "49.99", "A different item", "Other Title");

        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldHaveSameHashCode_whenTwoDTOsAreEqual() {
        US024AucListingDTO dto1 = new US024AucListingDTO("id-1", "99.99", "A nice item", "Listing Title");
        US024AucListingDTO dto2 = new US024AucListingDTO("id-1", "99.99", "A nice item", "Listing Title");

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void shouldReturnMeaningfulToString() {
        US024AucListingDTO dto = new US024AucListingDTO("id-1", "99.99", "A nice item", "Listing Title");

        String result = dto.toString();

        assertTrue(result.contains("id-1"));
        assertTrue(result.contains("99.99"));
        assertTrue(result.contains("A nice item"));
        assertTrue(result.contains("Listing Title"));
    }
}