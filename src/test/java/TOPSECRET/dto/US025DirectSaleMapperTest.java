package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    private IPublicationSampleRepository publicationSampleRepositoryDouble;
    private IBibliographicalDataRepository bibliographicalDataRepositoryDouble;

    @BeforeEach
    void setUp() {
        publicationSampleRepositoryDouble = mock(IPublicationSampleRepository.class);
        bibliographicalDataRepositoryDouble = mock(IBibliographicalDataRepository.class);
    }

    @Test
    void toDTOs_whenValidInputs_shouldMapFieldsCorrectly() {

        // Arrange
        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn("Author One");
        when(authorDouble.id()).thenReturn(mock(TOPSECRET.domain.ValueObjects.AuthorId.class));

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

        when(publicationSampleRepositoryDouble.findPublicationSampleByBibliographicalDataIds(List.of(bdIdDouble)))
                .thenReturn(List.of(sampleDouble));
        when(bibliographicalDataRepositoryDouble.findBibliographicalDataByAuthorId(authorDouble.id()))
                .thenReturn(List.of(bdDouble));

        US025DirectSaleMapper sut = new US025DirectSaleMapper(
                publicationSampleRepositoryDouble, bibliographicalDataRepositoryDouble);

        // Act
        List<US025DirectSaleDTO> result = sut.toDTOs(authorDouble, List.of(listingDouble), List.of(bdIdDouble));

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
    void toDTOs_whenAuthorIsNull_shouldThrowException() {

        // Arrange
        US025DirectSaleMapper sut = new US025DirectSaleMapper(
                publicationSampleRepositoryDouble, bibliographicalDataRepositoryDouble);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.toDTOs(null, List.of(), List.of())
        );
    }

    @Test
    void toDTOs_whenListingsIsNull_shouldThrowException() {

        // Arrange
        Author authorDouble = mock(Author.class);
        US025DirectSaleMapper sut = new US025DirectSaleMapper(
                publicationSampleRepositoryDouble, bibliographicalDataRepositoryDouble);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.toDTOs(authorDouble, null, List.of())
        );
    }

    @Test
    void toDTOs_whenBibliographicalDataIdsIsNull_shouldThrowException() {

        // Arrange
        Author authorDouble = mock(Author.class);
        US025DirectSaleMapper sut = new US025DirectSaleMapper(
                publicationSampleRepositoryDouble, bibliographicalDataRepositoryDouble);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.toDTOs(authorDouble, List.of(), null)
        );
    }

    @Test
    void toDTOs_whenEmptyListings_shouldReturnEmptyList() {

        // Arrange
        Author authorDouble = mock(Author.class);
        when(authorDouble.id()).thenReturn(mock(TOPSECRET.domain.ValueObjects.AuthorId.class));

        when(publicationSampleRepositoryDouble.findPublicationSampleByBibliographicalDataIds(List.of()))
                .thenReturn(List.of());
        when(bibliographicalDataRepositoryDouble.findBibliographicalDataByAuthorId(authorDouble.id()))
                .thenReturn(List.of());

        US025DirectSaleMapper sut = new US025DirectSaleMapper(
                publicationSampleRepositoryDouble, bibliographicalDataRepositoryDouble);

        // Act
        List<US025DirectSaleDTO> result = sut.toDTOs(authorDouble, List.of(), List.of());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void constructorShouldThrowExceptionWhenPublicationSampleRepositoryIsNull() {

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new US025DirectSaleMapper(null, bibliographicalDataRepositoryDouble)
        );
    }

    @Test
    void constructorShouldThrowExceptionWhenBibliographicalDataRepositoryIsNull() {

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new US025DirectSaleMapper(publicationSampleRepositoryDouble, null)
        );
    }

    @Test
    void toDTOs_whenSampleNotFoundForListingItem_shouldSkipItem() {

        // Arrange
        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn("Author One");
        when(authorDouble.id()).thenReturn(mock(TOPSECRET.domain.ValueObjects.AuthorId.class));

        PublicationSampleId psIdDouble = mock(PublicationSampleId.class);
        PublicationSampleId differentPsIdDouble = mock(PublicationSampleId.class);
        BibliographicalDataID bdIdDouble = mock(BibliographicalDataID.class);

        Listing listingDouble = mock(Listing.class);
        when(listingDouble.getItemsId()).thenReturn(List.of(psIdDouble));

        PublicationSample sampleDouble = mock(PublicationSample.class);
        when(sampleDouble.id()).thenReturn(differentPsIdDouble);

        when(publicationSampleRepositoryDouble.findPublicationSampleByBibliographicalDataIds(List.of(bdIdDouble)))
                .thenReturn(List.of(sampleDouble));
        when(bibliographicalDataRepositoryDouble.findBibliographicalDataByAuthorId(authorDouble.id()))
                .thenReturn(List.of());

        US025DirectSaleMapper sut = new US025DirectSaleMapper(
                publicationSampleRepositoryDouble, bibliographicalDataRepositoryDouble);

        // Act
        List<US025DirectSaleDTO> result = sut.toDTOs(authorDouble, List.of(listingDouble), List.of(bdIdDouble));

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toDTOs_whenBdNotFoundForSample_shouldSkipItem() {

        // Arrange
        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn("Author One");
        when(authorDouble.id()).thenReturn(mock(TOPSECRET.domain.ValueObjects.AuthorId.class));

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

        when(publicationSampleRepositoryDouble.findPublicationSampleByBibliographicalDataIds(List.of(bdIdDouble)))
                .thenReturn(List.of(sampleDouble));
        when(bibliographicalDataRepositoryDouble.findBibliographicalDataByAuthorId(authorDouble.id()))
                .thenReturn(List.of(bdDouble));

        US025DirectSaleMapper sut = new US025DirectSaleMapper(
                publicationSampleRepositoryDouble, bibliographicalDataRepositoryDouble);

        // Act
        List<US025DirectSaleDTO> result = sut.toDTOs(authorDouble, List.of(listingDouble), List.of(bdIdDouble));

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}