package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.AuthorAuthorizationPolicy;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.AuthorFactory;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuthorService}.
 */
@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private IAuthorRepository authorRepository;

    @Mock
    private AuthorFactory authorFactory;

    @Mock
    private AuthorAuthorizationPolicy policy;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private ICountryRepository countryRepository;

    @InjectMocks
    private AuthorService authorService;

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private UserId userId() { return new UserId("user-1"); }
    private User user() { return mock(User.class); }
    private Author author() { return mock(Author.class); }
    private CountryId countryId() { return new CountryId("PORTUGAL"); }
    private LocalDate dateOfBirth() { return LocalDate.of(1922, 11, 16); }

    // =========================================================================
    // Constructor
    // =========================================================================

    @Test
    void constructor_shouldThrowNullPointerException_whenFactoryIsNull() {
        assertThrows(NullPointerException.class,
                () -> new AuthorService(null, authorRepository, policy, userRepository, countryRepository));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenRepositoryIsNull() {
        assertThrows(NullPointerException.class,
                () -> new AuthorService(authorFactory, null, policy, userRepository, countryRepository));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenPolicyIsNull() {
        assertThrows(NullPointerException.class,
                () -> new AuthorService(authorFactory, authorRepository, null, userRepository, countryRepository));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenUserRepositoryIsNull() {
        assertThrows(NullPointerException.class,
                () -> new AuthorService(authorFactory, authorRepository, policy, null, countryRepository));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenCountryRepositoryIsNull() {
        assertThrows(NullPointerException.class,
                () -> new AuthorService(authorFactory, authorRepository, policy, userRepository, null));
    }

    // =========================================================================
    // createAndSave — success
    // =========================================================================

    @Test
    void createAndSave_shouldReturnSavedAuthor() throws InstanceAlreadyExistsException, AccessDeniedException {

        // ARRANGE
        User user = user();
        Author author = author();
        Author savedAuthor = author();

        when(userRepository.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canCreateAuthor(user)).thenReturn(true);
        when(authorFactory.create("José Saramago", countryId(), dateOfBirth(), "Biography"))
                .thenReturn(author);
        when(authorRepository.save(author)).thenReturn(savedAuthor);

        // ACT
        Author result = authorService.createAndSave(
                "José Saramago", countryId(), dateOfBirth(), "Biography", userId());

        // ASSERT
        assertSame(savedAuthor, result);
    }

    @Test
    void createAndSave_shouldCallRepositorySave_whenUserIsAdmin() throws Exception {

        // ARRANGE
        User user = user();
        Author author = author();

        when(userRepository.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canCreateAuthor(user)).thenReturn(true);
        when(authorFactory.create("José Saramago", countryId(), dateOfBirth(), "Biography"))
                .thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);

        // ACT
        Author result = authorService.createAndSave(
                "José Saramago", countryId(), dateOfBirth(), "Biography", userId());

        // ASSERT
        assertSame(author, result);
    }

    // =========================================================================
    // createAndSave — access denied
    // =========================================================================

    @Test
    void createAndSave_shouldThrowAccessDenied_whenUserIsNotAdmin() {

        // ARRANGE
        User user = user();

        when(userRepository.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canCreateAuthor(user)).thenReturn(false);

        // ACT + ASSERT
        assertThrows(AccessDeniedException.class,
                () -> authorService.createAndSave(
                        "José Saramago", countryId(), dateOfBirth(), "Biography", userId()));
    }

    // =========================================================================
    // createAndSave — repository error
    // =========================================================================

    @Test
    void createAndSave_shouldPropagateException_whenRepositoryThrowsOnSave() throws Exception {

        // ARRANGE
        User user = user();
        Author author = author();

        when(userRepository.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canCreateAuthor(user)).thenReturn(true);
        when(authorFactory.create("José Saramago", countryId(), dateOfBirth(), "Biography"))
                .thenReturn(author);
        when(authorRepository.save(author)).thenThrow(new RuntimeException("DB error"));

        // ACT + ASSERT
        assertThrows(RuntimeException.class,
                () -> authorService.createAndSave(
                        "José Saramago", countryId(), dateOfBirth(), "Biography", userId()));
    }

    // =========================================================================
    // resolveCountry
    // =========================================================================

    @Test
    void resolveCountry_shouldReturnCountry_whenFound() {

        // ARRANGE
        Country countryDouble = mock(Country.class);
        when(countryRepository.findByID(countryId())).thenReturn(Optional.of(countryDouble));

        // ACT
        Country result = authorService.resolveCountry(countryId());

        // ASSERT
        assertSame(countryDouble, result);
    }

    @Test
    void resolveCountry_shouldThrowException_whenCountryNotFound() {

        // ARRANGE
        when(countryRepository.findByID(countryId())).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> authorService.resolveCountry(countryId()));
    }

    @Test
    void createAndSave_shouldThrowIllegalArgument_whenUserNotFound() {

        // ARRANGE
        when(userRepository.findByID(userId())).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> authorService.createAndSave(
                        "José Saramago", countryId(), dateOfBirth(), "Biography", userId()));
    }
}