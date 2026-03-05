package TOPSECRET.repository;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.AuthorFactory;
import TOPSECRET.domain.Country;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuthorRepository}.
 *
 * <p>Both collaborators — {@link AuthorFactory} and the backing {@link List} —
 * are mocked, isolating the repository's own logic completely.
 *
 * <p><b>What is tested here (repository logic only):</b>
 * <ul>
 *   <li>Constructor — null list, null factory</li>
 *   <li>{@link AuthorRepository#createAndAddAuthor} — delegates to factory,
 *       adds result to list, rejects duplicates</li>
 *   <li>{@link AuthorRepository#getAllAuthors} — returns unmodifiable copy</li>
 * </ul>
 *
 */
class AuthorRepositoryTest {

        List<Author> authors = mock(ArrayList.class);       //TODO: Double check this
        AuthorFactory authorFactory = mock(AuthorFactory.class);

        AuthorRepository authorRepository = new AuthorRepository(authors, authorFactory);

        // ========================================
        // CONSTRUCTOR TESTS
        // ========================================
        @Test
        void shouldThrowExceptionWhenListIsNull() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new AuthorRepository(null, authorFactory)
            );
            assertEquals("Author cannot be null", exception.getMessage());
        }

        @Test
        void shouldThrowExceptionWhenFactoryIsNull() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new AuthorRepository(authors, null)
            );
            assertEquals("AuthorFactory cannot be null", exception.getMessage());
        }

        // ========================================
        // CREATE AND ADD TESTS
        // ========================================
    /**
     * I stub {@code iterator()} when mocking a {@link List}.
     *
     * <p>A Java for-each loop internally calls {@code iterator()} on the collection.
     * When the list is a Mockito mock, {@code iterator()} returns {@code null} by default,
     * causing a {@link NullPointerException} the moment the loop starts.
     *
     * <p>We fix this by stubbing it with a real iterator: Colletions.
     * @see AuthorRepository
     * @see java.util.Collections#emptyIterator()
     */
        @Test
        void shouldDelegateCreationToFactoryAndAddToList() {
            // Arrange
            Author mockAuthor = mock(Author.class);
            when(authorFactory.create("João", new Country("Portugal"), LocalDate.of(1978, 5, 12), "valid bio"))
                    .thenReturn(mockAuthor);
            when(authors.iterator()).thenReturn(Collections.emptyIterator());

            // Act
            Author result = authorRepository.createAndAddAuthor("João", new Country("Portugal"), LocalDate.of(1978, 5, 12), "valid bio");

            // Assert
            assertEquals(mockAuthor, result);
            verify(authorFactory).create("João", new Country("Portugal"), LocalDate.of(1978, 5, 12), "valid bio");
            verify(authors).add(mockAuthor);
        }

    @Test
    void shouldThrowExceptionWhenAuthorAlreadyExists() {
        // Arrange
        Author mockAuthor = mock(Author.class);

        when(authorFactory.create("João", new Country("Portugal"), LocalDate.of(1978, 5, 12), "valid bio"))
                .thenReturn(mockAuthor);

        // Use thenAnswer so a fresh iterator is returned every time iterator() is called
        when(authors.iterator()).thenAnswer(invocation -> List.of(mockAuthor).iterator());

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authorRepository.createAndAddAuthor("João", new Country("Portugal"), LocalDate.of(1978, 5, 12), "valid bio")
        );
        assertEquals("Author already exists", exception.getMessage());
        verify(authors, never()).add(any());
    }

        @Test
        void shouldPropagateExceptionFromFactory() {
            // Arrange
            when(authorFactory.create(null, new Country("Portugal"), LocalDate.of(1978, 5, 12), "bio"))
                    .thenThrow(new IllegalArgumentException("Invalid data"));

            // Act + Assert
            assertThrows(IllegalArgumentException.class,
                    () -> authorRepository.createAndAddAuthor(null, new Country("Portugal"), LocalDate.of(1978, 5, 12), "bio"));
            verify(authors, never()).add(any());
        }

        // ========================================
        // GET ALL AUTHORS TESTS
        // ========================================


        @Test
        void shouldReturnUnmodifiableList() {
            // Arrange
            Author mockAuthor = mock(Author.class);
            when(authors.toArray()).thenReturn(new Object[]{mockAuthor});

            // Act
            List<Author> result = authorRepository.getAllAuthors();

            // Assert
            assertThrows(UnsupportedOperationException.class, () -> result.add(mockAuthor));
        }

    @Test
    void shouldReturnDefensiveCopy() {
        // Arrange — real list so getAllAuthors() actually has something to copy
        AuthorRepository realRepository = new AuthorRepository(new ArrayList<>(), authorFactory);
        when(authorFactory.create("João", new Country("Portugal"), LocalDate.of(1978, 5, 12), "bio"))
                .thenReturn(mock(Author.class));
        realRepository.createAndAddAuthor("João", new Country("Portugal"), LocalDate.of(1978, 5, 12), "bio");

        // Act
        List<Author> result1 = realRepository.getAllAuthors();
        List<Author> result2 = realRepository.getAllAuthors();

        // Assert — different instances, same content
        assertNotSame(result1, result2);
        assertEquals(result1, result2);
    }
}