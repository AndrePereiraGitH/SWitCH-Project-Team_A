package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link US025DirectSaleMapper}.
 *
 * Verifies that domain entities are correctly converted to
 * {@link US025DirectSaleDTO} representations.
 */
class US025DirectSaleMapperTest {

    @Test
    void toDTO_whenValidInputs_shouldMapFieldsCorrectly() {

        // Arrange
        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn("Author One");

        Price priceDouble = mock(Price.class);
        when(priceDouble.getPrice()).thenReturn(new BigDecimal("10.00"));

        UserId sellerIdDouble = mock(UserId.class);
        when(sellerIdDouble.toString()).thenReturn("seller-1");

        Listing listingDouble = mock(Listing.class);
        when(listingDouble.getPrice()).thenReturn(priceDouble);
        when(listingDouble.getListingTitle()).thenReturn("Listing One");
        when(listingDouble.getSellerId()).thenReturn(sellerIdDouble);

        BibliographicalData bdDouble = mock(BibliographicalData.class);
        when(bdDouble.getTitle()).thenReturn("Title One");

        // Act
        US025DirectSaleDTO dto = US025DirectSaleMapper.toDTO(authorDouble, listingDouble, bdDouble);

        // Assert
        assertNotNull(dto);
        assertEquals("Title One", dto.publicationTitle());
        assertEquals(List.of("Author One"), dto.authors());
        assertEquals(10.0, dto.price());
        assertEquals("Listing One", dto.listingTitle());
        assertEquals("seller-1", dto.seller());
    }

    @Test
    void constructor_shouldThrowUnsupportedOperationException() {
        // Act + Assert
        try {
            Constructor<US025DirectSaleMapper> constructor = US025DirectSaleMapper.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
            fail("Expected InvocationTargetException");
        } catch (InvocationTargetException e) {
            assertInstanceOf(UnsupportedOperationException.class, e.getCause());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}