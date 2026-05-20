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
        String result = sut.publicationTitle();

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
        List<String> result = sut.authors();

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
        BigDecimal result = sut.price();

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
        String result = sut.listingTitle();

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
        String result = sut.seller();

        // ASSERT
        assertEquals(SELLER, result);
    }
}