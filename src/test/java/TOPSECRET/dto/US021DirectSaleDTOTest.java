package TOPSECRET.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class US021DirectSaleDTOTest {

    // =====================
    // Null validation tests
    // =====================
    @Test
    void shouldThrowExceptionWhenListingIdIsNull() {
        // Arrange
        String listingId = null;
        String listingTitle = "Title";
        BigDecimal price = new BigDecimal("10");
        String shortDescription = "Desc";
        String sellerId = "seller";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US021DirSaleListingDTO(listingId, listingTitle, price, shortDescription, sellerId));
    }

    // ============
    // equals tests - CORRIGIDOS
    // ============
    @Test
    void shouldReturnTrueWhenSameObject() {
        // Arrange
        US021DirSaleListingDTO dto = new US021DirSaleListingDTO(
                "id", "Title", new BigDecimal("10"), "Desc", "seller");

        // Act
        boolean result = dto.equals(dto);

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenAllFieldsAreEqual() {
        // Arrange
        US021DirSaleListingDTO dto1 = new US021DirSaleListingDTO(
                "id", "Title", new BigDecimal("10"), "Desc", "seller");
        US021DirSaleListingDTO dto2 = new US021DirSaleListingDTO(
                "id", "Title", new BigDecimal("10"), "Desc", "seller");

        // Act
        boolean result = dto1.equals(dto2);

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenListingIdIsDifferent() {
        // Arrange
        US021DirSaleListingDTO dto1 = new US021DirSaleListingDTO(
                "id1", "Title", new BigDecimal("10"), "Desc", "seller");
        US021DirSaleListingDTO dto2 = new US021DirSaleListingDTO(
                "id2", "Title", new BigDecimal("10"), "Desc", "seller");

        // Act
        boolean result = dto1.equals(dto2);

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenObjectIsNull() {
        // Arrange
        US021DirSaleListingDTO dto = new US021DirSaleListingDTO(
                "id", "Title", new BigDecimal("10"), "Desc", "seller");

        // Act
        boolean result = dto.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenDifferentClass() {
        // Arrange
        US021DirSaleListingDTO dto = new US021DirSaleListingDTO(
                "id", "Title", new BigDecimal("10"), "Desc", "seller");

        // Act
        boolean result = dto.equals("not a DTO");

        // Assert
        assertFalse(result);
    }

    // ===================
    // hashCode tests
    // ====================
    @Test
    void shouldHaveSameHashCodeWhenObjectsAreEqual() {
        // Arrange
        US021DirSaleListingDTO dto1 = new US021DirSaleListingDTO(
                "id", "Title", new BigDecimal("10"), "Desc", "seller");
        US021DirSaleListingDTO dto2 = new US021DirSaleListingDTO(
                "id", "Title", new BigDecimal("10"), "Desc", "seller");

        // Act
        int hashCode1 = dto1.hashCode();
        int hashCode2 = dto2.hashCode();

        // Assert
        assertEquals(hashCode1, hashCode2);
    }

    // ==============
    // toString tests
    // =============
    @Test
    void toStringShouldNotBeNull() {
        // Arrange
        US021DirSaleListingDTO dto = new US021DirSaleListingDTO(
                "id", "Title", new BigDecimal("10"), "Desc", "seller");

        // Act
        String result = dto.toString();

        // Assert
        assertNotNull(result);
    }

    @Test
    void toStringShouldContainAllFields() {
        // Arrange
        US021DirSaleListingDTO dto = new US021DirSaleListingDTO(
                "id", "Title", new BigDecimal("10"), "Desc", "seller");

        // Act
        String result = dto.toString();

        // Assert
        assertTrue(result.contains("listingId"));
        assertTrue(result.contains("listingTitle"));
        assertTrue(result.contains("price"));
        assertTrue(result.contains("shortDescription"));
        assertTrue(result.contains("sellerId"));
    }
}