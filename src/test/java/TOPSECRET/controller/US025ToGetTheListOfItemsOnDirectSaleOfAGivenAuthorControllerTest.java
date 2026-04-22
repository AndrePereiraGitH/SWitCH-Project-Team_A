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
import TOPSECRET.dto.US025DirectSaleMapper;
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
    private US025DirectSaleMapper directSaleMapperDouble;

    @BeforeEach
    void setUp() {
        authorRepositoryDouble = mock(IAuthorRepository.class);
        listingRepositoryDouble = mock(IListingRepository.class);
        publicationSampleRepositoryDouble = mock(IPublicationSampleRepository.class);
        bibliographicalDataRepositoryDouble = mock(IBibliographicalDataRepository.class);
        directSaleMapperDouble = mock(US025DirectSaleMapper.class);
    }

    @Test
    void shouldReturnAuthorsDTOList() {

        // Arrange
        US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController sut =
                new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        authorRepositoryDouble,
                        listingRepositoryDouble,
                        publicationSampleRepositoryDouble,
                        bibliographicalDataRepositoryDouble,
                        directSaleMapperDouble);

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
        AuthorId authorId = mock(AuthorId.class);
        BibliographicalDataID bdId = mock(BibliographicalDataID.class);
        PublicationSampleId psId = mock(PublicationSampleId.class);

        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn("Author One");
        when(authorDouble.id()).thenReturn(authorId);

        Price priceDouble = mock(Price.class);
        when(priceDouble.getPrice()).thenReturn(new BigDecimal("10.00"));

        Listing listingDouble = mock(Listing.class);
        when(listingDouble.getItemsId()).thenReturn(List.of(psId));
        when(listingDouble.getPrice()).thenReturn(priceDouble);
        when(listingDouble.getListingTitle()).thenReturn("Listing One");
        when(listingDouble.getSellerId()).thenReturn(new UserId("seller-1"));

        PublicationSample sampleDouble = mock(PublicationSample.class);
        when(sampleDouble.id()).thenReturn(psId);
        when(sampleDouble.getBibliographicalDataID()).thenReturn(bdId);

        BibliographicalData bdDouble = mock(BibliographicalData.class);
        when(bdDouble.id()).thenReturn(bdId);
        when(bdDouble.getTitle()).thenReturn("Some Title");

        when(authorRepositoryDouble.findAuthorById(any(AuthorId.class)))
                .thenReturn(Optional.of(authorDouble));

        when(bibliographicalDataRepositoryDouble.findBibliographicalDataIdByAuthorId(any(AuthorId.class)))
                .thenReturn(List.of(bdId));

        when(publicationSampleRepositoryDouble.findPublicationSamplesIdsByBiblioDataIds(anyList()))
                .thenReturn(List.of(psId));

        when(listingRepositoryDouble.findDirectSaleListingsContaining(anyList()))
                .thenReturn(List.of(listingDouble));

        when(publicationSampleRepositoryDouble.findPublicationSampleByBibliographicalDataIds(anyList()))
                .thenReturn(List.of(sampleDouble));

        when(bibliographicalDataRepositoryDouble.findBibliographicalDataByAuthorId(any(AuthorId.class)))
                .thenReturn(List.of(bdDouble));

        US025DirectSaleDTO dtoDouble = mock(US025DirectSaleDTO.class);
        when(directSaleMapperDouble.toDTOs(any(Author.class), anyList(), anyList()))
                .thenReturn(List.of(dtoDouble));

        US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController sut =
                new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        authorRepositoryDouble,
                        listingRepositoryDouble,
                        publicationSampleRepositoryDouble,
                        bibliographicalDataRepositoryDouble,
                        directSaleMapperDouble);

        // Act
        List<US025DirectSaleDTO> result = sut.getListOfItemsOnDirectSaleOfAGivenAuthor(authorId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnEmptyListForAuthorWithoutDirectSales() {

        // Arrange
        US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController sut =
                new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        authorRepositoryDouble,
                        listingRepositoryDouble,
                        publicationSampleRepositoryDouble,
                        bibliographicalDataRepositoryDouble,
                        directSaleMapperDouble);

        Author authorDouble = mock(Author.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        when(authorDouble.id()).thenReturn(authorIdDouble);

        when(authorRepositoryDouble.findAuthorById(authorIdDouble)).thenReturn(Optional.of(authorDouble));
        when(bibliographicalDataRepositoryDouble.findBibliographicalDataByAuthorId(authorIdDouble)).thenReturn(List.of());
        when(publicationSampleRepositoryDouble.findPublicationSampleByBibliographicalDataIds(List.of())).thenReturn(List.of());
        when(listingRepositoryDouble.findDirectSaleListingsContaining(List.of())).thenReturn(List.of());

        // Act
        List<US025DirectSaleDTO> result = sut.getListOfItemsOnDirectSaleOfAGivenAuthor(authorIdDouble);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenAuthorNotFound() {

        // Arrange
        Author authorDouble = mock(Author.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        when(authorDouble.id()).thenReturn(authorIdDouble);

        US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController sut =
                new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        authorRepositoryDouble,
                        listingRepositoryDouble,
                        publicationSampleRepositoryDouble,
                        bibliographicalDataRepositoryDouble,
                        directSaleMapperDouble);

        when(authorRepositoryDouble.findAuthorById(authorIdDouble))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.getListOfItemsOnDirectSaleOfAGivenAuthor(authorIdDouble)
        );
    }

    @Test
    void shouldThrowExceptionWhenAuthorIdIsNull() {

        // Arrange
        US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController sut =
                new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        authorRepositoryDouble,
                        listingRepositoryDouble,
                        publicationSampleRepositoryDouble,
                        bibliographicalDataRepositoryDouble,
                        directSaleMapperDouble);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.getListOfItemsOnDirectSaleOfAGivenAuthor(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenAuthorIdIsBlank() {

        // Arrange
        Author authorDouble = mock(Author.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        when(authorDouble.id()).thenReturn(authorIdDouble);

        US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController sut =
                new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        authorRepositoryDouble,
                        listingRepositoryDouble,
                        publicationSampleRepositoryDouble,
                        bibliographicalDataRepositoryDouble,
                        directSaleMapperDouble);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.getListOfItemsOnDirectSaleOfAGivenAuthor(authorIdDouble)
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
                        bibliographicalDataRepositoryDouble,
                        directSaleMapperDouble)
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
                        bibliographicalDataRepositoryDouble,
                        directSaleMapperDouble)
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
                        bibliographicalDataRepositoryDouble,
                        directSaleMapperDouble)
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
                        null,
                        directSaleMapperDouble)
        );
    }
}