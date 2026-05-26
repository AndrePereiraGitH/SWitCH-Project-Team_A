package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.GenreAuthorizationPolicy;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link GenreLinkProvider}.
 *
 * <p>Validates link generation based on user authorization rules.</p>
 */
class GenreLinkProviderTest {

    private final GenreAuthorizationPolicy policy = mock(GenreAuthorizationPolicy.class);
    private final IUserRepository repository = mock(IUserRepository.class);

    @Test
    void getLinks_shouldReturnLink_whenUserCanCreateGenre() {

        // ARRANGE
        GenreLinkProvider sut = new GenreLinkProvider(policy, repository);

        UserId userId = new UserId("user-1");
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));

        when(policy.canCreateGenre(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertEquals(1, result.size());
        assertEquals("create-genre", result.get(0).getRel().value());
    }

    @Test
    void getLinks_shouldReturnHref_whenUserCanCreateGenre() {

        // ARRANGE
        GenreLinkProvider sut = new GenreLinkProvider(policy, repository);

        UserId userId = new UserId("user-1");
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));

        when(policy.canCreateGenre(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertFalse(result.isEmpty());
        assertNotNull(result.get(0).getHref());
    }

    @Test
    void getLinks_shouldReturnEmptyList_whenUserCannotCreateGenre() {

        // ARRANGE
        GenreLinkProvider sut = new GenreLinkProvider(policy, repository);

        UserId userId = new UserId("user-1");
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateGenre(user)).thenReturn(false);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getLinks_shouldThrowException_whenUserDoesNotExist() {

        // ARRANGE
        GenreLinkProvider sut = new GenreLinkProvider(policy, repository);
        UserId userId = new UserId("user-1");

        when(repository.findByID(userId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        IllegalArgumentException ex =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> sut.getLinks(userId)
                );

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void getLinks_shouldReturnNonNullList_whenUserCannotCreateGenre() {

        // ARRANGE
        GenreLinkProvider sut =
                new GenreLinkProvider(policy, repository);

        UserId userId = new UserId("user-1");
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateGenre(user)).thenReturn(false);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertNotNull(result);
    }
}