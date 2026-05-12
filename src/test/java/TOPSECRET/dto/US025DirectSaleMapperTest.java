package TOPSECRET.dto;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link US025DirectSaleMapper}.
 *
 * <p>Covers:
 * <ul>
 *   <li>{@code toDTO()} - valid conversion with all fields correctly mapped</li>
 * </ul>
 */
class US025DirectSaleMapperTest {

    // =========================================================================
    // toDTO
    // =========================================================================

    /**
     * Ensures {@code toDTO()} returns a DTO with all fields correctly mapped
     * from the given listing and bibliographical data.
     */
    @Test
    void toDTOShouldReturnDTOWithCorrectFields() {

        // ARRANGE
        Listing listingDouble = mock(Listing.class);
        BibliographicalData bdDouble = mock(BibliographicalData.class);
        Price priceDouble = mock(Price.class);
        UserId sellerIdDouble = mock(UserId.class);

        when(bdDouble.getTitle()).thenReturn("Clean Code");
        when(priceDouble.getPrice()).thenReturn(new BigDecimal("29.99"));
        when(listingDouble.getPrice()).thenReturn(priceDouble);
        when(listingDouble.getListingTitle()).thenReturn("Clean Code - Good Condition");
        when(sellerIdDouble.toString()).thenReturn("seller123");
        when(listingDouble.getSellerId()).thenReturn(sellerIdDouble);

        List<String> authorNames = List.of("Robert Martin");

        US025DirectSaleMapper sut = new US025DirectSaleMapper();

        // ACT
        US025DirectSaleDTO result = sut.toDTO(listingDouble, bdDouble, authorNames);

        // ASSERT
        assertEquals("Clean Code", result.getPublicationTitle());
        assertEquals(authorNames, result.getAuthors());
        assertEquals(new BigDecimal("29.99"), result.getPrice());
        assertEquals("Clean Code - Good Condition", result.getListingTitle());
        assertEquals("seller123", result.getSeller());
    }

    /**
     * Ensures {@code toDTO()} returns a DTO with an empty authors list
     * when no author names are provided.
     */
    @Test
    void toDTOShouldReturnDTOWithEmptyAuthorsWhenNoAuthorNamesProvided() {

        // ARRANGE
        Listing listingDouble = mock(Listing.class);
        BibliographicalData bdDouble = mock(BibliographicalData.class);
        Price priceDouble = mock(Price.class);
        UserId sellerIdDouble = mock(UserId.class);

        when(bdDouble.getTitle()).thenReturn("Clean Code");
        when(priceDouble.getPrice()).thenReturn(new BigDecimal("29.99"));
        when(listingDouble.getPrice()).thenReturn(priceDouble);
        when(listingDouble.getListingTitle()).thenReturn("Clean Code - Good Condition");
        when(sellerIdDouble.toString()).thenReturn("seller123");
        when(listingDouble.getSellerId()).thenReturn(sellerIdDouble);

        US025DirectSaleMapper sut = new US025DirectSaleMapper();

        // ACT
        US025DirectSaleDTO result = sut.toDTO(listingDouble, bdDouble, List.of());

        // ASSERT
        assertTrue(result.getAuthors().isEmpty());
    }
}