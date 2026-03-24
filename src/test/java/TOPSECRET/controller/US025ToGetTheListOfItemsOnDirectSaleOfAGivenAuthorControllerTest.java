package TOPSECRET.controller;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.PublicationSample;
import TOPSECRET.dto.AuthorDTO_US025;
import TOPSECRET.repository.IAuthorRepository;
import TOPSECRET.repository.IListingRepository;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for US025 - Get list of items on direct sale of a given author
 * SUT: US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController
 *
 * Repository dependencies are mocked to isolate controller logic.
 */
class US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorControllerTest {

    /**
     * Ensures all authors are retrieved and converted to DTOs.
     */
    @Test
    void shouldReturnAuthorsDTOList() {

        // ARRANGE
        IAuthorRepository IAuthorRepository = mock(IAuthorRepository.class);
        IListingRepository listingRepository = mock(IListingRepository.class);

        US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController sut =
                new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        IAuthorRepository,
                        listingRepository
                );

        Author author1 = mock(Author.class);
        Author author2 = mock(Author.class);
        
        when(IAuthorRepository.getAllAuthors())
                .thenReturn(List.of(author1, author2));

        // ACT
        List<AuthorDTO_US025> result = sut.getAllAuthors();

        // ASSERT
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    /**
     * Ensures publication samples are returned for an author with direct sales.
     */
    @Test
    void shouldReturnPublicationSamplesForAuthorWithDirectSales() {

        // ARRANGE
        IAuthorRepository IAuthorRepository = mock(IAuthorRepository.class);
        IListingRepository listingRepository = mock(IListingRepository.class);

        US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController sut =
                new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        IAuthorRepository,
                        listingRepository
                );

        Author author = mock(Author.class);

        PublicationSample sample1 = mock(PublicationSample.class);
        PublicationSample sample2 = mock(PublicationSample.class);

        when(listingRepository.getListOfItemsOnDirectSaleOfAGivenAuthor(author))
                .thenReturn(List.of(sample1, sample2));

        // ACT
        List<PublicationSample> result =
                sut.getListOfItemsOnDirectSaleOfAGivenAuthor(author);

        // ASSERT
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    /**
     * Ensures empty list is returned when author has no direct sales.
     */
    @Test
    void shouldReturnEmptyListForAuthorWithoutDirectSales() {

        // ARRANGE
        IAuthorRepository IAuthorRepository = mock(IAuthorRepository.class);
        IListingRepository listingRepository = mock(IListingRepository.class);

        US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController sut =
                new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        IAuthorRepository,
                        listingRepository
                );

        Author author = mock(Author.class);

        when(listingRepository.getListOfItemsOnDirectSaleOfAGivenAuthor(author))
                .thenReturn(List.of());

        // ACT
        List<PublicationSample> result =
                sut.getListOfItemsOnDirectSaleOfAGivenAuthor(author);

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Ensures exception is thrown when author is null.
     */
    @Test
    void shouldThrowExceptionWhenAuthorIsNull() {

        // ARRANGE
        US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController sut =
                new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        mock(IAuthorRepository.class),
                        mock(IListingRepository.class)
                );

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.getListOfItemsOnDirectSaleOfAGivenAuthor(null)
        );
    }

    /**
     * Ensures constructor throws exception when AuthorRepository is null.
     */
    @Test
    void constructorShouldThrowExceptionWhenAuthorRepositoryIsNull() {

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        null,
                        mock(IListingRepository.class)
                )
        );
    }

    /**
     * Ensures constructor throws exception when ListingRepository is null.
     */
    @Test
    void constructorShouldThrowExceptionWhenListingRepositoryIsNull() {

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        mock(IAuthorRepository.class),
                        null
                )
        );
    }
}