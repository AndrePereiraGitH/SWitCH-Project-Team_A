package TOPSECRET.controller;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.dto.AuthorDTO_US025;
import TOPSECRET.dto.DirectSaleDTO_US025;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for US025 - Get list of items on direct sale of a given author
 * SUT: US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController
 *
 */
class US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorControllerTest {

    private IAuthorRepository authorRepository;
    private IListingRepository listingRepository;
    private IBibliographicalDataRepository bibliographicalDataRepository;
    private IPublicationSampleRepository publicationSampleRepository;
    private US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController sut;

    @BeforeEach
    void setUp() {
        authorRepository = mock(IAuthorRepository.class);
        listingRepository = mock(IListingRepository.class);
        bibliographicalDataRepository = mock(IBibliographicalDataRepository.class);
        publicationSampleRepository = mock(IPublicationSampleRepository.class);

        sut = new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                authorRepository,
                listingRepository,
                bibliographicalDataRepository,
                publicationSampleRepository);
    }

    @Test
    void shouldReturnAuthorsDTOList() {

        // ARRANGE
        Author author1 = mock(Author.class);
        Author author2 = mock(Author.class);

        when(author1.getName()).thenReturn("Author One");
        when(author2.getName()).thenReturn("Author Two");
        when(authorRepository.getAllAuthors()).thenReturn(List.of(author1, author2));

        // ACT
        List<AuthorDTO_US025> result = sut.getAllAuthors();

        // ASSERT
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnItemsOnDirectSaleForGivenAuthor() {

        // ARRANGE
        Author author = mock(Author.class);
        AuthorId authorId = mock(AuthorId.class);
        when(author.id()).thenReturn(authorId);

        PublicationSampleId itemId = mock(PublicationSampleId.class);
        PublicationSample item = mock(PublicationSample.class);
        BibliographicalDataID bdId = mock(BibliographicalDataID.class);
        BibliographicalData bd = mock(BibliographicalData.class);
        Listing listing = mock(Listing.class);

        when(listing.getItemsId()).thenReturn(List.of(itemId));
        when(listingRepository.findAllDirectSaleListings()).thenReturn(List.of(listing));
        when(publicationSampleRepository.findPublicationSampleById(itemId)).thenReturn(Optional.of(item));
        when(item.getBibliographicalDataID()).thenReturn(bdId);
        when(bibliographicalDataRepository.findBibliographicalDataById(bdId)).thenReturn(Optional.of(bd));
        when(bd.getAuthors()).thenReturn(List.of(authorId));
        when(bd.getTitle()).thenReturn("Some Title");
        when(listing.getPrice()).thenReturn(mock(TOPSECRET.domain.ValueObjects.Price.class));
        when(listing.getListingTitle()).thenReturn("Some Listing");
        when(listing.getSellerId()).thenReturn(mock(TOPSECRET.domain.ValueObjects.UserId.class));

        // ACT
        List<DirectSaleDTO_US025> result = sut.getListOfItemsOnDirectSaleOfAGivenAuthor(author);

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnEmptyListForAuthorWithoutDirectSales() {

        // ARRANGE
        Author author = mock(Author.class);
        AuthorId authorId = mock(AuthorId.class);
        when(author.id()).thenReturn(authorId);

        PublicationSampleId itemId = mock(PublicationSampleId.class);
        PublicationSample item = mock(PublicationSample.class);
        BibliographicalDataID bdId = mock(BibliographicalDataID.class);
        BibliographicalData bd = mock(BibliographicalData.class);
        Listing listing = mock(Listing.class);

        when(listing.getItemsId()).thenReturn(List.of(itemId));
        when(listingRepository.findAllDirectSaleListings()).thenReturn(List.of(listing));
        when(publicationSampleRepository.findPublicationSampleById(itemId)).thenReturn(Optional.of(item));
        when(item.getBibliographicalDataID()).thenReturn(bdId);
        when(bibliographicalDataRepository.findBibliographicalDataById(bdId)).thenReturn(Optional.of(bd));
        when(bd.getAuthors()).thenReturn(List.of());

        // ACT
        List<DirectSaleDTO_US025> result = sut.getListOfItemsOnDirectSaleOfAGivenAuthor(author);

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenAuthorIsNull() {

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.getListOfItemsOnDirectSaleOfAGivenAuthor(null)
        );
    }

    @Test
    void constructorShouldThrowExceptionWhenAuthorRepositoryIsNull() {

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        null,
                        listingRepository,
                        bibliographicalDataRepository,
                        publicationSampleRepository)
        );
    }

    @Test
    void constructorShouldThrowExceptionWhenListingRepositoryIsNull() {

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        authorRepository,
                        null,
                        bibliographicalDataRepository,
                        publicationSampleRepository)
        );
    }

    @Test
    void constructorShouldThrowExceptionWhenBibliographicalDataRepositoryIsNull() {

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        authorRepository,
                        listingRepository,
                        null,
                        publicationSampleRepository)
        );
    }

    @Test
    void constructorShouldThrowExceptionWhenPublicationSampleRepositoryIsNull() {

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        authorRepository,
                        listingRepository,
                        bibliographicalDataRepository,
                        null)
        );
    }
}