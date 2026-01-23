package TOPSECRET.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DirectSaleDTO_US023Test {

    @Test
    void constructorShouldSetAllFieldsCorrectly() {
        // Arrange
        String expectedPublicationTitle = "SCRUM for Dummies";
        List<String> expectedAuthors = List.of("Prince");
        BigDecimal expectedPrice = new BigDecimal("19.99");
        String expectedListingTitle = "Great Condition Book";
        String expectedSeller = "anyUser";

        // Act
        DirectSaleDTO_US023 dto = new DirectSaleDTO_US023(
                expectedPublicationTitle,
                expectedAuthors,
                expectedPrice,
                expectedListingTitle,
                expectedSeller
        );

        // Assert
        assertEquals(expectedPublicationTitle, dto.publicationTitle);
        assertEquals(expectedAuthors, dto.authors);
        assertEquals(expectedPrice, dto.price);
        assertEquals(expectedListingTitle, dto.listingTitle);
        assertEquals(expectedSeller, dto.seller);
    }

    @Test
    void constructorShouldAllowNullValues() {
        // Act
        DirectSaleDTO_US023 dto = new DirectSaleDTO_US023(
                null,
                null,
                null,
                null,
                null
        );

        // Assert
        assertNull(dto.publicationTitle);
        assertNull(dto.authors);
        assertNull(dto.price);
        assertNull(dto.listingTitle);
        assertNull(dto.seller);
    }

    @Test
    void authorsListShouldBeStoredAsProvided() {
        // Arrange
        List<String> authors = List.of("Author A", "Author B");

        // Act
        DirectSaleDTO_US023 dto = new DirectSaleDTO_US023(
                "Some Title",
                authors,
                new BigDecimal("10.00"),
                "Listing Title",
                "Seller"
        );

        // Assert
        assertIterableEquals(authors, dto.authors);
    }
}