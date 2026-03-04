package TOPSECRET.controller;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.Country;
import TOPSECRET.repository.AuthorRepository;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link ToRegisterAuthorController} application controller.
 *
 * <p>This test class verifies the behaviour of {@link ToRegisterAuthorController},
 * ensuring that it correctly validates its constructor dependency and delegates
 * author registration to the {@link AuthorRepository}.
 *
 * <p><b>Test double strategy:</b> {@link AuthorRepository} is replaced with a
 * Mockito mock created via {@code mock(AuthorRepository.class)}. This isolates
 * the controller from the persistence layer and allows verification of the
 * interaction between the controller and the repository.
 *
 * <p><b>What is tested:</b>
 * <ul>
 *   <li>Constructor validation — an {@link IllegalArgumentException} is thrown
 *       when the {@link AuthorRepository} dependency is {@code null}</li>
 *   <li>Delegation behaviour — {@link ToRegisterAuthorController#registerAuthor(String, Country, java.time.LocalDate, String)}
 *       calls {@link AuthorRepository#createAndAddAuthor(String, Country, java.time.LocalDate, String)}
 *       with the same arguments</li>
 *   <li>The controller returns the same {@link Author} instance produced by the repository</li>
 * </ul>
 *
 * <p><b>What is NOT tested here:</b> domain validation rules or persistence
 * behaviour implemented inside {@link AuthorRepository} or {@link Author}.
 * Those responsibilities are covered by their respective unit tests.
 */
class ToRegisterAuthorControllerTest {

    @Test
    void constructorShouldThrowIllegalArgumentException_whenRepositoryIsNull() {

        // ACT
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new ToRegisterAuthorController(null)
        );

        // ASSERT
        assertEquals("AuthorRepository cannot be null", ex.getMessage());
    }

    @Test
    void registerAuthor_shouldDelegateToRepository_andReturnRepositoryResult_AAA() {

        // ARRANGE
        AuthorRepository authorRepository = mock(AuthorRepository.class);
        ToRegisterAuthorController controller = new ToRegisterAuthorController(authorRepository);

        String name = "John";
        Country country = mock(Country.class);
        LocalDate dateOfBirth = LocalDate.of(1984, 12, 3);
        String biography = "bio";

        Author expectedAuthor = mock(Author.class);

        when(authorRepository.createAndAddAuthor(name, country, dateOfBirth, biography))
                .thenReturn(expectedAuthor);

        // ACT
        Author result = controller.registerAuthor(name, country, dateOfBirth, biography);

        // ASSERT
        assertSame(expectedAuthor, result);

        verify(authorRepository, times(1))
                .createAndAddAuthor(name, country, dateOfBirth, biography);
    }
}