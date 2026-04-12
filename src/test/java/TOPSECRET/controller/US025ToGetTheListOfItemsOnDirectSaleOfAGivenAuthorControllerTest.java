package TOPSECRET.controller;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.dto.AuthorDTO_US025;
import TOPSECRET.dto.DirectSaleDTO_US025;

import TOPSECRET.dto.DirectSaleMapper_US025;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController}.
 *
 * Verifies that the controller correctly orchestrates author retrieval
 * and delegates direct sale item lookup to the mapper.
 */
class US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorControllerTest {

    private IAuthorRepository authorRepository;
    private DirectSaleMapper_US025 directSaleMapper;
    private US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController sut;

    @BeforeEach
    void setUp() {
        authorRepository = mock(IAuthorRepository.class);
        directSaleMapper = mock(DirectSaleMapper_US025.class);

        sut = new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                authorRepository,
                directSaleMapper);
    }

    @Test
    void shouldReturnAuthorsDTOList() {

        // ARRANGE
        Author author1double = mock(Author.class);
        Author author2double = mock(Author.class);

        when(author1double.getName()).thenReturn("Author One");
        when(author2double.getName()).thenReturn("Author Two");
        when(authorRepository.getAllAuthors()).thenReturn(List.of(author1double, author2double));

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
        DirectSaleDTO_US025 dtodouble = mock(DirectSaleDTO_US025.class);
        when(directSaleMapper.toDTOListByAuthor(any())).thenReturn(List.of(dtodouble));

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
        when(directSaleMapper.toDTOListByAuthor(any())).thenReturn(List.of());

        // ACT
        List<DirectSaleDTO_US025> result = sut.getListOfItemsOnDirectSaleOfAGivenAuthor(authorId);

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
                        directSaleMapper)
        );
    }

    @Test
    void constructorShouldThrowExceptionWhenDirectSaleMapperIsNull() {

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> new US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        authorRepository,
                        null)
        );
    }
}