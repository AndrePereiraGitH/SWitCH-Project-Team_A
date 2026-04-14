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

        sut = new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                authorRepositoryDouble,
                listingRepositoryDouble,
                publicationSampleRepositoryDouble,
                bibliographicalDataRepositoryDouble);
    }

    @Test
    void shouldReturnAuthorsDTOList() {

        // Arrange
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
        String authorIdStr = "author-123";
        AuthorId authorIdDouble = new AuthorId(authorIdStr);

        Author authorDouble = mock(Author.class);
        when(authorDouble.id()).thenReturn(authorIdDouble);
        when(authorDouble.getName()).thenReturn("Author One");

        PublicationSampleId itemIdDouble = mock(PublicationSampleId.class);
        BibliographicalDataID bdIdDouble = mock(BibliographicalDataID.class);

        Listing listingDouble = mock(Listing.class);
        when(listingDouble.getItemsId()).thenReturn(List.of(itemIdDouble));
        Price priceDouble = mock(Price.class);
        when(priceDouble.getPrice()).thenReturn(new BigDecimal("10.00"));
        when(listingDouble.getPrice()).thenReturn(priceDouble);
        when(listingDouble.getListingTitle()).thenReturn("Listing One");
        when(listingDouble.getSellerId()).thenReturn(mock(UserId.class));

        PublicationSample itemDouble = mock(PublicationSample.class);
        when(itemDouble.getBibliographicalDataID()).thenReturn(bdIdDouble);

        BibliographicalData bdDouble = mock(BibliographicalData.class);
        when(bdDouble.getAuthors()).thenReturn(List.of(authorIdDouble));
        when(bdDouble.getTitle()).thenReturn("Some Title");

        when(authorRepositoryDouble.findAuthorById(any(AuthorId.class))).thenReturn(Optional.of(authorDouble));
        when(listingRepositoryDouble.findAllDirectSaleListings()).thenReturn(List.of(listingDouble));
        when(publicationSampleRepositoryDouble.findPublicationSampleById(itemIdDouble)).thenReturn(Optional.of(itemDouble));
        when(bibliographicalDataRepositoryDouble.findBibliographicalDataById(bdIdDouble)).thenReturn(Optional.of(bdDouble));

        // Act
        List<US025DirectSaleDTO> result = sut.getListOfItemsOnDirectSaleOfAGivenAuthor(authorIdStr);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnEmptyListForAuthorWithoutDirectSales() {

        // Arrange
        String authorIdStr = "author-123";
        Author authorDouble = mock(Author.class);
        AuthorId authorIdDouble = new AuthorId(authorIdStr);
        when(authorDouble.id()).thenReturn(authorIdDouble);

        when(authorRepositoryDouble.findAuthorById(any(AuthorId.class))).thenReturn(Optional.of(authorDouble));
        when(listingRepositoryDouble.findAllDirectSaleListings()).thenReturn(List.of());

        // Act
        List<US025DirectSaleDTO> result = sut.getListOfItemsOnDirectSaleOfAGivenAuthor(authorIdStr);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenAuthorNotFound() {

        // Arrange
        when(authorRepositoryDouble.findAuthorById(any(AuthorId.class))).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.getListOfItemsOnDirectSaleOfAGivenAuthor("author-123")
        );
    }

    @Test
    void shouldThrowExceptionWhenAuthorIdIsNull() {

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.getListOfItemsOnDirectSaleOfAGivenAuthor(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenAuthorIdIsBlank() {

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.getListOfItemsOnDirectSaleOfAGivenAuthor("   ")
        );
    }

    @Test
    void constructorShouldThrowExceptionWhenAuthorRepositoryIsNull() {

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

    @Test
    void shouldThrowExceptionWhenPublicationSampleNotFound() {

        String authorIdStr = "author-123";
        AuthorId authorIdDouble = new AuthorId(authorIdStr);

        Author authorDouble = mock(Author.class);
        when(authorDouble.id()).thenReturn(authorIdDouble);

        PublicationSampleId itemIdDouble = mock(PublicationSampleId.class);

        Listing listingDouble = mock(Listing.class);
        when(listingDouble.getItemsId()).thenReturn(List.of(itemIdDouble));

        when(authorRepositoryDouble.findAuthorById(any(AuthorId.class)))
                .thenReturn(Optional.of(authorDouble));
        when(listingRepositoryDouble.findAllDirectSaleListings())
                .thenReturn(List.of(listingDouble));
        when(publicationSampleRepositoryDouble.findPublicationSampleById(itemIdDouble))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> sut.getListOfItemsOnDirectSaleOfAGivenAuthor(authorIdStr)
        );
    }

    @Test
    void shouldThrowExceptionWhenBibliographicalDataNotFound() {

        String authorIdStr = "author-123";
        AuthorId authorIdDouble = new AuthorId(authorIdStr);

        Author authorDouble = mock(Author.class);
        when(authorDouble.id()).thenReturn(authorIdDouble);

        PublicationSampleId itemIdDouble = mock(PublicationSampleId.class);
        BibliographicalDataID bdIdDouble = mock(BibliographicalDataID.class);

        Listing listingDouble = mock(Listing.class);
        when(listingDouble.getItemsId()).thenReturn(List.of(itemIdDouble));

        PublicationSample itemDouble = mock(PublicationSample.class);
        when(itemDouble.getBibliographicalDataID()).thenReturn(bdIdDouble);

        when(authorRepositoryDouble.findAuthorById(any(AuthorId.class)))
                .thenReturn(Optional.of(authorDouble));
        when(listingRepositoryDouble.findAllDirectSaleListings())
                .thenReturn(List.of(listingDouble));
        when(publicationSampleRepositoryDouble.findPublicationSampleById(itemIdDouble))
                .thenReturn(Optional.of(itemDouble));
        when(bibliographicalDataRepositoryDouble.findBibliographicalDataById(bdIdDouble))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> sut.getListOfItemsOnDirectSaleOfAGivenAuthor(authorIdStr)
        );
    }
}