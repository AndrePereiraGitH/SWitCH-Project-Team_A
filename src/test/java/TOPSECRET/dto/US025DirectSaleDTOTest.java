package TOPSECRET.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link US025DirectSaleDTO}.
 *
 * <p>Covers:
 * <ul>
 *   <li>Constructor - valid creation</li>
 *   <li>Getters - correct field retrieval after construction</li>
 *   <li>Immutability - all fields are final</li>
 *   <li>Equality - Lombok {@code @Value} generated {@code equals} and {@code hashCode}</li>
 * </ul>
 */
class US025DirectSaleDTOTest {

    private static final String PUBLICATION_TITLE = "Clean Code";
    private static final List<String> AUTHORS = List.of("Robert Martin");
    private static final BigDecimal PRICE = new BigDecimal("29.99");
    private static final String LISTING_TITLE = "Clean Code - Good Condition";
    private static final String SELLER = "seller123";

    // =========================================================================
    // Constructor
    // =========================================================================

    /**
     * Ensures a valid {@link US025DirectSaleDTO} is created with all given fields.
     */
    @Test
    void constructorShouldCreateDTOWithGivenFields() {

        // ACT
        US025DirectSaleDTO sut = new US025DirectSaleDTO(
                PUBLICATION_TITLE, AUTHORS, PRICE, LISTING_TITLE, SELLER);

        // ASSERT
        assertNotNull(sut);
    }

    // =========================================================================
    // Getters
    // =========================================================================

    /**
     * Ensures {@code getPublicationTitle()} returns the title provided at construction.
     */
    @Test
    void getPublicationTitleShouldReturnPublicationTitle() {

        // ARRANGE
        US025DirectSaleDTO sut = new US025DirectSaleDTO(
                PUBLICATION_TITLE, AUTHORS, PRICE, LISTING_TITLE, SELLER);

        // ACT
        String result = sut.getPublicationTitle();

        // ASSERT
        assertEquals(PUBLICATION_TITLE, result);
    }

    /**
     * Ensures {@code getAuthors()} returns the authors provided at construction.
     */
    @Test
    void getAuthorsShouldReturnAuthors() {

        // ARRANGE
        US025DirectSaleDTO sut = new US025DirectSaleDTO(
                PUBLICATION_TITLE, AUTHORS, PRICE, LISTING_TITLE, SELLER);

        // ACT
        List<String> result = sut.getAuthors();

        // ASSERT
        assertEquals(AUTHORS, result);
    }

    /**
     * Ensures {@code getPrice()} returns the price provided at construction.
     */
    @Test
    void getPriceShouldReturnPrice() {

        // ARRANGE
        US025DirectSaleDTO sut = new US025DirectSaleDTO(
                PUBLICATION_TITLE, AUTHORS, PRICE, LISTING_TITLE, SELLER);

        // ACT
        BigDecimal result = sut.getPrice();

        // ASSERT
        assertEquals(PRICE, result);
    }

    /**
     * Ensures {@code getListingTitle()} returns the listing title provided at construction.
     */
    @Test
    void getListingTitleShouldReturnListingTitle() {

        // ARRANGE
        US025DirectSaleDTO sut = new US025DirectSaleDTO(
                PUBLICATION_TITLE, AUTHORS, PRICE, LISTING_TITLE, SELLER);

        // ACT
        String result = sut.getListingTitle();

        // ASSERT
        assertEquals(LISTING_TITLE, result);
    }

    /**
     * Ensures {@code getSeller()} returns the seller provided at construction.
     */
    @Test
    void getSellerShouldReturnSeller() {

        // ARRANGE
        US025DirectSaleDTO sut = new US025DirectSaleDTO(
                PUBLICATION_TITLE, AUTHORS, PRICE, LISTING_TITLE, SELLER);

        // ACT
        String result = sut.getSeller();

        // ASSERT
        assertEquals(SELLER, result);
    }

    // =========================================================================
    // Immutability
    // =========================================================================

    /**
     * Ensures all fields are declared as {@code final}.
     */
    @Test
    void allFieldsShouldBeFinal() throws NoSuchFieldException {

        // ARRANGE
        String[] fieldNames = {"publicationTitle", "authors", "price", "listingTitle", "seller"};

        for (String fieldName : fieldNames) {

            // ACT
            var field = US025DirectSaleDTO.class.getDeclaredField(fieldName);

            // ASSERT
            assertTrue(java.lang.reflect.Modifier.isFinal(field.getModifiers()),
                    "Field " + fieldName + " should be final");
        }
    }

    // =========================================================================
    // Equality
    // =========================================================================

    /**
     * Ensures two {@link US025DirectSaleDTO} instances with the same fields are equal.
     */
    @Test
    void equalsShouldReturnTrueForSameFields() {

        // ARRANGE
        US025DirectSaleDTO sut = new US025DirectSaleDTO(
                PUBLICATION_TITLE, AUTHORS, PRICE, LISTING_TITLE, SELLER);
        US025DirectSaleDTO other = new US025DirectSaleDTO(
                PUBLICATION_TITLE, AUTHORS, PRICE, LISTING_TITLE, SELLER);

        // ASSERT
        assertEquals(sut, other);
    }

    /**
     * Ensures two {@link US025DirectSaleDTO} instances with different titles are not equal.
     */
    @Test
    void equalsShouldReturnFalseForDifferentPublicationTitle() {

        // ARRANGE
        US025DirectSaleDTO sut = new US025DirectSaleDTO(
                PUBLICATION_TITLE, AUTHORS, PRICE, LISTING_TITLE, SELLER);
        US025DirectSaleDTO other = new US025DirectSaleDTO(
                "Different Title", AUTHORS, PRICE, LISTING_TITLE, SELLER);

        // ASSERT
        assertNotEquals(sut, other);
    }

    /**
     * Ensures two {@link US025DirectSaleDTO} instances with the same fields have the same hash code.
     */
    @Test
    void hashCodeShouldBeEqualForSameFields() {

        // ARRANGE
        US025DirectSaleDTO sut = new US025DirectSaleDTO(
                PUBLICATION_TITLE, AUTHORS, PRICE, LISTING_TITLE, SELLER);
        US025DirectSaleDTO other = new US025DirectSaleDTO(
                PUBLICATION_TITLE, AUTHORS, PRICE, LISTING_TITLE, SELLER);

        // ASSERT
        assertEquals(sut.hashCode(), other.hashCode());
    }
}