package TOPSECRET.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class US021DirectSaleDTOTest {

    // ============================================================
    // Successful Creation Tests
    // ============================================================

    @Test
    void shouldCreateDTOWithValidInputs() {
        // Arrange
        String listingId = "listing-123";
        String listingTitle = "Harry Potter and the Philosopher's Stone";
        BigDecimal price = new BigDecimal("15.99");
        String shortDescription = "Book in excellent condition";
        String sellerId = "seller-456";

        // Act
        US021DirectSaleDTO dto = new US021DirectSaleDTO(
                listingId, listingTitle, price, shortDescription, sellerId
        );

        // Assert
        assertEquals(listingId, dto.getListingId());
        assertEquals(listingTitle, dto.getListingTitle());
        assertEquals(price, dto.getPrice());
        assertEquals(shortDescription, dto.getShortDescription());
        assertEquals(sellerId, dto.getSellerId());
    }

    @Test
    void shouldCreateDTOWithZeroPrice() {
        // Arrange
        String listingId = "listing-123";
        String listingTitle = "Free Book";
        BigDecimal price = BigDecimal.ZERO;
        String shortDescription = "Free to a good home";
        String sellerId = "seller-456";

        // Act
        US021DirectSaleDTO dto = new US021DirectSaleDTO(
                listingId, listingTitle, price, shortDescription, sellerId
        );

        // Assert
        assertEquals(listingId, dto.getListingId());
        assertEquals(listingTitle, dto.getListingTitle());
        assertEquals(BigDecimal.ZERO, dto.getPrice());
        assertEquals(shortDescription, dto.getShortDescription());
        assertEquals(sellerId, dto.getSellerId());
    }

    @Test
    void shouldCreateDTOWithEmptyShortDescription() {
        // Arrange
        String listingId = "listing-123";
        String listingTitle = "Book";
        BigDecimal price = new BigDecimal("10.00");
        String shortDescription = "";
        String sellerId = "seller-456";

        // Act
        US021DirectSaleDTO dto = new US021DirectSaleDTO(
                listingId, listingTitle, price, shortDescription, sellerId
        );

        // Assert
        assertEquals(listingId, dto.getListingId());
        assertEquals(listingTitle, dto.getListingTitle());
        assertEquals(price, dto.getPrice());
        assertEquals("", dto.getShortDescription());
        assertEquals(sellerId, dto.getSellerId());
    }

    // ============================================================
    // Validation Tests - Null Values
    // ============================================================

    @Test
    void shouldThrowExceptionWhenListingIdIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new US021DirectSaleDTO(null, "Title", new BigDecimal("10"), "Desc", "seller")
        );
        assertEquals("listingId cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenListingTitleIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new US021DirectSaleDTO("id", null, new BigDecimal("10"), "Desc", "seller")
        );
        assertEquals("listingTitle cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new US021DirectSaleDTO("id", "Title", null, "Desc", "seller")
        );
        assertEquals("price cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenShortDescriptionIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new US021DirectSaleDTO("id", "Title", new BigDecimal("10"), null, "seller")
        );
        assertEquals("shortDescription cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSellerIdIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new US021DirectSaleDTO("id", "Title", new BigDecimal("10"), "Desc", null)
        );
        assertEquals("sellerId cannot be null", exception.getMessage());
    }

    // ============================================================
    // equals() Tests
    // ============================================================

    @Test
    void shouldReturnTrueWhenSameObject() {
        US021DirectSaleDTO dto = new US021DirectSaleDTO(
                "id", "Title", new BigDecimal("10"), "Desc", "seller"
        );
        assertEquals(dto, dto);
    }

    @Test
    void shouldReturnTrueWhenAllFieldsAreEqual() {
        US021DirectSaleDTO dto1 = new US021DirectSaleDTO(
                "id", "Title", new BigDecimal("10"), "Desc", "seller"
        );
        US021DirectSaleDTO dto2 = new US021DirectSaleDTO(
                "id", "Title", new BigDecimal("10"), "Desc", "seller"
        );
        assertEquals(dto1, dto2);
    }

    @Test
    void shouldReturnFalseWhenListingIdIsDifferent() {
        US021DirectSaleDTO dto1 = new US021DirectSaleDTO(
                "id1", "Title", new BigDecimal("10"), "Desc", "seller"
        );
        US021DirectSaleDTO dto2 = new US021DirectSaleDTO(
                "id2", "Title", new BigDecimal("10"), "Desc", "seller"
        );
        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldReturnFalseWhenListingTitleIsDifferent() {
        US021DirectSaleDTO dto1 = new US021DirectSaleDTO(
                "id", "Title1", new BigDecimal("10"), "Desc", "seller"
        );
        US021DirectSaleDTO dto2 = new US021DirectSaleDTO(
                "id", "Title2", new BigDecimal("10"), "Desc", "seller"
        );
        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldReturnFalseWhenPriceIsDifferent() {
        US021DirectSaleDTO dto1 = new US021DirectSaleDTO(
                "id", "Title", new BigDecimal("10"), "Desc", "seller"
        );
        US021DirectSaleDTO dto2 = new US021DirectSaleDTO(
                "id", "Title", new BigDecimal("20"), "Desc", "seller"
        );
        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldReturnFalseWhenShortDescriptionIsDifferent() {
        US021DirectSaleDTO dto1 = new US021DirectSaleDTO(
                "id", "Title", new BigDecimal("10"), "Desc1", "seller"
        );
        US021DirectSaleDTO dto2 = new US021DirectSaleDTO(
                "id", "Title", new BigDecimal("10"), "Desc2", "seller"
        );
        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldReturnFalseWhenSellerIdIsDifferent() {
        US021DirectSaleDTO dto1 = new US021DirectSaleDTO(
                "id", "Title", new BigDecimal("10"), "Desc", "seller1"
        );
        US021DirectSaleDTO dto2 = new US021DirectSaleDTO(
                "id", "Title", new BigDecimal("10"), "Desc", "seller2"
        );
        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldReturnFalseWhenObjectIsNull() {
        US021DirectSaleDTO dto = new US021DirectSaleDTO(
                "id", "Title", new BigDecimal("10"), "Desc", "seller"
        );
        assertNotEquals(null, dto);
    }

    @Test
    void shouldReturnFalseWhenDifferentClass() {
        US021DirectSaleDTO dto = new US021DirectSaleDTO(
                "id", "Title", new BigDecimal("10"), "Desc", "seller"
        );
        String differentObject = "not a DTO";
        assertNotEquals(dto, differentObject);
    }

    // ============================================================
    // hashCode() Tests
    // ============================================================

    @Test
    void shouldHaveSameHashCodeWhenObjectsAreEqual() {
        US021DirectSaleDTO dto1 = new US021DirectSaleDTO(
                "id", "Title", new BigDecimal("10"), "Desc", "seller"
        );
        US021DirectSaleDTO dto2 = new US021DirectSaleDTO(
                "id", "Title", new BigDecimal("10"), "Desc", "seller"
        );
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void shouldHaveDifferentHashCodeWhenListingIdDiffers() {
        US021DirectSaleDTO dto1 = new US021DirectSaleDTO(
                "id1", "Title", new BigDecimal("10"), "Desc", "seller"
        );
        US021DirectSaleDTO dto2 = new US021DirectSaleDTO(
                "id2", "Title", new BigDecimal("10"), "Desc", "seller"
        );
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    // ============================================================
    // toString() Tests
    // ============================================================

    @Test
    void toStringShouldNotBeNull() {
        US021DirectSaleDTO dto = new US021DirectSaleDTO(
                "id", "Title", new BigDecimal("10"), "Desc", "seller"
        );
        assertNotNull(dto.toString());
    }

    @Test
    void toStringShouldContainClassName() {
        US021DirectSaleDTO dto = new US021DirectSaleDTO(
                "id", "Title", new BigDecimal("10"), "Desc", "seller"
        );
        assertTrue(dto.toString().contains("US021DirectSaleDTO"));
    }

    @Test
    void toStringShouldContainAllFields() {
        US021DirectSaleDTO dto = new US021DirectSaleDTO(
                "id", "Title", new BigDecimal("10"), "Desc", "seller"
        );
        String result = dto.toString();
        assertTrue(result.contains("listingId"));
        assertTrue(result.contains("listingTitle"));
        assertTrue(result.contains("price"));
        assertTrue(result.contains("shortDescription"));
        assertTrue(result.contains("sellerId"));
    }

    @Test
    void toStringShouldContainShortDescription() {
        US021DirectSaleDTO dto = new US021DirectSaleDTO(
                "id", "Title", new BigDecimal("10"), "expected-desc", "seller"
        );
        assertTrue(dto.toString().contains("expected-desc"));
    }

    @Test
    void toStringShouldContainSellerId() {
        US021DirectSaleDTO dto = new US021DirectSaleDTO(
                "id", "Title", new BigDecimal("10"), "Desc", "expected-seller"
        );
        assertTrue(dto.toString().contains("expected-seller"));
    }

    // ============================================================
    // Getter Tests
    // ============================================================

    @Test
    void shouldReturnCorrectListingId() {
        US021DirectSaleDTO dto = new US021DirectSaleDTO(
                "expected-id", "Title", new BigDecimal("10"), "Desc", "seller"
        );
        assertEquals("expected-id", dto.getListingId());
    }

    @Test
    void shouldReturnCorrectListingTitle() {
        US021DirectSaleDTO dto = new US021DirectSaleDTO(
                "id", "expected-title", new BigDecimal("10"), "Desc", "seller"
        );
        assertEquals("expected-title", dto.getListingTitle());
    }

    @Test
    void shouldReturnCorrectPrice() {
        BigDecimal expected = new BigDecimal("25.50");
        US021DirectSaleDTO dto = new US021DirectSaleDTO(
                "id", "Title", expected, "Desc", "seller"
        );
        assertEquals(expected, dto.getPrice());
    }

    @Test
    void shouldReturnCorrectShortDescription() {
        US021DirectSaleDTO dto = new US021DirectSaleDTO(
                "id", "Title", new BigDecimal("10"), "expected-desc", "seller"
        );
        assertEquals("expected-desc", dto.getShortDescription());
    }

    @Test
    void shouldReturnCorrectSellerId() {
        US021DirectSaleDTO dto = new US021DirectSaleDTO(
                "id", "Title", new BigDecimal("10"), "Desc", "expected-seller"
        );
        assertEquals("expected-seller", dto.getSellerId());
    }
}