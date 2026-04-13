package TOPSECRET.controller;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.dto.AuthorDTO_US025;
import TOPSECRET.dto.DirectSaleDTO_US025;
import TOPSECRET.dto.DirectSaleMapper_US025;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController}.
 *
 * Verifies that the controller correctly orchestrates author retrieval
 * and delegates direct sale item lookup to the mapper.
 */
class US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorControllerTest {

    private IAuthorRepository authorRepositoryDouble;
    private DirectSaleMapper_US025 directSaleMapperDouble;
    private US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController sut;

    @BeforeEach
    void setUp() {
        authorRepositoryDouble = mock(IAuthorRepository.class);
        directSaleMapperDouble = mock(DirectSaleMapper_US025.class);

        sut = new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                authorRepositoryDouble,
                directSaleMapperDouble);
    }

    @Test
    void shouldReturnAuthorsDTOList() {

        // ARRANGE
        Author author1Double = mock(Author.class);
        Author author2Double = mock(Author.class);

        when(author1Double.getName()).thenReturn("Author One");
        when(author2Double.getName()).thenReturn("Author Two");
        when(authorRepositoryDouble.getAllAuthors()).thenReturn(List.of(author1Double, author2Double));

        // ACT
        List<AuthorDTO_US025> result = sut.getAllAuthors();

        // ASSERT
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnItemsOnDirectSaleForGivenAuthor() {

        // ARRANGE
        String authorId = "author-123";
        Author authorDouble = mock(Author.class);
        DirectSaleDTO_US025 dtoDouble = mock(DirectSaleDTO_US025.class);

        when(authorRepositoryDouble.findAuthorById(any(AuthorId.class))).thenReturn(Optional.of(authorDouble));
        when(directSaleMapperDouble.toDTOListByAuthor(authorDouble)).thenReturn(List.of(dtoDouble));

        // ACT
        List<DirectSaleDTO_US025> result = sut.getListOfItemsOnDirectSaleOfAGivenAuthor(authorId);

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnEmptyListForAuthorWithoutDirectSales() {

        // ARRANGE
        String authorId = "author-123";
        Author authorDouble = mock(Author.class);

        when(authorRepositoryDouble.findAuthorById(any(AuthorId.class))).thenReturn(Optional.of(authorDouble));
        when(directSaleMapperDouble.toDTOListByAuthor(authorDouble)).thenReturn(List.of());

        // ACT
        List<DirectSaleDTO_US025> result = sut.getListOfItemsOnDirectSaleOfAGivenAuthor(authorId);

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenAuthorNotFound() {

        // ARRANGE
        String authorId = "author-123";
        when(authorRepositoryDouble.findAuthorById(any(AuthorId.class))).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.getListOfItemsOnDirectSaleOfAGivenAuthor(authorId)
        );
    }

    @Test
    void shouldThrowExceptionWhenAuthorIdIsNull() {

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.getListOfItemsOnDirectSaleOfAGivenAuthor(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenAuthorIdIsBlank() {

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.getListOfItemsOnDirectSaleOfAGivenAuthor("   ")
        );
    }

    @Test
    void constructorShouldThrowExceptionWhenAuthorRepositoryIsNull() {

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        null,
                        directSaleMapperDouble)
        );
    }

    @Test
    void constructorShouldThrowExceptionWhenDirectSaleMapperIsNull() {

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        authorRepositoryDouble,
                        null)
        );
    }
}