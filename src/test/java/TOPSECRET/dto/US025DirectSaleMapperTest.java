package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
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

        PublicationSampleId psIdDouble = mock(PublicationSampleId.class);
        BibliographicalDataID bdIdDouble = mock(BibliographicalDataID.class);

        Listing listingDouble = mock(Listing.class);
        when(listingDouble.getItemsId()).thenReturn(List.of(psIdDouble));
        when(listingDouble.getPrice()).thenReturn(priceDouble);
        when(listingDouble.getListingTitle()).thenReturn("Listing One");
        when(listingDouble.getSellerId()).thenReturn(sellerIdDouble);

        PublicationSample sampleDouble = mock(PublicationSample.class);
        when(sampleDouble.id()).thenReturn(psIdDouble);
        when(sampleDouble.getBibliographicalDataID()).thenReturn(bdIdDouble);

        BibliographicalData bdDouble = mock(BibliographicalData.class);
        when(bdDouble.id()).thenReturn(bdIdDouble);
        when(bdDouble.getTitle()).thenReturn("Title One");

        // Act
        List<US025DirectSaleDTO> result = US025DirectSaleMapper.toDTO(
                authorDouble,
                List.of(listingDouble),
                List.of(sampleDouble),
                List.of(bdDouble)
        );

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Title One", result.get(0).publicationTitle());
        assertEquals(List.of("Author One"), result.get(0).authors());
        assertEquals(10.0, result.get(0).price());
        assertEquals("Listing One", result.get(0).listingTitle());
        assertEquals("seller-1", result.get(0).seller());
    }

    @Test
    void toDTO_whenAuthorIsNull_shouldThrowException() {

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> US025DirectSaleMapper.toDTO(null, List.of(), List.of(), List.of())
        );
    }

    @Test
    void toDTO_whenListingsIsNull_shouldThrowException() {

        // Arrange
        Author authorDouble = mock(Author.class);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> US025DirectSaleMapper.toDTO(authorDouble, null, List.of(), List.of())
        );
    }

    @Test
    void toDTO_whenBibliographicalDataIsNull_shouldThrowException() {

        // Arrange
        Author authorDouble = mock(Author.class);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> US025DirectSaleMapper.toDTO(authorDouble, List.of(), List.of(), null)
        );
    }

    @Test
    void toDTO_whenEmptyListings_shouldReturnEmptyList() {

        // Arrange
        Author authorDouble = mock(Author.class);

        // Act
        List<US025DirectSaleDTO> result = US025DirectSaleMapper.toDTO(
                authorDouble, List.of(), List.of(), List.of()
        );

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
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

    @Test
    void toDTO_whenSamplesIsNull_shouldThrowException() {

        // Arrange
        Author authorDouble = mock(Author.class);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> US025DirectSaleMapper.toDTO(authorDouble, List.of(), null, List.of())
        );
    }

    @Test
    void toDTO_whenSampleNotFoundForListingItem_shouldSkipItem() {

        // Arrange
        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn("Author One");

        PublicationSampleId psIdDouble = mock(PublicationSampleId.class);
        PublicationSampleId differentPsIdDouble = mock(PublicationSampleId.class);

        Listing listingDouble = mock(Listing.class);
        when(listingDouble.getItemsId()).thenReturn(List.of(psIdDouble));

        PublicationSample sampleDouble = mock(PublicationSample.class);
        when(sampleDouble.id()).thenReturn(differentPsIdDouble);

        // Act
        List<US025DirectSaleDTO> result = US025DirectSaleMapper.toDTO(
                authorDouble, List.of(listingDouble), List.of(sampleDouble), List.of()
        );

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toDTO_whenBdNotFoundForSample_shouldSkipItem() {

        // Arrange
        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn("Author One");

        PublicationSampleId psIdDouble = mock(PublicationSampleId.class);
        BibliographicalDataID bdIdDouble = mock(BibliographicalDataID.class);
        BibliographicalDataID differentBdIdDouble = mock(BibliographicalDataID.class);

        Listing listingDouble = mock(Listing.class);
        when(listingDouble.getItemsId()).thenReturn(List.of(psIdDouble));

        PublicationSample sampleDouble = mock(PublicationSample.class);
        when(sampleDouble.id()).thenReturn(psIdDouble);
        when(sampleDouble.getBibliographicalDataID()).thenReturn(bdIdDouble);

        BibliographicalData bdDouble = mock(BibliographicalData.class);
        when(bdDouble.id()).thenReturn(differentBdIdDouble);

        // Act
        List<US025DirectSaleDTO> result = US025DirectSaleMapper.toDTO(
                authorDouble, List.of(listingDouble), List.of(sampleDouble), List.of(bdDouble)
        );

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}