package TOPSECRET.controller;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.US025AuthorDTO;
import TOPSECRET.dto.US025DirectSaleDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController}.
 *
 * Verifies that the controller correctly orchestrates repository calls
 * and delegates DTO conversion to the mapper classes.
 */
class US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorControllerTest {

    private IAuthorRepository authorRepositoryDouble;
    private IListingRepository listingRepositoryDouble;
    private IPublicationSampleRepository publicationSampleRepositoryDouble;
    private IBibliographicalDataRepository bibliographicalDataRepositoryDouble;
    private US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController sut;

    @BeforeEach
    void setUp() {
        authorRepositoryDouble = mock(IAuthorRepository.class);
        listingRepositoryDouble = mock(IListingRepository.class);
        publicationSampleRepositoryDouble = mock(IPublicationSampleRepository.class);
        bibliographicalDataRepositoryDouble = mock(IBibliographicalDataRepository.class);
    }

    @Test
    void shouldReturnAuthorsDTOList() {

        // Arrange
        sut = new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                authorRepositoryDouble,
                listingRepositoryDouble,
                publicationSampleRepositoryDouble,
                bibliographicalDataRepositoryDouble);

        Author author1Double = mock(Author.class);
        Author author2Double = mock(Author.class);

        when(author1Double.getName()).thenReturn("Author One");
        when(author2Double.getName()).thenReturn("Author Two");
        when(authorRepositoryDouble.getAllAuthors()).thenReturn(List.of(author1Double, author2Double));

        // Act
        List<US025AuthorDTO> result = sut.getAllAuthors();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnItemsOnDirectSaleForGivenAuthor() {

        // Arrange
        sut = new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                authorRepositoryDouble,
                listingRepositoryDouble,
                publicationSampleRepositoryDouble,
                bibliographicalDataRepositoryDouble);

        String authorIdStr = "author-123";
        AuthorId authorIdDouble = new AuthorId(authorIdStr);

        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn("Author One");

        PublicationSampleId psIdDouble = mock(PublicationSampleId.class);
        BibliographicalDataID bdIdDouble = mock(BibliographicalDataID.class);

        BibliographicalData bdDouble = mock(BibliographicalData.class);
        when(bdDouble.id()).thenReturn(bdIdDouble);
        when(bdDouble.getTitle()).thenReturn("Some Title");

        PublicationSample sampleDouble = mock(PublicationSample.class);
        when(sampleDouble.id()).thenReturn(psIdDouble);
        when(sampleDouble.getBibliographicalDataID()).thenReturn(bdIdDouble);

        Price priceDouble = mock(Price.class);
        when(priceDouble.getPrice()).thenReturn(new BigDecimal("10.00"));

        Listing listingDouble = mock(Listing.class);
        when(listingDouble.getItemsId()).thenReturn(List.of(psIdDouble));
        when(listingDouble.getPrice()).thenReturn(priceDouble);
        when(listingDouble.getListingTitle()).thenReturn("Listing One");
        when(listingDouble.getSellerId()).thenReturn(mock(UserId.class));

        when(authorRepositoryDouble.findAuthorById(any(AuthorId.class))).thenReturn(Optional.of(authorDouble));
        when(bibliographicalDataRepositoryDouble.findByAuthorId(any(AuthorId.class))).thenReturn(List.of(bdDouble));
        when(publicationSampleRepositoryDouble.findByBibliographicalData(List.of(bdDouble))).thenReturn(List.of(sampleDouble));
        when(listingRepositoryDouble.findDirectSaleListingsContaining(List.of(psIdDouble))).thenReturn(List.of(listingDouble));

        // Act
        List<US025DirectSaleDTO> result = sut.getListOfItemsOnDirectSaleOfAGivenAuthor(authorIdStr);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnEmptyListForAuthorWithoutDirectSales() {

        // Arrange
        sut = new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                authorRepositoryDouble,
                listingRepositoryDouble,
                publicationSampleRepositoryDouble,
                bibliographicalDataRepositoryDouble);

        String authorIdStr = "author-123";

        Author authorDouble = mock(Author.class);

        when(authorRepositoryDouble.findAuthorById(any(AuthorId.class))).thenReturn(Optional.of(authorDouble));
        when(bibliographicalDataRepositoryDouble.findByAuthorId(any(AuthorId.class))).thenReturn(List.of());
        when(publicationSampleRepositoryDouble.findByBibliographicalData(List.of())).thenReturn(List.of());
        when(listingRepositoryDouble.findDirectSaleListingsContaining(List.of())).thenReturn(List.of());

        // Act
        List<US025DirectSaleDTO> result = sut.getListOfItemsOnDirectSaleOfAGivenAuthor(authorIdStr);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenAuthorNotFound() {

        // Arrange
        sut = new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                authorRepositoryDouble,
                listingRepositoryDouble,
                publicationSampleRepositoryDouble,
                bibliographicalDataRepositoryDouble);

        when(authorRepositoryDouble.findAuthorById(any(AuthorId.class))).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.getListOfItemsOnDirectSaleOfAGivenAuthor("author-123")
        );
    }

    @Test
    void shouldThrowExceptionWhenAuthorIdIsNull() {

        // Arrange
        sut = new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                authorRepositoryDouble,
                listingRepositoryDouble,
                publicationSampleRepositoryDouble,
                bibliographicalDataRepositoryDouble);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.getListOfItemsOnDirectSaleOfAGivenAuthor(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenAuthorIdIsBlank() {

        // Arrange
        sut = new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                authorRepositoryDouble,
                listingRepositoryDouble,
                publicationSampleRepositoryDouble,
                bibliographicalDataRepositoryDouble);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.getListOfItemsOnDirectSaleOfAGivenAuthor("   ")
        );
    }

    @Test
    void constructorShouldThrowExceptionWhenAuthorRepositoryIsNull(){

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        null,
                        listingRepositoryDouble,
                        publicationSampleRepositoryDouble,
                        bibliographicalDataRepositoryDouble)
        );
    }

    @Test
    void constructorShouldThrowExceptionWhenListingRepositoryIsNull() {

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        authorRepositoryDouble,
                        null,
                        publicationSampleRepositoryDouble,
                        bibliographicalDataRepositoryDouble)
        );
    }

    @Test
    void constructorShouldThrowExceptionWhenPublicationSampleRepositoryIsNull() {

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        authorRepositoryDouble,
                        listingRepositoryDouble,
                        null,
                        bibliographicalDataRepositoryDouble)
        );
    }

    @Test
    void constructorShouldThrowExceptionWhenBibliographicalDataRepositoryIsNull() {

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        authorRepositoryDouble,
                        listingRepositoryDouble,
                        publicationSampleRepositoryDouble,
                        null)
        );
    }
}