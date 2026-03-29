package TOPSECRET.controller;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Country;
import TOPSECRET.domain.Author.IAuthorRepository;
import org.junit.jupiter.api.Test;
/**
 * Unit tests for the {@link US005ToRegisterAuthorController} application controller.
 *
 * <p>This test class verifies the behaviour of {@link US005ToRegisterAuthorController},
 * ensuring that it correctly validates its constructor dependency and delegates
 * author registration to the {@link IAuthorRepository}.
 *
 * <p><b>Test double strategy:</b> {@link IAuthorRepository} is replaced with a
 * manually implemented test double (fake). This isolates the controller from the
 * persistence layer and allows verification of the interaction between the controller
 * and the repository without relying on external mocking frameworks.
 *
 * <p><b>What is tested:</b>
 * <ul>
 *   <li>Constructor validation — an {@link IllegalArgumentException} is thrown
 *       when the {@link IAuthorRepository} dependency is {@code null}</li>
 *   <li>Delegation behaviour — {@link US005ToRegisterAuthorController#register(String, Country, java.time.LocalDate, String)}
 *       calls {@link IAuthorRepository#createAndAddAuthor(String, Country, java.time.LocalDate, String)}
 *       with the same arguments</li>
 *   <li>The controller returns the same {@link Author} instance produced by the repository</li>
 * </ul>
 *
 * <p><b>What is NOT tested here:</b> domain validation rules or persistence
 * behaviour implemented inside {@link IAuthorRepository} or {@link Author}.
 * Those responsibilities are covered by their respective unit tests.
 */
class US005ToRegisterAuthorControllerTest {

    @Test
    void constructorShouldThrowIllegalArgumentException_whenRepositoryIsNull() {

        // ACT
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new US005ToRegisterAuthorController(null)
        );

        // ASSERT
        assertEquals("AuthorRepository cannot be null", ex.getMessage());
    }

    @Test
    void shouldDelegateToRepository_andReturnRepositoryResult() {

        // ARRANGE
        IAuthorRepository IAuthorRepository = mock(IAuthorRepository.class);
        US005ToRegisterAuthorController controller = new US005ToRegisterAuthorController(IAuthorRepository);

        String name = "John";
        Country country = mock(Country.class);
        LocalDate dateOfBirth = LocalDate.of(1984, 12, 3);
        String biography = "bio";

        Author expectedAuthor = mock(Author.class);

        when(IAuthorRepository.createAndAddAuthor(name, country, dateOfBirth, biography))
                .thenReturn(expectedAuthor);

        // ACT
        Author result = controller.register(name, country, dateOfBirth, biography);

        // ASSERT
        assertSame(expectedAuthor, result);
    }
}
