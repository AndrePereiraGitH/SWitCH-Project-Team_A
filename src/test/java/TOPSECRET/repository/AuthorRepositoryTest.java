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

        private final List<Author> authors = mock(ArrayList.class);       //TODO: Double check this
        private final AuthorFactory authorFactory = mock(AuthorFactory.class);
        private final AuthorRepository authorRepository = new AuthorRepository(authors, authorFactory);
        private final Country country = mock(Country.class);
        private final String name = "João";
        String validBio = "bio";

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
     */
        @Test
        void shouldDelegateCreationToFactoryAndAddToList() {
            // Arrange
            Author mockAuthor = mock(Author.class);
            when(authorFactory.create(name, country, LocalDate.of(1978, 5, 12), validBio))
                    .thenReturn(mockAuthor);
            when(authors.iterator()).thenReturn(Collections.emptyIterator());

            // Act
            Author result = authorRepository.createAndAddAuthor(name, country, LocalDate.of(1978, 5, 12), validBio);

            // Assert
            assertEquals(mockAuthor, result);
            verify(authorFactory).create(name, country, LocalDate.of(1978, 5, 12), validBio);
            verify(authors).add(mockAuthor);
        }

    @Test
    void shouldThrowExceptionWhenAuthorAlreadyExists() {
        // Arrange
        Author mockAuthor = mock(Author.class);

        when(authorFactory.create(name, country, LocalDate.of(1978, 5, 12), validBio))
                .thenReturn(mockAuthor);

        // Use thenAnswer so a fresh iterator is returned every time iterator() is called
        when(authors.iterator()).thenAnswer(invocation -> List.of(mockAuthor).iterator());

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authorRepository.createAndAddAuthor(name, country, LocalDate.of(1978, 5, 12), validBio)
        );
        assertEquals("Author already exists", exception.getMessage());
        verify(authors, never()).add(any());
    }

        @Test
        void shouldPropagateExceptionFromFactory() {
            // Arrange
            when(authorFactory.create(null, country, LocalDate.of(1978, 5, 12), validBio))
                    .thenThrow(new IllegalArgumentException("Invalid data"));

            // Act + Assert
            assertThrows(IllegalArgumentException.class,
                    () -> authorRepository.createAndAddAuthor(null, country, LocalDate.of(1978, 5, 12), validBio));
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
        when(authorFactory.create(name, country, LocalDate.of(1978, 5, 12), "bio"))
                .thenReturn(mock(Author.class));
        realRepository.createAndAddAuthor(name, country, LocalDate.of(1978, 5, 12), "bio");

        // Act
        List<Author> result1 = realRepository.getAllAuthors();
        List<Author> result2 = realRepository.getAllAuthors();

        // Assert — different instances, same content
        assertNotSame(result1, result2);
        assertEquals(result1, result2);
    }
}