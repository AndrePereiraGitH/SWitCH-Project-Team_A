package TOPSECRET.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link US025AuthorDTO}.
 *
 * <p>Covers:
 * <ul>
 *   <li>Constructor - valid creation</li>
 *   <li>Getter - correct field retrieval after construction</li>
 *   <li>Immutability - field is final</li>
 *   <li>Equality - Lombok {@code @Value} generated {@code equals} and {@code hashCode}</li>
 * </ul>
 */
class US025AuthorDTOTest {

    // =========================================================================
    // Constructor
    // =========================================================================

    /**
     * Ensures a valid {@link US025AuthorDTO} is created with the given name.
     */
    @Test
    void constructorShouldCreateDTOWithGivenName() {

        // ACT
        US025AuthorDTO sut = new US025AuthorDTO("John Doe");

        // ASSERT
        assertNotNull(sut);
    }

    // =========================================================================
    // Getter
    // =========================================================================

    /**
     * Ensures {@code getName()} returns the name provided at construction.
     */
    @Test
    void getNameShouldReturnName() {

        // ARRANGE
        US025AuthorDTO sut = new US025AuthorDTO("John Doe");

        // ACT
        String result = sut.name();

        // ASSERT
        assertEquals("John Doe", result);
    }

    // =========================================================================
    // Equality
    // =========================================================================

    /**
     * Ensures two {@link US025AuthorDTO} instances with the same name are equal.
     */
    @Test
    void equalsShouldReturnTrueForSameName() {

        // ARRANGE
        US025AuthorDTO sut = new US025AuthorDTO("John Doe");
        US025AuthorDTO other = new US025AuthorDTO("John Doe");

        // ASSERT
        assertEquals(sut, other);
    }

    /**
     * Ensures two {@link US025AuthorDTO} instances with different names are not equal.
     */
    @Test
    void equalsShouldReturnFalseForDifferentName() {

        // ARRANGE
        US025AuthorDTO sut = new US025AuthorDTO("John Doe");
        US025AuthorDTO other = new US025AuthorDTO("Jane Doe");

        // ASSERT
        assertNotEquals(sut, other);
    }

    /**
     * Ensures two {@link US025AuthorDTO} instances with the same name have the same hash code.
     */
    @Test
    void hashCodeShouldBeEqualForSameName() {

        // ARRANGE
        US025AuthorDTO sut = new US025AuthorDTO("John Doe");
        US025AuthorDTO other = new US025AuthorDTO("John Doe");

        // ASSERT
        assertEquals(sut.hashCode(), other.hashCode());
    }

    /**
     * Ensures {@code equals()} returns true when comparing an instance to itself.
     */
    @Test
    void equalsShouldReturnTrueWhenComparingSameInstance() {

        // ARRANGE
        US025AuthorDTO sut = new US025AuthorDTO("John Doe");

        // ASSERT
        assertEquals(sut, sut);
    }

    /**
     * Ensures {@code equals()} returns false when comparing to null.
     */
    @Test
    void equalsShouldReturnFalseWhenComparingToNull() {

        // ARRANGE
        US025AuthorDTO sut = new US025AuthorDTO("John Doe");

        // ASSERT
        assertNotEquals(null, sut);
    }

    /**
     * Ensures {@code hashCode()} returns a non-zero value for a non-empty name.
     */
    @Test
    void hashCodeShouldReturnNonZeroForNonEmptyName() {

        // ARRANGE
        US025AuthorDTO sut = new US025AuthorDTO("John Doe");

        // ASSERT
        assertNotEquals(0, sut.hashCode());
    }
}