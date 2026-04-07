package TOPSECRET.domain.Enum;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link PublicationStatus} enumeration.
 * <p>
 * These tests verify the business rule implemented in
 * {@code PublicationStatus#isAvailableForSale()}, ensuring that each
 * publication status correctly reports whether a publication may be listed
 * for sale.
 */
class PublicationStatusTest {

    @Test
    void ensureSoldIsNotAvailableForSale() {
        // Arrange
        PublicationStatus status = PublicationStatus.SOLD;

        // Act
        boolean result = status.isAvailableForSale();

        // Assert
        assertFalse(result);
    }

    @Test
    void ensureForSaleIsNotAvailableForSale() {
        // Arrange
        PublicationStatus status = PublicationStatus.FOR_SALE;

        // Act
        boolean result = status.isAvailableForSale();

        // Assert
        assertFalse(result);
    }

    @Test
    void ensureReadIsAvailableForSale() {
        // Arrange
        PublicationStatus status = PublicationStatus.READ;

        // Act
        boolean result = status.isAvailableForSale();

        // Assert
        assertTrue(result);
    }

    @Test
    void ensureReadingIsAvailableForSale() {
        // Arrange
        PublicationStatus status = PublicationStatus.READING;

        // Act
        boolean result = status.isAvailableForSale();

        // Assert
        assertTrue(result);
    }
}